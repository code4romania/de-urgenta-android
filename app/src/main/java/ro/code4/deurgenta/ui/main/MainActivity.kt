package ro.code4.deurgenta.ui.main

import android.os.Bundle
import android.widget.Button
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.onNavDestinationSelected
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import org.koin.androidx.viewmodel.ext.android.viewModel
import ro.code4.deurgenta.BuildConfig
import ro.code4.deurgenta.R
import ro.code4.deurgenta.helper.startActivityWithoutTrace
import ro.code4.deurgenta.helper.takeUserTo
import ro.code4.deurgenta.ui.auth.AuthActivity
import ro.code4.deurgenta.ui.base.BaseActivity

class MainActivity : BaseActivity<MainViewModel>() {

    override val layout: Int
        get() = R.layout.activity_main
    override val viewModel: MainViewModel by viewModel()
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(findViewById(R.id.toolbar))
        drawerLayout = findViewById(R.id.drawerLayout)
        navView = findViewById(R.id.navView)
        navController =
            (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_item_home, R.id.nav_item_groups, R.id.nav_item_backpacks, R.id.nav_item_courses,
                R.id.nav_item_about, R.id.nav_item_settings
            ),
            drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // This needs to be set after `setupWithNavController`
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)

        navView.setCheckedItem(R.id.nav_item_home)

        navView.setNavigationItemSelectedListener { item ->
            val handled = onNavDestinationSelected(item, navController)

            if (handled) {
                drawerLayout.closeDrawer(findViewById(R.id.side_navigation))
                true
            } else {
                when (item.itemId) {
                    R.id.nav_item_logout -> {
                        viewModel.logout()
                        true
                    }
                    else -> false
                }
            }
        }

        findViewById<Button>(R.id.nav_footer_donate_button).setOnClickListener {
            takeUserTo(BuildConfig.CODE4RO_DONATE_URL)
        }

        viewModel.onLogoutLiveData().observe(this) {
            startActivityWithoutTrace(AuthActivity::class.java)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
