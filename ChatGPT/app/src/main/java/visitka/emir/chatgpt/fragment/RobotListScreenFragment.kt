package visitka.emir.chatgpt.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import visitka.emir.chatgpt.R


class RobotListScreenFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_robot_list_screen, container, false)

        val moveChatBtn = view.findViewById<Button>(R.id.moveChatBtn)

        moveChatBtn.setOnClickListener{
            val action =
                RobotListScreenFragmentDirections
                    .actionRobotListScreenFragmentToChatScreenFragment()
            findNavController().navigate(action)
        }

        return view
    }


}