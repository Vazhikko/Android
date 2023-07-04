package com.example.musicplayer32

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class RecyclerViewMusicAdapter(private val musicList : List<Music>): RecyclerView.Adapter<RecyclerViewMusicAdapter.AudioViewHolder>() {
    private lateinit var mListener :  onItemClickListener
    interface onItemClickListener{

        fun onItemClick(position: Int)

    }

    fun setOnItemClickListener(listener : onItemClickListener){
        mListener = listener
    }

    class AudioViewHolder(itemView : View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView){
        private lateinit var musicName : TextView
        private lateinit var author: TextView
        private lateinit var item :View
        init {
            itemView.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }
            musicName = itemView.findViewById(R.id.musicName)
            author = itemView.findViewById(R.id.author)
        }
        fun setData(music: Music){
            musicName.text = music.title
            author.text = music.author

        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_music, parent, false)
        return AudioViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: AudioViewHolder, position: Int) {
        holder.setData(musicList[position])
    }

    override fun getItemCount(): Int {
        return musicList.size
    }
}