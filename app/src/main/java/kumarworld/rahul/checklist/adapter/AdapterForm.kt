package kumarworld.rahul.checklist.adapter

import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.github.chrisbanes.photoview.PhotoView
import kotlinx.android.synthetic.main.form_card.view.*
import kotlinx.android.synthetic.main.review_dialog.*
import kotlinx.android.synthetic.main.review_dialog.view.*
import kumarworld.rahul.checklist.Activity_Form
import kumarworld.rahul.checklist.OnItemClickListener
import kumarworld.rahul.checklist.OpenCamera
import kumarworld.rahul.checklist.R
import kumarworld.rahul.checklist.data.Question
import java.io.ByteArrayOutputStream


import java.util.ArrayList
import java.util.HashMap

class AdapterForm(private val mContext: Context, private val mArrayListForm: ArrayList<Question>,private val listener: OpenCamera) :
    RecyclerView.Adapter<AdapterForm.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.form_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems()
        holder.txtQua!!.text=mArrayListForm.get(position).getmQue()
        holder.txtHead!!.text=mArrayListForm.get(position).getmHeading()
        if ((position!==0)){
            if (mArrayListForm.get(position).getmHeading().equals(mArrayListForm.get(position-1).getmHeading())){
                holder.txtHead!!.visibility=View.GONE
            }
        }
        if (mArrayListForm.get(position).getmCheckingStatus().equals("NO")){
            holder.chNo!!.isChecked = true
        }
        if (mArrayListForm.get(position).getmCheckingStatus().equals("YES")){
            holder.chYes!!.isChecked = true
        }
        holder.chYes!!.setOnClickListener {
            if (holder!!.chYes!!.isChecked) {
                holder.chNo!!.isChecked = false
                submitStatus("",mArrayListForm.get(position).getmQueId(),"YES",mContext)


            }
        }
        holder.chNo!!.setOnClickListener {
          if  (holder!!.chNo!!.isChecked) {
                holder.chYes!!.isChecked = false
             // Activity_Form.submitStatus(mArrayListForm.get(position).getmQue(),mArrayListForm.get(position).getmCheckingStatus(),,mContext)

              val mDialogView = LayoutInflater.from(mContext).inflate(R.layout.review_dialog, null)
              //AlertDialogBuilder
              val mBuilder = AlertDialog.Builder(mContext)
                  .setView(mDialogView)
                  .setCancelable(false)
                  .setTitle("Review")

              //show dialog

              val  mAlertDialog = mBuilder.show()
             //login button click of custom layout

              mDialogView.dialogLoginBtn.setOnClickListener {
                  //dismiss dialog
                  submitStatus(mDialogView.dialogNameEt.text.toString(),mArrayListForm.get(position).getmQueId(),"NO",mContext)
                  mAlertDialog.dismiss()
                  //get text from EditTexts of custom layout
                  val name = mDialogView.dialogNameEt.text.toString()

              }

              mDialogView.dialogCaptureImg.setOnClickListener{
                    listener.openCam()
                  //  mAlertDialog.imageView.setImageBitmap(Activity_Form.bit)

              }
              mDialogView.dialogShowImg.setOnClickListener{
                  if (Activity_Form.bit!=null){
                      val mBuilder2 = AlertDialog.Builder(mContext)
                      val mView = LayoutInflater.from(mContext).inflate(R.layout.dialog_custom_layout, null)
                      val photoView = mView.findViewById<PhotoView>(R.id.imageView)
                      val btnClose=mView.findViewById<Button>(R.id.btnClose)
                      //Picasso.get().load(galleryImages.get(p1)).into(photoView)
                      photoView.setImageBitmap(Activity_Form.bit)

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

                  holder.chNo!!.isChecked = false
                  mAlertDialog.dismiss()
              }
            }


        }

    }

    override fun getItemCount(): Int {
        return mArrayListForm.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
      var txtQua: TextView?=null
        var chYes:CheckBox?=null
        var chNo:CheckBox?=null
        var txtHead:TextView?=null

        fun bindItems() {
            txtQua = itemView.txtQua
            chYes=itemView.checkYes
            chNo=itemView.checkNo
            txtHead=itemView.txtHeading
        }
    }



    fun submitStatus(remark: String, question_id: String, checking_status: String,context:Context) {
        var url="https://app.megapolis.co.in/checking_status"
        var queue= Volley.newRequestQueue(context)
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

                params.put("project_id", Activity_Form.proID)
                params.put("remark", remark)
                params.put("checking_status", checking_status)
                params.put("checklist_id", Activity_Form.chkID)
                params.put("building_id", Activity_Form.buildID)
                params.put("user_id", Activity_Form.userID)
                params.put("flat_no", Activity_Form.faltNo)
                params.put("question_id", question_id)
                params.put("date",Activity_Form.date)
                params.put("location",Activity_Form.addressss)
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


}
