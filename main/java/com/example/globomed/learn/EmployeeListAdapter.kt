package com.example.globomed.learn

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item.view.*


class EmployeeListAdapter(
    private val context: Context
) : RecyclerView.Adapter<EmployeeListAdapter.EmployeeViewHolder>() {
    lateinit var employeeList: ArrayList<Employee>
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EmployeeViewHolder {

        val itemView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        return EmployeeViewHolder(itemView)
    }

    override fun getItemCount(): Int =employeeList.size

    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
                val employee=employeeList[position]
        holder.setData(employee.name, employee.designation, employee.isSurgeon)
        holder.setListener()
    }

    fun setEmployees(employees: ArrayList<Employee>) {
        employeeList = employees
        notifyDataSetChanged()

    }

    inner class EmployeeViewHolder(itemView: View)  : RecyclerView.ViewHolder(itemView) {
        private var pos = 0
        fun setData(name: String, designation: String, isSurgeon: Int.Companion) {
            name.also { it.also { itemView.tvEmpName.text = it } }
            itemView.tvEmpDesignation.text=designation
            (if (1==isSurgeon)"YES"
            else "NO").also { itemView.tvIsSurgeonConfirm.text = it }
            pos.also { this.pos = it }
        }

        fun setListener() {

            itemView.setOnClickListener {
               val intent=Intent(context,UpdateEmployeeActivity::class.java)
                intent.putExtra(GloboMedDbContract.EmployeeEntry.COLUMN_ID,employeeList[pos].id)
                (context as Activity).startActivityForResult(intent,2)


            }
        }

    }
}
