package kumarworld.rahul.checklist

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_admin_check.*
import kumarworld.rahul.checklist.data.Question

import org.json.JSONArray
import org.json.JSONObject
import java.util.HashMap

class Activity_AdminMain :AppCompatActivity() {
    val mChkID:ArrayList<String> =ArrayList()
    val mChkName:ArrayList<String> =ArrayList()

    val mProjectName:ArrayList<String> = ArrayList()
    val mProjectId:ArrayList<String> = ArrayList()


    val mBuildingName:ArrayList<String> = ArrayList()
    val mBuildingID:ArrayList<String> = ArrayList()

    val mFlatName:ArrayList<String> =ArrayList()


    var chkID=""
    var proID=""
    var buildID=""
    var userID=""
    var FlatName=""
    var BuildIDs=""
   // var project_id=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_check)
        userID=intent.getStringExtra("userID")
        proID=intent.getStringExtra("proID")
        BuildIDs=intent.getStringExtra("buildID")
        Log.e("aaaaaaaaaaaaaaa",""+BuildIDs)
        /*val parts = BuildIDs.split(",")
        for (i in 0 until parts.lastIndex+1){
            Log.e("ddddddddddddddd",""+parts[i])
        }
*/


        if (intent.getStringExtra("Role").equals("PM")){
           getBuilding(proID)
           // pro_tital.visibility=View.GONE
            //spinnerProjectName.visibility=View.GONE

        }

        getAllProject()
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
                    //Toast.makeText(applicationContext, "CHK ID"+chkID, Toast.LENGTH_LONG).show()
                    // getProject(cityIDss,locationID)
                }catch (e : IllegalArgumentException){

                }
            })

            val dialog = builder.create()
            dialog.show()
        }


        spinnerProjectName.setOnClickListener(){
            val array = arrayOfNulls<String>(mProjectName.size)
            mProjectName.toArray(array)
            mBuildingID.clear()
            mBuildingName.clear()
            spinnerBuildingNo.text=""
            mFlatName.clear()
            spinnerFlatNo.text=""

            val builder = AlertDialog.Builder(this)


            // Set a title for alert dialog
            builder.setTitle("Select Project .")
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
            mFlatName.clear()
            spinnerFlatNo.text=""

            val builder = AlertDialog.Builder(this)


            // Set a title for alert dialog
            builder.setTitle("Select Building .")
            builder.setItems(array,{_, which ->
                val selected = mBuildingName[which]
                try {
                    spinnerBuildingNo.text=selected
                    buildID=mBuildingID[which] as String
                   // Toast.makeText(applicationContext, "Building  ID  - "+buildID, Toast.LENGTH_LONG).show()
                     getFlatList(proID,buildID)
                }catch (e:IllegalArgumentException){

                }
            })

            val dialog = builder.create()
            dialog.show()
        }

        spinnerFlatNo.setOnClickListener(){
            val array = arrayOfNulls<String>(mFlatName.size)
            mFlatName.toArray(array)

            val builder = AlertDialog.Builder(this)


            // Set a title for alert dialog
            builder.setTitle("Select Flat.")
            builder.setItems(array,{_, which ->
                val selected = mFlatName[which]
                try {
                    spinnerFlatNo.text=selected
                    FlatName=mFlatName[which] as String
                   // Toast.makeText(applicationContext, "Flat  ID  - "+FlatName, Toast.LENGTH_LONG).show()
                    // getBuilding(projectID)
                }catch (e:IllegalArgumentException){

                }
            })

            val dialog = builder.create()
            dialog.show()
        }


        btnNext.setOnClickListener{
            if (chkID!="" && proID!="" && FlatName!="" && buildID!=""){
                var intent=Intent(this,Activity_FormAdmin::class.java)
                intent.putExtra("AchkID",chkID)
                intent.putExtra("AproID",proID)
                intent.putExtra("Abuild",buildID)
                intent.putExtra("AFlat", FlatName)
                intent.putExtra("AuserID",userID)
                startActivity(intent)
            }else{

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
                        val parts = proID.split(",")
                        for (j in 0 until parts.lastIndex+1){
                            if (id==parts[j]){
                                mProjectId.add(id)
                                mProjectName.add(project_name)
                            }

                        }

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
                        val parts = BuildIDs.split(",")
                        for (j in 0 until parts.lastIndex+1){
                            if (id==parts[j]){
                                mBuildingName.add(building_name)
                                mBuildingID.add(id)
                            }

                        }


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


    fun getFlatList(projectID : String,build_ID:String){
        var url="https://app.megapolis.co.in/all_flats"
        var queue= Volley.newRequestQueue(this)
        var stringRequest=object : StringRequest(
            Request.Method.POST,url,
            Response.Listener { response ->

                Log.e("wwwwwwwwwwwwwwwwwww",""+response)

                mFlatName.clear()
                var strResp = response.toString()
                val jsonObj: JSONObject = JSONObject(strResp)
                var status=jsonObj.getInt("status")
                if (status==200){
                    val jsonArray: JSONArray = jsonObj.getJSONArray("data")
                    for (i in 0 until jsonArray.length()) {
                        var jsonInner: JSONObject = jsonArray.getJSONObject(i)
                       // var id = jsonInner.getString("id")
                        var flat_no = jsonInner.getString("flat_no")
                        mFlatName.add(flat_no)


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
                params.put("project_id",projectID)
                params.put("building_id",build_ID)
                return params
            }


        }
        stringRequest.retryPolicy = DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue!!.add(stringRequest)
    }
}