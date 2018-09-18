package mac.jacwang.aurora20150610.Comm;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mac.jacwang.aurora20150610.DataAnalysis.data_model;
import mac.jacwang.aurora20150610.DataAnalysis.t_store;
import mac.jacwang.aurora20150610.R;
import mac.jacwang.aurora20150610.store_info;

public class promoteRAdapter extends RecyclerView.Adapter<ViewHolder>{

    private List<HashMap<String,String>> data = new ArrayList<HashMap<String,String>>();

    public promoteRAdapter(List<HashMap<String,String>> data) {
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        
        View itemLayoutView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.promote_list, null);
        mViewHolder viewHolder = new mViewHolder(itemLayoutView);
        viewHolder.setData(data);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        String activity_file = data.get(position).get("activity");
        //Log.d("activity_file",activity_file);
        mViewHolder viewHolder = (mViewHolder)holder;
        Picasso.with(viewHolder.logo.getContext())
                .load(Static_var.PROMOTE_PATH+data.get(position).get("logo"))
                .placeholder(R.drawable.no_logo)
                .into(viewHolder.logo);
        viewHolder.name.setText(data.get(position).get("name"));
        viewHolder.bonus.setText(Html.fromHtml("<big><font color=\"#ff0000\"><b>" + data.get(position).get("bonus") + "</b></font></big>點"));

        if( !activity_file.isEmpty()) {
            Paint paint = viewHolder.bonus.getPaint();
            paint.setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            paint.setAntiAlias(true);
            Picasso.with(viewHolder.activity.getContext())
                    .load(Static_var.PROMOTE_ACTIVITY_PATH+activity_file)
                    .into(viewHolder.activity);
        }else{
            viewHolder.bonus.getPaint().setFlags(0);
            Picasso.with(viewHolder.activity.getContext())
                    .load(R.drawable.space)
                    .into(viewHolder.activity);
        }

        viewHolder.describe.setText(data.get(position).get("describe"));
        viewHolder.describe.setVisibility(View.GONE);

    }

    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if(data!=null){
            return data.size();
        }else{
            return 0;
        }
    }

    public void add(List<HashMap<String,String>> items){
        for(HashMap<String,String> item:items){
            data.add(item);
            notifyItemInserted(data.size() - 1);
        }
    }

    public void removeAll(){
        if(data!=null) {
            while(data.size()!=0){
                data.remove(0);
                notifyItemRemoved(0);
            }
//            int len = data.size();
//            Log.d("data.size", len + "");
//            for (int i = 0; i < len; i++) {
//                Log.d("data.i",i+"");
//                data.remove(0);
//                notifyItemRemoved(0);
//            }
//            Log.d("data.end", "end??");
        }
    }


    //------------------------------------- ViewHolder ----------------------------------------------//
    // inner class to hold a reference to each item of RecyclerView
    public static class mViewHolder extends myViewHolder implements View.OnClickListener {

        public TextView name,describe,bonus;
        public ImageView logo,activity;
        private Context mContext;

        public mViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            itemLayoutView.setOnClickListener(this);
            //txtViewTitle = (TextView) itemLayoutView.findViewById(R.id.item_title);
            //imgViewIcon = (ImageView) itemLayoutView.findViewById(R.id.item_icon);
            name = (TextView) itemLayoutView.findViewById(R.id.name);
            describe = (TextView) itemLayoutView.findViewById(R.id.describe);
            bonus = (TextView) itemLayoutView.findViewById(R.id.bonus);
            logo = (ImageView) itemLayoutView.findViewById(R.id.logo);
            activity = (ImageView) itemLayoutView.findViewById(R.id.activity_picture);
            mContext = itemLayoutView.getContext();
        }

        List<HashMap<String,String>> data;

        public void setData(List<HashMap<String,String>> data){
            this.data = data;
        }

        @Override
        public void onClick(View view) {
            if(mContext!=null&& data!=null){
                defaultCall(mContext,"55104,9");
            }
        }
    }
}