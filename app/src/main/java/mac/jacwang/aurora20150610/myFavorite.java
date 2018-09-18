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
import mac.jacwang.aurora20150610.Comm.favoriteRAdapter;
import mac.jacwang.aurora20150610.Comm.mRAdapterManually;
import mac.jacwang.aurora20150610.Comm.paramsModels;
import mac.jacwang.aurora20150610.DataAnalysis.storeBL;
import mac.jacwang.aurora20150610.DataAnalysis.t_store;

public class myFavorite extends mActionBarActivity {

    enum state{GET_FAVORITE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nearby_search);
        setTitle("我的最愛");
    }

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    favoriteRAdapter mRAdapter;
    Thread t1;

    public void init() {

        // 1. get a reference to recyclerView
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        // 2. set layoutManger
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(10, 0, 10, 0));
        recyclerView.setHasFixedSize(true);
        // 3. create an adapter
        mRAdapter = new favoriteRAdapter(new ArrayList<t_store>());
        // 4. set adapter
        recyclerView.setAdapter(mRAdapter);
        // 5. set item animator to DefaultAnimator
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        t1 = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    while(true) {
                        if (Static_var.favorite != null) {
                            mHandler.sendEmptyMessage(state.GET_FAVORITE.ordinal());
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
    }

    @Override
    public void onResume(){
        super.onResume();
        init();
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if( msg.what == state.GET_FAVORITE.ordinal()){
                mRAdapter.add(Static_var.favorite);
                Log.d("favoritelist",Static_var.favoriteId.toString());
            }
        }
    };
}
