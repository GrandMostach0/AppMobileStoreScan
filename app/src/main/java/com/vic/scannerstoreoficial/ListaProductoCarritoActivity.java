package com.vic.scannerstoreoficial;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.vic.scannerstoreoficial.interfaz.DominioCarrito;
import com.vic.scannerstoreoficial.interfaz.carritoList;

import java.util.ArrayList;

public class ListaProductoCarritoActivity extends AppCompatActivity {

    boolean adminFlAG = false;

    DominioCarrito adapterCarrito;
    ArrayList<carritoList> carrito = new ArrayList<>();

    Dialog dialog;

    FirebaseAuth mAtuh;
    FirebaseFirestore mFirestore;

    LinearLayout ly_menu, ly_comprar;
    TextView txtTotalCompra, txtCodigoBarras, txtNombre;
    Button btn_agregar, btnPagoDigital, btnPagoEfectivo, btnCobrar;
    RecyclerView recyclerViewCarrito;
    ImageView qrImage_carritoCompra;

    int total = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_producto_carrito);

        dialog = new Dialog(this);

        mAtuh = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        ly_menu = findViewById(R.id.irInicio);
        ly_comprar = findViewById(R.id.irCompras);
        qrImage_carritoCompra = (ImageView) findViewById(R.id.imgQr_carrito);
        btn_agregar = findViewById(R.id.btnAgregarCarrito);
        btnPagoDigital = findViewById(R.id.btnPagoDigital);
        btnPagoEfectivo = findViewById(R.id.btnPagoEfectivo);
        txtTotalCompra = findViewById(R.id.txtTotalCarritoCompra);
        txtCodigoBarras = findViewById(R.id.txtCodigoBarraCarrito);
        recyclerViewCarrito = findViewById(R.id.recyclerView_carrito);
        btnCobrar = findViewById(R.id.btnCobrarLista);
        recyclerViewCarrito.setLayoutManager(new LinearLayoutManager(this));

        ly_comprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ListaProductoCarritoActivity.this, "Ya estas en esta ventana", Toast.LENGTH_SHORT).show();
            }
        });
        ly_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ListaProductoCarritoActivity.this, MenuPrincipalActivity.class));
                finish();
            }
        });
        btn_agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(ListaProductoCarritoActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("Lector - CDP");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(true);
                integrator.setBarcodeImageEnabled(true);
                integrator.initiateScan();
            }
        });

        btnPagoDigital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ListaProductoCarritoActivity.this, "Proximamente", Toast.LENGTH_SHORT).show();
            }
        });

        btnPagoEfectivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(adminFlAG == false){
                    Intent i = new Intent(ListaProductoCarritoActivity.this, PagoEfectivoR.class);
                    i.putExtra("valorPagar", txtTotalCompra.getText().toString());
                    startActivity(i);
                }else{
                    try{
                        if(!txtTotalCompra.getText().equals("")){
                            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                            Bitmap bitmap = barcodeEncoder.encodeBitmap(txtTotalCompra.getText().toString(), BarcodeFormat.QR_CODE, 150, 150);
                            qrImage_carritoCompra.setImageBitmap(bitmap);
                        }else{
                            Toast.makeText(ListaProductoCarritoActivity.this, "El campo esta vacio", Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        Log.d("Error", " e " + e.getMessage());
                    }
                }
            }
        });

        btnCobrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListaProductoCarritoActivity.this, PagoEfectivoR.class);
                startActivity(intent);
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if(result != null){
            if(result.getContents() == null){
                //Toast para hacer alertas
                Toast.makeText(this, "Lectora Cancelada", Toast.LENGTH_LONG).show(); //para mostrar el mensaje
            }else{
                Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                txtCodigoBarras.setText(result.getContents());
                getDatos(txtCodigoBarras.getText().toString());
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    public void getDatos(String codigo){
        mFirestore.collection("all_productos").document(codigo).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String nombre = documentSnapshot.getString("NombreProducto");
                String categoria = documentSnapshot.getString("categoria");
                String cantidad = documentSnapshot.getString("cantidad");
                double precio = documentSnapshot.getDouble("Precio");
                String descripcion = documentSnapshot.getString("descripcion");
                String Image = documentSnapshot.getString("Image");
                //int precioF = Integer.parseInt(Precio);

                total = total + (int) precio;
                txtTotalCompra.setText("" + total);
                carrito.add(new carritoList(nombre, "hola", 0,(int) precio, "noni", codigo, Image));
                adapterCarrito = new DominioCarrito(ListaProductoCarritoActivity.this, carrito);
                adapterCarrito.notifyDataSetChanged();
                recyclerViewCarrito.setAdapter(adapterCarrito);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ListaProductoCarritoActivity.this, "Error al traer los datos", Toast.LENGTH_SHORT).show();
            }
        });
    }


    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAtuh.getCurrentUser();
        if(!mAtuh.getUid().equals("evZXCZNsvTTxp1su7OsOarkfyaV2")){
            btnCobrar.setEnabled(false);
            btnCobrar.setVisibility(View.INVISIBLE);

            adminFlAG = true;

        }else{
            adminFlAG = false;
        }
    }

}