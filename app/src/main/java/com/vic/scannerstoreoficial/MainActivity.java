package com.vic.scannerstoreoficial;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.vic.scannerstoreoficial.interfaz.image;
import com.vic.scannerstoreoficial.Adapter.imageAdapter;

public class MainActivity extends AppCompatActivity {

    //
    ViewPager2 pager2;
    private List<image> imageList;
    private imageAdapter adapterImageView;
    private Handler sliderHanlder = new Handler();

    //hacemos las declaraciones de los componentes ahora en la clase
    Button btnRegistro, btnRegistrar;
    EditText txtEmail, txtContra;
    ImageView iniciarGoogle, iniciarFacebook;

    //depencias [Firebase]
    FirebaseAuth mAuth;
    GoogleSignInClient mGoogleSingInClient;
    private final static int RC_SIGN_IN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        //instancia de los elementos
        txtEmail = findViewById(R.id.txtEmailRefactor);
        txtContra = findViewById(R.id.txtContraRefactor);
        btnRegistro = findViewById(R.id.btnLogeo);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        iniciarGoogle = findViewById(R.id.log_google);
        iniciarFacebook = findViewById(R.id.logo_facebook);

        /*
        pager2 = findViewById(R.id.viewPager2Menu);
        imageList = new ArrayList<>();
        imageList.add(new image(R.drawable.reabaja));
        imageList.add(new image(R.drawable.descuentos));
        imageList.add(new image(R.drawable.regalo));
        imageList.add(new image(R.drawable.logo_promociones));
        pager2.setAdapter(new imageAdapter(imageList, pager2));

        viewPager2.setOffscreenPageLimit(3);
        viewPager2.setClipChildren(false);
        viewPager2.setClipToPadding(false);

        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        CompositePageTransformer transformer = new CompositePageTransformer();
        transformer.addTransformer(new MarginPageTransformer(40));
        transformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r  * 0.14f);
            }
        });

        viewPager2.setPageTransformer(transformer);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHanlder.removeCallbacks(slideRunnable);
                sliderHanlder.postDelayed(slideRunnable, 2000);
            }
        });

         */

        //instancia
        mAuth = FirebaseAuth.getInstance();
        crearSolicitud();

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = txtEmail.getText().toString().trim();
                String pass = txtContra.getText().toString().trim();

                //validacion de los campos
                if(email.isEmpty() || pass.isEmpty()){
                    Toast.makeText(MainActivity.this, "Llene los campos", Toast.LENGTH_SHORT).show();
                }else{
                    if(emailValido(email)){
                        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    //INICIAMOS SESION
                                    Toast.makeText(MainActivity.this, "Bienvenido", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(MainActivity.this, MenuPrincipalActivity.class));
                                }else{
                                    //LAS CREDENCIALES SON INCORRECTAS
                                    Toast.makeText(MainActivity.this, "Error al iniciar sesion", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{
                        Toast.makeText(MainActivity.this, "Patron incorrecto", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RegisterActiviy.class));
            }
        });

        iniciarGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
        
        iniciarFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Por el momento no funciona esta accion", Toast.LENGTH_SHORT).show();
            }
        });

    }// -------------------

    /*
    private Runnable slideRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
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

     */

    //paso 1. crear solicitud
    private void crearSolicitud(){
        //CONFIGURAMOS EL INCICIO DE SESION DE GOOGLE
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        //Creamos un googleSingInClient
        mGoogleSingInClient = GoogleSignIn.getClient(this, gso);
    }
    //berificamos que tenga una sesion abierta si es asi entonces ire directo al scanner

    //paso 2. crear la pantalla de inicio de google
    public void signIn(){
        Intent sigIntent = mGoogleSingInClient.getSignInIntent();
        startActivityForResult(sigIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //resultado devuleto al inciar la intencion desde GoogleSingInApi.getSingIntent
        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
                //el inicio de sesión fue exitosom, autentique con FIREBASE
                GoogleSignInAccount account = task.getResult(ApiException.class);
                AutenticacionFirebase(account); //Aqui se ejecuta el metodo para logearnos con Google
            }catch (ApiException e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    //MÉTODO PARA AUTENTICAR CON FIRABASE - GOOGLE
    private void AutenticacionFirebase(GoogleSignInAccount account){
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //SI INICIO CORECTAMENTE
                            FirebaseUser user = mAuth.getCurrentUser();//OBTENEMOS AL USUARIO, EL CUAL QUIERE INICIAR SESION

                            //si el usuario inicio sesion por primera vez
                            if(task.getResult().getAdditionalUserInfo().isNewUser()){
                                String uid = user.getUid();
                                String correo = user.getEmail();
                                String name = user.getDisplayName();

                                //HASMAP PARA MANDAR LOS DATOS
                                HashMap<Object, String> DatosUsuario = new HashMap<>();
                                DatosUsuario.put("iud", uid);
                                DatosUsuario.put("Name", name);
                                DatosUsuario.put("Gmail", correo);

                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference reference = database.getReference("USUARIOS_APP");
                                reference.child(uid).setValue(DatosUsuario);

                            }

                            //ahora nos dirigimos a la actividad "INICIO"
                            startActivity(new Intent(MainActivity.this, MenuPrincipalActivity.class));
                        }else{
                            Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser usario = mAuth.getCurrentUser();
        if(usario != null){
            irMenuPrincipal();
        }
    }

    //metodo para ir el menuPrincipal
    public void irMenuPrincipal(){
        startActivity(new Intent(MainActivity.this, MenuPrincipalActivity.class));
        finish();
    }

    //metodo para ir al camara de scanner
    public void irCamara(){
        Intent i = new Intent(MainActivity.this, PagoEfectivoR.class);
        startActivity(i);
        finish();
        //evitamos que vaya a la pagina otra vez
    }

    private boolean emailValido(String email){
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}