package com.example.programiranjeregistracijaba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.internal.cache.DiskLruCache;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.programiranjeregistracijaba.model.Automobil;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class PretraziAutomobil extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference reference;
    private TextInputLayout pretraga_txt;
    private Button pretrazi_btn;
    FirebaseUser user;
    FirebaseRecyclerAdapter<Automobil, ViewHolderAutomobil> adapter;
    String  broj_tablica;
    String osoba_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pretrazi_automobil);
        this.recyclerView = findViewById(R.id.rezultat_pretrage);
        this.pretraga_txt = findViewById(R.id.pretraga_txt);
        this.pretrazi_btn = findViewById(R.id.pretrazi_btn);
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getSupportActionBar().hide();
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("automobili");

        this.pretrazi_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String broj_tablica = pretraga_txt.getEditText().getText().toString();
                if(broj_tablica.isEmpty()) {
                    pretraga_txt.setError("Upišite broj tablica a zatim pretražite");
                    recyclerView.setAdapter(null);
                }
                else {
                    pretraziFirebase(broj_tablica);
                    pretraga_txt.setError("");
                    adapter.startListening();

                }
            }
        });
    }



    private void pretraziFirebase(String text_pretrage) {
        // Postavite postavke za pretvorbu JSON objekta u Movie klasu
        FirebaseRecyclerOptions<Automobil> options = new FirebaseRecyclerOptions.Builder<Automobil>().setQuery(
                reference.orderByChild("broj_tablica").startAt(text_pretrage.toUpperCase()).endAt(text_pretrage.toUpperCase()), new SnapshotParser<Automobil>() {
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
                    public void onItemClick(View v, int position) {
                        String id = getRef(position).getKey();
                        //Toast.makeText(getApplicationContext(), id, Toast.LENGTH_LONG).show();


                        Intent i= new Intent(getApplicationContext(),AutomatskoPretrazivanjeOsoba.class);
                        i.putExtra("Rezultat", osoba_id);
                        startActivity(i);
                        //String id2 = getRef(position).orderByChild("osoba_id");
                        //reference.orderByChild(id).orderByChild("osoba_id")
                        //String osoba_id = reference.orderByChild(id).orderByChild("osoba_id").toString();
                        //Toast.makeText(getApplicationContext(), id, Toast.LENGTH_SHORT).show();
                        //Intent i= new Intent(getApplicationContext(),AutomatskoPretrazivanjeOsoba.class);
                        //i.putExtra("Rezultat", id);
                        //startActivity(i);

                    }
                });
                return ViewHolderautomobil;
            }

            @Override
            protected void onBindViewHolder(@NonNull ViewHolderAutomobil holder, int position, @NonNull Automobil automobil) {
                holder.setDetails(automobil);
                osoba_id = (String) holder.osoba_id_txt.getText();
                //Toast.makeText(PretraziAutomobil.this, ide, Toast.LENGTH_SHORT).show();
            }
        };
        broj_tablica = pretraga_txt.getEditText().getText().toString();
        if(broj_tablica.equals("")){
            recyclerView = null;
        }
        else {
        this.recyclerView.setAdapter(adapter);
    }}


}

