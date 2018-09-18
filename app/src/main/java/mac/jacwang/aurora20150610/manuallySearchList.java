package mac.jacwang.aurora20150610;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import mac.jacwang.aurora20150610.Comm.DBConnector;
import mac.jacwang.aurora20150610.Comm.DividerItemDecoration;
import mac.jacwang.aurora20150610.Comm.OnRcvScrollListener;
import mac.jacwang.aurora20150610.Comm.Static_var;
import mac.jacwang.aurora20150610.Comm.mRAdapter;
import mac.jacwang.aurora20150610.Comm.mRAdapterManually;
import mac.jacwang.aurora20150610.Comm.paramsModels;
import mac.jacwang.aurora20150610.DataAnalysis.storeBL;
import mac.jacwang.aurora20150610.DataAnalysis.t_store;

public class manuallySearchList extends Fragment {


    List<t_store> store_list;
    mRAdapterManually mRAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View v = inflater.inflate(R.layout.nearby_search, container, false);

        store_list = new ArrayList<t_store>();
        mRAdapter = new mRAdapterManually(store_list);

        return v;

    }

    paramsModels param = new paramsModels();
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        // 1. get a reference to recyclerView
        recyclerView = (RecyclerView) getView().findViewById(R.id.recycler_view);
        // 2. set layoutManger
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(10,0,10,0));
        recyclerView.setHasFixedSize(true);
        // 3. create an adapter
        mRAdapter = new mRAdapterManually(store_list);
        // 4. set adapter
        recyclerView.setAdapter(mRAdapter);
        // 5. set item animator to DefaultAnimator
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        FillStoreList();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //相当于Fragment的onResume
            //init();
        } else {
            //相当于Fragment的onPause
        }
    }

    private void FillStoreList() {
        param.table = "store";
        param.type = Static_var.search_type+"";
        param.city = Static_var.search_city;
        param.dist = Static_var.search_dist;
        param.brand = Static_var.search_brand;
        param.page = "1";
        new DBConnector(mHandler, param, Static_var.STORE_DATA);
    }


    private void setScrollListener() {
        recyclerView.addOnScrollListener(new OnRcvScrollListener(){
            @Override
            public void onBottom() {
                super.onBottom();
                // 到底部自动加载
                if (!mRAdapter.stillLoading) {
                    param.page = (Integer.valueOf(param.page) + 1) + "";
                    new DBConnector(mHandler, param, Static_var.STORE_DATA);
                }
            }
        });
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String result = null;
            try {
                switch (msg.what) {
                    // 顯示網路上抓取的資料
                    case Static_var.STORE_DATA:
                        if (msg.obj instanceof String)
                            result = (String) msg.obj;

                        if (result.trim().equals("[]") || result.equals("false")) {
                        } else {
                            mRAdapter.add(new storeBL().analysisStoreList(result));
                            setScrollListener();
                        }
                        break;
                }
            } catch (Exception e) {
                Log.e("manually2Error", e.getMessage());
            }
        }
    };
}
