package com.test.mvvm.repository

import android.content.Context
import com.test.mvvm.database.DBHelper
import com.test.mvvm.vm.TestData

class Test1Repository {

    companion object {
        fun addData(context:Context, testData: TestData){
            val dbHelper = DBHelper(context)

            val sql = """insert into TestTable
            | (testData1, testData2) values (?,?)
        """.trimMargin()

            val args = arrayOf(testData.data1, testData.data2)

            dbHelper.writableDatabase.execSQL(sql, args)
            dbHelper.close()
        }

        fun getAll(context: Context) : MutableList<TestData>{
            val tempList = mutableListOf<TestData>()

            val dbHelper = DBHelper(context)

            val sql = "select * from TestTable"

            val c1 = dbHelper.writableDatabase.rawQuery(sql,null)
            while (c1.moveToNext()){
                val a1 = c1.getColumnIndex("testIdx")
                val a2 = c1.getColumnIndex("testData1")
                val a3 = c1.getColumnIndex("testData2")

                val testIdx = c1.getInt(a1)
                val testData1 = c1.getString(a2)
                val testData2 = c1.getString(a3)

                val t1 = TestData(testIdx, testData1, testData2)

                tempList.add(t1)
            }

            dbHelper.close()

            return tempList
        }

        fun getOne(context:Context, testIdx:Int) : TestData{

            val dbHelper = DBHelper(context)

            val sql = "select * from TestTable where testIdx = ?"
            val args = arrayOf(testIdx.toString())

            val c1 = dbHelper.writableDatabase.rawQuery(sql, args)
            c1.moveToNext()

            val a2 = c1.getColumnIndex("testData1")
            val a3 = c1.getColumnIndex("testData2")
            val testData1 = c1.getString(a2)
            val testData2 = c1.getString(a3)

            val t1 = TestData(testIdx, testData1, testData2)

            dbHelper.close()

            return t1
        }
    }


}