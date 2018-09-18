package mac.jacwang.aurora20150610;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mac.jacwang.aurora20150610.Comm.DividerItemDecoration;
import mac.jacwang.aurora20150610.Comm.PosGetter;
import mac.jacwang.aurora20150610.Comm.RecyclerItemClickListener;
import mac.jacwang.aurora20150610.Comm.Static_var;
import mac.jacwang.aurora20150610.Comm.categoryRAdapter;
import mac.jacwang.aurora20150610.DataAnalysis.data_model;

/**
 * Created by jac on 15/6/14.
 */
public class store_search_category extends mActionBarActivity {

    private PosGetter posGetter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_search_category);

        categoryInit();
    }

    private int[] a_image = {
            R.drawable.category_drink
            ,R.drawable.category_taxi
            ,R.drawable.category_supermarket
            ,R.drawable.category_reservation
            ,R.drawable.category_hotel
    };

    private List images = new ArrayList<data_model>();

    private void categoryInit() {

        for(int i=0;i<a_image.length;i++){
            data_model data = new data_model();
            data.setImage(a_image[i]);
            images.add(data);
        }
        RecyclerView recyclerView;
        LinearLayoutManager layoutManager;
        recyclerView = (RecyclerView)findViewById(R.id.category_recycler);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(-10,0,-35,0));
        recyclerView.setHasFixedSize(true);
        categoryRAdapter cRAdapter ;
        cRAdapter = new categoryRAdapter(images);
        recyclerView.setAdapter(cRAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // do whatever
                        Intent intent = new Intent();
                        intent.setClass(store_search_category.this, store_search_list.class);
                        Static_var.search_type = Static_var.STORE_TYPE[position];
                        startActivity(intent);
                    }
                })
        );
    }

    @Override
    protected void onPause() {
        super.onPause();
        posGetter = null;
    }

    @Override
    public void onResume(){
        super.onPause();
        posGetter = new PosGetter(getApplicationContext());
        posGetter.setOnGPSListener(new PosGetter.OnGPSListener() {
            @Override
            public void onGet(Location location, String address) {
            }
            @Override
            public void onChg(Location location, String address) {
                posGetter=null;
            }
        });
    }

    private SimpleAdapter adapter;
    private String[] from = {"category_img"};
    private int[] to = {R.id.image};
    private int[] imgs = {R.drawable.category_drink
            ,R.drawable.category_taxi
            ,R.drawable.category_supermarket
            ,R.drawable.category_reservation
            ,R.drawable.category_hotel};
    private ArrayList<HashMap<String,Object>> data;
    private SimpleAdapter setCategoryList(){
        data = new ArrayList<HashMap<String, Object>>();
        for(int i = 0 ; i < imgs.length ; i++ ){
            HashMap<String,Object> temp = new HashMap<String,Object>();
            temp.put(from[0],imgs[i]);
            data.add(temp);
        }
        adapter = new SimpleAdapter(this,data,R.layout.category_vertical,from,to);
        return adapter;
    }
}
