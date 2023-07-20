package com.test.mini01_lbs01

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.UiSettings
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.test.mini01_lbs01.databinding.ActivityMainBinding
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    lateinit var activityMainBinding: ActivityMainBinding
    var googleMap : GoogleMap? = null

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener

    lateinit var currentLatLng: LatLng


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // SplashScreen
        installSplashScreen()

        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        // 구글지도
//        MapsInitializer.initialize(this,MapsInitializer.Renderer.LATEST,null)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                val currentLatLng = LatLng(location.latitude, location.longitude)
                googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
                locationManager.removeUpdates(this)

            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
        }

        activityMainBinding.run {
            toolbar.run {
                title= "구글지도"
                inflateMenu(R.menu.mune_main)
                setOnMenuItemClickListener {
                    when (it?.itemId) {
                        R.id.menu_current_location -> {
                            // 현재 위치 표시 메뉴를 선택했을 때 처리할 로직을 여기에 추가
                            showCurrentLocation()
                        }
                        R.id.menu_place -> {

                        }

                    }
                    false
                }
            }
        }

    }



    private fun requestNearbyPlaces(latitude: Double, longitude: Double) {
        Thread {
            try {


                val apiKey = "AIzaSyBf9jmJ_ZWwjK0SDqJcfPRZHihl7O2OxA8"
                val urlStr = "https://maps.googleapis.com/maps/api/place/nearbysearch/json" +
                        "?location=$latitude,$longitude" +
                        "&radius=50000" +
//                        "&type=restaurant" +
                        "&key=$apiKey"

                val url = URL(urlStr)

                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connectTimeout = 5000
                connection.readTimeout = 5000

                val inputStream = connection.inputStream
                val bufferedReader = BufferedReader(InputStreamReader(inputStream))
                val response = StringBuilder()

                bufferedReader.forEachLine {
                    response.append(it)
                }

                bufferedReader.close()
                connection.disconnect()

                // 받아온 JSON 응답 처리
                handleResponse(response.toString())


            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }

    private fun handleResponse(response: String) {
        val placesList = mutableListOf<PlaceData>()

        try {
            val jsonObject = JSONObject(response)
            if (jsonObject.has("results")) {
                val resultsArray = jsonObject.getJSONArray("results")
                for (i in 0 until resultsArray.length()) {
                    val placeObject = resultsArray.getJSONObject(i)
                    val name = placeObject.getString("name")
                    val locationObject = placeObject.getJSONObject("geometry").getJSONObject("location")
                    val lat = locationObject.getDouble("lat")
                    val lng = locationObject.getDouble("lng")
                    placesList.add(PlaceData(name, lat, lng))
                }
            }

            // 받아온 장소 정보를 지도에 마커로 표시
            runOnUiThread {

                    Toast.makeText(this,placesList.toString(),Toast.LENGTH_SHORT).show()

                for (place in placesList) {
                    val latLng = LatLng(place.latitude, place.longitude)
                    googleMap?.addMarker(MarkerOptions().position(latLng).title(place.name))
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showCurrentLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // 위치 권한이 허용되지 않은 경우, 권한 요청
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
            return
        }

        // 현재 위치를 가져와 지도를 해당 위치로 이동
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                currentLatLng = LatLng(location.latitude, location.longitude)
                googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))

                requestNearbyPlaces(location.latitude, location.longitude)
            }
        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        // 위치 권한 확인 및 요청
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
            return
        }

        // 현재 위치를 표시하는 레이어를 활성화
        googleMap!!.isMyLocationEnabled = true
        val uiSetting: UiSettings = googleMap!!.uiSettings
        uiSetting.isZoomControlsEnabled = true

        // 사용자의 현재 위치 요청
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            0L,
            0f,
            locationListener
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 권한이 허용된 경우 지도 초기화
                val mapFragment = supportFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
                mapFragment.getMapAsync(this)
            }
        }
    }

    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 1001
    }

}
data class PlaceData(val name: String, val latitude: Double, val longitude: Double)