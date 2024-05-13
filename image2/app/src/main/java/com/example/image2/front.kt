package com.example.image2




import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.lang.Exception

class front : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_front)
        startheavytask()
    }

    private fun startheavytask() {
        longop().execute()
    }
    private open inner class longop: AsyncTask<String?, Void ?,String?>(){
        override fun doInBackground(vararg params: String?): String? {
            for (i in 0..6){
                try {
                    Thread.sleep(500)
                }
                catch (e:Exception){
                    Thread.interrupted()
                }
            }
            return "result"
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            val intent=Intent(this@front,select::class.java)
            startActivity(intent)
        }
    }
}