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

class MapsUsuaris : Fragment(), OnMapReadyCallback {
    // TODO: Rename and change types of parameters

    private var _binding: FragmentMapsUsuarisBinding? = null
    private val binding get() = _binding!!


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
        val maps = LatLng(41.3225857, 2.1359805)

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

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMapsUsuarisBinding.inflate(inflater, container, false)
        binding.btnTornarGeolocalitzacio.setOnClickListener {
            findNavController().navigate(R.id.action_mapsUsuaris_to_menuUsuari)
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
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.fragmentMap) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }
}