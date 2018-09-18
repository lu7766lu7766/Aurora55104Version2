package mac.jacwang.aurora20150610.DataAnalysis;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jac on 15/7/1.
 */
public class cityDA {
    public List analysisCityArray(String json){
        List<String> list = new ArrayList<String>();

        try {
            JSONArray jsonArray = new JSONArray(json);
            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonData = jsonArray.getJSONObject(i);
                list.add(jsonData.getString("fv_city"));
            }
        } catch(Exception e) {
            Log.e("AuroraJacError", e.getMessage());
        }

        return list;
    }
}
