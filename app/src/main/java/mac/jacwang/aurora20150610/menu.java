package mac.jacwang.aurora20150610;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;
import android.view.View.OnClickListener;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import mac.jacwang.aurora20150610.Comm.DBConnector;
import mac.jacwang.aurora20150610.Comm.Static_var;
import mac.jacwang.aurora20150610.DataAnalysis.News4CellDA;
import mac.jacwang.aurora20150610.DataAnalysis.resultDA;

/**
 * Created by jac on 15/6/14.
 */
public class menu extends mActionBarActivity implements OnClickListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        new SqliteDownloader().execute();

        ImageButton store_search_btn = (ImageButton)findViewById(R.id.menu_store_search);
        store_search_btn.setOnClickListener(this);

        ImageButton member_ship_btn = (ImageButton)findViewById(R.id.menu_member_ship);
        member_ship_btn.setOnClickListener(this);

        ImageButton store_login_btn = (ImageButton)findViewById(R.id.menu_store_login);
        store_login_btn.setOnClickListener(this);

        ImageButton promote_btn = (ImageButton)findViewById(R.id.menu_promote);
        promote_btn.setOnClickListener(this);

        //load_news();
        checkVersion();
    }

    @Override
    public void onClick(View view){
        Intent intent;
        switch(view.getId()){
            case R.id.menu_store_search:
                intent = new Intent();
                intent.setClass(menu.this, store_search_category.class);
                startActivity(intent);
                break;
            case R.id.menu_member_ship:
//                Toast.makeText(getApplicationContext(),"功能尚在建置中",Toast.LENGTH_SHORT).show();
                intent = new Intent();
                String login = settings.getString(Static_var.login, "");
                Class target = login.equals("1") ? memberShip.class : memberJoin.class;
                intent.setClass(menu.this,target);
                startActivity(intent);
                break;
            case R.id.menu_store_login:
                //Toast.makeText(getApplicationContext(),"功能尚在建置中",Toast.LENGTH_SHORT).show();
                String site = "http://web.55104.com.tw";
                Intent ie = new Intent(Intent.ACTION_VIEW,Uri.parse(site));
                startActivity(ie);
//                Intent ie2 = new Intent();
//                ComponentName comp = new ComponentName("com.android.browser",
//                        "com.android.browser.BrowserActivity");
//                ie2.setComponent(comp);
//                ie2.setAction(Intent.ACTION_VIEW);
//                ie2.setData(Uri.parse(strURL2));
//                startActivity(ie2);
                break;
            case R.id.menu_promote:
//                Toast.makeText(getApplicationContext(),"功能尚在建置中",Toast.LENGTH_SHORT).show();
                intent = new Intent();
                intent.setClass(menu.this,promote_list.class);
                startActivity(intent);
                break;

        }
    }

    private void checkVersion(){
        int appVersion;
        PackageManager manager = this.getPackageManager();
        try { PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            appVersion = info.versionCode; //版本名
            HashMap<String,String> params = new HashMap<String, String>();
            params.put("table","chkAndroidVersion");
            params.put("version",appVersion+"");
            new DBConnector(mHandler,params,Static_var.VERSION_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void load_news(){
        HashMap<String,String> params = new HashMap<String, String>();
        params.put("table","getNews4Cell");
        new DBConnector(mHandler,params,Static_var.NEWS_DATA);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String result = "";
            if (msg.obj instanceof String)
                result = (String) msg.obj;

            switch (msg.what) {
                case Static_var.NEWS_DATA:
                    HashMap<Integer,HashMap<String,String>> news_list = News4CellDA.getResult(result);
                    int len = news_list.size();
                    if(len>0) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(menu.this, AlertDialog.THEME_HOLO_DARK)
                                .setTitle("公告").setNegativeButton("關閉", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                        ScrollView sv = new ScrollView(menu.this);
                        sv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                        LinearLayout ll = new LinearLayout(menu.this);
                        ll.setOrientation(LinearLayout.VERTICAL);
                        ll.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.TOP);
                        ll.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
                        sv.addView(ll);
                        int ww = (int)getScreenWidth()-50;
                        int hh = (int)getScreenHeight()-100;
                        for (int i = 0; i < len; i++) {
                            ImageView iv = new ImageView(menu.this);
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ww-100,hh-100);
                            iv.setLayoutParams(layoutParams);
                            iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
                            Picasso.with(getApplicationContext())
                                    .load(news_list.get(i).get("images"))
                                    .into(iv);
                            ll.addView(iv);
                        }
                        builder.setView(sv).show()
                                .getWindow()
                                .setLayout(ww, hh);

                    }
                    break;
                case Static_var.VERSION_DATA:
                    result = resultDA.getResult(result);
                    if(result.equals("success")){
                        new AlertDialog.Builder(menu.this, AlertDialog.THEME_HOLO_DARK)
                                .setTitle("更新")
                                .setMessage("已有更新的版本釋出，為避免您使用前亦損失，請前往更新！")
                                .setPositiveButton("前往更新",new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //String url = "https://play.google.com/store/apps/details?id=mac.jacwang.aurora20150610&hl=zh_TW";
                                        String sParam = "mac.jacwang.aurora20150610";
                                        Intent intent;
                                        try
                                        {
                                            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+sParam));
                                            startActivity(intent);
                                        }
                                        catch (android.content.ActivityNotFoundException anfe)
                                        {
                                            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id="+sParam));
                                            startActivity(intent);
                                        }
                                    }
                                } )
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        //
                                    }
                                })
                                .show();
                    }else{

                    }
                    break;
            }
        }
    };

    class SqliteDownloader extends AsyncTask<String, Void , String> {
        /*--- constructor ---*/

        public long sFileSize   = 0;
        public int read_size   = 0;
        public int total_size = 0;
        ProgressDialog progressDialog;
        int stopCommand=0;
        public String DB_DIR,DB_PATH;
        public static final String DATABASE_NAME = "sqlite.db";
        String sqlite_local_path = Static_var.APP_PATH + DATABASE_NAME;
        File fsqlite;

        public SqliteDownloader() {

            if(android.os.Build.VERSION.SDK_INT >= 17){
                DB_DIR = getApplicationContext().getApplicationInfo().dataDir + "/databases/";
            }
            else
            {
                DB_DIR = "/data/data/" + getApplicationContext().getPackageName() + "/databases/";
            }
            DB_PATH = DB_DIR+DATABASE_NAME;
        }

        @Override
        protected void onProgressUpdate(Void... values) {

            //此method是在呼叫publishProgress()時的才會trigger到的
            //可以當配progressBar來使用，這裡不多作說明。
            super.onProgressUpdate(values);

        }


        @Override
        protected void onPreExecute() {

            //前置，資料夾建置
            File app_dir = new File(Static_var.APP_PATH);
            if (!app_dir.isDirectory()) {
                app_dir.mkdirs();
            }
            File db_dir = new File(DB_DIR);
            if (!db_dir.isDirectory()) {
                db_dir.mkdirs();
            }
            File db_path = new File(DB_PATH);
            if (!db_path.exists()) {
                InputStream is = null;
                try {
                    is = getAssets().open(DATABASE_NAME);
                    String outFileName = db_path.getPath();
                    OutputStream os = new FileOutputStream(outFileName);
                    byte[] mBuffer = new byte[1024];
                    int mLength;
                    while ((mLength = is.read(mBuffer))>0)
                    {
                        os.write(mBuffer, 0, mLength);
                    }
                    os.flush();
                    os.close();
                    is.close();
                    Log.d("copyAssetsDB2DBpath",outFileName+"^success");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //此method是在執行doInBackground以前，才會呼叫的
            Log.d("main", "開始下載");

            fsqlite = new File(DB_PATH);
            sFileSize = fsqlite.length();//取得目前db檔案大小
            Log.d("db_size", sFileSize+" : byte ");
            progressDialog = new ProgressDialog(menu.this, AlertDialog.THEME_HOLO_DARK);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.setButton(ProgressDialog.BUTTON_POSITIVE, "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    stopCommand = 1;
                    progressDialog.cancel();
                }
            });
            progressDialog.setTitle("正在更新資料庫");
            progressDialog.setMessage("請稍候");
            progressDialog.setProgress(100);


            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            //此method是在doInBackground完成以後，才會呼叫的
            super.onPostExecute(result);
            //Log.d("main", "下載完成或取消下載");
            progressDialog.cancel();
            try {
                copyDB(sqlite_local_path,DB_PATH);
            } catch (IOException e) {
                e.printStackTrace();
            }
            load_news();
        }

        /*
        *注意!strUrlFile是一個array..代表著在呼叫此class時
        *可以帶入很多個參數(strUrlFile)，
        *如:new downloadTask().execute("http://www.xxx","http://www.xxx");
        */
        protected String doInBackground(String... strUrlFile) {

            try {
                HttpURLConnection urlConnection = (HttpURLConnection) new URL(Static_var.SQLITE_URL).openConnection();
                InputStream is = urlConnection.getInputStream();
                //is.available();//本機檔案才可以這樣讀
                total_size = urlConnection.getContentLength();
                Log.d("diff_size", (sFileSize - total_size) + "");
                if(Math.abs(sFileSize-total_size)<=1024){
                    return"";
                }
                mHandler.obtainMessage(1).sendToTarget();


                FileOutputStream fos = new FileOutputStream(sqlite_local_path);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) > 0 && stopCommand==0){
                    read_size+=length;
                    fos.write(buffer, 0, length);
                    progressDialog.setProgress( (int) Math.round(read_size*100/total_size) );
                    //Log.d("percent",Math.round(read_size*100.0/total_size)+"%");
                }
                fos.flush();
                fos.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            /*HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            try {
                BufferedOutputStream bout = new BufferedOutputStream(
                        new FileOutputStream(
                                Static_var.APP_PATH + "sqlite.db"));

                request.setURI(new URI(Static_var.SQLITE_URL));
                HttpResponse response = client.execute(request);
                StatusLine status = response.getStatusLine();
                //textView1.append("status.getStatusCode(): " + status.getStatusCode() + "\n");
                //Log.d("Test", "Statusline: " + status);
                //Log.d("Test", "Statuscode: " + status.getStatusCode());

                HttpEntity entity = response.getEntity();
                //textView1.append("length: " + entity.getContentLength() + "\n");
                //textView1.append("type: " + entity.getContentType() + "\n");
                Log.d("Test", "Length: " + entity.getContentLength());
                //Log.d("Test", "type: " + entity.getContentType());
                Log.d("Test", "Length: " + iFileSize);
                if( Math.abs( iFileSize - entity.getContentLength())<100 ){//大小相同不下載
                    return "";
                }
                entity.writeTo(bout);

                bout.flush();
                bout.close();
                //textView1.append("OK");


            } catch (URISyntaxException e) {
                // TODO Auto-generated catch block
                //textView1.append("URISyntaxException");
            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                //textView1.append("ClientProtocolException");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                //textView1.append("IOException");
            }*/
            return "";
        }

        private void copyDB() throws IOException
        {
            File dsqlite = new File(sqlite_local_path);

            if( dsqlite.exists() && stopCommand==0 ){
                InputStream mInput = new FileInputStream(dsqlite);

                String outFileName = DB_PATH;
                OutputStream mOutput = new FileOutputStream(outFileName);
                byte[] mBuffer = new byte[1024];
                int mLength;
                while ((mLength = mInput.read(mBuffer))>0)
                {
                    mOutput.write(mBuffer, 0, mLength);
                }
                mOutput.flush();
                mOutput.close();
                mInput.close();
                File dbFile = new File(outFileName);
                if(dbFile.exists()){
                    Log.d("copy_success!",dbFile.getPath());
                    dsqlite.delete();
                }else{
                    Log.d("copy_error!",dbFile.getPath());
                }

            }else{
                Log.d("copy","download_error or stop_download");
            }
        }

        private void copyDB(String source_file_path,String target_file_path) throws IOException
        {
            File dsqlite = new File(source_file_path);

            if( dsqlite.exists() && stopCommand==0 ){
                InputStream is = new FileInputStream(dsqlite);
                OutputStream os = new FileOutputStream(target_file_path);
                byte[] mBuffer = new byte[1024];
                int mLength;
                while ((mLength = is.read(mBuffer))>0)
                {
                    os.write(mBuffer, 0, mLength);
                }
                os.flush();
                os.close();
                is.close();
//                File dbFile = new File(target_file_path);
//                if(dbFile.exists()){
//                    Log.d("copy_success!",dbFile.getPath());
//                    dsqlite.delete();
//                }else{
//                    Log.d("copy_error!",dbFile.getPath());
//                }
            }else{
                Log.d("copy","download_error or stop_download");
            }
        }

        private boolean chkDB(){
            File db_path = new File(DB_PATH);
            if(db_path.exists()){
                return true;
            }
            return false;
        }

        public Handler mHandler= new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 1:
                        progressDialog.show();
                        break;
                }
                // process incoming messages here
            }
        };
    }
}
