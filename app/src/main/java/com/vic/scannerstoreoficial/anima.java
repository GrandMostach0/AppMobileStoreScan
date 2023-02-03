package com.vic.scannerstoreoficial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class anima extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //vamos a ajustar la venta para que cubra todo la pantalla
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_anima);

        //AHORA VAMOS A PONERLE LAS ANIMACIONES DEL
        Animation animacion1 = AnimationUtils.loadAnimation(this, R.anim.desplazamiento_arriba);
        Animation animacion2 = AnimationUtils.loadAnimation(this, R.anim.desplazamiento_abajo);

        //instanciamos los vales
        TextView txtEmpresaName = findViewById(R.id.txtNombreEmpresa);
        TextView txtDe = findViewById(R.id.txtDe);
        ImageView imageView = findViewById(R.id.imgViewLogo);

        txtDe.setAnimation(animacion2);
        txtEmpresaName.setAnimation(animacion2);
        imageView.setAnimation(animacion2);

        //cuando termine la animacion entonces nos vamos a otra actividad o ventana
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(anima.this, MainActivity.class);
                startActivity(intent);
                //ponemos que termine para que no puede regresar hacia atras y ver de nuevo
                finish();
            }
        }, 3000);

    }
}