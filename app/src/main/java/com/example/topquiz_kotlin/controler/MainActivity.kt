package com.example.topquiz_kotlin.controler

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.topquiz_kotlin.R
import com.example.topquiz_kotlin.model.User
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {

    private lateinit var mGreetingText: TextView
    private lateinit var mNameInput: TextInputEditText
    private lateinit var mQuizOptions: RadioGroup
    private lateinit var mPlayButton: Button

    private lateinit var mUser: User

    // Variable de l'API permettant de stocker des informations
    private lateinit var mPreferences: SharedPreferences

    companion object {
        // Variable d'instance definissant l'identifiant de la GameActivity
        const val GAME_ACTIVITY_REQUEST_CODE = 1

        // Variable d'instance definissant l'identifiant du nom de l'utilisateur/joueur
        const val FIRSTNAME_PLAYER = "FIRSTNAME_PLAYER"

        // Variable d'instance definissant l'identifiant du score de l'utilisateur/joueur
        const val SCORE_PLAYER = "SCORE_PLAYER"

        // Variables d'instance envoyant l'information dans la GameActivite et celle stockant l'information
        const val SELECTED_QUIZ = "selectedQuiz"
        const val CURRENT_SELECTED_QUIZ = "currentSelectedQuiz"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        println("MainActivity :: onCreate()")

        mPreferences = getPreferences(Context.MODE_PRIVATE)

        mGreetingText = findViewById<TextView>(R.id.text_start)
        mNameInput = findViewById<TextInputEditText>(R.id.edit_start_text)
        mPlayButton = findViewById<Button>(R.id.first_button)
        mQuizOptions = findViewById(R.id.quiz_options)

        mPlayButton.isEnabled = false   // Desactive le button au demarrage

        mUser = User()
        salutationUtilisateur()

        /* La methode .addTextChangedListener()
        appelle la fonction onTextChanged qui verifie si un text est entre dans l'EditText */
        mNameInput.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    mPlayButton.isEnabled = s.toString().length != 0
                }

                override fun afterTextChanged(s: Editable) {}
        })

        // On met le bouton en ecoute, en attente d'un click
        mPlayButton.setOnClickListener {
            /* Instructions permettant de quitter d'une activite a une autre */
            val gameActivity = Intent(this@MainActivity, GameActivity::class.java)
            val selectedQuiz = when(mQuizOptions.checkedRadioButtonId){
                R.id.quiz_option_1 -> 1
                R.id.quiz_option_2 -> 2
                R.id.quiz_option_3 -> 3
                else -> 4
            }
            gameActivity.putExtra(SELECTED_QUIZ, selectedQuiz) // Envoi du quiz choisi dans la GameActivity
            startActivityForResult(gameActivity, GAME_ACTIVITY_REQUEST_CODE) // Demarrage de l'activite avec attente de resultat

            mUser.setFirstName(mNameInput.text.toString())
            // Insertion du nom et du quiz choisi dans le fichier
            mPreferences.edit().putString(FIRSTNAME_PLAYER, mUser.getFirstName()).apply()
            mPreferences.edit().putInt(CURRENT_SELECTED_QUIZ, selectedQuiz).apply()
        }
    }

    // Methode qui recupere le resultat d'une autre activite
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        /*
        Les conditions du if signifie :
        1 ere : Verification de l'identifiant de l'activite dont on veut le resultat
        2 eme : Verification de la finition de la dites activite
         */
        if(GAME_ACTIVITY_REQUEST_CODE == requestCode && Activity.RESULT_OK == resultCode){
            // Recuperation du score et du nombre de question pose de l'activite
            val score = data?.getIntExtra(GameActivity.BUNDLE_EXTRA_SCORE, 0)
            if (score != null) {
                // Insertion du score dans le fichier
                mPreferences.edit().putInt(SCORE_PLAYER, score).apply()
            }

            salutationUtilisateur()
        }
    }

    private fun salutationUtilisateur(){
        val firstname = mPreferences.getString(FIRSTNAME_PLAYER, null)
        if(firstname != null){
            val score = mPreferences.getInt(SCORE_PLAYER, 0)
            val quiz = when(mPreferences.getInt(CURRENT_SELECTED_QUIZ, 2)){
                1 -> "Naruto Shippuden"
                2 -> "Shingeki No Kyojin"
                3 -> "One Piece"
                else -> "My Hero Academia"
            }
            if(score >= 10){
                val salutation = "Bon retour, ${firstname} !\nTon dernier score etait de ${score}/10 dans le quiz ${quiz}, une nouvelle partie ?"
                mGreetingText.text = salutation
            }else{
                val salutation = "Bon retour, ${firstname} !\nTon dernier score etait de ${score}/10 dans le quiz ${quiz}, feras-tu mieux cette fois ?"
                mGreetingText.text = salutation
            }
            mNameInput.setText(firstname)
            mNameInput.setSelection(firstname.length)
            mPlayButton.isEnabled = true

            // Coche la dernier case de quiz choisie
            mQuizOptions.check(when(quiz){
                "Naruto Shippuden" -> R.id.quiz_option_1
                "Shingeki No Kyojin" -> R.id.quiz_option_2
                "One Piece" -> R.id.quiz_option_3
                else -> R.id.quiz_option_4
            })
        }
    }

    /* Les differents etats d'une application mobile sont les suivantes
    en plus de la methode onCreate() plus haut */
    override fun onStart() {
        super.onStart()
        println("MainActivity :: onStart()")
    }

    override fun onPause() {
        super.onPause()
        println("MainActivity :: onPause()")
    }

    override fun onResume() {
        super.onResume()
        println("MainActivity :: onResume()")
    }

    override fun onStop() {
        super.onStop()
        println("MainActivity :: onStop()")
    }

    override fun onDestroy() {
        super.onDestroy()
        println("MainActivity :: onDestroy()")
    }
}