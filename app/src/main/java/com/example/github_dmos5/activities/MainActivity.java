package com.example.github_dmos5.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.github_dmos5.R;
import com.example.github_dmos5.api.RetrofitService;
import com.example.github_dmos5.model.Repositorio;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_PERMISSION = 64;
    private static final String BASE_URL= "https://api.github.com";

    private Retrofit mRetrofit;

    private RecyclerView repositorios;
    private TextView semdados_textview;
    private RelativeLayout dados_relativeLayout;
    private RepositorioAdapter madapter;

    private List<Repositorio> mRepositorioList;


    private EditText user_editText;
    private Button search_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRepositorioList = new ArrayList<>();

        getLayoutElements();
    }

    private void getLayoutElements() {
        user_editText = findViewById(R.id.editText_user);
        search_button = findViewById(R.id.button_search);
        dados_relativeLayout = findViewById(R.id.relativeLayour_dados);
        repositorios = findViewById(R.id.recycler_view);
        semdados_textview = findViewById(R.id.text_semdados);

        search_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == search_button){
            if(temPermissao()){
                buscaRepositorios();
            } else {
                solicitaPermissao();
            }
        }
    }



    private boolean temPermissao() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED;
    }

    private void solicitaPermissao() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.INTERNET)){
            final Activity activity = this;
            new AlertDialog.Builder(this)
                    .setMessage(R.string.explicacao_permissao)
                    .setPositiveButton(R.string.botao_fornecer, new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i){
                            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.INTERNET}, REQUEST_PERMISSION);
                        }
                    })
                    .setNegativeButton(R.string.botao_nao_fornecer, new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i){
                            dialogInterface.dismiss();
                        }
                    })
            .show();
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.INTERNET
                    },
                    REQUEST_PERMISSION);
        }
    }


    private void buscaRepositorios() {
        mRetrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()).build();

        String userString = user_editText.getText().toString();
        if(userString.isEmpty()){
            Toast.makeText(this, getString(R.string.user_invalido), Toast.LENGTH_SHORT).show();
        } else {

            RetrofitService mRetrofitService = mRetrofit.create(RetrofitService.class);

            Call<List<Repositorio>> call = mRetrofitService.getDados(userString);

            call.enqueue(new Callback<List<Repositorio>>() {
                @Override
                public void onResponse(Call<List<Repositorio>> call, Response<List<Repositorio>> response) {
                    if (response.isSuccessful()) {
                        mRepositorioList = response.body();
                        if(mRepositorioList.isEmpty()){
                            mRepositorioList = null;
                        }
                    } else {
                        Toast.makeText(MainActivity.this, getString(R.string.erro_user), Toast.LENGTH_SHORT).show();
                        mRepositorioList = null;
                    }
                    updateUI();
                }

                @Override
                public void onFailure(Call<List<Repositorio>> call, Throwable t) {
                    Toast.makeText(MainActivity.this, getString(R.string.erro_api), Toast.LENGTH_SHORT).show();
                }
            });


        }

    }

    private void updateUI(){
        if(mRepositorioList != null){
            if(semdados_textview.getVisibility() == View.VISIBLE){
                semdados_textview.setVisibility(View.INVISIBLE);
            }
            madapter = new RepositorioAdapter(mRepositorioList);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            repositorios.setLayoutManager(layoutManager);
            repositorios.setAdapter(madapter);
            dados_relativeLayout.setVisibility(View.VISIBLE);

        } else {
            semdados_textview.setVisibility(View.VISIBLE);
            if(dados_relativeLayout.getVisibility() == View.VISIBLE){
                dados_relativeLayout.setVisibility(View.INVISIBLE);
            }
        }
    }
}