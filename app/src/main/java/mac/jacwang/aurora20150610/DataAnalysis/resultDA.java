package mac.jacwang.aurora20150610.DataAnalysis;

import android.util.Log;
import org.json.JSONObject;


/**
 * Created by jac on 15/7/1.
 */
public class resultDA {
    public static String getResult(String json){
        try {
            JSONObject jsonObj = new JSONObject(json);
            return jsonObj.getString("result");
        } catch(Exception e) {
            Log.e("getResultError", e.getMessage());
        }
        return "";
    }
}
