package mac.jacwang.aurora20150610;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

/**
 * Created by jac_note on 2015/4/6.
 */
public class welcome extends Activity {

    int wait_time = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);

        new CountDownTimer(wait_time, wait_time) {
            @Override
            public void onTick(long l) {
                //do notthing
            }
            @Override
            public void onFinish() {
                Intent intent = new Intent();
                //intent.setClass(welcome.this,MainActivity.class);
                intent.setClass(welcome.this,menu.class);
                startActivity(intent);
                finish();
            }
        }.start();
    }
}
