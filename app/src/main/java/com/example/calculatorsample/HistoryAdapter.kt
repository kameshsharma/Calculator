package com.example.calculatorsample

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_history_item.view.*

class HistoryAdapter(private val list:List<CalculatorStorage>): RecyclerView.Adapter<HistoryAdapter.HistoryHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryHolder {
        return HistoryHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_history_item,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: HistoryHolder, position: Int) {
        holder.bindItem(list[position])
    }

    inner class HistoryHolder(itemView:View): RecyclerView.ViewHolder(itemView) {

        fun bindItem(calculatorStorage: CalculatorStorage) {
            itemView.tv_input.text = calculatorStorage.inputString

            if (calculatorStorage.outputResult.equals("0.0",true)) {
                itemView.tv_result.text = ""
            } else {
                itemView.tv_result.text = calculatorStorage.outputResult
            }

        }
    }
}