package benicio.solucoes.parkingcampeao;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import benicio.solucoes.parkingcampeao.databinding.ActivityAdminBinding;
import benicio.solucoes.parkingcampeao.databinding.ActivityMainBinding;

public class AdminActivity extends AppCompatActivity {

    private ActivityAdminBinding mainBinding;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private Intent updateIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        sharedPreferences = getSharedPreferences("prefs_empresa", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        configurarValores();
        mainBinding.btnSalvar.setOnClickListener(v -> {
            editor.putString("nome", mainBinding.nome.getText().toString()).apply();
            editor.putString("endereco", mainBinding.endereco.getText().toString()).apply();
            editor.putString("cnpj", mainBinding.cnpj.getText().toString()).apply();
            editor.putString("telefone", mainBinding.telefone.getText().toString()).apply();
            editor.putString("tolerancia", mainBinding.valorTolerancia.getText().toString()).apply();

            Toast.makeText(this, "Salvo com sucesso!", Toast.LENGTH_SHORT).show();
        });

        updateIntent = new Intent(this, ConfigurarValorActivity.class);

        mainBinding.configMoto.setOnClickListener(v -> {
            updateIntent.putExtra("tipo", "Moto");
            startActivity(updateIntent);
        });
        mainBinding.configCaminhao.setOnClickListener(v -> {
            updateIntent.putExtra("tipo", "Caminhão");
            startActivity(updateIntent);
        });
        mainBinding.configCarreta.setOnClickListener(v -> {
            updateIntent.putExtra("tipo", "Carreta");
            startActivity(updateIntent);
        });
        mainBinding.configCarro.setOnClickListener(v -> {
            updateIntent.putExtra("tipo", "Carro");
            startActivity(updateIntent);
        });
        mainBinding.configGrande.setOnClickListener(v -> {
            updateIntent.putExtra("tipo", "Grande");
            startActivity(updateIntent);
        });

    }

    private void configurarValores() {
        mainBinding.nome.setText(sharedPreferences.getString("nome", ""));
        mainBinding.endereco.setText(sharedPreferences.getString("endereco", ""));
        mainBinding.cnpj.setText(sharedPreferences.getString("cnpj", ""));
        mainBinding.telefone.setText(sharedPreferences.getString("telefone", ""));

//        mainBinding.valorhoraMoto.setText(sharedPreferences.getString("valorhoraMoto", ""));
//        mainBinding.valorhoraGrande.setText(sharedPreferences.getString("valorhoraGrande", ""));
//        mainBinding.valorhoraCarro.setText(sharedPreferences.getString("valorhoraCarro", ""));
//        mainBinding.valorhoraCaminhao.setText(sharedPreferences.getString("valorhoraCaminhao", ""));
//        mainBinding.valorhoraCarreta.setText(sharedPreferences.getString("valorhoraCarreta", ""));

//        mainBinding.valorhoraOutros.setText(sharedPreferences.getString("valorhoraOutros", ""));

        mainBinding.valorTolerancia.setText(sharedPreferences.getString("tolerancia", ""));
    }
}