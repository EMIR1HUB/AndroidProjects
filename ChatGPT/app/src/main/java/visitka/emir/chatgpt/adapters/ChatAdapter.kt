package visitka.emir.chatgpt.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import visitka.emir.chatgpt.databinding.ItemReceiverBinding
import visitka.emir.chatgpt.databinding.ItemSenderBinding
import visitka.emir.chatgpt.models.Chat
import visitka.emir.chatgpt.utils.gone
import visitka.emir.chatgpt.utils.hideKeyBoard
import visitka.emir.chatgpt.utils.visible
import java.text.SimpleDateFormat
import java.util.Locale

class ChatAdapter(
    private val onClickCallback: (message: String, view: View) -> Unit
) :
    ListAdapter<Chat, RecyclerView.ViewHolder>(DiffCallback()) {

    class SenderViewHolder(private val itemSenderBinding: ItemSenderBinding) :
        RecyclerView.ViewHolder(itemSenderBinding.root) {
        fun bind(chat: Chat) {
            itemSenderBinding.txtMessage.text = chat.message.content
            val dataFormat = SimpleDateFormat("dd-MMM-yyyy hh:mm a", Locale.getDefault())
            itemSenderBinding.txtDate.text = dataFormat.format(chat.date)
        }
    }

    class ReceiverViewHolder(private val itemReceiverBinding: ItemReceiverBinding) :
        RecyclerView.ViewHolder(itemReceiverBinding.root) {
        fun bind(chat: Chat) {
            if (chat.message.content.isNotEmpty()) {
                itemReceiverBinding.txtMessage.text = chat.message.content
                val dataFormat = SimpleDateFormat("dd-MMM-yyyy hh:mm a", Locale.getDefault())
                itemReceiverBinding.txtDate.text = dataFormat.format(chat.date)

                itemReceiverBinding.typingLAV.gone()
                itemReceiverBinding.txtDate.visible()
                itemReceiverBinding.txtMessage.visible()
            } else {
                itemReceiverBinding.txtDate.gone()
                itemReceiverBinding.txtMessage.gone()
                itemReceiverBinding.typingLAV.visible()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 1) { // Receiver Item
            ReceiverViewHolder(
                ItemReceiverBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else {  // Sender Item
            SenderViewHolder(
                ItemSenderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val chat = getItem(position)

        if (chat.message.role == "user") {
            (holder as SenderViewHolder).bind(chat)
        } else {
            (holder as ReceiverViewHolder).bind(chat)
        }

        holder.itemView.setOnLongClickListener{
            holder.itemView.context.hideKeyBoard(it)
            if(holder.adapterPosition != -1){
                onClickCallback(chat.message.content, holder.itemView)
            }
            true
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).message.role == "user") {
            0   // Sender Item
        } else {
            1   // Receiver Item
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Chat>() {
        override fun areItemsTheSame(oldItem: Chat, newItem: Chat): Boolean {
            return oldItem.chatId == newItem.chatId
        }

        override fun areContentsTheSame(oldItem: Chat, newItem: Chat): Boolean {
            return oldItem == newItem
        }
    }
}