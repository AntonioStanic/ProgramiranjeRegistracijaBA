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
import android.widget.Toast;

import com.example.programiranjeregistracijaba.model.Automobil;
import com.example.programiranjeregistracijaba.model.Osoba;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AutomatskoPretrazivanjeOsoba extends AppCompatActivity {
    RecyclerView recyclerView;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference reference;
    FirebaseRecyclerAdapter<Osoba, ViewHolder> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automatsko_pretrazivanje_osoba);

        recyclerView = findViewById(R.id.recyclerView);
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getSupportActionBar().hide();
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("osoba");

        Intent i = getIntent();
        String osoba_id = i.getStringExtra("Rezultat");
        pretraziFirebase (osoba_id);
    }
    private void pretraziFirebase(String text_pretrage) {
        // Postavite postavke za pretvorbu JSON objekta u Movie klasu
        FirebaseRecyclerOptions<Osoba> options = new FirebaseRecyclerOptions.Builder<Osoba>().setQuery(
                reference.orderByKey().startAt(text_pretrage).endAt(text_pretrage + "\uf8ff"), new SnapshotParser<Osoba>() {
                    @NonNull
                    @Override
                    public Osoba parseSnapshot(@NonNull DataSnapshot snapshot) {
                        return snapshot.getValue(Osoba.class);
                    }
                }
        ).build();

        // Adapter koji dohvaca elemente iz baze
        adapter = new FirebaseRecyclerAdapter<Osoba, ViewHolder>(options) {
            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.lista_osoba, viewGroup, false);

                ViewHolder ViewHolder = new ViewHolder(view);

                ViewHolder.setOnClickListener(new ViewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View v, int postion) {
                        String id = getRef(postion).getDatabase().getReference("osoba_id").getKey();
                        //reference.child("filmovi").child(id).child("naziv").setValue("TEST");
                        Toast.makeText(getApplicationContext(), id, Toast.LENGTH_SHORT).show();
                    }
                });
                return ViewHolder;
            }

            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Osoba osoba) {
                holder.setDetails(osoba);
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
