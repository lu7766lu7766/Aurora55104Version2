package mac.jacwang.aurora20150610.Comm;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
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

public class mRAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public enum ITEM_TYPE {
        ITEM_STORE,
        ITEM_ADVERTISEMENT
    }

    private List<t_store> storesData = new ArrayList<t_store>();

    public mRAdapter(List<t_store> storesData) {
        this.storesData = storesData;
    }

    public boolean stillLoading = false;
    private boolean isLoaded = false;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        // create a new view
        //View itemLayoutView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_layout, null);
        View itemLayoutView = null;
        if (i == ITEM_TYPE.ITEM_STORE.ordinal()) {
            itemLayoutView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.store_list, null);
            StoreViewHolder viewHolder = new StoreViewHolder(itemLayoutView);
            viewHolder.setData(storesData);
            return viewHolder;
        }else if(i == ITEM_TYPE.ITEM_ADVERTISEMENT.ordinal()){
            itemLayoutView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ad_list, null);
            ADViewHolder viewHolder = new ADViewHolder(itemLayoutView);
            viewHolder.setData(storesData);
            return viewHolder;
        }
        // create ViewHolder

//        viewHolder = new ViewHolder(itemLayoutView);
//        viewHolder.setData(storesData);
//        return viewHolder;
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get data from your itemsData at this position
        // - replace the contents of the view with that itemsData

        //ViewHolder viewHolder = (ViewHolder) vh;
        //viewHolder.txtViewTitle.setText(itemsData[position].getTitle());
        //viewHolder.imgViewIcon.setImageResource(itemsData[position].getImageUrl());

//        Picasso.with(viewHolder.imgViewIcon.getContext()).cancelRequest(viewHolder.imgViewIcon);
//        Picasso.with(viewHolder.imgViewIcon.getContext()).load(itemsData[position].getImageUrl()).into(viewHolder.imgViewIcon);
//        Log.i("AuroraJacImgPath",storesData[position].getLogo());
        //Log.i("imggggg:",storesData.get(position).getLogoName());
        if (holder instanceof StoreViewHolder) {
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

            if (storesData.get(position).getDistance() > 1000) {
                viewHolder.distance_val.setText((Math.floor(storesData.get(position).getDistance() / 100) / 10) + "");
                viewHolder.distance_unit.setText("km");
            } else {
                viewHolder.distance_val.setText(storesData.get(position).getDistance() + "");
                viewHolder.distance_unit.setText("m");
            }
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
        }else if (holder instanceof ADViewHolder) {
            ADViewHolder viewHolder = (ADViewHolder)holder;
            Picasso.with(viewHolder.image.getContext()).cancelRequest(viewHolder.image);
            Picasso.with(viewHolder.image.getContext())
                    .load(Static_var.ADVERTISEMENT_PATH + storesData.get(position).getLogoName())
                    .placeholder(R.drawable.ad_list_loading)
                    .fit().centerCrop()
                    .error(R.drawable.ad_list_loading_error)
                    .into(viewHolder.image);
        }
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

    @Override
    public int getItemViewType(int position) {
        return (position+1) % (Static_var.insertADNum+1) == 0 && position>0 ? ITEM_TYPE.ITEM_ADVERTISEMENT.ordinal() : ITEM_TYPE.ITEM_STORE.ordinal();
    }

    private timerTask tk;
    public void add(List<t_store> items){
        if( !isLoaded ){
            tk = new timerTask(items.size()*150,150,items);
            tk.start();
        }else{
            for(t_store item:items){
                if( (storesData.size()+1)%(Static_var.insertADNum+1)==0 ) {
                    List<data_model> listAD = Static_var.listAD;
                    if (listAD != null) {
                        int adlen = listAD.size();
                        int index = (int) Math.floor(Math.random() * adlen);
                        t_store ad = new t_store();
                        ad.setLogo(listAD.get(index).getImageURL());
                        ad.setPhone(listAD.get(index).getPhone());
                        storesData.add(ad);
                    } else {
                        t_store ad = new t_store();
                        storesData.add(ad);
                    }

                    notifyItemInserted(storesData.size() - 1);
                }
                storesData.add(item);
                notifyItemInserted(storesData.size()-1);
            }
        }
    }

    public void stopAdd(){
        if(tk!=null)
        {
            tk.cancel();
        }
    }

    //------------------------------------- ViewHolder ----------------------------------------------//
    // inner class to hold a reference to each item of RecyclerView
    public static class StoreViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //public TextView txtViewTitle;
        //public ImageView imgViewIcon;
        public TextView brand_name,subname,address,distance_val,distance_unit;
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
            distance_val = (TextView) itemLayoutView.findViewById(R.id.distance_val);
            distance_unit = (TextView) itemLayoutView.findViewById(R.id.distance_unit);
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
            //Toast.makeText(view.getContext(), "position = " + getPosition(), Toast.LENGTH_SHORT).show();
        }
    }

    public static class ADViewHolder extends myViewHolder implements View.OnClickListener {

        public ImageView image;
        private Context mContext;

        public ADViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            image = (ImageView) view.findViewById(R.id.image);
            mContext = view.getContext();
        }

        List<t_store> storesData;

        public void setData(List<t_store> storesData){
            this.storesData = storesData;
        }

        @Override
        public void onClick(View view) {
            if(mContext!=null&& storesData!=null) {
                String phone = storesData.get(getAdapterPosition()).getPhone();
                if(!phone.isEmpty()) {
                    defaultCall(mContext,phone);
                }else{
                    //沒電話要做啥？
                }
            }
        }
    }
    //-------------------------------------------- timerTask ------------------------------------------//

    public class timerTask extends CountDownTimer {

        List<t_store> items ;
        int tickTimes = 0;

        public timerTask(long millisInFuture, long countDownInterval,List<t_store> items){
            super( millisInFuture, countDownInterval);
            this.items = items;
            stillLoading = true;
        }

        @Override
        public void onTick(long l) {
            if( (storesData.size()+1)%(Static_var.insertADNum+1)==0 ) {
                List<data_model> listAD = Static_var.listAD;
                if (listAD != null) {
                    int adlen = listAD.size();
                    int index = (int) Math.floor(Math.random() * adlen);
                    t_store ad = new t_store();
                    ad.setLogo(listAD.get(index).getImageURL());
                    ad.setPhone(listAD.get(index).getPhone());
                    storesData.add(ad);
                } else {
                    t_store ad = new t_store();
                    storesData.add(ad);
                }

                notifyItemInserted(storesData.size() - 1);
            }

            t_store item = items.get(tickTimes++);
            storesData.add(item);
            notifyItemInserted(storesData.size() - 1);
            //notifyItemMoved(0, storesData.size() - 1);
        }

        @Override
        public void onFinish() {
            stillLoading=false;
            isLoaded=true;
            //do nothing
        }
    }

    public void removeAll(){
        int len = storesData.size();
        for(int i = 0 ;i<len ; i++){
            remove(storesData.get(i));
        }
    }

    public void remove(t_store item) {
        int position = storesData.indexOf(item);
        storesData.remove(position);
        notifyItemRemoved(position);
    }


}