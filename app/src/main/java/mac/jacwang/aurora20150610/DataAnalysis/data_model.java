package mac.jacwang.aurora20150610.DataAnalysis;

import com.niusounds.sqlite.Persistence;
import com.niusounds.sqlite.PrimaryKey;

import java.io.Serializable;

import mac.jacwang.aurora20150610.Comm.Static_var;

public class data_model implements Serializable{

    public data_model() {}

    private int index;
    public int getIndex(){ return index; }
    public void setIndex(int index){ this.index = index;}

    private int image;
    public int getImage(){ return image; }
    public void setImage(int image){ this.image = image;}

    private String phone;
    public String getPhone(){ return phone; }
    public void setPhone(String s){ this.phone = s;}

    private String imageURL;
    public String getImageURL(){ return imageURL; }
    public void setImageURL(String s){ this.imageURL = s;}

    private String URL;
    public String getURL(){ return URL; }
    public void setURL(String s){ this.URL = s;}

}
