package com.example.programiranjeregistracijaba;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class RegistracijaAktivnost extends AppCompatActivity {

    EditText email_korisnika_txt;
    EditText lozinka_korisnika_txt;
    CardView registrirajSe_btn;
    TextView prijaviSe_btn;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registracija_aktivnost);
        getSupportActionBar().hide();
        auth = FirebaseAuth.getInstance();

        this.email_korisnika_txt = findViewById(R.id.email_korisnika_txt);
        this.lozinka_korisnika_txt = findViewById(R.id.lozinka_korisnika_txt);
        this.registrirajSe_btn = findViewById(R.id.registrirajSe_btn);
        this.prijaviSe_btn = findViewById(R.id.Spremi_Kaznu_Btn);

        this.registrirajSe_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = email_korisnika_txt.getText().toString();
                String lozinka = lozinka_korisnika_txt.getText().toString();

                auth.createUserWithEmailAndPassword(email, lozinka)
                        .addOnCompleteListener(RegistracijaAktivnost.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(getApplicationContext(), "Uspješno ste registrirani.", Toast.LENGTH_LONG).show();
                                Intent i = new Intent(RegistracijaAktivnost.this, PrijavaAktivnost.class);
                                startActivity(i);
                            }
                        });
            }
        });


        prijaviSe_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegistracijaAktivnost.this, PrijavaAktivnost.class);
                startActivity(i);
            }
        });
    }
}
