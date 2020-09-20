package com.jagito.findTheWordKotlin.model

import android.content.Context
import com.securepreferences.SecurePreferences

class LocalStorage(context: Context) {
    //private val pref = SecurePreferences(context, "FIND_THE_WORD", "LocalStorage")
    private val pref = context.getSharedPreferences("LocalStorage", Context.MODE_PRIVATE)

    var isSaved:Boolean
        get() = pref.getBoolean("IS_SAVED",false)
        set(value) = pref.edit().putBoolean("IS_SAVED",value).apply()
    var score: Int
        get() = pref.getInt("SCORE", 120)
        set(value) = pref.edit().putInt("SCORE", value).apply()

    var level: Int
        get() = pref.getInt("LEVEL", 0)
        set(value) = pref.edit().putInt("LEVEL", value).apply()

    var answer: ArrayList<String>
        set(value) {
                val e = pref.edit()
                e.putInt("SIZE_OF_ANSWER", value.size)
                value.forEachIndexed { index, i ->
                    e.putString("ANSWER_$index", i)
                }
            e.apply()
        }
        get() {
            val ls = ArrayList<String>()
            val size = pref.getInt("SIZE_OF_ANSWER", 0)
            for (index in 0 until size) {
                val t = pref.getString("ANSWER_$index", "")
                ls.add(t.toString())
            }
            return ls
        }
    var shuffleAnswer: String
        get() = pref.getString("SHUFFLE", "").toString()
        set(value) = pref.edit().putString("SHUFFLE", value).apply()

    var isClickable: ArrayList<Boolean>
        set(value) {
            val e = pref.edit()
            e.putInt("SIZE_OF_CLICK", value.size)
            value.forEachIndexed { index, i ->
                e.putBoolean("CLICK_$index", i)

            }
            e.apply()
        }
        get() {
            val ls = ArrayList<Boolean>()
            val size = pref.getInt("SIZE_OF_CLICK", 0)
            for (index in 0 until size) {
                val t = pref.getBoolean("CLICK_$index", false)
                ls.add(t)
            }
            return ls
        }

    var hideList: ArrayList<Boolean>
        set(value) {
            val e = pref.edit()
            e.putInt("SIZE_OF_HIDE", value.size)
            value.forEachIndexed { index, i ->
                e.putBoolean("HIDE_$index", i)

            }
            e.apply()
        }
        get() {
            val ls = ArrayList<Boolean>()
            val size = pref.getInt("SIZE_OF_HIED", 0)
            for (index in 0 until size) {
                val t = pref.getBoolean("HIDE_$index", false)
                ls.add(t)
            }
            return ls
        }

    fun clear(){
        pref.edit().clear().apply()
    }
}