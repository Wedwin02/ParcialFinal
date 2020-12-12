package DB;

import java.util.ArrayList;

import Clases.DescripcionParcial;
import Clases.informacion;

public class Respuesta {

    private ArrayList<DescripcionParcial> Datos;

    public ArrayList<DescripcionParcial> getData(){
        return Datos;
    }

    public void setData(ArrayList<DescripcionParcial> data) {
        this.Datos = data;
    }
}
