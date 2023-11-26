package visitka.emir.chatgpt.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import visitka.emir.chatgpt.database.ChatGPTDatabase
import visitka.emir.chatgpt.models.Robot
import visitka.emir.chatgpt.utils.Resource
import visitka.emir.chatgpt.utils.Resource.Error
import visitka.emir.chatgpt.utils.Resource.Loading
import visitka.emir.chatgpt.utils.Resource.Success
import visitka.emir.chatgpt.utils.StatusResult

class RobotRepository(application: Application) {
    private val robotDao = ChatGPTDatabase.getInstance(application).robotDao

    private val _robotStateFlow = MutableStateFlow<Resource<Flow<List<Robot>>>>(Loading())
    val robotStateFlow: StateFlow<Resource<Flow<List<Robot>>>>
        get() = _robotStateFlow

    private val _statusLiveData = MutableLiveData<Resource<StatusResult>?>()
    val statusLiveData: LiveData<Resource<StatusResult>?>
        get() = _statusLiveData

    fun clearStatusLiveDate(){
        _statusLiveData.value = null
    }

    fun getRobotList() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                _robotStateFlow.emit(Loading())
                val result = robotDao.getRobotList()
                _robotStateFlow.emit(Success(result))
            } catch (e: Exception) {
                e.printStackTrace()
                _robotStateFlow.emit(Error(e.message.toString()))
            }
        }
    }


    fun insertRobot(robot: Robot) {
        try {
            _statusLiveData.postValue(Loading())
            CoroutineScope(Dispatchers.IO).launch {
                val result = robotDao.insertRobot(robot)
                handleResult(result.toInt(), "Успешно добавлен робот", StatusResult.Added)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            _statusLiveData.postValue(Error(e.message.toString()))
        }
    }

    fun deleteRobotUsingId(robotId: String) {
        try {
            _statusLiveData.postValue(Loading())
            CoroutineScope(Dispatchers.IO).launch {
                async {
                    robotDao.deleteChatUsingRobotId(robotId)
                }.await()

                val result = async {
                    robotDao.deleteRobotUsingId(robotId)
                }.await()

                handleResult(result, "Успешно удален робот", StatusResult.Deleted)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            _statusLiveData.postValue(Error(e.message.toString()))
        }
    }

    fun updateRobot(robot: Robot) {
        try {
            _statusLiveData.postValue(Loading())
            CoroutineScope(Dispatchers.IO).launch {
                val result = robotDao.updateRobot(robot)
                handleResult(result, "Успешное обновление робота", StatusResult.Update)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            _statusLiveData.postValue(Error(e.message.toString()))
        }
    }

    private fun handleResult(result: Int, message: String, statusResult: StatusResult) {
        if (result != -1) {
            _statusLiveData.postValue(Success(statusResult, message))
        } else {
            _statusLiveData.postValue(Error("Что-то пошло не так"))
        }
    }
}