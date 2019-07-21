package `in`.teardown.soft.roomdatabase

import `in`.teardown.soft.roomdatabase.model.AppDatabase
import android.arch.persistence.room.Room
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {


    override fun onClick(v: View?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

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