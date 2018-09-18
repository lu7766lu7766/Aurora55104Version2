package mac.jacwang.aurora20150610;

import android.app.ProgressDialog;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import mac.jacwang.aurora20150610.Comm.DividerItemDecoration;
import mac.jacwang.aurora20150610.Comm.MyDAO;
import mac.jacwang.aurora20150610.Comm.OnRcvScrollListener;
import mac.jacwang.aurora20150610.Comm.PosGetter;
import mac.jacwang.aurora20150610.Comm.Static_var;
import mac.jacwang.aurora20150610.Comm.mRAdapter;
import mac.jacwang.aurora20150610.DataAnalysis.t_store;

public class nearbySearch extends Fragment {

    PosGetter posGet ;

    List<t_store> store_list ;
    mRAdapter mRAdapter ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        if(store_list==null) {
            View v = inflater.inflate(R.layout.nearby_search, container, false);

            return v;
        }
        return null;
    }

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        if(store_list==null){
            posGet = new PosGetter(getActivity());

            store_list = new ArrayList<t_store>();
            // 1. get a reference to recyclerView
            recyclerView = (RecyclerView)getView().findViewById(R.id.recycler_view);
            // 2. set layoutManger
            layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.addItemDecoration(new DividerItemDecoration(10, 0, 10, 0));
            recyclerView.setHasFixedSize(true);
            // 3. create an adapter
            mRAdapter = new mRAdapter(store_list);
            // 4. set adapter
            recyclerView.setAdapter(mRAdapter);
            // 5. set item animator to DefaultAnimator
            recyclerView.setItemAnimator(new DefaultItemAnimator());

            posGet = new PosGetter(getActivity());
            PosGet();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //相当于Fragment的onResume
            //init();
//            if(posGet==null) {
//            }
        } else {
            //相当于Fragment的onPause
            posGet = null;
        }
    }

    Double lat,lng;
    MyDAO dao;
    List<t_store> list;
    int page ,onceLimit=15;
    String limit="0,"+onceLimit;
    String where_con = "fi_type=?";
    String[] where_val= {Static_var.search_type+""} ;
    String order_by;
    Thread t1;

    private void PosGet(){
        page = 0 ;
        t1 = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    while(true) {
                        if (Static_var.location != null) {
                            mHandler.sendEmptyMessage(1);
                            t1.interrupt();
                            t1 = null;
                            break;
                        }
                        Thread.sleep(1000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t1.start();

        posGet.setOnGPSListener(new PosGetter.OnGPSListener() {
            @Override
            public void onGet(Location location, String address) {
                Static_var.location = location;
            }

            @Override
            public void onChg(Location location, String address) {
                Static_var.location = location;
                store_list = new ArrayList<t_store>();
                mRAdapter = new mRAdapter(store_list);
                recyclerView.setAdapter(mRAdapter);
                getData(location);
            }
        });
        setScrollListener();
    }

    private void getData(Location location){
        if(location != null) {
            lat = location.getLatitude();
            lng = location.getLongitude();
            order_by = "abs(ff_latitude - " + lat + ") + abs(ff_longitude - " + lng + ")";
            Log.d("order_by",order_by);
            where_val[0] = Static_var.search_type + "";
            dao = MyDAO.getInstance(getActivity(), "sqlite.db", t_store.class);
            list = dao.get(t_store.class, where_con, where_val, order_by, limit);
            fillAddress();
            mRAdapter.add(list);
        }
    }

    private void limitAdd(){
        String[] a_str = this.limit.split(",");
        int sum = 0;
        for(String val : a_str){
            sum+=Integer.valueOf(val);
        }
        this.limit = sum+","+onceLimit;

    }

    Thread t2;
    private void fillAddress(){
         t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                if (list.size() > 0) {
                    for (t_store item : list) {
                        if (item.getDistance() > 0) {
                            continue;
                        }
                        double distance = Math.round(GetDistance(lat, lng, item.getLatitude(), item.getLongitude()));
                        item.setDistance(distance);
                    }
                    t2.interrupt();
                    t2=null;
                }
            }
        });
        t2.start();
    }



    private void setScrollListener(){
        recyclerView.addOnScrollListener(new OnRcvScrollListener() {
            @Override
            public void onBottom() {
                super.onBottom();
                // 到底部自动加载
                if (!mRAdapter.stillLoading) {
                    limitAdd();
                    list = dao.get(t_store.class,where_con,where_val,order_by, limit);
                    fillAddress();
                    mRAdapter.add(list);
                    //new DBConnector(mHandler, param, Static_var.STORE_DATA);
                }
            }
        });
    }

    public double GetDistance(double Lat1, double Long1, double Lat2, double Long2)
    {
        double Lat1r = ConvertDegreeToRadians(Lat1);
        double Lat2r = ConvertDegreeToRadians(Lat2);
        double Long1r = ConvertDegreeToRadians(Long1);
        double Long2r = ConvertDegreeToRadians(Long2);

        double R = 6371; // Earth's radius (km)
        double d = Math.acos(Math.sin(Lat1r) *
                Math.sin(Lat2r) + Math.cos(Lat1r) *
                Math.cos(Lat2r) *
                Math.cos(Long2r-Long1r)) * R;
        return d * 1000;
    }

    private double ConvertDegreeToRadians(double degrees)
    {
        return (Math.PI/180)*degrees;
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    getData(Static_var.location);
                    break;
            }
        }
    };
}
