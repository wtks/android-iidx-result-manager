package work.wtks.android.iidxresultmanager.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import kotlinx.android.synthetic.main.activity_main.*
import work.wtks.android.iidxresultmanager.R
import work.wtks.android.iidxresultmanager.ui.register.RegisterResultActivity
import java.io.FileNotFoundException
import java.io.IOException

class MainActivity : AppCompatActivity() {
    companion object {
        const val REQUEST_GET_IMAGE = 1111
    }

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_GET_IMAGE && resultCode == Activity.RESULT_OK) {
            // 画像選択結果受信
            try {
                val choosedPhotoUri = data?.data ?: kotlin.run {
                    val image = viewModel.tempImageFile ?: return // 何も画像が送られてこなかった
                    if (image.length() == 0L) return // 何も画像が撮られなかった
                    return@run image.toUri()
                }
                startActivity(Intent(application, RegisterResultActivity::class.java).apply {
                    putExtra(RegisterResultActivity.ARG_PHOTO_URI, choosedPhotoUri)
                })
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun launchPhotoChooser() {
        val intent1 = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/jpeg"
            addCategory(Intent.CATEGORY_OPENABLE)
        }

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            putExtra(MediaStore.EXTRA_OUTPUT, viewModel.initTempImageFile())
        }

        val chooserIntent = Intent.createChooser(intent1, "写真を選択").apply {
            putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(intent))
        }
        startActivityForResult(
            chooserIntent,
            MainActivity.REQUEST_GET_IMAGE
        )
    }

}
