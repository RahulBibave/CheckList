package kumarworld.rahul.checklist

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.fragment_status.*
import kumarworld.rahul.checklist.adapter.AdapterViewStatus
import kumarworld.rahul.checklist.data.Status
import org.json.JSONArray
import org.json.JSONObject
import java.util.HashMap

class ActivityDetails :AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_status)


       // var project_id=this.arguments!!.getString("project_id")
      //  var checklist_id=this.arguments!!.getString("checklist_id")
       // var building_id=this.arguments!!.getString("building_id")
       // var flat_no=this.arguments!!.getString("flat_no")
        img_arrowback.setOnClickListener { onBackPressed() }
        var question_id=intent.getStringExtra("question_id")

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
                        var statusdata=Status(remark,status,userName,date,role,location,image)
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


    }

    override fun onBackPressed() {
        super.onBackPressed()
    }


}