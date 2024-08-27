package benicio.solucoes.parkingcampeao.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import benicio.solucoes.parkingcampeao.R;
import benicio.solucoes.parkingcampeao.VerImagensActivity;
import benicio.solucoes.parkingcampeao.model.VeiculoModel;
import benicio.solucoes.parkingcampeao.model.VeiculoUtils;

public class AdapterVeiculo extends RecyclerView.Adapter<AdapterVeiculo.MyViewHolder> {

    Activity c;
    List<VeiculoModel> lista;

    public AdapterVeiculo(Activity c, List<VeiculoModel> lista) {
        this.c = c;
        this.lista = lista;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_veiculo, parent, false));
    }

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

            veiculoModel.setStatus("Concluído");
            veiculoModel.setDataSaida(dataConclusao);

            List<VeiculoModel> listaExistente = VeiculoUtils.returnListVeiculos(c);
            Collections.reverse(listaExistente);
            listaExistente.get(position).setStatus("Concluído");
            listaExistente.get(position).setDataSaida(dataConclusao);
            VeiculoUtils.saveListVeiculos(c, listaExistente);
            Toast.makeText(c, "Concluído " + dataConclusao, Toast.LENGTH_SHORT).show();
            this.notifyDataSetChanged();
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
}
