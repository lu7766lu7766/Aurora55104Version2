package mac.jacwang.aurora20150610.DataAnalysis;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by jac on 2015/10/21.
 */
public class News4CellDA {
    public static HashMap<Integer,HashMap<String,String>> getResult(String json){
        HashMap<Integer,HashMap<String,String>> result = new HashMap();
        try {
            Log.d("json",json);
            JSONArray jsonArray = new JSONArray(json);

            for(int i = 0; i < jsonArray.length(); i++) {
                HashMap<String,String> news_info = new HashMap<>();
                JSONObject jsonData = jsonArray.getJSONObject(i);
                news_info.put("id", jsonData.getString("fi_id"));
                news_info.put("images",jsonData.getString("fv_images"));
                result.put(i,news_info);
            }

        } catch(Exception e) {
            Log.e("AuroraJacError", e.getMessage());
        }
        return result;
    }
}
