package copernic.cat.kingsleague

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import copernic.cat.kingsleague.databinding.FragmentMenuJugadorsBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MenuJugadors.newInstance] factory method to
 * create an instance of this fragment.
 */
class MenuJugadors : Fragment() {
    // TODO: Rename and change types of parameters
    private var _binding: FragmentMenuJugadorsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentMenuJugadorsBinding.inflate(inflater, container, false)

        binding.btnCrearJugador.setOnClickListener() {
            findNavController().navigate(R.id.action_menuJugadors_to_crearJugador)
        }

        binding.btnEliminarJugador.setOnClickListener() {
            findNavController().navigate(R.id.action_menuJugadors_to_eliminarJugadors)
        }


        binding.btnTornarMenujugador.setOnClickListener() {
            findNavController().navigate(R.id.action_menuJugadors_to_menu)
        }

        return binding.root
    }
}