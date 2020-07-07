package `in`.teardown.soft.roomdatabase

import `in`.teardown.soft.roomdatabase.model.AppDatabase
import android.arch.persistence.room.Room
import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {


    override fun onClick(v: View?) {
        val dialog = AlertDialog.Builder(this)

        dialog.setTitle("New Task");
        dialog.setView(layoutInflater.inflate(R.layout.dialog_add_task, null, false))
        dialog.setPositiveButton("Create", DialogInterface.OnClickListener { dialog, which ->
            
        })

        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }s

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addTask.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "room-database.db"
        ).build()
    }

    override fun onPause() {
        super.onPause()
        db.close()
    }

}