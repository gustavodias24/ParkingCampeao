package benicio.solucoes.parkingcampeao;

import android.content.Context;
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
            editor.putString("horasCobrarMenos", mainBinding.horasCobrarMenos.getText().toString()).apply();
            editor.putString("valorhoraDiaria", mainBinding.valorhoraDiaria.getText().toString()).apply();
            editor.putString("valorhoraMensalidade", mainBinding.valorhoraMensalidade.getText().toString()).apply();
            editor.putString("valorhoraMoto", mainBinding.valorhoraMoto.getText().toString()).apply();
            editor.putString("valorhoraGrande", mainBinding.valorhoraGrande.getText().toString()).apply();
            editor.putString("valorhoraCarro", mainBinding.valorhoraCarro.getText().toString()).apply();
            editor.putString("valorhoraCaminhao", mainBinding.valorhoraCaminhao.getText().toString()).apply();
            editor.putString("valorhoraCarreta", mainBinding.valorhoraCarreta.getText().toString()).apply();
            editor.putString("valorhoraOutros", mainBinding.valorhoraOutros.getText().toString()).apply();
            editor.putString("tolerancia", mainBinding.valorTolerancia.getText().toString()).apply();


            Toast.makeText(this, "Salvo com sucesso!", Toast.LENGTH_SHORT).show();
        });

    }

    private void configurarValores() {
        mainBinding.nome.setText(sharedPreferences.getString("nome", ""));
        mainBinding.endereco.setText(sharedPreferences.getString("endereco", ""));
        mainBinding.cnpj.setText(sharedPreferences.getString("cnpj", ""));
        mainBinding.telefone.setText(sharedPreferences.getString("telefone", ""));
        mainBinding.horasCobrarMenos.setText(sharedPreferences.getString("horasCobrarMenos", ""));
        mainBinding.valorhoraDiaria.setText(sharedPreferences.getString("valorhoraDiaria", ""));
        mainBinding.valorhoraMensalidade.setText(sharedPreferences.getString("valorhoraMensalidade", ""));
        mainBinding.valorhoraMoto.setText(sharedPreferences.getString("valorhoraMoto", ""));
        mainBinding.valorhoraGrande.setText(sharedPreferences.getString("valorhoraGrande", ""));
        mainBinding.valorhoraCarro.setText(sharedPreferences.getString("valorhoraCarro", ""));
        mainBinding.valorhoraCaminhao.setText(sharedPreferences.getString("valorhoraCaminhao", ""));
        mainBinding.valorhoraCarreta.setText(sharedPreferences.getString("valorhoraCarreta", ""));
        mainBinding.valorhoraOutros.setText(sharedPreferences.getString("valorhoraOutros", ""));
        mainBinding.valorTolerancia.setText(sharedPreferences.getString("tolerancia", ""));
    }
}