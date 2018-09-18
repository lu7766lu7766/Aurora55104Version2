package mac.jacwang.aurora20150610.Comm;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import mac.jacwang.aurora20150610.DataAnalysis.data_model;
import mac.jacwang.aurora20150610.DataAnalysis.t_store;
import mac.jacwang.aurora20150610.R;
import mac.jacwang.aurora20150610.store_info;

public class favoriteRAdapter extends RecyclerView.Adapter<ViewHolder>{

    private List<t_store> storesData = new ArrayList<t_store>();
    private boolean isListAD = true;

    public favoriteRAdapter(List<t_store> storesData) {
        this.storesData = storesData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View itemLayoutView = null;

            itemLayoutView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.store_list_manually, null);
            StoreViewHolder viewHolder = new StoreViewHolder(itemLayoutView);
            viewHolder.setData(storesData);
            return viewHolder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

            StoreViewHolder viewHolder = (StoreViewHolder)holder;
            Picasso.with(viewHolder.brand_logo.getContext()).cancelRequest(viewHolder.brand_logo);
            Picasso.with(viewHolder.brand_logo.getContext())
                    .load(storesData.get(position).getLogo())
                    .placeholder(R.drawable.no_logo)
                    .into(viewHolder.brand_logo);
            if(storesData.get(position).getSpecialPrice()==1){
                Picasso.with(viewHolder.special_price.getContext()).cancelRequest(viewHolder.special_price);
                Picasso.with(viewHolder.special_price.getContext())
                        .load(R.drawable.store_list_special_price)
                        .into(viewHolder.special_price);
            }

            viewHolder.brand_name.setText(storesData.get(position).getBrandName());
            viewHolder.subname.setText(storesData.get(position).getSubName());
            viewHolder.address.setText(storesData.get(position).getAddress());
            int level = storesData.get(position).getLevel();
            int level_img = R.drawable.star0;
            switch (level){
                case 1:
                    level_img = R.drawable.star1;
                    break;
                case 2:
                    level_img = R.drawable.star2;
                    break;
                case 3:
                    level_img = R.drawable.star3;
                    break;
                case 4:
                    level_img = R.drawable.star4;
                    break;
                case 5:
                    level_img = R.drawable.star5;
                    break;
            }
            Picasso.with(viewHolder.level.getContext()).cancelRequest(viewHolder.level);
            Picasso.with(viewHolder.level.getContext())
                    .load(level_img)
                    .into(viewHolder.level);

    }

    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if(storesData!=null){
            return storesData.size();
        }else{
            return 0;
        }
    }

    public void add(List<t_store> items){
        for(t_store item:items){
            storesData.add(item);
            notifyItemInserted(storesData.size()-1);
        }
    }


    public static class StoreViewHolder extends ViewHolder implements View.OnClickListener {

        //public TextView txtViewTitle;
        //public ImageView imgViewIcon;
        public TextView brand_name,subname,address;
        public ImageView brand_logo,special_price,level;
        private Context mContext;

        public StoreViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            itemLayoutView.setOnClickListener(this);
            //txtViewTitle = (TextView) itemLayoutView.findViewById(R.id.item_title);
            //imgViewIcon = (ImageView) itemLayoutView.findViewById(R.id.item_icon);
            brand_name = (TextView) itemLayoutView.findViewById(R.id.brand_name);
            subname = (TextView) itemLayoutView.findViewById(R.id.subname);
            address = (TextView) itemLayoutView.findViewById(R.id.address);
            brand_logo = (ImageView) itemLayoutView.findViewById(R.id.brand_logo);
            special_price = (ImageView) itemLayoutView.findViewById(R.id.special_price);
            level = (ImageView) itemLayoutView.findViewById(R.id.level);
            mContext = itemLayoutView.getContext();
        }

        List<t_store> storesData;

        public void setData(List<t_store> storesData){
            this.storesData = storesData;
        }

        @Override
        public void onClick(View view) {
            if(mContext!=null&& storesData!=null){
                Intent it = new Intent();
                it.setClass(mContext, store_info.class);
                it.putExtra("store",storesData.get(getAdapterPosition()));
                mContext.startActivity(it);
            }
        }
    }
}