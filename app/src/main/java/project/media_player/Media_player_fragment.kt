package project.media_player

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.provider.MediaStore.Video.Media
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.coroutines.flow.combine
import project.media_player.databinding.FragmentMediaPlayerFragmentBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Media_player_fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class Media_player_fragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var mediaPlayer: MediaPlayer
        private lateinit var runnable: Runnable

    lateinit var binding:FragmentMediaPlayerFragmentBinding
    private var handler: Handler = Handler()
    private var pause: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMediaPlayerFragmentBinding.inflate(inflater, container, false)

        //start the media player

        binding.playBtn.setOnClickListener {
            if (pause) {
                mediaPlayer.seekTo(mediaPlayer.currentPosition)
                mediaPlayer.start()

                pause = false
                Toast.makeText(requireContext(), "media playing", Toast.LENGTH_SHORT).show()
            } else {

                mediaPlayer = MediaPlayer.create(requireContext(), R.raw.butterfly)
                mediaPlayer.start()
                Toast.makeText(requireContext(), "media playing", Toast.LENGTH_SHORT).show()

            }
            initializeSeekBar()
            binding.playBtn.isEnabled = false
            binding.pauseBtn.isEnabled = true
            binding.stopBtn.isEnabled = true

            mediaPlayer.setOnCompletionListener {
                binding.playBtn.isEnabled = true
                binding.pauseBtn.isEnabled = false
                binding.stopBtn.isEnabled = false

                Toast.makeText(requireContext(), "end", Toast.LENGTH_SHORT).show()
            }
        }

        // Pause the media player
        binding.pauseBtn.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
                pause = true
                binding.playBtn.isEnabled = true
                binding.pauseBtn.isEnabled = false
                binding.stopBtn.isEnabled = true
                Toast.makeText(requireContext(), "media pause", Toast.LENGTH_SHORT).show()
            }
        }

        // Stop the media player

        // Seek bar change listener
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                if (b) {
                    mediaPlayer.seekTo(i * 1000)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }
        })

        return binding.root
    }

    private fun initializeSeekBar() {
        binding.seekBar.max = mediaPlayer.seconds

        runnable = Runnable {
            binding.seekBar.progress = mediaPlayer.currentSeconds

            binding.tvPass.text = "${mediaPlayer.currentSeconds} sec"
            val diff = mediaPlayer.seconds - mediaPlayer.currentSeconds
            binding.tvDue.text = "$diff sec"

            handler.postDelayed(runnable, 1000)
        }
        handler.postDelayed(runnable, 1000)
    }


// Creating an extension property to get the media player time duration in seconds
val MediaPlayer.seconds: Int
    get() {
        return this.duration / 1000
    }

// Creating an extension property to get media player current position in seconds
val MediaPlayer.currentSeconds: Int
    get() {
        return this.currentPosition / 1000
    }



companion object {
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Media_player_fragment.
     */
    // TODO: Rename and change types and number of parameters
    @JvmStatic
    fun newInstance(param1: String, param2: String) = Media_player_fragment().apply {
        arguments = Bundle().apply {
            putString(ARG_PARAM1, param1)
            putString(ARG_PARAM2, param2)
        }
    }
}
}
