package com.example.afinal

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

data class Note(val note: String = "", val timestamp: Long = 0)

class NotesActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var notesRecyclerView: RecyclerView
    private lateinit var notesAdapter: NoteAdapter
    private val notesList = mutableListOf<Note>()
    private lateinit var noteInputField: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes)

        firestore = FirebaseFirestore.getInstance()
        notesRecyclerView = findViewById(R.id.notesRecyclerView)
        noteInputField = findViewById(R.id.noteInputField)
        val addNoteButton = findViewById<Button>(R.id.addNoteButton)

        // Setup RecyclerView
        notesAdapter = NoteAdapter(notesList)
        notesRecyclerView.layoutManager = LinearLayoutManager(this)
        notesRecyclerView.adapter = notesAdapter

        // Add new note
        addNoteButton.setOnClickListener {
            val noteContent = noteInputField.text.toString().trim()
            if (noteContent.isNotEmpty()) {
                addNoteToFirestore(noteContent)
            } else {
                Toast.makeText(this, "Please enter a note", Toast.LENGTH_SHORT).show()
            }
        }

        // Fetch and display notes
        fetchNotes()
    }

    private fun addNoteToFirestore(noteContent: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val note = hashMapOf(
                "note" to noteContent,
                "timestamp" to System.currentTimeMillis(),
                "userId" to userId
            )

            firestore.collection("notes")
                .add(note)
                .addOnSuccessListener {
                    Toast.makeText(this, "Note Added!", Toast.LENGTH_SHORT).show()
                    fetchNotes() // Refresh the notes list
                    noteInputField.text.clear() // Clear the input field after adding the note
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error adding note: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun fetchNotes() {
        firestore.collection("notes")
            .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                notesList.clear() // Clear the current list of notes
                for (document in documents) {
                    val note = document.toObject(Note::class.java)
                    notesList.add(note)
                }
                notesAdapter.notifyDataSetChanged() // Notify adapter to update the view
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error fetching notes: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
