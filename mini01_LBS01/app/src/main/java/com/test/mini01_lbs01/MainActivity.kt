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
import androidx.appcompat.app.AlertDialog
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
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.test.mini01_lbs01.databinding.ActivityMainBinding
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    lateinit var activityMainBinding: ActivityMainBinding
    var googleMap: GoogleMap? = null

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener

    lateinit var currentLatLng: LatLng

    val dialogData = arrayOf(
        "accounting", "airport", "amusement_park",
        "aquarium", "art_gallery", "atm", "bakery",
        "bank", "bar", "beauty_salon", "bicycle_store",
        "book_store", "bowling_alley", "bus_station",
        "cafe", "campground", "car_dealer", "car_rental",
        "car_repair", "car_wash", "casino", "cemetery",
        "church", "city_hall", "clothing_store", "convenience_store",
        "courthouse", "dentist", "department_store", "doctor",
        "drugstore", "electrician", "electronics_store", "embassy",
        "fire_station", "florist", "funeral_home", "furniture_store",
        "gas_station", "gym", "hair_care", "hardware_store", "hindu_temple",
        "home_goods_store", "hospital", "insurance_agency",
        "jewelry_store", "laundry", "lawyer", "library", "light_rail_station",
        "liquor_store", "local_government_office", "locksmith", "lodging",
        "meal_delivery", "meal_takeaway", "mosque", "movie_rental", "movie_theater",
        "moving_company", "museum", "night_club", "painter", "park", "parking",
        "pet_store", "pharmacy", "physiotherapist", "plumber", "police", "post_office",
        "primary_school", "real_estate_agency", "restaurant", "roofing_contractor",
        "rv_park", "school", "secondary_school", "shoe_store", "shopping_mall",
        "spa", "stadium", "storage", "store", "subway_station", "supermarket",
        "synagogue", "taxi_stand", "tourist_attraction", "train_station",
        "transit_station", "travel_agency", "university", "eterinary_care", "zoo"
    )

    // 마커들을 관리하는 리스트
    private val markersList: MutableList<Marker> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // SplashScreen
        installSplashScreen()

        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        // 구글지도
//        MapsInitializer.initialize(this,MapsInitializer.Renderer.LATEST,null)

        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                val currentLatLng = LatLng(location.latitude, location.longitude)
                googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
                locationManager.removeUpdates(this)

            }
//            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
        }

        activityMainBinding.run {
            toolbar.run {
                title = "구글지도"
                inflateMenu(R.menu.mune_main)
                setOnMenuItemClickListener {
                    when (it?.itemId) {
                        R.id.menu_current_location -> {
                            // 현재 위치 표시 메뉴를 선택했을 때 처리할 로직을 여기에 추가
                            showCurrentLocation()
                        }
                        R.id.menu_place -> {
                            // 주변 장소 검색 타입 목록을 표시
                            showPlaceDialog()
                            // 이벤트 처리가 완료되었음을 알리기 위해 true를 반환.
                            return@setOnMenuItemClickListener true
                        }
                    }
                    false
                }
            }
        }
    }

    private fun requestNearbyPlaces(placeType: String) {
        Thread {
            try {
                val apiKey = "AIzaSyBf9jmJ_ZWwjK0SDqJcfPRZHihl7O2OxA8"
                val urlStr = "https://maps.googleapis.com/maps/api/place/nearbysearch/json" +
                        "?location=${currentLatLng.latitude},${currentLatLng.longitude}" +
                        "&radius=50000" +
                        "&type=$placeType" +
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
        // 기존 마커 제거
        for (marker in markersList) {
            marker.remove()
        }
        markersList.clear()
        val placesList = mutableListOf<PlaceData>()

        try {
            val jsonObject = JSONObject(response)
            if (jsonObject.has("results")) {
                val resultsArray = jsonObject.getJSONArray("results")
                for (i in 0 until resultsArray.length()) {
                    val placeObject = resultsArray.getJSONObject(i)
                    val name = placeObject.getString("name")
                    val locationObject =
                        placeObject.getJSONObject("geometry").getJSONObject("location")
                    val lat = locationObject.getDouble("lat")
                    val lng = locationObject.getDouble("lng")
                    placesList.add(PlaceData(name, lat, lng))
                }
            }

            // 받아온 장소 정보를 지도에 마커로 표시
            runOnUiThread {
                for (place in placesList) {
                    val latLng = LatLng(place.latitude, place.longitude)
                    val marker = googleMap?.addMarker(MarkerOptions().position(latLng).title(place.name))
                    if (marker != null) {
                        markersList.add(marker)
                    }
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // 영어 카테고리 이름에 대응하는 한글 카테고리 이름을 반환하는 함수
    private fun getKoreanType(type: String): String {
        return when (type) {
            "accounting" -> "회계사무소"
            "airport" -> "공항"
            "amusement_park" -> "놀이공원"
            "aquarium" -> "수족관"
            "art_gallery" -> "미술관"
            "atm" -> "ATM기계"
            "bakery" -> "빵집"
            "bank" -> "은행"
            "bar" -> "주점"
            "beauty_salon" -> "미용실"
            "bicycle_store" -> "자전거 가게"
            "book_store" -> "서점"
            "bowling_alley" -> "볼링장"
            "bus_station" -> "버스정류장"
            "cafe" -> "카페"
            "campground" -> "야영장"
            "car_dealer" -> "자동차 판매점"
            "car_rental" -> "렌터카"
            "car_repair" -> "카센터"
            "car_wash" -> "세차장"
            "casino" -> "카지노"
            "cemetery" -> "묘지"
            "church" -> "교회"
            "city_hall" -> "시청"
            "clothing_store" -> "옷가게"
            "convenience_store" -> "편의점"
            "courthouse" -> "법원"
            "dentist" -> "치과"
            "department_store" -> "백화점"
            "doctor" -> "병원"
            "drugstore" -> "약국"
            "electrician" -> "철물점"
            "electronics_store" -> "전자제품 가게"
            "embassy" -> "대사관"
            "fire_station" -> "소방서"
            "florist" -> "꽃집"
            "funeral_home" -> "장의사"
            "furniture_store" -> "가구판매점"
            "gas_station" -> "주유소"
            "gym" -> "헬스장"
            "hair_care" -> "미용실"
            "hardware_store" -> "철물점"
            "hindu_temple" -> "힌두사원"
            "home_goods_store" -> "가정용품점"
            "hospital" -> "병원"
            "insurance_agency" -> "보험회사"
            "jewelry_store" -> "보석점"
            "laundry" -> "세탁소"
            "lawyer" -> "변호사"
            "library" -> "도서관"
            "light_rail_station" -> "경전철역"
            "liquor_store" -> "주류판매점"
            "local_government_office" -> "지방 정부 사무소"
            "locksmith" -> "자물쇠장"
            "lodging" -> "숙박 시설"
            "meal_delivery" -> "식사 배달"
            "meal_takeaway" -> "식사 포장"
            "mosque" -> "모스크"
            "movie_rental" -> "비디오 대여"
            "movie_theater" -> "영화관"
            "moving_company" -> "이사짐 센터"
            "museum" -> "박물관"
            "night_club" -> "나이트 클럽"
            "painter" -> "화가"
            "park" -> "공원"
            "parking" -> "주차장"
            "pet_store" -> "애완동물 가게"
            "pharmacy" -> "약국"
            "physiotherapist" -> "물리치료사"
            "plumber" -> "배관공"
            "police" -> "경찰서"
            "post_office" -> "우체국"
            "primary_school" -> "초등학교"
            "real_estate_agency" -> "부동산 중개사"
            "restaurant" -> "음식점"
            "roofing_contractor" -> "지붕 공사자"
            "rv_park" -> "RV 파크"
            "school" -> "학교"
            "secondary_school" -> "중학교"
            "shoe_store" -> "신발가게"
            "shopping_mall" -> "쇼핑몰"
            "spa" -> "스파"
            "stadium" -> "경기장"
            "storage" -> "창고"
            "store" -> "가게"
            "subway_station" -> "지하철역"
            "supermarket" -> "슈퍼마켓"
            "synagogue" -> "유대교회"
            "taxi_stand" -> "택시 승차장"
            "tourist_attraction" -> "관광 명소"
            "train_station" -> "기차역"
            "transit_station" -> "환승역"
            "travel_agency" -> "여행사"
            "university" -> "대학교"
            "veterinary_care" -> "동물 병원"
            "zoo" -> "동물원"
            else -> type
        }
    }

    private fun showPlaceDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("검색 장소 선택")

        // 영어로 된 타입 목록을 한글로 변환하여 보여줌
        val dialogDataKorean = Array(dialogData.size) { index ->
            // for 루프를 돌면서 dialogData 리스트의 각 항목에 접근합니다.
            val englishType = dialogData[index] // 영어로 된 타입
            val koreanType = getKoreanType(englishType) // 영어 타입을 한글로 변환
            // 변환된 한글 카테고리를 dialogDataKorean 배열에 저장합니다.
            koreanType
        }

        builder.setItems(dialogDataKorean) { _, i ->
            val selectedPlace = dialogData[i]
            // 선택된 장소 타입 주변 장소 검색
            requestNearbyPlaces(selectedPlace)
        }
        val dialog = builder.create()
        dialog.show()
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

//                requestNearbyPlaces(location.latitude, location.longitude)
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
                val mapFragment =
                    supportFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
                mapFragment.getMapAsync(this)
            }
        }
    }

    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 1001
    }
}

data class PlaceData(val name: String, val latitude: Double, val longitude: Double)