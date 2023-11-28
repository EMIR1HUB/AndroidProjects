package visitka.emir.chatgpt.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import visitka.emir.chatgpt.responce.ChatRequest
import visitka.emir.chatgpt.responce.ChatResponse
import visitka.emir.chatgpt.responce.CreateImageRequest
import visitka.emir.chatgpt.responce.ImageResponse
import visitka.emir.chatgpt.utils.OPENAI_API_KEY

interface ApiInterface {

    @POST("chat/completions")
    fun createChatCompletion(
        @Body chatRequest : ChatRequest,
        @Header("Content-Type") contentType : String = "application/json",
        @Header("Authorization") authorization : String = "Bearer $OPENAI_API_KEY",
    ) : Call<ChatResponse>

    @POST("images/generations")
    fun createImage(
        @Body createImageRequest : CreateImageRequest,
        @Header("Content-Type") contentType : String = "application/json",
        @Header("Authorization") authorization : String = "Bearer $OPENAI_API_KEY",
    ) : Call<ImageResponse>
}