package ro.code4.deurgenta.ui.main

import android.os.Bundle
import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import ro.code4.deurgenta.R
import ro.code4.deurgenta.helper.startActivityWithoutTrace
import ro.code4.deurgenta.ui.base.BaseActivity
import ro.code4.deurgenta.ui.login.LoginActivity

class MainActivity : BaseActivity<MainViewModel>() {
    override val layout: Int
        get() = R.layout.activity_main

    private val firebaseAnalytics: FirebaseAnalytics by inject()
    override val viewModel: MainViewModel by viewModel()
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private var selectedItem: MenuItem? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)

        navController = findNavController(R.id.nav_host_fragment)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // This needs to be set after `setupWithNavController`
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)

        navView.setCheckedItem(R.id.nav_home)

        viewModel.onLogoutLiveData().observe(this, Observer {
            startActivityWithoutTrace(LoginActivity::class.java)
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun setTitle(title: String) {
        supportActionBar?.title = title
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}

