package mac.jacwang.aurora20150610.DataAnalysis;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static mac.jacwang.aurora20150610.Comm.Static_var.*;
import com.niusounds.sqlite.SQLiteDAO;

/**
 * Created by jac on 15/6/20.
 */
public class storeDA {

    public List<t_store> analysisStoreList(String storeJson){
        List<t_store> store_list = new ArrayList<t_store>();
        try {
            JSONArray jsonArray = new JSONArray(storeJson);

            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonData = jsonArray.getJSONObject(i);
                t_store poi = new t_store();
                poi.setFiId(jsonData.getInt("fi_id"));
                poi.setBrandName(jsonData.getString("fv_brand_name"));
                poi.setAddress(jsonData.getString("fv_address"));
                poi.setLogo(jsonData.getString("fv_logo"));
                if(!jsonData.getString("fv_menu").equals("null"))
                    poi.setMenu(jsonData.getString("fv_menu"));
                poi.setPhone(jsonData.getString("fv_phone"));
                poi.setSubName(jsonData.getString("fv_subname"));
                poi.setSpecailPrice(jsonData.getInt("fi_special_price"));
                poi.setSpecialInfo(jsonData.getString("fv_special_info"));

                poi.setLevel(jsonData.getInt("fi_level"));
                //poi.setLongitude(Double.parseDouble(jsonData.getString("ff_longitude")));
                //poi.setLatitude(Double.parseDouble(jsonData.getString("ff_latitude")));

                //NumberFormat nf = NumberFormat.getInstance();
                //nf.setMaximumFractionDigits( 0 );
                //poi.setDistance(Math.floor(Double.parseDouble(jsonData.getString("distance"))));
                poi.setDistance( Math.floor(jsonData.getDouble("distance")) );

                store_list.add(poi);
            }
        } catch(Exception e) {
            Log.e("AuroraJacError",e.getMessage());
        }

        return store_list;
    }
}
