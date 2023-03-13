package copernic.cat.kingsleague.administrador

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import copernic.cat.kingsleague.R
import copernic.cat.kingsleague.databinding.FragmentMenuEquipsBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MenuEquips.newInstance] factory method to
 * create an instance of this fragment.
 */
class MenuEquips : Fragment() {
    private var _binding: FragmentMenuEquipsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMenuEquipsBinding.inflate(inflater, container, false)

        binding.btnCrearEquips.setOnClickListener() {
            findNavController().navigate(R.id.action_menuEquips_to_crearEquips)
        }

        binding.btnEliminarEquips.setOnClickListener() {
            findNavController().navigate(R.id.action_menuEquips_to_eliminarEquips)
        }


        binding.btnModificarPuntuacio.setOnClickListener() {
            findNavController().navigate(R.id.action_menuEquips_to_modificarClassificacio)
        }

        binding.btnTornarMenuequips.setOnClickListener() {
            findNavController().navigate(R.id.action_menuEquips_to_menu)
        }

        return binding.root

    }
}