package benicio.solucoes.parkingcampeao;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.Collections;
import java.util.List;

import benicio.solucoes.parkingcampeao.adapter.AdapterVeiculo;
import benicio.solucoes.parkingcampeao.databinding.ActivityListVeiculosBinding;
import benicio.solucoes.parkingcampeao.databinding.ActivityMainBinding;
import benicio.solucoes.parkingcampeao.model.VeiculoModel;
import benicio.solucoes.parkingcampeao.model.VeiculoUtils;

public class ListVeiculosActivity extends AppCompatActivity {

    private ActivityListVeiculosBinding mainBinding;
    private List<VeiculoModel> listaVeiculos;
    private AdapterVeiculo adapterVeiculo;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityListVeiculosBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        listaVeiculos = VeiculoUtils.returnListVeiculos(this);
        Collections.reverse(listaVeiculos);

        mainBinding.rvVeiculos.setLayoutManager(new LinearLayoutManager(this));
        mainBinding.rvVeiculos.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mainBinding.rvVeiculos.setHasFixedSize(true);
        sharedPreferences = getSharedPreferences("prefs_empresa", Context.MODE_PRIVATE);
        adapterVeiculo = new AdapterVeiculo(this, listaVeiculos, sharedPreferences);
        mainBinding.rvVeiculos.setAdapter(adapterVeiculo);

        mainBinding.btnVoltar.setOnClickListener(v -> {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        });

    }
}