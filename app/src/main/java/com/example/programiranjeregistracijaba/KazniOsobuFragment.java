package com.example.programiranjeregistracijaba;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.programiranjeregistracijaba.model.KaznaOsoba;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class KazniOsobuFragment extends Fragment {

    private TextInputLayout iznosKazne;
    private TextInputLayout opisKazne;
    private TextInputLayout brojOsobne;
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

        View v = inflater.inflate(R.layout.kazna_osoba_activity, container,false);





        iznosKazne = v.findViewById(R.id.iznos_kazne_txt);
        opisKazne =  v.findViewById(R.id.opis_kazne_txt);
        spremiKaznu_btn = v.findViewById(R.id.Spremi_Kaznu_Btn);
        brojOsobne =  v.findViewById(R.id.broj_osobne_txt);
        progressBar =  v.findViewById(R.id.progress_bar);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("kazne_osoba");
        spremiKaznu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                provjeriUznos(iznosKazne);
                provjeriUznos(opisKazne);
                provjeriUznos(brojOsobne);
                prenesiPodatke();
            }
        });

        return v;
    }

    private void prenesiPodatke() {
        if (brojOsobne != null && opisKazne != null && iznosKazne!= null) {
            Toast.makeText(getActivity(), "Prijenos uspješno završen", Toast.LENGTH_SHORT).show();
            KaznaOsoba upload = new KaznaOsoba(
                    iznosKazne.getEditText().getText().toString().trim(),
                    opisKazne.getEditText().getText().toString().trim(),
                    brojOsobne.getEditText().getText().toString().trim());
            String uploadId = databaseReference.push().getKey();
            databaseReference.child(uploadId).setValue(upload);
            iznosKazne.getEditText().setText("");
            opisKazne.getEditText().setText("");
            brojOsobne.getEditText().setText("");

        }


        else {
            Toast.makeText(getActivity(), "Molimo popunite sve podatke", Toast.LENGTH_SHORT).show();
            provjeriUznos(iznosKazne);
            provjeriUznos(opisKazne);
            provjeriUznos(brojOsobne);
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

