package work.wtks.android.iidxresultmanager.ui.register

import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.exifinterface.media.ExifInterface
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_register_result.*
import kotlinx.android.synthetic.main.content_register_result.*
import work.wtks.android.iidxresultmanager.R
import work.wtks.android.iidxresultmanager.data.valueobject.Difficulty
import work.wtks.android.iidxresultmanager.data.valueobject.RandomOption
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

class RegisterResultActivity : AppCompatActivity() {
    companion object {
        const val ARG_PHOTO_URI = "photo_uri"
    }

    private val viewModel: RegisterResultViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_result)
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_close_24)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        spinner_random_option.adapter = ArrayAdapter(
            applicationContext,
            android.R.layout.simple_spinner_item,
            RandomOption.values().map { it.name }).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spinner_random_option.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) = Unit
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val option = (parent as? Spinner)?.selectedItem as? String ?: return
                viewModel.randomOption.value = RandomOption.valueOf(option)
            }
        }
        viewModel.randomOption.observe(this, Observer {
            spinner_random_option.setSelection(it.ordinal)
        })

        spinner_difficulty.adapter =
            ArrayAdapter(applicationContext, android.R.layout.simple_spinner_item,
                Difficulty.values()
                    .map { it.name }).apply { setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
        viewModel.difficulty.observe(this, Observer {
            spinner_difficulty.setSelection(it.ordinal)
        })

        checkBox_legacyOption.setOnCheckedChangeListener { _, v ->
            viewModel.legacyOption.value = v
        }
        viewModel.legacyOption.observe(this, Observer {
            checkBox_legacyOption.isChecked = it
        })

        spinner_title.setTitle("曲名を選択")
        spinner_title.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) = Unit
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val title = parent?.getItemAtPosition(position) as? String ?: return
                viewModel.songTitleIdx.value = position
                viewModel.songTitle.value = title
            }
        }
        viewModel.songTitleIdx.observe(this, Observer {
            spinner_title.setSelection(it)
        })

        button_ok.setOnClickListener { registerResult() }
        viewModel.isOkButtonEnabled.observe(this, Observer { button_ok.isEnabled = it })

        viewModel.photoUri.observe(this, Observer {
            imageView_photo.setImageURI(it!!)
        })
        viewModel.dateTime.observe(this, Observer {
            textView_dateTime.text = SimpleDateFormat.getDateTimeInstance().format(it)
        })

        val extra = intent.extras ?: return
        setImage(extra[ARG_PHOTO_URI] as Uri)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return false
    }

    private fun registerResult() {
        viewModel.registerResult()
        finish()
    }

    private fun setImage(uri: Uri) {
        viewModel.photoUri.value = uri

        val exif = contentResolver.openInputStream(uri)!!.use {
            ExifInterface(it)
        }

        if (exif.gpsDateTime > 0) {
            viewModel.dateTime.value = Date(exif.gpsDateTime)
        } else if (exif.dateTimeOriginal > 0) {
            viewModel.dateTime.value = Date.from(
                LocalDateTime.ofEpochSecond(
                    exif.dateTimeOriginal / 1000,
                    0,
                    ZoneOffset.of("+09:00")
                )
                    .toInstant(
                        ZoneOffset.UTC
                    )
            )
        } else {
            viewModel.dateTime.value = Date()
        }
    }
}