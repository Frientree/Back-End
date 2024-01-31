package com.d101.presentation.main

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.d101.presentation.BackgroundMusicPlayer
import com.d101.presentation.R
import com.d101.presentation.databinding.ActivityMainBinding
import com.d101.presentation.main.state.MainActivityViewState
import com.d101.presentation.main.viewmodel.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val viewModel: MainActivityViewModel by viewModels()
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        BackgroundMusicPlayer.initMusicList(this)

        BackgroundMusicPlayer.playMusic(this, BackgroundMusicPlayer.getMusicList()[0])

        initNavigationView()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currentViewState.collect {
                    when (it) {
                        MainActivityViewState.CalendarView -> {
                            // 아이콘을 바꾼다.
                            binding.leafFloatingActionButton.setImageDrawable(
                                AppCompatResources.getDrawable(
                                    this@MainActivity,
                                    R.drawable.btn_tree,
                                ),
                            )
                        }

                        MainActivityViewState.MyPageView -> {
                            // 아이콘을 바꾼다.
                            binding.leafFloatingActionButton.setImageDrawable(
                                AppCompatResources.getDrawable(
                                    this@MainActivity,
                                    R.drawable.btn_tree,
                                ),
                            )
                        }

                        MainActivityViewState.TreeView -> {
                            // 아이콘을 바꾼다.
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
        }

        binding.leafFloatingActionButton.setOnClickListener {
            when (viewModel.currentViewState.value) {
                MainActivityViewState.TreeView -> {
                    Toast.makeText(this, "이파리 기능", Toast.LENGTH_SHORT).show()
                }

                else -> {
                    navController.navigate(R.id.treeFragment)
                    binding.bottomNavigationView.selectedItemId = R.id.treeFragment
                    viewModel.changeViewState(MainActivityViewState.TreeView)
                }
            }
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

    override fun onDestroy() {
        super.onDestroy()
        BackgroundMusicPlayer.releaseMusicPlayer()
    }
}
