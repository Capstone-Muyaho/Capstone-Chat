package com.example.capstone_frontend

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import java.util.*

class ChatAdapter(private val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var items = ArrayList<ChatItem>()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val view: View
        val context = parent.context
        val inflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return if (viewType == ChatType.CENTER_MESSAGE) {
            view = inflater.inflate(R.layout.chat_center_item, parent, false)
            CenterViewHolder(view)
        } else if (viewType == ChatType.LEFT_MESSAGE) {
            view = inflater.inflate(R.layout.chat_left_item, parent, false)
            LeftViewHolder(view)
        } else if (viewType == ChatType.RIGHT_MESSAGE) {
            view = inflater.inflate(R.layout.chat_right_item, parent, false)
            RightViewHolder(view)
        } else if (viewType == ChatType.LEFT_IMAGE) {
            view = inflater.inflate(R.layout.chat_left_image, parent, false)
            LeftImageViewHolder(view)
        } else {
            view = inflater.inflate(R.layout.chat_right_image, parent, false)
            RightImageViewHolder(view)
        }
    }

    override fun onBindViewHolder(
        viewHolder: RecyclerView.ViewHolder,
        position: Int
    ) {
        if (viewHolder is CenterViewHolder) {
            val item = items[position]
            viewHolder.setItem(item)
        } else if (viewHolder is LeftViewHolder) {
            val item = items[position]
            viewHolder.setItem(item)
        } else if (viewHolder is RightViewHolder) {
            val item = items[position]
            viewHolder.setItem(item)
        } else if (viewHolder is LeftImageViewHolder) {
            val item = items[position]
            viewHolder.setItem(item, context)
        } else {
            val item = items[position]
            (viewHolder as RightImageViewHolder).setItem(item, context)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun addItem(item: ChatItem) {
        items.add(item)
        notifyDataSetChanged()
    }

    fun setItems(items: ArrayList<ChatItem>) {
        this.items = items
    }

    fun getItem(position: Int): ChatItem {
        return items[position]
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].viewType
    }

    inner class CenterViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var contentText: TextView
        fun setItem(item: ChatItem) {
            contentText.text = item.content
        }

        init {
            contentText = itemView.findViewById(R.id.content_text)
        }
    }

    inner class LeftViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var nameText: TextView
        var contentText: TextView
        var sendTimeText: TextView
        fun setItem(item: ChatItem) {
            nameText.text = item.name
            contentText.text = item.content
            sendTimeText.text = item.sendTime
        }

        init {
            nameText = itemView.findViewById(R.id.name_text)
            contentText = itemView.findViewById(R.id.msg_text)
            sendTimeText = itemView.findViewById(R.id.send_time_text)
        }
    }

    inner class RightViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var contentText: TextView
        var sendTimeText: TextView
        fun setItem(item: ChatItem) {
            contentText.text = item.content
            sendTimeText.text = item.sendTime
        }

        init {
            contentText = itemView.findViewById(R.id.msg_text)
            sendTimeText = itemView.findViewById(R.id.send_time_text)
        }
    }

    inner class LeftImageViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var nameText: TextView
        var image: ImageView
        var sendTimeText: TextView
        fun setItem(item: ChatItem, context: Context?) {
            val option = MultiTransformation(CenterCrop(), RoundedCorners(8))
            if (context != null) {
                Glide.with(context)
                    .load(item.content)
                    .apply(RequestOptions.bitmapTransform(option))
                    .into(image)
            }
            nameText.text = item.name
            sendTimeText.text = item.sendTime
        }

        init {
            nameText = itemView.findViewById(R.id.name_text)
            image = itemView.findViewById(R.id.image_view)
            sendTimeText = itemView.findViewById(R.id.send_time_text)
        }
    }

    inner class RightImageViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var image: ImageView
        var sendTimeText: TextView
        fun setItem(item: ChatItem, context: Context?) {
            val option = MultiTransformation(CenterCrop(), RoundedCorners(8))
            if (context != null) {
                Glide.with(context)
                    .load(item.content)
                    .apply(RequestOptions.bitmapTransform(option))
                    .into(image)
            }
            sendTimeText.text = item.sendTime
        }

        init {
            image = itemView.findViewById(R.id.image_view)
            sendTimeText = itemView.findViewById(R.id.send_time_text)
        }
    }

}