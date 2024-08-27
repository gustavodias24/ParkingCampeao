package benicio.solucoes.parkingcampeao;

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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import benicio.solucoes.parkingcampeao.adapter.AdapterImagens;
import benicio.solucoes.parkingcampeao.adapter.AdapterVeiculo;
import benicio.solucoes.parkingcampeao.databinding.ActivityMainBinding;
import benicio.solucoes.parkingcampeao.databinding.ActivityVerImagensBinding;

public class VerImagensActivity extends AppCompatActivity {

    private ActivityVerImagensBinding mainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityVerImagensBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );


        Bundle b = getIntent().getExtras();
        mainBinding.rvImagens.setLayoutManager(new LinearLayoutManager(this));
        mainBinding.rvImagens.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mainBinding.rvImagens.setHasFixedSize(true);
        List<String> imagens = new Gson().fromJson(
                b.getString("imagens", ""),
                new TypeToken<List<String>>() {
                }.getType()
        );
        AdapterImagens adapterImagens = new AdapterImagens(imagens);
        mainBinding.rvImagens.setAdapter(adapterImagens);


    }
}