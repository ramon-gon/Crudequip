package copernic.cat.kingsleague

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import copernic.cat.kingsleague.databinding.ActivityMenuAdmBinding

class MenuAdm : AppCompatActivity() {
    private lateinit var binding: ActivityMenuAdmBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuAdmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.navHostFragmentContentMainAdmin.getFragment<Fragment>().findNavController()
            .setGraph(R.navigation.nav_graph_administrador)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_main, menu)

        return super.onCreateOptionsMenu(menu)
    }
}