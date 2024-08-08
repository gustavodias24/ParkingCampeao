package benicio.solucoes.parkingcampeao;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import benicio.solucoes.parkingcampeao.databinding.ActivityRecolherDadoBinding;

public class RecolherDadoActivity extends AppCompatActivity {

    private ActivityRecolherDadoBinding mainBinding;
    private Bundle b;

    private String currentPhotoPath;
    private int fotoID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityRecolherDadoBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());

        mainBinding.foto1.setOnClickListener(v -> {
            dispatchTakePictureIntent();
            fotoID = 1;
        });
        mainBinding.foto2.setOnClickListener(v -> {
            dispatchTakePictureIntent();
            fotoID = 2;
        });
        mainBinding.foto3.setOnClickListener(v -> {
            dispatchTakePictureIntent();
            fotoID = 3;
        });
        mainBinding.foto4.setOnClickListener(v -> {
            dispatchTakePictureIntent();
            fotoID = 4;
        });

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        b = getIntent().getExtras();

        switch (Objects.requireNonNull(b).getString("tipo", "")) {
            case "moto":
                mainBinding.imageTipoEscolhido.setImageResource(R.drawable.moto);
                break;
            case "grande":
                mainBinding.imageTipoEscolhido.setImageResource(R.drawable.grande);
                break;
            case "carro":
                mainBinding.imageTipoEscolhido.setImageResource(R.drawable.carro);
                break;
            case "caminhao":
                mainBinding.imageTipoEscolhido.setImageResource(R.drawable.caminhao);
                break;
            case "carreta":
                mainBinding.imageTipoEscolhido.setImageResource(R.drawable.carreta);
                break;
            case "outros":
                mainBinding.imageTipoEscolhido.setImageResource(R.drawable.outros);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Uri imageUri = Uri.fromFile(new File(currentPhotoPath));
            switch (fotoID) {
                case 1:
                    mainBinding.foto1.setImageURI(imageUri);
                    break;
                case 2:
                    mainBinding.foto2.setImageURI(imageUri);
                    break;
                case 3:
                    mainBinding.foto3.setImageURI(imageUri);
                    break;
                case 4:
                    mainBinding.foto4.setImageURI(imageUri);
                    break;
            }
        }
    }

    private File createImageFile() throws IOException {
        // Cria um nome de arquivo único
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefixo */
                ".jpg",         /* sufixo */
                storageDir      /* diretório */
        );

        // Salva o caminho do arquivo para usá-lo posteriormente
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(this, "Erro ao criar arquivo de imagem", Toast.LENGTH_SHORT).show();
            }

            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "benicio.solucoes.parkingcampeao.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, 1);
            }
        }
    }

}