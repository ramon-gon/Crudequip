package copernic.cat.kingsleague.usuari

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import copernic.cat.kingsleague.R
import copernic.cat.kingsleague.databinding.FragmentMenuUsuariBinding

class MenuUsuari : Fragment() {
    private var _binding: FragmentMenuUsuariBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentMenuUsuariBinding.inflate(inflater, container, false)

        binding.btnJugadors.setOnClickListener() {
            findNavController().navigate(R.id.action_menuUsuari_to_jugadors)
        }

        binding.btnClasificacio.setOnClickListener() {
               findNavController().navigate(R.id.action_menuUsuari_to_classificacio2)
        }

        return binding.root
    }
}