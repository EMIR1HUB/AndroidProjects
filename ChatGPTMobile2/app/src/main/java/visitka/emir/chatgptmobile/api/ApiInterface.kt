package visitka.emir.chatgptmobile.api

import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import visitka.emir.chatgptmobile.chat.ChatModel
import visitka.emir.chatgptmobile.models.imageresponse.GenerateImageModel

interface ApiInterface {

    @POST("/v1/completions")
    suspend fun getChat(
        @Header("Content-Type") contentType: String,
        @Header("Authorization") authorization: String,
        @Body requestBody: RequestBody
    ) : ChatModel

    @POST("/v1/images/generations")
    suspend fun generateImage(
        @Header("Content-Type") contentType: String,
        @Header("Authorization") authorization: String,
        @Body requestBody: RequestBody
    ) : GenerateImageModel
}