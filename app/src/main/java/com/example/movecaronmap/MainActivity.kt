package com.example.movecaronmap

import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.movecaronmap.utils.AnimationUtils
import com.example.movecaronmap.utils.MapUtils
import com.example.movecaronmap.utils.MapUtils.getRoute
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*

class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var googleMap: GoogleMap
    private lateinit var defaultLocation: LatLng
    private val markers: ArrayList<Marker> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun moveCamera(latLng: LatLng) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
    }

    private fun animateCamera(latLng: LatLng) {
        val cameraPosition = CameraPosition.Builder().target(latLng).zoom(15f).build()
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    private fun showDefaultLocation(latLng: LatLng) {
        moveCamera(latLng)
        animateCamera(latLng)
        //setMarker(latLng, "Chișinău", R.drawable.ic_map_pin)
    }

    private fun setMarker(
        location: LatLng,
        searchText: String,
        resourceId: Int
    ): Marker? {
        return googleMap.addMarker(
            MarkerOptions()
                .position(location)
                .title(searchText)
                .icon(BitmapDescriptorFactory.fromResource(resourceId))
        )
    }

    private fun addMarkerToArray(location: LatLng) {
        val marker = setMarker(location, markers.size.toString(), R.drawable.ic_map_pin)
        marker?.let { markers.add(it) }
    }

    private fun addMarkersToArray(routeList: List<LatLng>) {
        for (routePoint in routeList)
            addMarkerToArray(LatLng(routePoint.latitude, routePoint.longitude))
    }

    private fun drawRoute(route: List<LatLng>) {
//        val builder = LatLngBounds.Builder()
//        for (latLng in route) {
//            builder.include(latLng)
//        }
//        val bounds = builder.build()
//        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 2))

        val options = PolylineOptions()
            .color(Color.MAGENTA)
            .width(5f)
            .addAll(route)
        googleMap.addPolyline(options)
    }

    private var carMarker: Marker? = null
    private var previousLatLng: LatLng? = null
    private var currentLatLng: LatLng? = null

    fun updateCarLocation(latLng: LatLng) {
        if (carMarker == null) {
            val bitmapOptions = BitmapFactory.Options()
//            bitmapOptions.inSampleSize = 2
            carMarker = googleMap.addMarker(
                MarkerOptions().position(latLng)
                    .icon(BitmapDescriptorFactory.fromBitmap(
                            BitmapFactory.decodeResource(
                                resources, R.drawable.car, bitmapOptions))))
        }

        if (previousLatLng == null) {
            currentLatLng = latLng
            previousLatLng = currentLatLng
            carMarker?.position = currentLatLng!!
            carMarker?.setAnchor(0.5f, 0.5f)
            animateCamera(currentLatLng!!)
        } else {
            previousLatLng = currentLatLng
            currentLatLng = latLng
            val valueAnimator = AnimationUtils.carAnimator()
            valueAnimator.addUpdateListener { valAnimator ->
                if (currentLatLng != null && previousLatLng != null) {
                    val multiplier = valAnimator.animatedFraction
                    val nextLocation = LatLng(
                        multiplier * currentLatLng!!.latitude + (1 - multiplier) * previousLatLng!!.latitude,
                        multiplier * currentLatLng!!.longitude + (1 - multiplier) * previousLatLng!!.longitude
                            )
                    carMarker?.position = nextLocation
//                    rotateMarker(carMarker!!, previousLatLng!!,nextLocation)
                    val turningAngle = MapUtils.getTurningAngle(previousLatLng!!,nextLocation)
                    if (!turningAngle.isNaN())
                        carMarker?.rotation = (if (-turningAngle > 180) turningAngle / 2 else turningAngle)
                    carMarker?.setAnchor(0.5f, 0.5f)
                    animateCamera(nextLocation)
                }
            }
            valueAnimator.start()
        }
    }

    private lateinit var handler: Handler
    private lateinit var runnable: Runnable

    private fun moveCar(route: List<LatLng>) {
        handler = Handler()
        var index = 0
        runnable = Runnable {
            run {
                if (index < route.size) {
                    updateCarLocation(route[index])
                    handler.postDelayed(runnable, 3000)
                    ++index
                } else {
                    handler.removeCallbacks(runnable)
                }
            }
        }
        handler.postDelayed(runnable, 5000)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        defaultLocation = LatLng(47.022778, 28.835278)
        showDefaultLocation(defaultLocation)
        val route = getRoute()
        drawRoute(route!!)
        addMarkersToArray(route)

        Handler().postDelayed(Runnable {
            moveCar(route)
        }, 2000)
    }
}