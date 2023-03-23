package copernic.cat.kingsleague

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.firestore.FirebaseFirestore
import copernic.cat.kingsleague.databinding.FragmentEquipsSafeArgsBinding
import copernic.cat.kingsleague.databinding.FragmentJugadorsAdminBinding
import copernic.cat.kingsleague.ui.Utils
import copernic.cat.kingsleague.ui.fragment.administrador.ClassificacioAdminDirections
import copernic.cat.kingsleague.ui.fragment.usuari.ClassificacioDirections

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EquipsSafeArgs.newInstance] factory method to
 * create an instance of this fragment.
 */
class EquipsSafeArgs : Fragment() {
    private var _binding: FragmentEquipsSafeArgsBinding? = null
    private val binding get() = _binding!!
    val bd = FirebaseFirestore.getInstance()
    private val utils = Utils()
    private val args: EquipsSafeArgsArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEquipsSafeArgsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnTornarEquipsSafeArgs.setOnClickListener {
            var ola = bd.collection("Usuaris").document(utils.getCorreoUserActural())
            ola.get().addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.getBoolean("admin") == true) {


                    view.findNavController()
                        .navigate(R.id.action_equipsSafeArgs_to_classificacioAdmin)

                } else {
                    view.findNavController().navigate(R.id.action_equipsSafeArgs2_to_classificacio2)

                }
            }
        }
    }
}