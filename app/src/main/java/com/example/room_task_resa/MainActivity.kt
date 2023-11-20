package com.example.room_task_resa


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.example.room_task_resa.databinding.ActivityMainBinding
import com.example.roomtask.database.Note
import com.example.roomtask.database.NoteDao
import com.example.roomtask.database.NoteRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private lateinit var mNotesDao: NoteDao
    private lateinit var executorService: ExecutorService
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        executorService = Executors.newSingleThreadExecutor()
        val db = NoteRoomDatabase.getDatabase(this)
        mNotesDao = db!!.noteDao()!!

        binding.btnAdd.setOnClickListener{
            val intent = Intent(this@MainActivity, TambahCatatan::class.java)
            startActivity(intent)
        }

        binding.listView.setOnItemClickListener() {
                adapterView, view, i, id ->
            val item = adapterView.adapter.getItem(i) as Note
            val intent = Intent(this@MainActivity, PerbaruiCatatan::class.java)
            intent.putExtra("EXT_ID", item.id)
            intent.putExtra("EXT_TITLE", item.title)
            intent.putExtra("EXT_DESCRIPTION", item.description)
            intent.putExtra("EXT_DATE", item.date)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        getAllNotes()
    }

    private fun getAllNotes() {
        mNotesDao.allNotes.observe(this) {
                notes ->
            val adapter: ArrayAdapter<Note> = ArrayAdapter<Note>(
                this,
                android.R.layout.simple_list_item_1, notes
            )
            binding.listView.adapter = adapter
        }
    }
}