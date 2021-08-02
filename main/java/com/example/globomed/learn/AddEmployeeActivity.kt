package com.example.globomed.learn

import androidx.appcompat.app.AppCompatActivity
import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add.*
import java.text.SimpleDateFormat
import java.util.*
import com.example.globomed.learn.GloboMedDbContract.EmployeeEntry as EmployeeEntry

class AddEmployeeActivity : AppCompatActivity() {

    private val myCalendar = Calendar.getInstance()
    private lateinit var dataBaseHelper: DataBaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        dataBaseHelper = DataBaseHelper(this)

        // on clicking ok on the calender dialog
        val date = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)


            etDOB.setText(getFormattedDate(myCalendar.timeInMillis))
        }


        etDOB.setOnClickListener {
            setUpCalender(date)
        }

        bSave.setOnClickListener{
        saveEmployee()
        }


    }

    private fun saveEmployee() {
        var isValid=true


        etEmpName.error=if (etEmpName.text.toString().isEmpty()){
            isValid=false
            "Required Field"
        } else null
        (if (etDesignation?.text.toString().isEmpty()){
            isValid=false
            "Required Field"
        } else null).also { etDesignation.error = it }
        if (isValid){
            val name:String=etEmpName?.text.toString()
            val designation:String=etDesignation?.text.toString()
            val dob:Long=myCalendar.timeInMillis
            val isSurgeon= if (sSurgeon.isChecked) 1 else 0

            val db:SQLiteDatabase = dataBaseHelper.writableDatabase
            val values=ContentValues()
            values.put(EmployeeEntry.COLUMN_NAME,name)
            values.put(EmployeeEntry.COLUMN_DESIGNATION,designation)
            values.put(EmployeeEntry.COLUMN_DOB,dob)
            values.put(EmployeeEntry.COLUMN_SURGEON,isSurgeon)

            val results:Long=db.insert(EmployeeEntry.TABLE_NAME,null,values)

            setResult(RESULT_OK, Intent())

            Toast.makeText(applicationContext,"Employee Added", Toast.LENGTH_SHORT).show()
        }
        finish()
    }


    private fun setUpCalender(date: DatePickerDialog.OnDateSetListener) {

        DatePickerDialog(
                this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun getFormattedDate(dobInMillis: Long?): String {

        return dobInMillis?.let {
            val sdf = SimpleDateFormat("d MMM, yyyy", Locale.getDefault())
            sdf.format(dobInMillis)
        } ?: "Not Found"
    }
}
