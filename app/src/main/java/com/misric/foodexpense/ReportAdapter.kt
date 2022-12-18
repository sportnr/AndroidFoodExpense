package com.misric.foodexpense

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ReportAdapter(var context : Context, var records : MutableList<MutableList<String>>) :
RecyclerView.Adapter<ReportAdapter.ViewHolder>(){

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        var year : TextView
        var month : TextView
        var sumEur : TextView

        init {
            year = itemView.findViewById(R.id.tv_recyclerReportYear)
            month = itemView.findViewById(R.id.tv_recyclerReportMonth)
            sumEur = itemView.findViewById(R.id.tv_recyclerReportSumAmount)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var layoutInflater = LayoutInflater.from(context)
        var recycView = layoutInflater.inflate(R.layout.recycler_report, parent, false)
        return ViewHolder(recycView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.year.text = records.get(position).get(0)
        holder.month.text = records.get(position).get(1)
        holder.sumEur.text = records.get(position).get(2)
    }

    override fun getItemCount(): Int {
        return records.size
    }
}