package kumarworld.rahul.checklist

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.RequestFuture
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
private const val PERMISSION_REQUEST = 10
class Activity_Login : AppCompatActivity() {


    private var USERID="USERID"
    private var ROLE = "ROLE"
    private var PROID = "PROID"
    private var BUILDID = "BUILDID"
    private var permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    private var myPreferences = "myPrefs"
    var sharedPreferences : SharedPreferences?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
         sharedPreferences = getSharedPreferences(myPreferences, Context.MODE_PRIVATE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermission(permissions)) {
                // enableView()

            } else {
                requestPermissions(permissions, PERMISSION_REQUEST)
            }
        }
        btnLogin.setOnClickListener {
            if (isNetworkAvailable()){
                Login()
            }
            else{
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Megapolis")
                builder.setMessage("Please Check Internet Coonection")
                builder.setPositiveButton("OK"){dialog, which ->
                    dialog.dismiss()
                }
                val dialog: AlertDialog = builder.create()
                dialog.show()
            }

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
    fun Login(){
        var url="https://app.megapolis.co.in/user-login"
        var queue=Volley.newRequestQueue(this)
        var stringRequest=object :StringRequest(Request.Method.POST,url,
            Response.Listener { response ->

                Log.e("qwwwwwwwwww",""+response)

                var strResp = response.toString()
                val jsonObj: JSONObject = JSONObject(strResp)
                var status=jsonObj.getInt("status")
                Log.e("qwwwwwwwwww",""+status)
                if (status==200){

                    val jsonInner: JSONObject = jsonObj.getJSONObject("data")


                        var id = jsonInner.getString("id")
                        var name = jsonInner.getString("name")
                        //var mobile_no = jsonInner.getString("mobile_no")
                      //  var email = jsonInner.getString("email")
                        var role = jsonInner.getString("role")
                      //  var city = jsonInner.getString("city")
                       var project_id=jsonInner.getString("project_id")
                       var building_id=jsonInner.getString("building_id")




                    if (role.equals("Admin") || role.equals("PM") ){
                        var intent=Intent(this,Activity_AdminMain::class.java)
                        intent.putExtra("userID",id)
                        intent.putExtra("Role",role)
                        intent.putExtra("proID",project_id)
                        intent.putExtra("buildID",building_id)
                        val editor = sharedPreferences!!.edit()
                        editor.putString(USERID, id)
                        editor.putString(ROLE, role)
                        editor.putString(PROID, project_id)
                        editor.putString(BUILDID, building_id)
                        editor.apply()
                        startActivity(intent)
                        finish()
                    }else if( role.equals("QA/QC Engr") || role.equals("Sr Engr")){
                        var intent=Intent(this,Activity_AdminMain::class.java)
                        intent.putExtra("userID",id)
                        intent.putExtra("Role",role)
                        intent.putExtra("proID",project_id)
                        intent.putExtra("buildID",building_id)
                        val editor = sharedPreferences!!.edit()
                        editor.putString(USERID, id)
                        editor.putString(ROLE, role)
                        editor.putString(PROID, project_id)
                        editor.putString(BUILDID, building_id)
                        editor.apply()
                        startActivity(intent)
                        finish()
                    }
                    else{
                        var intent=Intent(this,MainActivity::class.java)
                        intent.putExtra("userID",id)
                        val editor = sharedPreferences!!.edit()
                        editor.putString(USERID, id)
                        editor.putString(ROLE, "OTHER")
                        editor.putString(PROID, project_id)
                        editor.putString(BUILDID, building_id)
                        editor.apply()
                        startActivity(intent)
                        finish()
                    }

                }else{
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Megapolis")
                    builder.setMessage("login failed enter valid Mobile number and Password")
                    builder.setPositiveButton("OK"){dialog, which ->
                        dialog.dismiss()
                    }
                    val dialog: AlertDialog = builder.create()
                    dialog.show()
                }


            },
            Response.ErrorListener {
                    error ->

                val builder = AlertDialog.Builder(this)
                builder.setTitle("Megapolis")
                builder.setMessage("login failed enter valid Mobile number and Password")
                builder.setPositiveButton("OK"){dialog, which ->
                    dialog.dismiss()
                }
                val dialog: AlertDialog = builder.create()
                dialog.show()

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
                params.put("mobile_no",edt_emailID_login.text.toString())
                params.put("password", edt_Password_login.text.toString())

                return params
            }

        }
        stringRequest.retryPolicy = DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue!!.add(stringRequest)
    }


    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE)
        return if (connectivityManager is ConnectivityManager) {
            val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
            networkInfo?.isConnected ?: false
        } else false
    }
}