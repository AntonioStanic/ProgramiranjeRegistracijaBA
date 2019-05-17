package com.example.programiranjeregistracijaba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.programiranjeregistracijaba.model.Automobil;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AutomobiliOsobe extends AppCompatActivity {
    RecyclerView recyclerView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    FirebaseRecyclerAdapter<Automobil, ViewHolderAutomobil> adapter;
    String osoba_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automobili_osobe);
        getSupportActionBar().hide();
        this.recyclerView = findViewById(R.id.recyclerView);
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("automobili");

        Intent i = getIntent();
        String osoba_id = i.getStringExtra("Rezultat");
        pretraziFirebase (osoba_id);
    }

    private void pretraziFirebase(String text_pretrage) {
        // Postavite postavke za pretvorbu JSON objekta u Movie klasu
        FirebaseRecyclerOptions<Automobil> options = new FirebaseRecyclerOptions.Builder<Automobil>().setQuery(
                reference.orderByChild("osoba_id").startAt(text_pretrage).endAt(text_pretrage + "\uf8ff"), new SnapshotParser<Automobil>() {
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

                    }
                });
                return ViewHolderautomobil;
            }

            @Override
            protected void onBindViewHolder(@NonNull ViewHolderAutomobil holder, int position, @NonNull Automobil automobil) {
                holder.setDetails(automobil);

            }
        };

        this.recyclerView.setAdapter(adapter);
    }
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
