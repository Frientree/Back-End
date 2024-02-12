package com.d101.presentation.main

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.TypedValue
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.OvershootInterpolator
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.d101.presentation.R
import com.d101.presentation.databinding.ActivityMainBinding
import com.d101.presentation.main.event.MainActivityEvent
import com.d101.presentation.main.fragments.dialogs.LeafDialogInterface
import com.d101.presentation.main.fragments.dialogs.LeafMessageBaseFragment
import com.d101.presentation.main.fragments.dialogs.LeafReceiveBaseFragment
import com.d101.presentation.main.state.MainActivityViewState
import com.d101.presentation.main.viewmodel.MainActivityViewModel
import com.d101.presentation.music.BackgroundMusicService
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import utils.CustomToast
import utils.repeatOnStarted

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var canLaunchMenu: Boolean = true
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val viewModel: MainActivityViewModel by viewModels()
    private lateinit var navController: NavController
    private lateinit var tokenReceiver: BroadcastReceiver

    private var lastBackPressedTime = 0L

    var musicService: BackgroundMusicService? = null
    private var isBound = false

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as BackgroundMusicService.MusicBinder
            musicService = binder.getService()
            isBound = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            isBound = false
        }
    }

    private val px by lazy {
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            60f,
            this.resources.displayMetrics,
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initNavigationView()
        initTokenReceiver()

        receiveFCMToken()
        initEvent()
        setOnBackPressed()

        repeatOnStarted {
            viewModel.eventFlow.collect { event ->
                when (event) {
                    is MainActivityEvent.ShowErrorEvent -> {
                        showToast(event.message)
                    }
                }
            }
        }

        repeatOnStarted {
            viewModel.currentViewState.collect {
                when (it) {
                    MainActivityViewState.CalendarView -> {
                        binding.leafFloatingActionButton.setImageDrawable(
                            AppCompatResources.getDrawable(
                                this@MainActivity,
                                R.drawable.btn_tree,
                            ),
                        )
                    }

                    MainActivityViewState.MyPageView -> {
                        binding.leafFloatingActionButton.setImageDrawable(
                            AppCompatResources.getDrawable(
                                this@MainActivity,
                                R.drawable.btn_tree,
                            ),
                        )
                    }

                    MainActivityViewState.TreeView -> {
                        binding.leafFloatingActionButton.setImageDrawable(
                            AppCompatResources.getDrawable(
                                this@MainActivity,
                                R.drawable.btn_leaf,
                            ),
                        )
                    }
                }
            }
        }

        repeatOnStarted {
            viewModel.visibility.collect {
                delay(100)

                binding.writeLeafButton.visibility = if (it) View.GONE else View.VISIBLE
                binding.readLeafButton.visibility = if (it) View.GONE else View.VISIBLE
                if (it) binding.blur.visibility = View.GONE
            }
        }
    }

    private fun setOnBackPressed() {
        val backPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (viewModel.currentViewState.value == MainActivityViewState.TreeView) {
                    if (canLaunchMenu.not()) {
                        controlLeafButton()
                    } else if (
                        System.currentTimeMillis() - lastBackPressedTime < BACK_PRESS_INTERVAL
                    ) {
                        isEnabled = false
                        onBackPressed()
                    } else {
                        showToast("뒤로 버튼을 한 번 더 누르면 종료됩니다.")
                        lastBackPressedTime = System.currentTimeMillis()
                    }
                } else {
                    isEnabled = false
                    onBackPressed()
                }
            }
        }
        onBackPressedDispatcher.addCallback(this, backPressedCallback)
    }

    private fun initEvent() {
        binding.leafFloatingActionButton.setOnClickListener {
            when (viewModel.currentViewState.value) {
                MainActivityViewState.TreeView -> {
                    binding.blur.visibility = View.VISIBLE
                    binding.writeLeafButton.visibility = View.VISIBLE
                    binding.readLeafButton.visibility = View.VISIBLE
                    controlLeafButton()
                }

                else -> {
                    val navOptions = NavOptions.Builder()
                        .setPopUpTo(R.id.treeFragment, true)
                        .build()
                    navController.navigate(R.id.treeFragment, null, navOptions)
                    binding.bottomNavigationView.selectedItemId = R.id.treeFragment
                    viewModel.changeViewState(MainActivityViewState.TreeView)
                }
            }
        }
        binding.blur.setOnClickListener {
            controlLeafButton()
        }
        binding.writeLeafButton.setOnClickListener {
            val dialog = LeafMessageBaseFragment()
            LeafDialogInterface.dialog = dialog
            dialog.show(supportFragmentManager, "")
        }
        binding.readLeafButton.setOnClickListener {
            val dialog = LeafReceiveBaseFragment()
            LeafDialogInterface.dialog = dialog
            dialog.show(supportFragmentManager, "")
        }
    }

    private fun showToast(message: String) = CustomToast.createAndShow(this, message)

    private fun initTokenReceiver() {
        tokenReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                intent?.getStringExtra("TOKEN")?.let {
                    uploadToken(it)
                }
            }
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(
            tokenReceiver,
            IntentFilter("FCM_NEW_TOKEN"),
        )
    }

    private fun receiveFCMToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(
            OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }
                if (task.result != null) {
                    uploadToken(task.result!!)
                }
            },
        )
        createNotificationChannel(CHANNEL_ID, "FRIENTREE")
    }

    private fun createNotificationChannel(id: String, name: String) {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(id, name, importance)

        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun initNavigationView() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        navController = navHostFragment.navController

        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.calendarFragment -> {
                    viewModel.changeViewState(
                        MainActivityViewState.CalendarView,
                    )
                }

                R.id.myPageFragment -> {
                    viewModel.changeViewState(MainActivityViewState.MyPageView)
                }

                R.id.treeFragment -> {
                    viewModel.changeViewState(MainActivityViewState.TreeView)
                }
            }
        }
    }

    private fun controlLeafButton() {
        if (canLaunchMenu) {
            startLeafAnimation()
        } else {
            endLeafAnimation()
        }
        canLaunchMenu = !canLaunchMenu

        viewModel.finishAnimation(canLaunchMenu)
    }

    private fun startLeafAnimation() {
        val animation = AnimationUtils.loadAnimation(this, R.anim.rotate_plus_to_close)
        binding.leafFloatingActionButton.animation = animation
        binding.leafFloatingActionButton.startAnimation(animation)

        val animationSet = HashMap<Int, ObjectAnimator>()
        binding.writeLeafButton.let {
            animationSet[WRITE_UP] = upAnimation(it, 0f, -px)
            animationSet.put(WRITE_LEFT, leftAnimation(it, 0f, -px * 1.5f))
        }
        binding.readLeafButton.let {
            animationSet[READ_UP] = upAnimation(it, 0f, -px, 600)
            animationSet.put(READ_RIGHT, rightAnimation(it, 0f, px * 1.5f, 600))
        }

        val animator = AnimatorSet()
        animator.playTogether(
            animationSet[WRITE_UP],
            animationSet[WRITE_LEFT],
            animationSet[READ_UP],
            animationSet[READ_RIGHT],
        )

        animator.start()
    }

    private fun endLeafAnimation() {
        val animation = AnimationUtils.loadAnimation(this, R.anim.rotate_close_to_plus)
        binding.leafFloatingActionButton.animation = animation
        binding.leafFloatingActionButton.startAnimation(animation)

        val animationSet = HashMap<Int, ObjectAnimator>()
        binding.writeLeafButton.let {
            animationSet[WRITE_DOWN] = downAnimation(it, -px, 0f)
            animationSet.put(WRITE_RIGHT, rightAnimation(it, -px * 1.5f, 0f))
        }
        binding.readLeafButton.let {
            animationSet[READ_DOWN] = downAnimation(it, -px, 0f)
            animationSet.put(READ_LEFT, leftAnimation(it, -px * 1.5f, 0f))
        }

        val animator = AnimatorSet().apply {
            play(animationSet[WRITE_DOWN]).with(animationSet[WRITE_RIGHT])
                .with(animationSet[READ_DOWN]).with(animationSet[READ_LEFT])
        }
        animator.start()
    }

    private fun upAnimation(
        button: Button,
        startValue: Float,
        endValue: Float,
        duration: Long = DURATION,
    ): ObjectAnimator {
        val up = ObjectAnimator.ofFloat(button, "translationY", startValue, endValue)
        up.duration = duration
        up.interpolator = OvershootInterpolator()

        return up
    }

    private fun downAnimation(
        button: Button,
        startValue: Float,
        endValue: Float,
        duration: Long = DURATION,
    ): ObjectAnimator {
        val down = ObjectAnimator.ofFloat(button, "translationY", startValue, endValue)
        down.duration = duration
        down.interpolator = OvershootInterpolator()

        return down
    }

    private fun leftAnimation(
        button: Button,
        startValue: Float,
        endValue: Float,
        duration: Long = DURATION,
    ): ObjectAnimator {
        val left = ObjectAnimator.ofFloat(button, "translationX", startValue, endValue)
        left.duration = duration
        left.interpolator = OvershootInterpolator()

        return left
    }

    private fun rightAnimation(
        button: Button,
        startValue: Float,
        endValue: Float,
        duration: Long = DURATION,
    ): ObjectAnimator {
        val right = ObjectAnimator.ofFloat(button, "translationX", startValue, endValue)
        right.duration = duration
        right.interpolator = OvershootInterpolator()

        return right
    }

    override fun onStart() {
        super.onStart()
        Intent(this, BackgroundMusicService::class.java).also { intent ->
            this.bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        if (isBound) {
            unbindService(connection)
            isBound = false
        }
    }

    fun uploadToken(token: String) {
        viewModel.uploadToken(token)
    }

    companion object {
        const val CHANNEL_ID = "FRIENTREE_MESSAGING_CHANNEL"
        const val BACK_PRESS_INTERVAL = 2000L
        const val DURATION = 400L
        const val WRITE_UP = 0
        const val READ_UP = 1
        const val WRITE_DOWN = 2
        const val READ_DOWN = 3
        const val WRITE_LEFT = 4
        const val WRITE_RIGHT = 5
        const val READ_RIGHT = 6
        const val READ_LEFT = 7
    }
}
