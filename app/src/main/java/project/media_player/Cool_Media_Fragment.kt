package project.media_player

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import project.media_player.databinding.FragmentCoolMediaBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Cool_Media_Fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class Cool_Media_Fragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var runnable: Runnable
    private var handler: Handler = Handler()
    private var pause: Boolean = false
    var position = 0
    private val musicFiles = arrayOf("butterfly", "dancing_with_your_ghost", "ippo", "makeba")
    lateinit var binding: FragmentCoolMediaBinding

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
        binding = FragmentCoolMediaBinding.inflate(inflater, container, false)

        val anim = AnimationUtils.loadAnimation(requireContext(), R.anim.circle_explosion_animation)
            .apply {
                duration = 1300
                interpolator = AccelerateDecelerateInterpolator()
            }
        val anim_reverse = AnimationUtils.loadAnimation(requireContext(), R.anim.reverse_explosion)
            .apply {
                duration = 1300
                interpolator = AccelerateDecelerateInterpolator()
            }

        val rotate_dis =
            AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_disappearing).apply {
                duration = 1500
                interpolator = AccelerateDecelerateInterpolator()
            }
        val rotate_a =
            AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_appearing).apply {
                duration = 1000
                interpolator = AccelerateDecelerateInterpolator()
            }
        val rotate_a_r =
            AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_reverse_a).apply {
                duration = 1000
                interpolator = AccelerateDecelerateInterpolator()
            }


        binding.play.setOnClickListener {
            binding.circle.visibility = View.VISIBLE

            binding.play.visibility = View.INVISIBLE

            binding.stop.visibility = View.VISIBLE
            binding.next.visibility = View.VISIBLE
            binding.previous.visibility = View.VISIBLE

            binding.circle.startAnimation(anim)

            anim.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                    binding.next.startAnimation(rotate_a)
                    binding.previous.startAnimation(rotate_a_r)
                }

                override fun onAnimationEnd(animation: Animation?) {
                    binding.coolChange.visibility = View.VISIBLE
                }

                override fun onAnimationRepeat(animation: Animation?) {
                }

            })

            if (pause) {
                mediaPlayer.seekTo(mediaPlayer.currentPosition)
                mediaPlayer.start()
                pause = false
            } else {

                val songResourceId =
                    resources.getIdentifier(musicFiles[position], "raw", "project.media_player")

                mediaPlayer = MediaPlayer.create(requireContext(), songResourceId)

                mediaPlayer.start()

//                position = (position + 1) % musicFiles.size
                if (musicFiles.size - 1 == position) {
                    position = 0
                } else position++


            }

            initializeSeekBar()

            mediaPlayer.setOnCompletionListener {
                binding.play.visibility = View.VISIBLE
                binding.stop.visibility = View.GONE
            }
        }

        binding.next.setOnClickListener {
            mediaPlayer.reset()
            if (position == musicFiles.size - 1) {
                position = 0
                Toast.makeText(
                    requireContext(),
                    "this is last song,there is no next one",
                    Toast.LENGTH_SHORT
                ).show()

            } else {
                val songResourceId =
                    resources.getIdentifier(musicFiles[position], "raw", "project.media_player")

                mediaPlayer = MediaPlayer.create(requireContext(), songResourceId)
                mediaPlayer.start()
                Log.d("NEXT", "onCreateView: " + songResourceId + "  " + position)
                position++
            }


        }

        binding.previous.setOnClickListener {

            if (position == musicFiles.size - 1) {
                position = 0
            } else {

                mediaPlayer.reset()
                val songResourceId =
                    resources.getIdentifier(musicFiles[position], "raw", "project.media_player")

                mediaPlayer = MediaPlayer.create(requireContext(), songResourceId)
                mediaPlayer.start()
                Log.d("PREVIOUS", "onCreateView: " + position % musicFiles.size)
                position--
            }
        }




        binding.stop.setOnClickListener {
            binding.circle.startAnimation(anim_reverse)

            anim_reverse.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                    binding.coolChange.visibility = View.GONE

                }

                override fun onAnimationEnd(animation: Animation?) {
                    binding.circle.visibility = View.GONE
                }

                override fun onAnimationRepeat(animation: Animation?) {
                }

            })
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
                pause = true
                binding.play.visibility = View.VISIBLE
                binding.stop.visibility = View.GONE
            }
        }



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
        var minute: Int = 0
        var second: Int = 0

        runnable = Runnable {
            binding.seekBar.progress = mediaPlayer.currentSeconds
            second = mediaPlayer.currentSeconds - minute * 60
            minute = mediaPlayer.currentSeconds / 60
            binding.currentTime.text = minute.toString() + ":" + second.toString()
            binding.fullTime.text =
                (mediaPlayer.seconds / 60).toString() + ":" + (mediaPlayer.seconds - (mediaPlayer.seconds / 60) * 60).toString()

            handler.postDelayed(runnable, 10)
        }
        handler.postDelayed(runnable, 10)
    }

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
         * @return A new instance of fragment Cool_Media_Fragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) = Cool_Media_Fragment().apply {
            arguments = Bundle().apply {
                putString(ARG_PARAM1, param1)
                putString(ARG_PARAM2, param2)
            }
        }
    }
}