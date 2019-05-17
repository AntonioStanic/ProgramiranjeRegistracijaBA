package com.example.programiranjeregistracijaba;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.programiranjeregistracijaba.model.KaznaAutomobil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import static android.app.Activity.RESULT_OK;

public class KazniAutomobilFragment extends Fragment {
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.kazna_automobil_activity, container, false);


        iznosKazne = v.findViewById(R.id.iznos_kazne_txt);
        opisKazne = v.findViewById(R.id.opis_kazne_txt);
        dodajSliku_btn = v.findViewById(R.id.dodaj_sliku_btn);
        spremiKaznu_btn = v.findViewById(R.id.Spremi_Kaznu_Btn);
        tablice = v.findViewById(R.id.tablice_txt);
        progressBar = v.findViewById(R.id.progress_bar);

        storageReference = FirebaseStorage.getInstance().getReference("kazne_slike");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("kazne_automobila");


        dodajSliku_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        spremiKaznu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(getActivity(), "Slanje podataka u tijeku", Toast.LENGTH_SHORT).show();
                } else {

                    prenesiPodatke();
                }

            }
        });


        return v;
    }
        private void openFileChooser() {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                    && data != null && data.getData() != null  && iznosKazne != null && opisKazne != null) {
                mImageUri = data.getData();
                dodajSliku_btn.setImageURI(mImageUri);



            }
        }

        private String dohvatiEkstenziju(Uri uri) {
            Context applicationContext = IzdajKaznu.getContextOfApplication();
            ContentResolver cR = applicationContext.getContentResolver();
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

                                Toast.makeText(getActivity(), "Prijenos uspješno završen", Toast.LENGTH_LONG).show();
                                KaznaAutomobil upload = new KaznaAutomobil(
                                        iznosKazne.getEditText().getText().toString().trim(),
                                        opisKazne.getEditText().getText().toString().trim(),
                                        taskSnapshot.getDownloadUrl().toString(),
                                        tablice.getEditText().getText().toString().trim());
                                String uploadId = databaseReference.push().getKey();
                                databaseReference.child(uploadId).setValue(upload);
                                mImageUri = null;
                                iznosKazne.getEditText().setText("");
                                opisKazne.getEditText().setText("");
                                tablice.getEditText().setText("");
                                dodajSliku_btn.setImageResource(R.drawable.ic_add_box_black_24dp);

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getActivity(), "Molimo popunite sve podatke", Toast.LENGTH_SHORT).show();
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


    }
