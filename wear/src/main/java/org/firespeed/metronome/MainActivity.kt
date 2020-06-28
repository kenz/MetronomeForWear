package org.firespeed.metronome

import android.content.Context
import android.os.Bundle
import android.os.PowerManager
import android.os.Vibrator
import android.view.WindowManager
import android.widget.SeekBar
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil.setContentView
import androidx.fragment.app.FragmentActivity
import androidx.wear.ambient.AmbientModeSupport
import org.firespeed.metronome.actions.BeepTaktAction
import org.firespeed.metronome.actions.VibratorTaktAction
import org.firespeed.metronome.animator.LinearAnimator
import org.firespeed.metronome.databinding.ActivityMainBinding
import org.firespeed.metronome.ui.MetronomeViewModel
import androidx.activity.viewModels

class MainActivity : FragmentActivity(), AmbientModeSupport.AmbientCallbackProvider {
    override fun getAmbientCallback(): AmbientModeSupport.AmbientCallback? {
        return MyAmbientCallback()
    }

    class MyAmbientCallback : AmbientModeSupport.AmbientCallback() {

    }

    private val viewModel: MetronomeViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val binding: ActivityMainBinding = setContentView(this, R.layout.activity_main)
        binding.also {
            it.activity = this
            it.lifecycleOwner = this
            it.viewModel = viewModel
        }
        binding.seekBmp.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                viewModel.setBpm(p1)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}

        })
        binding.editBpm.addTextChangedListener { viewModel.setBpm(it.toString().toInt()) }
        binding.progressBar.max = LinearAnimator.MAX_VALUE

        lifecycle.addObserver(viewModel)
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        val taktAction = VibratorTaktAction(vibrator, 30L)
        val beepAction = BeepTaktAction()
        viewModel.onTaktTimeListener = {
            beepAction.action()
            taktAction.action()
        }

        viewModel.onValueUpdateListener = {
            binding.progressBar.progress = it
        }

        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        @Suppress("DEPRECATION")
        val wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "FlashWear:WatchFaceWakelockTag") // note WakeLock spelling
        wakeLock.acquire(Long.MAX_VALUE)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun onStop() {
        super.onStop()
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }


}
