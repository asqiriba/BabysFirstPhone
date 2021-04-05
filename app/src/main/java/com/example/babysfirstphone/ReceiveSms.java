package com.example.babysfirstphone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class ReceiveSms extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
            Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
            SmsMessage[] msgs = null;
            String msg_from;
            if (bundle != null){
                //---retrieve the SMS message received---
                try{
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for(int i=0; i<msgs.length; i++){
                        msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                        msg_from = msgs[i].getOriginatingAddress();
                        String msgBody = msgs[i].getMessageBody();

                        // A custom Intent that will used as another Broadcast
                        Intent in = new Intent("SmsMessage.intent.MAIN").
                                putExtra("get_msg", msg_from + ":" + msgBody);
                        // To display a Toast whenever there is an SMS.
//                        Toast.makeText(context, msgBody, Toast.LENGTH_LONG).show();
                        context.sendBroadcast(in);

//                        Toast.makeText(context, "From: " + msg_from + ", Body: " + msgBody, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

    }

}


