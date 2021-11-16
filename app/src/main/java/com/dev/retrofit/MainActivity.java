package com.dev.retrofit;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.dev.retrofit.Interfaces.UsuariosApi;
import com.dev.retrofit.Models.Usuarios;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    int REQUEST_CODE = 200;

    ListView listapersonas;
    ArrayList<String> titulos = new ArrayList<>();
    ArrayAdapter arrayAdapter;
    EditText txtidusuario;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ObtenerUsuario();

        txtidusuario=(EditText)findViewById(R.id.TxtSearch);

        txtidusuario.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                ObtenerUsuario(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //Method get listPersonas
                verificarPermisos();

    }



    //Metodo para verficar si tengo x permisos
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void verificarPermisos() {

        int PermisoInternet = ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET);

        if (PermisoInternet == PackageManager.PERMISSION_GRANTED){

            obtenerListaPersonas();
            arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,titulos);
            listapersonas = (ListView) findViewById(R.id.listausers);
            listapersonas.setAdapter(arrayAdapter);

        }else{

            Toast.makeText(getApplicationContext(), "No tienes permiso a Internet, pone un SuperPack", Toast.LENGTH_SHORT).show();
            //Solicito el permiso a Internet

            requestPermissions(new String[]{Manifest.permission.INTERNET},REQUEST_CODE);

        }

    }

    private void requestInternetPermission()
    {
       if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.INTERNET)){
            new AlertDialog.Builder(this)
                    .setTitle("Solicitud de permiso")
                    .setMessage("Este permiso es necesario para acceder al metodo de consulta usuarios")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.INTERNET},REQUEST_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create().show();
       }else{
           ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET},REQUEST_CODE);
       }
    }
    /*
    @Override
    public void onRequesPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int [] grantResults)
        if (requestCode == REQUEST_CODE){

    } */



    private void obtenerListaPersonas()
    {
        Retrofit retrofit = new Retrofit.Builder()
        .baseUrl("https://jsonplaceholder.typicode.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build();

        //LLamada interfaces
        UsuariosApi usuariosApi = retrofit.create(UsuariosApi.class);


        Call<List<Usuarios>> calllista = usuariosApi.getUsuarios();

        calllista.enqueue(new Callback<List<Usuarios>>() {
            @Override
            public void onResponse(Call<List<Usuarios>> call, Response<List<Usuarios>> response) {

                for(Usuarios usuarios : response.body())
                {
                    Log.i(usuarios.getTitle(),"onResponse");
                    titulos.add(usuarios.getTitle());

                    arrayAdapter.notifyDataSetChanged();

                }

            }

            @Override
            public void onFailure(Call<List<Usuarios>> call, Throwable t) {
                //call.toString();
                t.getMessage();
            }
        });

    }

    private void ObtenerUsuario()
    {
        listapersonas=null;
        titulos.remove(0);
        arrayAdapter.notifyDataSetChanged();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //LLamada interfaces
        UsuariosApi usuariosApi = retrofit.create(UsuariosApi.class);


        Call<Usuarios> calllista = usuariosApi.getUsuarios(texto);

        calllista.enqueue(new Callback<Usuarios>() {
            @Override
            public void onResponse(Call<Usuarios> call, Response<Usuarios> response) {

                    Log.i(response.body().getTitle(),"onResponse");
                    titulos.add(response.body().getTitle());

                    arrayAdapter.notifyDataSetChanged();

                }


            @Override
            public void onFailure(Call<Usuarios> call, Throwable t) {
                //call.toString();
                t.getMessage();
            }
        });

    }
}