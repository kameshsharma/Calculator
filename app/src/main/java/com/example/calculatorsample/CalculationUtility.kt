package com.example.calculatorsample
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.ArrayList
class CalculationUtility {

    private var operators: ArrayList<Char> = ArrayList()
    private var history = ArrayList<CalculatorStorage>()

    init {
        operators.add('*')
        operators.add('+')
        operators.add('/')
        operators.add('-')
    }

    companion object {
        private var storageUtility: CalculationUtility? = null

        fun getInstance(): CalculationUtility? {
            if (storageUtility == null) {
                storageUtility = CalculationUtility()
            }
            return storageUtility
        }

        fun clearInstance() {
            storageUtility = null
        }
    }

    fun setCalculateResult(storage: CalculatorStorage,function: () -> Unit) {
        if (history.size == 11) {
            function.invoke()
        } else {
            history.add(storage)
        }
    }

    fun getHistory(): List<CalculatorStorage> {
        return history
    }

    fun findPostfix(inputP: String)//this will return the postfix expression of given input
            : String {
        var inputP = inputP
        if (!validateInput(inputP))
            return "Invalid expression"
        val temp = StringBuilder()
        val al = ArrayList<Char>()
        al.add('(')
        var ch: Char
        inputP = "$inputP)"
        var i = 0
        while (i < inputP.length) {
            ch = inputP[i]
            when {
                ch == '(' -> al.add('(')
                operators.contains(ch) -> {
                    al.add(ch)
                    temp.append(pop(al, ch))
                }
                ch == ')' -> temp.append(pop(al))
                else -> {
                    while (ch != '(' && ch != ')' && !operators.contains(ch)) {
                        temp.append(ch)
                        i += 1
                        ch = inputP[i]
                    }
                    temp.append(" ")
                    i -= 1
                }
            }
            i++

        }
        return temp.toString()
    }

    private fun pop(al: ArrayList<Char>, ch: Char): String {
        var temp = ""
        var i = al.size - 1
        while (al[i] != '(' && i >= 0) {
            if (operators.indexOf(al[i]) < operators.indexOf(ch)) {
                temp += al[i]
                al.removeAt(i)
            }
            i--
        }
        return temp
    }

    private fun validateInput(input: String): Boolean {
        if (input.replace("[^(]".toRegex(), "").length != input.replace(
                "[^)]".toRegex(),
                ""
            ).length || input.split("[^\\d]".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().isEmpty() || input.isEmpty()
        )
            return false

        var pattern = Pattern.compile("(\\d\\()|(\\)\\d)|(\\([*\\/+^-])|([*\\/+^-]\\))")
        var matcher = pattern.matcher(input)
        if (matcher.find())
            return false

        //
        pattern = Pattern.compile("[^\\d|+*^()\\/-]")
        matcher = pattern.matcher(input)
        if (matcher.find())
            return false


        //to check if 2 consecutive operators are there...
        pattern = Pattern.compile("[*-+^\\/]{2}")
        matcher = pattern.matcher(input)
        return !matcher.find()

    }


    private fun pop(al: ArrayList<Char>)// pop all operators until '(' reached and add them to temp
            : String {
        var temp = ""
        var i = al.size - 1
        while (al[i] != '(' && i >= 0) {
            temp += al[i]
            al.removeAt(i)
            i--
        }
        al.removeAt(al.size - 1)
        return temp
    }


    fun evaluatePostfix(inputP: String)//this will return the final result after evaluating postfix expression
            : Float {
        val stack = Stack<Float>()
        var ch: Char
        var inputA: Float
        var inputB: Float
        var i = 0
        while (i < inputP.length) {
            ch = inputP[i]

            if (operators.contains(ch)) {
                inputB = stack.pop()
                inputA = stack.pop()
                when (ch) {
                    '*' -> stack.push(inputA * inputB)
                    '+' -> stack.push(inputA + inputB)
                    '/' -> stack.push(inputA / inputB)
                    '-' -> stack.push(inputA - inputB)
                }
            } else {
                val temp = StringBuilder()
                while (!operators.contains(ch) && i < inputP.length && ch != ' ') {
                    temp.append(ch)
                    i += 1
                    ch = inputP[i]
                }
                stack.push(java.lang.Float.parseFloat(temp.toString()))
            }
            i++

        }
        return stack.pop()
    }

}