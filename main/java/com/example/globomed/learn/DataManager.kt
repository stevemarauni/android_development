package com.example.globomed.learn

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import com.example.globomed.learn.GloboMedDbContract.EmployeeEntry

object DataManager {
    fun fetchAllEmployees(dataBaseHelper: DataBaseHelper, isSurgeon: Int.Companion):ArrayList<Employee>{
        val employees=ArrayList<Employee>()
        val db= dataBaseHelper.readableDatabase
        val  columns = arrayOf(
            EmployeeEntry.COLUMN_ID,
            EmployeeEntry.COLUMN_NAME,
            EmployeeEntry.COLUMN_DOB,
            EmployeeEntry.COLUMN_DESIGNATION,
            EmployeeEntry.COLUMN_SURGEON

        )
       val cursor = db.query(EmployeeEntry.TABLE_NAME, columns,null,null,null,null,null)
        val idPos =cursor.getColumnIndex(EmployeeEntry.COLUMN_ID)
        val namePos=cursor.getColumnIndex(EmployeeEntry.COLUMN_NAME)
        val dobPos=cursor.getColumnIndex(EmployeeEntry.COLUMN_DOB)
        val designationPos= cursor.getColumnIndex(EmployeeEntry.COLUMN_DESIGNATION)

        while( cursor.moveToNext()){
            val id= cursor.getString(idPos)
            val name=cursor.getString(namePos)
            val dob=cursor.getLong(dobPos)
            val designation=cursor.getString(designationPos)

            employees.add(Employee(id,name,dob,designation,isSurgeon))
        }
        cursor.close()
        return employees
    }
    fun fetchEmployee(dataBaseHelper: DataBaseHelper.Companion, empId: String, isSurgeon: Int.Companion): Employee? {
        val db:SQLiteDatabase = dataBaseHelper.readableDatabase
        var employee: Employee?=null

        val columns:Array<String> = arrayOf(
            EmployeeEntry.COLUMN_NAME,
            EmployeeEntry.COLUMN_DOB,
            EmployeeEntry.COLUMN_DESIGNATION
        )
        val selection:String =EmployeeEntry.COLUMN_ID+ " LIKE ?"

        val selectionArgs: Array<String> = arrayOf(empId)

        val cursor = db.query(EmployeeEntry.TABLE_NAME, columns,selection,selectionArgs,null,null,null)

        val namePos=cursor.getColumnIndex(EmployeeEntry.COLUMN_NAME)
        val dobPos=cursor.getColumnIndex(EmployeeEntry.COLUMN_DOB)
        val designationPos= cursor.getColumnIndex(EmployeeEntry.COLUMN_DESIGNATION)

        while(cursor.moveToNext()){
            val name=cursor.getString(namePos)
            val dob=cursor.getLong(dobPos)
            val designation=cursor.getString(designationPos)

            employee= Employee(empId,name,dob,designation,isSurgeon)
            cursor.close()
            return employee
        }
        cursor.close()
        return employee
    }
    fun updateEmployee(dataBaseHelper: DataBaseHelper,employee: Employee){
        val  db:SQLiteDatabase=dataBaseHelper.writableDatabase
        val values=ContentValues()
        values.put(EmployeeEntry.COLUMN_NAME,employee.name)
        values.put(EmployeeEntry.COLUMN_DESIGNATION,employee.designation)
        values.put(EmployeeEntry.COLUMN_DOB,employee.dob)
        //values.put(EmployeeEntry.COLUMN_SURGION,employee.isSurgeon)

        val selection :String = EmployeeEntry.COLUMN_ID + " LIKE ? "

        val selectionArgs :Array<String> = arrayOf(employee.id)

        db.update(EmployeeEntry.TABLE_NAME,values,selection,selectionArgs)

    }
    fun deleteEmployee(dataBaseHelper: DataBaseHelper,empId: String):Int{
        val db =dataBaseHelper.writableDatabase
        val selection :String = EmployeeEntry.COLUMN_ID + " LIKE ? "

        val selectionArgs :Array<String> = arrayOf(empId)
       return db.delete(EmployeeEntry.TABLE_NAME,selection,selectionArgs)

    }

    fun deleteAllEmployee(databaseHelper: DataBaseHelper): Int {
        val db=databaseHelper.writableDatabase
        return db.delete(EmployeeEntry.TABLE_NAME,"1",null)

    }
}