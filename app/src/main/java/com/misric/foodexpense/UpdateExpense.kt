package com.misric.foodexpense

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog

class UpdateExpense : AppCompatActivity() {
    val TAG = "UpdateExpense myTag"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_expense)

        val expense = intent.getParcelableExtra<Expense>("expense")

        // holder.avatar.setImageResource(res.getIdentifier(expenses.get(position).Avatar,"drawable", packNm))

        val avatar : ImageView = findViewById(R.id.iv_updExpenseAvatar)
        avatar.setImageResource(resources.getIdentifier(expense?.Avatar, "drawable", packageName))

        val date : TextView = findViewById(R.id.tv_updExpenseDate)
        date.text = expense?.Date

        val category : TextView = findViewById(R.id.tv_updExpenseCategory)
        category.text = expense?.Category

        val amount : TextView = findViewById(R.id.tv_updExpenseAmount)
        amount.text = expense?.Amount.toString()

        val note : TextView = findViewById(R.id.tv_updExpenseNote)
        note.text = expense?.Note

        val selectedYear : EditText = findViewById(R.id.et_updExpenseYear)

        val selectedMonth : Spinner = findViewById(R.id.spinner_updExpenseMonth)
        ArrayAdapter.createFromResource(this, R.array.months, android.R.layout.simple_spinner_item)
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                selectedMonth.adapter = adapter
            }

        val selectedDay : Spinner = findViewById(R.id.spinner_updExpenseDay)
        ArrayAdapter.createFromResource(this, R.array.days, android.R.layout.simple_spinner_item)
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                selectedDay.adapter = adapter
            }

        val btnUpdateExpense : Button = findViewById(R.id.btn_updExpenseUpdate)
        btnUpdateExpense.setOnClickListener {
            if (selectedYear.text.isEmpty()) {
                AlertDialog.Builder(this)
                    .setTitle("Warning")
                    .setMessage("Year cannot be empty")
                    .setNeutralButton(android.R.string.ok, null)
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .show()
            } else {
                updateExpense(expense?.Id, selectedYear.text.toString(), selectedMonth.selectedItem.toString(), selectedDay.selectedItem.toString())
            }
        }

        val btnDeleteExpense : Button = findViewById(R.id.btn_updExpenseDelete)
        btnDeleteExpense.setOnClickListener {
            deleteExpense(expense?.Id)
        }

        val btnReturn : Button = findViewById(R.id.btn_updExpenseReturn)
        btnReturn.setOnClickListener {
            finish()
        }
    }

    fun deleteExpense(id : Int?) {
        val db = ExpenseDbHelper(this, null)
        db.deleteRecord(id)
        finish()
    }

    fun updateExpense(id : Int?, year : String, month : String, day : String) {
        val db = ExpenseDbHelper(this, null)
        db.updateRecord(id, year, month, day)
        finish()
    }
}