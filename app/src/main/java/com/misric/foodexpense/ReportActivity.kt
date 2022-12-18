package com.misric.foodexpense

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager

class ReportActivity : AppCompatActivity() {
    private lateinit var recReport : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        recReport = findViewById(R.id.recView_report)

        val btnBack : Button = findViewById(R.id.btn_reportBack)
        btnBack.setOnClickListener {
            finish()
        }

        getData()
    }

    private fun getData() {
        val reportObject = mutableListOf<MutableList<String>>()

        val db = ExpenseDbHelper(this, null)
        val cur = db.getStatsPerMonth()

        if (cur != null) {
            val year = cur.getColumnIndex(ExpenseDbHelper.COL_YEAR)
            val month = cur.getColumnIndex(ExpenseDbHelper.COL_MONTH)

            while (cur.moveToNext()) {
                val oneRecord = mutableListOf<String>()
                oneRecord.add(cur.getString(year))
                oneRecord.add(cur.getString(month))
                oneRecord.add(cur.getString(2))
                reportObject.add(oneRecord)
            }
        }
        db.close()
        recyclerReport(reportObject)
    }

    private fun recyclerReport(values : MutableList<MutableList<String>>) {
        val reportAdapter = ReportAdapter(this, values)
        recReport.layoutManager = LinearLayoutManager(this)
        recReport.adapter = reportAdapter
    }
}