package com.uso.examhn17_i04_001;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import Clases.DescripcionParcial;
import Clases.InformacionService;
import Clases.informacion;
import DB.Respuesta;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {


    private Retrofit retrofit;
    public ListView myList;
    public EditText numeroA;
    public EditText numeroB;
    public EditText numeroC;
    public Button calcular;
    public TextView r, signomastxt,signomenostxt;
    private static int contProceso = 0;
    private ProgressBar progresbar;
    private Handler manejadorProcesos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.myList = findViewById(R.id.MyList);
        this.numeroA = findViewById(R.id.NumeroA);
        numeroA.setEnabled(false);
        this.numeroB = findViewById(R.id.NumeroB);
        numeroB.setEnabled(false);
        this.numeroC = findViewById(R.id.NumeroC);
        numeroC.setEnabled(false);
        this.r = findViewById(R.id.RespuestaFianl);
        this.signomastxt = findViewById(R.id.signoMas);
        this.signomenostxt = findViewById(R.id.signoMenos);
        this.manejadorProcesos = new Handler();
        this.progresbar = findViewById(R.id.progressBar);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        this.retrofit = new Retrofit.Builder().baseUrl("https://em012020.000webhostapp.com/index.php/EFinal/").addConverterFactory(GsonConverterFactory.create()).build();

        cargarInformacion();


        this.calcular = findViewById(R.id.BtnCalcular);

        this.calcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!InfoIsValid()){
                    Toast.makeText(MainActivity.this,"Campos requeridos",Toast.LENGTH_SHORT).show();
                }else{
                    new Thread(new ProcesoSecundario()).start();
                }
            }
        });
    }


    private void cargarInformacion() {

        InformacionService service = retrofit.create(InformacionService.class);
        Call<Respuesta> respuesta = service.getInformacion();


        respuesta.enqueue(new Callback<Respuesta>() {
            @Override
            public void onResponse(Call<Respuesta> call, Response<Respuesta> response) {

                //validamos respuesta

                if(response.isSuccessful()){
                    Respuesta respuestaOptenida =  response.body();
                    ArrayList<DescripcionParcial> listaDescripcion = respuestaOptenida.getData();
                    Listar(listaDescripcion);

                }else{
                    Toast.makeText(MainActivity.this,"No fue posible ver la información, porfavor vuelve a entrar.",Toast.LENGTH_LONG).show();
                    Log.d("Eror",response.message());
                }
            }

            @Override
            public void onFailure(Call<Respuesta> call, Throwable t) {
                Toast.makeText(MainActivity.this,"Error al obtener la informacion",Toast.LENGTH_LONG).show();
                Log.d("Eror",t.getMessage());
            }
        });

    }

    private void Listar(ArrayList<DescripcionParcial> info){

        List<String> RespuestaToString = new ArrayList<>();

        for (DescripcionParcial in : info){
            RespuestaToString.add("Descripción: "+in.getDescripcion() +" Datos: "+ in.getDatos());

            String spl = in.getDatos();
            String[] partes= spl.split(",");
            String ap = partes[0];
            String bp = partes[1];
            String cp = partes[2];
            numeroA.setText(ap);
            numeroB.setText(bp);
            numeroC.setText(cp);
        }
        this.myList.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,RespuestaToString));

    }

    private void Calcular(){

         int a = Integer.valueOf(this.numeroA.getText().toString());
         int b = Integer.valueOf(this.numeroB.getText().toString());
         int c =  Integer.valueOf(this.numeroC.getText().toString());


         double raiz = 0;
         double signoMas,signoMenos, divMas,divMenos, div = 0;
        DecimalFormat df = new DecimalFormat("#0.00");

         raiz = Math.pow(b,2) - 4*a*c;

         if(raiz<0){
             Toast.makeText(MainActivity.this,"La solución no es real",Toast.LENGTH_LONG).show();
         }else {
             div = 2*a;
             signoMas = -b + Math.sqrt(raiz);
             divMas = signoMas/div;
             signoMenos =  -b - Math.sqrt(raiz);
             divMenos = signoMenos/div;
             r.setText("La solucion es: ");
             this.signomastxt.setText("Cuando es signo mas: "+df.format(divMas));
             this.signomenostxt.setText("Cuando es signo menos: "+df.format(divMenos));
         }
    }



    final class ProcesoSecundario implements Runnable{
        @Override
        public void run() {
            while (contProceso < 100){
                metodoEspera();
                manejadorProcesos.post(new Runnable() {
                    @Override
                    public void run() {
                        progresbar.setProgress(contProceso);
                        //Validar si el proceso ya termino
                        if(contProceso == 100){
                            Calcular();
                            calcular.setEnabled(true);
                            contProceso = 0;
                        }
                    }
                });
            }
        }

        private void metodoEspera(){
            try {
                Thread.sleep(90);
                contProceso++;
                signomastxt.setText("");
                signomenostxt.setText("");
                r.setText("Esperando respuesta...");
                calcular.setEnabled(false);

            }catch(Exception e){

            }
        }

    }
    private boolean InfoIsValid(){
        if(this.numeroA.getText().toString().trim().length() <= 1 &&
                this.numeroB.getText().toString().trim().length() <= 1 &&
                this.numeroC.getText().toString().trim().length() <= 1
        ){
            return false;
        }
        return true;
    }
}