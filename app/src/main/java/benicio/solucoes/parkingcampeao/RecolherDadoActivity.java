package benicio.solucoes.parkingcampeao;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import java.nio.charset.StandardCharsets;

import benicio.solucoes.parkingcampeao.databinding.ActivityRecolherDadoBinding;
import benicio.solucoes.parkingcampeao.model.VeiculoModel;
import benicio.solucoes.parkingcampeao.model.VeiculoUtils;

public class RecolherDadoActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    private static final int REQUEST_ENABLE_BT = 100;
    private BluetoothDevice printerBluetooth;

    private ActivityRecolherDadoBinding mainBinding;
    private Bundle b;

    private String currentPhotoPath;
    private int fotoID;

    private String tipo, placa;

    private VeiculoModel veiculoModel = new VeiculoModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityRecolherDadoBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());

        sharedPreferences = getSharedPreferences("prefs_empresa", Context.MODE_PRIVATE);

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
        tipo = Objects.requireNonNull(b).getString("tipo", "");
        placa = Objects.requireNonNull(b).getString("placa", "");
        veiculoModel.setTipo(tipo);
        veiculoModel.setPlaca(placa);


        mainBinding.btnImprimir.setOnClickListener(v -> {
            try {
                imprimir((ImageButton) v);
            } catch (Exception e) {
                Toast.makeText(this, "Erro ao executar impressao\n\n" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.i("Request", new StringBuilder().append("Request Result, permissions: ")
                .append(permissions[0])
                .append(" grantResults: ")
                .append(grantResults[0]).toString());
    }

    @SuppressLint("MissingPermission")
    private void acharPrinterBluetooth() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth não foi encontrado ou não disponível neste equipamento.", Toast.LENGTH_SHORT).show();
            // Device doesn't support Bluetooth
            return;
        }

        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();
        for (BluetoothDevice bondedDevice : bondedDevices) {
            if (bondedDevice.getName().toLowerCase().contains("print")) {
                printerBluetooth = bondedDevice;
                break;
            }
        }
    }

    @SuppressLint({"MissingPermission", "SimpleDateFormat"})
    private void imprimir(ImageButton view) throws Exception {
        if (printerBluetooth == null)
            acharPrinterBluetooth();
        if (printerBluetooth == null)
            return;

        BluetoothSocket impressora = printerBluetooth.createInsecureRfcommSocketToServiceRecord(UUID.randomUUID());

        impressora.connect();
        try {
            view.setEnabled(false);
            view.setImageResource(R.drawable.imprimindo);

            veiculoModel.setDataEntrada(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()));
            veiculoModel.setOperador(sharedPreferences.getString("operador", ""));

            VeiculoModel.imprimirEntrada(impressora.getOutputStream(), this, sharedPreferences, veiculoModel);

            List<VeiculoModel> listaVeiculos = VeiculoUtils.returnListVeiculos(this);
            listaVeiculos.add(veiculoModel);
            VeiculoUtils.saveListVeiculos(this, listaVeiculos);
            Toast.makeText(this, "Veículo cadastrado", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(this, ListVeiculosActivity.class));
        } finally {
            view.setEnabled(true);
            impressora.close();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Uri imageUri = Uri.fromFile(new File(currentPhotoPath));

            veiculoModel.getListaFotos().add(imageUri.toString());

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