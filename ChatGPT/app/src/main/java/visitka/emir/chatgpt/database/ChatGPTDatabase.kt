package visitka.emir.chatgpt.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import visitka.emir.chatgpt.converters.TypeConverter
import visitka.emir.chatgpt.dao.ChatDAO
import visitka.emir.chatgpt.dao.RobotDAO
import visitka.emir.chatgpt.models.Chat
import visitka.emir.chatgpt.models.Robot

@Database(
    entities = [Chat::class, Robot::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(TypeConverter::class)
abstract class ChatGPTDatabase : RoomDatabase() {
    abstract val chatDao: ChatDAO
    abstract val robotDao: RobotDAO

    companion object{
        @Volatile
        private var INSTANCE: ChatGPTDatabase? = null

        fun getInstance(context: Context) : ChatGPTDatabase{
            synchronized(this){
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    ChatGPTDatabase::class.java,
                    "chat_gpt_db"
                ).build()
                    .also {
                        INSTANCE = it
                    }
            }
        }
    }
}