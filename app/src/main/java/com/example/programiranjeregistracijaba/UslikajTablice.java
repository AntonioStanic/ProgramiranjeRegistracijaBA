package com.example.programiranjeregistracijaba;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextDetector;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;


import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class UslikajTablice extends AppCompatActivity {

    private Button dodaj_sliku_btn;
    private Button pretraziBtn;
    private ImageView imageView;
    private TextView rezultat_ocitavanja;
    private Bitmap imageBitmap;
    Bitmap bmap;

    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 400;
    private static final int IMAGE_PICK_GALLERY_CODE = 1000;
    private static final int IMAGE_PICK_CAMERA_CODE = 1001;
    String cameraPermission [];
    String storagePermission [];

    Uri slika_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uslikaj_tablice);
        getSupportActionBar().hide();

        cameraPermission = new String [] {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE};


        dodaj_sliku_btn = findViewById(R.id.dodaj_sliku_btn);
        pretraziBtn = findViewById(R.id.pretraziBtn);
        imageView = findViewById(R.id.imageView);
        rezultat_ocitavanja = findViewById(R.id.rezultat_txt);

        dodaj_sliku_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowImageImportDialog();
            }
        });
        pretraziBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String rezultat = rezultat_ocitavanja.getText().toString();
                if(rezultat.equals("")){
                    Toast.makeText(UslikajTablice.this, "Molimo odaberite sliku kako bi ste dobili broj tablica", Toast.LENGTH_LONG).show();
                }
                    else{

                    Intent i= new Intent(getApplicationContext(),AutomatskoPretrazivanjeAutomobila.class);
                    i.putExtra("Rezultat", rezultat);
                    startActivity(i);
                }
            }
        });


    }



    static final int REQUEST_IMAGE_CAPTURE = 1;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //imamo sliku iz kamere
        if(resultCode == RESULT_OK){
            if(requestCode == IMAGE_PICK_GALLERY_CODE){
                //imamo sliku iz galerije i ovdje ju mozemo izrezati
                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);  // ovdje se omogucuje okvir za rezanje
            }
            if(requestCode == IMAGE_PICK_CAMERA_CODE){
                //imamo sliku iz kamere i ovdje ju mozemo izrezati
                CropImage.activity(slika_uri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);
            }
        }

        //dohvati izrezanu sliku
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK){
                Uri rezultatUri = result.getUri(); //get image uri, 36:18

                //postavljanje slike u image view
                imageView.setImageURI(rezultatUri);

                imageView.buildDrawingCache();
                bmap = imageView.getDrawingCache();


                //dohvati crtajuci bitmap za prepoznavanje teksta
                BitmapDrawable bitmapDrawable = (BitmapDrawable)imageView.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();
                detectTxt();

            }
            else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                //ako postoji neka greska, prikazi ju
                Exception greska = result.getError();
                Toast.makeText(this, ""+greska, Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void detectTxt() {
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bmap);
        FirebaseVisionTextDetector detector = FirebaseVision.getInstance().getVisionTextDetector();
        detector.detectInImage(image).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            @Override
            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                processTxt(firebaseVisionText);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void processTxt(FirebaseVisionText text) {
        List<FirebaseVisionText.Block> blocks = text.getBlocks();
        if (blocks.size() == 0) {
            Toast.makeText(UslikajTablice.this, "No Text :(", Toast.LENGTH_LONG).show();
            return;
        }
        for (FirebaseVisionText.Block block : text.getBlocks()) {
            String txt = block.getText();
            rezultat_ocitavanja.setTextSize(24);
            rezultat_ocitavanja.setText(txt.trim().replace(" ",""));
        }
    }


    private void ShowImageImportDialog() {
        //koje ce se stavke prikazati u dialogu
        String [] stavke = {" Kamera", " Galerija"};
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        //postavljanje naslova
        dialog.setTitle("Odaberite sliku");
        dialog.setItems(stavke, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0){
                    //odabrana je opcija KAMERA
                    if(!checkCameraPermission()){
                        //nije odobreno koristenje kamere, zatrazi dopustenje
                        requestCameraPermission();
                    }else{
                        //dopustena je upotreba kamere, moze se uslikati slika
                        pickCamera();
                    }
                }
                if(which == 1){
                    //odabrana je opcija GALERIJA
                    if(!checkStoragePermission()){
                        //nije odobreno koristenje galerije, zatrazi dopustenje
                        requestStoragePermission();
                    }else{
                        //dopustena je upotreba galerije, moze se odabrati slika
                        pickGallery();
                    }
                }

            }
        });
        dialog.create().show(); //prikazivanje dialoga, hocemo li odabrati kameru ili galeriju
    }

    private void pickGallery() {
        //uzimanje slike iz galerije
        Intent intent = new Intent(Intent.ACTION_PICK);
        //postavi tip intenta u sliku
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);
    }

    private void pickCamera() {
        //uzimanje slike iz kamere, takoder ce se slika sacuvati u galeriju da bi se dobila bolja kvaliteta
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Nova slika"); // naziv slike
        values.put(MediaStore.Images.Media.DESCRIPTION, "Sa slike u tekst"); // opis slike
        slika_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, slika_uri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST_CODE);
    }

    private boolean checkStoragePermission() {
        boolean rezultat = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return rezultat;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, cameraPermission, CAMERA_REQUEST_CODE);
    }

    private boolean checkCameraPermission() {
        //provjeravamo je li dopustena upotreba kamere i vracamo rezultat
        // da bi dobili dobru kvalitetu slike, pozeljno je da ju spremimo  vanjski spremnik prije umetanja u imageview i zbog toga nam treba dopustenje
        boolean rezultat = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean rezultat1 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return rezultat && rezultat1;
    }

    //rukovanje rezultatom dozvole

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case CAMERA_REQUEST_CODE:
                if(grantResults.length > 0){
                    boolean kameraPrihvacena = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean spremanjePrihvaceno = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;

                    if(kameraPrihvacena && spremanjePrihvaceno){
                        pickCamera();
                    }else{
                        Toast.makeText(this, "Dopustenje odbijeno!", Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case STORAGE_REQUEST_CODE:
                if(grantResults.length > 0){

                    boolean spremanjePrihvaceno = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;

                    if(spremanjePrihvaceno){
                        pickGallery();
                    }else{
                        Toast.makeText(this, "Dopustenje odbijeno!", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }



}
