package mac.jacwang.aurora20150610;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import mac.jacwang.aurora20150610.Comm.DBConnector;
import mac.jacwang.aurora20150610.Comm.DividerItemDecoration;
import mac.jacwang.aurora20150610.Comm.PosGetter;
import mac.jacwang.aurora20150610.Comm.RecyclerItemClickListener;
import mac.jacwang.aurora20150610.Comm.Static_var;
import mac.jacwang.aurora20150610.Comm.catesubRAdapter;
import mac.jacwang.aurora20150610.DataAnalysis.data_model;
import mac.jacwang.aurora20150610.DataAnalysis.ADBL;

public class store_search_list extends mActionBarActivity{

    enum state {GET_ADDRESS};
    private FragmentTabHost mTabHost;
    private ViewPager mViewPager;
    private Class[] mFragments = new Class[] {
            nearbySearch.class,
            manuallySearch.class };

    private String[] mIndicator = {"附近查詢","手動查詢"};

    private int[] a_image = {
            R.drawable.catesub_drink
            , R.drawable.catesub_taxi
            , R.drawable.catesub_supermarket
            , R.drawable.catesub_reservation
            , R.drawable.catesub_hotel
    };
    private int[] a_image_b = {
            R.drawable.catesub_drink_b
            , R.drawable.catesub_taxi_b
            , R.drawable.catesub_supermarket_b
            , R.drawable.catesub_reservation_b
            , R.drawable.catesub_hotel_b
    };

    private List images = new ArrayList<data_model>();
    TextView address_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_search_list);
        setTitle("店家搜索");

        mViewPager = (ViewPager) findViewById(R.id.my_pager);
        mTabHost = (FragmentTabHost) findViewById(R.id.my_tab);

        address_txt  = (TextView)findViewById(R.id.address);

        catesub_init();

        init();
    }

    PosGetter posGet;
    Thread t1;
    @Override
    public void onResume(){
        super.onResume();
        posGet = new PosGetter(getApplicationContext());
        posGet.setOnGPSListener(new PosGetter.OnGPSListener() {
            @Override
            public void onGet(Location location, String address) {
            }

            @Override
            public void onChg(Location location, String address) {
                posGet = null;
            }
        });
        t1 = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    while(true) {
                        Log.d("find_pos??", "find......");
                        if (Static_var.location != null) {
                            mHandler.sendEmptyMessage(state.GET_ADDRESS.ordinal());
                            t1.interrupt();
                            t1=null;
                        }
                        Thread.sleep(1000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t1.start();

        if(isGPSNetworkon()) {
            address_txt.setText("現在位址:尚未定位完成");
        }else if(isGPSon()){
            address_txt.setText("現在位址:請開啟網路定位");
        }else{
            address_txt.setText("現在位址:定位尚未開啟");
        }
    }

    @Override
    public void onPause(){
        posGet = null;
        super.onPause();
    }

    public void setAddress(String address){

        HashMap paramList = new HashMap();
        paramList.put("table","getAdvertisement");
        paramList.put("type","1");

        HashMap paramHalf = new HashMap();
        paramHalf.put("table","getAdvertisement");
        paramHalf.put("type","0");

        Log.d("address",address+"");
        if(address!=null && !address.isEmpty() && address.length()>5){

            address_txt.setText("現在位址:" + address.substring(5));

            paramList.put("address",address);
            paramHalf.put("address",address);

        }else{

            if(Static_var.location!=null){
                address_txt.setText("現在經緯度:"+Static_var.location.getLatitude()+","+Static_var.location.getLongitude());
            }else{
                address_txt.setText("現在位址:很抱歉，無法取得您現在的地址");
            }

        }

        new DBConnector(mHandler, paramList, Static_var.GET_LIST_AD);
        new DBConnector(mHandler, paramHalf, Static_var.GET_HALF_AD);
    }

    RecyclerView recyclerView;
    catesubRAdapter cRAdapter ;
    int pre_position = 0;

    private void catesub_init(){

        int len = Static_var.STORE_TYPE.length;
        for(int index=0;index<len;index++){
            if( Static_var.search_type == Static_var.STORE_TYPE[index] ){
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


        LinearLayoutManager layoutManager;
        recyclerView = (RecyclerView)findViewById(R.id.catesub_recycler);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(-10,-15,0,3));
        recyclerView.setHasFixedSize(true);
        cRAdapter = new catesubRAdapter(images);
        recyclerView.setAdapter(cRAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addOnItemTouchListener(
            new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                @Override public void onItemClick(View view, int position) {
                    // do whatever
                    Static_var.search_type = Static_var.STORE_TYPE[position];
                    mTabHost.setCurrentTab(0);
                    adapter = null;
                    adapter = new TabsAdapter(store_search_list.this, mTabHost, mViewPager);
                    for (int i = 0; i < mFragments.length; i++) {
                        adapter.addTab(mFragments[i], null);
                    }

                    images.remove(pre_position);
                    data_model pre_data = new data_model();
                    pre_data.setImage(a_image[pre_position]);
                    images.add(pre_position, pre_data);

                    images.remove(position);
                    data_model data = new data_model();
                    data.setImage(a_image_b[position]);
                    images.add(position, data);

                    cRAdapter.notifyDataSetChanged();
                    pre_position = position;
                }
            })
        );
    }



    TabsAdapter adapter;
    private void init() {
        mTabHost.setup(this, getSupportFragmentManager());
        adapter = new TabsAdapter(this, mTabHost, mViewPager);
        for (int i = 0; i < mFragments.length; i++) {
            TabSpec tabSpec = mTabHost.newTabSpec((i + 1) + "").setIndicator(
                    mIndicator[i] + "");
            // 将Tab按钮添加进Tab选项卡中
            mTabHost.addTab(tabSpec, mFragments[i], null);
            TextView tv = (TextView) mTabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTextSize(16);
            adapter.addTab(mFragments[i], null);
        }

        mTabHost.getTabWidget().getChildAt(1).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTabHost.getTabWidget().getChildAt(1)
                        .isSelected()) {
                    resetManuallyFragment();
                    mViewPager.setCurrentItem(1);
                }
                mTabHost.getTabWidget().setCurrentTab(1);
                mTabHost.setCurrentTab(1);

            }
        });
    }

    private void resetManuallyFragment() {
        adapter = null;
        adapter = new TabsAdapter(store_search_list.this, mTabHost, mViewPager);
        for (int i = 0; i < mFragments.length; i++) {
            adapter.addTab(mFragments[i], null);
        }
        //adapter.notifyDataSetChanged();
        //mViewPager.setCurrentItem(0);
        //adapter.replaceTab(mFragments[1],null,1);
        //adapter.addTab(mFragments[1], null);
        //adapter.notifyDataSetChanged();

    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if( msg.what == state.GET_ADDRESS.ordinal() ){
                Log.d("location??",Static_var.location.toString());
                Log.d("locationaddress??",Static_var.getAddressByLocation(getApplicationContext(), Static_var.location));
                Log.d("locationlat??",Static_var.location.getLatitude()+"");
                Log.d("locationlat??",Static_var.location.getLatitude()+"");
                Log.d("locationlng??",Static_var.location.getLongitude()+"");
                setAddress(Static_var.getAddressByLocation(getApplicationContext(),Static_var.location));
            }else {
                String result = "";
                if (msg.obj instanceof String) ;
                result = (String) msg.obj;
                try {
                    switch (msg.what) {
                        case Static_var.GET_LIST_AD:
                            if (!result.trim().equals("[]") && !result.equals("false")) {
                                Static_var.listAD = new ADBL().analysisListAD(result);
                            }
                            break;
                        case Static_var.GET_HALF_AD:
                            if (!result.trim().equals("[]") && !result.equals("false")) {
                                Static_var.halfAD = new ADBL().analysisHalfAD(result);
                            }
                            break;
                    }
                } catch (Exception e) {
                    Log.e("store_search_error", e.getMessage());
                }
            }
        }
    };

    public static class TabsAdapter extends FragmentStatePagerAdapter implements
            OnPageChangeListener, OnTabChangeListener {

        private final Context mContext;
        private final ViewPager mViewPager;
        private final FragmentTabHost mTabHost;
        private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();

        static final class TabInfo {//我们可以利用一个bundle对象来保存Fragment中的数据
            private final Class<?> clss;
            private final Bundle args;

            TabInfo(Class<?> _class, Bundle _args) {
                clss = _class;
                args = _args;
            }
        }

        public TabsAdapter(FragmentActivity activity, FragmentTabHost tabHost,
                           ViewPager pager) {
            super(activity.getSupportFragmentManager());
            mContext = activity;
            mViewPager = pager;
            mTabHost = tabHost;
            mViewPager.setAdapter(this);
            mViewPager.addOnPageChangeListener(this);
            mViewPager.setOffscreenPageLimit(2);
            mTabHost.setOnTabChangedListener(this);

        }


        //当TabHost添加标签页后也需要调用此方法以保持和ViewPager的一致
        public void addTab(Class<?> clss, Bundle args) {
            TabInfo info = new TabInfo(clss, args);
            mTabs.add(info);
            notifyDataSetChanged();
        }

        public void replaceTab(Class<?> clss, Bundle args,int position){
            TabInfo info = new TabInfo(clss, args);
            //mViewPager.setAdapter(null);
            mTabs.remove(position);
            mTabs.add(position, info);
            //mViewPager.setAdapter(this);
            //mViewPager.addOnPageChangeListener(this);
            notifyDataSetChanged();
        }

        public void removeTab(int position){
            mTabs.remove(position);
            notifyDataSetChanged();
        }

        //设置TabHost改变后同时改变ViewPager
        @Override
        public void onTabChanged(String arg0) {
            // TODO Auto-generated method stub
            int position = mTabHost.getCurrentTab();
            for(int i=0; i<mTabHost.getTabWidget().getChildCount(); i++)
            {
                mTabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#00ffffff"));
            }
            //mTabHost.getTabWidget().setCurrentTab(position);
            mTabHost.getTabWidget().getChildAt(position).setBackgroundColor(Color.parseColor("#ffbcc6d2"));//bcc6d2//e2eeff
            //mTabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#00000000"));
            mViewPager.setCurrentItem(position);
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub
        }

        //当ViewPager被滑动后也要改变TabHost
        @Override
        public void onPageSelected(int position) {
            // TODO Auto-generated method stub
            mTabHost.setCurrentTab(position);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mTabs.size();
        }

        @Override
        public Fragment getItem(int position) {
            TabInfo info = mTabs.get(position);
            return Fragment.instantiate(mContext, info.clss.getName(),
                    info.args);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //重载该方法，防止其它视图被销毁，防止加载视图卡顿
            //super.destroyItem(container, position, object);
        }
        @Override
        public int getItemPosition(Object object){
            return PagerAdapter.POSITION_NONE;
        }
    }

    private String getAddressByLocation(Location location) {
        String returnAddress = "";
        try {
            if (location != null) {

                if (!Geocoder.isPresent()){ //Since: API Level 9
                    return "";
                }

                Double longitude = location.getLongitude();        //取得經度
                Double latitude = location.getLatitude();        //取得緯度

                Geocoder gc = new Geocoder(getApplicationContext(), Locale.TRADITIONAL_CHINESE);        //地區:台灣
                //自經緯度取得地址
                List<Address> lstAddress = gc.getFromLocation(latitude, longitude, 1);

                returnAddress = lstAddress.get(0).getAddressLine(0);
            }
            else
            {
                return "";
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return returnAddress;
    }

}
