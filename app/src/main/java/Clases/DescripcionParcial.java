package Clases;
 import  com.google.gson.annotations.SerializedName;
public class DescripcionParcial {

    @SerializedName("Descripcion")
    private  String Descripcion;
    @SerializedName("Datos")
    private   String Datos;


    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public String getDatos() {
        return Datos;
    }

    public void setDatos(String datos) {
        Datos = datos;
    }
}
