package kumarworld.rahul.checklist

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main_form.*
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
private const val PERMISSION_REQUEST = 10
class MainActivity : AppCompatActivity() {
    val mChkID:ArrayList<String> =ArrayList()
    val mChkName:ArrayList<String> =ArrayList()

    val mProjectName:ArrayList<String> = ArrayList()
    val mProjectId:ArrayList<String> = ArrayList()

    val mBuildingName:ArrayList<String> = ArrayList()
    val mBuildingID:ArrayList<String> = ArrayList()

    var chkID=""
    var proID=""
    var buildID=""
    var userID=""

    var addressss=""


    lateinit var locationManager: LocationManager
    private var hasGps = false
    private var hasNetwork = false
    private var locationGps: Location? = null
    private var locationNetwork: Location? = null

    private var permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_form)
        getAllProject()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermission(permissions)) {
                // enableView()
                getLocation()
            } else {
                requestPermissions(permissions, PERMISSION_REQUEST)
            }
        } else {
            getLocation()
            // enableView()
        }
        userID=intent.getStringExtra("userID")

        //getBuilding("3")
        getCheckList()



        spinnerChecklist.setOnClickListener {
            val array = arrayOfNulls<String>(mChkName.size)
            mChkName.toArray(array)

            val builder = AlertDialog.Builder(this)


            // Set a title for alert dialog
            builder.setTitle("Select Check List.")
            builder.setItems(array,{_, which ->
                val selected = mChkName[which]
                try {
                    spinnerChecklist.text=selected
                    chkID=mChkID[which] as String
                  //  Toast.makeText(applicationContext, "CHK ID"+chkID, Toast.LENGTH_LONG).show()
                   // getProject(cityIDss,locationID)
                }catch (e:IllegalArgumentException){

                }
            })

            val dialog = builder.create()
            dialog.show()
        }


        spinnerProjectName.setOnClickListener(){
            mBuildingName.clear()
            mBuildingID.clear()
            buildID=""
            spinnerBuildingNo.text=""
            val array = arrayOfNulls<String>(mProjectName.size)
            mProjectName.toArray(array)

            val builder = AlertDialog.Builder(this)


            // Set a title for alert dialog
            builder.setTitle("Select Project.")
            builder.setItems(array,{_, which ->
                val selected = mProjectName[which]
                try {
                    spinnerProjectName.text=selected
                     proID=mProjectId[which] as String
                   // Toast.makeText(applicationContext, "Project  ID  - "+proID, Toast.LENGTH_LONG).show()
                     getBuilding(proID)
                }catch (e:IllegalArgumentException){

                }
            })

            val dialog = builder.create()
            dialog.show()
        }


        spinnerBuildingNo.setOnClickListener(){
            val array = arrayOfNulls<String>(mBuildingName.size)
            mBuildingName.toArray(array)

            val builder = AlertDialog.Builder(this)


            // Set a title for alert dialog
            builder.setTitle("Select Building.")
            builder.setItems(array,{_, which ->
                val selected = mBuildingName[which]
                try {
                    spinnerBuildingNo.text=selected
                     buildID=mBuildingID[which] as String
                  //  Toast.makeText(applicationContext, "Building  ID  - "+buildID, Toast.LENGTH_LONG).show()
                   // getBuilding(projectID)
                }catch (e:IllegalArgumentException){

                }
            })

            val dialog = builder.create()
            dialog.show()
        }

        val sdf = SimpleDateFormat("dd/MMM/yyyy hh:mm a")
        edtDate.text = sdf.format(Date())




        btnNext.setOnClickListener {
            if(chkID!=""&& proID!=""&& buildID!=""&& edtFlatNo.text.toString()!=""){
                var intentNext = Intent(this, Activity_Form::class.java)
                intentNext.putExtra("chk_id", chkID)
                intentNext.putExtra("pro_id", proID)
                intentNext.putExtra("build_id", buildID)
                intentNext.putExtra("dra_ref", edtDrawingRef.text.toString())
                intentNext.putExtra("flat_no", edtFlatNo.text.toString())
                intentNext.putExtra("user_id", userID)
                intentNext.putExtra("date", edtDate.text.toString())
                intentNext.putExtra("addressss",addressss)


                startActivity(intentNext)

            }else {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Megapolis")
                builder.setMessage("Enter All Data")
                builder.setPositiveButton("OK"){dialog, which ->
                    dialog.dismiss()
                }
                val dialog: AlertDialog = builder.create()
                dialog.show()
            }
        }

    }

    fun getAllProject(){
        var url="https://app.megapolis.co.in/all_projects"
        var queue= Volley.newRequestQueue(this)
        var stringRequest=object : StringRequest(
            Request.Method.GET,url,
            Response.Listener { response ->

                Log.e("FFFFFFFFFFFFFFF",""+response)

                var strResp = response.toString()
                val jsonObj: JSONObject = JSONObject(strResp)
                var status=jsonObj.getInt("status")
                if (status==200){
                    val jsonArray: JSONArray = jsonObj.getJSONArray("data")
                    for (i in 0 until jsonArray.length()) {
                        var jsonInner: JSONObject = jsonArray.getJSONObject(i)
                        var id = jsonInner.getString("id")
                        var project_name = jsonInner.getString("project_name")
                        mProjectId.add(id)
                        mProjectName.add(project_name)
                    }

                }


            },
            Response.ErrorListener {
                    error ->

            }) {
            //Header
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Client-Service","frontend-client")
                headers.put("Auth-Key","checklistapi")
                return headers
            }


        }
        stringRequest.retryPolicy = DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue!!.add(stringRequest)
    }




    fun getBuilding(projectId:String){

        var url="https://app.megapolis.co.in/all_buildings"
        var queue= Volley.newRequestQueue(this)
        var stringRequest=object : StringRequest(
            Request.Method.POST,url,
            Response.Listener { response ->

                Log.e("getBuildingssssssssssssssssssssss",""+response)
                mBuildingID.clear()
                mBuildingName.clear()
                var strResp = response.toString()
                val jsonObj: JSONObject = JSONObject(strResp)
                var status=jsonObj.getInt("status")
                if (status==200){
                    val jsonArray: JSONArray = jsonObj.getJSONArray("data")
                    for (i in 0 until jsonArray.length()) {
                        var jsonInner: JSONObject = jsonArray.getJSONObject(i)
                        var id = jsonInner.getString("id")
                        var building_name = jsonInner.getString("building_name")
                        mBuildingName.add(building_name)
                        mBuildingID.add(id)

                    }

                }


            },
            Response.ErrorListener {
                    error ->

            }) {
            //Header
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Client-Service","frontend-client")
                headers.put("Auth-Key","checklistapi")
                return headers
            }
            public override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params.put("project_id",projectId)
                return params
            }


        }
        stringRequest.retryPolicy = DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue!!.add(stringRequest)

    }




    fun getCheckList(){
        var url="https://app.megapolis.co.in/all_checklist"
        var queue= Volley.newRequestQueue(this)
        var stringRequest=object : StringRequest(
            Request.Method.GET,url,
            Response.Listener { response ->

                Log.e("getCheckListttttttttttttttttttttt",""+response)

                var strResp = response.toString()
                val jsonObj: JSONObject = JSONObject(strResp)
                var status=jsonObj.getInt("status")
                if (status==200){
                    val jsonArray: JSONArray = jsonObj.getJSONArray("data")
                    for (i in 0 until jsonArray.length()) {
                        var jsonInner: JSONObject = jsonArray.getJSONObject(i)
                        var id = jsonInner.getString("id")
                        var checklist_name = jsonInner.getString("checklist_name")
                        mChkID.add(id)
                        mChkName.add(checklist_name)
                    }

                }


            },
            Response.ErrorListener {
                    error ->

            }) {
            //Header
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Client-Service","frontend-client")
                headers.put("Auth-Key","checklistapi")
                return headers
            }


        }
        stringRequest.retryPolicy = DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue!!.add(stringRequest)

    }
    @SuppressLint("MissingPermission")
    private fun getLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        hasGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        hasNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        if (hasGps || hasNetwork) {

            if (hasGps) {
                Log.d("CodeAndroidLocation", "hasGps")
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0F, object :
                    LocationListener {
                    override fun onLocationChanged(location: Location?) {
                        if (location != null) {
                            locationGps = location
                            //  tv_result.append("\nGPS ")
                            //   tv_result.append("\nLatitude : " + locationGps!!.latitude)
                            //   tv_result.append("\nLongitude : " + locationGps!!.longitude)
                            Log.d("CodeAndroidLocation", " GPS Latitude : " + locationGps!!.latitude)
                            Log.d("CodeAndroidLocation", " GPS Longitude : " + locationGps!!.longitude)
                        }
                    }

                    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

                    }

                    override fun onProviderEnabled(provider: String?) {

                    }

                    override fun onProviderDisabled(provider: String?) {

                    }

                })

                val localGpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if (localGpsLocation != null)
                    locationGps = localGpsLocation
            }
            if (hasNetwork) {
                Log.d("CodeAndroidLocation", "hasGps")
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0F, object :
                    LocationListener {
                    override fun onLocationChanged(location: Location?) {
                        if (location != null) {
                            locationNetwork = location
                            // tv_result.append("\nNetwork ")
                            // tv_result.append("\nLatitude : " + locationNetwork!!.latitude)
                            // tv_result.append("\nLongitude : " + locationNetwork!!.longitude)
                            Log.e("CodeAndroidLocation", " Network Latitude : " + locationNetwork!!.latitude)
                            Log.e("CodeAndroidLocation", " Network Longitude : " + locationNetwork!!.longitude)
                            GetAddress(locationNetwork!!.latitude,locationNetwork!!.longitude)
                        }
                    }

                    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

                    }

                    override fun onProviderEnabled(provider: String?) {

                    }

                    override fun onProviderDisabled(provider: String?) {

                    }

                })

                val localNetworkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                if (localNetworkLocation != null)
                    locationNetwork = localNetworkLocation
            }

            if(locationGps!= null && locationNetwork!= null){
                if(locationGps!!.accuracy > locationNetwork!!.accuracy){
                    //      tv_result.append("\nNetwork ")
                    //        tv_result.append("\nLatitude : " + locationNetwork!!.latitude)
                    //        tv_result.append("\nLongitude : " + locationNetwork!!.longitude)
                    Log.e("CodeAndroidLocation", " Network Latitude : " + locationNetwork!!.latitude)
                    Log.e("CodeAndroidLocation", " Network Longitude : " + locationNetwork!!.longitude)
                    GetAddress(locationNetwork!!.latitude,locationNetwork!!.longitude)

                }else{
//                    tv_result.append("\nGPS ")
                    //                   tv_result.append("\nLatitude : " + locationGps!!.latitude)
                    //                 tv_result.append("\nLongitude : " + locationGps!!.longitude)
                    Log.e("CodeAndroidLocation", " GPS Latitude : " + locationGps!!.latitude)
                    Log.e("CodeAndroidLocation", " GPS Longitude : " + locationGps!!.longitude)
                    GetAddress(locationGps!!.latitude,locationGps!!.longitude)
                }
            }

        } else {
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }
    }


    private fun checkPermission(permissionArray: Array<String>): Boolean {
        var allSuccess = true
        for (i in permissionArray.indices) {
            if (checkCallingOrSelfPermission(permissionArray[i]) == PackageManager.PERMISSION_DENIED)
                allSuccess = false
        }
        return allSuccess
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST) {
            var allSuccess = true
            for (i in permissions.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    allSuccess = false
                    val requestAgain = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && shouldShowRequestPermissionRationale(permissions[i])
                    if (requestAgain) {
                        Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Go to settings and enable the permission", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            if (allSuccess)
                getLocation()
            // enableView()

        }
    }


    fun GetAddress(latitude: Double,longitude:Double){
        val geocoder: Geocoder
        val addresses: List<Address>
        geocoder = Geocoder(this, Locale.getDefault())

        addresses = geocoder.getFromLocation(
            latitude,
            longitude,
            1
        ) // Here 1 represent max location result to returned, by documents it recommended 1 to 5

        val address =
            addresses[0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        val city = addresses[0].getLocality()
        val state = addresses[0].getAdminArea()
        val country = addresses[0].getCountryName()
        val postalCode = addresses[0].getPostalCode()
        val knownName = addresses[0].getFeatureName() // Only if available else return NULL
        addressss=address

    }
}
