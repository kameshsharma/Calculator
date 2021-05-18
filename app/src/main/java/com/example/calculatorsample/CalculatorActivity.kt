package com.example.calculatorsample

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_main.*
class CalculatorActivity : AppCompatActivity(), View.OnClickListener {

    private var isEqualClicked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
    }

    private fun initViews() {
        iv_history?.setOnClickListener(this)
        button_divide?.setOnClickListener(this)
        buttonAdd?.setOnClickListener(this)
        buttonSubtraction?.setOnClickListener(this)
        buttonMultiply?.setOnClickListener(this)
        buttonEqual?.setOnClickListener(this)
        button_clear?.setOnClickListener(this)
        button7?.setOnClickListener(this)
        button8?.setOnClickListener(this)
        button9?.setOnClickListener(this)
        button4?.setOnClickListener(this)
        button5?.setOnClickListener(this)
        button6?.setOnClickListener(this)
        button1?.setOnClickListener(this)
        button2?.setOnClickListener(this)
        button3?.setOnClickListener(this)
        buttonZero?.setOnClickListener(this)
    }

    @SuppressLint("SetTextI18n")
    override fun onClick(view: View?) {
        when(view?.id) {
            R.id.button_clear -> {
                tv_result.text = ""
            }
            R.id.iv_history -> {
                showBottomSheet()
            }
            R.id.button_divide -> {
                if (!isLabelEmpty()) {
                    enterNewData()
                    tv_result.text = "${tv_result.text}/"
                }
            }
            R.id.buttonAdd -> {
                if (!isLabelEmpty()) {
                    enterNewData()
                    tv_result.text = "${tv_result.text}+"
                }
            }
            R.id.buttonSubtraction -> {
                if (!isLabelEmpty()) {
                    enterNewData()
                    tv_result.text = "${tv_result.text}-"
                }
            }
            R.id.buttonMultiply -> {
                if (!isLabelEmpty()) {
                    enterNewData()
                    tv_result.text = "${tv_result.text}*"
                }
            }
            R.id.buttonEqual -> {
                if (!isLabelEmpty()){
                    isEqualClicked = true
                    evaluateExpression()
                }
            }
            R.id.button1 -> {
                enterNewData()
                tv_result.text = "${tv_result.text}1"
            }
            R.id.button2 -> {
                enterNewData()
                tv_result.text = "${tv_result.text}2"
            }
            R.id.button3 -> {
                enterNewData()
                tv_result.text = "${tv_result.text}3"
            }
            R.id.button4 -> {
                enterNewData()
                tv_result.text = "${tv_result.text}4"
            }
            R.id.button5 -> {
                enterNewData()
                tv_result.text = "${tv_result.text}5"
            }
            R.id.button6 -> {
                enterNewData()
                tv_result.text = "${tv_result.text}6"
            }
            R.id.button7 -> {
                enterNewData()
                tv_result.text = "${tv_result.text}7"
            }
            R.id.button8 -> {
                enterNewData()
                tv_result.text = "${tv_result.text}8"
            }
            R.id.button9 -> {
                enterNewData()
                tv_result.text = "${tv_result.text}9"
            }
            R.id.buttonZero -> {
                enterNewData()
                tv_result.text = "${tv_result.text}0"
            }
        }
    }

    private fun isLabelEmpty(): Boolean {
        if (tv_result.text.isEmpty()) {
            Toast.makeText(this,"Please enter data",Toast.LENGTH_LONG).show()
            return true
        }
        return false
    }

    private fun enterNewData() {
        if (isEqualClicked) {
            tv_result.text = ""
            isEqualClicked = false
        }
    }

    private fun showBottomSheet() {
        val dialog = BottomSheetDialog(this)
        dialog.setContentView(R.layout.dialog_show_history)
        dialog.dismissWithAnimation = true
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
//        dialog.behavior.peekHeight = view?.measuredHeight?:0
        dialog.behavior.isFitToContents = true


        val historyRecyclerView = dialog.findViewById<RecyclerView>(R.id.rv_history_list)
        val imageView = dialog.findViewById<ImageView>(R.id.iv_close)

        historyRecyclerView?.layoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,false)
        historyRecyclerView?.adapter = CalculationUtility.getInstance()?.getHistory()?.let {
            HistoryAdapter(it)
        }

        imageView?.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun evaluateExpression() {
        val instance = CalculationUtility.getInstance()
        val expression = tv_result.text.toString()

        val postfixExpression = instance?.findPostfix(expression)?:""
        if(postfixExpression.equals("Invalid expression",true)) {
            tv_result.text = ""
            return
        }

        val result = instance?.evaluatePostfix(postfixExpression)?:0F

        //data store in Singleton object
        CalculationUtility.getInstance()?.setCalculateResult(CalculatorStorage(tv_result.text.toString(), result.toString())){
            Toast.makeText(this,"You can store only 10 data",Toast.LENGTH_LONG).show()
        }

        tv_result.text = result.toString()
    }

    override fun onDestroy() {
        CalculationUtility.clearInstance()
        super.onDestroy()
    }
}