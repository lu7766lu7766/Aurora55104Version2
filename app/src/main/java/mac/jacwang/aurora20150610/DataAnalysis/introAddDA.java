package mac.jacwang.aurora20150610.DataAnalysis;

import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;


/**
 * Created by jac on 15/7/1.
 */
public class introAddDA {
    public static HashMap<String,String> getResult(String json){
        HashMap<String,String> map = new HashMap<>();
        try {
            JSONObject jsonObj = new JSONObject(json);
            map.put("result",jsonObj.getString("result"));
            map.put("add_bonus",jsonObj.getString("add_bonus"));
            return map;
        } catch(Exception e) {
            Log.e("getResultError", e.getMessage());
        }
        return map;
    }
}
