package org.firespeed.metronome

import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.os.PowerManager
import android.os.Vibrator
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil.setContentView
import androidx.fragment.app.FragmentActivity
import androidx.wear.ambient.AmbientModeSupport
import org.firespeed.metronome.actions.BeepTaktAction
import org.firespeed.metronome.actions.VibratorTaktAction
import org.firespeed.metronome.databinding.ActivityMainBinding
import org.firespeed.metronome.ui.MetronomeViewModel

class MainActivity : FragmentActivity(), AmbientModeSupport.AmbientCallbackProvider {
    override fun getAmbientCallback(): AmbientModeSupport.AmbientCallback? {
        return MyAmbientCallback()
    }

    class MyAmbientCallback : AmbientModeSupport.AmbientCallback()

    private val viewModel: MetronomeViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val binding: ActivityMainBinding = setContentView(this, R.layout.activity_main)
        binding.also {
            it.activity = this
            it.lifecycleOwner = this
            it.viewModel = viewModel
        }

        binding.editBpm.addTextChangedListener { viewModel.setBpm(it.toString().toInt()) }

        lifecycle.addObserver(viewModel)
        viewModel.actionVibrator =
            VibratorTaktAction(getSystemService(Context.VIBRATOR_SERVICE) as Vibrator, 30L)
        viewModel.actionSound = BeepTaktAction()

        viewModel.setValueUpdateListener {
            binding.hand.rotation = it
            binding.handShadow.rotation = it
        }

        val centerY = (getSystemService(Context.WINDOW_SERVICE) as WindowManager).let {
            val size = Point()
            it.defaultDisplay.getSize(size)
            size.y / 2
        }
        binding.hand.viewTreeObserver.addOnGlobalLayoutListener {
            binding.hand.pivotY = centerY - binding.hand.y
            binding.hand.pivotX = binding.hand.width / 2f
            binding.handShadow.pivotY = binding.hand.pivotY + binding.handShadow.y - binding.hand.y
        }
        binding.handShadow.viewTreeObserver.addOnGlobalLayoutListener {
            binding.handShadow.pivotX = binding.handShadow.width / 2f
        }

        @Suppress("DEPRECATION")
        (getSystemService(Context.POWER_SERVICE) as PowerManager).newWakeLock(
            PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "FlashWear:WatchFaceWakelockTag"
        ).acquire(Long.MAX_VALUE)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

    }

    override fun onStop() {
        super.onStop()
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }


}
