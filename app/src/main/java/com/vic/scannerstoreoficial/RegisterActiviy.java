package com.vic.scannerstoreoficial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActiviy extends AppCompatActivity{

    //instancias [firebase]
    FirebaseFirestore mifirestore;
    FirebaseAuth mAtuh;

    //elemento o componentes
    Button btnRegistro, btnregresar;
    EditText txtEmailR, txtPasswordR, txtNombreUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_activiy);

        //instancias
        mifirestore = FirebaseFirestore.getInstance();
        mAtuh = FirebaseAuth.getInstance();

        //instancias componentes}
        txtEmailR = findViewById(R.id.txtCorreoRegistro);
        txtPasswordR = findViewById(R.id.txtPassWordRegistro);
        btnRegistro = findViewById(R.id.btnRegistro);
        btnregresar = findViewById(R.id.btn_Regresar);
        txtNombreUsuario = findViewById(R.id.txtNombreUsuario);

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String CorreoUser = txtEmailR.getText().toString().trim();
                String PasswordUser = txtPasswordR.getText().toString().trim();
                String NombreUser = txtNombreUsuario.getText().toString().trim();

                if(NombreUser.isEmpty() || CorreoUser.isEmpty() || PasswordUser.isEmpty()){
                    //hacer una notificacion si todo esta vacio
                    Toast.makeText(RegisterActiviy.this, "Completa los datos", Toast.LENGTH_LONG).show();
                }else if(!NombreUser.isEmpty() && !CorreoUser.isEmpty() && !PasswordUser.isEmpty()){
                    //varificamos que sea correo valido
                    if(emailValido(CorreoUser)){
                        if(PasswordUser.length() > 6){
                            //si no esta vacio entonces se va al metodo de registro
                            RegistroUsuario(NombreUser, CorreoUser, PasswordUser);
                        }else{
                            Toast.makeText(RegisterActiviy.this, "La contasenia debe ser mayo a 6 caracteres", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(RegisterActiviy.this, "Ingrese un Correo valido", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnregresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActiviy.this, MainActivity.class));
            }
        });

    }

    public void RegistroUsuario(String NameU, String CorreoU, String PasswordU){
        mAtuh.createUserWithEmailAndPassword(CorreoU, PasswordU).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = mAtuh.getCurrentUser();
                    //LOS DATOS A ALMACENAR EN EL FIRABASE
                    assert  user != null; //AFIRMAMOS QUE EL USUARIO NO SEA NULO
                    String uid = user.getUid();
                    /*Creamos un HASMAP para enviar los datos tipo JSON*/
                    HashMap<Object, String> DatosUsuario = new HashMap<>();
                    DatosUsuario.put("iud", uid);
                    DatosUsuario.put("Name", NameU);
                    DatosUsuario.put("Gmail", CorreoU);
                    DatosUsuario.put("Password", PasswordU);

                    //INICIALIZAMOS LA INSTANCIA A LA BASE DE DATOS DE FIREBASE
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    //CREAMOS LA BASE DE DATOS
                    DatabaseReference reference = database.getReference("USUARIOS_APP");
                    reference.child(uid).setValue(DatosUsuario);

                    Toast.makeText(RegisterActiviy.this, "Cuenta creada correctamente", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }else{
                    //no crea
                }
            }
        });
    }

    private boolean emailValido(String email){
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}