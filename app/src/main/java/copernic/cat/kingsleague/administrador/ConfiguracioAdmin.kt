package copernic.cat.kingsleague.administrador

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
import androidx.navigation.fragment.findNavController
import copernic.cat.kingsleague.R

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
class ConfiguracioAdmin : Fragment() {
    private var _binding: FragmentConfiguracioAdminBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentConfiguracioAdminBinding.inflate(inflater, container, false)

        binding.btnFotoPerfil.setOnClickListener() {
            findNavController().navigate(R.id.action_configuracioAdmin_to_fotoPerfilAdmin)
        }
        auth= Firebase.auth
        binding.btnTancarSessio.setOnClickListener() {
            auth.signOut()
            activity?.finish()
        }


        binding.btnTornarConfiguracio.setOnClickListener() {
            findNavController().navigate(R.id.action_configuracioAdmin_to_menu)
        }

        binding.btnGeolocalitzacio.setOnClickListener() {
            findNavController().navigate(R.id.action_configuracioAdmin_to_maps)
        }
        return binding.root
    }
}