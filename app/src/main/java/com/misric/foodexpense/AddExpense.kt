package com.misric.foodexpense

import android.annotation.SuppressLint
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import java.time.LocalDate
import java.util.*

class AddExpense : AppCompatActivity() {
    lateinit var spinnerCategory: Spinner
    lateinit var amount: EditText
    lateinit var note: EditText

    private val context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_expense)

        spinnerCategory = findViewById(R.id.spinner_category)
        ArrayAdapter.createFromResource(
            this,
            R.array.expense_categories,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerCategory.adapter = adapter
        }

        amount = findViewById(R.id.et_amount)
        note = findViewById(R.id.et_note)

        val returnToPrevious: Button = findViewById(R.id.btn_return)
        returnToPrevious.setOnClickListener {
            finish()
        }

        val addExpense: Button = findViewById(R.id.btn_add)
        addExpense.setOnClickListener {
            if(amount.text.isEmpty()) {
                AlertDialog.Builder(context)
                    .setTitle("Warning")
                    .setMessage("Amount cannot be empty.")
                    .setNeutralButton(android.R.string.ok, object: DialogInterface.OnClickListener{
                        override fun onClick(p0: DialogInterface?, p1: Int) {
                            Log.d("","")
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .show()
            } else {
                addExpenseToDb()
            }
        }

    }

    fun addExpenseToDb() {
        val expenseModel = Expense()

        expenseModel.Year = Calendar.getInstance().get(Calendar.YEAR)
        expenseModel.Month = Calendar.getInstance().get(Calendar.MONTH) + 1
        expenseModel.Date = LocalDate.now().toString()
        expenseModel.Category = spinnerCategory.selectedItem.toString()
        expenseModel.Note = note.text.toString()
        expenseModel.Amount = amount.text.toString().toFloat()
        expenseModel.Avatar = spinnerCategory.selectedItem.toString().lowercase()


        val expenseDb = ExpenseDbHelper(this@AddExpense, null)
        expenseDb.addExpense(expenseModel)

    }
}