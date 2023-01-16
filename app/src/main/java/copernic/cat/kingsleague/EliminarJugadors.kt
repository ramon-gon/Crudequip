package copernic.cat.kingsleague

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import copernic.cat.kingsleague.databinding.FragmentCrearEquipsBinding
import copernic.cat.kingsleague.databinding.FragmentEliminarEquipsBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ModificarJugadors.newInstance] factory method to
 * create an instance of this fragment.
 */
class EliminarJugadors: Fragment() {
    private var _binding: FragmentEliminarEquipsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentEliminarEquipsBinding.inflate(inflater, container, false)

        binding.btnTornarEliminarEquips.setOnClickListener() {
            findNavController().navigate(R.id.action_eliminarJugadors_to_menuJugadors)
        }

        binding.eliminarEquip.setOnClickListener() {
            findNavController().navigate(R.id.action_eliminarJugadors_to_menuJugadors)
        }


        return binding.root
    }
}