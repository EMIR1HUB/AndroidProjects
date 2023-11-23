package visitka.emir.chatgpt

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.android.material.snackbar.Snackbar
import visitka.emir.chatgpt.utils.NetworkConnectivityObserver
import visitka.emir.chatgpt.utils.NetworkStatus


class MainActivity : AppCompatActivity() {

    private val networkConnectivityObserver: NetworkConnectivityObserver by lazy {
        NetworkConnectivityObserver(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // здесь setKeepOnScreenCondition false, поэтому действие перенаправляет другое действие
        // и здесь некоторый вызов API
        // если setKeepOnScreenCondition истинно, код действия не перенаправляет другое действие
        splashScreen.setKeepOnScreenCondition { false }
//        Handler(Looper.getMainLooper()).postDelayed({
//            startActivity(Intent(this, SecondActivity::class.java))
//            finish()
//        }, 2000)

        val snackbar = Snackbar.make(
            findViewById(android.R.id.content),
            "No Internet Connection",
            Snackbar.LENGTH_INDEFINITE
        ).setAction("Wifi") {
            startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
        }

        networkConnectivityObserver.observe(this) {
            when (it) {
                NetworkStatus.Available -> {
                    if (snackbar.isShown) {
                        snackbar.dismiss()
                    }
                }

                else -> {
                    snackbar.show()
                }
            }
        }

    }
}