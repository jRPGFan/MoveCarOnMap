package com.example.movecaronmap.utils

import android.graphics.Color
import android.os.Handler
import android.os.SystemClock
import android.view.animation.LinearInterpolator
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.PolylineOptions
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

object MapUtils {
    fun getRoute() : List<LatLng>? {
//        if (markers.size < 2) {
//            Toast.makeText(requireContext(), "Place a marker first!", Toast.LENGTH_SHORT).show()
//            return null
//        }

        return buildMockRoute()

//        val url = getURL(markers[0].position, markers[markers.size - 1].position)
//        var decodedPoints: List<LatLng>? = null
//
//        GlobalScope.launch(context = Dispatchers.IO) {
//            val result = URL(url).readText()
//            val parser = Parser()
//            val stringBuilder = StringBuilder(result)
//            val json: JsonObject = parser.parse(stringBuilder) as JsonObject
//            val routes = json.array<JsonObject>("routes")
//            val points = routes!!["legs"]["steps"][0] as JsonArray<JsonObject>
//            val polylinePoints = points.flatMap {
//                PolyUtil.decode(it.obj("polyline")?.string("points")!!) }
//            decodedPoints = polylinePoints
//        }
//
//        return decodedPoints
    }

    private fun buildMockRoute() : List<LatLng> {
        val routePoints: ArrayList<LatLng> = arrayListOf()
        routePoints.add(LatLng(47.02278994619585,28.83524928241968))
        routePoints.add(LatLng(47.02367355268536,28.83392695337534))
        routePoints.add(LatLng(47.026712581093754,28.838206753134724))
        routePoints.add(LatLng(47.02861951335688,28.83531834930181))
        routePoints.add(LatLng(47.02559544914988,28.831015750765797))
        routePoints.add(LatLng(47.02278994619585,28.83524928241968))
        return routePoints
    }

//    fun getURL(from: LatLng, to: LatLng): String {
//        val originationPoint = "origin" + from.latitude + "," + from.longitude
//        val destinationPoint = "destination" + to.latitude + "," + to.longitude
//        val sensor = "sensor=false"
//        val queryParams = "$originationPoint&$destinationPoint&$sensor"
//        return "https://maps.googleapis.com/maps/api/directions/json?$queryParams"
//    }

    fun getTurningAngle(latLng1: LatLng, latLng2: LatLng): Float {
        val PI = 3.14159
        val lat1: Double = latLng1.latitude * PI / 180
        val long1: Double = latLng1.longitude * PI / 180
        val lat2: Double = latLng2.latitude * PI / 180
        val long2: Double = latLng2.longitude * PI / 180
        val dLon = long2 - long1
        val y = sin(dLon) * cos(lat2)
        val x = cos(lat1) * sin(lat2) - (sin(lat1) * cos(lat2) * cos(dLon))
        var rotation = atan2(y, x)
        rotation = Math.toDegrees(rotation)
        rotation = (rotation + 360) % 360
        return rotation.toFloat()
    }

//    private lateinit var runnable: Runnable
//
//    fun rotateMarker(carMarker: Marker, latLng1: LatLng, latLng2: LatLng) {
//        val handler = Handler()
//        var start = SystemClock.uptimeMillis()
//        val duration = 2000
//        val interpolator = LinearInterpolator()
//        val PI = 3.14159
//        val lat1: Double = latLng1.latitude * PI / 180
//        val long1: Double = latLng1.longitude * PI / 180
//        val lat2: Double = latLng2.latitude * PI / 180
//        val long2: Double = latLng2.longitude * PI / 180
//        val dLon = long2 - long1
//        val y = sin(dLon) * cos(lat2)
//        val x = cos(lat1) * sin(lat2) - (sin(lat1) * cos(lat2) * cos(dLon))
//        var rotation = atan2(y, x)
//        rotation = Math.toDegrees(rotation)
//        rotation = (rotation + 360) % 360
//
//        runnable = Runnable {
//            run {
//                var elapsed = SystemClock.uptimeMillis() - start
//                var t = interpolator.getInterpolation((elapsed/duration).toFloat())
//                carMarker.rotation =  (if (-rotation > 180) (rotation / 2).toFloat() else rotation.toFloat())
//
//                if (t < 1.0) {
//                    handler.postDelayed(runnable, 16)
//                }
//            }
//        }
//    }
}