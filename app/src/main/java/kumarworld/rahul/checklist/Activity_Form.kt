package kumarworld.rahul.checklist

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

import kotlinx.android.synthetic.main.activity_form.*
import kotlinx.android.synthetic.main.demo.*
import kumarworld.rahul.checklist.adapter.AdapterForm
import kumarworld.rahul.checklist.data.Question
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.util.*

class Activity_Form : AppCompatActivity() ,OpenCamera{

    var bitmap: Bitmap?= null
    val requestCode = 20
    companion object {
        var chkID = ""
        var proID = ""
        var buildID = ""
        var date = ""
        var userID = ""
        var faltNo = ""
        var refer = ""
        var addressss=""
        var bit : Bitmap ?=null

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)
        chkID=intent.getStringExtra("chk_id")
        proID=intent.getStringExtra("pro_id")
        buildID=intent.getStringExtra("build_id")
        refer=intent.getStringExtra("dra_ref")
        faltNo=intent.getStringExtra("flat_no")
        userID=intent.getStringExtra("user_id")
        date=intent.getStringExtra("date")
        addressss=intent.getStringExtra("addressss")
        getAllQuation()

        txtNewForm.setOnClickListener { onBackPressed() }
        img_arrowback.setOnClickListener { onBackPressed() }

        recyclerView.layoutManager= LinearLayoutManager(this)


    }


    fun getAllQuation(){
        var url="https://app.megapolis.co.in/all_questions"
        var queue= Volley.newRequestQueue(this)
        var stringRequest=object : StringRequest(
            Request.Method.POST,url,
            Response.Listener { response ->
                var mFormList = ArrayList<Question>()
                Log.e("AllDataaaaaaaaaaaaaaaaaaaaa",""+response)

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
                        var mRemark = jsonInner.getString("remark")
                        var mCheckingStatus = jsonInner.getString("checking_status")
                        var mForm=Question(mQueId,mHeading,mQue,mRemark,mCheckingStatus)

                        mFormList.add(mForm)


                    }
                    var adapter= AdapterForm(this, mFormList,this)
                    recyclerView.adapter=adapter
                }


            },
            Response.ErrorListener {
                    error ->
                Log.e("sdwd",""+error)

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
                params.put("project_id",proID)
                params.put("checklist_id",chkID)
                params.put("building_id",buildID)
                params.put("user_id",userID)
                params.put("flat_no",faltNo)
                params.put("drawing_ref",refer)
                params.put("date","12-11-2019")

                Log.e("daaaaasasdasdasasasda",""+proID+"    "+chkID+"    "+buildID+"    "+userID+"    "+faltNo+"    "+refer+"    "+date)
                return params
            }


        }
        stringRequest.retryPolicy = DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue!!.add(stringRequest)

    }


    override fun openCam() {
        val intentCapture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intentCapture, requestCode)


    }
    fun getStringImage(bmp: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val imageBytes = baos.toByteArray()
        return Base64.encodeToString(imageBytes, Base64.DEFAULT)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (this.requestCode == requestCode && resultCode == Activity.RESULT_OK) {
            bitmap = data!!.extras!!.get("data") as Bitmap
            bit=bitmap
            Log.e("sfefrgrgtyhty",""+bit)
          //  imageView.setImageBitmap(bitmap)
        }


    }

    override fun onBackPressed() {
        super.onBackPressed()
    }




}