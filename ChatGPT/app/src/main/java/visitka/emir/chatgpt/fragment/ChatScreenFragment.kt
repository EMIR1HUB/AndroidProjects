package visitka.emir.chatgpt.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import visitka.emir.chatgpt.R
import visitka.emir.chatgpt.adapters.ChatAdapter
import visitka.emir.chatgpt.utils.Status
import visitka.emir.chatgpt.utils.copyToClipBoard
import visitka.emir.chatgpt.utils.hideKeyBoard
import visitka.emir.chatgpt.utils.longToastShow
import visitka.emir.chatgpt.utils.robotImageList
import visitka.emir.chatgpt.utils.shareMsg
import visitka.emir.chatgpt.viewModels.ChatViewModel
import java.util.Locale


class ChatScreenFragment : Fragment() {


    private val chatViewModel: ChatViewModel by lazy {
        ViewModelProvider(this)[ChatViewModel::class.java]
    }

    private val chatArgs: ChatScreenFragmentArgs by navArgs()
    private lateinit var textToSpeech: TextToSpeech
    private lateinit var edMessage: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_chat_screen, container, false)

        val toolbarView = view.findViewById<View>(R.id.toolbarlayout)

        val closeImage = toolbarView.findViewById<ImageView>(R.id.backImg)
        val robotImage = toolbarView.findViewById<ImageView>(R.id.robotImage)
        robotImage.setImageResource(robotImageList[chatArgs.robotImg])

        closeImage.setOnClickListener {
            findNavController().navigateUp()
        }

        textToSpeech = TextToSpeech(view.context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = textToSpeech.setLanguage(Locale.getDefault())
                if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED
                ) {
                    view.context.longToastShow("язык не поддерживается")
                }
            }
        }
        val titleTxt = toolbarView.findViewById<TextView>(R.id.titleTxt)
        titleTxt.text = chatArgs.robotName


        val chatRV = view.findViewById<RecyclerView>(R.id.chatRV)
        val chatAdapter = ChatAdapter() { message, textView ->
            val popup = PopupMenu(context, textView)
            try {
                val fields = popup.javaClass.declaredFields
                for (field in fields) {
                    if ("mPopup" == field.name) {
                        field.isAccessible = true
                        val menuPopupHelper = field.get(popup)
                        val classPopupHelper = Class.forName(menuPopupHelper.javaClass.name)
                        val setForceIcons = classPopupHelper.getMethod(
                            "setForceShowIcon",
                            Boolean::class.javaPrimitiveType
                        )
                        setForceIcons.invoke(menuPopupHelper, true)
                        break
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            popup.menuInflater.inflate(R.menu.option_menu, popup.menu)

            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.copyMenu -> {
                        textToSpeech.stop()
                        view.context.copyToClipBoard(message)
                        return@setOnMenuItemClickListener true
                    }

                    R.id.selectTxtMenu -> {
                        textToSpeech.stop()
                        val action = ChatScreenFragmentDirections.actionChatScreenFragmentToSelectTextScreenFragment(
                            message
                        )
                        findNavController().navigate(action)
                        return@setOnMenuItemClickListener true
                    }

                    R.id.textToVoiceMenu -> {
                        textToSpeech.speak(
                            message,
                            TextToSpeech.QUEUE_FLUSH,
                            null,
                            null
                        )
                        return@setOnMenuItemClickListener true
                    }

                    R.id.shareTxtMenu -> {
                        textToSpeech.stop()
                        view.context.shareMsg(message)
                        return@setOnMenuItemClickListener true
                    }

                    else -> {
                        return@setOnMenuItemClickListener true
                    }
                }
            }
            popup.show()
        }
        chatRV.adapter = chatAdapter
        chatAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                chatRV.smoothScrollToPosition(positionStart)
            }
        })

        val sendImageBtn = view.findViewById<ImageButton>(R.id.sendImageBtn)
        edMessage = view.findViewById(R.id.edMessage)

        sendImageBtn.setOnClickListener {
            textToSpeech.stop()
            view.context.hideKeyBoard(it)
            if (edMessage.text.toString().trim().isNotEmpty()) {
                chatViewModel.createChatCompletion(edMessage.text.toString().trim(), chatArgs.robotId)
                edMessage.text = null
            } else {
                view.context.longToastShow("Требуется текст")
            }
        }

        val voiceToTextBtn = view.findViewById<ImageButton>(R.id.voiceToTextBtn)
        voiceToTextBtn.setOnClickListener {
            textToSpeech.stop()
            edMessage.text = null
            try {
                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                intent.putExtra(
                    RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                )
                intent.putExtra(
                    RecognizerIntent.EXTRA_LANGUAGE,
                    Locale.getDefault()
                )
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Скажи что-нибудь")
                result.launch(intent)
            }catch (e:Exception){
                e.printStackTrace()
            }
        }

        callGetChatList(chatRV, chatAdapter)
        chatViewModel.getChatList(chatArgs.robotId)

        return view
    }

    override fun onDestroyView(){
        super.onDestroyView()
        textToSpeech.stop()
    }

    private val result = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result->
        if (result.resultCode == Activity.RESULT_OK){
            val results = result.data?.getStringArrayListExtra(
                RecognizerIntent.EXTRA_RESULTS
            ) as ArrayList<String>

            edMessage.setText(results[0])
        }
    }

    private fun callGetChatList(chatRV: RecyclerView, chatAdapter: ChatAdapter) {
        CoroutineScope(Dispatchers.Main).launch {
            chatViewModel
                .chatStateFlow
                .collectLatest {
                    when(it.status){
                        Status.LOADING -> {}
                        Status.SUCCESS -> {
                            it.data?.collect{ chatList ->
                                chatAdapter.submitList(chatList)
                            }
                        }
                        Status.ERROR -> {
                            it.message?.let { it1 -> chatRV.context.longToastShow(it1) }
                        }
                    }
                }
        }
    }
}