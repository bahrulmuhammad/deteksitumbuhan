package com.example.bebenay.deteksitumbuhan.View;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.bebenay.deteksitumbuhan.R;
import com.example.bebenay.deteksitumbuhan.View.IdentifikasiActivity;

import java.io.File;


/**
 * Created by bebenay on 9/30/2015.
 */
public class KameraActivity extends AppCompatActivity {

    static  final int TAKE_PICTURE = 1, IMAGE_PICK = 79;
    Uri outputFileUri, selectedImageUri;
    private String selectedImagePath;
    Button  btnLoad, btnTake;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kamera_activity);


        btnLoad = (Button) findViewById(R.id.btnLoad);
        btnTake = (Button) findViewById(R.id.btnTake);
    }


    public boolean onCreateOptionMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void btnAmbilGambar(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file  = new File("storage/sdcard0/jepretTumbuhan","MyPhoto.jpg");
        outputFileUri = Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        startActivityForResult(intent, TAKE_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TAKE_PICTURE) {
            if (resultCode == RESULT_OK) {
                try {
                    Intent i = new Intent();
                    i.setAction("com.example.IdentifikasiActivity");
                    String fileUri = outputFileUri.getPath();
                    i.putExtra("image_path", fileUri);
                    startActivity(i);
                } catch (Exception e) {
                    Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
                }

            } else if(resultCode == RESULT_CANCELED) {

            }
        }

        if (requestCode == IMAGE_PICK && resultCode ==Activity.RESULT_OK) {
            selectedImageUri = data.getData();
            selectedImagePath = getPath(selectedImageUri);

            Intent i = new Intent(this, IdentifikasiActivity.class);
            i.putExtra("image_path", selectedImagePath);
            startActivity(i);
        }
    }

    public void onLoad(View v) {
        Intent intent  = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        getIntent().setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Pilihan Gambar"), IMAGE_PICK);
    }


    public String getPath(Uri uri) {
        String [] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


}
