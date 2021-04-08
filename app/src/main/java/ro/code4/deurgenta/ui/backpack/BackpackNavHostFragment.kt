package ro.code4.deurgenta.ui.backpack

import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import ro.code4.deurgenta.R

/**
 * A container for the backpack related navigation graph.
 */
class BackpackNavHostFragment : NavHostFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController.graph = navController.navInflater.inflate(R.navigation.navigation_backpack)
    }
}