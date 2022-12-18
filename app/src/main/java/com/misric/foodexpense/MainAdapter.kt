package com.misric.foodexpense

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MainAdapter(var mContext: Context, var expenses: MutableList<Expense>, var onExpenseListener: OnExpenseListener, var res: Resources, var packNm: String) :
RecyclerView.Adapter<MainAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var layoutInflater = LayoutInflater.from(mContext)
        var recView = layoutInflater.inflate(R.layout.recycler_main, parent, false)
        return MyViewHolder(recView, onExpenseListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.date.text = expenses.get(position).Date
        holder.category.text = expenses.get(position).Category
        holder.amount.text = expenses.get(position).Amount.toString()
        holder.note.text = expenses.get(position).Note
        holder.avatar.setImageResource(res.getIdentifier(expenses.get(position).Avatar,"drawable", packNm))

    }

    override fun getItemCount(): Int {
        return expenses.size
    }

    class MyViewHolder(itemView: View, var onExpenseListener: OnExpenseListener): RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var date: TextView
        var category: TextView
        var amount: TextView
        var note: TextView
        var avatar: ImageView

        init {
            date = itemView.findViewById(R.id.tv_updExpenseDate)
            category = itemView.findViewById(R.id.tv_updExpenseCategory)
            note = itemView.findViewById(R.id.tv_updExpenseNote)
            amount = itemView.findViewById(R.id.tv_updExpenseAmount)
            avatar = itemView.findViewById(R.id.iv_updExpenseAvatar)

            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            onExpenseListener.onExpenseClick(adapterPosition)
        }
    }

    interface OnExpenseListener {
        fun onExpenseClick(position: Int)
    }
}