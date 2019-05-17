package com.example.programiranjeregistracijaba;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.programiranjeregistracijaba.model.Kazna;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextDetector;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;


import java.util.List;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;


public class IzdajKaznu extends AppCompatActivity {

    private TextInputLayout iznosKazne;
    private TextInputLayout opisKazne;
    private TextInputLayout tablice;
    private ImageButton dodajSliku_btn;
    private CardView spremiKaznu_btn;
    private ProgressBar progressBar;
    private Uri mImageUri; //sluzi za prikazivanje slike u image view i da mozemo onda prenijeti u storage na firebase
    private static final int PICK_IMAGE_REQUEST = 1;
    StorageReference storageReference;
    DatabaseReference databaseReference;

    private StorageTask mUploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_izdaj_kaznu);

        getSupportActionBar().hide();
        contextOfApplication = getApplicationContext();
        Fragment odabraniFragment = new KazniOsobuFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.prikaz_fragmenta, odabraniFragment).commit();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.podnozje_nav);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment odabraniFragment = new KazniOsobuFragment();
                switch (item.getItemId()) {
                    case R.id.navigation_osoba:
                        odabraniFragment = new KazniOsobuFragment();
                        break;
                    case R.id.navigation_automobil:
                        odabraniFragment = new KazniAutomobilFragment();
                        break;

                }
                getSupportFragmentManager().beginTransaction().replace(R.id.prikaz_fragmenta, odabraniFragment).commit();
                return true;
            }
        });
    }
    public static Context contextOfApplication;
    public static Context getContextOfApplication()
    {
        return contextOfApplication;
    }


    /*
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null  && iznosKazne != null && opisKazne != null) {
            mImageUri = data.getData();
            dodajSliku_btn.setImageURI(mImageUri);



        }
    }

    private String dohvatiEkstenziju(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void prenesiPodatke() {
        if (mImageUri != null && opisKazne != null && iznosKazne!= null) {
            StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    + "." + dohvatiEkstenziju(mImageUri));

            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setProgress(0);
                                }
                            }, 500);

                            Toast.makeText(IzdajKaznu.this, "Prijenos uspješno završen", Toast.LENGTH_LONG).show();
                            Kazna upload = new Kazna(
                                    tablice.getEditText().getText().toString().trim(),
                                    iznosKazne.getEditText().getText().toString().trim(),
                                    opisKazne.getEditText().getText().toString().trim(),
                                    taskSnapshot.getDownloadUrl().toString());
                            String uploadId = databaseReference.push().getKey();
                            databaseReference.child(uploadId).setValue(upload);
                            mImageUri = null;
                            iznosKazne.getEditText().setText("");
                            opisKazne.getEditText().setText("");
                            dodajSliku_btn.setImageResource(R.drawable.ic_add_box_black_24dp);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(IzdajKaznu.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressBar.setProgress((int) progress);
                        }
                    });
        } else {
            Toast.makeText(this, "Molimo popunite sve podatke", Toast.LENGTH_SHORT).show();
            provjeriUznos(iznosKazne);
            provjeriUznos(opisKazne);
        }
    }

    private boolean provjeriUznos(TextInputLayout text){
        String ispitaj = text.getEditText().getText().toString().trim();

        if (ispitaj.isEmpty()) {
            text.setError("Ovo polje ne može biti prazno");
            return false;
        }

        else {
            text.setError(null);
            return true;
        }
    }
*/

}
