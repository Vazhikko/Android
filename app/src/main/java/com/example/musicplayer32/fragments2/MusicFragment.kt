package com.example.musicplayer32.fragments2


import android.database.Cursor
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer32.Music
import com.example.musicplayer32.R
import com.example.musicplayer32.RecyclerViewMusicAdapter
import java.lang.Exception


class MusicFragment : Fragment(R.layout.my_music_layout) {
    private lateinit var seekBar: SeekBar
    private lateinit var buttonSkip : ImageView
    private lateinit var buttonPlay : ImageView
    private lateinit var buttonPrevious : ImageView
    private lateinit var recyclerView : RecyclerView
    private lateinit var songName : TextView
    private var player : MediaPlayer? = null
    private var previousPosition :Int? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        seekBar = view.findViewById(R.id.seekBar)
        songName = view.findViewById(R.id.songName)
        player = MediaPlayer()
        recyclerView = view.findViewById(R.id.recycleView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = RecyclerViewMusicAdapter(getMusics())
        if (adapter.itemCount > 0){player!!.setDataSource(getMusics()[0].uri)}
        recyclerView.adapter = adapter
        buttonPlay = view.findViewById(R.id.buttonPlay)
        buttonSkip = view.findViewById(R.id.buttonSkip)
        buttonPrevious = view.findViewById(R.id.buttonPrevious)
        adapter.setOnItemClickListener(object : RecyclerViewMusicAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                if (previousPosition != position) {
                    player?.stop()
                    player = MediaPlayer()
                    player!!.setDataSource(getMusics()[position].uri)
                    player!!.prepare()
                    player!!.start()
                    buttonPlay.setImageResource(R.drawable.ic_baseline_pause_24)

                    if (getMusics()[position].title!!.length  > 37){
                        songName.text = getMusics()[position].title!!.substring(0,37) + "..."
                    }else{
                        songName.text = getMusics()[position].title
                    }
                }
                previousPosition = position
            }
        })
        buttonPlay.setOnClickListener{
            if (player?.isPlaying == true){
                player!!.pause()
                buttonPlay.setImageResource(R.drawable.ic_baseline_play_arrow_24)
            }else{
                if(previousPosition == null){
                    previousPosition = 0
                    player = MediaPlayer()
                    player!!.setDataSource(getMusics()[previousPosition!!].uri)
                    player!!.prepare()
                    player!!.start()
                    if (getMusics()[previousPosition!!].title!!.length > 37){
                        songName.text = getMusics()[previousPosition!!].title!!.substring(0,37) + "..."

                    }else{
                        songName.text = getMusics()[previousPosition!!].title
                    }
                }
                player?.start()
                buttonPlay.setImageResource(R.drawable.ic_baseline_pause_24)
            }
        }
        buttonSkip.setOnClickListener{
            if (previousPosition == adapter.itemCount -1 ){
                player!!.stop()
                player!!.reset()
                previousPosition = 0
                player!!.setDataSource(getMusics()[previousPosition!!].uri)
                player!!.prepare()
                player!!.start()
            }else if (previousPosition != null) {

                player!!.stop()
                player!!.reset()
                player!!.setDataSource(getMusics()[previousPosition!! + 1].uri)
                player!!.prepare()
                player!!.start()
                previousPosition = previousPosition!! + 1

            }
            else{
                player = MediaPlayer()
                previousPosition = 0
                player!!.setDataSource(getMusics()[previousPosition!!].uri)
                player!!.prepare()
                player!!.start()

            }
            if (getMusics()[previousPosition!!].title!!.length > 37){
                songName.text = getMusics()[previousPosition!!].title!!.substring(0,37) + "..."

            }else{
                songName.text = getMusics()[previousPosition!!].title
            }
            buttonPlay.setImageResource(R.drawable.ic_baseline_pause_24)

        }
        buttonPrevious.setOnClickListener {
            if (previousPosition == 0){
                player!!.stop()
                player = MediaPlayer()
                previousPosition = adapter.itemCount
                player!!.setDataSource(getMusics()[previousPosition!! - 1].uri)
                player!!.prepare()
                player!!.start()
                previousPosition = previousPosition!! - 1
            }
            else if (previousPosition != null ) {

                player!!.stop()
                player!!.reset()
                player!!.setDataSource(getMusics()[previousPosition!! - 1].uri)
                player!!.prepare()
                player!!.start()
                previousPosition = previousPosition!! - 1
            }

            else{
                player = MediaPlayer()
                previousPosition = adapter.itemCount
                player!!.setDataSource(getMusics()[previousPosition!! - 1].uri)
                player!!.prepare()
                player!!.start()
                previousPosition = previousPosition!! - 1
            }
            if (getMusics()[previousPosition!!].title!!.length > 37){
                songName.text = getMusics()[previousPosition!!].title!!.substring(0,37) + "..."
            }else{
                songName.text = getMusics()[previousPosition!!].title
            }
            buttonPlay.setImageResource(R.drawable.ic_baseline_pause_24)
        }
        seekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, pos: Int, changed: Boolean) {
                if (changed){
                    player?.seekTo(pos)
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }

        })
        var handler = Handler()
        handler.postDelayed(object : Runnable{
            override fun run() {
                try {
                    seekBar.max = player!!.duration
                    seekBar.progress = player!!.currentPosition
                    handler.postDelayed(this, 1000)
                }catch (e: Exception){
                    seekBar.progress = 0
                }
            }
        },0)
    }


    override fun onPause() {
        super.onPause()
        if (player?.isPlaying == true && requireActivity().lifecycle.currentState != (Lifecycle.State.STARTED) ) {
            player!!.pause()

        }
    }


    private fun getMusics(): List<Music> {
        val musicList = ArrayList<Music>()
        val uri : Uri? = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val selection : String = MediaStore.Audio.Media.IS_MUSIC + "!=0"
        val cursor : Cursor? = activity?.contentResolver?.query(uri!!,null,selection,null,null)
        while (cursor!!.moveToNext()){
            var url :String = cursor!!.getString(cursor!!.getColumnIndex(MediaStore.Audio.Media.DATA))
            var author : String = cursor!!.getString(cursor!!.getColumnIndex(MediaStore.Audio.Media.ARTIST))
            var title : String = cursor!!.getString((cursor!!.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)))
            title
            musicList.add(Music(title,author,url))
        }
        return musicList
    }

}