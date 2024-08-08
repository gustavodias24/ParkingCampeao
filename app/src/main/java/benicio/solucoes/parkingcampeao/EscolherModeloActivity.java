package benicio.solucoes.parkingcampeao;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import benicio.solucoes.parkingcampeao.databinding.ActivityEscolherModeloBinding;
import benicio.solucoes.parkingcampeao.databinding.ActivityMainBinding;

public class EscolherModeloActivity extends AppCompatActivity implements View

        .OnClickListener {

    private ActivityEscolherModeloBinding mainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityEscolherModeloBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        mainBinding.moto.setOnClickListener(this);
        mainBinding.grande.setOnClickListener(this);
        mainBinding.carro.setOnClickListener(this);
        mainBinding.caminhao.setOnClickListener(this);
        mainBinding.carreta.setOnClickListener(this);
        mainBinding.outros.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        String tipo = "";

        if (view.getId() == R.id.moto) {
            tipo = "moto";
        } else if (view.getId() == R.id.grande) {
            tipo = "grande";
        } else if (view.getId() == R.id.carro) {
            tipo = "carro";
        } else if (view.getId() == R.id.caminhao) {
            tipo = "caminhao";
        } else if (view.getId() == R.id.carreta) {
            tipo = "carreta";
        } else if (view.getId() == R.id.outros) {
            tipo = "outros";
        }

        if (!tipo.isEmpty()) {
            Intent i = new Intent(this, RecolherDadoActivity.class);
            i.putExtra("tipo", tipo);
            startActivity(i);
        }
    }
}