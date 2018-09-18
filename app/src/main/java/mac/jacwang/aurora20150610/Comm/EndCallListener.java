package mac.jacwang.aurora20150610.Comm;

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

/**
 * Created by jac on 2015/10/30.
 */
public abstract class EndCallListener extends PhoneStateListener {

    private int prev_state;

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        switch(state){
            case TelephonyManager.CALL_STATE_RINGING:
//                Log.d(TAG, "CALL_STATE_RINGING");
                prev_state=state;
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
//                Log.d(TAG, "CALL_STATE_OFFHOOK");
                prev_state=state;
                break;
            case TelephonyManager.CALL_STATE_IDLE:
//                Log.d(TAG, "CALL_STATE_IDLE==>"+incoming_nr);
                if((prev_state==TelephonyManager.CALL_STATE_OFFHOOK)){
                    prev_state=state;
                    this.onEndCall();
                    //Answered Call which is ended
                }
                if((prev_state==TelephonyManager.CALL_STATE_RINGING)){
                    prev_state=state;
                    //Rejected or Missed call
                }
                break;

        }
    }

    public abstract void onEndCall();

}
