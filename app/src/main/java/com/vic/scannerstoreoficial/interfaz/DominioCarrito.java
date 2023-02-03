package com.vic.scannerstoreoficial.interfaz;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.vic.scannerstoreoficial.R;

import java.util.ArrayList;

public class DominioCarrito extends RecyclerView.Adapter<DominioCarrito.ViewHolder> {
   private ArrayList<carritoList> carrito;
   private Context mContext;
   private int valor = 0;

   public DominioCarrito(Context mContext, ArrayList<carritoList> carrito){
       this.mContext = mContext;
       this.carrito = carrito;
   }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.activity_viewholder_lista_compra, parent, false);
       return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
       holder.txtNombreProducto.setText("Nombre:" + carrito.get(position).getNombreProducto());
       holder.txtCodigoProducto.setText("Codigo:"+carrito.get(position).getcodigo());
       holder.txtprecioUni.setText("Precio Unitario: "+carrito.get(position).getPrecio());
       String img = carrito.get(position).getImage();
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
       holder.imgbtn_eliminar.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               eliminar(position);
           }
       });

    }

    public void eliminar(int position){
       carrito.remove(position);
       notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return carrito.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtNombreProducto, txtCodigoProducto, txtprecioUni, txtCantidad;
        ImageButton btnImage_eliminar;
        ImageView img_producto;
        ImageButton imgbtn_eliminar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_producto = itemView.findViewById(R.id.img_producto_compra);
            txtNombreProducto = itemView.findViewById(R.id.txtNameProducto_compra);
            txtCodigoProducto = itemView.findViewById(R.id.txtCodigoProducto_compra);
            txtprecioUni = itemView.findViewById(R.id.txtPrecioProducto_compra);
            imgbtn_eliminar = itemView.findViewById(R.id.imgbtn_EliminarProducto);
        }
    }

}
