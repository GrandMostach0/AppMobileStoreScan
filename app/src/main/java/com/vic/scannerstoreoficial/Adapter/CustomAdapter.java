package com.vic.scannerstoreoficial.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vic.scannerstoreoficial.DetallesProducto;
import com.vic.scannerstoreoficial.ListaProductoActivity;
import com.vic.scannerstoreoficial.ListaProductoEnlatodActivity;
import com.vic.scannerstoreoficial.ListaProductoFrutaActivity;
import com.vic.scannerstoreoficial.ListaProductoLacteosActivity;
import com.vic.scannerstoreoficial.ListaProductoRefrescosActivity;
import com.vic.scannerstoreoficial.R;
import com.vic.scannerstoreoficial.interfaz.Dominio;

import java.util.ArrayList;


public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private ArrayList<Dominio> categoria_Dominio;
    private Context mcontext;
    private int posicion;

    public CustomAdapter(){ }

    public CustomAdapter(Context mcontext, ArrayList<Dominio> categoria_Dominio){
        this.mcontext = mcontext;
        this.categoria_Dominio = categoria_Dominio;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mcontext);
        View viewR = inflater.inflate(R.layout.viewholder_category, parent, false);
        return new ViewHolder(viewR);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.categoriaNombre.setText(categoria_Dominio.get(position).getTitle());
        holder.categoriaPic.setImageResource(categoria_Dominio.get(position).getThumbail());

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mcontext, "Posicion: " + position, Toast.LENGTH_SHORT).show();
                if(position == 0){
                    Toast.makeText(mcontext, "Categoria " +  categoria_Dominio.get(position).getTitle(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(mcontext, ListaProductoActivity.class);
                    mcontext.startActivity(intent);
                }else if(position == 1){
                    Toast.makeText(mcontext, "Categoria " +  categoria_Dominio.get(position).getTitle(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(mcontext, ListaProductoEnlatodActivity.class);
                    mcontext.startActivity(intent);
                }else if(position == 2){
                    Toast.makeText(mcontext, "Categoria " +  categoria_Dominio.get(position).getTitle(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(mcontext, ListaProductoFrutaActivity.class);
                    mcontext.startActivity(intent);
                }else if(position == 3){
                    Toast.makeText(mcontext, "Categoria " +  categoria_Dominio.get(position).getTitle(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(mcontext, ListaProductoLacteosActivity.class);
                    mcontext.startActivity(intent);
                }else if(position == 4){
                    Toast.makeText(mcontext, "Categoria " +  categoria_Dominio.get(position).getTitle(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(mcontext, ListaProductoRefrescosActivity.class);
                    mcontext.startActivity(intent);
                }

            }
        });

    }
    @Override
    public int getItemCount() { return categoria_Dominio.size();}

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView categoriaNombre;
        ImageView categoriaPic;
        ConstraintLayout mainLayout;

        public ViewHolder(@NonNull View itemview) {
            super(itemview);
            categoriaNombre = itemview.findViewById(R.id.categoriaNombre);
            categoriaPic = itemview.findViewById(R.id.categoriaPic);
            mainLayout = itemview.findViewById(R.id.mainLayout);
        }
    }

    public void setPosicion(int posicion){
        this.posicion = posicion;
    }

    public int getPosicion(){
        return posicion;
    }
}
