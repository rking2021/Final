package com.example.afinal

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val identifyBtn = findViewById<Button>(R.id.btnIdentify)
        val notesBtn = findViewById<Button>(R.id.btnNotes)
        val forumBtn = findViewById<Button>(R.id.btnForum)

        identifyBtn.setOnClickListener {
            Toast.makeText(this, "Launching Identify", Toast.LENGTH_SHORT).show()
            // Navigating to IdentifyActivity when ready
            startActivity(Intent(this, IdentifyActivity::class.java))
        }

        notesBtn.setOnClickListener {
            Toast.makeText(this, "Opening Notes", Toast.LENGTH_SHORT).show()
            // Navigating to NotesActivity when ready
            startActivity(Intent(this, NotesActivity::class.java))
        }

        forumBtn.setOnClickListener {
            Toast.makeText(this, "Forum coming soon!", Toast.LENGTH_SHORT).show()
            // Navigating to ForumActivity when it's ready
            // startActivity(Intent(this, ForumActivity::class.java)) ← Add when ready
        }
    }
}
