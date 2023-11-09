package visitka.emir.mychatgpt

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import visitka.emir.mychatgpt.databinding.ActivityMainBinding
import visitka.emir.mychatgpt.ui.theme.MyChatGPTTheme

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.generateImage.setOnClickListener{
            startActivity(Intent(this, ImageGenerateActivity::class.java))
        }

        binding.generateImage.setOnClickListener{
            startActivity(Intent(this, ChatActivity::class.java))
        }
    }
}
