package visitka.emir.chatgpt.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import visitka.emir.chatgpt.models.Chat
import java.util.Date

@Dao
interface ChatDAO {

    @Query("SELECT * FROM Chat ORDER BY date DESC")
    fun getChatList(): Flow<List<Chat>>

    @Query("SELECT * FROM Chat ORDER BY date DESC LIMIT 5")
    fun getChatListWitchOutFlow(): List<Chat>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChat(chat: Chat): Long

    @Query("DELETE FROM Chat WHERE chatId == :chatId")
    suspend fun deleteChatUsingChatId(chatId: String): Int

    @Query("UPDATE CHAT SET content=:content, role=:role, date=:date WHERE chatId == :chatId")
    suspend fun updateChatPaticularField(
        chatId: String,
        content: String,
        role: String,
        date: Date
    )
}