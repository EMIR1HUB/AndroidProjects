package visitka.emir.chatgpt.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import visitka.emir.chatgpt.models.Chat
import visitka.emir.chatgpt.repository.ChatRepository

class ChatViewModel(application : Application) : AndroidViewModel(application) {

    private val chatRepository = ChatRepository(application)
    val chatStateFlow get() = chatRepository.chatStateFlow

    fun createChatCompletion(message: String) {
        chatRepository.createChatCompletion(message)
    }

    fun getChatList() {
        chatRepository.getChatList()
    }
}