package com.evandro.mykeyboard;

import android.os.AsyncTask;
import android.util.Log;
import android.view.textclassifier.TextLinks;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SegundoPlano extends AsyncTask<Void, Void, Void> {
    private final String texto;

    public SegundoPlano(String texto) {
        this.texto = texto;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Log.d("Recebimento de texto", texto);
        String email = "primeiro.email@mail.com";
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody formbody = new FormBody.Builder().add("text", texto).add("email", email).build();
        Request request = new Request.Builder().url("http://192.168.18.97:5000/salvar").post(formbody).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                //Toast.makeText(SegundoPlano.this, "network not found", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

            }
        });
        return null;
    }

}
