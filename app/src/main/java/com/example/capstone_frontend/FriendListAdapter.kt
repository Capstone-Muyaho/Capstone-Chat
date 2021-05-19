package com.example.capstone_frontend

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class FriendListAdapter(val friendList: ArrayList<FriendInformation>) : RecyclerView.Adapter<FriendListAdapter.CustomViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendListAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return CustomViewHolder(view).apply {
            itemView.setOnClickListener {
                val curPos : Int = adapterPosition
                val profile: FriendInformation = friendList.get(curPos)
                Toast.makeText(parent.context, "닉네임 : ${profile.nickname} 유형 : ${profile.profiletype}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount(): Int {
        return friendList.size
    }

    override fun onBindViewHolder(holder: FriendListAdapter.CustomViewHolder, position: Int) {
        holder.gender.setImageResource(friendList.get(position).gender)
        holder.nickname.text = friendList.get(position).nickname.toString()
        holder.profiletype.text = friendList.get(position).profiletype.toString()
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val gender = itemView.findViewById<ImageView>(R.id.iv_profile) //성별
        val nickname = itemView.findViewById<TextView>(R.id.tv_nickname) //닉네임
        val profiletype = itemView.findViewById<TextView>(R.id.tv_profiletype) //유형
    }

}