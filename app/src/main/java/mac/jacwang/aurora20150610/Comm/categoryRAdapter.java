package mac.jacwang.aurora20150610.Comm;

import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import mac.jacwang.aurora20150610.DataAnalysis.data_model;
import mac.jacwang.aurora20150610.R;

public class categoryRAdapter extends RecyclerView.Adapter<categoryRAdapter.ViewHolder>{

    private List<data_model> data = new ArrayList<data_model>();

    public categoryRAdapter(List<data_model> data) {
        this.data = data;
    }

    ViewHolder viewHolder;

    public boolean stillLoading = false;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category_vertical, null);

        // create ViewHolder
        viewHolder = new ViewHolder(itemLayoutView);
        viewHolder.setData(data);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Picasso.with(viewHolder.image.getContext())
                .load(data.get(position).getImage())
                .error(R.drawable.catesub_drink)
                .into(viewHolder.image);
//        viewHolder.image.setImageDrawable(viewHolder.image.getContext().getResources().getDrawable( data.get(position).getImage() ));

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

    private timerTask tk;
    public void add(List<data_model> items){
        tk = new timerTask(items.size()*150,150,items);
        tk.start();
    }

    //------------------------------------- ViewHolder ----------------------------------------------//
    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView image;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            image = (ImageView) itemLayoutView.findViewById(R.id.image);
        }

        List<data_model> data;

        public void setData(List<data_model> data){
            this.data = data;
        }

    }
    //-------------------------------------------- timerTask ------------------------------------------//

    public class timerTask extends CountDownTimer {

        List<data_model> items ;
        int tickTimes = 0;

        public timerTask(long millisInFuture, long countDownInterval,List<data_model> items){
            super( millisInFuture, countDownInterval);
            this.items = items;
            stillLoading = true;
        }

        @Override
        public void onTick(long l) {
            data_model item = items.get(tickTimes++);
            data.add(item);
            notifyItemInserted(data.size() - 1);
            //notifyItemMoved(0, data.size() - 1);
        }

        @Override
        public void onFinish() {
            stillLoading=false;
            //do nothing
        }
    }

    public void removeAll(){
        int len = data.size();
        for(int i = 0 ;i<len ; i++){
            remove(data.get(i));
        }
    }

    public void remove(data_model item) {
        int position = data.indexOf(item);
        data.remove(position);
        notifyItemRemoved(position);
    }
}