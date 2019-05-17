package com.example.programiranjeregistracijaba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Movie;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.programiranjeregistracijaba.model.Automobil;
import com.example.programiranjeregistracijaba.model.Osoba;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class AutomatskoPretrazivanjeAutomobila extends AppCompatActivity {

    RecyclerView recyclerView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    FirebaseRecyclerAdapter<Automobil, ViewHolderAutomobil> adapter;
    String osoba_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automatsko_pretrazivanje_automobila);
        getSupportActionBar().hide();
        this.recyclerView = findViewById(R.id.rezultat_pretrage);
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("automobili");

        Intent i = getIntent();
        String broj_tablica = i.getStringExtra("Rezultat");
        pretraziFirebase (broj_tablica);

    }


    private void pretraziFirebase(String text_pretrage) {
        // Postavite postavke za pretvorbu JSON objekta u Movie klasu
        FirebaseRecyclerOptions<Automobil> options = new FirebaseRecyclerOptions.Builder<Automobil>().setQuery(
                reference.orderByChild("broj_tablica").startAt(text_pretrage).endAt(text_pretrage + "\uf8ff"), new SnapshotParser<Automobil>() {
                    @NonNull
                    @Override
                    public Automobil parseSnapshot(@NonNull DataSnapshot snapshot) {
                        return snapshot.getValue(Automobil.class);
                    }
                }
        ).build();

        // Adapter koji dohvaca elemente iz baze
        adapter = new FirebaseRecyclerAdapter<Automobil, ViewHolderAutomobil>(options) {
            @NonNull
            @Override
            public ViewHolderAutomobil onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.lista_automobila, viewGroup, false);

                ViewHolderAutomobil ViewHolderautomobil = new ViewHolderAutomobil(view);

                ViewHolderautomobil.setOnClickListener(new ViewHolderAutomobil.ClickListener() {
                    @Override
                    public void onItemClick(View v, int postion) {
                        String id = getRef(postion).getKey();
                        //reference.child("filmovi").child(id).child("naziv").setValue("TEST");
                        //Toast.makeText(getApplicationContext(), id, Toast.LENGTH_SHORT).show();
                        Intent i= new Intent(getApplicationContext(),AutomatskoPretrazivanjeOsoba.class);
                        i.putExtra("Rezultat", osoba_id);
                        startActivity(i);
                    }
                });
                return ViewHolderautomobil;
            }

            @Override
            protected void onBindViewHolder(@NonNull ViewHolderAutomobil holder, int position, @NonNull Automobil automobil) {
                holder.setDetails(automobil);
                osoba_id = (String) holder.osoba_id_txt.getText();
            }
        };

        this.recyclerView.setAdapter(adapter);
    }



/*

    private void pretraziFirebase (final String text_pretrage) {
        Query firebaseSearchQuery = reference.orderByChild("broj_tablica").startAt(text_pretrage).endAt(text_pretrage + "\uf8ff");
        FirebaseRecyclerAdapter<Automobil, ViewHolderAutomobil> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Automobil, ViewHolderAutomobil>(
                Automobil.class,
                R.layout.lista_automobila,
                ViewHolderAutomobil.class,
                firebaseSearchQuery

        ) {

            @Override
            protected void populateViewHolder(ViewHolderAutomobil viewHolder, Automobil automobil, int i) {
                viewHolder.setDetails(getApplicationContext(), automobil.getIme(), automobil.getPrezime(), automobil.getMarka(), automobil.getBroj_tablica(), automobil.getRegistriran_do());

            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }*/

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();

    }
}

