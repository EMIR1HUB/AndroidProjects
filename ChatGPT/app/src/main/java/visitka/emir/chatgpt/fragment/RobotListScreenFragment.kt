package visitka.emir.chatgpt.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import visitka.emir.chatgpt.R
import visitka.emir.chatgpt.adapters.RobotAdapter
import visitka.emir.chatgpt.models.Robot
import visitka.emir.chatgpt.utils.EncryptSharedPreferenceManager
import visitka.emir.chatgpt.utils.Status
import visitka.emir.chatgpt.utils.StatusResult
import visitka.emir.chatgpt.utils.gone
import visitka.emir.chatgpt.utils.longToastShow
import visitka.emir.chatgpt.utils.robotImageList
import visitka.emir.chatgpt.utils.visible
import visitka.emir.chatgpt.viewModels.RobotViewModel
import java.util.UUID


class RobotListScreenFragment : Fragment() {

    private val robotViewModel : RobotViewModel by lazy{
        ViewModelProvider(this)[RobotViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_robot_list_screen, container, false)
        try {

            val toolbarView = view.findViewById<View>(R.id.toolbarlayout)

            val robotImageLL = toolbarView.findViewById<View>(R.id.robotImageLL)
            robotImageLL.gone()

            val closeImage = toolbarView.findViewById<ImageView>(R.id.backImg)
            closeImage.setOnClickListener {
                findNavController().navigateUp()
            }

            val titleTxt = toolbarView.findViewById<TextView>(R.id.titleTxt)
            titleTxt.text = "ChatGPT Mobile"
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {

            val settingImg =
                view.findViewById<ImageView>(R.id.settingImg)
            settingImg.visible()
            val encryptSharedPreferenceManager = EncryptSharedPreferenceManager(view.context)
            settingImg.setOnClickListener {
                apiKeyDialog(it, encryptSharedPreferenceManager)
            }

            val addRobotFabBtn =
                view.findViewById<ExtendedFloatingActionButton>(R.id.addRobotFabBtn)

            addRobotFabBtn.setOnClickListener {
                addRobotDialog(it)
            }

            val robotAdapter = RobotAdapter { type, position, robot ->
                when (type) {
                    "delete" -> {
                        robotViewModel.deleteRobotUsingId(robot.robotId)
                    }

                    "update" -> {
                        updateRobotDialog(view, robot)
                    }

                    else -> {
                        if (encryptSharedPreferenceManager.openAPIKey.trim().isNotEmpty()) {
                            val action =
                                RobotListScreenFragmentDirections
                                    .actionRobotListScreenFragmentToChatScreenFragment(
                                        robot.robotId,
                                        robot.robotImg,
                                        robot.robotName
                                    )
                            findNavController().navigate(action)
                        } else {
                            view.context.longToastShow("Введите Api ключ в настройках")
                        }
                    }
                }
            }
            val robotRv = view.findViewById<RecyclerView>(R.id.robotRV)
            robotRv.adapter = robotAdapter
            robotAdapter.registerAdapterDataObserver(object :
                RecyclerView.AdapterDataObserver() {
                override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                    super.onItemRangeInserted(positionStart, itemCount)
                    robotRv.smoothScrollToPosition(positionStart)
                }
            })
            callGetRobotList(robotAdapter, view)
            robotViewModel.getRobotList()
            statusCallback(view)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun callGetRobotList(robotAdapter: RobotAdapter, view: View) {
        CoroutineScope(Dispatchers.Main).launch {
            robotViewModel
                .robotStateFlow
                .collectLatest {
                    when (it.status) {
                        Status.LOADING -> {}
                        Status.SUCCESS -> {
                            it.data?.collect { robotList ->
                                robotAdapter.submitList(robotList)
                            }
                        }

                        Status.ERROR -> {
                            it.message?.let { it1 -> view.context.longToastShow(it1) }
                        }
                    }
                }
        }
    }

    override fun onDestroyView(){
        super.onDestroyView()
        robotViewModel.clearStatusLiveDate()
    }

    private fun statusCallback(view: View) {
        robotViewModel
            .statusLiveData
            .observe(viewLifecycleOwner) {
                if (it != null) {
                    when (it.status) {
                        Status.LOADING -> {}
                        Status.SUCCESS -> {
                            when (it.data as StatusResult) {
                                StatusResult.Added -> {
                                    Log.d("StatusResult", "Added")
                                }

                                StatusResult.Update -> {
                                    Log.d("StatusResult", "Update")
                                }

                                StatusResult.Deleted -> {
                                    Log.d("StatusResult", "Deleted")
                                }
                            }
                            it.message?.let { it1 -> view.context.longToastShow(it1) }
                        }

                        Status.ERROR -> {
                            it.message?.let { it1 -> view.context.longToastShow(it1) }
                        }
                    }
                }
            }
    }

    private fun updateRobotDialog(view: View, robot: Robot) {
        val edRobotName = TextInputEditText(view.context)
        edRobotName.hint = "Введите Имя Робота"
        edRobotName.maxLines = 3

        val textInputLayout = TextInputLayout(view.context)
        val container = FrameLayout(view.context)
        val params = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(50, 30, 50, 30)
        textInputLayout.layoutParams = params

        textInputLayout.addView(edRobotName)
        container.addView(textInputLayout)

        MaterialAlertDialogBuilder(view.context)
            .setTitle("Обновить робота")
            .setView(container)
            .setCancelable(false)
            .setPositiveButton("Обновить") { dialog, which ->
                val robotName = edRobotName.text.toString().trim()
                if (robotName.isNotEmpty()) {
                    robotViewModel.updateRobot(
                        Robot(
                            robot.robotId,
                            robotName,
                            robot.robotImg
                        )
                    )

                } else {
                    view.context.longToastShow("Обязательный")
                }
            }
            .setNegativeButton("Отменить", null)
            .create()
            .show()
        edRobotName.setText(robot.robotName)
    }

    private fun addRobotDialog(view: View) {
        val edRobotName = TextInputEditText(view.context)
        edRobotName.hint = "Введите Имя Робота"
        edRobotName.maxLines = 3

        val textInputLayout = TextInputLayout(view.context)
        val container = FrameLayout(view.context)
        val params = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(50, 30, 50, 30)
        textInputLayout.layoutParams = params

        textInputLayout.addView(edRobotName)
        container.addView(textInputLayout)

        MaterialAlertDialogBuilder(view.context)
            .setTitle("Добавить нового робота")
            .setView(container)
            .setCancelable(false)
            .setPositiveButton("Добавить"){dialog, which ->
                val robotName = edRobotName.text.toString().trim()
                if (robotName.isNotEmpty()) {
                    robotViewModel.insertRobot(
                        Robot(
                            UUID.randomUUID().toString(),
                            robotName,
                            (robotImageList.indices).random()
                        )
                    )

                } else {
                    view.context.longToastShow("Обязательный")
                }
            }
            .setNegativeButton("Отменить", null)
            .create()
            .show()

    }

    private fun apiKeyDialog( view: View, encryptSharedPreferenceManager: EncryptSharedPreferenceManager ) {
        val edApiKey = TextInputEditText(view.context)
        edApiKey.hint = "Введите Api Ключ"
        edApiKey.maxLines = 3

        val textInputLayout = TextInputLayout(view.context)
        val container = FrameLayout(view.context)
        val params = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(50, 30, 50, 30)
        textInputLayout.layoutParams = params

        textInputLayout.addView(edApiKey)
        container.addView(textInputLayout)

        MaterialAlertDialogBuilder(view.context)
            .setTitle("Open API Ключ")
            .setView(container)
            .setCancelable(false)
            .setPositiveButton("Обновить") { dialog, which ->
                val apiKey = edApiKey.text.toString().trim()
                if (apiKey.isNotEmpty()) {
                    encryptSharedPreferenceManager.openAPIKey = apiKey
                } else {
                    view.context.longToastShow("Обязательный")
                }
            }
            .setNegativeButton("Отменить", null)
            .create()
            .show()
        if (encryptSharedPreferenceManager.openAPIKey.trim().isNotEmpty()) {
            edApiKey.setText(encryptSharedPreferenceManager.openAPIKey.trim())
        }
    }
}