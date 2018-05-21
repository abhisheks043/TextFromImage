package com.example.abhishek.textfromimage

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v7.app.AlertDialog
import android.util.Log
import android.util.SparseArray
import android.view.View
import android.widget.Toast
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.text.TextBlock
import com.google.android.gms.vision.text.TextRecognizer
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private val PICK_IMAGE_REQUEST = 1
    var bitmap: Bitmap? = null
    var check_test =0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn.setOnClickListener(View.OnClickListener {

            var intent = Intent()
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE_REQUEST)

        })

        btn2.setOnClickListener(View.OnClickListener {
            if(check_test ==1)
                checkText()
            else
                Toast.makeText(this, "Choose Image First", Toast.LENGTH_SHORT).show()
        })


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode === PICK_IMAGE_REQUEST && data != null && data.data != null) {

            val uri = data.data

            try {
                bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                // Log.d(TAG, String.valueOf(bitmap));

                im.setImageBitmap(bitmap)
                check_test=1;


            } catch (e: IOException) {
                e.printStackTrace()
            }

        }


    }



    private fun checkText() {
//        var file = File(storageDirectory+"im"+indx+".png")
//        var mBitmap: Bitmap? = convertFiletoBitmap(file)
        var textRecognizer: TextRecognizer = TextRecognizer.Builder(applicationContext).build()
        var im_frame: Frame = Frame.Builder()
                .setBitmap(bitmap)
                .build()

        var img_text: String = ""
        var textBlock: SparseArray<TextBlock> = textRecognizer.detect(im_frame)
        Log.e("text", textBlock.size().toString())
        for(i in 0 until textBlock.size() ){
            var text: TextBlock = textBlock.get(textBlock.keyAt(i))
            img_text += text.value
            Log.e("text" , img_text)
        }

        var builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Recognised Text from Image is")
                .setMessage(img_text)
                .show()

    }


    private fun convertFiletoBitmap(file: File): Bitmap?{

        if(file.exists() ){
            val bm: Bitmap = BitmapFactory.decodeFile(file.absolutePath)
            return bm
        }
        else{
            Log.e("error","Files dont exist")
        }
        return null
    }


}