package com.vic.scannerstoreoficial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DetallesProducto extends AppCompatActivity {

    //elementos
    TextView txtNombrePrudcto, txtPrecioProducto, txtDescripcionProducto;
    ImageView imageProducto;

    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_producto);

        //recive los datos
        Intent intent = getIntent();
        String codigo = intent.getStringExtra("codigo");
        String nombre = intent.getStringExtra("nombre");
        String precio = intent.getStringExtra("precio");
        String descripcion = intent.getStringExtra("descripcion");
        String imagen = intent.getStringExtra("Image");
        mFirestore = FirebaseFirestore.getInstance();

        txtNombrePrudcto = findViewById(R.id.txtNombreProducto);
        txtPrecioProducto = findViewById(R.id.txtprecioProducto);
        txtDescripcionProducto = findViewById(R.id.txtDescripcionProducto);
        imageProducto = findViewById(R.id.imagen_Producto_Detalle);

        try{
            if(!imagen.equals("")){
                Picasso.with(this)
                        .load(imagen)
                        .into(imageProducto);
            }
        }catch (Exception e){
            Log.d("Error", "e: " + e);
        }

        txtNombrePrudcto.setText(nombre);
        txtPrecioProducto.setText(precio);
        txtDescripcionProducto.setText(descripcion);

    }
}