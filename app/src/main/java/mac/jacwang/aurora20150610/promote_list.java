package mac.jacwang.aurora20150610;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mac.jacwang.aurora20150610.Comm.DBConnector;
import mac.jacwang.aurora20150610.Comm.DividerItemDecoration;
import mac.jacwang.aurora20150610.Comm.OnRcvScrollListener;
import mac.jacwang.aurora20150610.Comm.RecyclerItemClickListener;
import mac.jacwang.aurora20150610.Comm.Static_var;
import mac.jacwang.aurora20150610.Comm.catesubRAdapter;
import mac.jacwang.aurora20150610.Comm.promoteRAdapter;
import mac.jacwang.aurora20150610.DataAnalysis.data_model;
import mac.jacwang.aurora20150610.DataAnalysis.promoteDA;

public class promote_list extends mActionBarActivity {

    enum state{GET_PROMOTE,CHG_PROMOTE}
    HashMap<Integer,List<HashMap<String,String>>> promote_list_all = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.promote_layout);
        setTitle("好康專區");

        promotecate_init();
        init();
//        TextView describe_txt = (TextView)findViewById(R.id.describe);
//        describe_txt.setVisibility(View.GONE);
    }

    private int[] a_image = {
            R.drawable.promote_cellphone
            , R.drawable.promote_3c
            , R.drawable.promote_other
    };
    private int[] a_image_b = {
            R.drawable.promote_cellphone_b
            , R.drawable.promote_3c_b
            , R.drawable.promote_other_b
    };
    private List images = new ArrayList<data_model>();
    int pre_position = 0;
    catesubRAdapter cateadapter;
    HashMap<String,String> params2 = new HashMap<>();

    private void promotecate_init(){

        int len = Static_var.PROMOTE_TYPE.length;
        for(int index=0;index<len;index++){
            if( Static_var.search_promote_type == Static_var.PROMOTE_TYPE[index] ){
                pre_position = index;
                break;
            }
        }

        for(int i=0;i<a_image.length;i++){
            data_model data = new data_model();
            if(i==pre_position){
                data.setImage(a_image_b[i]);
            }else {
                data.setImage(a_image[i]);
            }
            images.add(data);
        }

        RecyclerView cate_view;
        LinearLayoutManager lm;

        cate_view = (RecyclerView)findViewById(R.id.promotecate_recycler);
        lm = new LinearLayoutManager(this);
        lm.setOrientation(LinearLayoutManager.HORIZONTAL);
        cate_view.setLayoutManager(lm);
        cate_view.addItemDecoration(new DividerItemDecoration(-5, 5, 0, 5));
        cate_view.setHasFixedSize(true);
        cateadapter = new catesubRAdapter(images);
        cate_view.setAdapter(cateadapter);
        cate_view.setItemAnimator(new DefaultItemAnimator());
        cate_view.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // do whatever
                        Static_var.search_promote_type = Static_var.PROMOTE_TYPE[position];


                        images.remove(pre_position);
                        data_model pre_data = new data_model();
                        pre_data.setImage(a_image[pre_position]);
                        images.add(pre_position, pre_data);

                        images.remove(position);
                        data_model data = new data_model();
                        data.setImage(a_image_b[position]);
                        images.add(position, data);

                        cateadapter.notifyDataSetChanged();
                        pre_position = position;

                        if(promote_list_all.get(Static_var.search_promote_type)!=null){
                            updatePromoteList(promote_list_all.get(Static_var.search_promote_type));
                        }else {
                            page = 1;
                            params2.put("table", "getPromote");
                            params2.put("page", page++ + "");
                            params2.put("type", Static_var.search_promote_type + "");
                            new DBConnector(mHandler, params2, state.CHG_PROMOTE.ordinal());
                        }
                    }
                })
        );
    }

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    promoteRAdapter mRAdapter;
    HashMap<String,String> params = new HashMap<>();
    int page = 1;

    public void init() {
        TextView exchg_describe_txt = (TextView)findViewById(R.id.exchg_describe);
        exchg_describe_txt.setText(Html.fromHtml(
                "兌換禮物請撥<big><font color=\"#ff0000\"><b>55104#9</b></font></big><br>" +
                "由客服人員為您服務"));

        // 1. get a reference to recyclerView
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        // 2. set layoutManger
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(10, 0, 10, 0));
        recyclerView.setHasFixedSize(true);
        // 3. create an adapter
        mRAdapter = new promoteRAdapter(new ArrayList<HashMap<String,String>>());
        // 4. set adapter
        recyclerView.setAdapter(mRAdapter);
        // 5. set item animator to DefaultAnimator
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        params.put("table", "getPromote");
        params.put("page",page+++"");
        params.put("type",Static_var.search_promote_type+"");
        new DBConnector(mHandler,params,state.GET_PROMOTE.ordinal());
        //setScrollListener();
    }

    private void setScrollListener() {
        recyclerView.addOnScrollListener(new OnRcvScrollListener(){
            @Override
            public void onBottom() {
                super.onBottom();
                params.put("page",page+++"");
                new DBConnector(mHandler, params, state.GET_PROMOTE.ordinal());
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();

    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            String result = null;
            if (msg.obj instanceof String)
                result = (String) msg.obj;
            //Log.d("result",result+"%%");
            if( msg.what == state.GET_PROMOTE.ordinal()){
                List<HashMap<String,String>> promote_list = new promoteDA().getResult(result);
                mRAdapter.add(promote_list);
            }else if( msg.what == state.CHG_PROMOTE.ordinal()){
                List<HashMap<String,String>> promote_list = new promoteDA().getResult(result);
                updatePromoteList(promote_list);
                promote_list_all.put(Static_var.search_promote_type,promote_list);
            }
        }
    };

    private void updatePromoteList(List<HashMap<String,String>> promote_list){
        mRAdapter.removeAll();
        mRAdapter.add(promote_list);
    }
}
