package mac.jacwang.aurora20150610;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import mac.jacwang.aurora20150610.DataAnalysis.resultDA;


public class memberJoinSuccess extends mActionBarActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_join_success);
        setTitle("已加入會員");

        init();
    }

    private void init(){
        ImageButton store_search_btn,member_ship_btn,promote_btn;
        store_search_btn = (ImageButton)findViewById(R.id.store_search);
        member_ship_btn = (ImageButton)findViewById(R.id.member_ship);
        promote_btn = (ImageButton)findViewById(R.id.promote);

        store_search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(memberJoinSuccess.this,store_search_list.class);
                startActivity(intent);
                finish();
            }
        });

        member_ship_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(memberJoinSuccess.this,memberShip.class);
                startActivity(intent);
                finish();
            }
        });

        promote_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(memberJoinSuccess.this,promote_list.class);
                startActivity(intent);
                finish();
//                Toast.makeText(getApplicationContext(),"功能尚在建置",Toast.LENGTH_SHORT).show();
            }
        });
    }

}
