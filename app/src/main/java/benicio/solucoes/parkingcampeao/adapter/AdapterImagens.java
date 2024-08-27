package benicio.solucoes.parkingcampeao.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import benicio.solucoes.parkingcampeao.R;

public class AdapterImagens extends RecyclerView.Adapter<AdapterImagens.MyViewHolder> {


    List<String> imagens;

    public AdapterImagens(List<String> imagens) {
        this.imagens = imagens;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_iamgens, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.imageVeiculo.setImageURI(Uri.parse(imagens.get(position)));
    }

    @Override
    public int getItemCount() {
        return imagens.size();
    }

    public final class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageVeiculo;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageVeiculo = itemView.findViewById(R.id.imagemVeiculo);
        }
    }
}
