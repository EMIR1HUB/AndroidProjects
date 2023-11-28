package visitka.emir.chatgpt.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import visitka.emir.chatgpt.repository.ChatRepository
import visitka.emir.chatgpt.responce.CreateImageRequest

class ChatViewModel(application : Application) : AndroidViewModel(application) {

    private val chatRepository = ChatRepository(application)
    val chatStateFlow get() = chatRepository.chatStateFlow
    val imageStateFlow get() = chatRepository.imageStateFlow

    fun createChatCompletion(message: String, robotId: String) {
        chatRepository.createChatCompletion(message, robotId)
    }
    fun createImage(body:CreateImageRequest) {
        chatRepository.createImage(body)
    }

    fun getChatList(robotId: String) {
        chatRepository.getChatList(robotId)
    }
}