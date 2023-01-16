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
class Classificacio : Fragment() {
    private var _binding: FragmentClassificacioBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentClassificacioBinding.inflate(inflater, container, false)
        auth = Firebase.auth
        binding.btnTornarClassificacio.setOnClickListener {
            findNavController().navigate(R.id.action_classificacio2_to_menuUsuari)
        }
        return binding.root
    }
}