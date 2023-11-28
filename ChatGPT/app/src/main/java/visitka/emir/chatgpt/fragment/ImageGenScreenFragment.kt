package visitka.emir.chatgpt.fragment

import android.app.Dialog
import android.app.DownloadManager
import android.content.Context
import android.content.Context.DOWNLOAD_SERVICE
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import visitka.emir.chatgpt.R
import visitka.emir.chatgpt.adapters.ImageAdapter
import visitka.emir.chatgpt.responce.CreateImageRequest
import visitka.emir.chatgpt.utils.EncryptSharedPreferenceManager
import visitka.emir.chatgpt.utils.Status
import visitka.emir.chatgpt.utils.appSettingOpen
import visitka.emir.chatgpt.utils.gone
import visitka.emir.chatgpt.utils.hideKeyBoard
import visitka.emir.chatgpt.utils.longToastShow
import visitka.emir.chatgpt.utils.setupDialog
import visitka.emir.chatgpt.utils.visible
import visitka.emir.chatgpt.viewModels.ChatViewModel
import java.io.File


class ImageGenScreenFragment : Fragment() {

    private val chatViewModel: ChatViewModel by lazy {
        ViewModelProvider(this)[ChatViewModel::class.java]
    }

    private val viewImageDialog: Dialog by lazy {
        Dialog(requireActivity(), R.style.DialogCustomTheme).apply {
            setupDialog(R.layout.view_image_dialog)
        }
    }
    private val multiplePermissionNameList = if (Build.VERSION.SDK_INT >= 33) {
        arrayListOf()
    } else {
        arrayListOf(
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val settingImg =
            view.findViewById<ImageView>(R.id.settingImg)
        settingImg.visible()
        val encryptSharedPreferenceManager = EncryptSharedPreferenceManager(view.context)
        settingImg.setOnClickListener {
            apiKeyDialog(it, encryptSharedPreferenceManager)
        }

        val imageRV = view.findViewById<RecyclerView>(R.id.imageRv)
        val loadingPB = view.findViewById<ProgressBar>(R.id.loadingPB)
        val downloadAllBtn = view.findViewById<Button>(R.id.downloadAllBtn)

        val loadImage = viewImageDialog.findViewById<ImageView>(R.id.loadImage)
        val cancelBtn = viewImageDialog.findViewById<Button>(R.id.cancelBtn)
        val downloadBtn = viewImageDialog.findViewById<Button>(R.id.downloadBtn)

        cancelBtn.setOnClickListener {
            viewImageDialog.dismiss()
        }

        val imageAdapter = ImageAdapter { position, data ->
            viewImageDialog.show()
            Glide.with(loadImage)
                .load(data.url)
                .placeholder(R.drawable.ic_placeholder)
                .into(loadImage)
            downloadBtn.setOnClickListener {
                if (it.context.checkMultiplePermission()) {
                    it.context.download(data.url)
                } else {
                    appSettingOpen(it.context)
                }
            }
        }

        imageRV.adapter = imageAdapter
        downloadAllBtn.setOnClickListener {
            if (it.context.checkMultiplePermission()) {
                imageAdapter.currentList.map { list ->
                    it.context.download(list.url)
                }
            } else {
                appSettingOpen(it.context)
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            chatViewModel.imageStateFlow.collect {
                when (it.status) {
                    Status.LOADING -> {
                        withContext(Dispatchers.Main) {
                            loadingPB.visible()
                        }
                    }

                    Status.SUCCESS -> {
                        withContext(Dispatchers.Main) {
                            loadingPB.gone()

                            imageAdapter.submitList(
                                it.data?.data
                            )
                            if (imageAdapter.currentList.isNotEmpty()) {
                                downloadAllBtn.visible()
                            } else {
                                downloadAllBtn.gone()
                            }
                        }
                    }

                    Status.ERROR -> {
                        withContext(Dispatchers.Main) {
                            loadingPB.gone()
                            it.message?.let { it1 -> view.context.longToastShow(it1) }
                        }
                    }
                }
            }
        }
    }

    private fun Context.checkMultiplePermission(): Boolean {
        val listPermissionNeeded = arrayListOf<String>()
        for (permission in multiplePermissionNameList) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                listPermissionNeeded.add(permission)
            }
        }
        if (listPermissionNeeded.isNotEmpty()) {
            return false
        }
        return true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_image_gen_screen, container, false)
        val toolbarView = view.findViewById<View>(R.id.toolbarlayout)

        val robotImageLL = toolbarView.findViewById<View>(R.id.robotImageLL)
        robotImageLL.gone()

        val closeImage = toolbarView.findViewById<ImageView>(R.id.backImg)
        closeImage.setOnClickListener {
            findNavController().navigateUp()
        }

        val titleTxt = toolbarView.findViewById<TextView>(R.id.titleTxt)
        titleTxt.text = "Image Generate"


        val numberListACT = view.findViewById<AutoCompleteTextView>(R.id.numberListACT)
        numberListACT.setAdapter(
            ArrayAdapter(
                view.context,
                android.R.layout.simple_list_item_1,
                (1..4).toList()
            )
        )

        val imageSizeRG = view.findViewById<RadioGroup>(R.id.imageSizeRG)
        val edPrompt = view.findViewById<EditText>(R.id.edPrompt)


        val generateBtn = view.findViewById<Button>(R.id.generateBtn)
        generateBtn.setOnClickListener {
            view.context.hideKeyBoard(it)
            if (edPrompt.text.toString().trim().isNotEmpty()) {
                if (edPrompt.text.toString().trim().length < 1000) {
                    Log.d("Prompt", edPrompt.text.toString().trim())
                    Log.d("numberListACT", numberListACT.text.toString().trim())

                    val selectedSizeRB =
                        view.findViewById<RadioButton>(imageSizeRG.checkedRadioButtonId)
                    Log.d("selectedSizeRB", selectedSizeRB.text.toString().trim())

                    chatViewModel.createImage(
                        CreateImageRequest(
                            numberListACT.text.toString().toInt(),
                            edPrompt.text.toString().trim(),
                            selectedSizeRB.text.toString().trim()
                        )
                    )
//                edPrompt.text = null
                } else {
                    view.context.longToastShow("Максимальное длина - 1000 символов")
                }
            } else {
                view.context.longToastShow("Требуется запрос")
            }
        }

        return view
    }

    private fun apiKeyDialog(
        view: View,
        encryptSharedPreferenceManager: EncryptSharedPreferenceManager
    ) {
        val edApiKey = TextInputEditText(view.context)
        edApiKey.hint = "Введите Api Ключ"
        edApiKey.maxLines = 3

        val textInputLayout = TextInputLayout(view.context)
        val container = FrameLayout(view.context)
        val params = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(50, 30, 50, 30)
        textInputLayout.layoutParams = params

        textInputLayout.addView(edApiKey)
        container.addView(textInputLayout)

        MaterialAlertDialogBuilder(view.context)
            .setTitle("Open API Ключ")
            .setView(container)
            .setCancelable(false)
            .setPositiveButton("Обновить") { dialog, which ->
                val apiKey = edApiKey.text.toString().trim()
                if (apiKey.isNotEmpty()) {
                    encryptSharedPreferenceManager.openAPIKey = apiKey
                } else {
                    view.context.longToastShow("Обязательный")
                }
            }
            .setNegativeButton("Отменить", null)
            .create()
            .show()
        if (encryptSharedPreferenceManager.openAPIKey.trim().isNotEmpty()) {
            edApiKey.setText(encryptSharedPreferenceManager.openAPIKey.trim())
        }
    }

    private fun getRandomString(): String {
        val allowedChar = ('A'..'Z') + ('a'..'z') + ('0'..'9')

        return (1..7)
            .map { allowedChar.random() }
            .joinToString("")
    }

    private fun Context.download(url: String) {
        val folder = File(
            Environment.getExternalStorageDirectory().toString() + "/Download/Image"
        )
        if (!folder.exists()) {
            folder.mkdirs()
        }
        longToastShow("Начало загрузки")
        val fileName = getRandomString() + ".jpg"

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        val request = DownloadManager.Request(Uri.parse(url))
        request.setAllowedNetworkTypes(
            DownloadManager.Request.NETWORK_WIFI or
                    DownloadManager.Request.NETWORK_MOBILE
        )
        request.setTitle(fileName)
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(
            Environment.DIRECTORY_DOWNLOADS,
            "Image/$fileName"
        )
        downloadManager.enqueue(request)

    }
}