package visitka.emir.chatgpt.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import visitka.emir.chatgpt.databinding.ViewRobotLayoutBinding
import visitka.emir.chatgpt.models.Robot
import visitka.emir.chatgpt.utils.robotImageList


class RobotAdapter(
    private val onClickDeleteUpdateCallback: (type: String, position: Int, robot: Robot) -> Unit
) : ListAdapter<Robot, RobotAdapter.ViewHolder>(DiffCallback()) {
    class DiffCallback : DiffUtil.ItemCallback<Robot>() {
        override fun areItemsTheSame(oldItem: Robot, newItem: Robot): Boolean {
            return oldItem.robotId == newItem.robotId
        }

        override fun areContentsTheSame(oldItem: Robot, newItem: Robot): Boolean {
            return oldItem == newItem
        }
    }

    class ViewHolder(val viewRobotLayoutBinding: ViewRobotLayoutBinding) :
        RecyclerView.ViewHolder(viewRobotLayoutBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RobotAdapter.ViewHolder {
        return ViewHolder(
            ViewRobotLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val robot = getItem(position)
        holder.viewRobotLayoutBinding.robotNameTxt.text = robot.robotName
        holder.viewRobotLayoutBinding.robotImage.setImageResource(robotImageList[robot.robotImg])

        holder.itemView.setOnClickListener {
            if (holder.adapterPosition != -1) {
                onClickDeleteUpdateCallback("click", holder.adapterPosition, robot)
            }
        }
        holder.viewRobotLayoutBinding.deleteImage.setOnClickListener {
            if (holder.adapterPosition != -1) {
                onClickDeleteUpdateCallback("delete", holder.adapterPosition, robot)
            }
        }
        holder.viewRobotLayoutBinding.editImage.setOnClickListener {
            if (holder.adapterPosition != -1) {
                onClickDeleteUpdateCallback("update", holder.adapterPosition, robot)
            }
        }
    }
}