package mac.jacwang.aurora20150610;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mac.jacwang.aurora20150610.Comm.DBConnector;
import mac.jacwang.aurora20150610.Comm.Static_var;
import mac.jacwang.aurora20150610.DataAnalysis.data_model;
import mac.jacwang.aurora20150610.DataAnalysis.resultDA;
import mac.jacwang.aurora20150610.DataAnalysis.t_store;

public class store_info extends mActionBarActivity implements View.OnClickListener  {

    public enum state{getFavorite,getHalfAD,chgHalfAD}

    private static final String TAG = Static_var.TAG;
    t_store store_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_info);

        setTitle("店家資訊");

        init();
    }

    boolean isInFavorite = false;
    ImageView favorite ;
    int fi_id;

    private void init(){

        Intent intent = this.getIntent();
        store_info = (t_store)intent.getSerializableExtra("store");
        fi_id = store_info.getFiId();
        favorite = (ImageView)findViewById(R.id.favorite);


        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    while(true) {
                        if (Static_var.favoriteId != null) {
                            mHandler.obtainMessage(state.getFavorite.ordinal()).sendToTarget();
                            break;
                        }
                        Thread.sleep(1000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        ImageView menu_img = (ImageView)findViewById(R.id.menu);
        menu_img.setOnClickListener(this);



        ImageView call_img = (ImageView)findViewById(R.id.call);
        call_img.setOnClickListener(this);

        TextView brand_name_txt = (TextView)findViewById(R.id.brand_name);
        brand_name_txt.setText(store_info.getBrandName());

        TextView address_txt = (TextView)findViewById(R.id.address);
        address_txt.setText(store_info.getAddress());


        TextView subname_txt = (TextView)findViewById(R.id.sub_name);
        subname_txt.setText(store_info.getSubName());

        ImageView logo_img = (ImageView)findViewById(R.id.logo);
        Picasso.with(logo_img.getContext())
                .load(store_info.getLogo())
                .placeholder(R.drawable.no_logo)
                .into(logo_img);

        ImageView level_img = (ImageView)findViewById(R.id.level);
        int level = store_info.getLevel();
        int level_res = R.drawable.star0;
        switch (level){
            case 1:
                level_res = R.drawable.star1;
                break;
            case 2:
                level_res = R.drawable.star2;
                break;
            case 3:
                level_res = R.drawable.star3;
                break;
            case 4:
                level_res = R.drawable.star4;
                break;
            case 5:
                level_res = R.drawable.star5;
                break;
        }
        Picasso.with(getApplicationContext())
                .load(level_res)
                .into(level_img);

        TextView distance_val = (TextView)findViewById(R.id.distance_val);
        TextView distance_unit = (TextView)findViewById(R.id.distance_unit);

        Double distance = store_info.getDistance();
        if(distance==0){//手動查詢
            distance_val.setText("");
            distance_unit.setText("");
        }else{//附近查詢
            if(distance>1000){
                distance_val.setText(Math.floor(store_info.getDistance()/100)/10+"" );
                distance_unit.setText("km");
            }else{
                distance_val.setText(store_info.getDistance()+"");
                distance_unit.setText("m");
            }
        }

        TextView specail_info = (TextView)findViewById(R.id.special_info);
        if(store_info.getSpecialInfo().isEmpty()){
            specail_info.setVisibility(View.GONE);
        }
        specail_info.setText(store_info.getSpecialInfo());

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    if(Static_var.halfAD!=null){
                        mHandler.obtainMessage(state.getHalfAD.ordinal()).sendToTarget();
                        break;
                    }
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }).start();
    }

    ViewPager store_info_ad;
    List<data_model> halfAD;
    int wait_time = 5000,aslistLimit=5;
    private boolean isADTouch = false,isADInit=false;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String result = "";
            if (msg.obj instanceof String)
                result = (String) msg.obj;

            if(msg.what == state.getFavorite.ordinal() ){
                for (int id : Static_var.favoriteId) {
                    if (fi_id == id) {
                        isInFavorite = true;
                        Picasso.with(getApplicationContext())
                                .load(R.drawable.store_info_add_favorite_yes)
                                .into(favorite);
                        break;
                    }
                }
                favorite.setOnClickListener(store_info.this);

            }else if( msg.what == state.getHalfAD.ordinal() ){
                halfAD = Static_var.halfAD;

                store_info_ad = (ViewPager)findViewById(R.id.store_info_ad);
                List<ImageView> adlist = new ArrayList<ImageView>();

                for(int i=0; i<aslistLimit; i++){
                    if(i==halfAD.size())
                        break;
                    ImageView ad = new ImageView(getApplicationContext());
                    data_model ad_item = halfAD.get(i%halfAD.size());

                    ad.setLayoutParams(new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
                    ad.setId(i);
                    ad.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            switch (motionEvent.getAction()){
                                case MotionEvent.ACTION_DOWN:
                                    isADTouch=true;
                                    break;
                            }
                            return false;
                        }
                    });
                    if(!ad_item.getURL().isEmpty()) {
                        ad.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Uri uri = Uri.parse(halfAD.get(view.getId() % halfAD.size()).getURL());
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(intent);
                            }
                        });
                    }
                    Picasso.with(getApplicationContext())
                            .load(Static_var.ADVERTISEMENT_PATH + halfAD.get(i).getImageURL())
                            .into(ad);
                    adlist.add(ad);
                }

                store_info_ad.setAdapter(new adAdapter(adlist));

                Runnable nextAD = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            while(true) {
                                mHandler.obtainMessage(state.chgHalfAD.ordinal(), (store_info_ad.getCurrentItem() + 1) % aslistLimit).sendToTarget();
                                Thread.sleep(wait_time);
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };
                Thread t2 = new Thread(nextAD);
                t2.start();
                store_info_ad.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                        isADTouch = isADInit ? true : false;
                        isADInit = true;
                    }

                    @Override
                    public void onPageSelected(int position) {
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {
                        isADTouch = false;
                    }
                });
            }else if( msg.what == state.chgHalfAD.ordinal() ){
                if(!isADTouch)
                    store_info_ad.setCurrentItem((int) msg.obj);
            }


            switch (msg.what) {//DB回來
                case Static_var.ADD_FAVORITE:
                    result = resultDA.getResult(result);
                    if(result.equals("success")){
                        Static_var.favoriteId.add(fi_id);
                        Static_var.favorite.add(store_info);
                        Toast.makeText(getApplicationContext(),"已加入最愛",Toast.LENGTH_SHORT).show();
                        Log.d("favoritelist",Static_var.favoriteId.toString());
                    }
                    break;

                case Static_var.DEL_FAVORITE:
                    result = resultDA.getResult(result);
                    if(result.equals("success")){
                        int len = Static_var.favoriteId.size();
                        for(int index=0;index<len;index++){
                            if(Static_var.favoriteId.get(index)==fi_id){
                                Static_var.favoriteId.remove(index);
                                Static_var.favorite.remove(index);
                                break;
                            }
                        }
                    }
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        super.onClick(v);
        Intent intent;
        Uri uri;
        switch(v.getId()){
            case R.id.menu:
                String menu = store_info.getMenu();

                if(menu.isEmpty()){
                    Toast.makeText(getApplicationContext(),"本店尚未有Menu，敬請見諒",Toast.LENGTH_SHORT).show();
                }else{
                    intent = new Intent();
                    intent.setClass(store_info.this,web_img.class);
                    intent.putExtra("brand_menu",menu);
                    startActivity(intent);
                }

                break;
            case R.id.call:
                String phone = store_info.getPhone();
                phone = phone.replace('#',',');
                defaultCall(phone);
                break;
//            case R.id.back_btn:
//                onBackPressed();
//                break;
            case R.id.favorite:
                String myPhone = settings.getString(Static_var.phone, "");
                Log.d("favoriteClick",myPhone);
                if(myPhone.isEmpty()){//未登入會未申請
                    intent = new Intent();
                    intent.setClass(store_info.this,memberJoin.class);
                    startActivity(intent);
                }else{
                    if(isInFavorite){
                        HashMap<String, String> params = new HashMap<String, String>();
                        params.put("table", "favoriteRemove");
                        params.put("phone", myPhone);
                        params.put("store_id", store_info.getFiId() + "");
                        new DBConnector(mHandler, params, Static_var.DEL_FAVORITE);
                        Picasso.with(getApplicationContext())
                                .load(R.drawable.store_info_add_favorite_yet)
                                .into(favorite);
                        isInFavorite = false;
                    }else {
                        HashMap<String, String> params = new HashMap<String, String>();
                        params.put("table", "favoriteJoin");
                        params.put("phone", myPhone);
                        params.put("store_id", store_info.getFiId() + "");
                        new DBConnector(mHandler, params, Static_var.ADD_FAVORITE);
                        Picasso.with(getApplicationContext())
                                .load(R.drawable.store_info_add_favorite_yes)
                                .into(favorite);
                        isInFavorite = true;
                    }
                }
                break;
        }
    }
    class adAdapter extends PagerAdapter {

        private List<ImageView> mListViews;

        public adAdapter(List<ImageView> mListViews) {
            this.mListViews = mListViews;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object)   {
            container.removeView((ImageView) object);
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = mListViews.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return  mListViews.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0==arg1;
        }
    }
}
