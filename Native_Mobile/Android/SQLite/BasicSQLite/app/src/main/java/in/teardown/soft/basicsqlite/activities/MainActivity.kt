package `in`.teardown.soft.basicsqlite.activities

import `in`.teardown.soft.basicsqlite.R
import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import android.provider.BaseColumns
import android.support.design.widget.TextInputEditText
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import android.view.*
import android.widget.LinearLayout

import android.widget.TextView

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    lateinit var db: Database.NoteDbHelper
    lateinit var adapter: NotesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        adapter = NotesAdapter(ArrayList(), baseContext)
        recyclerView.adapter = adapter
//        recyclerView.layoutManager = LinearLayoutManager(baseContext)
//        recyclerView.layoutManager = GridLayoutManager(baseContext, 2)
        recyclerView.layoutManager = StaggeredGridLayoutManager(1, LinearLayout.HORIZONTAL)
        fab.setOnClickListener { view ->
            val alertView = LayoutInflater.from(this).inflate(R.layout.create_note, null, false)
            val etTitle = alertView.findViewById<TextInputEditText>(R.id.title)
            val etContent = alertView.findViewById<TextInputEditText>(R.id.content)

            val dialogBuilder = AlertDialog.Builder(this)
                dialogBuilder.setTitle("New Note")
                dialogBuilder.setView(alertView)
                dialogBuilder.setPositiveButton("Create", DialogInterface.OnClickListener { dialog, which ->
                    val title = etTitle.text.toString().trim()
                    val content = etContent.text.toString().trim()

                    if (title.isEmpty()) {
                        etTitle.setText("")
                        etTitle.error = "Can't be Empty"
                    } else
                        etTitle.error = null

                    if (content.isEmpty()) {
                        etContent.setText("")
                        etContent.error = "Can't be Empty"
                    } else
                        etContent.error = null

                    db.CreateNoteEntity(title, content)
                    dialog.dismiss()
                })
            dialogBuilder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })
            dialogBuilder.setOnDismissListener { ReloadView() }
            dialogBuilder.show()
        }

    }

    override fun onResume() {
        super.onResume()
        ReloadView()
    }

    private fun ReloadView() {
        db = Database.NoteDbHelper(applicationContext)
            adapter.addNewAll(db?.GetAllNotes())
    }

    override fun onPause() {
        super.onPause()
        db?.close()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    class NotesAdapter (private val itemList : ArrayList<Database.NoteContract>, private val context: Context) : RecyclerView.Adapter<NotesAdapter.ViewHolder>() {

        override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(context).inflate(android.R.layout.test_list_item, viewGroup, false).findViewById(android.R.id.text1))
        }

        override fun getItemCount(): Int = itemList.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.tvTitle.setText(itemList[position].title)
        }

        fun addNewAll(items: ArrayList<Database.NoteContract>) {
            itemList.clear()
            itemList.addAll(items)
            notifyDataSetChanged()
        }

        class ViewHolder(var tvTitle: TextView)  : RecyclerView.ViewHolder(tvTitle)
    }

    class Database {

        class NoteContract {

            var id: Long? = null
            var title: String? = null
            var content: String? = null

            // Table contents are grouped together in an anonymous object.
            object NoteBasicEntry : BaseColumns {
                const val TABLE_NAME = "NOTE_TABLE"
                const val COLUMN_NAME_NOTE_TITLE = "NOTE_TITLE"
                const val COLUMN_NAME_NOTE_CONTENT = "NOTE_CONTENT"
            }
        }

                class NoteDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

                    override fun onCreate(db: SQLiteDatabase) {
                        db.execSQL(SQL_CREATE_ENTRIES)
                    }

                    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
                        onCreate(db)
                    }

                    companion object {
                        // If you change the database schema, you must increment the database version.
                        const val DATABASE_VERSION = 1
                        const val DATABASE_NAME = "FeedReader.db"
                    }

                    fun CreateNoteEntity(title: String, content: String) {
                        // Gets the data repository in write mode
                        val db = writableDatabase

                        val values = ContentValues().apply {
                            put(NoteContract.NoteBasicEntry.COLUMN_NAME_NOTE_TITLE, title)
                            put(NoteContract.NoteBasicEntry.COLUMN_NAME_NOTE_CONTENT, content)
                        }

                        val newRowId = db?.insert(NoteContract.NoteBasicEntry.TABLE_NAME, null, values)
                    }

                    fun GetAllNotes(): ArrayList<NoteContract> {
                        val db = readableDatabase
                        val projection = arrayOf(BaseColumns._ID, NoteContract.NoteBasicEntry.COLUMN_NAME_NOTE_TITLE, NoteContract.NoteBasicEntry.COLUMN_NAME_NOTE_CONTENT)
                        val sortOrder = "${NoteContract.NoteBasicEntry.COLUMN_NAME_NOTE_CONTENT} DESC"

                        val cursor = db.query(NoteContract.NoteBasicEntry.TABLE_NAME, null, null, null, null, null, sortOrder)
                        val notes = ArrayList<NoteContract>()
                        with(cursor) {
                            while (moveToNext()) {
                                val item = NoteContract()
                                item.id = getLong(getColumnIndexOrThrow(BaseColumns._ID))
                                item.title = getString(getColumnIndexOrThrow(NoteContract.NoteBasicEntry.COLUMN_NAME_NOTE_TITLE))
                                item.content = getString(getColumnIndexOrThrow(NoteContract.NoteBasicEntry.COLUMN_NAME_NOTE_CONTENT))
                                notes.add(item)
                            }
                        }
                        return notes
                    }
        }

        companion object {
            private const val SQL_CREATE_ENTRIES =
                "CREATE TABLE ${NoteContract.NoteBasicEntry.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
//                    "${NoteEntity.COLUMN_NAME_LOCAL_ID} TEXT," +
                "${NoteContract.NoteBasicEntry.COLUMN_NAME_NOTE_TITLE} TEXT," +
                "${NoteContract.NoteBasicEntry.COLUMN_NAME_NOTE_CONTENT} TEXT)"
        }

    }
}