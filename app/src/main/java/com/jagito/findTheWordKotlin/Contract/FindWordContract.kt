package com.jagito.findTheWordKotlin.Contract

import com.jagito.findTheWordKotlin.model.LocalStorage
import com.jagito.findTheWordKotlin.model.QuestionData

interface FindWordContract {

    interface Model {
        val questions: List<QuestionData>
        val pref: LocalStorage
        var score: Int
    }

    interface View {

        fun countAnswerButtons(): Int

        fun countVariantButtons(): Int
        fun hideVariant(pos: Int)

        fun goneAnswer(pos: Int)

        fun showVariant(pos: Int)

        fun isShownVariant(pos: Int): Boolean

        fun getAnswerText(pos: Int): String

        fun getVariantText(pos: Int): String

        fun setAnswerText(pos: Int, text: String?)

        fun setVariantText(pos: Int, text: String)

        fun setAnswerVisibility(pos: Int)

        fun setVariantVisibilityOFF(pos: Int)

        fun setVariantVisibilityON(index: Int)

        fun wrongAnswerMessage()

        fun hideWrongAnswerMessage()

        fun correctMessage()

        fun hideCorrectMessage()

        fun showFinish()

        fun setLevel(level: Int)

        fun setScore(score: Int)

        fun setImage(imageID: Int)

        fun show_showLetterDialog()

        fun hide_showLetterDialog()

        fun show_deleteLetterDialog()

        fun hide_deleteLetterDialog()

        fun show_restartDialog()

        fun hidde_restartDialog()

        fun getTagAnswer(index: Int): String

        fun getTagVariant(index: Int): String

        fun setTagAnswer(index: Int, tag: String)

        fun setTagVariant(index: Int, tag: String)

        fun clickableOFF(index: Int)
        fun clickableON(index: Int)

        fun showToast(message: String)

        fun setDefaultBackgroundAnswer(index: Int)

        fun isSelected(index: Int): Boolean


    }

    interface Presenter {
        fun clickVariant(pos: Int)

        fun clickAnswer(pos: Int)

        fun loadData()

        fun onClickContinue()

        fun onClickClearVariant()

        fun onClickRestart()

        fun onClickNO()

        fun onClickYES(key: String)

        fun onClickShowAnswer()

        fun saveData()

        fun restore()
    }
}