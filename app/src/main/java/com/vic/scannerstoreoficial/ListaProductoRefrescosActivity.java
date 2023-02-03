package com.vic.scannerstoreoficial;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.vic.scannerstoreoficial.productos.ReciclyeViewAdapter_Carne;
import com.vic.scannerstoreoficial.productos.ReciclyeViewAdapter_Enlatados;
import com.vic.scannerstoreoficial.productos.ReciclyeViewAdapter_Refrescos;
import com.vic.scannerstoreoficial.productos.carnes;
import com.vic.scannerstoreoficial.productos.enlatados;
import com.vic.scannerstoreoficial.productos.refresco;

import java.util.ArrayList;
import java.util.List;

public class ListaProductoRefrescosActivity extends AppCompatActivity {


    FirebaseFirestore mFirestore;
    //componentes

    //instancias para reciciclar datos de la vista
    private ReciclyeViewAdapter_Refrescos mRecyclerAdapterRefresco;
    private RecyclerView listaDatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_producto_refrescos);

        //instancia de la base de datos
        mFirestore = FirebaseFirestore.getInstance();

        listaDatos = findViewById(R.id.listaRefresco);
        listaDatos.setLayoutManager(new GridLayoutManager(this, 2));
        Query query = mFirestore.collection("refrescos");

        FirestoreRecyclerOptions<refresco> firestoreRecyclerOptions = new
                FirestoreRecyclerOptions.Builder<refresco>().setQuery(query, refresco.class).build();

        mRecyclerAdapterRefresco = new ReciclyeViewAdapter_Refrescos(this, firestoreRecyclerOptions);
        mRecyclerAdapterRefresco.notifyDataSetChanged();
        listaDatos.setAdapter(mRecyclerAdapterRefresco);
    }


}