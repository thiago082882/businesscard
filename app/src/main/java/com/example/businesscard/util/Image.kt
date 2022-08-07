package com.example.businesscard.util

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.businesscard.R
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class Image {

    companion object {

        //Gerando uma imagem do cartão para compartilhamento
        fun share(context: Context, view: View) {
            val bitmap = getScreenShotFromView(view)
            bitmap?.let {
                saveMediaToStorage(context, bitmap)//Salva no aparelho a imagem
            }
        }

        //Pega a imagem do cartão gerada
        private fun getScreenShotFromView(view: View): Bitmap? {
            var screenshot: Bitmap? = null
            try {
                screenshot =
                    Bitmap.createBitmap(
                        view.measuredWidth,
                        view.measuredHeight,
                        Bitmap.Config.ARGB_8888
                    )
                val canvas = Canvas(screenshot)
                view.draw(canvas)
            } catch (e: Exception) {
                Log.e("GFG", "Failed to capture screenshot because:" + e.message)
            }
            return screenshot
        }

        //Método que salva no aparelho a imagem
        private fun saveMediaToStorage(context: Context, bitmap: Bitmap) {
            val filename = "${System.currentTimeMillis()}.jpg" //Salva através dos segundos

            var fos: OutputStream? = null

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                context.contentResolver?.also { resolver ->
                    val contentValues = ContentValues().apply {
                        put(MediaStore.MediaColumns.DISPLAY_NAME, filename)//Nome do arquivo
                        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")//Tipo do arquivo
                        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)//Onde será salvo no celular
                    }
                    val imageUri: Uri? =
                        resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

                    //Caso a imagem não seja null
                    fos = imageUri?.let {
                        shareIntent(context, imageUri)
                        resolver.openOutputStream(it)
                    }
                }
            } else {
                // Versão do SDK < Q
                val imagesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                val image = File(imagesDir, filename)
                val imageUri: Uri =
                    FileProvider.getUriForFile(
                        context,
                        "br.com.dio.businesscard.fileprovider",
                        image
                    )
                shareIntent(context, imageUri)
                fos = FileOutputStream(image)
            }

            fos?.use {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
                Toast.makeText(context, "Imagem capturada com sucesso", Toast.LENGTH_SHORT).show()
            }
        }

        private fun shareIntent(context: Context, image: Uri) {
            val shareIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, image)
                type = "image/jpeg"
            }
            context.startActivity(
                Intent.createChooser(
                    shareIntent,
                    context.resources.getText(R.string.label_share_to)
                )
            )
        }
    }


}