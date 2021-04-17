package com.example.topquiz_kotlin.controler

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.topquiz_kotlin.R
import com.example.topquiz_kotlin.model.Question
import com.example.topquiz_kotlin.model.QuestionBank

class GameActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mGameQuestion: TextView
    private lateinit var mButtonAnswer1: Button
    private lateinit var mButtonAnswer2: Button
    private lateinit var mButtonAnswer3: Button
    private lateinit var mButtonAnswer4: Button
    private lateinit var mQuestionNumber: TextView

    private lateinit var mQuestionBank: QuestionBank
    private lateinit var mQuestion: Question
    private var mNumberOfQuestion = 0
    private var mScore = 0
    private var mNumberCurrentQuestion = 1
    val numberOfQuestion = 10

    /* Booleen verifiant si l'action de l'utilisateur est pris en compte ou pas :
    true pour oui et false pour non */
    private var mEnableTouchEvents = false

    companion object{
        /* Variable de classe pour stocker le nom de la cle de l'Intent
        Cette cle sera utilise par la MainActivity pour recuperer le score */
        const val BUNDLE_EXTRA_SCORE = "BUNDLE_EXTRA_SCORE"
        /* Variables de classe stockant les cles du score et du nombre de question en cours */
        const val BUNDLE_STATE_SCORE = "currentScore"
        const val BUNDLE_STATE_QUESTION = "currentQuestion"

        const val BUNDLE_STATE_NUM_CURRENT_QUESTION = "numberCurrentQuestion"
        const val SELECTED_QUIZ = "selectedQuiz"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        println("GameActivity :: onCreate()")

        if(savedInstanceState != null){
            mScore = savedInstanceState.getInt(BUNDLE_STATE_SCORE)
            mNumberOfQuestion = savedInstanceState.getInt(BUNDLE_STATE_QUESTION)
            mNumberCurrentQuestion = savedInstanceState.getInt(BUNDLE_STATE_NUM_CURRENT_QUESTION)
        }else{
            mScore = 0
            mNumberOfQuestion = numberOfQuestion
            mNumberCurrentQuestion = 1
        }

        // Recuperation du quiz choisi depuis la MainActivity
        val extras = intent.extras
        if (extras != null) {
            val value = extras.getInt(SELECTED_QUIZ)
            mQuestionBank = this.questionGeneration(value)
        }

        mQuestionNumber = findViewById<TextView>(R.id.number_current_question)
        mGameQuestion = findViewById<TextView>(R.id.game_question)
        mButtonAnswer1 = findViewById<Button>(R.id.game_answer1)
        mButtonAnswer2 = findViewById<Button>(R.id.game_answer2)
        mButtonAnswer3 = findViewById<Button>(R.id.game_answer3)
        mButtonAnswer4 = findViewById<Button>(R.id.game_answer4)
        
        mButtonAnswer1.setOnClickListener(this)
        mButtonAnswer2.setOnClickListener(this)
        mButtonAnswer3.setOnClickListener(this)
        mButtonAnswer4.setOnClickListener(this)
        // On place un identifiant sur chaque bouton pour connaitre sur lequel l'utilisateur a clique
        mButtonAnswer1.tag = 0
        mButtonAnswer2.tag = 1
        mButtonAnswer3.tag = 2
        mButtonAnswer4.tag = 3
        
        mQuestion = mQuestionBank.getQuestion()
        displayQuestion(mQuestion)
        
        mEnableTouchEvents = true
    }
    
    private fun questionGeneration(choiceQuiz : Int) : QuestionBank {
        val listQuiz : List<Question> = if(choiceQuiz == 1){
            narutoShippListQuestion
        }else if (choiceQuiz == 2){
            snkListQuestion
        }else if (choiceQuiz == 3){
            opListQuestion
        }else{
            mhaListQuestion
        }
        return QuestionBank(listQuiz)
    }

    private fun displayQuestion(question : Question){
        mQuestionNumber.text = "${mNumberCurrentQuestion}/${numberOfQuestion}"
        mGameQuestion.text = question.getQuestion()
        val reponse = question.getChoiceList()
        mButtonAnswer1.setText(reponse.get(0))
        mButtonAnswer2.setText(reponse.get(1))
        mButtonAnswer3.setText(reponse.get(2))
        mButtonAnswer4.setText(reponse.get(3))
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(BUNDLE_STATE_SCORE, mScore)
        outState.putInt(BUNDLE_STATE_QUESTION, mNumberOfQuestion)
        outState.putInt(BUNDLE_STATE_NUM_CURRENT_QUESTION, mNumberCurrentQuestion)
    }

    override fun onClick(v: View?) {
        val reponseIndex = v?.tag as Int
        if(reponseIndex == mQuestion.getAnswerIndex()){
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show()
            mScore++
        }else{
            Toast.makeText(this, "Incorrect", Toast.LENGTH_SHORT).show()
        }
        mEnableTouchEvents = false
        mNumberOfQuestion--
        mNumberCurrentQuestion++

        /* La classe Handler permet de communiquer avec systeme
           La methode .postDelayed permettant d'effectuer une tache apres une duree de temps precise
           Les taches a effectuer sont dans la fonction run() */
        Handler().postDelayed({
            mEnableTouchEvents = true
            if (mNumberOfQuestion == 0) {
                // Fin du jeu
                end()
            } else {
                // Question suivante
                mQuestion = mQuestionBank.getQuestion()
                displayQuestion(mQuestion)
            }
        }, 2000)
    }

    private fun end(){
        val builder = AlertDialog.Builder(this)
        if(mScore < 5){
            builder.setTitle("Pitoyable !") // Titre de la boite de dialogue
                .setMessage("Votre score est de $mScore") // Message de la boite de dialogue
                // Message du bouton et implementation de l'interface qui gere le clic du bouton
                .setPositiveButton(
                    "OK"
                ) { dialog, which -> /* Envoi du resultat (score) a une autre activite */
                    val intent = Intent()
                    intent.putExtra(BUNDLE_EXTRA_SCORE, mScore)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }.create().show()
        }else{
            builder.setTitle("Bravo")
                .setMessage("Votre score est de $mScore")
                .setPositiveButton(
                    "OK"
                ) { dialog, which ->
                    val intent = Intent()
                    intent.putExtra(BUNDLE_EXTRA_SCORE, mScore)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }.create().show()
        }
    }

    /* Methode permettant de dire au systeme si l'action de l'utilisateur doit etre pris en compte ou non
    en fonction de la valeur de verite renvoye
     */
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return mEnableTouchEvents && super.dispatchTouchEvent(ev)
    }

    /* Les differents etats d'une application mobile sont les suivantes
    en plus de la methode onCreate() plus haut */
    override fun onStart() {
        super.onStart()
        println("GameActivity :: onStart()")
    }

    override fun onPause() {
        super.onPause()
        println("GameActivity :: onPause()")
    }

    override fun onResume() {
        super.onResume()
        println("GameActivity :: onResume()")
    }

    override fun onStop() {
        super.onStop()
        println("GameActivity :: onStop()")
    }

    override fun onDestroy() {
        super.onDestroy()
        println("GameActivity :: onDestroy()")
    }
}