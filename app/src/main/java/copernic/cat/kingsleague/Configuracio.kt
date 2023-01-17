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
import copernic.cat.kingsleague.databinding.FragmentConfiguracioAdminBinding
import copernic.cat.kingsleague.databinding.FragmentConfiguracioBinding
import copernic.cat.kingsleague.databinding.FragmentMenuAdminBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Configuracio.newInstance] factory method to
 * create an instance of this fragment.
 */
class Configuracio : Fragment() {
    private var _binding: FragmentConfiguracioBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentConfiguracioBinding.inflate(inflater, container, false)

        binding.btnFotoPerfil.setOnClickListener() {
            findNavController().navigate(R.id.action_configuracio2_to_fotoPerfil)
        }
        auth= Firebase.auth

        binding.btnTancarSessio.setOnClickListener() {
            auth.signOut()
            activity?.finish()        }


        binding.btnTornarConfiguracio.setOnClickListener() {
            findNavController().navigate(R.id.action_configuracio2_to_menuUsuari)
        }

        binding.btnGeolocalitzacio.setOnClickListener() {
            findNavController().navigate(R.id.action_configuracio2_to_mapsUsuaris)
        }
        return binding.root
    }
}