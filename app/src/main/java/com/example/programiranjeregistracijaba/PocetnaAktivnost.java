package com.example.programiranjeregistracijaba;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.appcompat.app.AppCompatActivity;

public class PocetnaAktivnost extends AppCompatActivity {
    LinearLayout pretrazi_osobu;
    LinearLayout pretrazi_automobil;
    LinearLayout uslikaj_tablice;
    LinearLayout odjavi_me;
    LinearLayout izdaj_kaznu;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pocetna_aktivnost);
        this.izdaj_kaznu = findViewById(R.id.izdaj_kaznu);
        this.pretrazi_osobu = findViewById(R.id.pretrazi_osobu);
        this.pretrazi_automobil = findViewById(R.id.pretrazi_automobil);
        this.uslikaj_tablice = findViewById(R.id.uslikaj_tablice);
        this.odjavi_me = findViewById(R.id.odjavi_me);

        String email = user.getEmail();
        getSupportActionBar().hide();

        izdaj_kaznu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Intent i = new Intent(PocetnaAktivnost.this, IzdajKaznu.class);
                Intent i = new Intent(PocetnaAktivnost.this, IzdajKaznu.class);
                startActivity(i);
            }
        });

        pretrazi_osobu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PocetnaAktivnost.this, PretraziOsobu.class);
                startActivity(i);
            }
        });

        pretrazi_automobil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PocetnaAktivnost.this, PretraziAutomobil.class);
                startActivity(i);
            }
        });


        uslikaj_tablice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PocetnaAktivnost.this, UslikajTablice.class);
                startActivity(i);
            }
        });

        odjavi_me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PocetnaAktivnost.this, PrijavaAktivnost.class);
                startActivity(i);
                finish();
                FirebaseAuth.getInstance().signOut();
            }
        });

    }
}
