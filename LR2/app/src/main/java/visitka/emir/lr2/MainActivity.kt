package visitka.emir.lr2


import androidx.activity.ComponentActivity
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat


class MainActivity : ComponentActivity() {

    private lateinit var scrollView: ScrollView
    private lateinit var chatLayout: LinearLayout
    private lateinit var editText: EditText
    private lateinit var sendButton: Button
    private lateinit var moodSpinner: Spinner
    private lateinit var moodImageView: ImageView // Добавлен ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        scrollView = findViewById(R.id.scrollView)
        chatLayout = findViewById(R.id.chatLayout)
        editText = findViewById(R.id.editText)
        sendButton = findViewById(R.id.sendButton)
        moodSpinner = findViewById(R.id.moodSpinner)
        moodImageView = findViewById(R.id.moodImageView) // Инициализация ImageView

        val moods = resources.getStringArray(R.array.moods)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, moods)
        moodSpinner.adapter = adapter

        sendButton.setOnClickListener {
            sendMessage()
        }
    }

    private fun sendMessage() {
        val messageText = editText.text.toString()
        val mood = moodSpinner.selectedItem.toString()

        if (messageText.isNotEmpty()) {
            displayMessage("Вы: $messageText", mood)
            editText.text.clear()
        }
    }

    private fun displayMessage(message: String, mood: String) {
        val messageLayout = LinearLayout(this)
        messageLayout.orientation = LinearLayout.HORIZONTAL

        val messageTextView = TextView(this)
        messageTextView.text = message
        messageLayout.addView(messageTextView)

        val moodImageView = ImageView(this)
        val drawableId: Int = when (mood) {
            "Хорошее" -> R.drawable.happy
            "Плохое" -> R.drawable.sad
            "Нейтральное" -> R.drawable.normal
            else -> 0
        }

        if (drawableId != 0) {
            moodImageView.setImageResource(drawableId)
            moodImageView.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            messageLayout.addView(moodImageView)
        }

        chatLayout.addView(messageLayout)
        scrollView.post {
            scrollView.fullScroll(View.FOCUS_DOWN)
        }
    }

    private fun displayMoodImage(mood: String) {
        val drawableId: Int = when (mood) {
            "Хорошее" -> R.drawable.happy
            "Плохое" -> R.drawable.sad
            "Нейтральное" -> R.drawable.normal
            else -> 0 // Заглушка, если настроение не определено
        }

        if (drawableId != 0) {
            moodImageView.setImageResource(drawableId) // Установка изображения в ImageView
        } else {
            moodImageView.setImageResource(0) // Сброс изображения, если настроение не определено
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val mood = moodSpinner.selectedItemPosition
        outState.putInt("moodPosition", mood)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val moodPosition = savedInstanceState.getInt("moodPosition", 0)
        moodSpinner.setSelection(moodPosition)
    }
}
