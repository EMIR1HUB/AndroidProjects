package visitka.emir.chatgpt.responce

data class CreateImageRequest(
    val n: Int,
    val prompt: String,
    val size: String
)