package com.example.programiranjeregistracijaba;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.programiranjeregistracijaba.model.Automobil;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolderAutomobil extends RecyclerView.ViewHolder {

    View mView;
    TextView broj_tablica_txt;
    TextView marka_txt;
    TextView registriran_do_txt;
    TextView osoba_id_txt;
    ImageView slika_img;

    public ViewHolderAutomobil(@NonNull View itemView) {
        super(itemView);
        mView = itemView;
        this.broj_tablica_txt = itemView.findViewById(R.id.broj_tablica_txt);
        this.marka_txt = itemView.findViewById(R.id.marka_txt);
        this.registriran_do_txt = itemView.findViewById(R.id.registriran_do);
        this.osoba_id_txt = itemView.findViewById(R.id.osoba_id_txt);
        this.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(v,getAdapterPosition());

            }
        });
    }


    public void setDetails(Automobil automobil) {
        this.broj_tablica_txt.setText(automobil.broj_tablica);
        this.marka_txt.setText(automobil.marka);
        this.registriran_do_txt.setText(automobil.registriran_do);
        this.osoba_id_txt.setText(automobil.osoba_id);


    }
    private ClickListener clickListener;

    public void setOnClickListener (ClickListener clickListener){
        this.clickListener = clickListener;
    }
    public interface ClickListener{
        public void onItemClick(View v, int postion);
    }



}


