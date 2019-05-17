package com.example.programiranjeregistracijaba;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.programiranjeregistracijaba.model.Osoba;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PretraziOsobu extends AppCompatActivity {


    private TextInputLayout pretraga_txt;
    private Button pretrazi_btn;
    private RecyclerView recyclerView;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference reference;
    FirebaseRecyclerAdapter<Osoba, ViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pretrazi_osobu);
        getSupportActionBar().hide();

        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("osoba");

        this.pretraga_txt = findViewById(R.id.pretraga_txt);
        this.pretrazi_btn = findViewById(R.id.pretrazi_btn);

        this.recyclerView = findViewById(R.id.rezultat_pretrage);
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        this.pretrazi_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String osoba_txt = pretraga_txt.getEditText().getText().toString();
                Toast.makeText(PretraziOsobu.this, osoba_txt, Toast.LENGTH_SHORT).show();
                if(osoba_txt.isEmpty()) {
                    pretraga_txt.setError("Upišite JMBG korisnika a zatim pretražite");
                    recyclerView.setAdapter(null);
                }
                else {
                    pretraziFirebase(osoba_txt);
                    pretraga_txt.setError("");
                    adapter.startListening();
                }
            }
        });

    }


    private void pretraziFirebase(String text_pretrage) {
        // Postavite postavke za pretvorbu JSON objekta u Movie klasu
        FirebaseRecyclerOptions<Osoba> options = new FirebaseRecyclerOptions.Builder<Osoba>().setQuery(
                reference.orderByChild("jmbg").startAt(text_pretrage).endAt(text_pretrage), new SnapshotParser<Osoba>() {
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
                        String id = getRef(postion).getKey();
                        //reference.child("filmovi").child(id).child("naziv").setValue("TEST");
                        Toast.makeText(getApplicationContext(), id, Toast.LENGTH_SHORT).show();
                        Intent i= new Intent(getApplicationContext(),AutomobiliOsobe.class);
                        i.putExtra("Rezultat", id);
                        startActivity(i);
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
}

