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
import com.vic.scannerstoreoficial.productos.ReciclyeViewAdapter_Lacteos;
import com.vic.scannerstoreoficial.productos.carnes;
import com.vic.scannerstoreoficial.productos.enlatados;
import com.vic.scannerstoreoficial.productos.lacteos;

import java.util.ArrayList;
import java.util.List;

public class ListaProductoLacteosActivity extends AppCompatActivity {

    //componentes
    //elementos de la base de datos
    FirebaseFirestore mFirestore;
    //componentes

    //instancias para reciciclar datos de la vista
    private ReciclyeViewAdapter_Lacteos mRecyclerAdapterLacteos;
    private RecyclerView listaDatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_producto_lacteos);

        //instancia de la base de datos
        mFirestore = FirebaseFirestore.getInstance();

        listaDatos = findViewById(R.id.listaProductoLacteos);
        listaDatos.setLayoutManager(new GridLayoutManager(this, 2));
        Query query = mFirestore.collection("lacteos");

        FirestoreRecyclerOptions<lacteos> firestoreRecyclerOptions = new
                FirestoreRecyclerOptions.Builder<lacteos>().setQuery(query, lacteos.class).build();

        mRecyclerAdapterLacteos = new ReciclyeViewAdapter_Lacteos(this, firestoreRecyclerOptions);
        listaDatos.setAdapter(mRecyclerAdapterLacteos);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mRecyclerAdapterLacteos.startListening();
    }
}