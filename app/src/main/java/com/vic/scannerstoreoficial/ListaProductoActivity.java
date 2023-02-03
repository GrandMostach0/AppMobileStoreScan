package com.vic.scannerstoreoficial;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.vic.scannerstoreoficial.Adapter.CustomAdapter;
import com.vic.scannerstoreoficial.productos.ReciclyeViewAdapter_Carne;
import com.vic.scannerstoreoficial.productos.ReciclyeViewAdapter_Enlatados;
import com.vic.scannerstoreoficial.productos.carnes;
import com.vic.scannerstoreoficial.productos.enlatados;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ListaProductoActivity extends AppCompatActivity {

    //elementos de la base de datos
    FirebaseFirestore mFirestore;
    //componentes

    //instancias para reciciclar datos de la vista
    private ReciclyeViewAdapter_Carne mRecyclerAdapterCarne;
    private RecyclerView listaDatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_producto);

        //instancia de la base de datos
        mFirestore = FirebaseFirestore.getInstance();

        listaDatos = findViewById(R.id.listaProductos);
        listaDatos.setLayoutManager(new GridLayoutManager(this, 2));
        Query query = mFirestore.collection("carnes");

        FirestoreRecyclerOptions<carnes> firestoreRecyclerOptions = new
                FirestoreRecyclerOptions.Builder<carnes>().setQuery(query, carnes.class).build();

        mRecyclerAdapterCarne = new ReciclyeViewAdapter_Carne(this, firestoreRecyclerOptions);
        mRecyclerAdapterCarne.notifyDataSetChanged();
        listaDatos.setAdapter(mRecyclerAdapterCarne);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mRecyclerAdapterCarne.startListening();
    }
}