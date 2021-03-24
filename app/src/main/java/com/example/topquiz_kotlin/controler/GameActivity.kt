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

    private lateinit var mQuestionBank: QuestionBank
    private lateinit var mQuestion: Question
    private var mNumberOfQuestion = 0
    private var mScore = 0

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
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        println("GameActivity :: onCreate()")

        if(savedInstanceState != null){
            mScore = savedInstanceState.getInt(BUNDLE_STATE_SCORE)
            mNumberOfQuestion = savedInstanceState.getInt(BUNDLE_STATE_QUESTION)
        }else{
            mScore = 0
            mNumberOfQuestion = 10
        }

        mGameQuestion = findViewById<TextView>(R.id.game_question)
        mButtonAnswer1 = findViewById<Button>(R.id.game_answer1)
        mButtonAnswer2 = findViewById<Button>(R.id.game_answer2)
        mButtonAnswer3 = findViewById<Button>(R.id.game_answer3)
        mButtonAnswer4 = findViewById<Button>(R.id.game_answer4)
        
        mQuestionBank = this.questionGeneration()
        
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
    
    private fun questionGeneration() : QuestionBank {
        val question1 = Question(
            "Comment s'appelle le titan de Eren en Japonais ?",
            listOf(
                "Shingeki No Kyojin",
                "Kyojin",
                "Shingetsu Bahamut",
                "Shingeki"
            ),
            0
        )
        val question2 = Question(
            "Le type de lecteur ciblé par les studios d’edition varie en fonction des editeurs." +
                    "C’est l’exemple de <L'attaque des Titans> qui voit sa cible editoriale variée." +
                    "Qu’elles sont les deux types de mangas sous lequel Shingeki peut facilement se fondre ?",
            listOf(
                "Shonen & Yaoi",
                "Seinen & Josei",
                "Shonen & Seinen",
                "Shonen & Josei"
            ),
            2
        )
        val question3 = Question(
            "Donnez les noms des 3 murs du plus petit au plus grand dans lequel l’humanité se cache " +
                    "pour se proteger des Titans ? ",
            listOf(
                "Mur Sina-Mur Rose-Mur Maria",
                "Mur Rose-Mur Sina-Mur Maria",
                "Mur Maria-Mur Sina-Mur Rose",
                "Mur Rose-Mur Maria-Mur Sina"
            ),
            0
        )
        val question4 = Question(
            "De combien de mètres le Titan Colossal dépasse t’il le Mur Maria ?",
            listOf("5 m", "10 m", "15 m", "20 m"),
            1
        )
        val question5 = Question(
            "Au sein du bataillon d'exploration, quel est le grade de Livai Ackerman ?",
            listOf(
                "Caporal-Chef",
                "Caporal-Major",
                "Lieutenant-Caporal",
                "Caporal"
            ),
            0
        )
        val question6 = Question(
            "Comment s’appelle le liquide qui sert a la transmission du pouvoir de metarmorphose en Titan ?",
            listOf(
                "Liquide Celebro-Spinale",
                "Liquide Cerebro-Spilale",
                "Liquide Cerebro-Spinale",
                "Liquide Celebro-Spilale"
            ),
            2
        )
        val question7 = Question(
            "Quelle est la profession de l’homme dont l'auteur s’est inspiré pour créer le Chara-Design du Titan Cuirassé ?",
            listOf("Sprinteur", "Catcheur", "Forgeron", "Boxeur"),
            1
        )
        val question8 = Question(
            "C’est quoi le nom de l’opening 1 et de l’ending 2 de Shingeki no Kyojin ?",
            listOf(
                "Guren No Yumiya & Great Escape",
                "Sasageyo & Call Your Name",
                "Jiyuu No Tsubasa & Utsukushiki Zankoku Na Sekai",
                "The Reluctant Heroes & DOA"
            ),
            0
        )
        val question9 = Question(
            "Qui a construit les murs qui protège les Eldiens des Titans ?",
            listOf("Karl Fritz", "Les Mahrs", "Les Titans Primordiaux", "Ymir"),
            0
        )
        val question10 = Question(
            "Un Eldien est un natif de… ",
            listOf(
                "L'ile du Paradis",
                "L'Empire Fritz",
                "L'Empire Eldienne",
                "L'Empire d'Eldia"
            ),
            3
        )
        val question11 = Question(
            "D’où est ce que le Roi Karl Fritz s’est t’il inspiré pour nommer les différents Murs sur l’ile du paradis ?",
            listOf(
                "De ses 3 filles",
                "Des 3 soeurs d'Ymir Fritz",
                "Des 3 premieres detenteuses de l'Originel",
                "Des 3 filles d'Ymir Fritz"
            ),
            3
        )
        val question12 = Question(
            "Pourquoi le 145e Roi d’Eldia fit le vœu de renoncer à utiliser le pouvoir du TITAN ORIGINEL ?",
            listOf(
                "Pour proteger son peuple de l'exterieur",
                "Pour garder l'equilibre du Monde",
                "Par pitie des peuples exterieurs",
                "Pour s’assurer que le titan en question ne soit jamais utilisé a de mauvaises fins"
            ),
            3
        )
        val question13 = Question(
            "Combien d’année ce sont Ecoulée entre la destruction du Mur dans le district de Shiganshina et la conquete de " +
                    "celle si par le bataillon d’exploration ? ",
            listOf("3 ans", "4 ans", "5 ans", "6 ans"),
            2
        )
        val question14 = Question(
            "Quel est le nom complet de la premiere victime D’Armin en titan ?",
            listOf("Bertolt Hoover", "Erwin Smith", "Marco Bott", "Reiner Braun"),
            0
        )
        val question15 = Question(
            "Après la Bataille ou fut mort le Major Erwin, a combien s’éleve le nombre de Survivant au sein du bataillon d’exploration ?",
            listOf("9", "8", "10", "7"),
            0
        )
        val question16 = Question(
            "Le district de Trost est rataché a quel Mur ?",
            listOf(
                "Le Mur Maria",
                "Le Mur Rose",
                "Le Mur Sina",
                "Le Mur Shiganshina"
            ),
            1
        )
        val question17 = Question(
            "Combien de Titan Primordiaux sont impliqués a la bataille de Shiganshina ?",
            listOf("5", "4", "3", "6"),
            0
        )
        val question18 = Question(
            "Combien de jeunes en tout faisaient partie de la 104e Brigade d’entrainement ?",
            listOf("216", "217", "218", "219"),
            2
        )
        val question19 = Question(
            "Quel est l’autre nom donner au Bataillon d’exploration ?",
            listOf(
                "Rodeurs des Murs",
                "Bataillon de la Liberte",
                "Chasseurs de Titans",
                "Corps de Reconnaissance"
            ),
            3
        )
        val question20 = Question(
            "Comment s’appelle les ailes qui se trouvent sur l’insigne du bataillon d’explorations ? ",
            listOf(
                "Les Ailes de la Foi",
                "Les Ailes de l'Exploration",
                "Les Ailes de la Liberté",
                "Les Ailes du Courage"
            ),
            2
        )
        val question21 = Question(
            "Après la mort du Major Erwin Smith qui prend le commandement du bataillon d’exploration ?",
            listOf("Armin Arlert", "Hansi Zoe", "Livai Ackerman", "Eren Jager"),
            1
        )
        val question22 = Question(
            "Quel est le nom complet de l’assassin de BEAN & SAWNEY ?",
            listOf(
                "Jean Kirstein",
                "Annie Leonhart",
                "Reiner Braun",
                "Aucune des propositions"
            ),
            1
        )
        val question23 = Question(
            "Un detenteur de Titan Primordial a une longevite de combien d'annees ?",
            listOf("7 ans", "20 ans", "13 ans", "16 ans"),
            2
        )
        val question24 = Question(
            "A quel age Eren obtient son Titan Primordial ?",
            listOf("9 ans", "10 ans", "11 ans", "12 ans"),
            1
        )
        val question25 = Question(
            "Qui est l'auteur de Shingeki No Kyojin ?",
            listOf(
                "Masashi Kishimoto",
                "Eichiro Oda",
                "Hiro Mashima",
                "Hajime Isayama"
            ),
            3
        )
        val question26 = Question(
            "Comment s'appelle la Reine de l'ile du Paradis ?",
            listOf(
                "Historia Fritz",
                "Historia Reiss",
                "Christa Reiss",
                "Christa Fritz"
            ),
            1
        )
        val question27 = Question(
            "En quelle annee est sortie Shingeki No Kyojin ?",
            listOf("1999", "2006", "2009", "2013"),
            2
        )
        val question28 = Question(
            "Combien de Titans possede Eren ?",
            listOf("3", "1", "2", "4"),
            0
        )
        val question29 = Question(
            "Comment s'appelle le soldat qui a sauve Eren et Mikasa lors de la premiere apparition du " +
                    "titan colossal ?",
            listOf("Dieter", "Moses", "Waltz", "Hannes"),
            3
        )
        return QuestionBank(
            listOf(
                question1,
                question2,
                question3,
                question4,
                question5,
                question6,
                question7,
                question8,
                question9,
                question10,
                question11,
                question12,
                question13,
                question14,
                question15,
                question16,
                question17,
                question18,
                question19,
                question20,
                question21,
                question22,
                question23,
                question24,
                question25,
                question26,
                question27,
                question28,
                question29
            )
        )
    }

    private fun displayQuestion(question : Question){
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