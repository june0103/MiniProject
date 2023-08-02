package com.test.mvvm.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context:Context) : SQLiteOpenHelper(context, "Text.db", null, 1) {
    override fun onCreate(p0: SQLiteDatabase?) {
        val sql = """create table TestTable
            | (testIdx integer primary key autoincrement,
            | testData1 text not null,
            | testData2 text not null)
        """.trimMargin()

        p0?.execSQL(sql)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }
}