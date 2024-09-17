package benicio.solucoes.parkingcampeao;

import android.annotation.SuppressLint;
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

import java.util.Objects;

import benicio.solucoes.parkingcampeao.databinding.ActivityAdminBinding;
import benicio.solucoes.parkingcampeao.databinding.ActivityConfigurarValorBinding;

public class ConfigurarValorActivity extends AppCompatActivity {

    private ActivityConfigurarValorBinding mainBinding;

    String tipo;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    String valorMeiaHoraString = "valorMeiaHora";
    String valorHoraString = "valorHora";
    String valorExcedenteString = "valorExcedente";
    String valorDiarioString = "valorDiario";
    String valorMensalString = "valorMensal";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityConfigurarValorBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());

        sharedPreferences = getSharedPreferences("prefs_empresa", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        mainBinding.btnVoltar.setOnClickListener(v -> finish());

        tipo = Objects.requireNonNull(getIntent().getExtras()).getString("tipo", "");

        valorMeiaHoraString = valorMeiaHoraString + tipo;
        valorHoraString = valorHoraString + tipo;
        valorDiarioString = valorDiarioString + tipo;
        valorExcedenteString = valorExcedenteString + tipo;
        valorMensalString = valorMensalString + tipo;

        mainBinding.textAtualizando.setText("Atualizando Preço de " + tipo);

        configurarValoresInInput();

        mainBinding.btnSalvar.setOnClickListener(v -> salvarInfo());
    }

    private void salvarInfo() {
        editor.putString(valorDiarioString, Objects.requireNonNull(mainBinding.inputDiaria.getText()).toString()).apply();
        editor.putString(valorMeiaHoraString, Objects.requireNonNull(mainBinding.inputMeiaHora.getText()).toString()).apply();
        editor.putString(valorHoraString, Objects.requireNonNull(mainBinding.inputHora.getText()).toString()).apply();
        editor.putString(valorExcedenteString, Objects.requireNonNull(mainBinding.inputExcedente.getText()).toString()).apply();
        editor.putString(valorMensalString, Objects.requireNonNull(mainBinding.inputMensalista.getText()).toString()).apply();

        Toast.makeText(this, "Salvo!", Toast.LENGTH_SHORT).show();
    }

    private void configurarValoresInInput() {
        mainBinding.inputDiaria.setText(sharedPreferences.getString(valorDiarioString, ""));
        mainBinding.inputMeiaHora.setText(sharedPreferences.getString(valorMeiaHoraString, ""));
        mainBinding.inputHora.setText(sharedPreferences.getString(valorHoraString, ""));
        mainBinding.inputExcedente.setText(sharedPreferences.getString(valorExcedenteString, ""));
        mainBinding.inputMensalista.setText(sharedPreferences.getString(valorMensalString, ""));
    }
}