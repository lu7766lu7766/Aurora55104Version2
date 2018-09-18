package mac.jacwang.aurora20150610;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;


import java.util.HashMap;

import mac.jacwang.aurora20150610.Comm.Static_var;


public class MainActivity extends mActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView store_search = (ImageView)findViewById(R.id.store_search);
        store_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,store_search_category.class);
                startActivity(intent);
            }
        });


//        HashMap<String,String> params = new HashMap<String,String>();
//        params.put()
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:

                    break;
            }
        }
    };
}
