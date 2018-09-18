package mac.jacwang.aurora20150610;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

import mac.jacwang.aurora20150610.Comm.DBConnector;
import mac.jacwang.aurora20150610.Comm.Static_var;
import mac.jacwang.aurora20150610.DataAnalysis.ADBL;
import mac.jacwang.aurora20150610.DataAnalysis.brandBL;
import mac.jacwang.aurora20150610.DataAnalysis.cityBL;
import mac.jacwang.aurora20150610.DataAnalysis.distBL;

public class manuallySearch extends Fragment {

    private String[] a_city = {};
    private String[] a_dist = {};
    private String[] a_brand = {};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    LayoutInflater inflater;
    ViewGroup container;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.inflater = inflater;
        this.container = container;
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.manually_search, container, false);

        var_init();

        return view;
    }

    private void var_init(){
        Static_var.search_brand = "null";
        Static_var.search_city = "null";
        Static_var.search_dist = "null";
    }

    private ArrayAdapter<String> city_adapter;
    private ArrayAdapter<String> dist_adapter;
    private ArrayAdapter<String> brand_adapter;

    private Spinner city_spinner,dist_spinner,brand_spinner;

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ImageButton enter_btn = (ImageButton)getView().findViewById(R.id.enter_btn);
        city_spinner = (Spinner)getView().findViewById(R.id.city_spinner);
        dist_spinner = (Spinner)getView().findViewById(R.id.dist_spinner);
        brand_spinner = (Spinner)getView().findViewById(R.id.brand_spinner);

//        paramsModels city_params = new paramsModels();
//        city_params.table = "city";
        HashMap city_params = new HashMap();
        city_params.put("table","city");
        new DBConnector(mHandler,city_params, Static_var.CITY_DATA);
//        paramsModels brand_params = new paramsModels();
//        brand_params.table = "brand";
//        brand_params.type = Static_var.search_type + "";
        HashMap brand_params = new HashMap();
        brand_params.put("table","brand");
        brand_params.put("type",Static_var.search_type + "");
        new DBConnector(mHandler, brand_params, Static_var.BRAND_DATA);


        enter_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Fragment newFragment = new manuallySearchList();
//                FragmentTransaction transaction = getFragmentManager().beginTransaction();
//                try {
//                    ((FrameLayout) container.getChildAt(1).findViewById(R.id.fragment_container)).removeAllViews();
//                }catch (Exception e){
//                    ((FrameLayout) container.getChildAt(0).findViewById(R.id.fragment_container)).removeAllViews();
//                }
//                transaction.replace(R.id.fragment_container, newFragment);
//                transaction.addToBackStack(null);
//                transaction.commit();

                //Toast.makeText(getActivity(), "此功能尚在建置" + container.getChildCount(), Toast.LENGTH_SHORT).show();
                HashMap paramList = new HashMap();
                paramList.put("table", "getAdvertisement");
                paramList.put("type", "1");

                String address = Static_var.search_city=="null"?"":Static_var.search_city;
                address += Static_var.search_dist=="null"?"":Static_var.search_dist;

                paramList.put("address", address);
                new DBConnector(mHandler, paramList, Static_var.GET_LIST_AD);
            }
        });
    }

    //使用陣列形式操作
    class CitySelectedListener implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> arg0, View arg1, int i,long l) {
//            paramsModels dist_params = new paramsModels();
//            dist_params.table = "dist";
//            dist_params.city = a_city[i];
            HashMap dist_params = new HashMap();
            dist_params.put("table","dist");
            dist_params.put("city",a_city[i]);
            new DBConnector(mHandler,dist_params,Static_var.DIST_DATA);
            if(a_city[i].indexOf("不限")>-1)
                Static_var.search_city = "null";
            else
                Static_var.search_city = a_city[i];
        }
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }

    //使用陣列形式操作
    class DistSelectedListener implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> arg0, View arg1, int i,long l) {
            if(a_dist[i].indexOf("不限")>-1)
                Static_var.search_dist = "null";
            else
                Static_var.search_dist = a_dist[i];
        }
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }

    //使用陣列形式操作
    class BrandSelectedListener implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> arg0, View arg1, int i,long l) {
            if(a_brand[i].indexOf("不限")>-1)
                Static_var.search_brand = "null";
            else
                Static_var.search_brand = a_brand[i];
        }
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String result = null;
            try{
                if (msg.obj instanceof String)
                    result = (String) msg.obj;
                switch (msg.what) {
                    // 顯示網路上抓取的資料
                    case Static_var.CITY_DATA:

                        if (result.trim().equals("[]")||result.equals("false")) {
                        }
                        else
                        {
                            List city_list = new cityBL().analysisCityArray(result);
                            a_city = (String[]) city_list.toArray(new String[city_list.size()]);
                            if(a_city.length>0) {

                                Static_var.search_city = a_city[0];

                                city_adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, a_city);
//                                paramsModels dist_params = new paramsModels();
//                                dist_params.table = "dist";
//                                dist_params.city = a_city[0];
                                HashMap dist_params = new HashMap();
                                dist_params.put("table","dist");
                                dist_params.put("city",a_city[0]);
                                new DBConnector(mHandler, dist_params, Static_var.DIST_DATA);

                                city_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                //將adapter 添加到spinner中
                                city_spinner.setAdapter(city_adapter);
                                //添加事件Spinner事件監聽
                                city_spinner.setOnItemSelectedListener(new CitySelectedListener());
                                //設置預設值
                                city_spinner.setVisibility(View.VISIBLE);
                            }

                        }
                        break;

                    case Static_var.DIST_DATA:

                        if (result.trim().equals("[]")||result.equals("false")) {
                        }
                        else
                        {
                            List dist_list = new distBL().analysisDistArray(result);
                            dist_list.add(0,"不限區域");

                            a_dist = (String[]) dist_list.toArray(new String[dist_list.size()]);

                            dist_adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, a_dist);

                            dist_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            //將adapter 添加到spinner中
                            dist_spinner.setAdapter(dist_adapter);
                            //設置預設值
                            dist_spinner.setVisibility(View.VISIBLE);

                            dist_spinner.setOnItemSelectedListener(new DistSelectedListener());

                        }
                        break;

                    case Static_var.BRAND_DATA:

                        if (result.trim().equals("[]")||result.equals("false")) {
                        }
                        else
                        {
                            List brand_list = new brandBL().analysisBrandArray(result);
                            brand_list.add(0,"不限品牌");
                            a_brand = (String[]) brand_list.toArray(new String[brand_list.size()]);
//                            a_brand = new String[brand_list.size()];
//                            a_brand = (String[])brand_list.toArray();
                            //if(getActivity()!=null){
                                brand_adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,a_brand);

                                brand_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                //將adapter 添加到spinner中
                                brand_spinner.setAdapter(brand_adapter);
                                //設置預設值
                                brand_spinner.setVisibility(View.VISIBLE);

                                brand_spinner.setOnItemSelectedListener(new BrandSelectedListener());
                            //}
                        }
                        break;
                    case Static_var.GET_LIST_AD:
                        if (!result.trim().equals("[]") && !result.equals("false")) {
                            Static_var.listADManually = new ADBL().analysisListAD(result);
                        }
                        Fragment newFragment = new manuallySearchList();
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        try {
                            ((FrameLayout) container.getChildAt(1).findViewById(R.id.fragment_container)).removeAllViews();
                        }catch (Exception e){
                            ((FrameLayout) container.getChildAt(0).findViewById(R.id.fragment_container)).removeAllViews();
                        }
                        transaction.replace(R.id.fragment_container, newFragment);
                        //transaction.addToBackStack(null);
                        transaction.commit();
                        break;
                }
            }catch(Exception e){
                Log.e("manuallysearchError",e.getMessage());
            }
        }
    };

}
