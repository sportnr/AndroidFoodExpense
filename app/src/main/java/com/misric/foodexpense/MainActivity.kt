package com.misric.foodexpense

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class MainActivity : AppCompatActivity(), MainAdapter.OnExpenseListener {
    private lateinit var tvTitle: TextView
    private lateinit var tvLimit: TextView
    private lateinit var tvSpent: TextView
    private lateinit var tvBalance: TextView
    private lateinit var recViewMain: RecyclerView
    private lateinit var selectedYear: EditText
    private lateinit var selectedMonth: EditText

    private val defaultLimit = "400"
    private val context = this

    private lateinit var expenses : MutableList<Expense>

    private val activityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        refresh()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvTitle = findViewById(R.id.tv_Title)
        tvLimit = findViewById(R.id.tv_limitValue)
        tvSpent = findViewById(R.id.tv_spentValue)
        tvBalance = findViewById(R.id.tv_balanceValue)
        recViewMain = findViewById(R.id.recView_main)
        selectedYear = findViewById(R.id.et_mainLayoutYear)
        selectedMonth = findViewById(R.id.et_mainLayoutMonth)

        val addExpense: Button = findViewById(R.id.btn_addExpense)
        addExpense.setOnClickListener {
            intent = Intent(context, AddExpense::class.java) // context or this@MainActivity
            activityLauncher.launch(intent)
            //context.startActivity(intent)
        }

        val refresh: Button = findViewById(R.id.btn_refresh)
        refresh.setOnClickListener {
            refresh()
        }

        val btnReport: Button = findViewById(R.id.btn_report)
        btnReport.setOnClickListener {
            intent = Intent(this, ReportActivity::class.java)
            startActivity(intent)
        }

        refresh()
    }

    private fun refresh() {
        var currentYear : Int
        var currentMonth : Int

        if (selectedYear.text.isEmpty()) {
            currentYear = Calendar.getInstance().get(Calendar.YEAR)
        } else {
            currentYear = selectedYear.text.toString().toInt()
        }

        if (selectedMonth.text.isEmpty()) {
            currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1
        } else {
            currentMonth = selectedMonth.text.toString().toInt()
        }

        tvTitle.text = "${currentYear.toString()}/${currentMonth.toString()}"

        tvLimit.text = defaultLimit
        val spent = getDbTotalAmountPerMonth(currentYear, currentMonth)
        tvSpent.text = spent.toString()

        val balance = defaultLimit.toFloat() - spent
        tvBalance.text = String.format("%.2f", balance)

        if(defaultLimit.toFloat() < spent) {
            tvBalance.setTextColor(Color.parseColor("#FF0000"))
        } else {
            tvBalance.setTextColor(Color.parseColor("#008000"))
        }

        showRecords(currentYear, currentMonth)
    }

    fun getDbTotalAmountPerMonth(yr: Int, mnth: Int): Float {
        val db = ExpenseDbHelper(context, null)
        val cursor = db.getSpentPerMonth(yr, mnth)

        var spent: String = ""

        if (cursor != null) {
            while (cursor.moveToNext()) {
                if (cursor.getString(0) != null) spent = cursor.getString(0)
            }
        }
        db.close()

        return (if (spent.isEmpty()) 0f else spent.toFloat())
    }

    private fun showRecords(yr: Int, mnth: Int) {
        val db = ExpenseDbHelper(context, null)
        val cursor = db.getRecordsForMonth(yr, mnth)

        expenses = mutableListOf<Expense>()

        if (cursor != null) {
            val id = cursor.getColumnIndex(ExpenseDbHelper.COL_ID)
            val date = cursor.getColumnIndex(ExpenseDbHelper.COL_DATE)
            val category = cursor.getColumnIndex(ExpenseDbHelper.COL_CATEGORY)
            val amout = cursor.getColumnIndex(ExpenseDbHelper.COL_AMOUNT)
            val note = cursor.getColumnIndex(ExpenseDbHelper.COL_NOTE)
            val avatar = cursor.getColumnIndex(ExpenseDbHelper.COL_AVATAR)

            while(cursor.moveToNext()) {
                val exp = Expense()
                exp.Id = cursor.getString(id).toInt()
                exp.Date = cursor.getString(date)
                exp.Category = cursor.getString(category)
                exp.Amount = cursor.getString(amout).toFloat()
                exp.Note = cursor.getString(note)
                exp.Avatar = cursor.getString(avatar)
                expenses.add(exp)
            }
        }
        db.close()

        recyclerRecords(expenses)

    }

    private fun recyclerRecords(expenses: MutableList<Expense>) {
        var mainAdapter = MainAdapter(this, expenses, this, resources, packageName)
        recViewMain.layoutManager = LinearLayoutManager(this)
        recViewMain.adapter = mainAdapter
    }

    override fun onExpenseClick(position: Int) {
        intent = Intent(this, UpdateExpense::class.java)
        intent.putExtra("expense", expenses.get(position))
        activityLauncher.launch(intent)
        //startActivity(intent)
    }
}