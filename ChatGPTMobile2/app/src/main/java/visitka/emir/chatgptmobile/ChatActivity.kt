package visitka.emir.chatgptmobile

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.RequestBody
import visitka.emir.chatgptmobile.adapter.MessageAdapter
import visitka.emir.chatgptmobile.api.ApiUtilities
import visitka.emir.chatgptmobile.databinding.ActivityChatBinding
import visitka.emir.chatgptmobile.models.MessageModel
import visitka.emir.chatgptmobile.models.request.ChatRequest

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding

    var list = ArrayList<MessageModel>()
    private lateinit var mLayoutManager: LinearLayoutManager
    private lateinit var adapter: MessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener { finish() }


        mLayoutManager = LinearLayoutManager(this)
        mLayoutManager.stackFromEnd = true
        adapter = MessageAdapter(list)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = mLayoutManager

        binding.sendbtn.setOnClickListener {
            if (binding.userMsg.text!!.isEmpty()) {
                Toast.makeText(this, "Задайте свой вопрос", Toast.LENGTH_SHORT).show()
            } else {
                callApi()
            }
        }
    }

    private fun callApi() {
        list.add(MessageModel(true, false, binding.userMsg.text.toString()))

        adapter.notifyItemInserted(list.size - 1)

        binding.recyclerView.recycledViewPool.clear()
        binding.recyclerView.smoothScrollToPosition(list.size - 1)

        val apiInterface = ApiUtilities.getApiInterface()
        val requestBody = RequestBody.create(
            MediaType.parse("application/json"),
            Gson().toJson(
                ChatRequest(
                    250,
                    "text-davinci-003",
                    binding.userMsg.text.toString(),
                    0.7
                )
            )
        )
        // "gpt-3.5-turbo-instruct"

        val contentType = "application/json"
        val authorization = "Bearer ${Utils.API_KEY}"

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = apiInterface.getChat(
                    contentType, authorization, requestBody
                )

                val textResponse = response.choices.first().text

                list.add(MessageModel(false, false, textResponse))
                withContext(Dispatchers.Main) {
                    adapter.notifyItemInserted(list.size - 1)

                    binding.recyclerView.recycledViewPool.clear()
                    binding.recyclerView.smoothScrollToPosition(list.size - 1)
                }

                binding.userMsg.text!!.clear()
            }catch (e: Exception){
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ChatActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}