package benicio.solucoes.parkingcampeao.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class VeiculoUtils {
    public final static String NAME_PREFS = "veiculo_prefs";
    public final static String NAME = "veiculos";

    public static void saveListVeiculos(Context c, List<VeiculoModel> veiculoModelList) {
        SharedPreferences sharedPreferences = c.getSharedPreferences(NAME_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(NAME, new Gson().toJson(veiculoModelList)).apply();
    }

    public static List<VeiculoModel> returnListVeiculos(Context c) {
        SharedPreferences sharedPreferences = c.getSharedPreferences(NAME_PREFS, Context.MODE_PRIVATE);

        List<VeiculoModel> list = new Gson().fromJson(
                sharedPreferences.getString(NAME, ""),
                new TypeToken<List<VeiculoModel>>() {
                }.getType()
        );


        if (list != null)
            return list;

        return new ArrayList<>();

    }


}
