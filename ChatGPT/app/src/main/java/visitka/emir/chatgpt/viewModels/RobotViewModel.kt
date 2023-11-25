package visitka.emir.chatgpt.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import visitka.emir.chatgpt.models.Robot
import visitka.emir.chatgpt.repository.RobotRepository

class RobotViewModel(application: Application) : AndroidViewModel(application) {

    private val robotrepository = RobotRepository(application)
    val statusLiveData get() = robotrepository.statusLiveData

    fun insertRobot(robot: Robot) {
        robotrepository.insertRobot(robot)
    }
}