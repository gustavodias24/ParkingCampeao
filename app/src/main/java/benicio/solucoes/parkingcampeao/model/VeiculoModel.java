package benicio.solucoes.parkingcampeao.model;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.time.Duration;
import java.time.LocalDateTime;

import benicio.solucoes.parkingcampeao.EscPosBase;
import benicio.solucoes.parkingcampeao.PrinterConverter;
import benicio.solucoes.parkingcampeao.R;

public class VeiculoModel {

    String status = "Pendente";
    String tipo, placa, dataEntrada, dataSaida, operador;

    List<String> listaFotos = new ArrayList<>();

    String valorTempoPago = "Tempo: 0 horas e 0 minutos\nValor R$ 0.0";

    public VeiculoModel() {
    }


    public static void imprimirSaida(OutputStream out, Activity a, SharedPreferences sharedPreferences, VeiculoModel v) throws IOException {
        out.write(EscPosBase.init_printer());


        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inTargetDensity = 100;
        options.inDensity = 100;
        Bitmap bitmap = BitmapFactory.decodeResource(a.getResources(), R.drawable.logoimprimir, options);
        byte[] logoImpressaobyte = PrinterConverter.bitmapToBytes(bitmap);

        out.write(EscPosBase.nextLine());
        out.write(EscPosBase.alignCenter());
        out.write(logoImpressaobyte);
        out.write(EscPosBase.alignLeft());
        out.write(EscPosBase.nextLine());
        out.write("--------------------------------".getBytes(StandardCharsets.UTF_8));

        out.write(EscPosBase.TEXT_WEIGHT_BOLD);

        out.write(EscPosBase.nextLine());
        out.write(EscPosBase.alignLeft());
        String empresaString = "Empresa: " + VeiculoModel.normalize(sharedPreferences.getString("nome", ""));
        out.write(empresaString.getBytes(StandardCharsets.UTF_8));

        out.write(EscPosBase.nextLine());
        out.write(EscPosBase.alignLeft());
        String enderecoString = "Endereco: " + VeiculoModel.normalize(sharedPreferences.getString("endereco", ""));
        out.write(enderecoString.getBytes(StandardCharsets.UTF_8));

        out.write(EscPosBase.nextLine());
        out.write(EscPosBase.alignLeft());
        String cnpjString = "CNPJ: " + VeiculoModel.normalize(sharedPreferences.getString("cnpj", ""));
        out.write(cnpjString.getBytes(StandardCharsets.UTF_8));

        out.write(EscPosBase.nextLine());
        out.write(EscPosBase.alignLeft());
        String foneString = "Fone: " + VeiculoModel.normalize(sharedPreferences.getString("telefone", ""));
        out.write(foneString.getBytes(StandardCharsets.UTF_8));

        out.write(EscPosBase.alignLeft());
        out.write(EscPosBase.nextLine());
        out.write("--------------------------------".getBytes(StandardCharsets.UTF_8));

        out.write(EscPosBase.nextLine());
        out.write(EscPosBase.alignCenter());
        out.write("SAIDA".getBytes(StandardCharsets.UTF_8));

        out.write(EscPosBase.nextLine());
        out.write(EscPosBase.alignLeft());
        String veiculoString = "Veiculo: " + v.getPlaca();
        out.write(veiculoString.getBytes(StandardCharsets.UTF_8));

        out.write(EscPosBase.nextLine());
        out.write(EscPosBase.alignLeft());
        String operadorString = "Operador: " + v.operador;
        out.write(operadorString.getBytes(StandardCharsets.UTF_8));

        out.write(EscPosBase.nextLine());
        out.write(EscPosBase.alignLeft());
        String dataEntradaString = "Data entrada: " + v.dataEntrada;
        out.write(dataEntradaString.getBytes(StandardCharsets.UTF_8));

        out.write(EscPosBase.nextLine());
        out.write(EscPosBase.alignLeft());
        String dataSaidaString = "Data saida: " + v.dataSaida;
        out.write(dataSaidaString.getBytes(StandardCharsets.UTF_8));

        out.write(EscPosBase.nextLine());
        out.write(EscPosBase.alignLeft());
        String dataPermanenciaString = v.valorTempoPago.split("\n")[0];
        out.write(dataPermanenciaString.getBytes(StandardCharsets.UTF_8));

        out.write(EscPosBase.nextLine());
        out.write(EscPosBase.alignCenter());
        out.write(EscPosBase.getFontTall());
        String valorString = VeiculoModel.normalize(v.valorTempoPago.split("\n")[1]);
        out.write(valorString.getBytes(StandardCharsets.UTF_8));

        out.flush();

        out.write(EscPosBase.nextLine());
        out.write(EscPosBase.alignCenter());
        out.write("--------------------------------".getBytes(StandardCharsets.UTF_8));
        out.write(EscPosBase.nextLine());
        out.write(EscPosBase.alignCenter());
        out.write("AGRADECEMOS SUA VISITA".getBytes(StandardCharsets.UTF_8));
        out.write(EscPosBase.nextLine());
        out.write("================================".getBytes(StandardCharsets.UTF_8));
        out.write(EscPosBase.nextLine());

        out.flush();

        out.write(EscPosBase.nextLine(3));
        out.flush();

    }

    public static void imprimirEntrada(OutputStream out, Activity a, SharedPreferences sharedPreferences, VeiculoModel v) throws IOException {
        out.write(EscPosBase.init_printer());

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inTargetDensity = 100;
        options.inDensity = 100;
        Bitmap bitmap = BitmapFactory.decodeResource(a.getResources(), R.drawable.logoimprimir, options);
        byte[] logoImpressaobyte = PrinterConverter.bitmapToBytes(bitmap);

        out.write(EscPosBase.nextLine());
        out.write(EscPosBase.alignCenter());
        out.write(logoImpressaobyte);
        out.write(EscPosBase.alignLeft());
        out.write(EscPosBase.nextLine());
        out.write("--------------------------------".getBytes(StandardCharsets.UTF_8));

        out.write(EscPosBase.TEXT_WEIGHT_BOLD);

        out.write(EscPosBase.nextLine());
        out.write(EscPosBase.alignLeft());
        String empresaString = "Empresa: " + VeiculoModel.normalize(sharedPreferences.getString("nome", ""));
        out.write(empresaString.getBytes(StandardCharsets.UTF_8));

        out.write(EscPosBase.nextLine());
        out.write(EscPosBase.alignLeft());
        String enderecoString = "Endereco: " + VeiculoModel.normalize(sharedPreferences.getString("endereco", ""));
        out.write(enderecoString.getBytes(StandardCharsets.UTF_8));

        out.write(EscPosBase.nextLine());
        out.write(EscPosBase.alignLeft());
        String cnpjString = "CNPJ: " + VeiculoModel.normalize(sharedPreferences.getString("cnpj", ""));
        out.write(cnpjString.getBytes(StandardCharsets.UTF_8));

        out.write(EscPosBase.nextLine());
        out.write(EscPosBase.alignLeft());
        String foneString = "Fone: " + VeiculoModel.normalize(sharedPreferences.getString("telefone", ""));
        out.write(foneString.getBytes(StandardCharsets.UTF_8));

        out.write(EscPosBase.alignLeft());
        out.write(EscPosBase.nextLine());
        out.write("--------------------------------".getBytes(StandardCharsets.UTF_8));

        out.write(EscPosBase.nextLine());
        out.write(EscPosBase.alignCenter());
        out.write("ENTRADA".getBytes(StandardCharsets.UTF_8));

        out.write(EscPosBase.nextLine());
        out.write(EscPosBase.alignLeft());
        String veiculoString = "Veiculo: " + v.getPlaca();
        out.write(veiculoString.getBytes(StandardCharsets.UTF_8));

        out.write(EscPosBase.nextLine());
        out.write(EscPosBase.alignLeft());
        @SuppressLint("SimpleDateFormat") String dataString = "Data: " + v.getDataEntrada();
        out.write(dataString.getBytes(StandardCharsets.UTF_8));

        out.write(EscPosBase.nextLine());
        out.write(EscPosBase.alignLeft());
        String operadorString = "Operador: " + VeiculoModel.normalize(sharedPreferences.getString("operador", ""));
        out.write(operadorString.getBytes(StandardCharsets.UTF_8));

        out.flush();

        out.write(EscPosBase.nextLine());
        out.write(EscPosBase.alignLeft());
        out.write("--------------------------------".getBytes(StandardCharsets.UTF_8));
        out.write(EscPosBase.nextLine());
        out.write("Nao nos responsabilizamos por objetos deixados no interior do veiculo.".getBytes(StandardCharsets.UTF_8));
        out.write(EscPosBase.nextLine());
        out.write("TOLERANCIA DE 10 MINUTOS".getBytes(StandardCharsets.UTF_8));
        out.write(EscPosBase.nextLine());
        out.write("================================".getBytes(StandardCharsets.UTF_8));
        out.write(EscPosBase.nextLine());

        out.flush();

        out.write(EscPosBase.nextLine(3));
        out.flush();
    }

    @SuppressLint("DefaultLocale")
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String calcularTempoEPreco(String entrada, String saida, SharedPreferences sharedPreferences) {
        // Formato de data e hora esperado
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        // Parse das strings de entrada e saída para LocalDateTime
        LocalDateTime entradaTime = LocalDateTime.parse(entrada, formatter);
        LocalDateTime saidaTime = LocalDateTime.parse(saida, formatter);

        // Calcula a duração entre entrada e saída
        Duration duracao = Duration.between(entradaTime, saidaTime);

        // Obtém o total de minutos
        long minutosTotal = duracao.toMinutes();

        // Se o tempo de permanência for menor ou igual à tolerância, não cobra
        if (minutosTotal <= Integer.parseInt(sharedPreferences.getString("tolerancia", "0"))) {
            return "Tempo: 0 horas e 0 minutos\nPreço a pagar: R$ 0.0";
        }

        // Calcula o total de horas e minutos
        long horas = minutosTotal / 60;
        long minutos = minutosTotal % 60;

        // Calcula o preço total: horas cheias + 1 hora adicional se houver minutos
        double precoTotal = horas * Integer.parseInt(sharedPreferences.getString("valorhora", "0"));
        ;
        if (minutos > 0) {
            precoTotal += Integer.parseInt(sharedPreferences.getString("valorhora", "0"));
            ; // Adiciona mais uma hora se houver minutos adicionais
        }

        // Retorna o tempo e o preço
        return String.format("Tempo: %d horas e %d minutos\nPreço a pagar: R$ %.2f", horas, minutos, precoTotal);
    }

    @Override
    public String toString() {
        return
                "Status: " + status + '\n' +
                        "Tipo: " + tipo + '\n' +
                        "Placa: " + placa + '\n' +
                        "Data Entrada: " + dataEntrada + '\n' +
                        "Data Saida: " + dataSaida + '\n' +
                        "Operador: " + operador + '\n' +
                        valorTempoPago;
    }

    public static String normalize(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("");
    }

    public List<String> getListaFotos() {
        return listaFotos;
    }

    public void setListaFotos(List<String> listaFotos) {
        this.listaFotos = listaFotos;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(String dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public String getDataSaida() {
        return dataSaida;
    }

    public void setDataSaida(String dataSaida) {
        this.dataSaida = dataSaida;
    }

    public String getOperador() {
        return operador;
    }

    public void setOperador(String operador) {
        this.operador = operador;
    }

    public String getValorTempoPago() {
        return valorTempoPago;
    }

    public void setValorTempoPago(String valorTempoPago) {
        this.valorTempoPago = valorTempoPago;
    }
}
