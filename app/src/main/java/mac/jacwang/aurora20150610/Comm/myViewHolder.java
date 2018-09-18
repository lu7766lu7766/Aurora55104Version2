package mac.jacwang.aurora20150610.Comm;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

/**
 * Created by jac on 2015/9/28.
 */
public class myViewHolder extends RecyclerView.ViewHolder {
    public myViewHolder(View itemView) {
        super(itemView);
    }

    public void defaultCall(Context mContext,String phone){
        phone = phone.replace("-","").replace("(","").replace("_","");
        Intent intent= new Intent("android.intent.action.CALL", Uri.parse("tel:" + phone));
        try {
            intent.setPackage(Static_var.defaultPhonePackage);
            mContext.startActivity(intent);
        }catch(Exception e){
            Log.e("call_error", e.getMessage());
            Toast.makeText(mContext, "請用預設撥號程式撥出", Toast.LENGTH_SHORT).show();
            mContext.startActivity(intent);
        }
    }
}
