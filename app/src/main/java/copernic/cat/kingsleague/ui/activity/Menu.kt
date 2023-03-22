package copernic.cat.kingsleague.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import copernic.cat.kingsleague.R
import copernic.cat.kingsleague.databinding.ActivityMenuBinding

class Menu : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.navHostFragmentContentMain.getFragment<Fragment>().findNavController()
            .setGraph(R.navigation.nav_graph_usuaris)
    }

    /*
    onCreateOptionsMenu es un método que se ejecuta cuando se crea el menú de opciones de la actividad.

    El método infla el archivo de recursos de menú R.menu.menu_main y
    lo agrega al menú de opciones. El objeto menuInflater se utiliza para
    inflar los archivos de recursos de menú en objetos de menú.

     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_main, menu)

        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.configuracio -> {
            binding.navHostFragmentContentMain.findNavController()
                .navigate(R.id.configuracio2)
            true
        }
        R.id.menuUsuari -> {
            onBackPressed()
            true
        }
        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        if ((binding.navHostFragmentContentMain.findNavController().currentDestination?.id
                ?: -1) != R.id.menuUsuari
        )
            findNavController(R.id.nav_host_fragment_content_main).popBackStack()
    }
}