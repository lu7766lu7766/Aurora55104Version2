package mac.jacwang.aurora20150610.Comm;

import android.os.Handler;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by jac on 2015/4/10.
 */
public class DBConnector {

    String Url = Static_var.DB_CONNECT_PATH;

    Handler mHandler;
    public DBConnector(Handler handler,paramsModels param,int type)
    {
        this.mHandler = handler;
        Thread t = new Thread(new sendPostRunnable(param,type));
        t.start();
    }
    public DBConnector(Handler handler,HashMap param,int type)
    {
        this.mHandler = handler;
        Thread t = new Thread(new sendPostRunnable(param,type));
        t.start();
    }

    class sendPostRunnable implements Runnable
    {
        paramsModels param = null;
        HashMap Hparam = null;
        int type = 0;
        // 建構子，設定要傳的字串
        public sendPostRunnable(paramsModels param,int type)
        {
            this.param = param;
            this.type = type;
        }

        public sendPostRunnable(HashMap param,int type)
        {
            this.Hparam = param;
            this.type = type;
        }

        @Override
        public void run()
        {
            String result = null;
            if(Hparam==null) {
                result = sendPostDataToInternet(param);
            }else{
                result = sendPostDataToInternet(Hparam);
            }
            mHandler.obtainMessage(type, result).sendToTarget();
        }
    }
    private String sendPostDataToInternet(paramsModels param)
    {

        String SyncURL=Url;
        String response;
        HttpPost hp = new HttpPost(SyncURL);
        HttpResponse hr;

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        //Add Post Data

            params.add(new BasicNameValuePair("table", param.table));
            params.add(new BasicNameValuePair("type", param.type));
            params.add(new BasicNameValuePair("brand", param.brand));
            params.add(new BasicNameValuePair("city", param.city));
            params.add(new BasicNameValuePair("dist", param.dist));
            params.add(new BasicNameValuePair("lat", param.lat));
            params.add(new BasicNameValuePair("lng", param.lng));
            params.add(new BasicNameValuePair("page", param.page));

        try {
            UrlEncodedFormEntity urf = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            hp.setEntity(urf);
            hr = new DefaultHttpClient().execute(hp);
            if(hr.getStatusLine().getStatusCode()==200){
                response= EntityUtils.toString(hr.getEntity());
                return response;
            }else{
                Log.i("AuroraJac","can't connect");
                return "";
            }
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            return new String( "UnsupportedEncodingException:"+e.getMessage());
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            return new String( "ClientProtocolException:"+e.getMessage());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            return new String( "IOException:"+e.getMessage());
        }
//        try{
//            String link = Url;
//            String data  = "table="  + URLEncoder.encode(table, "UTF-8") +
//                           "&type="  + URLEncoder.encode(type,  "UTF-8") +
//                           "&brand=" + URLEncoder.encode(brand, "UTF-8") +
//                           "&city="  + URLEncoder.encode(city,  "UTF-8") +
//                           "&dist="  + URLEncoder.encode(dist,  "UTF-8") +
//                           "&lat="   + URLEncoder.encode(lat,   "UTF-8") +
//                           "&lng="   + URLEncoder.encode(lng,   "UTF-8") +
//                           "&page="  + URLEncoder.encode(page,  "UTF-8") ;
//            URL url = new URL(link);
//            Log.i(TAG,"link:"+link);
//            URLConnection conn = url.openConnection();
//            conn.setDoOutput(true);
//            OutputStreamWriter wr = new OutputStreamWriter
//                    (conn.getOutputStream());
//            wr.write( data );
//            wr.flush();
//            BufferedReader reader = new BufferedReader
//                    (new InputStreamReader(conn.getInputStream()));
//            StringBuilder sb = new StringBuilder();
//            String line = null;
//            // Read Server Response
//            while((line = reader.readLine()) != null)
//            {
//                Log.i(TAG,"line:"+line);
//                sb.append(line);
//                break;
//            }
//            return sb.toString();
//        }catch(Exception e){
//            return new String("Exception: " + e.getMessage());
//        }
    }

    private String sendPostDataToInternet(HashMap param)
    {

        String SyncURL=Url;
        String response;
        HttpPost hp = new HttpPost(SyncURL);
        HttpResponse hr;

        List<NameValuePair> params = new ArrayList<NameValuePair>();

        Iterator iterator = param.keySet().iterator();
        while (iterator.hasNext()) {
            Object key = iterator.next();
            params.add(new BasicNameValuePair(key+"", param.get(key)+""));
        }

        try {
            UrlEncodedFormEntity urf = new UrlEncodedFormEntity(params, HTTP.UTF_8);
            hp.setEntity(urf);
            hr = new DefaultHttpClient().execute(hp);
            if(hr.getStatusLine().getStatusCode()==200){
                response= EntityUtils.toString(hr.getEntity());
                return response;
            }else{
                Log.i("AuroraJac","can't connect");
                return "";
            }
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            return new String( "UnsupportedEncodingException:"+e.getMessage());
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            return new String( "ClientProtocolException:"+e.getMessage());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            return new String( "IOException:"+e.getMessage());
        }
    }
}
