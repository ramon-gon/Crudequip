package copernic.cat.kingsleague

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import copernic.cat.kingsleague.databinding.FragmentCrearEquipsBinding
import copernic.cat.kingsleague.databinding.FragmentModificarClassificacioBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ModificarClassificacio.newInstance] factory method to
 * create an instance of this fragment.
 */
class ModificarClassificacio : Fragment() {
    private var _binding: FragmentModificarClassificacioBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentModificarClassificacioBinding.inflate(inflater, container, false)

        binding.btnTornarModificarClassificacio.setOnClickListener() {
            findNavController().navigate(R.id.action_modificarClassificacio_to_menuEquips)
        }

        binding.ModificarClassificacio.setOnClickListener() {
            findNavController().navigate(R.id.action_modificarClassificacio_to_menuEquips)
        }


        return binding.root
    }
}