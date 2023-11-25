package visitka.emir.chatgpt.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import visitka.emir.chatgpt.database.ChatGPTDatabase
import visitka.emir.chatgpt.models.Robot
import visitka.emir.chatgpt.utils.Resource
import visitka.emir.chatgpt.utils.Resource.*
import visitka.emir.chatgpt.utils.StatusResult

class RobotRepository(application: Application) {
    private val robotDao = ChatGPTDatabase.getInstance(application).robotDao

    private val _statusLiveData = MutableLiveData<Resource<StatusResult>>()
    val statusLiveData: LiveData<Resource<StatusResult>>
        get() = _statusLiveData

    fun insertRobot(robot: Robot){
        try{
            _statusLiveData.postValue(Loading())
            CoroutineScope(Dispatchers.IO).launch {
                val result = robotDao.insertRobot(robot)
                handleResult(result.toInt(), "Успешное добавление робота", StatusResult.Added)
            }
        }catch (e:Exception){
            _statusLiveData.postValue(Error(e.message.toString()))
        }
    }

    private fun handleResult(result: Int, message: String, statusResult: StatusResult) {
        if(result != -1){
            _statusLiveData.postValue(Success(statusResult, message))
        }else{
            _statusLiveData.postValue(Error("Что-то пошло не так"))
        }
    }
}