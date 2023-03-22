package copernic.cat.kingsleague.ui.fragment.usuari

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import copernic.cat.kingsleague.R
import copernic.cat.kingsleague.databinding.FragmentMapsBinding
import copernic.cat.kingsleague.databinding.FragmentMapsUsuarisBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Maps.newInstance] factory method to
 * create an instance of this fragment.
 */
class MapsUsuaris : Fragment(), OnMapReadyCallback{
    // TODO: Rename and change types of parameters

    private var _binding: FragmentMapsUsuarisBinding? = null
    private val binding get() = _binding!!
    private var param1: String? = null
    private var param2: String? = null


    private lateinit var map: GoogleMap
    override fun onMapReady(googleMap: GoogleMap) {
        // creem una funció que es cridarà una vegada s'ha cargat el mapa
        // creem una variable de tipus googleMap
        map = googleMap

        //  cridem al metode
        createMarker()
    }
    private fun createMarker() {
        // posem les coordenades de la  localitzacio
        val maps = LatLng(41.3225857,2.1359805)

        // creem un Marker per marcar el lloc que hem seleccionat anteriorment i li posem un titol
        map.addMarker(MarkerOptions().position(maps).title("Kings League"))

        // un cop es carrega el  mapa fem zoom per ampliar el lloc i li diem el temps que
        // triga a fer zoom
        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(maps, 20f),
            1000,
            null
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMapsUsuarisBinding.inflate(inflater, container, false)
        binding.btnTornarGeolocalitzacio.setOnClickListener {
            findNavController().navigate(R.id.action_mapsUsuaris_to_configuracio2)
        }
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //  cridem al metode
        createMapFragment()
    }

    private fun createMapFragment() {
        // metode per crear el mapa
        // creem una variable del mateix tipus que el fragment map, SupportMapFragment
        val mapFragment =  childFragmentManager.findFragmentById(R.id.fragmentMap) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Maps.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MapsUsuaris().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}