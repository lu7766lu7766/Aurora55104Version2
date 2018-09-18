package mac.jacwang.aurora20150610;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import mac.jacwang.aurora20150610.Comm.DBConnector;
import mac.jacwang.aurora20150610.Comm.Static_var;
import mac.jacwang.aurora20150610.DataAnalysis.resultDA;
import mac.jacwang.aurora20150610.DataAnalysis.storeBL;
import mac.jacwang.aurora20150610.DataAnalysis.t_store;


public class mActionBarActivity extends ActionBarActivity implements OnClickListener {

    public  String TAG = "Jac Debug";
    private float screenWidth,screenHeight;
    public boolean is_GPS_on;
    private MenuItem GPSitem = null;
//    private MenuItem Infoitem = null;
    public SharedPreferences settings;
    LocationManager service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.default_layout);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;

        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.action_bar);
        ImageButton back_btn = (ImageButton)findViewById(R.id.back_btn);
        back_btn.setOnClickListener(this);
        ImageButton home_btn = (ImageButton)findViewById(R.id.home_btn);
        home_btn.setOnClickListener(this);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        service = (LocationManager) getSystemService(LOCATION_SERVICE);


//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_icon);
        btnInit();
        dataInit();
    }

    List<PackageInfo> packs;

    private void dataInit(){
        settings = getSharedPreferences(Static_var.data,0);

        if (Static_var.favorite == null && !settings.getString(Static_var.phone, "").isEmpty()){
            HashMap<String,String> params = new HashMap<String,String>();
            params.put("table", "getFavorite");
            params.put("phone",settings.getString(Static_var.phone, ""));
            new DBConnector(mHandler, params, Static_var.GET_FAVORITE);
        }

//        settings.edit().putString(Static_var.callPackage,null).commit();
        Static_var.defaultPhonePackage = settings.getString(Static_var.callPackage,"");
//        Log.d("package",Static_var.defaultPhonePackage);
        if(Static_var.defaultPhonePackage.isEmpty()||Static_var.defaultPhonePackage==null) {
            packs = getPackageManager().getInstalledPackages(0);
            String[] defaultCall = {
                    "com.android.server.telecom","com.android.phone"
            };
            for(int i = 0; i<defaultCall.length; i++){
                if(isIntentAvailable(defaultCall[i])){
                    Static_var.defaultPhonePackage=defaultCall[i];
//                    Log.d("package",Static_var.defaultPhonePackage);
                    break;
                }
//                Log.d("package_while",defaultCall[i]);
            }
        }
    }

    private boolean isIntentAvailable(String action) {
        for (int i = 0; i < packs.size(); ++i) {
            final String packageName = packs.get(i).packageName;
            //Log.d("package_name",packageName);
            if (packageName != null && packageName.startsWith(action)) {
                return true;//isDefaultPhone = true;
            }
        }
        return false;
    }

    public String defaultCall(String phone){
        phone = phone.replace("-","").replace("(","").replace("_","");
        Intent intent= new Intent("android.intent.action.CALL",Uri.parse("tel:"+phone));

        try {
            intent.setPackage(Static_var.defaultPhonePackage);
            startActivity(intent);
        }catch(Exception e){
            for(int i=0;i<packs.size();i++){
                try {
                    Static_var.defaultPhonePackage = packs.get(i).packageName;
                    intent.setPackage(Static_var.defaultPhonePackage);
                    startActivity(intent);
//                    Log.d("package", Static_var.defaultPhonePackage);
                    settings.edit().putString(Static_var.callPackage,Static_var.defaultPhonePackage).commit();
                    return "";
                }catch(Exception ee){
                }
            }
            intent= new Intent("android.intent.action.CALL",Uri.parse("tel:"+phone));
            startActivity(intent);
            Toast.makeText(getApplicationContext(),"請用預設程式撥打",Toast.LENGTH_SHORT).show();

        }return "";
    }

    public Class getThisClass(){
        return getClass();
    }

    private void btnInit(){
        ImageButton info = (ImageButton)findViewById(R.id.little_btn_info);
        info.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(mActionBarActivity.this)
                        .setTitle("55014查號台 版權聲明")
                        .setMessage(
                                "1.本程式資料為依據店家公開之地址電話及官網資訊取得,不保證其資料之正確與完整。\n" +
                                "2.有關本程式使用之商標圖片,智財權皆屬該公司所有,本程式無侵害智財權之意圖。\n" +
                                "3.本程式內提供之各店家相關網站與連結資訊,使用者可能因本程式連結至外部業者經營之網頁,不代表本程式與其業者有往來關係。\n" +
                                "4.本程式內之商標,智財權及其他權利,屬相關權利人所有,本程式不授權予使用者。\n" +
                                "5.有關本程式內之店家資訊,若發現錯誤或圖片、商標等不當使用之問題,請撥55104#9告知客服人員處理。\n" +
                                "6.有關本程式之版面設計與相關圖案，智財權皆屬本公司所有。\n" +
                                "7.本程式內購買項目恕不提供退費。\n" +
                                "8.本程式將匿名收集使用者的位置以及使用資訊進行相關研究及開發。\n" +
                                "\n" +
                                "用戶許可協議：\n" +
                                "\n" +
                                "1.若客戶為個人用戶,本程式使用授權僅屬個人使用。\n" +
                                "2.請勿在不屬於你的手機上安裝本程式 請勿使用反編譯、分解、破譯、解壓本程式或對本程式進行其他逆向工程。\n" +
                                "3.不得轉售本程式或未經授權販售予第三方。\n" +
                                "3.本程式以授權形式進行發售，無出售程式之行為，使用人之使用許可權不含程式的所有權。\n" +
                                "\n" +
                                "法律問題.\n" +
                                "因不當使用本程式衍生之一切法律問題均由使用者自行承擔")
                        .show();
            }
        });

        ImageButton service_btn = (ImageButton)findViewById(R.id.little_btn_service);
        service_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                defaultCall("55104,9");
            }
        });

        ImageButton share_btn = (ImageButton)findViewById(R.id.little_btn_share);
        share_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "55104查號台\n"+
                        "叫外送，叫計程車，訂餐廳，訂民宿的神兵利器\n"+
                        "Android:\n"+
                        "https://play.google.com/store/apps/details?id=mac.jacwang.aurora20150610&hl=zh_TW\n"+
                        "Iphone:\n"+
                        "https://itunes.apple.com/tw/app/55104cha-hao-tai/id998083729?l=zh&mt=8";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "55104查號台");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share 55104"));
            }
        });

        ImageButton member_ship_btn = (ImageButton)findViewById(R.id.little_btn_member_ship);
        member_ship_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                String classname = mActionBarActivity.this.getLocalClassName();

                if(!classname.equals("memberShip")){
                    Intent intent = new Intent();
                    String login = settings.getString(Static_var.login, "");
                    Class target = login.equals("1") ? memberShip.class : memberJoin.class;
                    intent.setClass(mActionBarActivity.this,target);
                    startActivity(intent);
                }else{
                    onResume();
                }
            }
        });

        ImageButton favorite = (ImageButton)findViewById(R.id.little_btn_favorite);
        favorite.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(),"功能尚在建置中",Toast.LENGTH_SHORT).show();
                if(getThisClass()!=myFavorite.class) {
                    Intent intent = new Intent();
                    intent.setClass(mActionBarActivity.this, myFavorite.class);
                    startActivity(intent);
                }
            }
        });

        ImageButton promote = (ImageButton)findViewById(R.id.little_btn_promote);
        promote.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(), "功能尚在建置中", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(mActionBarActivity.this,promote_list.class);
                startActivity(intent);
            }
        });
    }

    public void setTitle(String title){
        TextView tv = (TextView)findViewById(R.id.action_bar_title);
        tv.setText(title);
    }

    @Override
    public void onResume(){

        if(GPSitem!=null){
            setGPSIcon();
        }
        super.onResume();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        GPSitem = menu.getItem(0);
        setGPSIcon();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.GPSItem:
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                break;
//            case R.id.AboutMe:
//                Toast.makeText(getApplicationContext(),"About me!!",Toast.LENGTH_SHORT).show();
//                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setContentView(int id){

        RelativeLayout content = (RelativeLayout) findViewById(R.id.content);
        LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(id, null);
        content.addView(v);
    }

    public void setContentView(View v){

        RelativeLayout content = (RelativeLayout) findViewById(R.id.content);
        content.addView(v);
    }

    static boolean isAlert = false;

    private void setGPSIcon(){
        is_GPS_on = isGPSon()||isGPSNetworkon();
        if( !is_GPS_on && !isAlert ) {
            new AlertDialog.Builder(this)
                    .setTitle("您尚未開啟ＧＰＳ")
                    .setMessage("為避免您權益損失，請問是否開啟？")
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton("取消", null)
                    .show();
            isAlert = true;
        }
        GPSitem.setIcon(is_GPS_on?R.drawable.gps_on:R.drawable.gps_off);
    }

    public boolean isGPSon(){

        return service.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public boolean isGPSNetworkon(){
        return service.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public float getScreenWidth(){
        return screenWidth;
    }

    public float getScreenHeight(){
        return screenHeight;
    }

    @Override
    public void onClick(View v) {
        switch( v.getId() ){
            case R.id.back_btn:
                onBackPressed();
            break;
            case R.id.home_btn:
                Intent it = new Intent(this,menu.class);
                startActivity(it);
            break;
        }
    }
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            String result = "";
            if (msg.obj instanceof String)
                result = (String) msg.obj;

            switch (msg.what) {
                case Static_var.GET_FAVORITE:
                    Static_var.favoriteId = new ArrayList<>();
                    Static_var.favorite = new storeBL().analysisStoreList(result);
                    for (t_store item: Static_var.favorite) {
                        Static_var.favoriteId.add(item.getFiId());
                    }
                    break;
            }
        }
    };

}
