package mac.jacwang.aurora20150610;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

import mac.jacwang.aurora20150610.Comm.DBConnector;
import mac.jacwang.aurora20150610.Comm.Static_var;
import mac.jacwang.aurora20150610.DataAnalysis.cityBL;
import mac.jacwang.aurora20150610.DataAnalysis.distBL;
import mac.jacwang.aurora20150610.DataAnalysis.resultDA;


public class recommendStore extends mActionBarActivity{

    private String[] a_zip = {"02","03","037","04","047","048","049","05","06","07","08","089","082","0826","0836"};
    private String[] a_city = {};
    private String[] a_dist = {};

    private ArrayAdapter<String> city_adapter;
    private ArrayAdapter<String> dist_adapter;
    private ArrayAdapter<String> zip_adapter;

    String city,dist,zip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recommend_store);
        setTitle("推薦店家");

        init();
    }

    EditText phone_txt,store_name_txt,store_subname_txt,address_txt;
    Spinner zip_spinner,city_spinner,dist_spinner;
    ImageButton recommend_btn;

    private void init(){
        phone_txt = (EditText)findViewById(R.id.phone);
        store_name_txt = (EditText)findViewById(R.id.store_name);
        store_subname_txt = (EditText)findViewById(R.id.store_subname);
        address_txt = (EditText)findViewById(R.id.address);

        zip_spinner = (Spinner)findViewById(R.id.zip_spinner);
        city_spinner = (Spinner)findViewById(R.id.city_spinner);
        dist_spinner = (Spinner)findViewById(R.id.dist_spinner);

        recommend_btn = (ImageButton)findViewById(R.id.recommend);

        HashMap city_params = new HashMap();
        city_params.put("table","city");
        new DBConnector(mHandler,city_params, Static_var.CITY_DATA);

        zip_adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.myspinner,a_zip);
        zip_adapter.setDropDownViewResource(R.layout.myspinner);
        zip_spinner.setAdapter(zip_adapter);
        zip_spinner.setOnItemSelectedListener(new ZipSelectedListener());
        zip_spinner.setVisibility(View.VISIBLE);

        recommend_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap upload_store = new HashMap();
                upload_store.put("table","uploadStore");
                upload_store.put("intro_phone",settings.getString(Static_var.phone, ""));
                upload_store.put("store_phone",zip+phone_txt.getText());
                upload_store.put("subname",store_subname_txt.getText());
                upload_store.put("brand",store_name_txt.getText());
                upload_store.put("address",address_txt.getText());
                new DBConnector(mHandler,upload_store, Static_var.UPLOAD_STORE_DATA);
            }
        });
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            String result = "";
            if (msg.obj instanceof String)
                result = (String) msg.obj;
            switch (msg.what) {
                case Static_var.CITY_DATA:
                    List city_list = new cityBL().analysisCityArray(result);
                    a_city = (String[]) city_list.toArray(new String[city_list.size()]);
                    if(a_city.length>0) {

                        city = a_city[0];

                        city_adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.myspinner, a_city);
                        HashMap dist_params = new HashMap();
                        dist_params.put("table","dist");
                        dist_params.put("city",a_city[0]);
                        new DBConnector(mHandler, dist_params, Static_var.DIST_DATA);

                        city_adapter.setDropDownViewResource(R.layout.myspinner);
                        city_spinner.setAdapter(city_adapter);
                        city_spinner.setOnItemSelectedListener(new CitySelectedListener());
                        city_spinner.setVisibility(View.VISIBLE);
                    }
                    break;

                case Static_var.DIST_DATA:
                    List dist_list = new distBL().analysisDistArray(result);

                    a_dist = (String[]) dist_list.toArray(new String[dist_list.size()]);

                    dist_adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.myspinner, a_dist);

                    dist_adapter.setDropDownViewResource(R.layout.myspinner);
                    dist_spinner.setAdapter(dist_adapter);
                    dist_spinner.setVisibility(View.VISIBLE);
                    dist_spinner.setOnItemSelectedListener(new DistSelectedListener());

                    break;

                case Static_var.UPLOAD_STORE_DATA:
                    result = resultDA.getResult(result);
                    switch (result){
                        case "data_incomplete":
                            Toast.makeText(getApplicationContext(),"資料並不完整",Toast.LENGTH_SHORT).show();
                            break;
                        case "upload_error":
                            Toast.makeText(getApplicationContext(),"上傳失敗，請確認網路連線或與直接撥打客服",Toast.LENGTH_SHORT).show();
                            break;
                        case "success":
                            Intent intent = new Intent();
                            intent.setClass(recommendStore.this,recommendStoreSuccess.class);
                            startActivity(intent);
                            finish();
                            break;
                    }
                    break;
            }
        }
    };

    class CitySelectedListener implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> arg0, View arg1, int i,long l) {
            HashMap dist_params = new HashMap();
            dist_params.put("table","dist");
            dist_params.put("city",a_city[i]);
            new DBConnector(mHandler,dist_params,Static_var.DIST_DATA);

            city = a_city[i];
        }
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }

    class DistSelectedListener implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> arg0, View arg1, int i,long l) {
            dist = a_dist[i];
        }
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }

    class ZipSelectedListener implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> arg0, View arg1, int i,long l) {
            zip = a_zip[i];
        }
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }
}
