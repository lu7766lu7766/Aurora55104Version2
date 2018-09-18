package mac.jacwang.aurora20150610.Comm;

/**
 * Created by jac on 15/8/25.
 */
import mac.jacwang.aurora20150610.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class HorizontalListViewAdapter extends BaseAdapter{
    private int[] mIconIDs;
    private Context mContext;
    private LayoutInflater mInflater;
    private int selectIndex = -1;

    public HorizontalListViewAdapter(Context context,  int[] ids){
        this.mContext = context;
        this.mIconIDs = ids;
        mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);
    }
    @Override
    public int getCount() {
        return mIconIDs.length;
    }
    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if(convertView==null){
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.catesub_horilist, null);
            holder.mImage=(ImageView)convertView.findViewById(R.id.image);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }
        if(position == selectIndex){
            convertView.setSelected(true);
        }else{
            convertView.setSelected(false);
        }
        holder.mImage.setImageResource(mIconIDs[position]);

        return convertView;
    }

    private static class ViewHolder {
        private ImageView mImage;
    }

    public void setSelectIndex(int i){
        selectIndex = i;
    }
}
