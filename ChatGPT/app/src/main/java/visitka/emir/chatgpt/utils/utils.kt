package visitka.emir.chatgpt.utils

import android.content.ClipData
import android.content.Intent
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.ClipboardManager
import android.view.View
import android.widget.Toast
import android.view.inputmethod.InputMethodManager
import visitka.emir.chatgpt.R

val robotImageList = listOf(
    R.drawable.robot_1,
    R.drawable.robot_2,
    R.drawable.robot_3,
    R.drawable.robot_4,
    R.drawable.robot_5,
    R.drawable.robot_6,
    R.drawable.robot_7,
    R.drawable.robot_8,
)
enum class Status{
    LOADING,
    SUCCESS,
    ERROR
}
enum class NetworkStatus{
    Available,Unavailable
}

enum class StatusResult{
    Added
}

fun View.visible(){
    visibility = View.VISIBLE
}

fun View.invisible(){
    visibility = View.INVISIBLE
}

fun View.gone(){
    visibility = View.GONE
}

fun Context.hideKeyBoard(it: View) {
    try {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(it.windowToken,0)
    }catch (e:Exception){
        e.printStackTrace()
    }
}

fun Context.longToastShow(message: String){
    Toast.makeText(this,message,Toast.LENGTH_LONG).show()
}

fun Context.copyToClipBoard(message: String) {
    val clipBoard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("Скопированный текст",message)
    clipBoard.setPrimaryClip(clip)
    longToastShow("Скопированно")
}

fun Context.shareMsg(message: String) {
    val intent = Intent(Intent.ACTION_SEND)
    intent.type = "text/plain"
    intent.putExtra(Intent.EXTRA_TEXT,message)
    startActivity(Intent.createChooser(intent,"Поделиться сообщением"))
}