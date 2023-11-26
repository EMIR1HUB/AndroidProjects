package visitka.emir.chatgpt.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import visitka.emir.chatgpt.repository.ChatRepository

class ChatViewModel(application : Application) : AndroidViewModel(application) {

    private val chatRepository = ChatRepository(application)
    val chatStateFlow get() = chatRepository.chatStateFlow

    fun createChatCompletion(message: String, robotId: String) {
        chatRepository.createChatCompletion(message, robotId)
    }

    fun getChatList(robotId: String) {
        chatRepository.getChatList(robotId)
    }
}