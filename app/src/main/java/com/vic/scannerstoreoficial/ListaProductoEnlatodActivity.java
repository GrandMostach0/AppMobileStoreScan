package com.vic.scannerstoreoficial;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.vic.scannerstoreoficial.productos.ReciclyeViewAdapter_Enlatados;
import com.vic.scannerstoreoficial.productos.enlatados;

import java.util.ArrayList;
import java.util.List;

public class ListaProductoEnlatodActivity extends AppCompatActivity {

    //componentes
    FirebaseFirestore miFarestore;

    private ReciclyeViewAdapter_Enlatados mreciclyeViewAdapter_enlatados;
    private RecyclerView listaDatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_producto_enlatados);
        miFarestore = FirebaseFirestore.getInstance();

        listaDatos = findViewById(R.id.listaProductoEnlatados);
        listaDatos.setLayoutManager(new GridLayoutManager(this, 2));
        Query query = miFarestore.collection("pet");

        FirestoreRecyclerOptions<enlatados> firestoreRecyclerOptions = new
                FirestoreRecyclerOptions.Builder<enlatados>().setQuery(query, enlatados.class).build();

        mreciclyeViewAdapter_enlatados = new ReciclyeViewAdapter_Enlatados(this, firestoreRecyclerOptions);
        mreciclyeViewAdapter_enlatados.notifyDataSetChanged();
        listaDatos.setAdapter(mreciclyeViewAdapter_enlatados);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mreciclyeViewAdapter_enlatados.startListening();
    }
}