package com.d101.presentation.main

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.OvershootInterpolator
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.d101.presentation.R
import com.d101.presentation.databinding.ActivityMainBinding
import com.d101.presentation.main.fragments.dialogs.LeafDialogInterface
import com.d101.presentation.main.fragments.dialogs.LeafMessageBaseFragment
import com.d101.presentation.main.fragments.dialogs.LeafReceiveBaseFragment
import com.d101.presentation.main.state.MainActivityViewState
import com.d101.presentation.main.viewmodel.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import utils.repeatOnStarted

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var canLaunchMenu: Boolean = true
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val viewModel: MainActivityViewModel by viewModels()
    private lateinit var navController: NavController

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
        initEvent()

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
                    navController.navigate(R.id.treeFragment)
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

    private fun initNavigationView() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        navController = navHostFragment.navController

        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.calendarFragment -> viewModel.changeViewState(
                    MainActivityViewState.CalendarView,
                )

                R.id.myPageFragment -> viewModel.changeViewState(MainActivityViewState.MyPageView)
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
//        val endWidth = resources.getDimension(R.dimen.full_width)
//            .toInt()

        val animator = AnimatorSet()
        animator.playTogether(
            animationSet[WRITE_UP],
            animationSet[WRITE_LEFT],
            animationSet[READ_UP],
            animationSet[READ_RIGHT],
        )
//        animator.play(changeButtonWidth(binding.writeLeafButton, 50, endWidth))
//            .with(changeButtonWidth(binding.readLeafButton, 50, endWidth))
//            .after(animationSet[READ_RIGHT])

        animator.start()
    }

    // 열심히 만들었는데 별로 안예뻐서 일단 보류함.
//    private fun changeButtonWidth(button : Button, startWidth: Int, endWidth: Int) : ValueAnimator{
//        return ValueAnimator.ofInt(startWidth, endWidth).apply {
//            duration = 100
//            addUpdateListener { animation ->
//                val animatedValue = animation.animatedValue as Int
//                val layoutParams = button.layoutParams
//                layoutParams.width = animatedValue
//                button.layoutParams = layoutParams
//            }
//        }
//    }
    private fun endLeafAnimation() {
        val animation = AnimationUtils.loadAnimation(this, R.anim.rotate_close_to_plus)
        binding.leafFloatingActionButton.animation = animation
        binding.leafFloatingActionButton.startAnimation(animation)

        // 버튼의 시작 너비와 끝 너비를 정의합니다.
//        val startWidth = binding.writeLeafButton.width

//        changeButtonWidth(binding.writeLeafButton, startWidth, 0).start()
//        changeButtonWidth(binding.readLeafButton, startWidth, 0).start()

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

    companion object {
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
