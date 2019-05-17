package com.example.programiranjeregistracijaba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DodajPolicajcaAktivnost extends AppCompatActivity {
    EditText email_txt;
    EditText lozinka_txt;
    CardView spremi_btn;
    FirebaseAuth auth;
    FirebaseAuth auth2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dodaj_policajca_aktivnost);
        auth = FirebaseAuth.getInstance();

        this.email_txt = findViewById(R.id.email_korisnika_txt);
        this.lozinka_txt = findViewById(R.id.lozinka_korisnika_txt);
        this.spremi_btn = findViewById(R.id.registrirajSe_btn);


        this.spremi_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = email_txt.getText().toString();
                String lozinka = lozinka_txt.getText().toString();

                auth2.createUserWithEmailAndPassword(email, lozinka)
                        .addOnCompleteListener(DodajPolicajcaAktivnost.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(getApplicationContext(), "Uspješno ste registrirani.", Toast.LENGTH_LONG).show();
                                email_txt.setText("");
                                lozinka_txt.setText("");
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                String email = user.getEmail();
                                Toast.makeText(DodajPolicajcaAktivnost.this, email, Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });



    }
}
