package kumarworld.rahul.checklist

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.*
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.github.chrisbanes.photoview.PhotoView
import kotlinx.android.synthetic.main.activity_main_form.*
import kotlinx.android.synthetic.main.fragment_status.*
import kotlinx.android.synthetic.main.review_dialog.view.*
import kumarworld.rahul.checklist.adapter.AdapterViewStatus
import kumarworld.rahul.checklist.data.Status
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
private const val PERMISSION_REQUEST = 10
class ActivityDetails :AppCompatActivity() {
    var addressss=""
    val requestCode = 20
    var bitmap: Bitmap?= null
    var bit : Bitmap ?=null
    private var myPreferences = "myPrefs"

    lateinit var locationManager: LocationManager
    private var hasGps = false
    private var hasNetwork = false
    private var locationGps: Location? = null
    private var locationNetwork: Location? = null
    var date=""
    var question_id=""
    private var permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_status)
        val sdf = SimpleDateFormat("dd/MMM/yyyy hh:mm a")
        date = sdf.format(Date())
        if (Activity_AdminMain.mRole.equals("PM")){
            btnAddReview.visibility=View.VISIBLE
        }
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
       // var project_id=this.arguments!!.getString("project_id")
      //  var checklist_id=this.arguments!!.getString("checklist_id")
       // var building_id=this.arguments!!.getString("building_id")
       // var flat_no=this.arguments!!.getString("flat_no")
        img_arrowback.setOnClickListener { onBackPressed() }
        question_id=intent.getStringExtra("question_id")

        var url="https://app.megapolis.co.in/remarks"
        var queue= Volley.newRequestQueue(this)
        var stringRequest=object : StringRequest(
            Request.Method.POST,url,
            Response.Listener { response ->
                 var mStatusList = ArrayList<Status>()
                Log.e("ffffffffffffffffffffffffff",""+response)
                /*     val builder = AlertDialog.Builder(mContext)
                     builder.setTitle("Kumar Properties")
                     builder.setMessage(response)
                     builder.setPositiveButton("OK"){dialog, which ->
                         dialog.dismiss()
                     }
                     val dialog: AlertDialog = builder.create()
                     dialog.show()
     */
                var strResp = response.toString()
                val jsonObj: JSONObject = JSONObject(strResp)
                var status=jsonObj.getInt("status")
                if (status==200){
                    val jsonArray: JSONArray = jsonObj.getJSONArray("data")
                    for (i in 0 until jsonArray.length()) {
                        var jsonInner: JSONObject = jsonArray.getJSONObject(i)
                        var remark = jsonInner.getString("remark")
                        var status = jsonInner.getString("checking_status")
                        var userName = jsonInner.getString("user_name")
                        var date = jsonInner.getString("date")
                        var role = jsonInner.getString("role")
                        var location=jsonInner.getString("location")
                        var image=jsonInner.getString("image")
                        var question_idBack=jsonInner.getString("question_id")
                        var checklist_id=jsonInner.getString("checklist_id")
                        var flat_no=jsonInner.getString("flat_no")
                        var building_id=jsonInner.getString("building_id")
                        var project_id=jsonInner.getString("project_id")
                        var statusdata=Status(remark,status,userName,date,role,location,image,checklist_id,question_idBack,flat_no,building_id,project_id)
                        mStatusList.add(statusdata)

                    }
                    recyclerStatus.layoutManager=LinearLayoutManager(this)
                    var adapter=AdapterViewStatus(this,mStatusList)
                    recyclerStatus.adapter=adapter

                }else if (status==201){
                    txtDataNotFound.visibility=View.VISIBLE
                }



            },
            Response.ErrorListener {
                    error ->
                Log.e("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",""+error)
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
                params.put("project_id",  Activity_FormAdmin.proID)
                params.put("checklist_id", Activity_FormAdmin.chkID)
                params.put("building_id", Activity_FormAdmin.buildID)
                params.put("flat_no",     Activity_FormAdmin.faltNo)
                params.put("question_id", question_id)


                Log.e("xxxxxxxxxxxxxxxxxx",""+ Activity_FormAdmin.proID +"    "+ Activity_FormAdmin.chkID +"    "+ Activity_FormAdmin.buildID +"        "+ Activity_FormAdmin.faltNo+"  "+question_id )
                return params

            }


        }
        stringRequest.retryPolicy = DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue!!.add(stringRequest)

        AddComment()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }


    private fun AddComment(){

        checkDone!!.setOnClickListener {
            if (checkDone!!.isChecked) {
                checkNotDone!!.isChecked = false
                submitStatus("",question_id,"YES")


            }
        }
        checkNotDone!!.setOnClickListener {
            if  (checkNotDone!!.isChecked) {
                checkDone!!.isChecked = false
               // submitStatus(mArrayListForm.get(position).getmQue(),mArrayListForm.get(position).getmCheckingStatus(),,mContext)

                val mDialogView = LayoutInflater.from(this).inflate(R.layout.review_dialog, null)
                //AlertDialogBuilder
                val mBuilder = AlertDialog.Builder(this)
                    .setView(mDialogView)
                    .setCancelable(false)
                    .setTitle("Review")

                //show dialog

                val  mAlertDialog = mBuilder.show()
                //login button click of custom layout

                mDialogView.dialogLoginBtn.setOnClickListener {
                    //dismiss dialog
                    submitStatus(mDialogView.dialogNameEt.text.toString(),question_id,"NO")
                    mAlertDialog.dismiss()
                    //get text from EditTexts of custom layout
                    val name = mDialogView.dialogNameEt.text.toString()

                }

                mDialogView.dialogCaptureImg.setOnClickListener{
                         openCam()
                    // mAlertDialog.imageView.setImageBitmap(Activity_Form.bit)

                }
                mDialogView.dialogShowImg.setOnClickListener{
                    if (bit!=null){
                        val mBuilder2 = AlertDialog.Builder(this)
                        val mView = LayoutInflater.from(this).inflate(R.layout.dialog_custom_layout, null)
                        val photoView = mView.findViewById<PhotoView>(R.id.imageView)
                        val btnClose=mView.findViewById<Button>(R.id.btnClose)
                        //Picasso.get().load(galleryImages.get(p1)).into(photoView)
                        photoView.setImageBitmap(bit)

                        mBuilder2.setView(mView)
                        val mDialog = mBuilder2.create()
                        // val animation = AnimationUtils.loadAnimation(context, R.anim.fade_in)
                        //  photoView.startAnimation(animation)
                        mDialog.show()
                        btnClose.setOnClickListener { mDialog.dismiss() }
                    }

                }
                //cancel button click of custom layout
                mDialogView.dialogCancelBtn.setOnClickListener {
                    //dismiss dialog

                    checkNotDone!!.isChecked = false
                    mAlertDialog.dismiss()
                }
            }


        }
    }






    fun submitStatus(remark: String, question_id: String, checking_status: String) {
        var url="https://app.megapolis.co.in/checking_status"
        var queue= Volley.newRequestQueue(this)
        var stringRequest=object : StringRequest(
            Request.Method.POST,url,
            Response.Listener { response ->
                Activity_Form.bit=null
                // var mFormList = ArrayList<Question>()
                Log.e("AllDataaaaaaaaaaaaaaaaaaaaa",""+response)

                /* var strResp = response.toString()
                 val jsonObj: JSONObject = JSONObject(strResp)
                 var status=jsonObj.getInt("status")
                 if (status==200){
                     val jsonArray: JSONArray = jsonObj.getJSONArray("data")
                     for (i in 0 until jsonArray.length()) {
                         var jsonInner: JSONObject = jsonArray.getJSONObject(i)
                         var mQueId = jsonInner.getString("que_id")
                         var mHeading = jsonInner.getString("heading")
                         var mQue = jsonInner.getString("que")
                         var mRemark = jsonInner.getString("remark")
                         var mCheckingStatus = jsonInner.getString("checking_status")
                         var mForm=Question(mQueId,mHeading,mQue,mRemark,mCheckingStatus)

                         mFormList.add(mForm)


                     }

                 }*/


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
                var image=""
                if (Activity_Form.bit==null){

                }else{
                    image = getStringImage(Activity_Form.bit!!)
                }

                params.put("project_id", Activity_FormAdmin.proID)
                params.put("remark", remark)
                params.put("checking_status", checking_status)
                params.put("checklist_id", Activity_FormAdmin.chkID)
                params.put("building_id", Activity_FormAdmin.buildID)
                params.put("user_id", Activity_AdminMain.userID)
                params.put("flat_no", Activity_FormAdmin.faltNo)
                params.put("question_id", question_id)
                params.put("date",date)
                params.put("location",addressss)
                params.put("image",image)


                Log.e("daaaaasasdasdasasasda",""+ Activity_Form.proID +"    "+ Activity_Form.chkID +"    "+ Activity_Form.buildID +"    "+ Activity_Form.userID +"    "+ Activity_Form.faltNo +"    "+ Activity_Form.refer +"    "+ Activity_Form.date)
                return params
            }


        }
        stringRequest.retryPolicy = DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue!!.add(stringRequest)
    }


    fun getStringImage(bmp: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val imageBytes = baos.toByteArray()
        return Base64.encodeToString(imageBytes, Base64.DEFAULT)
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


    fun openCam() {
        val intentCapture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intentCapture, requestCode)


    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (this.requestCode == requestCode && resultCode == Activity.RESULT_OK) {
            bitmap = data!!.extras!!.get("data") as Bitmap
            bit =bitmap
            Log.e("sfefrgrgtyhty",""+ bit)
            //  imageView.setImageBitmap(bitmap)
        }


    }

}