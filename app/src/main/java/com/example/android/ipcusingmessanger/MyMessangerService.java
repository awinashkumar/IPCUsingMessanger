package com.example.android.ipcusingmessanger;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

public class MyMessangerService extends Service {

    public static final int JOB_1 = 1;
    public static final int JOB_2 = 2;
    public static final int JOB_1_REPLY = 3;
    public static final int JOB_2_REPLY = 4;

    public MyMessangerService() {
    }

    private Messenger messenger = new Messenger(new IncommingMessangerHandler());

    public class IncommingMessangerHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Message message;
            String messageStr;
            Bundle bundle = new Bundle();

            switch (msg.what){

                case JOB_1:
                    messageStr = "This is first message from service";
                    message = Message.obtain(null, JOB_1_REPLY);
                    bundle.putString("replay_message", messageStr);
                    message.setData(bundle);
                    try {
                        msg.replyTo.send(message);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                case JOB_2:
                    messageStr = "This is second message from service";
                    message = Message.obtain(null, JOB_2_REPLY);
                    bundle.putString("replay_message", messageStr);
                    message.setData(bundle);
                    try {
                        msg.replyTo.send(message);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;

                default:
                    super.handleMessage(msg);
                    break;

            }

        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
       return messenger.getBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return true;
    }
}
