package copernic.cat.kingsleague

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import copernic.cat.kingsleague.databinding.FragmentClassificacioAdminBinding
import copernic.cat.kingsleague.databinding.FragmentClassificacioBinding
import copernic.cat.kingsleague.databinding.FragmentJugadorsBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Classificacio.newInstance] factory method to
 * create an instance of this fragment.
 */
class ClassificacioAdmin : Fragment() {
    private var _binding: FragmentClassificacioAdminBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentClassificacioAdminBinding.inflate(inflater, container, false)
        binding.btnTornarClassificacio.setOnClickListener {
            findNavController().navigate(R.id.action_classificacioAdmin_to_menu)
        }
        return binding.root
    }
}