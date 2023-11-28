package visitka.emir.chatgpt.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import visitka.emir.chatgpt.R
import visitka.emir.chatgpt.utils.EncryptSharedPreferenceManager
import visitka.emir.chatgpt.utils.longToastShow


class WelcomeScreenFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_welcome_screen, container, false)

        val chatBtn = view.findViewById<Button>(R.id.chatBtn).apply {
            startAnimation(
                AnimationUtils.loadAnimation(view.context, R.anim.zoom_in_cut)
            )
        }
        val generateImgBtn = view.findViewById<Button>(R.id.generateImgBtn).apply {
            startAnimation(
                AnimationUtils.loadAnimation(view.context, R.anim.zoom_in_cut)
            )
        }

        chatBtn.setOnClickListener{
            val action =
                WelcomeScreenFragmentDirections
                    .actionWelcomeScreenFragmentToRobotListScreenFragment()
            findNavController().navigate(action)
        }

        val encryptSharedPreferenceManager = EncryptSharedPreferenceManager(view.context)

        generateImgBtn.setOnClickListener{
            if (encryptSharedPreferenceManager.openAPIKey.trim().isNotEmpty()) {
                val action =
                    WelcomeScreenFragmentDirections
                        .actionWelcomeScreenFragmentToImageGenScreenFragment()
                findNavController().navigate(action)
            }else{
                view.context.longToastShow("Введите Api ключ в настройках")
            }
        }

        return view
    }


}