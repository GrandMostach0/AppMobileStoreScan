package com.vic.scannerstoreoficial;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class PagoEfectivoR extends AppCompatActivity {

    EditText txtEditMontoEntregado, EditCambioCalculado;
    TextView txtMontoPagar, btnVolverInicio, btnCobrar;
    Button btnCalcularCambio;

    String monto;

    FirebaseAuth mAtuh;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pago_efectivo_r);


        mAtuh = FirebaseAuth.getInstance();
        firebaseUser = mAtuh.getCurrentUser();

        Toast.makeText(this, "", Toast.LENGTH_SHORT).show();

        //instanciamos los elementos de interfaz
        txtMontoPagar = findViewById(R.id.txtTotalPagarEfectivo);
        txtEditMontoEntregado = findViewById(R.id.txtEditMontoEntregadoEfectivo);
        EditCambioCalculado = findViewById(R.id.txtEditCambioCalculadoEfectivo);
        btnCalcularCambio = findViewById(R.id.btnCalcularCambio);
        btnVolverInicio = findViewById(R.id.btnVolverInicio);
        btnCobrar = findViewById(R.id.btnCobrar);

        monto = getIntent().getStringExtra("valorPagar");
        txtMontoPagar.setText(monto);

        btnCalcularCambio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calcularCambio();
            }
        });

        btnCobrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(PagoEfectivoR.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("Lector - CDP");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(true);
                integrator.setBarcodeImageEnabled(true);
                integrator.initiateScan();
            }
        });

        btnVolverInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PagoEfectivoR.this, MenuPrincipalActivity.class));
            }
        });

    }

    public void calcularCambio(){
        int cantidadPagar = 0;
        int pagar = 0;
        try{
            cantidadPagar = Integer.parseInt(monto);
            pagar = Integer.parseInt(txtEditMontoEntregado.getText().toString());
        }catch (NumberFormatException ex){
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }

        if(txtMontoPagar.getText().equals("")){
            Toast.makeText(this, "No hay nada que pagar", Toast.LENGTH_SHORT).show();
        }else{
            int devolver = cantidadPagar - pagar;
            if(devolver < 0){
                devolver = (devolver) * -1;
                EditCambioCalculado.setText(""+devolver);
            }else{
                EditCambioCalculado.setText("" + devolver);
            }
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if(result != null){
            if(result.getContents() == null){
                //Toast para hacer alertas
                Toast.makeText(this, "Lectora Cancelada", Toast.LENGTH_LONG).show(); //para mostrar el mensaje
            }else{
                Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                txtMontoPagar.setText(result.getContents());
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

}