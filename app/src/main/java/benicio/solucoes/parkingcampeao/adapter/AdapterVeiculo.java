package benicio.solucoes.parkingcampeao.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import benicio.solucoes.parkingcampeao.ListVeiculosActivity;
import benicio.solucoes.parkingcampeao.R;
import benicio.solucoes.parkingcampeao.VerImagensActivity;
import benicio.solucoes.parkingcampeao.model.VeiculoModel;
import benicio.solucoes.parkingcampeao.model.VeiculoUtils;

public class AdapterVeiculo extends RecyclerView.Adapter<AdapterVeiculo.MyViewHolder> {

    private static final int REQUEST_ENABLE_BT = 100;
    private BluetoothDevice printerBluetooth;

    SharedPreferences sharedPreferences;
    Activity c;
    List<VeiculoModel> lista;

    public AdapterVeiculo(Activity c, List<VeiculoModel> lista, SharedPreferences sharedPreferences) {
        this.c = c;
        this.lista = lista;
        this.sharedPreferences = sharedPreferences;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_veiculo, parent, false));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        VeiculoModel veiculoModel = lista.get(position);

        holder.textDetalhes.setText(veiculoModel.toString());

        if (!veiculoModel.getStatus().equals("Pendente")) {
            holder.btnOk.setVisibility(View.GONE);
        }

        holder.btnVer.setOnClickListener(v -> {
            Intent i = new Intent(c, VerImagensActivity.class);
            String imagens = new Gson().toJson(veiculoModel.getListaFotos());
            i.putExtra("imagens", imagens);
            c.startActivity(
                    i
            );
        });

        holder.btnOk.setOnClickListener(v -> {

            @SuppressLint("SimpleDateFormat") String dataConclusao = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());

            String tempoEPreco = VeiculoModel.calcularTempoEPreco(veiculoModel.getDataEntrada(), dataConclusao, sharedPreferences);

            veiculoModel.setStatus("Concluído");
            veiculoModel.setDataSaida(dataConclusao);
            veiculoModel.setValorTempoPago(tempoEPreco);

            List<VeiculoModel> listaExistente = VeiculoUtils.returnListVeiculos(c);

            listaExistente.get(position).setStatus("Concluído");
            listaExistente.get(position).setDataSaida(dataConclusao);
            listaExistente.get(position).setValorTempoPago(tempoEPreco);

            VeiculoUtils.saveListVeiculos(c, listaExistente);
            Toast.makeText(c, "Concluído " + dataConclusao, Toast.LENGTH_SHORT).show();
            this.notifyDataSetChanged();
        });

        holder.btnImprimir.setOnClickListener(v -> {

            try {
                imprimir((Button) v, veiculoModel);
            } catch (Exception e) {
                Toast.makeText(c, "Erro ao executar impressao\n\n" + e.getMessage(), Toast.LENGTH_LONG).show();
            }


        });

    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public final class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textDetalhes;
        Button btnOk, btnImprimir, btnVer;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            btnOk = itemView.findViewById(R.id.concluirVeiculo);
            btnImprimir = itemView.findViewById(R.id.imprimirVeiculo);
            btnVer = itemView.findViewById(R.id.verVeiculo);


            textDetalhes = itemView.findViewById(R.id.detalhesVeiculo);
        }
    }

    @SuppressLint({"MissingPermission", "SimpleDateFormat"})
    private void imprimir(Button view, VeiculoModel veiculoModel) throws Exception {
        if (printerBluetooth == null)
            acharPrinterBluetooth();
        if (printerBluetooth == null)
            return;

        BluetoothSocket impressora = printerBluetooth.createInsecureRfcommSocketToServiceRecord(UUID.randomUUID());

        impressora.connect();
        try {
            view.setEnabled(false);

            if (veiculoModel.getStatus().equals("Pendente")) {
                VeiculoModel.imprimirEntrada(impressora.getOutputStream(), c, sharedPreferences, veiculoModel);
            } else {
                VeiculoModel.imprimirSaida(impressora.getOutputStream(), c, sharedPreferences, veiculoModel);
            }

        } finally {
            view.setEnabled(true);
            impressora.close();
        }
    }

    @SuppressLint("MissingPermission")
    private void acharPrinterBluetooth() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null) {
            Toast.makeText(c, "Bluetooth não foi encontrado ou não disponível neste equipamento.", Toast.LENGTH_SHORT).show();
            // Device doesn't support Bluetooth
            return;
        }

        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            c.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();
        for (BluetoothDevice bondedDevice : bondedDevices) {
            if (bondedDevice.getName().toLowerCase().contains("print")) {
                printerBluetooth = bondedDevice;
                break;
            }
        }
    }
}
