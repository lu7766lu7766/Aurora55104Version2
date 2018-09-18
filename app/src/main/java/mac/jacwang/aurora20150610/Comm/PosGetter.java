package mac.jacwang.aurora20150610.Comm;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

/**
 * Created by jac on 15/6/25.
 */
public class PosGetter implements LocationListener {

    LocationManager mLocationManager;
    Location nlocation,glocation,location;
    public static boolean isChged = false;

    Handler handler;
    Context context;
    String address;
    String provider;

    public PosGetter(final Context context){
        this.context = context;

        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        //Log.d("AuroraJacLocationGet","gps:"+mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)+"");
        //Log.d("AuroraJacLocationGet","network:"+mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)+"");
        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {

            Criteria criteria = new Criteria();	//資訊提供者選取標準
            provider = mLocationManager.getBestProvider(criteria, true);

            short i=0;
            Double distance=0.0;
            while( nlocation  == null && glocation==null && i<5 ){
                if(nlocation!=null&&glocation!=null)
                    distance = countDistance(location.getLatitude(),location.getLongitude(),glocation.getLatitude(),glocation.getLongitude());
                    glocation = mLocationManager.getLastKnownLocation(provider);
                    nlocation = mLocationManager.getLastKnownLocation("network");
                if(nlocation!=null){
                    location = nlocation;
                }
                if(glocation!=null && distance<200){
                    location = glocation;
                    break;
                }
                i++;
            }
            //location = mLocationManager.getLastKnownLocation(provider);
            mLocationManager.requestLocationUpdates(provider, 0, 0, this);

            init();
        }else{
            //Toast.makeText(context, "請開啟ＧＰＳ", Toast.LENGTH_SHORT).show();
        }
    }

    //boolean tips = false;
    public int sleep_time = 1000;
    private Thread t1;
    private void init() {

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // process incoming messages here
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        if (location != null) {
                            address = getAddressByLocation(location);
                            onGPSListener.onGet(location, address);
                            Static_var.location = location;
                        }
                        break;
                }
            }
        };

        t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                tryTimes = 0;

                try {
                    while (location == null) {
                        Thread.sleep(sleep_time);
                        location = tryTimes++ % 2 == 0 ?
                                mLocationManager.getLastKnownLocation(provider) :
                                mLocationManager.getLastKnownLocation("network");
                    }


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //handler.sendMessage(handler.obtainMessage(2));

                handler.sendMessage(handler.obtainMessage(1));
                t1.interrupt();
                t1 = null;
            }
        });
        t1.start();
    }

    int tryTimes = 0;
    @Override
    public void onLocationChanged(Location location) {
        mLocationManager.removeUpdates(this);
        mLocationManager.requestLocationUpdates(provider, 60000, 10, this);
        if(location!=null) {
            Static_var.location = location;
            if (this.location == null) {
                isChged = true;
                address = getAddressByLocation(location);
                onGPSListener.onChg(location, address);
                this.location = location;
            } else if (!isChged) {
                isChged = true;
                Double distance = countDistance(location.getLatitude(), location.getLongitude(), this.location.getLatitude(), this.location.getLongitude());
                if (distance > 300) {
                    address = getAddressByLocation(location);
                    onGPSListener.onChg(location, address);
                    this.location = location;
                }
            }
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    /**
     * 定義接口
     */
    public interface OnGPSListener{
        void onGet(Location location,String address);
        void onChg(Location location,String address);
    }
    private OnGPSListener onGPSListener = new OnGPSListener() {
        @Override
        public void onGet(Location location, String address) {

        }
        @Override
        public void onChg(Location location, String address) {

        }
    };
    public void setOnGPSListener(OnGPSListener onGPSListener){
        this.onGPSListener=onGPSListener;
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
                Log.d("cc",longitude+"/"+latitude);
                Geocoder gc = new Geocoder(this.context, Locale.TRADITIONAL_CHINESE);        //地區:台灣
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

    private double countDistance(double latitude1,double longitude1,double latitude2,double longitude2)
    {
        double radLatitude1 = latitude1 * Math.PI / 180;
        double radLatitude2 = latitude2 * Math.PI / 180;
        double l = radLatitude1 - radLatitude2;
        double p = longitude1 * Math.PI / 180 - longitude2 * Math.PI / 180;
        double distance = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(l / 2), 2)
                + Math.cos(radLatitude1) * Math.cos(radLatitude2)
                * Math.pow(Math.sin(p / 2), 2)));
        distance = distance * 6378137.0;
        distance = Math.round(distance * 10000) / 10000;

        return distance ;
    }

//    private class waitLocationTask extends AsyncTask<String, String , String>  {
//
//        @Override
//        protected String doInBackground(String... strings) {
//            while(Static_var.location!=null){
//                //do nothing
//            }
//            return null;
//        }
//
//        interface protected void onPostExecute(String result) {}
//    }
}
