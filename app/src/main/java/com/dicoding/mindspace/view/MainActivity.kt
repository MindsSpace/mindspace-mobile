package com.dicoding.mindspace.view

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.dicoding.mindspace.R
import com.dicoding.mindspace.databinding.ActivityMainBinding
import com.dicoding.mindspace.factory.ViewModelWithoutTokenFactory
import com.dicoding.mindspace.view.chat.ChatFragment
import com.google.android.material.bottomappbar.BottomAppBarTopEdgeTreatment
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel> {
        ViewModelWithoutTokenFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        setupNavigation()
        observeViewModel()
    }

    fun getDeleteJournalButton(): AppCompatImageButton {
        return binding.customActionDeleteJournal
    }

    fun getAddJournalButton(): AppCompatImageButton {
        return binding.customActionAddJournal
    }

    private fun observeViewModel() {
        viewModel.roomData.observe(this) { data ->
            if (data.success == true) {
                findNavController(R.id.fragment_container).navigate(
                    R.id.action_global_navigation_chat,
                    Bundle().apply { putString(ChatFragment.ROOM_ID, data.data?.id) }
                )
            }
        }
    }

    private fun setupView() {
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupNormalWindow()
    }

    private fun setupNormalWindow() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime())
            val isKeyboardVisible = imeInsets.bottom > 0

            view.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                if (isKeyboardVisible) imeInsets.bottom else -systemBars.bottom
            )
            insets
        }
    }

    private fun setupKeyboardWindow() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime())
            val isKeyboardVisible = imeInsets.bottom > 0

            view.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                if (isKeyboardVisible) imeInsets.bottom else systemBars.bottom
            )
            insets
        }
    }

    private fun setupNavigation() {
        setSupportActionBar(binding.topAppBar)

        // bottom app bar
        val bottomBar = binding.bottomAppBar
        val shapeModel = ShapeAppearanceModel.builder()
            .setTopEdge(
                BottomAppBarTopEdgeTreatment(
                    bottomBar.fabCradleMargin,
                    bottomBar.fabCradleRoundedCornerRadius,
                    bottomBar.cradleVerticalOffset
                )
            )
            .build()

        val background = bottomBar.background as MaterialShapeDrawable
        background.shapeAppearanceModel = shapeModel

        binding.chatFab.imageTintList =
            ColorStateList.valueOf(ContextCompat.getColor(this, R.color.green_100))

        // navigation
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        val navController = navHostFragment.navController
        val topAppBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_journal,
                R.id.navigation_history,
                R.id.navigation_profile
            )
        )

        setupActionBarWithNavController(navController, topAppBarConfiguration)

        binding.bottomNav.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            setupNormalWindow()
            setBottomVisible()
            setAddJournalMenuVisible(false)
            setDeleteJournalMenuVisible(false)

            when (destination.id) {
                R.id.navigation_chat, R.id.navigation_new_journal -> {
                    setBottomInvisible()
                    setupKeyboardWindow()
                }

                R.id.navigation_meditation -> {
                    setBottomInvisible()
                }

                R.id.navigation_journal -> {
                    setAddJournalMenuVisible(true)
                }

                R.id.navigation_detail_journal -> {
                    setDeleteJournalMenuVisible(true)
                }
            }
        }

        binding.chatFab.setOnClickListener {
            viewModel.createRoom()
        }

        val roomId = intent.getStringExtra(ChatFragment.ROOM_ID)
        if (!roomId.isNullOrEmpty()) {
            navController.navigate(R.id.navigation_home)
            navController.navigate(
                R.id.action_global_navigation_chat,
                Bundle().apply { putString(ChatFragment.ROOM_ID, roomId) }
            )
        }

        binding.customActionAddJournal.setOnClickListener {
            navController.navigate(
                R.id.action_navigation_journal_to_navigation_new_journal
            )
        }
    }

    private fun setAddJournalMenuVisible(show: Boolean) {
        binding.customActionAddJournal.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun setDeleteJournalMenuVisible(show: Boolean) {
        binding.customActionDeleteJournal.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun setBottomInvisible() {
        binding.bottomAppBar.visibility = View.GONE
        binding.chatFab.visibility = View.GONE
        binding.fragmentContainer.setPadding(0, 0, 0, 0)
    }

    private fun setBottomVisible() {
        binding.bottomAppBar.visibility = View.VISIBLE
        binding.chatFab.visibility = View.VISIBLE
        binding.fragmentContainer.setPadding(0, 0, 0, 80)
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragment_container)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_setting -> {
                showSettingOptionMenu()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showSettingOptionMenu() {
        val view = findViewById<View>(R.id.action_setting)
        PopupMenu(this, view, 0, 0, R.style.CustomPopupMenuStyle).run {
            menuInflater.inflate(R.menu.setting_menu, menu)

            setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_language -> {
                        startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                    }

                    R.id.action_logout -> {
                        viewModel.logout()
                    }
                }
                true
            }
            show()
        }
    }
}