package com.vic.scannerstoreoficial.interfaz;

public class carritoList {
    private String Image;
    private String NombreProducto;
    private String categoria;
    private int cantidad;
    private int Precio;
    private String Descripcion;
    private String codigo;

    public carritoList(){}

    public carritoList(String NombreProducto, String categoria, int cantidad, int Precio, String descripcion, String codigo, String Image) {
        this.NombreProducto = NombreProducto;
        this.categoria = categoria;
        this.cantidad = cantidad;
        this.Precio = Precio;
        this.Descripcion = descripcion;
        this.codigo = codigo;
        this.Image = Image;
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
        return Precio;
    }

    public void setPrecio(int Precio) {
        this.Precio = Precio;
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
