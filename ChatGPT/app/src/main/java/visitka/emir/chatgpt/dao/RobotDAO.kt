package visitka.emir.chatgpt.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import visitka.emir.chatgpt.models.Robot

@Dao
interface RobotDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRobot(robot: Robot): Long
}