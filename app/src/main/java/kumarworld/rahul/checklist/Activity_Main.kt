package kumarworld.rahul.checklist


import android.app.DatePickerDialog
import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import android.widget.DatePicker
import kotlinx.android.synthetic.main.activity_main_form.*
import java.text.SimpleDateFormat
import java.util.*


class Activity_Main : AppCompatActivity(){
    var cal = Calendar.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_form)
    }
}