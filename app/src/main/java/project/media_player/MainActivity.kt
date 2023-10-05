package project.media_player

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import project.media_player.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)

        supportFragmentManager.beginTransaction().add(R.id.main_activity,Cool_Media_Fragment()).commit()

        setContentView(binding.root)

    }
}