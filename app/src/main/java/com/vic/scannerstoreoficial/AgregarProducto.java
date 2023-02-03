package com.vic.scannerstoreoficial;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AgregarProducto extends AppCompatActivity {

    Spinner spiner1;
    ImageView imgProductoAdd;
    EditText nomProducto, precio, descripcion, codigoBarra, cantidadProducto;
    Button btnCapturarImagen, btnCodigoBarras, btnAgregar, btnCancelar, btnEliminarImagen;

    //base de datos FireBase
    private FirebaseFirestore mFirestore;
    private StorageReference storageReference;

    String storage_path="imgProductos/*";
    private Uri img_Url;
    String photo = "photo";
    String url_DescargaImage;

    //CODIGOS DEL INTENT RESULT CAMERA
    private static final int COD_CAMAERA = 21;
    private static final int COD_SEL_IMAGE=300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_producto);

        //dependencias FireStore
        mFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        //dependencias
        imgProductoAdd = findViewById(R.id.img_productoAdd);
        nomProducto = findViewById(R.id.txtNombreProductoAdd);
        spiner1 = findViewById(R.id.spinnerCategoriaAdd);

        String[] opcionesSpinner = {" ","carnes", "enlatados", "frutas", "lacteos", "refrescos"};
        ArrayAdapter<String> adaptadorOpciones = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, opcionesSpinner);
        spiner1.setAdapter(adaptadorOpciones);
        cantidadProducto = findViewById(R.id.txtCantidadProductoAdd);
        precio = findViewById(R.id.txtPrecioProductoAdd);
        descripcion = findViewById(R.id.txtDescripcionProductoAdd);
        codigoBarra = findViewById(R.id.txtCodigoBarraAdd);

        //botones
        btnCapturarImagen = findViewById(R.id.btnagregarImagenAdd);
        btnCodigoBarras = findViewById(R.id.btnAgregarCodigoBarraAdd);
        btnAgregar = findViewById(R.id.btnAgregarAdd);
        btnCancelar = findViewById(R.id.btnCancelarAdd);
        btnEliminarImagen = findViewById(R.id.btnEliminarmagenAdd);

        //acciones de clicleado
        btnCapturarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //abrirCamara();
                upLoadImage();
            }
        });

        btnEliminarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgProductoAdd.setImageURI(null);
            }
        });

        btnCodigoBarras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(AgregarProducto.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("Lector - CDP");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(true);
                integrator.setBarcodeImageEnabled(true);
                integrator.initiateScan();
            }
        });

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //obtenemos los datos que tiene la interfaz

                try {
                    String namep = nomProducto.getText().toString().trim();
                    int cantidad = Integer.parseInt(cantidadProducto.getText().toString().trim());
                    int preciop = Integer.parseInt(precio.getText().toString().trim());
                    String descp = descripcion.getText().toString().trim();
                    String codp = codigoBarra.getText().toString().trim();

                    Toast.makeText(AgregarProducto.this, url_DescargaImage , Toast.LENGTH_LONG).show();

                    String seleccion = spiner1.getSelectedItem().toString();

                    if(!seleccion.isEmpty() || !namep.isEmpty() || cantidad == 0 || preciop == 0 || !descp.isEmpty() || !codp.isEmpty() || !img_Url.equals("")){
                        if(seleccion.equals("carnes")){
                            Toast.makeText(AgregarProducto.this, seleccion, Toast.LENGTH_SHORT).show();
                            postCarnes(namep, seleccion, cantidad, preciop, descp, codp, img_Url);
                            postTodosLosProductos(namep, seleccion, cantidad, preciop, descp, codp, img_Url);

                        }else if(seleccion.equals("enlatados")){
                            Toast.makeText(AgregarProducto.this, seleccion, Toast.LENGTH_SHORT).show();
                            postEnlatados(namep, seleccion, cantidad, preciop, descp, codp, img_Url);
                            postTodosLosProductos(namep, seleccion, cantidad, preciop, descp, codp, img_Url);

                        }else if(seleccion.equals("frutas")){
                            Toast.makeText(AgregarProducto.this, seleccion, Toast.LENGTH_SHORT).show();
                            postFrutas(namep, seleccion, cantidad, preciop, descp, codp, img_Url);
                            postTodosLosProductos(namep, seleccion, cantidad, preciop, descp, codp, img_Url);

                        }else if(seleccion.equals("lacteos")){
                            Toast.makeText(AgregarProducto.this, seleccion, Toast.LENGTH_SHORT).show();
                            postLacteos(namep, seleccion, cantidad, preciop, descp, codp, img_Url);
                            postTodosLosProductos(namep, seleccion, cantidad, preciop, descp, codp, img_Url);

                        }else if(seleccion.equals("refrescos")){
                            Toast.makeText(AgregarProducto.this, seleccion, Toast.LENGTH_SHORT).show();
                            postRefrescos(namep, seleccion, cantidad, preciop, descp, codp, img_Url);
                            postTodosLosProductos(namep, seleccion, cantidad, preciop, descp, codp, img_Url);
                        }
                    }else{
                        Toast.makeText(AgregarProducto.this, "Algunos campos estan vacios, verifique de nuevo", Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){
                    Log.d("Exeption", "e" + e);
                    Toast.makeText(AgregarProducto.this, "Algunos campos estan vacios, verifique de nuevo", Toast.LENGTH_SHORT).show();
                }
                //subirImagen(img_Url);

            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AgregarProducto.this, "Accion cancelada", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AgregarProducto.this, MenuPrincipalActivity.class));
            }
        });

    }
    /*
    //- - - - - - - - - - metodo para capturar imagen - - - - - - - - - -
    public void abrirCamara(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File imageArchivo = null;
        try {
            imageArchivo = crearImagen();
        }catch (IOException e){
            Log.e("Error", e.toString());
        }

        if(imageArchivo!=null){
            Uri fotoUri = FileProvider.getUriForFile(this, "com.vic.scannerstoreoficial.fileprovider", imageArchivo);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fotoUri);
            startActivityForResult(intent, 1);
            Toast.makeText(this, ":" + fotoUri, Toast.LENGTH_LONG).show();
        }
    }

     */

    private void upLoadImage(){
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        startActivityForResult(i, COD_SEL_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d("img_url", "requestcode - RESULT OK: " + requestCode + " " + RESULT_OK);
        if(requestCode == COD_SEL_IMAGE && resultCode == RESULT_OK){
            img_Url = data.getData();
            imgProductoAdd.setImageURI(img_Url);
        }

        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if(result != null){
            if(result.getContents() == null){
                //Toast para hacer alertas
                Toast.makeText(this, "Lectora Cancelada", Toast.LENGTH_LONG).show(); //para mostrar el mensaje
            }else{
                Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                codigoBarra.setText(result.getContents());
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    /*
    private File crearImagen() throws IOException {
        String nombreImagen = "foto_";
        File directorio = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imagen = File.createTempFile(nombreImagen, "jpg", directorio);
        rutaImagen = imagen.getAbsolutePath();
        return imagen;
    }

     */

    /*
    private void subirImagen(Uri img_Url){

        refer.putFile(img_Url).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while(!uriTask.isSuccessful()){
                    if(uriTask.isSuccessful()){
                        uriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                url_DescargaImage = uri.toString();
                            }
                        });
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AgregarProducto.this, "Error al cargar la foto", Toast.LENGTH_SHORT).show();
            }
        });
    }

     */

    //- - - - - - - - - Metodo para enviar los datos a la base de datos

    public void postCarnes(String namep, String categoriap, int cantidadp,  int preciop, String descp, String codp, Uri uriImage){
        //progressDialog.setMessage("AGREGANDO UN NUEVO PRODUCTO");
        //progressDialog.show();
        StorageReference refer = storageReference.child("productos");
        StorageReference photoReference = refer.child(photo + codigoBarra.getText() + new Date().toString());
        photoReference.putFile(uriImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while(!uriTask.isSuccessful());
                Uri dowloadUri = uriTask.getResult();

                Map<String, Object> map = new HashMap<>();
                map.put("NombreProducto", namep);
                map.put("Categoria", categoriap);
                map.put("Cantidad", cantidadp);
                map.put("Precio", preciop);
                map.put("Descripcion", descp);
                map.put("codigo", codp);
                map.put("Image", dowloadUri.toString());

                /*
                mFirestore.collection("carnes").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(AgregarProducto.this, "Datos almacenados", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AgregarProducto.this, "Error al ingresar los datos", Toast.LENGTH_SHORT).show();
                    }
                });
                */

                mFirestore.collection("carnes").document(codp)
                        .set(map)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(AgregarProducto.this, "Datos almacenados", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(AgregarProducto.this, MenuPrincipalActivity.class));
                                //progressDialog.dismiss();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AgregarProducto.this, "Error al guardar", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    public void postEnlatados(String namep, String categoriap, int cantidadp, int preciop, String descp, String codp, Uri uriImage){
        StorageReference refer = storageReference.child("productos");
        StorageReference photoReference = refer.child(photo + codigoBarra.getText() + new Date().toString());
        photoReference.putFile(uriImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while(!uriTask.isSuccessful());
                Uri dowloadUri = uriTask.getResult();

                Map<String, Object> map = new HashMap<>();
                map.put("NombreProducto", namep);
                map.put("Categoria", categoriap);
                map.put("Cantidad", cantidadp);
                map.put("Precio", preciop);
                map.put("Descripcion", descp);
                map.put("codigo", codp);
                map.put("Image", dowloadUri.toString());

                /*
                mFirestore.collection("enlatados").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(AgregarProducto.this, "Datos almacenados", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AgregarProducto.this, "Error al ingresar los datos", Toast.LENGTH_SHORT).show();
                    }
                });
                 */

                mFirestore.collection("enlatados").document(codp)
                        .set(map)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(AgregarProducto.this, "Datos almacenados", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AgregarProducto.this, "Error al guardar", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });
    }

    public void postFrutas(String namep, String categoriap, int cantidadp, int preciop, String descp, String codp, Uri uriImage){
        StorageReference refer = storageReference.child("productos");
        StorageReference photoReference = refer.child(photo + codigoBarra.getText() + new Date().toString());
        photoReference.putFile(uriImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while(!uriTask.isSuccessful());
                Uri dowloadUri = uriTask.getResult();

                Map<String, Object> map = new HashMap<>();
                map.put("NombreProducto", namep);
                map.put("Categoria", categoriap);
                map.put("Cantidad", cantidadp);
                map.put("Precio", preciop);
                map.put("Descripcion", descp);
                map.put("codigo", codp);
                map.put("Image", dowloadUri.toString());

                /*
                mFirestore.collection("frutas").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(AgregarProducto.this, "Datos almacenados", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AgregarProducto.this, "Error al ingresar los datos", Toast.LENGTH_SHORT).show();
                    }
                });
                 */

                mFirestore.collection("frutas").document(codp)
                        .set(map)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(AgregarProducto.this, "Datos almacenados", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AgregarProducto.this, "Error al guardar", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });
    }

    public void postLacteos(String namep, String categoriap, int cantidadp,  int preciop, String descp, String codp, Uri uriImage){
        StorageReference refer = storageReference.child("productos");
        StorageReference photoReference = refer.child(photo + codigoBarra.getText() + new Date().toString());
        photoReference.putFile(uriImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while(!uriTask.isSuccessful());
                Uri dowloadUri = uriTask.getResult();

                Map<String, Object> map = new HashMap<>();
                map.put("NombreProducto", namep);
                map.put("Categoria", categoriap);
                map.put("Cantidad", cantidadp);
                map.put("Precio", preciop);
                map.put("Descripcion", descp);
                map.put("codigo", codp);
                map.put("Image", dowloadUri.toString());

                /*
                mFirestore.collection("lacteos").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(AgregarProducto.this, "Datos almacenados", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AgregarProducto.this, "Error al ingresar los datos", Toast.LENGTH_SHORT).show();
                    }
                });

                 */

                mFirestore.collection("lacteos").document(codp)
                        .set(map)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(AgregarProducto.this, "Datos almacenados", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AgregarProducto.this, "Error al guardar", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });
    }

    public void postRefrescos(String namep, String categoriap, int cantidadp,  int preciop, String descp, String codp, Uri uriImage){
        StorageReference refer = storageReference.child("productos");
        StorageReference photoReference = refer.child(photo + codigoBarra.getText() + new Date().toString());
        photoReference.putFile(uriImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while(!uriTask.isSuccessful());
                Uri dowloadUri = uriTask.getResult();

                Map<String, Object> map = new HashMap<>();
                map.put("NombreProducto", namep);
                map.put("Categoria", categoriap);
                map.put("Cantidad", cantidadp);
                map.put("Precio", preciop);
                map.put("Descripcion", descp);
                map.put("codigo", codp);
                map.put("Image", dowloadUri.toString());

                /*
                mFirestore.collection("refrescos").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(AgregarProducto.this, "Datos almacenados", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AgregarProducto.this, "Error al ingresar los datos", Toast.LENGTH_SHORT).show();
                    }
                });
                 */

                mFirestore.collection("refrescos").document(codp)
                        .set(map)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(AgregarProducto.this, "Datos almacenados", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AgregarProducto.this, "Error al guardar", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });
    }

    public void postTodosLosProductos(String namep, String categoriap, int cantidadp, int preciop, String descp, String codp, Uri uriImage){
        StorageReference refer = storageReference.child("productos");
        StorageReference photoReference = refer.child(photo + codigoBarra.getText() + new Date().toString());
        photoReference.putFile(uriImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while(!uriTask.isSuccessful());
                Uri dowloadUri = uriTask.getResult();

                Map<String, Object> map = new HashMap<>();
                map.put("NombreProducto", namep);
                map.put("Categoria", categoriap);
                map.put("Cantidad", cantidadp);
                map.put("Precio", preciop);
                map.put("Descripcion", descp);
                map.put("codigo", codp);
                map.put("Image", dowloadUri.toString());

                /*
                mFirestore.collection("all_productos").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(AgregarProducto.this, "Datos almacenados", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AgregarProducto.this, "Error al ingresar los datos", Toast.LENGTH_SHORT).show();
                    }
                });

                 */

                mFirestore.collection("all_productos").document(codp)
                        .set(map)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(AgregarProducto.this, "Datos almacenados", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AgregarProducto.this, "Error al guardar", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });
    }

}