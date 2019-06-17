package kumarworld.rahul.checklist

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_form.*
import kotlinx.android.synthetic.main.demo.*
import kumarworld.rahul.checklist.adapter.Adapter_AdminForm
import kumarworld.rahul.checklist.data.QuestionAdmin
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.util.ArrayList
import java.util.HashMap

class Activity_FormAdmin : AppCompatActivity(),OnItemClickListener {




    companion object {
        var chkID = ""
        var proID = ""
        var buildID = ""
        var userID = ""
        var faltNo = ""


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)
        chkID = intent.getStringExtra("AchkID")
        proID =intent.getStringExtra("AproID")
        buildID = intent.getStringExtra("Abuild")
        faltNo =intent.getStringExtra("AFlat")
        userID =intent.getStringExtra("AuserID")
        img_arrowback.setOnClickListener { onBackPressed() }
        txtNewForm.setOnClickListener { onBackPressed() }
        getAllQuation()
    }



    override fun onItemClick(pos: String) {
       // Toast.makeText(this,pos,Toast.LENGTH_LONG).show()

        var intent=Intent(this,ActivityDetails::class.java)
        intent.putExtra("question_id",pos)
        startActivity(intent)


    }
    fun getAllQuation(){
        var url="https://app.megapolis.co.in/checked_questions"
        var queue= Volley.newRequestQueue(this)
        var stringRequest=object : StringRequest(
            Request.Method.POST,url,
            Response.Listener { response ->
                var mFormList = ArrayList<QuestionAdmin>()
                Log.e("sssssssssssssssssssssss",""+response)

                var strResp = response.toString()
                val jsonObj: JSONObject = JSONObject(strResp)
                var status=jsonObj.getInt("status")
                if (status==200){
                    val jsonArray: JSONArray = jsonObj.getJSONArray("data")
                    for (i in 0 until jsonArray.length()) {
                        var jsonInner: JSONObject = jsonArray.getJSONObject(i)
                        var mQueId = jsonInner.getString("que_id")
                        var mHeading = jsonInner.getString("heading")
                        var mQue = jsonInner.getString("que")

                        var mForm= QuestionAdmin(mQueId,mHeading,mQue)

                        mFormList.add(mForm)


                    }
                   // var listener:OnItemClickListener
                    var adapter=Adapter_AdminForm(this,mFormList,this)
                    recyclerView.layoutManager= LinearLayoutManager(this)
                    recyclerView.adapter=adapter
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
                params.put("project_id", proID)
                params.put("checklist_id", chkID)
                params.put("building_id", buildID)
                params.put("flat_no", faltNo)


                Log.e("daaaaasasdasdasasasda",""+ proID +"    "+ chkID +"    "+ buildID +"    "+faltNo)
                return params
            }


        }
        stringRequest.retryPolicy = DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue!!.add(stringRequest)

    }



}