package kumarworld.rahul.checklist.adapter

import android.content.Context
import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.form_card.view.*
import kumarworld.rahul.checklist.*

import kumarworld.rahul.checklist.data.QuestionAdmin
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.util.ArrayList
import java.util.HashMap

class Adapter_AdminForm (private val mContext: Context, private val mArrayListForm: ArrayList<QuestionAdmin>,private val listener:OnItemClickListener) :
    RecyclerView.Adapter<Adapter_AdminForm.ViewHolderAdmin>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderAdmin {
        val view = LayoutInflater.from(mContext).inflate(R.layout.form_card, parent, false)
        return ViewHolderAdmin(view)
    }

    override fun getItemCount(): Int {
       return mArrayListForm.size
    }
    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: ViewHolderAdmin, position: Int) {
        holder.bindItems()
        holder.chYes!!.visibility=View.GONE
        holder.chNo!!.visibility=View.GONE
        holder.txtQua!!.text=mArrayListForm.get(position).que
        holder.txtHead!!.text=mArrayListForm.get(position).heading
        if ((position!==0)){
            if (mArrayListForm.get(position).heading.equals(mArrayListForm.get(position-1).heading)){
                holder.txtHead!!.visibility=View.GONE
            }
        }
        holder.linearQua!!.setOnClickListener {
            listener.onItemClick(mArrayListForm.get(position).que_id)
            //getStatus(mArrayListForm.get(position).que_id,mContext)
        }

    }


    inner class ViewHolderAdmin(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txtQua: TextView?=null
        var chYes: CheckBox?=null
        var chNo: CheckBox?=null
        var txtHead: TextView?=null
        var linearQua:LinearLayout?=null

        fun bindItems() {
            txtQua = itemView.txtQua
            chYes=itemView.checkYes
            chNo=itemView.checkNo
            txtHead=itemView.txtHeading
            linearQua=itemView.linearQua
        }
    }



    fun getStatus(question_id: String,context:Context) {
        var url="https://app.megapolis.co.in/remarks"
        var queue= Volley.newRequestQueue(context)
        var stringRequest=object : StringRequest(
            Request.Method.POST,url,
            Response.Listener { response ->
                // var mFormList = ArrayList<Question>()
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
                         var mQueId = jsonInner.getString("que_id")
                         var mHeading = jsonInner.getString("heading")
                         var mQue = jsonInner.getString("que")


                     }

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
                params.put("project_id", Activity_FormAdmin.proID)
                params.put("checklist_id", Activity_FormAdmin.chkID)
                params.put("building_id", Activity_FormAdmin.buildID)
                params.put("flat_no", Activity_FormAdmin.faltNo)
                params.put("question_id", question_id)


                Log.e("xxxxxxxxxxxxxxxxxx",""+ Activity_FormAdmin.proID +"    "+ Activity_FormAdmin.chkID +"    "+ Activity_FormAdmin.buildID +"        "+ Activity_FormAdmin.faltNo+"  "+question_id )
                return params

            }


        }
        stringRequest.retryPolicy = DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        queue!!.add(stringRequest)
    }


}