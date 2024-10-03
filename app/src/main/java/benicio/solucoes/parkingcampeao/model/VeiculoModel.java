package benicio.solucoes.parkingcampeao.model;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.text.style.IconMarginSpan;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.time.Duration;
import java.time.LocalDateTime;

import benicio.solucoes.parkingcampeao.EscPosBase;
import benicio.solucoes.parkingcampeao.PrinterConverter;
import benicio.solucoes.parkingcampeao.R;

public class VeiculoModel {

    String id = "";

    private final String valorMeiaHoraString = "valorMeiaHora";
    private final String valorHoraString = "valorHora";
    private final String valorExcedenteString = "valorExcedente";
    private final String valorDiarioString = "valorDiario";
    private final String valorMensalString = "valorMensal";

    String status = "Pendente";
    String tipo, placa, dataEntrada, dataSaida, operador;

    float valorMaisBarato = 0;

    boolean diaria = false, mensalidade = false, cobrarMenos = false;

    List<String> listaFotos = new ArrayList<>();

    String valorTempoPago = "Tempo: 0 horas e 0 minutos\nValor a pagar:  0.0";

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
        out.write(EscPosBase.alignCenter());
//        out.write(EscPosBase.getFontTall());
        String veiculoPlaca = VeiculoModel.normalize(v.getTipo().toUpperCase() + " " + v.getPlaca());
        out.write(String.join(" ", veiculoPlaca.split("")).getBytes(StandardCharsets.UTF_8));

        out.flush();

        out.write(EscPosBase.getResetPrinter());
        out.write(EscPosBase.nextLine());
        out.write(EscPosBase.alignLeft());
        out.write(EscPosBase.getFontNormal());
        String operadorString = "Operador: " + VeiculoModel.normalize(v.operador);
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
        String tipoPagamentoValor = v.valorTempoPago.split("\n")[0];
        out.write(tipoPagamentoValor.getBytes(StandardCharsets.UTF_8));

        out.write(EscPosBase.nextLine());
        out.write(EscPosBase.alignLeft());
        String dataPermanenciaString = v.valorTempoPago.split("\n")[1];
        out.write(dataPermanenciaString.getBytes(StandardCharsets.UTF_8));

        String valorStringLabel = VeiculoModel.normalize(v.valorTempoPago.split("\n")[2]);
        String valorLabel = valorStringLabel.split(":")[0] + ":";
        String valorString = valorStringLabel.split(":")[1];

        out.write(EscPosBase.nextLine());
        out.write(EscPosBase.alignLeft());
        out.write(valorLabel.getBytes(StandardCharsets.UTF_8));

        out.write(EscPosBase.getFontTall());
        out.write(EscPosBase.TEXT_WEIGHT_BOLD);
        out.write(EscPosBase.TEXT_COLOR_BLACK);
//        out.write(EscPosBase.TEXT_SIZE_DOUBLE_HEIGHT);
        out.write(EscPosBase.TEXT_SIZE_BIG);
        out.write(EscPosBase.nextLine());
        out.write(EscPosBase.alignCenter());
        out.write(String.join(" ", valorString.split("")).getBytes(StandardCharsets.UTF_8));

        out.flush();

        out.write(EscPosBase.getResetPrinter());
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
        out.write(EscPosBase.alignCenter());
//        out.write(EscPosBase.getFontTall());
        String veiculoPlaca = VeiculoModel.normalize(v.getTipo().toUpperCase() + " " + v.getPlaca());
        out.write(String.join(" ", veiculoPlaca.split("")).getBytes(StandardCharsets.UTF_8));

        out.flush();
        out.write(EscPosBase.getResetPrinter());
        out.write(EscPosBase.nextLine());
        out.write(EscPosBase.alignLeft());
        out.write(EscPosBase.getFontNormal());
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
        out.write(EscPosBase.alignCenter());
        String toleranciString = "TOLERANCIA DE " + sharedPreferences.getString("tolerancia", "0") + " MINUTO(S)";
        out.write(toleranciString.getBytes(StandardCharsets.UTF_8));
        out.write(EscPosBase.nextLine());
        out.write("================================".getBytes(StandardCharsets.UTF_8));
        out.write(EscPosBase.nextLine());

        out.flush();

        out.write(EscPosBase.nextLine(3));
        out.flush();
    }

    @SuppressLint("DefaultLocale")
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String calcularTempoEPreco(String entrada, String saida, SharedPreferences sharedPreferences, VeiculoModel veiculoModel) {

        String valor_mensal = sharedPreferences.getString(veiculoModel.valorMensalString + veiculoModel.getTipo(), "0").replace(",", ".");
        String valor_dario = sharedPreferences.getString(veiculoModel.valorDiarioString + veiculoModel.getTipo(), "0").replace(",", ".");
        String valor_hora = sharedPreferences.getString(veiculoModel.valorHoraString + veiculoModel.getTipo(), "0").replace(",", ".");
        String valor_meia_hora = sharedPreferences.getString(veiculoModel.valorMeiaHoraString + veiculoModel.getTipo(), "0").replace(",", ".");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        // Parse das strings de entrada e saída para LocalDateTime
        LocalDateTime entradaTime = LocalDateTime.parse(entrada, formatter);
        LocalDateTime saidaTime = LocalDateTime.parse(saida, formatter);

        // Calcula a duração entre entrada e saída
        Duration duracao = Duration.between(entradaTime, saidaTime);

        // Calcula o valor da cobrança
        float tolerancia = Float.parseFloat(sharedPreferences.getString("tolerancia", "0").replace(",", "."));
        float valorMensal = Float.parseFloat(valor_mensal);
        float valorDiario = Float.parseFloat(valor_dario);
        float valorHora = Float.parseFloat(valor_hora);
        float valorMeiaHora = Float.parseFloat(valor_meia_hora);

        double valor = calcularCobranca(duracao, tolerancia, valorMensal, valorDiario, valorHora, valorMeiaHora);
        return formatarPagamento(duracao, valor, tolerancia);

    }

    @SuppressLint("DefaultLocale")
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String formatarPagamento(Duration duracao, double valor, float tolerancia) {
        long dias = duracao.toDays();


        // Se a duração for de um mês ou mais
        if (dias >= 30) {
            long meses = dias / 30;
            long diasRestantes = dias % 30;
            return String.format("Tempo: %d meses e %d dias\nPagamento por mes\nValor a pagar:  %.2f", meses, diasRestantes, valor);
        }

        // Se a duração for de mais de um dia
        if (dias >= 1) {
            return String.format("Tempo: %d dias\nPagamento por dia\nValor a pagar:  %.2f", dias, valor);
        }

        // Se a duração for de mais de uma hora
        long horas = duracao.toHours();
        long minutos = duracao.toMinutes() % 60;
        if (horas >= 1) {
            return String.format("Tempo: %d horas e %d minutos\nPagamento por hora\nValor a pagar:  %.2f", horas, minutos, valor);
        }

        // Se a duração for de meia hora ou mais
        long minutosTotais = duracao.toMinutes();
        if (minutosTotais >= 0) {
            return String.format("Tempo: %d minutos\nPagamento por meia hora\nValor a pagar:  %.2f", minutosTotais, valor);
        }

        if (duracao.toMinutes() <= tolerancia) {
            return String.format("Tempo: %d minutos\nSem cobranca devido a tolerancia\nValor a pagar:  %.2f", minutosTotais, valor);
        }

        return "";
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static double calcularCobranca(Duration duracao, float tolerancia, float valorMensal,
                                          float valorDiario, float valorHora, float valorMeiaHora) {
        // Verifica se a duração é de um minuto ou menos
        if (duracao.toMinutes() <= tolerancia) {
            return 0.0;
        }

        // Calcula a duração efetiva após a tolerância
        long minutosEfetivos = duracao.toMinutes() - (long) tolerancia;

        // Verifica se a duração é de um mês ou mais
        long dias = duracao.toDays();
        if (dias >= 30) {
            long meses = dias / 30;
            long diasRestantes = dias % 30;
            double valor = meses * valorMensal;

            if (diasRestantes > 0) {
                valor += diasRestantes * valorDiario;
            }
            return valor;
        }

        // Verifica se a duração é de mais de um dia
        if (dias >= 1) {
            return dias * valorDiario;
        }

        // Verifica se a duração é de mais de uma hora
        long horas = minutosEfetivos / 60;
        if (horas >= 1) {
            return horas * valorHora + (minutosEfetivos % 60 > 0 ? valorMeiaHora : 0);
        }

        // Para durações que excedem a tolerância mas são menores que 30 minutos
        if (minutosEfetivos > 0) {
            return valorMeiaHora; // Cobrar 3,50 reais por meia hora
        }

        return 0.0;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isDiaria() {
        return diaria;
    }

    public void setDiaria(boolean diaria) {
        this.diaria = diaria;
    }

    public boolean isMensalidade() {
        return mensalidade;
    }

    public void setMensalidade(boolean mensalidade) {
        this.mensalidade = mensalidade;
    }

    public boolean isCobrarMenos() {
        return cobrarMenos;
    }

    public void setCobrarMenos(boolean cobrarMenos) {
        this.cobrarMenos = cobrarMenos;
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

    public float getValorMaisBarato() {
        return valorMaisBarato;
    }

    public void setValorMaisBarato(float valorMaisBarato) {
        this.valorMaisBarato = valorMaisBarato;
    }

    public String getValorTempoPago() {
        return valorTempoPago;
    }

    public void setValorTempoPago(String valorTempoPago) {
        this.valorTempoPago = valorTempoPago;
    }

    public float getValorPago() {
        // Extrai o valor do campo valorTempoPago
        try {
            String valorStr = valorTempoPago.split("\n")[2].split(":")[1].replace(",", ".");
            return Float.parseFloat(valorStr);
        } catch (Exception e) {
            return 0;
        }
    }
}
