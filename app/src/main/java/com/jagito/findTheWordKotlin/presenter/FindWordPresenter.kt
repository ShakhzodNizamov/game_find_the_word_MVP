package com.jagito.findTheWordKotlin.presenter


import android.util.Log
import com.jagito.findTheWordKotlin.Contract.FindWordContract
import com.jagito.findTheWordKotlin.model.QuestionData


import java.util.ArrayList
import java.util.Random

class FindWordPresenter(
    private val model: FindWordContract.Model,
    private val view: FindWordContract.View
) : FindWordContract.Presenter {
    private var shuffleAnswer: String? = null
    private var level: Int = 0
    private val addScore = 10
    private var isRestored = false

    private val currentQuestion: QuestionData
        get() = model.questions[level]

    private fun makeShuffleChar(forShuffle: String): String {

        val tmpList = ArrayList<String>()
        for (c in forShuffle) {
            tmpList.add(c.toString())
        }
        while (tmpList.size < 16) tmpList.add(('A'.toInt() + Random().nextInt(24)).toChar().toString())
        tmpList.shuffle()
        var tmp = ""
        for (s in tmpList) {
            tmp += s
        }
        return tmp
    }

    override fun clickVariant(pos: Int) {
        val q = currentQuestion
        val count = q.answer.length
        val text = view.getVariantText(pos)
        for (i in 0 until count) {
            if (view.getAnswerText(i).isEmpty()) {
                view.hideVariant(pos)
                view.setAnswerText(i, text)
                checkAnswer()
                return
            }
        }

    }

    override fun clickAnswer(pos: Int) {
        view.hideWrongAnswerMessage()
        val text = view.getAnswerText(pos)

        if (text.isEmpty()) return
        val count = view.countVariantButtons()
        for (i in 0 until count) {
            if (text == view.getVariantText(i) && view.isShownVariant(i)) {
                view.showVariant(i)
                view.setAnswerText(pos, null)
                return
            }
        }
    }

    override fun loadData() {
        if (model.pref.isSaved) {
            restore()

        } else {
            val q = currentQuestion
            shuffleAnswer = makeShuffleChar(q.answer)
        }
        clearButtons()
        setGone()
        setImage()
        setScore()
        setLevel()

        loadVariantButtons()
        model.pref.isSaved = false
        isRestored = false
        model.pref.clear()
    }

    override fun onClickContinue() {
        view.hideCorrectMessage()
        level++
        updateScore()
        loadData()
    }

    override fun onClickClearVariant() {
        view.show_deleteLetterDialog()
    }

    override fun onClickNO() {
        view.hide_deleteLetterDialog()
        view.hide_showLetterDialog()
        view.hidde_restartDialog()
    }

    override fun onClickYES(key: String) {
        view.hide_showLetterDialog()
        view.hide_deleteLetterDialog()
        if (key == "SHOW_LETTER") {
            showLetter()
        }
        if (key == "DELETE_LETTER") {
            deleteLetter()
        }
        if (key == "RESTART") {
            restartGame()
        }
    }

    override fun onClickShowAnswer() {
        view.show_showLetterDialog()
    }

    override fun saveData() {
        model.pref.isSaved = true
        model.pref.level = level
        model.pref.score = model.score
        model.pref.isClickable = getAnswerClickableList()
        model.pref.hideList = getVariantHideList()
        model.pref.answer = getSelectedAnswersList()
        model.pref.shuffleAnswer = shuffleAnswer.toString()
    }

    override fun restore() {


        level = model.pref.level
        model.score = model.pref.score
        shuffleAnswer = model.pref.shuffleAnswer

        // model.pref.clear()
    }

    private fun setLevel() {
        view.setLevel(level + 1)
    }

    private fun loadVariantButtons() {
        for (i in shuffleAnswer!!.indices) {
            view.setVariantVisibilityON(i)
            view.setVariantText(i, shuffleAnswer!![i].toString())
        }

        restoreVisibilityVariantButtons()

    }

    private fun clearButtons() {

        for (i in 0 until view.countAnswerButtons()) {
            view.setAnswerVisibility(i)
            view.clickableON(i)
            view.setAnswerText(i, null)
            view.setDefaultBackgroundAnswer(i)
        }
        for (i in 0 until view.countAnswerButtons()) {
            view.showVariant(i)

        }
        // if (model.pref.isSaved) {
        restoreLettersOfAnswerButton()
        restoreClickableAnswerButtons()
        // }
    }

    private fun setGone() {
        val length = currentQuestion.answer.length
        for (i in length until view.countAnswerButtons()) {
            view.goneAnswer(i)
        }
    }

    private fun checkAnswer() {
        val answer = currentQuestion.answer
        var textFromButtons = ""
        for (i in answer.indices) {

            textFromButtons += view.getAnswerText(i)
        }
        val isCorrect = answer == textFromButtons
        if (textFromButtons.length == currentQuestion.answer.length)
            if (isCorrect) {
                if (level + 1 == model.questions.size) {
                    view.showFinish()
                    return
                }
                view.correctMessage()
            } else
                view.wrongAnswerMessage()
    }

    private fun setScore() {
        view.setScore(model.score)
    }

    private fun updateScore() {
        model.score = model.score + addScore
        setScore()
    }

    private fun setImage() {
        view.setImage(currentQuestion.imgId)
    }

    private fun showLetter() {
        if (model.score < 40) {
            view.showToast("You don`t have enough coins !!!")
            return
        }
        val length = currentQuestion.answer.length
        var i = Random().nextInt(length)

        while (view.getTagAnswer(i) != "non") {
            if (checkMaxShows())
                i = Random().nextInt(length)
            else
                break
        }

        view.setTagAnswer(i, "added")
        view.setAnswerText(i, currentQuestion.answer[i].toString())
        view.clickableOFF(i)
        checkAnswer()
        model.score = (model.score - 40)
        setScore()
        for (j in 0 until view.countVariantButtons()) {
            if (view.getVariantText(j) == (currentQuestion.answer[i].toString())) {
                view.setVariantVisibilityOFF(i)
                break
            }
        }
    }

    private fun deleteLetter() {
        if (model.score < 40) {
            view.showToast("You don`t have enough coins !!!")
            return
        }
        val length = shuffleAnswer!!.length
        var i = Random().nextInt(length - 1)
        while (view.getTagVariant(i) != "non" && !isAnswer(view.getVariantText(i))) {
            i = Random().nextInt(length - 1)
        }

        view.setVariantVisibilityOFF(i)
        view.setTagVariant(i, "added")
        model.score = (model.score - 40)
        checkAnswer()
        setScore()
    }

    override fun onClickRestart() {

        view.show_restartDialog()

    }

    private fun isAnswer(s: String): Boolean {
        val ans = currentQuestion.answer
        for (i in ans.indices) {
            if (s[0] == ans[i]) {
                return true
            }
        }
        return false
    }

    private fun checkMaxShows(): Boolean {
        for (i in currentQuestion.answer.indices) {
            if (view.getTagAnswer(i) == "non") {
                return true
            }
        }
        view.showToast("Max shows")
        checkAnswer()
        return false
    }

    private fun getAnswerClickableList(): ArrayList<Boolean> {
        val ls = ArrayList<Boolean>()
        for (i in currentQuestion.answer.indices) {
            ls.add(view.isSelected(i))
        }
        return ls

    }

    private fun getVariantHideList(): ArrayList<Boolean> {
        val ls = ArrayList<Boolean>()
        for (i in shuffleAnswer!!.indices) {
            ls.add(view.isShownVariant(i))
        }
        return ls
    }

    private fun getSelectedAnswersList(): ArrayList<String> {
        val ls = ArrayList<String>()
        for (i in currentQuestion.answer.indices) {
            ls.add(view.getAnswerText(i))
        }
        return ls
    }

    /*
    private fun getSelectedVariantsList(): ArrayList<String> {
         val ls = ArrayList<String>()
         for (i in shuffleAnswer!!.indices) {
             ls.add(view.getAnswerText(i))
         }
         return ls
     } */

    private fun restoreClickableAnswerButtons() {
        val ls = model.pref.isClickable

        ls.forEachIndexed { index, b ->
            Log.d("LOL", "ttttttttt = $b,   size= ${ls.size}")
            if (!b) {
                view.clickableOFF(index)
            }
        }
        Log.d("LOL", "++-+-+-++-+--+-+-+-+-+-+-+-+-+-+-+-+-+-+--+-+-+-+-+-+-+-+-+-")
    }

    private fun restoreLettersOfAnswerButton() {
        val ls = model.pref.answer

        ls.forEachIndexed { index, s ->
            if (s.isNotEmpty()) {
                view.setAnswerText(index, s)
            }
        }
    }

    private fun restoreVisibilityVariantButtons() {
        val ls = model.pref.hideList

        ls.forEachIndexed { index, b ->
            if (b) {
                view.setVariantVisibilityOFF(index)
            }
        }
    }

    private fun restartGame(){
        view.hidde_restartDialog()
        view.hideWrongAnswerMessage()
        model.pref.clear()
        level = 0
        model.score = 120
        loadData()
    }
}