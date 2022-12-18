package com.misric.foodexpense

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CursorFactory
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast

class ExpenseDbHelper(var context: Context, cursorFactory: CursorFactory?) : SQLiteOpenHelper(context, DB_NAME, cursorFactory, DB_VER) {
    companion object {
        private val DB_NAME = "Food_Expense"
        private val DB_VER = 1
        private val TBL_EXPENSE = "Expense"
        private val TBL_CATEGORY = "Category"

        val COL_ID = "Id"

        // Table - Expense
        val COL_YEAR = "Year"
        val COL_MONTH = "Month"
        val COL_DATE = "Date"
        val COL_CATEGORY = "Category"
        val COL_NOTE = "Note"
        val COL_AVATAR = "Avatar"
        val COL_AMOUNT = "EUR"

        // Table - Category
        val COL_NAME = "Name"
    }

    override fun onCreate(p0: SQLiteDatabase?) {
        var sql = "CREATE TABLE $TBL_EXPENSE( " +
                "$COL_ID INTEGER PRIMARY KEY, " +
                "$COL_YEAR INTEGER, " +
                "$COL_MONTH INTEGER, " +
                "$COL_DATE TEXT, " +
                "$COL_CATEGORY TEXT, " +
                "$COL_NOTE TEXT, " +
                "$COL_AVATAR TEXT, " +
                "$COL_AMOUNT REAL)"
        p0?.execSQL(sql)

        sql = "CREATE TABLE $TBL_CATEGORY( " +
                "$COL_ID INTEGER PRIMARY KEY, " +
                "$COL_NAME TEXT)"
        p0?.execSQL(sql)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        val sql1 = "DROP TABLE IF EXISTS $TBL_EXPENSE"
        val sql2 = "DROP TABLE IF EXISTS $TBL_CATEGORY"
        p0?.execSQL(sql1)
        p0?.execSQL(sql2)
        onCreate(p0)
    }

    fun addCategory(name: String) {
        val value = ContentValues()
        value.put(COL_NAME, name)

        val db = writableDatabase
        val dbResult = db.insert(TBL_CATEGORY, null, value)
        db.close()
    }

    fun addExpense(expense: Expense) {
        val values = ContentValues()
        values.put(COL_YEAR, expense.Year)
        values.put(COL_MONTH, expense.Month)
        values.put(COL_DATE, expense.Date)
        values.put(COL_CATEGORY, expense.Category)
        values.put(COL_NOTE, expense.Note)
        values.put(COL_AVATAR, expense.Avatar)
        values.put(COL_AMOUNT, expense.Amount)

        val db = writableDatabase
        val dbResult = db.insert(TBL_EXPENSE, null, values)

        if(dbResult != -1L) {
            Toast.makeText(context, "Successfully inserted", Toast.LENGTH_SHORT).show()
        }
        db.close()
    }

    fun getSpentPerMonth(year: Int, month: Int): Cursor
    {
        val sql = "SELECT SUM($COL_AMOUNT) EXPENSESUM FROM $TBL_EXPENSE WHERE $COL_YEAR = $year AND $COL_MONTH = $month"
        val db = readableDatabase

        return db.rawQuery(sql, null)
    }

    fun getRecordsForMonth(year: Int, month: Int) : Cursor {
        val sql = "SELECT $COL_ID, $COL_DATE, $COL_CATEGORY, $COL_AMOUNT, $COL_NOTE, $COL_AVATAR " +
                "FROM $TBL_EXPENSE WHERE $COL_YEAR = $year AND $COL_MONTH = $month ORDER BY $COL_DATE"
        val db = readableDatabase
        return db.rawQuery(sql, null)
    }

    fun updateRecord(id : Int?, year : String, month : String, day : String) {
        val db = writableDatabase
        val values = ContentValues()

        val constructedDate = "$year-$month-$day"

        values.put(COL_YEAR, year)
        values.put(COL_MONTH, month)
        values.put(COL_DATE, constructedDate)

        db.update(TBL_EXPENSE, values, "Id=?", arrayOf(id.toString()))
        db.close()
    }

    fun deleteRecord(id : Int?) {
        val db = writableDatabase
        db.delete(TBL_EXPENSE, "Id=?", arrayOf(id.toString()))
        db.close()
    }

    fun getStatsPerMonth() : Cursor {
        val sql = "SELECT $COL_YEAR, $COL_MONTH, SUM($COL_AMOUNT) " +
                "FROM $TBL_EXPENSE " +
                "GROUP BY $COL_YEAR, $COL_MONTH " +
                "ORDER BY 1 DESC, 2 DESC"

        val db = readableDatabase
        return db.rawQuery(sql, null)
    }
}