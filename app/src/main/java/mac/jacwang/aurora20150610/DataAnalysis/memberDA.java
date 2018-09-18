package mac.jacwang.aurora20150610.DataAnalysis;

import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;


/**
 * Created by jac on 15/7/1.
 */
public class memberDA {
    public static HashMap<String,String> getResult(String json){
        HashMap<String,String> result = new HashMap<String,String>();
        try {
            JSONObject jsonObj = new JSONObject(json);
            result.put("promote_code", jsonObj.getString("fv_promotecode"));
            result.put("dail",jsonObj.getString("fi_deal"));
            result.put("intor_times",jsonObj.getString("fi_intro"));
            result.put("bonus",jsonObj.getString("fi_bonus"));
            result.put("intro_promote_code",jsonObj.getString("fv_intro_phone"));
        } catch(Exception e) {
            Log.e("AuroraJacError", e.getMessage());
        }
        return result;
    }
}
