package kumarworld.rahul.checklist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class Activity_Splash : AppCompatActivity() {
    private var mDelayHandler: Handler? = null
    private val SPLASH_DELAY: Long = 3000 //3 seconds
    private var myPreferences = "myPrefs"
    internal val mRunnable: Runnable = Runnable {
        if (!isFinishing) {
            val sharedPreferences = getSharedPreferences(myPreferences, Context.MODE_PRIVATE)
            val role=sharedPreferences.getString("ROLE", "")
            val id=sharedPreferences.getString("USERID", "")
            val project_id=sharedPreferences.getString("PROID", "")
            val building_id=sharedPreferences.getString("BUILDID", "")
            if (role.equals("Admin") || role.equals("PM")){
                var intent=Intent(this,Activity_AdminMain::class.java)
                intent.putExtra("userID",id)
                intent.putExtra("Role",role)
                intent.putExtra("proID",project_id)
                intent.putExtra("buildID",building_id)
                startActivity(intent)
                finish()
            }else if (role.equals("QA/QC Engr") || role.equals("Sr Engr")){
                var intent=Intent(this,Activity_AdminMain::class.java)
                intent.putExtra("userID",id)
                intent.putExtra("Role",role)
                intent.putExtra("proID",project_id)
                intent.putExtra("buildID",building_id)
                startActivity(intent)
                finish()
            }else if (role.equals("OTHER")){
                var intent=Intent(this,MainActivity::class.java)
                intent.putExtra("userID",id)
                startActivity(intent)
                finish()
            }else{
                val intent = Intent(applicationContext, Activity_Login::class.java)
                startActivity(intent)
                finish()
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        //Initialize the Handler
        mDelayHandler = Handler()

        //Navigate with delay
        mDelayHandler!!.postDelayed(mRunnable, SPLASH_DELAY)

    }

    public override fun onDestroy() {

        if (mDelayHandler != null) {
            mDelayHandler!!.removeCallbacks(mRunnable)
        }

        super.onDestroy()
    }
}