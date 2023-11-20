package com.example.room_task_resa

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.room_task_resa.databinding.ActivityMainBinding
import com.example.room_task_resa.databinding.PerbaruiCatatanBinding
import com.example.room_task_resa.databinding.TambahCatatanBinding
import com.example.roomtask.database.Note
import com.example.roomtask.database.NoteDao
import com.example.roomtask.database.NoteRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class PerbaruiCatatan : AppCompatActivity() {
    private lateinit var binding: PerbaruiCatatanBinding
    private var id: Int=0
    private lateinit var executorService: ExecutorService
    private lateinit var mNotesDao: NoteDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PerbaruiCatatanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle: Bundle? = intent.extras
        id = bundle!!.getInt("EXT_ID")!!
        val title = bundle!!.getString("EXT_TITLE")!!
        val description = bundle!!.getString("EXT_DESCRIPTION")!!
        val date = bundle!!.getString("EXT_DATE")!!

        executorService = Executors.newSingleThreadExecutor()
        val db = NoteRoomDatabase.getDatabase(this)
        mNotesDao = db!!.noteDao()!!

        with(binding){
            etTitle.setText(title)
            etDescription.setText(description)
            etDate.setText(date)

            btnUpdate.setOnClickListener{
                update(
                    Note(
                        id = id,
                        title = etTitle.text.toString(),
                        description = etDescription.text.toString(),
                        date = etDate.text.toString()
                    )
                )
                id = 0
                finish()
            }

            btnDelete.setOnClickListener{
                delete(
                    Note(
                        id = id,
                        title = title,
                        description = description,
                        date = date
                    )
                )
                id = 0
                finish()
            }
        }
    }

    private fun update(note: Note){
        executorService.execute{mNotesDao.update(note)}
    }

    private fun delete(note: Note){
        executorService.execute{mNotesDao.delete(note)}
    }
}