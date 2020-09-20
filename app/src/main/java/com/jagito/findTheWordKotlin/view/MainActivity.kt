package com.jagito.findTheWordKotlin.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.jagito.findTheWordKotlin.Contract.FindWordContract
import com.jagito.findTheWordKotlin.R
import com.jagito.findTheWordKotlin.model.FindWordRepository
import com.jagito.findTheWordKotlin.model.LocalStorage
import com.jagito.findTheWordKotlin.presenter.FindWordPresenter
import kotlinx.android.synthetic.main.activity_main.*
import java.util.ArrayList

class MainActivity : AppCompatActivity(), FindWordContract.View {


    private val answers = ArrayList<Button>()
    private val variants = ArrayList<Button>()


    private val preference by lazy { LocalStorage(this) }
    private lateinit var model: FindWordContract.Model
    private  val presenter: FindWordContract.Presenter by lazy {  FindWordPresenter(model, this) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        model = FindWordRepository(preference)

        loadView()
        presenter.loadData()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.saveData()
    }

    override fun onPause() {
        super.onPause()
        presenter.saveData()
    }

    override fun onStop() {
        super.onStop()
        presenter.saveData()
    }
    private fun loadView() {
        val answersGroup = layoutAnswer
        val variantTop = VariantsTop
        val variantBottom = VariantsBottom

        addToList(answersGroup, "ANSWER")
        addToList(variantTop, "VARIANT")
        addToList(variantBottom, "VARIANT")

        listener(answers, "ANSWER")
        listener(variants, "VARIANT")
        buttonBack.setOnClickListener { finish() }
        btn_toMain.setOnClickListener { finish() }
        btn_continue.setOnClickListener { presenter.onClickContinue() }

        show_letter.setOnClickListener { presenter.onClickShowAnswer() }
        delete_letter.setOnClickListener { presenter.onClickClearVariant() }
        restart.setOnClickListener { presenter.onClickRestart() }

        btn_ok_of_correct.setOnClickListener { presenter.onClickYES("SHOW_LETTER") }
        btn_ok_of_delete.setOnClickListener { presenter.onClickYES("DELETE_LETTER") }
        btn_ok_of_restart.setOnClickListener { presenter.onClickYES("RESTART") }

        btn_cancel_of_correct.setOnClickListener { presenter.onClickNO() }
        btn_cancel_of_delete.setOnClickListener { presenter.onClickNO() }
        btn_cancel_of_restart.setOnClickListener { presenter.onClickNO() }
    }

    private fun listener(buttons: ArrayList<Button>, key: String) {

        for (i in buttons.indices) {
            if (key == "ANSWER")
                buttons[i].setOnClickListener { presenter.clickAnswer(i) }
            else
                buttons[i].setOnClickListener { presenter.clickVariant(i) }
        }

    }

    private fun addToList(group: ViewGroup, key: String) {
        for (i in 0 until group.childCount) {
            if (key == "ANSWER") {
                group.getChildAt(i).tag = "non"
                answers.add(group.getChildAt(i) as Button)
            } else {
                group.getChildAt(i).tag = "non"
                variants.add(group.getChildAt(i) as Button)
            }
        }
    }

    override fun hideVariant(pos: Int) {
        variants[pos].visibility = View.INVISIBLE
    }

    override fun goneAnswer(pos: Int) {
        answers[pos].visibility = View.GONE
    }

    override fun showVariant(pos: Int) {
        variants[pos].visibility = View.VISIBLE
    }

    override fun isShownVariant(pos: Int): Boolean {
        return variants[pos].visibility == View.INVISIBLE
    }

    override fun getAnswerText(pos: Int): String {
        return answers[pos].text.toString()
    }

    override fun getVariantText(pos: Int): String {
        return variants[pos].text.toString()
    }

    override fun setAnswerText(pos: Int, text: String?) {
        answers[pos].text = text
    }

    override fun setVariantText(pos: Int, text: String) {
        variants[pos].text = text
    }

    override fun setAnswerVisibility(pos: Int) {
        answers[pos].visibility = View.VISIBLE
    }

    override fun setVariantVisibilityOFF(pos: Int) {
        variants[pos].visibility = View.INVISIBLE

    }

    override fun setVariantVisibilityON(index: Int) {
        variants[index].visibility = View.VISIBLE
    }

    override fun countAnswerButtons(): Int {
        return answers.size
    }

    override fun countVariantButtons(): Int {
        return variants.size
    }

    override fun wrongAnswerMessage() {
        incorrect_answer.visibility = View.VISIBLE
    }

    override fun hideWrongAnswerMessage() {
        incorrect_answer.visibility = View.INVISIBLE
    }

    override fun correctMessage() {
        layout_correct.visibility = View.VISIBLE
    }

    override fun hideCorrectMessage() {
        layout_correct.visibility = View.INVISIBLE
    }

    override fun showFinish() {
        layout_finish.setVisibility(View.VISIBLE)
    }

    override fun setLevel(level: Int) {
        textLevel.text = level.toString()
    }

    override fun setScore(score: Int) {
        textScore.text = score.toString()
    }

    override fun setImage(imageID: Int) {
        imageView.setImageResource(imageID)
    }

    override fun show_showLetterDialog() {
        layout_correct_letter.visibility = View.VISIBLE
    }

    override fun hide_showLetterDialog() {
        layout_correct_letter.visibility = View.INVISIBLE
    }

    override fun show_deleteLetterDialog() {
        layout_delete_letter.visibility = View.VISIBLE
    }

    override fun hide_deleteLetterDialog() {
        layout_delete_letter.visibility = View.INVISIBLE
    }

    override fun show_restartDialog() {
        layout_restart.visibility = View.VISIBLE
    }

    override fun hidde_restartDialog() {
        layout_restart.visibility = View.INVISIBLE
    }

    override fun getTagAnswer(index: Int): String {
        return answers[index].tag.toString()
    }

    override fun getTagVariant(index: Int): String {
        return variants[index].tag.toString()
    }

    override fun setTagAnswer(index: Int, tag: String) {
        answers[index].tag = tag
    }

    override fun setTagVariant(index: Int, tag: String) {
        variants[index].tag = tag
    }

    override fun clickableOFF(index: Int) {
        answers[index].setBackgroundResource(R.drawable.bg_keep_button)
        answers[index].isClickable = false
    }

    override fun clickableON(index: Int) {
        answers[index].isClickable = true
    }

    override fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun setDefaultBackgroundAnswer(index: Int) {
        answers[index].setBackgroundResource(R.drawable.bg_answer)
    }

    override fun isSelected(index: Int): Boolean {
        return answers[index].isClickable
    }
}
