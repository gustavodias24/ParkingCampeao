package benicio.solucoes.parkingcampeao;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import benicio.solucoes.parkingcampeao.databinding.ActivityMainBinding;
import benicio.solucoes.parkingcampeao.databinding.ActivityRelatorioBinding;
import benicio.solucoes.parkingcampeao.model.VeiculoModel;
import benicio.solucoes.parkingcampeao.model.VeiculoUtils;

public class RelatorioActivity extends AppCompatActivity {

    private ActivityRelatorioBinding mainBinding;
    DateTimeFormatter formatter;

    Calendar calendar;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityRelatorioBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());

        calendar = Calendar.getInstance();

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        mainBinding.inputDataInicial.setOnClickListener(v -> {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(RelatorioActivity.this,
                    (view, year1, monthOfYear, dayOfMonth) -> {
                        calendar.set(Calendar.YEAR, year1);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        // Formatar a data escolhida
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        mainBinding.inputDataInicial.setText(sdf.format(calendar.getTime()));
                    }, year, month, day);
            datePickerDialog.show();
        });

        mainBinding.inputDataFinal.setOnClickListener(v -> {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(RelatorioActivity.this,
                    (view, year1, monthOfYear, dayOfMonth) -> {
                        calendar.set(Calendar.YEAR, year1);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        // Formatar a data escolhida
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        mainBinding.inputDataFinal.setText(sdf.format(calendar.getTime()));
                    }, year, month, day);
            datePickerDialog.show();
        });

        formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        mainBinding.btnFiltrar.setOnClickListener(v -> {
            try {
                String dataInicialString = mainBinding.inputDataInicial.getText().toString();
                String dataFinalString = mainBinding.inputDataFinal.getText().toString();

                LocalDateTime dataInicial = LocalDateTime.parse(dataInicialString + " 00:00", formatter);
                LocalDateTime dataFinal = LocalDateTime.parse(dataFinalString + " 23:59", formatter);

                List<VeiculoModel> veiculosFiltrados = VeiculoUtils.returnListVeiculos(this).stream()
                        .filter(veiculo -> veiculo.getStatus().equals("Concluído"))
                        .filter(veiculo -> {
                            LocalDateTime dataEntrada = LocalDateTime.parse(veiculo.getDataEntrada(), formatter);
                            return (dataEntrada.isEqual(dataInicial) || dataEntrada.isAfter(dataInicial)) &&
                                    (dataEntrada.isEqual(dataFinal) || dataEntrada.isBefore(dataFinal));
                        })
                        .collect(Collectors.toList());

                // 1. Contar quantos veículos de cada tipo entraram
                Map<String, Long> veiculosPorTipo = veiculosFiltrados.stream()
                        .collect(Collectors.groupingBy(VeiculoModel::getTipo, Collectors.counting()));

                // 2. Somar o valor pago por cada tipo de veículo
                Map<String, Double> valorPorTipo = veiculosFiltrados.stream()
                        .collect(Collectors.groupingBy(VeiculoModel::getTipo, Collectors.summingDouble(VeiculoModel::getValorPago)));

                // 3. Contar quantas vezes cada placa apareceu e quanto gastou
                Map<String, Long> placaCount = veiculosFiltrados.stream()
                        .collect(Collectors.groupingBy(VeiculoModel::getPlaca, Collectors.counting()));

                Map<String, Double> valorPorPlaca = veiculosFiltrados.stream()
                        .collect(Collectors.groupingBy(VeiculoModel::getPlaca, Collectors.summingDouble(VeiculoModel::getValorPago)));

                // 4. Contar quantas vezes foi "diaria", "mensalidade" ou "cobrarMenos" e somar os valores
                long diariaCount = veiculosFiltrados.stream().filter(VeiculoModel::isDiaria).count();
                float diariaValor = veiculosFiltrados.stream().filter(VeiculoModel::isDiaria).map(VeiculoModel::getValorPago).reduce(0f, Float::sum);

                long mensalidadeCount = veiculosFiltrados.stream().filter(VeiculoModel::isMensalidade).count();
                float mensalidadeValor = veiculosFiltrados.stream().filter(VeiculoModel::isMensalidade).map(VeiculoModel::getValorPago).reduce(0f, Float::sum);

                long cobrarMenosCount = veiculosFiltrados.stream().filter(VeiculoModel::isCobrarMenos).count();
                float cobrarMenosValor = veiculosFiltrados.stream().filter(VeiculoModel::isCobrarMenos).map(VeiculoModel::getValorPago).reduce(0f, Float::sum);

                StringBuilder infoBuilder = new StringBuilder();

                infoBuilder.append("<b>Veículos por tipo:</b>").append("<br>");
                veiculosPorTipo.forEach((tipo, count) -> infoBuilder.append(tipo).append(": ").append(count).append("<br>"));

                infoBuilder.append("<br><b>Valor gasto por tipo de veículo:</b>").append("<br>");
                valorPorTipo.forEach((tipo, valor) -> infoBuilder.append(tipo).append(": R$ ").append(valor).append("<br>"));

                infoBuilder.append("<br><b>Contagem e valor por placa:</b>").append("<br>");
                placaCount.forEach((placa, count) -> infoBuilder.append(placa).append(": ").append(count)
                        .append(" vezes, valor total: R$ ").append(valorPorPlaca.get(placa)).append("<br>"));

                infoBuilder.append("<br><b>Diarias:</b> ").append(diariaCount).append(", valor total: R$ ").append(diariaValor).append("<br>");
                infoBuilder.append("<b>Mensalidades:</b> ").append(mensalidadeCount).append(", valor total: R$ ").append(mensalidadeValor).append("<br>");
                infoBuilder.append("<b>Cobrar Menos:</b> ").append(cobrarMenosCount).append(", valor total: R$ ").append(cobrarMenosValor).append("<br>");

                mainBinding.textResultado.setText(Html.fromHtml(infoBuilder.toString()));
            } catch (Exception e) {
                Toast.makeText(this, "Intervalo de Datas Inválidas", Toast.LENGTH_SHORT).show();
            }

        });

        mainBinding.btnVoltar.setOnClickListener(v -> finish());
    }
}