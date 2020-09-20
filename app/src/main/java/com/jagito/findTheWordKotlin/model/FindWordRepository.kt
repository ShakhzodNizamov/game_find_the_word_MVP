package com.jagito.findTheWordKotlin.model

import com.jagito.findTheWordKotlin.Contract.FindWordContract
import com.jagito.findTheWordKotlin.R
import java.util.ArrayList

class FindWordRepository(override val pref: LocalStorage) : FindWordContract.Model {

    private val list = ArrayList<QuestionData>()
    override var score = 120

    override val questions: List<QuestionData>
        get() = list

    init {
        list.add(QuestionData(R.drawable.school, "SCHOOL"))
        list.add(QuestionData(R.drawable.metro, "METRO"))
        list.add(QuestionData(R.drawable.sweet, "SWEET"))
        list.add(QuestionData(R.drawable.run, "RUN"))
        list.add(QuestionData(R.drawable.paper, "PAPER"))
        list.add(QuestionData(R.drawable.pencil, "PENCIL"))
        list.add(QuestionData(R.drawable.hotdog, "HOTDOG"))
        list.add(QuestionData(R.drawable.android, "ANDROID"))
        list.add(QuestionData(R.drawable.uzbek, "UZBEK"))
        list.add(QuestionData(R.drawable.apple, "APPLE"))
    }
}