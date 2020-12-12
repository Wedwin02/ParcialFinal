package Clases;

import DB.Respuesta;
import retrofit2.Call;
import retrofit2.http.GET;

public interface InformacionService {

    @GET("Get_Parcial?codigo=HN17-I04-001")
    Call<Respuesta> getInformacion();
}
