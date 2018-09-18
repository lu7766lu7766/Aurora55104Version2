package mac.jacwang.aurora20150610;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;


public class recommendStoreSuccess extends mActionBarActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recommend_store_success);
        setTitle("推薦完成");

        init();
    }

    private void init(){
        ImageButton store_search_btn,recommend_btn;
        store_search_btn = (ImageButton)findViewById(R.id.store_search);
        recommend_btn = (ImageButton)findViewById(R.id.recommend);

        store_search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(recommendStoreSuccess.this,store_search_list.class);
                startActivity(intent);
                finish();
            }
        });

        recommend_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(recommendStoreSuccess.this,recommendStore.class);
                startActivity(intent);
                finish();
//                Toast.makeText(getApplicationContext(),"功能尚在建置",Toast.LENGTH_SHORT).show();
            }
        });
    }

}
