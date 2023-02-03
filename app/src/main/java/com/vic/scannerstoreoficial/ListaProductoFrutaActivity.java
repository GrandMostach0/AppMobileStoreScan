package com.vic.scannerstoreoficial;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.vic.scannerstoreoficial.productos.ReciclyeViewAdapter_Enlatados;
import com.vic.scannerstoreoficial.productos.ReciclyeViewAdapter_Fruta;
import com.vic.scannerstoreoficial.productos.enlatados;
import com.vic.scannerstoreoficial.productos.frutas;

import java.util.ArrayList;
import java.util.List;

public class ListaProductoFrutaActivity extends AppCompatActivity {

    //componentes
    FirebaseFirestore mFirestore;

    private ReciclyeViewAdapter_Fruta mReciclyeViewAdapter_fruta;
    private RecyclerView listaDatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_producto_fruta);

        //instancia
        mFirestore = FirebaseFirestore.getInstance();

        listaDatos = findViewById(R.id.listaProductoFrutas);
        listaDatos.setLayoutManager(new GridLayoutManager(this, 2));
        Query query = mFirestore.collection("frutas");

        FirestoreRecyclerOptions<frutas> firestoreRecyclerOptions = new
                FirestoreRecyclerOptions.Builder<frutas>().setQuery(query, frutas.class).build();

        mReciclyeViewAdapter_fruta = new ReciclyeViewAdapter_Fruta(this, firestoreRecyclerOptions);
        mReciclyeViewAdapter_fruta.notifyDataSetChanged();
        listaDatos.setAdapter(mReciclyeViewAdapter_fruta);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mReciclyeViewAdapter_fruta.startListening();
    }
}