package org.firespeed.metronome

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.PowerManager
import android.os.Vibrator
import android.view.KeyEvent
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil.setContentView
import androidx.fragment.app.FragmentActivity
import androidx.wear.ambient.AmbientModeSupport
import dagger.hilt.android.AndroidEntryPoint
import org.firespeed.metronome.actions.BeepTaktAction
import org.firespeed.metronome.actions.VibratorTaktAction
import org.firespeed.metronome.databinding.MetronomeWearActivityBinding
import org.firespeed.metronome.ui.MetronomeViewModel
import org.firespeed.metronome.ui.settings.SettingActivity

@AndroidEntryPoint
class MetronomeWearActivity : FragmentActivity(), AmbientModeSupport.AmbientCallbackProvider {
    override fun getAmbientCallback(): AmbientModeSupport.AmbientCallback? {
        return MyAmbientCallback()
    }

    class MyAmbientCallback : AmbientModeSupport.AmbientCallback()
    // Activity
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (event.repeatCount == 0) {
            when (keyCode) {
                KeyEvent.KEYCODE_STEM_1 -> {
                    viewModel.startStop()
                    true
                }
                KeyEvent.KEYCODE_STEM_2 -> {
                    openSetting()
                    true
                }
                else -> super.onKeyDown(keyCode, event)

            }
        } else {
            super.onKeyDown(keyCode, event)
        }
    }

    private fun openSetting(){
        startActivity(  Intent(this, SettingActivity::class.java))
    }
    private val viewModel: MetronomeViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.metronome_wear_activity)

        val binding: MetronomeWearActivityBinding = setContentView(this, R.layout.metronome_wear_activity)
        binding.also {
            it.activity = this
            it.lifecycleOwner = this
            it.viewModel = viewModel
        }

        binding.background.setOnLongClickListener{
            openSetting()
            true
        }
        lifecycle.addObserver(viewModel)
        if ((getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?)?.hasVibrator() == true)
            viewModel.actionVibrator =
                VibratorTaktAction(getSystemService(Context.VIBRATOR_SERVICE) as Vibrator, 30L)

        if (packageManager.hasSystemFeature(PackageManager.FEATURE_AUDIO_LOW_LATENCY))
            viewModel.actionSound = BeepTaktAction()

        viewModel.setValueUpdateListener {
            binding.hand.rotation = it
            binding.handShadow.rotation = it
        }

       binding.hand.viewTreeObserver.addOnGlobalLayoutListener {
            val centerY = binding.metronome.height / 2
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
