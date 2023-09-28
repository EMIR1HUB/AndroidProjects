package visitka.emir.firstapp

import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.activity.ComponentActivity


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val developerPhoto = findViewById<ImageView>(R.id.developerPhoto)
        val fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        developerPhoto.startAnimation(fadeInAnimation)
    }
}

