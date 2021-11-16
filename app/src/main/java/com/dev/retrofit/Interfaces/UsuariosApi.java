package com.dev.retrofit.Interfaces;

import com.dev.retrofit.Models.Usuarios;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface UsuariosApi
{
    String Ruta0 = "/posts";
    String Ruta1 = "/posts{valor}";
    @GET(Ruta0)
    Call<List<Usuarios>> getUsuarios();

    @GET("Ruta1/{id}")
    Call<Usuarios> getUsuarios (@Path("valor") String valor);

}
