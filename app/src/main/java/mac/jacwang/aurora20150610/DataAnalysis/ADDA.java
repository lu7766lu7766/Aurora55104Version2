package mac.jacwang.aurora20150610.DataAnalysis;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jac on 15/6/20.
 */
public class ADDA {

    public List<data_model> analysisListAD(String storeJson){
        List<data_model> list = new ArrayList<data_model>();
        try {
            JSONArray jsonArray = new JSONArray(storeJson);

            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonData = jsonArray.getJSONObject(i);
                data_model model = new data_model();
                model.setImageURL(jsonData.getString("fv_ad"));
                model.setPhone(jsonData.getString("fv_phone"));
                //poi.setLongitude(Double.parseDouble(jsonData.getString("ff_longitude")));

                list.add(model);
            }
        } catch(Exception e) {
            Log.e("AuroraJacError",e.getMessage());
        }

        return list;
    }

    public List<data_model> analysisHalfAD(String storeJson){
        List<data_model> list = new ArrayList<data_model>();
        try {
            JSONArray jsonArray = new JSONArray(storeJson);

            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonData = jsonArray.getJSONObject(i);
                data_model model = new data_model();
                model.setImageURL(jsonData.getString("fv_ad"));
                model.setURL(jsonData.getString("fv_url"));
                //poi.setLongitude(Double.parseDouble(jsonData.getString("ff_longitude")));

                list.add(model);
            }
        } catch(Exception e) {
            Log.e("AuroraJacError",e.getMessage());
        }

        return list;
    }
}
