package com.vic.scannerstoreoficial;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.api.ApiException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.vic.scannerstoreoficial.Adapter.CustomAdapter;
import com.vic.scannerstoreoficial.Adapter.imageAdapter;
import com.vic.scannerstoreoficial.interfaz.Dominio;
import com.vic.scannerstoreoficial.interfaz.image;

import java.util.ArrayList;
import java.util.List;

public class MenuPrincipalActivity extends AppCompatActivity {

    //instancia para las vistas de la lista de los productos
    ViewPager2 viewPager;
    private List<image> imageList;
    private imageAdapter imageAdapterPager2;

    private Handler sliderHanlder = new Handler();

    //firebase
    FirebaseAuth mAtuh;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference MI_BASE_DATOS;
    FirebaseUser firebaseUser;

    //elementos de las vistas
    TextView txtNombreUser;

    //instancias de los elementos de la vista
    private RecyclerView.Adapter adapter, adapter2;
    private RecyclerView recyclerViewCategoriaLista, recyclerViewPopularLista;

    ImageView img_inicio, img_comprar, img_salir, img_us;
    LinearLayout ly_agregar_Producto, ly_ir_carrito, ly_salir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
        recyclerViewCategoria();

        recyclerViewPopular();


        //instancia a firebase
        mAtuh = FirebaseAuth.getInstance();
        firebaseUser = mAtuh.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        MI_BASE_DATOS = firebaseDatabase.getReference("USUARIOS_APP");

        txtNombreUser = findViewById(R.id.txtNombreUTitulo);
        img_us = findViewById(R.id.img_Usuario);
        ly_agregar_Producto = findViewById(R.id.espacioParaAgregar);
        ly_agregar_Producto.setEnabled(true);
        ly_ir_carrito = findViewById(R.id.espacioParaComprar);
        ly_salir = findViewById(R.id.espacioParaSalir);

        //slider de imagenes
        viewPager = findViewById(R.id.viewPager2Menu);
        imageList = new ArrayList<>();
        imageList.add(new image(R.drawable.reabaja));
        imageList.add(new image(R.drawable.descuentos));
        imageList.add(new image(R.drawable.regalo));
        imageList.add(new image(R.drawable.logo_promociones));
        imageAdapterPager2 = new imageAdapter(imageList, viewPager);
        viewPager.setAdapter(imageAdapterPager2);

        viewPager.setOffscreenPageLimit(3);
        viewPager.setClipChildren(false);
        viewPager.setClipToPadding(false);

        viewPager.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        CompositePageTransformer transformer = new CompositePageTransformer();
        transformer.addTransformer(new MarginPageTransformer(40));
        transformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r  * 0.14f);
            }
        });

        viewPager.setPageTransformer(transformer);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHanlder.removeCallbacks(slideRunnable);
                sliderHanlder.postDelayed(slideRunnable, 2000);
            }
        });

        //para salir de la app y cerrar sesion

        ly_salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAtuh.signOut();
                irMain();
            }

            //verificacion si hay conexion a internet

        });

        //para ir a comprar
        ly_ir_carrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ir_Comprar();
                finish();
            }
        });

        ly_agregar_Producto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                irAddProducto();
            }
        });

    }

    //-----------------------------------
    private Runnable slideRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        sliderHanlder.removeCallbacks(slideRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sliderHanlder.postDelayed(slideRunnable, 2000);
    }

    //_------------------

    //verificamos la sesion si esta abierta

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAtuh.getCurrentUser();
        if(user == null){
            irMain();
        }else if(!mAtuh.getUid().equals("evZXCZNsvTTxp1su7OsOarkfyaV2")){
            ly_agregar_Producto.setEnabled(false);
            ly_agregar_Producto.setVisibility(ly_agregar_Producto.INVISIBLE);
        }
        traerDatos();
    }

    public void irMain(){
        startActivity(new Intent(MenuPrincipalActivity.this, MainActivity.class));
        finish();
    }

    public void ir_Comprar(){
        startActivity(new Intent(MenuPrincipalActivity.this, ListaProductoCarritoActivity.class));
    }

    public void irAddProducto(){
        startActivity(new Intent(MenuPrincipalActivity.this, AgregarProducto.class));
        finish();
    }

    public void recyclerViewPopular(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewPopularLista = findViewById(R.id.VistaCategorias);
        recyclerViewPopularLista.setLayoutManager(linearLayoutManager);
    }

    //metodo para mostrar las categorias
    private void recyclerViewCategoria() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCategoriaLista = findViewById(R.id.VistaCategorias);
        recyclerViewCategoriaLista.setLayoutManager(linearLayoutManager);

        ArrayList<Dominio> categoriaLista = new ArrayList<>();
        categoriaLista.add(new Dominio("Carnes", "carnes", R.drawable.carnes));
        categoriaLista.add(new Dominio("Enlatados", "enlatados", R.drawable.enlatados));
        categoriaLista.add(new Dominio("Frutas", "frutas", R.drawable.frutas));
        categoriaLista.add(new Dominio("Lacteos", "lacteos", R.drawable.lacteos));
        categoriaLista.add(new Dominio("Refresco", "refresco", R.drawable.refresco));

        adapter = new CustomAdapter(this, categoriaLista);
        recyclerViewCategoriaLista.setAdapter(adapter);
    }

    /*Metodo para traer los datos de los usuarios*/
    public void traerDatos(){
        Query query = MI_BASE_DATOS.orderByChild("Gmail").equalTo(firebaseUser.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //RECORREMOS LOS USUARIOS QUE ESTAN REGISTRADOS EN LA BASE DE DATOS
                for(DataSnapshot ds: snapshot.getChildren()){
                    String nameUser = ""+ds.child("Name").getValue();
                    //String imgUser = ""+ds.child("img").getValue();

                    //CON LOS DATOS OBTENIDOS SETEAMOS LOS DATOS EN AL VISTAS
                    txtNombreUser.setText(nameUser);
                    /*Este apártado es para colocar una imagen si mas adelante se desea implementar

                    try{
                        //primero hay que implementar picasso
                        Picasso.get().load(imgUser).placeholder(R.drawable.img_perfil).into(foto_perfil);

                    }catch (Exception e){
                        //SI EL USUARIO NO CUENTA CON UNA IMAGEN EN LA BASE DE DATOS SE LE COLOCA UNA POR DEFECTO
                        Picasso.get().load(R.drawable.img_perfil).into(foto_perfil);
                    }

                     */

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}