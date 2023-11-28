package visitka.emir.chatgpt

import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.android.material.snackbar.Snackbar
import visitka.emir.chatgpt.utils.NetworkConnectivityObserver
import visitka.emir.chatgpt.utils.NetworkStatus
import visitka.emir.chatgpt.utils.appSettingOpen
import visitka.emir.chatgpt.utils.longToastShow
import visitka.emir.chatgpt.utils.warningPermissionDialog


class MainActivity : AppCompatActivity() {
    private val networkConnectivityObserver: NetworkConnectivityObserver by lazy {
        NetworkConnectivityObserver(this)
    }
    private val multiplePermissionId = 14
    private val multiplePermissionNameList = if (Build.VERSION.SDK_INT >= 33) {
        arrayListOf()
    } else {
        arrayListOf(
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        )
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
            "Нет интернет соединения",
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
        checkMultiplePermission()
    }

    private fun checkMultiplePermission(): Boolean {
        val listPermissionNeeded = arrayListOf<String>()
        for (permission in multiplePermissionNameList) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                listPermissionNeeded.add(permission)
            }
        }
        if (listPermissionNeeded.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                listPermissionNeeded.toTypedArray(),
                multiplePermissionId
            )
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == multiplePermissionId) {
            if (grantResults.isNotEmpty()) {
                var isGrant = true
                for (element in grantResults) {
                    if (element == PackageManager.PERMISSION_DENIED) {
                        isGrant = false
                    }
                }
                if (isGrant) {
                    // here all permission granted successfully
                    longToastShow("все разрешения успешно получены")
                } else {
                    var someDenied = false
                    for (permission in permissions) {
                        if (!ActivityCompat.shouldShowRequestPermissionRationale(
                                this,
                                permission
                            )
                        ) {
                            if (ActivityCompat.checkSelfPermission(
                                    this,
                                    permission
                                ) == PackageManager.PERMISSION_DENIED
                            ) {
                                someDenied = true
                            }
                        }
                    }
                    if (someDenied) {
                        // here app Setting open because all permission is not granted
                        // and permanent denied
                        appSettingOpen(this)
                    } else {
                        // here warning permission show
                        warningPermissionDialog(this) { _: DialogInterface, which: Int ->
                            when (which) {
                                DialogInterface.BUTTON_POSITIVE ->
                                    checkMultiplePermission()
                            }
                        }
                    }
                }
            }
        }
    }
}