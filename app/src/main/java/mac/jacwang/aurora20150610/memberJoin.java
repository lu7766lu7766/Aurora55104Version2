package mac.jacwang.aurora20150610;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.HashMap;

import mac.jacwang.aurora20150610.Comm.DBConnector;
import mac.jacwang.aurora20150610.Comm.EndCallListener;
import mac.jacwang.aurora20150610.Comm.Static_var;
import mac.jacwang.aurora20150610.DataAnalysis.resultDA;


public class memberJoin extends mActionBarActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_join);
        setTitle("55104查號台");

        init();
    }

    EditText cellphone_txt;//,passwd_txt,promote_code_txt;
    ImageButton sign_btn;//chk_btn,;

    private void init(){
        cellphone_txt = (EditText)findViewById(R.id.cellphone);

        sign_btn = (ImageButton)findViewById(R.id.sign);

        sign_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!cellphone_txt.getText().toString().isEmpty())
                {
                    settings.edit().putString(Static_var.phone, cellphone_txt.getText().toString()).commit();
                    defaultCall("55104,1");
                    TelephonyManager mTM = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                    mTM.listen(new EndCallListener() {
                        @Override
                        public void onEndCall() {
                            String phone = settings.getString(Static_var.phone, "");
                            if( !phone.isEmpty() ) {
                                cellphone_txt.setText(phone);
                                HashMap<String, String> params = new HashMap<String, String>();
                                params.put("table", "applyChk");
                                params.put("phone", phone);
                                new DBConnector(mHandler, params, Static_var.MEMBER_APPLY_CHK);
                            }
                        }
                    }, PhoneStateListener.LISTEN_CALL_STATE);
//                    HashMap<String,String> params = new HashMap<String, String>();
//                    params.put("table","memberJoin");
//                    params.put("phone",cellphone_txt.getText().toString());
//                    new DBConnector(mHandler,params,Static_var.MEMBER_JOIN);
                }

            }
        });

        cellphone_txt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (cellphone_txt.getText().length() == 10) {
                    sign_btn.setEnabled(true);
                } else {
                    sign_btn.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        sign_btn.setEnabled(false);
    }

    @Override
    public void onResume(){
        super.onResume();

    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            String result = "";
            Intent intent;
            if (msg.obj instanceof String)
                result = (String) msg.obj;
            result = resultDA.getResult(result);
            switch (msg.what) {

                case Static_var.MEMBER_APPLY_CHK:
                    if(result.equals("success")) {
                        HashMap<String,String> params = new HashMap<String, String>();
                        params.put("table","memberJoin");
                        params.put("phone",cellphone_txt.getText().toString());
                        new DBConnector(mHandler,params,Static_var.MEMBER_JOIN);
                    }else if( result.equals(("member_exists")) ){
                        settings.edit().putString(Static_var.login, "1").commit();
                        intent = new Intent();
                        intent.setClass(memberJoin.this,memberShip.class);
                        startActivity(intent);
                        finish();
                    }else{
                        if(!settings.getString(Static_var.phone, "").isEmpty()){
                            //Toast.makeText(getApplicationContext(),"請輸入正確的號碼",Toast.LENGTH_SHORT).show();
                            new AlertDialog.Builder(memberJoin.this, AlertDialog.THEME_HOLO_DARK)
                                    .setTitle("溫馨提示")
                                    .setMessage("您所輸入的號碼與實際號碼不符!\n"+
                                                "請再次確認號碼的正確性\n"+
                                                "或 聯絡客服")
                                    .setCancelable(false)
                                    .setNegativeButton("確定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    })
                                    .show();
                        }
                    }
                    break;

                case Static_var.MEMBER_JOIN:
                    switch (result){
                        case "intro_is_youself":
                            Toast.makeText(getApplicationContext(),"介紹人不能是自己",Toast.LENGTH_SHORT).show();
                            break;
                        case "phone_is_empty":
                            Toast.makeText(getApplicationContext(),"號碼不能是空白",Toast.LENGTH_SHORT).show();
                            break;
                        case "unapply":
                            Toast.makeText(getApplicationContext(),"號碼尚未驗證",Toast.LENGTH_SHORT).show();
                            break;
                        case "success":
                            settings.edit().putString(Static_var.login, "1").commit();
                            intent = new Intent();
                            intent.setClass(memberJoin.this,memberJoinSuccess.class);
                            startActivity(intent);
                            finish();
                            break;
                    }
                    break;
            }
        }
    };
}
