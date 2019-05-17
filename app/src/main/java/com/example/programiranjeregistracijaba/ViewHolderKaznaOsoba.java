package com.example.programiranjeregistracijaba;

import android.view.View;
import android.widget.TextView;

import com.example.programiranjeregistracijaba.model.KaznaOsoba;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolderKaznaOsoba extends RecyclerView.ViewHolder{
    TextView iznos_txt;
    TextView opis_txt;
    TextView broj_osobne_txt;
    View mView;

    public ViewHolderKaznaOsoba(@NonNull View itemView) {
        super(itemView);
        mView = itemView;
        this.iznos_txt = itemView.findViewById(R.id.iznos_kazne_txt);
        this.opis_txt = itemView.findViewById(R.id.opis_kazne_txt);
        this.broj_osobne_txt = itemView.findViewById(R.id.broj_osobne_txt);
        this.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(v,getAdapterPosition());
            }
        });

    }

    public void setDetails(KaznaOsoba kaznaOsoba){
        this.iznos_txt.setText(kaznaOsoba.iznos);
        this.opis_txt.setText(kaznaOsoba.opis);
        this.broj_osobne_txt.setText(kaznaOsoba.broj_osobne);


    }

    private ViewHolder.ClickListener clickListener;

    public void setOnClickListener (ViewHolder.ClickListener clickListener){
        this.clickListener = clickListener;
    }
    public interface ClickListener{
        public void onItemClick(View v, int postion);
    }
}
