package copernic.cat.kingsleague

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import copernic.cat.kingsleague.databinding.ActivityMenu2Binding

class MenuA : AppCompatActivity() {
    private lateinit var binding: ActivityMenu2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenu2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.navHostFragmentContentMainAdmin.getFragment<Fragment>().findNavController().setGraph(R.navigation.nav_graph_administrador)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_main, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.configuracio -> {
            binding.navHostFragmentContentMainAdmin.findNavController()
                .navigate(R.id.configuracioAdmin)
            true
        }
        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        if ((binding.navHostFragmentContentMainAdmin.findNavController().currentDestination?.id
                ?: -1) != R.id.menu
        )
            findNavController(R.id.nav_host_fragment_content_main_admin).popBackStack()
    }
}