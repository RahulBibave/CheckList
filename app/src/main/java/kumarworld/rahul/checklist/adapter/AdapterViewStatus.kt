package kumarworld.rahul.checklist.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.chrisbanes.photoview.PhotoView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.review_dialog.view.*
import kumarworld.rahul.checklist.Activity_Form
import kumarworld.rahul.checklist.R
import kumarworld.rahul.checklist.data.Status

import java.util.ArrayList

class AdapterViewStatus(internal var mContext: Context, internal var arrayListStatus: ArrayList<Status>) :
    RecyclerView.Adapter<AdapterViewStatus.ViewHolderStatus>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderStatus {
        val view = LayoutInflater.from(mContext).inflate(R.layout.review_view, parent, false)
        return ViewHolderStatus(view)
    }

    override fun onBindViewHolder(holder: ViewHolderStatus, position: Int) {
        val status = arrayListStatus[position]

        holder.txtStatus.text = status.status
        holder.txtDate.text = status.date
        holder.txtLocation.text = status.location
        holder.txtUserName.text = status.userName + " ( " + status.role + " )"
        if (status.image.equals("")) {
            holder.imageView.visibility = View.GONE
        } else {
            holder.imageView.visibility = View.VISIBLE
            Picasso.with(mContext).load(status.image).into(holder.imageView)
            holder.imageView.setOnClickListener {
                val mBuilder2 = AlertDialog.Builder(mContext)
                val mView = LayoutInflater.from(mContext).inflate(R.layout.dialog_custom_layout, null)
                val photoView = mView.findViewById<PhotoView>(R.id.imageView)
                val btnClose=mView.findViewById<Button>(R.id.btnClose)

                Picasso.with(mContext).load(status.image).into(photoView)


                mBuilder2.setView(mView)
                val mDialog = mBuilder2.create()
                // val animation = AnimationUtils.loadAnimation(context, R.anim.fade_in)
                //  photoView.startAnimation(animation)
                mDialog.show()
                btnClose.setOnClickListener { mDialog.dismiss() }
            }

        }

        holder.txtRemark.text = status.remark

    }
    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return arrayListStatus.size
    }

    inner class ViewHolderStatus(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var txtRemark: TextView
        internal var txtStatus: TextView
        internal var txtUserName: TextView
        internal var txtDate: TextView
        internal var txtRole: TextView? = null
        internal var txtLocation: TextView
        internal var imageView: ImageView

        init {
            txtRemark = itemView.findViewById(R.id.txtRemark)
            txtStatus = itemView.findViewById(R.id.txtStatus)
            txtUserName = itemView.findViewById(R.id.txtUserName)
            txtDate = itemView.findViewById(R.id.txtDate)

            txtLocation = itemView.findViewById(R.id.txtLocation)
            imageView = itemView.findViewById(R.id.imgStatus)
        }
    }
}
