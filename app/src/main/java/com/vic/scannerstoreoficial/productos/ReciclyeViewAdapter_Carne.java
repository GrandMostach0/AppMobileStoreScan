package com.vic.scannerstoreoficial.productos;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import com.vic.scannerstoreoficial.DetallesProducto;
import com.vic.scannerstoreoficial.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ReciclyeViewAdapter_Carne extends FirestoreRecyclerAdapter<carnes, ReciclyeViewAdapter_Carne.ViewHolder> {
    FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    Context mContext;

    public ReciclyeViewAdapter_Carne(Context mContext, @NonNull FirestoreRecyclerOptions<carnes> mData) {
        super(mData);
        this.mContext = mContext;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull carnes carnes) {
        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());
        final String id = documentSnapshot.getId();
        holder.txtInfoProducto.setText(carnes.getNombreProducto());
        String img = carnes.getImage();
        try{
            if(!img.equals("")){
                Picasso.with(mContext)
                        .load(img)
                        .resize(120, 125)
                        .into(holder.img_producto);
            }
        }catch (Exception e){
            Log.d("Error", "e " + e);
        }

        holder.principalLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, DetallesProducto.class);
                i.putExtra("nombre", carnes.getNombreProducto());
                i.putExtra("descripcion", carnes.getDescripcion());
                i.putExtra("precio", carnes.getPrecio());
                i.putExtra("Image", carnes.getImage());
                Toast.makeText(mContext, carnes.getNombreProducto(), Toast.LENGTH_SHORT).show();
                mContext.startActivity(i);
            }
        });

        holder.btnImage_eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteCarne(id);
            }
        });
    }

    private void deleteCarne(String id){
        mFirestore.collection("carnes").document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(mContext, "Eliminado correctamente", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(mContext, "Error al eliminar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_viewholder_lista_productos, parent, false);
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtInfoProducto;
        ImageButton btnImage_eliminar;
        ImageView img_producto;
        ConstraintLayout principalLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtInfoProducto = itemView.findViewById(R.id.txt_informacion_producto);
            principalLayout = itemView.findViewById(R.id.layoutprincipal);
            img_producto = itemView.findViewById(R.id.img_producto);
            btnImage_eliminar = itemView.findViewById(R.id.imgBtn_eliminar);
        }

    }

}
