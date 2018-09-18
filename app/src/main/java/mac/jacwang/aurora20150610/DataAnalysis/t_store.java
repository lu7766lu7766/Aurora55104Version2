package mac.jacwang.aurora20150610.DataAnalysis;

import java.io.Serializable;

import mac.jacwang.aurora20150610.Comm.Static_var;
import com.niusounds.sqlite.Persistence;
import com.niusounds.sqlite.PrimaryKey;

public class t_store implements Serializable{

    public t_store() {}

    @Persistence
    @PrimaryKey(autoIncrement = true)
    private int fi_id;              //fi_id
    public int getFiId(){return fi_id;}
    public void setFiId(int id){this.fi_id = id;}

    @Persistence
    private String fv_brand_name = "";      //店家名稱
    public String getBrandName(){ return fv_brand_name; }
    public void setBrandName(String brand_name){ this.fv_brand_name = brand_name; }

    @Persistence
    private String fv_subname = "";        //店家區域名稱
    public String getSubName(){ return fv_subname; }
    public void setSubName(String subname){ this.fv_subname = subname; }

    @Persistence
    private String fv_address = "";         //店家住址
    public void setAddress(String address) { this.fv_address = address; }
    public String getAddress() { return fv_address; }

    @Persistence
    private String fv_phone = "";
    public String getPhone() { return fv_phone; }
    public void setPhone(String phone) { this.fv_phone = phone; }


    @Persistence
    private String fv_logo = "";            //圖片
    public String getLogo(){ return Static_var.BRANDLOGO_PATH+fv_logo; }
    public String getLogoName(){ return fv_logo;}
    public void setLogo(String logo)
    {
        this.fv_logo = logo;
    }

    @Persistence
    private String fv_menu = "";            //圖片
    public String getMenu(){ return fv_menu; }
    public void setMenu(String menu) { this.fv_menu = menu; }

    @Persistence
    private double ff_latitude;        //景點店家緯度
    public double getLatitude() { return ff_latitude; }
    public void setLatitude(double latitude) { this.ff_latitude = latitude; }


    @Persistence
    private double ff_longitude;       //景點店家經度
    public double getLongitude(){ return ff_longitude; }
    public void setLongitude(double longitude){ this.ff_longitude = longitude; }


    @Persistence
    private double distance;        //景點店家距離
    public double getDistance(){ return distance; }
    public void setDistance(double distance){ this.distance = distance; }


    @Persistence
    private int fi_special_price;      //是否特價 1是 0否
    public double getSpecialPrice(){ return fi_special_price; }
    public void setSpecailPrice(int special_price){ this.fi_special_price = special_price; }

    @Persistence
    private String fv_special_info;       //特價資訊
    public String getSpecialInfo()
    {
        return fv_special_info;
    }
    public void setSpecialInfo(String special_info){ this.fv_special_info = special_info; }

    @Persistence
    private int fi_level;              //店家等級
    public int getLevel()
    {
        return fi_level;
    }
    public void setLevel(int level){ this.fi_level = level; }

    @Persistence
    private int fi_type;              //店家等級
    public int getType()
    {
        return fi_type;
    }
    public void setType(int type){ this.fi_type = type; }

}
