package mac.jacwang.aurora20150610.Comm;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.List;
import java.util.Locale;

import mac.jacwang.aurora20150610.DataAnalysis.data_model;
import mac.jacwang.aurora20150610.DataAnalysis.t_store;
import mac.jacwang.aurora20150610.MainActivity;

/**
 * Created by jac on 2015/4/10.
 */
public class Static_var {

    public static final String TAG = "AuroraJac";

    public static final String SENDER_ID = "373402963276";

    public static final int CITY_DATA = 10;
    public static final int DIST_DATA = 11;

    public static final int STORE_DATA = 20;
    public static final int BRAND_DATA = 21;
    public static final int TYPE_DATA = 22;
    public static final int UPLOAD_STORE_DATA = 22;

    public static final int SETIMAGE_DATA = 30;
    public static final int GET_HALF_AD = 40;
    public static final int GET_LIST_AD = 41;

    public static final int MEMBER_CHK = 50;
    public static final int MEMBER_APPLY_CHK = 51;
    public static final int MEMBER_JOIN = 52;
    public static final int MEMBER_INFO = 53;
    public static final int MEMBER_INTRO_ADD = 54;

    public static final int GET_FAVORITE = 60;
    public static final int ADD_FAVORITE = 61;
    public static final int DEL_FAVORITE = 62;

    public static final int NEWS_DATA = 70;
    public static final int VERSION_DATA = 71;

//    enum DATA{CITY,DIST,STORE,BRAND,SETIMAGE};

    public static int search_type = 1;//預設種類--1飲料
    public static int search_promote_type = 1;//預設種類--1手機
    public static String search_brand = "null";//預設品牌
    public static String search_city = "null";//預設品牌
    public static String search_dist = "null";//預設品牌
    //protected static final String HOST = "http://27.105.182.18:8080/aurora01/";
    //protected static final String HOST = "http://125.227.84.248:8080/aurora01/";
    public static final String HOST = "http://app.55104.com.tw:8080/";
    public static final String LIBRARY_PATH = "library/";
    public static final String IMAGE_PATH = "images/";

    public static final String BRANDLOGO_DIR = "brand_logo/";
    public static final String BRANDLOGO_PATH = HOST+IMAGE_PATH+BRANDLOGO_DIR;

    public static final String BRANDMENU_DIR = "brand_menu/";
    public static final String BRANDMENU_PATH = HOST+IMAGE_PATH+BRANDMENU_DIR;

    public static final String TYPELOGO_DIR = "store_type/";
    public static final String TYPELOGO_PATH = HOST+IMAGE_PATH+TYPELOGO_DIR;

    public static final String ADVERTISEMENT_DIR = "advertisement/";
    public static final String ADVERTISEMENT_PATH = HOST+IMAGE_PATH+ADVERTISEMENT_DIR;

    public static final String PROMOTE_DIR = "promote/";
    public static final String PROMOTE_PATH = HOST+IMAGE_PATH+PROMOTE_DIR;

    public static final String PROMOTE_ACTIVITY_DIR = "promote_activity/";
    public static final String PROMOTE_ACTIVITY_PATH = HOST+IMAGE_PATH+PROMOTE_ACTIVITY_DIR;

    public static final String DB_CONNECT_PFILE = "android_connect_db.php";
    public static final String DB_CONNECT_PATH = HOST+LIBRARY_PATH+DB_CONNECT_PFILE;

    public static final String SQLITE_URL = HOST+"backups/sqlite.db";

    public static final String LOCAL_PATH = "data/55104/";

    public static final String APP_PATH = Environment.getExternalStorageDirectory()+ File.separator + LOCAL_PATH;
    public static Location location = null;
    public static final int[] STORE_TYPE = {1,6,14,5,4};
    public static final int[] PROMOTE_TYPE = {1,2,3};

    public static final int insertADNum = 3;//每三筆塞一個選單廣告
    public static List<data_model> listAD = null;
    public static List<data_model> listADManually = null;
    public static List<data_model> halfAD = null;
    public static List<t_store> favorite = null;
    public static List<Integer> favoriteId = null;

    public static final String data = "DATA";
    public static final String phone = "PHONE";
    public static final String login = "LOGIN";
    public static final String callPackage = "CALLPACKAGE";

    public static String defaultPhonePackage = "";

    static public String getAddressByLocation(Context context,Location location) {
        String returnAddress = "";
        try {
            if (location != null) {

                if (!Geocoder.isPresent()){ //Since: API Level 9
                    return "";
                }

                Double longitude = location.getLongitude();        //取得經度
                Double latitude = location.getLatitude();        //取得緯度

                Geocoder gc = new Geocoder(context, Locale.TRADITIONAL_CHINESE);        //地區:台灣
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

    /**
     * Covert dp to px
     * @param dp
     * @param context
     * @return pixel
     */
    public static float convertDpToPixel(float dp, Context context){
        float px = dp * getDensity(context);
        return px;
    }

    /**
     * Covert px to dp
     * @param px
     * @param context
     * @return dp
     */
    public static float convertPixelToDp(float px, Context context){
        float dp = px / getDensity(context);
        return dp;
    }


    /**
     * 取得螢幕密度
     * 120dpi = 0.75
     * 160dpi = 1 (default)
     * 240dpi = 1.5
     * @param context
     * @return
     */
    public static float getDensity(Context context){
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return metrics.density;
    }

    /**
     *陣列亂數排列
     */
    public static int[] RandomArray(int[] mArray)
    {
        int mLength = mArray.length;
        int mRandom, mNumber;

        for(int i = 0; i < mLength; i++)
        {
            mRandom = (int)(Math.random() * mLength);
            mNumber = mArray[i];
            mArray[i] = mArray[mRandom];
            mArray[mRandom] = mNumber;
        }

        return mArray;
    }

    public static String splitClassName(String class_package) {

        int startIndex = class_package.lastIndexOf(46) + 1;
        int endIndex = class_package.length();
        return  class_package.substring(startIndex, endIndex);
    }
}
