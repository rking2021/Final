package com.example.afinal



import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class NoteAdapter(private val notesList: List<Note>) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notesList[position]
        holder.noteTextView.text = note.note
        // Format the timestamp as needed
        holder.timestampTextView.text = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(Date(note.timestamp))
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val noteTextView: TextView = itemView.findViewById(R.id.noteTextView)
        val timestampTextView: TextView = itemView.findViewById(R.id.timestampTextView)
    }
}
