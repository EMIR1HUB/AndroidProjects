package visitka.emir.chatgpt.utils

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

class EncryptSharedPreferenceManager(context: Context) {

    private val mainKeyAlies = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    private val encryptedSharedPreferences = EncryptedSharedPreferences.create(
        "preferences",
        mainKeyAlies,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
    private val encryptedEditor = encryptedSharedPreferences.edit()


    private val keyAPIKey = "openAPIKey"

    // OPENAI_API_KEY - default api key set
    var openAPIKey
        get() = encryptedSharedPreferences.getString(keyAPIKey, OPENAI_API_KEY).toString()
        set(value) {
            encryptedEditor.putString(keyAPIKey,value)
            encryptedEditor.commit()
        }

}