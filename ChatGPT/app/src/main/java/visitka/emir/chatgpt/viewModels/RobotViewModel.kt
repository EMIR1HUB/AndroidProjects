package visitka.emir.chatgpt.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import visitka.emir.chatgpt.models.Robot
import visitka.emir.chatgpt.repository.RobotRepository

class RobotViewModel(application: Application) : AndroidViewModel(application) {

    private val robotRepository = RobotRepository(application)
    val robotStateFlow get() = robotRepository.robotStateFlow
    val statusLiveData get() = robotRepository.statusLiveData

    fun getRobotList() {
        robotRepository.getRobotList()
    }

    fun clearStatusLiveDate(){
        robotRepository.clearStatusLiveDate()
    }

    fun insertRobot(robot: Robot) {
        robotRepository.insertRobot(robot)
    }

    fun deleteRobotUsingId(robotId: String) {
        robotRepository.deleteRobotUsingId(robotId)
    }

    fun updateRobot(robot: Robot) {
        robotRepository.updateRobot(robot)
    }
}