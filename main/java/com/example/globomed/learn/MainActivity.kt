package com.example.globomed.learn

import android.app.Activity
import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var dataBaseHelper: DataBaseHelper
    private val employeeListAdapter=EmployeeListAdapter(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dataBaseHelper= DataBaseHelper(this)

        recyclerView.adapter= employeeListAdapter
        recyclerView.layoutManager= LinearLayoutManager(this)
        employeeListAdapter.setEmployees(DataManager.fetchAllEmployees(dataBaseHelper,isSurgeon = Int))

        fab.setOnClickListener {
        val addEmployee =Intent(this, AddEmployeeActivity::class.java)
        startActivityForResult(addEmployee,1)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode,data)
        if ( resultCode== RESULT_OK){
            employeeListAdapter.setEmployees(DataManager.fetchAllEmployees(dataBaseHelper, isSurgeon =Int))
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.action_deleteAll->{
                val builder= AlertDialog.Builder(this)
                builder.setPositiveButton(R.string.yes){ _, _ ->
                    val result= DataManager.deleteAllEmployee(dataBaseHelper)
                    Toast.makeText(
                        applicationContext,"$result record deleted",
                        Toast.LENGTH_SHORT
                    ).show()
                    setResult(Activity.RESULT_OK, Intent())
                    finish()
                }
                    .setNegativeButton(R.string.no){dialog, id->
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