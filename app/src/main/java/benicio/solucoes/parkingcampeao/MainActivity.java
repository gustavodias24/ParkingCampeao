package benicio.solucoes.parkingcampeao;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import benicio.solucoes.parkingcampeao.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mainBinding;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());

        sharedPreferences = getSharedPreferences("prefs_empresa", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        mainBinding.operador.setText(sharedPreferences.getString("operador", ""));

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        mainBinding.btnRegistros.setOnClickListener(v -> startActivity(
                new Intent(this, ListVeiculosActivity.class)));


        mainBinding.btnEntrarOperador.setOnClickListener(v -> {
            editor.putString("operador", mainBinding.operador.getText().toString()).apply();
            startActivity(new Intent(this, EscolherModeloActivity.class));
        });

        mainBinding.btnEntrarAdm.setOnClickListener(v -> {
            String login, senha;
            login = mainBinding.inputUsuario.getText().toString();
            senha = mainBinding.inputSenha.getText().toString();

            if (login.equals("adm") && senha.equals("adm@321")) {
                mainBinding.inputUsuario.setText("");
                mainBinding.inputSenha.setText("");
                startActivity(new Intent(this, AdminActivity.class));
            }
        });

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }


        mainBinding.btnRelatorios.setOnClickListener(v -> startActivity(new Intent(this, RelatorioActivity.class)));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(this, "Permissão para usar a câmera é necessária", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

}