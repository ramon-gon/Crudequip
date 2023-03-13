package copernic.cat.kingsleague.administrador

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import copernic.cat.kingsleague.R
import copernic.cat.kingsleague.databinding.FragmentMenuAdminBinding

class MenuAdmin : Fragment() {
    private var _binding: FragmentMenuAdminBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentMenuAdminBinding.inflate(inflater, container, false)

        binding.btnJugadors.setOnClickListener() {
            findNavController().navigate(R.id.action_menu_to_jugadorsAdmin)
        }

        binding.btnClasificacio.setOnClickListener() {
            findNavController().navigate(R.id.action_menu_to_classificacioAdmin)
        }


        binding.btnEquipsModificar.setOnClickListener() {
            findNavController().navigate(R.id.action_menu_to_menuEquips)
        }

        binding.btnJugadorsModificar.setOnClickListener() {
            findNavController().navigate(R.id.action_menu_to_menuJugadors)
        }
        return binding.root
    }
}