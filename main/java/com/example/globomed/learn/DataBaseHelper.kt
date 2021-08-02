package com.example.globomed.learn

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DataBaseHelper(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(GloboMedDbContract.EmployeeEntry.SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(GloboMedDbContract.EmployeeEntry.SQL_DROP_TABLE)
    }

    companion object {
        lateinit var readableDatabase: SQLiteDatabase
        const val DATABASE_NAME = "globomed.db"
        const val DATABASE_VERSION = 2
    }
}