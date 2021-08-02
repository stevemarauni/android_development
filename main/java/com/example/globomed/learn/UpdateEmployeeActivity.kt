package com.example.globomed.learn

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.globomed.learn.DataManager.fetchEmployee
import com.example.globomed.learn.R.string
import kotlinx.android.synthetic.main.activity_add.*
import java.text.SimpleDateFormat
import java.util.*

class UpdateEmployeeActivity: AppCompatActivity() {

	private lateinit var databaseHelper : DataBaseHelper
	private val myCalendar = Calendar.getInstance()
	private var empId:String?=null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_add)

		databaseHelper = DataBaseHelper(this)
		val bundle:Bundle?=intent.extras
		bundle?.let {
			empId=bundle.getString(GloboMedDbContract.EmployeeEntry.COLUMN_ID)
			val employee:Employee?  = fetchEmployee(DataBaseHelper, empId!!, isSurgeon = Int)

			employee.let {
				etEmpName.setText(employee?.name)
				etDesignation.setText(employee?.designation)
				etDOB.setText(getFormattedDate(employee?.dob))
				if (employee != null) {
					sSurgeon.isChecked=(1==employee.isSurgeon)
				}

			}

		}

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

		bSave.setOnClickListener {
			saveEmployee()
		}

		bCancel.setOnClickListener {
			finish()
		}
	}

	private fun saveEmployee() {

		var isValid = true

		etEmpName.error = if (etEmpName?.text.toString().isEmpty()) {
			isValid = false
			"Required Field"
		} else null

		etDesignation.error = if (etDesignation?.text.toString().isEmpty()) {
			isValid = false
			"Required Field"
		} else null

		if (isValid) {
			val updateName:String=etEmpName.text.toString()
			val updateDOB:Long = myCalendar.timeInMillis
			val updateDesignation:String=etDesignation.text.toString()
			val updateIsSurgeon:Int=if (sSurgeon.isChecked)1 else 0

			val updatedEmployee= Employee(empId!!,updateName,updateDOB,updateDesignation, updateIsSurgeon)
			DataManager.updateEmployee(databaseHelper,updatedEmployee)

			setResult(Activity.RESULT_OK)

			Toast.makeText(applicationContext,"Employee Updated",Toast.LENGTH_SHORT).show()

			finish()
		}
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

	override fun onCreateOptionsMenu(menu: Menu?): Boolean {
		menuInflater.inflate(R.menu.menu_item, menu)
		return true
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		return when(item.itemId){
			R.id.action_delete->{
				val builder=AlertDialog.Builder(this)
				builder.setPositiveButton(string.yes){ _, _ ->
					val result= DataManager.deleteEmployee(databaseHelper, empId.toString())
					Toast.makeText(
						applicationContext,"$result record deleted",
						Toast.LENGTH_SHORT
					).show()
					setResult(Activity.RESULT_OK, Intent())
					finish()
				}
					.setNegativeButton(string.no){ dialog, _ ->
					dialog.dismiss()
				}
				val dialog =builder.create()
				dialog.setTitle("Are you sure")
				dialog.show()
				true
			}
			else -> super.onOptionsItemSelected(item)
		}
	}
}