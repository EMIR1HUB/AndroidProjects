package visitka.emir.chatgpt.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import visitka.emir.chatgpt.utils.BASE_URL

object ApiClient {

    @Volatile
    private var INSTACE : ApiInterface? = null

    fun getInstance() : ApiInterface{
        synchronized(this){
            return INSTACE?: Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiInterface::class.java)
                .also {
                    INSTACE = it
                }
        }
    }
}