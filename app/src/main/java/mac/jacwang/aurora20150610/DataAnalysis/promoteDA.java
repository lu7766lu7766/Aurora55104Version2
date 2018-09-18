package mac.jacwang.aurora20150610.DataAnalysis;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mac.jacwang.aurora20150610.Comm.Static_var;

/**
 * Created by jac on 15/7/1.
 */
public class promoteDA {
    public List<HashMap<String,String>> getResult(String json){
        List<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();

        try {
            JSONArray jsonArray = new JSONArray(json);
            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonData = jsonArray.getJSONObject(i);
                HashMap<String,String> data = new HashMap<String,String>();
                data.put("id",jsonData.getString("fi_id"));
                data.put("name",jsonData.getString("fv_name"));
                data.put("describe",jsonData.getString("fv_describe"));
                data.put("logo", jsonData.getString("fv_logo"));
                data.put("bonus", jsonData.getString("fi_bonus"));
                String str = jsonData.getString("fv_activity_picture");
                str = str.equals("null") ? "" : str;
                data.put("activity", str.equals("null") ? "" : str);
                list.add(data);
            }
        } catch(Exception e) {
            Log.e("PromoteError", e.getMessage());
        }

        return list;
    }
}
