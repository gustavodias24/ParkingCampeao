package benicio.solucoes.parkingcampeao;

import android.os.Bundle;
import android.view.WindowManager;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Collections;
import java.util.List;

import benicio.solucoes.parkingcampeao.databinding.ActivityListVeiculosBinding;
import benicio.solucoes.parkingcampeao.databinding.ActivityMainBinding;
import benicio.solucoes.parkingcampeao.model.VeiculoModel;
import benicio.solucoes.parkingcampeao.model.VeiculoUtils;

public class ListVeiculosActivity extends AppCompatActivity {

    private ActivityListVeiculosBinding mainBinding;
    private List<VeiculoModel> listaVeiculos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityListVeiculosBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        listaVeiculos = VeiculoUtils.returnListVeiculos(this);
        Collections.reverse(listaVeiculos);


    }
}