package com.example.room_task_resa
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.room_task_resa.databinding.TambahCatatanBinding
import com.example.roomtask.database.Note
import com.example.roomtask.database.NoteDao
import com.example.roomtask.database.NoteRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class TambahCatatan : AppCompatActivity() {
    private lateinit var mNotesDao: NoteDao
    private lateinit var executorService: ExecutorService
    private lateinit var binding: TambahCatatanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = TambahCatatanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        executorService = Executors.newSingleThreadExecutor()
        val db = NoteRoomDatabase.getDatabase(this)
        mNotesDao = db!!.noteDao()!!

        binding.btnCreate.setOnClickListener{
            insert(
                Note(
                    title = binding.etTitle.text.toString(),
                    description = binding.etDescription.text.toString(),
                    date = binding.etDate.text.toString(),
                )
            )
            finish()
        }
    }

    private fun insert(note: Note){
        executorService.execute{mNotesDao.insert(note)}
    }
}