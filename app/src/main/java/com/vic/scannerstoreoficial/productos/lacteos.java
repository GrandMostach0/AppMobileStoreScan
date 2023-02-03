package com.vic.scannerstoreoficial.productos;

public class lacteos {
    private String Image;
    private String NombreProducto;
    private String categoria;
    private int cantidad;
    private int precio;
    private String Descripcion;
    private String codigo;

    public lacteos(){}

    public lacteos(String NombreProducto, String categoria,int cantidad, int precio, String descripcion, String codigo, String Image) {
        this.Image = Image;
        this.NombreProducto = NombreProducto;
        this.categoria = categoria;
        this.cantidad = cantidad;
        this.precio = precio;
        this.Descripcion = descripcion;
        this.codigo = codigo;
    }

    public String getNombreProducto() {
        return NombreProducto;
    }

    public void setNombreProducto(String NombreProducto) {
        this.NombreProducto = NombreProducto;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public String getcodigo() {
        return codigo;
    }

    public void setcodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
