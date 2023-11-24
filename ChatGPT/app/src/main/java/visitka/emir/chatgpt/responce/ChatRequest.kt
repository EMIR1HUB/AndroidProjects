package visitka.emir.chatgpt.responce

data class ChatRequest(
    val messages: List<Message>,
    val model: String
)