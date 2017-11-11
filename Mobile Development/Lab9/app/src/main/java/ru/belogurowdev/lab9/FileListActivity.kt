package ru.belogurowdev.lab9

import android.app.ListActivity
import android.content.DialogInterface
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AlertDialog
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.mikepenz.fontawesome_typeface_library.FontAwesome
import com.mikepenz.iconics.IconicsDrawable
import java.io.File
import java.util.*

class FileListActivity : ListActivity() {

    private lateinit var textCurrentDir: TextView
    private lateinit var textCreate: TextView
    private lateinit var directoryIcon: ImageView
    private lateinit var createIcon: ImageView

    private lateinit var adapter: ArrayAdapter<Drawable>
    private lateinit var currentFilesDir: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_list)

        initializeViews()
        initializeListeners()
    }

    private fun initializeViews() {
        currentFilesDir = applicationContext.filesDir
        initAdapter()

        directoryIcon = findViewById(R.id.icon_current_dir)
        directoryIcon.setImageDrawable(IconicsDrawable(this)
                .icon(FontAwesome.Icon.faw_folder_open_o)
                .sizeDp(40)
                .paddingDp(5))

        createIcon = findViewById(R.id.icon_create)
        createIcon.setImageDrawable(IconicsDrawable(this)
                .icon(FontAwesome.Icon.faw_plus_square)
                .sizeDp(30)
                .paddingDp(5))

        textCurrentDir = findViewById(R.id.text_current_dir)
        textCurrentDir.text = currentFilesDir.absolutePath

        textCreate = findViewById(R.id.text_create)
    }

    private fun showError(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show()
    }

    private fun initializeListeners() {
        textCurrentDir.setOnClickListener {
            if (currentFilesDir.parentFile.isDirectory) {
                currentFilesDir = currentFilesDir.parentFile
                textCurrentDir.text = currentFilesDir.absolutePath
                initAdapter()
            }
        }

        textCreate.setOnClickListener {
            val input = EditText(this)

            AlertDialog.Builder(this)
                    .setTitle(getString(R.string.input_file_name))
                    .setView(input)
                    .setPositiveButton("File", { dialog, _ ->
                        val name = input.text.toString()
                        if (name.isNotEmpty()) {
                            if (isExternalStorageWritable()) {
                                val file = File(currentFilesDir, name)
                                file.createNewFile()
                                initAdapter()
                            } else {
                                showError("Storage not writable")
                            }
                            dialog.cancel()
                        } else {
                            showError("Cannot apply empty name")
                        }
                    })
                    .setNegativeButton("Folder", { dialog, _ ->
                        val name = input.text.toString()
                        if (name.isNotEmpty()) {
                            if (isExternalStorageWritable()) {
                                val file = File(currentFilesDir, name)
                                file.mkdir()
                                initAdapter()
                            } else {
                                showError("Storage not writable")
                            }
                            dialog.cancel()
                        } else {
                            showError("Cannot apply empty name")
                        }
                    })
                    .show()
        }
    }

    override fun onListItemClick(l: ListView?, v: View?, position: Int, id: Long) {
        super.onListItemClick(l, v, position, id)
        if (currentFilesDir.listFiles()[position].isDirectory) {
            currentFilesDir = currentFilesDir.listFiles()[position]

            textCurrentDir.text = currentFilesDir.absolutePath
            initAdapter()
        }
    }

    private fun initAdapter() {
        val filesDir = currentFilesDir
        if (isExternalStorageReadable()) {
            val directoryIcon = IconicsDrawable(this)
                    .icon(FontAwesome.Icon.faw_folder_o)
                    .sizeDp(30)
                    .paddingDp(5)
            val fileIcon = IconicsDrawable(this)
                    .icon(FontAwesome.Icon.faw_file)
                    .sizeDp(30)
                    .paddingDp(5)

            val icons = ArrayList<Drawable>()
            val files = ArrayList<String>()

            filesDir.listFiles().forEach {
                files.add(it.name)
                if (it.isFile) {
                    icons.add(fileIcon)
                } else if (it.isDirectory) {
                    icons.add(directoryIcon)
                }
            }

            adapter = object : ArrayAdapter<Drawable>(this, R.layout.item_list, icons) {
                override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
                    val view = View.inflate(applicationContext, R.layout.item_list, null)

                    val iconView = view.findViewById<ImageView>(R.id.list_item_icon)
                    val textView = view.findViewById<TextView>(R.id.list_item_text)
                    iconView.setImageDrawable(icons[position])
                    textView.text = files[position]
                    return view
                }
            }
            listAdapter = adapter
        } else {
            showError("Storage not readable")
        }
    }

    private fun isExternalStorageWritable() : Boolean {
        val state = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED == state
    }

    private fun isExternalStorageReadable() : Boolean {
        val state = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED == state || Environment.MEDIA_MOUNTED_READ_ONLY == state
    }
}
