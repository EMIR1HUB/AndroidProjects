package visitka.emir.chatgpt.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import visitka.emir.chatgpt.models.Robot

@Dao
interface RobotDAO {

    @Query("SELECT * FROM Robot")
    fun getRobotList(): Flow<List<Robot>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRobot(robot: Robot): Long

    @Update()
    suspend fun updateRobot(robot: Robot): Int

    @Query("DELETE FROM Robot WHERE robotId == :robotId")
    suspend fun deleteRobotUsingId(robotId: String): Int


    // удалить все сообщения чата, связанные с определенным идентификатором robotId
    @Query("DELETE FROM Chat WHERE robotId == :robotId")
    suspend fun deleteChatUsingRobotId(robotId: String)


}