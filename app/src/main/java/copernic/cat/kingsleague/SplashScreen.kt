package copernic.cat.kingsleague

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //esconde la ActionBar
        setContentView(R.layout.activity_splash_screen)
        if(supportActionBar != null)
            supportActionBar!!.hide()
        startTimer()
    }

    fun startTimer(){
        object: CountDownTimer(3000, 1000){
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                val intent = Intent(applicationContext, Login::class.java).apply{}
                startActivity(intent)
                finish()
            }

        }.start()
    }
}