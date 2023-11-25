package visitka.emir.chatgpt.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity()
data class Robot (
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "robotId")
    val robotId: String,
    val robotName: String,
    val robotImg: Int
)