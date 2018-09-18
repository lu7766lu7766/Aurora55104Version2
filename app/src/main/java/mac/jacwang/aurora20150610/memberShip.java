package mac.jacwang.aurora20150610;

import android.content.Intent;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;
import com.google.android.gms.iid.InstanceID;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import mac.jacwang.aurora20150610.Comm.DBConnector;
import mac.jacwang.aurora20150610.Comm.Static_var;
import mac.jacwang.aurora20150610.DataAnalysis.introAddDA;
import mac.jacwang.aurora20150610.DataAnalysis.memberDA;
import mac.jacwang.aurora20150610.DataAnalysis.resultDA;


public class memberShip extends mActionBarActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_ship);
        setTitle("會員專區");
        bonus = (TextView)findViewById(R.id.bonus);
        view_init();
        init();
        new GCMTask().execute();
    }

    EditText intro_promote_code_edit;
    TextView intro_promote_code, bonus;
    ImageButton write_in;
    private void view_init(){
        intro_promote_code_edit = (EditText)findViewById(R.id.intro_promote_code_edit);
        intro_promote_code = (TextView)findViewById(R.id.intro_promote_code);
        write_in = (ImageButton)findViewById(R.id.write_in);
        intro_promote_code_edit.setVisibility(View.GONE);
        intro_promote_code.setVisibility(View.GONE);
        write_in.setVisibility(View.GONE);
    }

    String phone;
    private void init(){

        ImageButton write_in = (ImageButton)findViewById(R.id.write_in);
        write_in.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String,String> params = new HashMap<String, String>();
                params.put("table","introducerAdd");
                params.put("phone",phone);
                params.put("introducer_phone",intro_promote_code_edit.getText().toString());
                new DBConnector(mHandler,params,Static_var.MEMBER_INTRO_ADD);
            }
        });

        ImageButton recommend_store = (ImageButton)findViewById(R.id.recommend_store);
        recommend_store.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(), "功能尚在建置中", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(memberShip.this,recommendStore.class);
                startActivity(intent);
            }
        });

        ImageButton exchg_present = (ImageButton)findViewById(R.id.exchg_present);
        exchg_present.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(), "功能尚在建置中", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(memberShip.this,promote_list.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        phone = settings.getString(Static_var.phone, "");
        TextView cellphone = (TextView)findViewById(R.id.cellphone);
        cellphone.setText(phone);
        HashMap<String,String> params = new HashMap<String, String>();
        params.put("table","getMemberInfo");
        params.put("phone",phone);
        new DBConnector(mHandler,params,Static_var.MEMBER_INFO);
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            String result = "";
            if (msg.obj instanceof String)
                result = (String) msg.obj;
            switch (msg.what) {
                case Static_var.MEMBER_INFO:

                    HashMap<String,String> member_info= memberDA.getResult(result);

                    TextView promote_code = (TextView)findViewById(R.id.promote_code);
                    promote_code.setText(member_info.get("promote_code"));

                    TextView dail_in_times = (TextView)findViewById(R.id.dail_in_times);
                    dail_in_times.setText(member_info.get("dail"));

                    TextView intor_times = (TextView)findViewById(R.id.intor_times);
                    intor_times.setText(member_info.get("intor_times"));

                    bonus.setText(member_info.get("bonus"));

                    view_init();
                    if( member_info.get("intro_promote_code").equals("null")){
                        intro_promote_code_edit.setVisibility(View.VISIBLE);
                        write_in.setVisibility(View.VISIBLE);
                    }else{
                        intro_promote_code.setText(member_info.get("intro_promote_code"));
                        intro_promote_code.setVisibility(View.VISIBLE);
                    }

                    break;
                case Static_var.MEMBER_INTRO_ADD:
                    HashMap<String,String> map = new HashMap<>();
                    map = introAddDA.getResult(result);
                    result = map.get("result");
                    int add_bonus = Integer.parseInt(map.get("add_bonus"));
                    switch (result){
                        case "intro_is_youself":
                            Toast.makeText(getApplicationContext(),"介紹人不能是自己",Toast.LENGTH_SHORT).show();
                            break;
                        case "phone_is_empty":
                            Toast.makeText(getApplicationContext(),"號碼不能是空白",Toast.LENGTH_SHORT).show();
                            break;
                        case "intro_not_exists":
                            Toast.makeText(getApplicationContext(),"介紹人不存在",Toast.LENGTH_SHORT).show();
                            break;
                        case "intro_is_empty":
                            Toast.makeText(getApplicationContext(),"介紹人不能是空白",Toast.LENGTH_SHORT).show();
                            break;
                        case "success":
                            String now_bonus = bonus.getText().toString();
                            bonus.setText(Integer.parseInt(now_bonus)+add_bonus+"");

                            view_init();
                            intro_promote_code.setText(intro_promote_code_edit.getText().toString().toUpperCase());
                            intro_promote_code.setVisibility(View.VISIBLE);
                            Toast.makeText(getApplicationContext(),"成功推薦介紹人",Toast.LENGTH_SHORT).show();
                           break;
                    }
                    break;
            }
        }
    };

    private class GCMTask extends AsyncTask<Void, Void, Void>
    {
        protected Void doInBackground(Void... params)
        {
//            Log.d(TAG, "檢查裝置是否支援 GCM");
//            // 檢查裝置是否支援 GCM
//            GCMRegistrar.checkDevice(memberShip.this);
//            GCMRegistrar.checkManifest(memberShip.this);
//            final String regId = GCMRegistrar.getRegistrationId(memberShip.this);
//            if (regId.equals(""))
//            {
//                Log.d(TAG, "尚未註冊 Google GCM, 進行註冊");
//                GCMRegistrar.register(memberShip.this, Static_var.SENDER_ID);
//            }
            MagicLenGCM mGMC = new MagicLenGCM(memberShip.this, new MagicLenGCM.MagicLenGCMListener() {
                @Override
                public void gcmRegistered(boolean successfull, String regID) {
                    Log.i(TAG,"successfull_regId:"+regID);
                }

                @Override
                public boolean gcmSendRegistrationIdToAppServer(String regID) {
                    Log.i(TAG,"gcmSendRegistrationIdToAppServer_regId:"+regID);
                    return false;
                }
            });

            mGMC.startGCM();
            //Log.i(TAG, "successfull_regId:" + mGMC.getRegistrationId());



            return null;
        }
    }
}
