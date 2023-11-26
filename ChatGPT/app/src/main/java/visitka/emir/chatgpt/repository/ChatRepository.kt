package visitka.emir.chatgpt.repository

import android.app.Application
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import visitka.emir.chatgpt.database.ChatGPTDatabase
import visitka.emir.chatgpt.models.Chat
import visitka.emir.chatgpt.network.ApiClient
import visitka.emir.chatgpt.responce.ChatRequest
import visitka.emir.chatgpt.responce.ChatResponse
import visitka.emir.chatgpt.responce.Message
import visitka.emir.chatgpt.utils.CHAT_GPT_MODEL
import visitka.emir.chatgpt.utils.Resource
import visitka.emir.chatgpt.utils.longToastShow
import java.util.Date
import java.util.UUID

class ChatRepository(val application: Application) {

    private val chatDao = ChatGPTDatabase.getInstance(application).chatDao
    private val apiClient = ApiClient.getInstance()

    private val _chatStateFlow = MutableStateFlow<Resource<Flow<List<Chat>>>>(Resource.Loading())
    val chatStateFlow: StateFlow<Resource<Flow<List<Chat>>>>
        get() = _chatStateFlow

    fun getChatList(robotId:String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                _chatStateFlow.emit(Resource.Loading())
                val result = async {
                    delay(300)
                    chatDao.getChatList(robotId)
                }.await()
                _chatStateFlow.emit(Resource.Success(result))
            } catch (e: Exception) {
                _chatStateFlow.emit(Resource.Error(e.message.toString()))
            }
        }
    }


    fun createChatCompletion(message: String, robotId:String) {
        val receiverId = UUID.randomUUID().toString()
        CoroutineScope(Dispatchers.IO).launch {
            delay(200)
            val senderId = UUID.randomUUID().toString()
            try {
                async {
                    chatDao.insertChat(
                        Chat(
                            senderId,
                            Message(
                                message,
                                "user"
                            ),
                            robotId,
                            Date()
                        )
                    )
                }.await()

                val messageList = chatDao.getChatListWitchOutFlow(robotId).map {
                    it.message
                }.reversed().toMutableList()

                if (messageList.size == 1) {
                    messageList.add(
                        0,
                        Message(
                            "You are a helpful assistant",
                            "system"
                        )
                    )
                }

                async {
                    chatDao.insertChat(
                        Chat(
                            receiverId,
                            Message(
                                "",
                                "assistant"
                            ),
                            robotId,
                            Date()
                        )
                    )
                }.await()

                val chatRequest = ChatRequest(
                    messageList,
                    CHAT_GPT_MODEL
                )
                apiClient.createChatCompletion(chatRequest)
                    .enqueue(object : Callback<ChatResponse> {
                        override fun onResponse(
                            call: Call<ChatResponse>,
                            response: Response<ChatResponse>,
                        ) {
                            CoroutineScope(Dispatchers.IO).launch {
                                val code = response.code()
                                if (code == 200) {
                                    response.body()?.choices?.get(0)?.message?.let {
                                        Log.d("message", it.toString())
                                        chatDao.updateChatPaticularField(
                                            receiverId,
                                            it.content,
                                            it.role,
                                            Date()
                                        )
                                    }
                                } else {
                                    Log.d("error", response.errorBody().toString())
                                    deleteChatIfApiFailure(receiverId, senderId)
                                }
                            }
                        }

                        override fun onFailure(call: Call<ChatResponse>, t: Throwable) {
                            t.printStackTrace()
                            deleteChatIfApiFailure(receiverId, senderId)
                        }

                    })

            } catch (e: Exception) {
                e.printStackTrace()
                deleteChatIfApiFailure(receiverId, senderId)
            }
        }
    }

    private fun deleteChatIfApiFailure(receiverId: String, senderId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            listOf(
                async { chatDao.deleteChatUsingChatId(receiverId) },
                async { chatDao.deleteChatUsingChatId(senderId) }
            ).awaitAll()
            withContext(Dispatchers.Main){
                application.longToastShow("Что-то пошло не так")
            }
//            _chatStateFlow.emit(Resource.Error("Что-то пошло не так"))
        }
    }
}