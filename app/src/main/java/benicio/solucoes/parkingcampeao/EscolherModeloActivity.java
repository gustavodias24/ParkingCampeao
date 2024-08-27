package benicio.solucoes.parkingcampeao;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

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
    private String tipo = "";


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

        mainBinding.btnProsseguir.setOnClickListener(v -> {
            if (!tipo.isEmpty()) {
                String placa = mainBinding.inputPlaca.getText().toString();
                if ( !placa.isEmpty() ){
                    Intent i = new Intent(this, RecolherDadoActivity.class);
                    i.putExtra("tipo", tipo);
                    i.putExtra("placa", placa);
                    finish();
                    startActivity(i);
                }else{
                    Toast.makeText(this, "Digite a placa do veículo", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this, "Escolha um tipo de veículo", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.moto) {
            mainBinding.veiculoSelecionado.setImageResource(R.drawable.moto);
            mainBinding.veiculoSelecionado.setVisibility(View.VISIBLE);
            tipo = "Moto";
        } else if (view.getId() == R.id.grande) {
            mainBinding.veiculoSelecionado.setImageResource(R.drawable.grande);
            mainBinding.veiculoSelecionado.setVisibility(View.VISIBLE);
            tipo = "Grande";
        } else if (view.getId() == R.id.carro) {
            mainBinding.veiculoSelecionado.setImageResource(R.drawable.carro);
            mainBinding.veiculoSelecionado.setVisibility(View.VISIBLE);
            tipo = "Carro";
        } else if (view.getId() == R.id.caminhao) {
            mainBinding.veiculoSelecionado.setImageResource(R.drawable.caminhao);
            mainBinding.veiculoSelecionado.setVisibility(View.VISIBLE);
            tipo = "Caminhão";
        } else if (view.getId() == R.id.carreta) {
            mainBinding.veiculoSelecionado.setVisibility(View.VISIBLE);
            tipo = "Carreta";
            mainBinding.veiculoSelecionado.setImageResource(R.drawable.carreta);
        } else if (view.getId() == R.id.outros) {
            mainBinding.veiculoSelecionado.setVisibility(View.VISIBLE);
            tipo = "Outros";
            mainBinding.veiculoSelecionado.setImageResource(R.drawable.outros);
        }

    }
}