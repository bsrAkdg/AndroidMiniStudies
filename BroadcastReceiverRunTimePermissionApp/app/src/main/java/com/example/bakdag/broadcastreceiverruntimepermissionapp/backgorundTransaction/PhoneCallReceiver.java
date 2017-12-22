package com.example.bakdag.broadcastreceiverruntimepermissionapp.backgorundTransaction;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.example.bakdag.broadcastreceiverruntimepermissionapp.R;

import static android.telephony.TelephonyManager.EXTRA_STATE_RINGING;

/**
 * Created by bakdag on 22.12.2017.
 */

public class PhoneCallReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {

        String state= intent.getStringExtra(TelephonyManager.EXTRA_STATE);

        if(state.equals(EXTRA_STATE_RINGING)){
            Toast.makeText(context, context.getString(R.string.ringing), Toast.LENGTH_LONG).show();
        }else if(state.equals(TelephonyManager.EXTRA_STATE_IDLE)){
            Toast.makeText(context, context.getString(R.string.idle), Toast.LENGTH_LONG).show();
        }else if(state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)){
            Toast.makeText(context, context.getString(R.string.offhook), Toast.LENGTH_LONG).show();
        }
    }
}
