package benicio.solucoes.parkingcampeao;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import benicio.solucoes.parkingcampeao.databinding.ActivityEscolherModeloBinding;
import benicio.solucoes.parkingcampeao.databinding.ActivityMainBinding;
import benicio.solucoes.parkingcampeao.model.VeiculoModel;
import benicio.solucoes.parkingcampeao.model.VeiculoUtils;

public class EscolherModeloActivity extends AppCompatActivity implements View

        .OnClickListener {

    private VeiculoModel veiculoSelecionadoSaida = null;

    private static final int REQUEST_ENABLE_BT = 100;
    private BluetoothDevice printerBluetooth;
    private ActivityEscolherModeloBinding mainBinding;
    private String tipo = "";
    List<VeiculoModel> listaExistente;

    private Dialog dialogPlaca;


    @RequiresApi(api = Build.VERSION_CODES.O)
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

        mainBinding.inputPlaca.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        mainBinding.moto.setOnClickListener(this);
        mainBinding.grande.setOnClickListener(this);
        mainBinding.carro.setOnClickListener(this);
        mainBinding.caminhao.setOnClickListener(this);
        mainBinding.carreta.setOnClickListener(this);

        mainBinding.btnProsseguir.setOnClickListener(v -> {
            String placa = mainBinding.inputPlaca.getText().toString();
            boolean prosseguir = true;

            if (!placa.isEmpty()) {
                Intent i = new Intent(this, RecolherDadoActivity.class);

                for (int index = 0; index < listaExistente.size(); index++) {

                    VeiculoModel veiculoPlaca = listaExistente.get(index);

                    if (veiculoPlaca.getPlaca().equals(placa) && veiculoPlaca.getStatus().equals("Pendente")) {
                        veiculoSelecionadoSaida = veiculoPlaca;
                        prosseguir = false;

                        AlertDialog.Builder b = new AlertDialog.Builder(EscolherModeloActivity.this);
                        b.setTitle("Veículo Identificado");
                        b.setMessage("Veículo da placa " + placa + " Já foi cadastrado no sistema, deseja concluir e imprimir a via?");
                        b.setPositiveButton("Sim", (dialogInterface, i1) -> {
                            try {
                                imprimir((Button) v);
                                mainBinding.inputPlaca.setText("");
                                dialogPlaca.dismiss();
                            } catch (Exception e) {
                                Toast.makeText(this, "Erro ao executar impressao\n\n" + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                        b.setNegativeButton("Não", null);
                        dialogPlaca = b.create();
                        dialogPlaca.show();
                    }

                }
                if (prosseguir) {
                    if (!tipo.isEmpty()) {
                        i.putExtra("tipo", tipo);
                        i.putExtra("placa", placa);
                        mainBinding.inputPlaca.setText("");
                        startActivity(i);
                    } else {
                        Toast.makeText(this, "Escolha um tipo de veículo", Toast.LENGTH_SHORT).show();
                    }
                }

            } else {
                Toast.makeText(this, "Digite a placa do veículo", Toast.LENGTH_SHORT).show();
            }


        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint({"MissingPermission", "SimpleDateFormat"})
    private void imprimir(Button view) throws Exception {
        if (printerBluetooth == null)
            acharPrinterBluetooth();
        if (printerBluetooth == null)
            return;

        BluetoothSocket impressora = printerBluetooth.createInsecureRfcommSocketToServiceRecord(UUID.randomUUID());

        impressora.connect();


        veiculoSelecionadoSaida.setStatus("Concluído");
        @SuppressLint("SimpleDateFormat") String dataConclusao = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
        veiculoSelecionadoSaida.setDataSaida(dataConclusao);

        Log.d("mayara", veiculoSelecionadoSaida.toString());


        SharedPreferences sharedPreferences = getSharedPreferences("prefs_empresa", Context.MODE_PRIVATE);
        String tempoEPreco = VeiculoModel.calcularTempoEPreco(veiculoSelecionadoSaida.getDataEntrada(), dataConclusao, sharedPreferences, veiculoSelecionadoSaida);

        veiculoSelecionadoSaida.setValorTempoPago(tempoEPreco);

        try {
            view.setEnabled(false);
            VeiculoUtils.saveListVeiculos(this, listaExistente);
            VeiculoModel.imprimirSaida(impressora.getOutputStream(), this, sharedPreferences, veiculoSelecionadoSaida);
            Toast.makeText(this, "Concluído " + dataConclusao, Toast.LENGTH_SHORT).show();
        } finally {
            view.setEnabled(true);
            impressora.close();
        }
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
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        listaExistente = VeiculoUtils.returnListVeiculos(this);
    }
}