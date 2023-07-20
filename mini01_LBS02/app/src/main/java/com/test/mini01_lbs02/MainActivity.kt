package com.test.mini01_lbs02

import android.Manifest
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.test.mini01_lbs02.databinding.ActivityMainBinding
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    lateinit var activityMainBinding: ActivityMainBinding

    // 승인받을 권한 목록
    val permissionList = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    // 위치 측정 리스너
    var myLocationListener: LocationListener? = null

    // 구글 지도 객체
    lateinit var mainGoogleMap : GoogleMap

    // 현재 사용자 위치에 표시되는 마커
    var myMarker : Marker? = null

    // 구글지도 주변검색 타입 리스트
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
        "transit_station", "travel_agency", "university", "eterinary_care","zoo"
    )

    // 사용자의 현재 위치
    lateinit var userLocation:Location

    // 데이터 리스트
    // 데이터를 담을 리스트
    val latitudeList = mutableListOf<Double>()
    val longitutdeList = mutableListOf<Double>()
    val nameList = mutableListOf<String>()
    val vicinityList = mutableListOf<String>()
    val markerList = mutableListOf<Marker>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // SplashScreen
        installSplashScreen()

        // 구글 지도 세팅
        MapsInitializer.initialize(this, MapsInitializer.Renderer.LATEST,null)

        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        activityMainBinding.run {
            toolbarMain.run {
                title = "LBSProject"
                inflateMenu(R.menu.main_menu)
                setOnMenuItemClickListener {
                    when(it?.itemId){
                        // 현재 위치 메뉴
                        R.id.main_manu_location -> {
                            // 현재 위치를 측정하고 지도 갱신
                            getMyLocation()
                        }
                        // 장소 종류 선택
                        R.id.main_menu_type -> {
                            val builder = AlertDialog.Builder(this@MainActivity)
                            builder.setTitle("장소 종류 선택")
                            builder.setNegativeButton("취소",null)
                            builder.setNeutralButton("초기화"){ dialogInterface: DialogInterface, i: Int ->
                                // 데이터 리스트 초기화
                                latitudeList.clear()
                                longitutdeList.clear()
                                nameList.clear()
                                vicinityList.clear()
                                for(marker in markerList){
                                    marker.remove()
                                }
                                markerList.clear()
                            }
                            builder.setItems(dialogData){ dialogInterface: DialogInterface, i: Int ->
                                thread {
                                    // 접속할 주소
                                    val location = "${userLocation.latitude},${userLocation.longitude}"
                                    val radius = 50000
                                    val language = "ko"
                                    val type = dialogData[i]
                                    val key = "AIzaSyDPBax2_VK5F9EyoPpwvP19e94iSNTFRrc"
                                    val site = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=${location}&radius=${radius}&language=${language}&type=${type}&key=${key}"
//                                    Log.d("lbs app", site)


                                    runOnUiThread {
                                        // 데이터 리스트 초기화
                                        // 데이터를 담을 리스트
                                        latitudeList.clear()
                                        longitutdeList.clear()
                                        nameList.clear()
                                        vicinityList.clear()
                                        for(marker in markerList){
                                            marker.remove()
                                        }
                                        markerList.clear()
                                    }

                                    // 다음 페이지의 토큰을 담을 변수
                                    var nextToken:String? = null

                                    do {
//                                        SystemClock.sleep(1000)
                                        // 만약 nextToken이 null 아니라면 주소 뒤에 붙혀준다
                                        val site2 = if(nextToken != null){
                                            "${site}&pagetoken=${nextToken}"
                                        } else{
                                            site
                                        }

                                        // 요청
                                        val url = URL(site2)
                                        val httpURLConnection = url.openConnection() as HttpURLConnection
                                        val inputStreamReader = InputStreamReader(httpURLConnection.inputStream)
                                        val bufferedReader = BufferedReader(inputStreamReader)

                                        // 문자열 데이터를 받아온다.
                                        var str:String? = null
                                        val stringBuffer = StringBuffer()

                                        do{
                                            str = bufferedReader.readLine()
                                            if(str != null){
                                                stringBuffer.append(str)
                                            }
                                        } while (str != null)

                                        val data = stringBuffer.toString()
//                                    Log.d("lbs app", data)

                                        // JSON 데이터 분석
                                        // JSON Object
                                        val root = JSONObject(data)
                                        // status 데이터
                                        val status = root.getString("status")
                                        // status가 ok일 경우에 실행
                                        if(status == "OK"){
                                            val resultsArray = root.getJSONArray("results")

                                            // jsonArray가 관리하는 JSONObject 수 만큼 반복
                                            for (idx in 0 until resultsArray.length()) {
                                                // idx 번째 JSONObject 추출
                                                val resultsObject = resultsArray.getJSONObject(idx)
                                                // 위도, 경도
                                                val locationObject = resultsObject.getJSONObject("geometry").getJSONObject("location")
                                                val lat = locationObject.getDouble("lat")
                                                val lng = locationObject.getDouble("lng")
                                                // 이름
                                                val name = resultsObject.getString("name")
                                                // 주소
                                                val vicinity = resultsObject.getString("vicinity")
//                                                Log.d("map app",lat.toString())
//                                                Log.d("map app",lng.toString())
//                                                Log.d("map app",name)
//                                                Log.d("map app",vicinity)
//                                                Log.d("map app","-------------------------------------------")

                                                // 데이터 담기
                                                latitudeList.add(lat)
                                                longitutdeList.add(lng)
                                                nameList.add(name)
                                                vicinityList.add(vicinity)
                                            }
                                        }

                                        // nextToken 값이 있다면
                                        if(root.has("next_page_token")){
                                            nextToken = root.getString("next_page_token")
                                        } else{
                                            nextToken = null
                                        }

                                    } while(nextToken != null)

                                    runOnUiThread {
                                        // 지도에 마커 표시
                                        for(idx in 0 until latitudeList.size){
                                            val markerOptions = MarkerOptions()
                                            val loc = LatLng(latitudeList[idx], longitutdeList[idx])
                                            markerOptions.position(loc)
                                            markerOptions.title(nameList[idx])
                                            markerOptions.snippet(vicinityList[idx])

                                            val marker = mainGoogleMap.addMarker(markerOptions)
                                            markerList.add(marker!!)
                                        }
                                    }

                                }
                            }
                            builder.show()
                        }
                    }
                    false
                }
            }
        }

        // 권한을 확인한다.
        requestPermissions(permissionList, 0)

        // 구글 지도를 보여주는 MapFragment 객체
        val supportMapFragment = supportFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        // 구글 지도 사용 준비가 완료되면 반응하는 리스너
        supportMapFragment.getMapAsync {
            // Toast.makeText(this,"구글 지도가 준비되었습니다", Toast.LENGTH_SHORT).show()

            // 구글맵 객체 변수에 담기
            mainGoogleMap = it

            // 구글맵 옵션 설정
            // 확대/축소 버튼
            it.uiSettings.isZoomControlsEnabled = true

            // 현재 위치 표시
            // it.isMyLocationEnabled = true

            // 현재 위치를 표시하는 버튼을 없앤다.
            // it.uiSettings.isMyLocationButtonEnabled = false

            // 구글맵 타입
            // 지도 안나오게
            // it.mapType = GoogleMap.MAP_TYPE_NONE
            // 기본
            // it.mapType = GoogleMap.MAP_TYPE_NORMAL
            // 지형지도
            // it.mapType = GoogleMap.MAP_TYPE_TERRAIN
            // 위성지도
            // it.mapType = GoogleMap.MAP_TYPE_SATELLITE
            // 위성지도에 지역표시까지
            // it.mapType = GoogleMap.MAP_TYPE_HYBRID

            // 위치 정보를 관리하는 객체
            val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

            // 권한 확인
            val a1 = ActivityCompat.checkSelfPermission(this@MainActivity,Manifest.permission.ACCESS_FINE_LOCATION)
            val a2 = ActivityCompat.checkSelfPermission(this@MainActivity,Manifest.permission.ACCESS_COARSE_LOCATION)
            if(a1 == PackageManager.PERMISSION_GRANTED && a2 == PackageManager.PERMISSION_GRANTED){
                // 현재 저장되어 있는 위치 정보값을 가지고온다.
                val location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                val location2 = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

                // 현재 위치를 표시한다.
                if(location1 != null){
                    setMyLocation(location1)
                } else if(location2 != null){
                    setMyLocation(location2)
                }

                // 현재 위치를 측정하여 지도를 갱신한다
                getMyLocation()
            }
        }
    }

    // 매개변수로 들어오는 위도 경도값을 통해 구글 지도를 해당 위치로 이동시킨다.
    fun setMyLocation(location: Location){
        // 측정된 사용자의 현재 위치 담기
        userLocation = location
        // 위치 측정 중단
        if(myLocationListener != null){
            val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
            locationManager.removeUpdates(myLocationListener!!)
            myLocationListener = null
        }
//        Toast.makeText(this,"위도 : ${location.latitude}, 경도 : ${location.longitude}", Toast.LENGTH_SHORT).show()

        // 위도 경도를 관리하는 객체
        val latLng = LatLng(location.latitude, location.longitude)

        // 지도를 이동시키는 객체
        // val cameraUpdate = CameraUpdateFactory.newLatLng(latLng)
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15f)

        // 지도 이동
        // mainGoogleMap.moveCamera(cameraUpdate)
        mainGoogleMap.animateCamera(cameraUpdate)

        // 현재 위치 마커 표시
        val markerOptions = MarkerOptions()
        markerOptions.position(latLng)

        // 마커 이미지 변경
//        val markerBitmap = BitmapDescriptorFactory.fromResource(R.drawable.person_pin_circle)
//        markerOptions.icon(markerBitmap)

        // 기존에 표시된 마커 제거
        if(myMarker != null){
            myMarker?.remove()
            myMarker = null
        }

        myMarker = mainGoogleMap.addMarker(markerOptions)
    }

    fun getMyLocation(){
        // 권한 확인
        val a1 = ActivityCompat.checkSelfPermission(this@MainActivity,Manifest.permission.ACCESS_FINE_LOCATION)
        val a2 = ActivityCompat.checkSelfPermission(this@MainActivity,Manifest.permission.ACCESS_COARSE_LOCATION)
        if(a1 == PackageManager.PERMISSION_GRANTED && a2 == PackageManager.PERMISSION_GRANTED){

            val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

            // 위치 측정 리스너
            myLocationListener = object : LocationListener{
                override fun onLocationChanged(p0: Location) {
                    setMyLocation(p0)
                }
            }

            // 위치 측정 요청
            if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)== true){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0f,myLocationListener!!)
            }

//            if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) == true){
//                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, myLocationListener!!)
//            }

        }
    }
}