package com.example.android.ipcusingmessanger;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Messenger messenger;
    private boolean isBind = false;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_activity);
        textView = (TextView) findViewById(R.id.tv);
        Button button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFirstMessage();
            }
        });
    }

    public void getFirstMessage() {

        System.out.println("111111");

        Message message = Message.obtain(null, MyMessangerService.JOB_1);
        message.replyTo = new Messenger(new ReplyMessageHandler());
        System.out.println("22222");
        try {
            System.out.println("3333");
            messenger.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void getSecondMessage() {
        Message message = Message.obtain(null, MyMessangerService.JOB_2);
        message.replyTo = new Messenger(new ReplyMessageHandler());
        try {
            messenger.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public class ReplyMessageHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            String messageStr;
            switch (msg.what) {

                case MyMessangerService.JOB_1_REPLY:
                    messageStr = msg.getData().getString("replay_message");
                    textView.setText(messageStr);
                    break;

                case MyMessangerService.JOB_2_REPLY:
                    messageStr = msg.getData().getString("replay_message");
                    textView.setText(messageStr);
                    break;

                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            messenger = new Messenger(iBinder);
            isBind = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

            messenger = null;
            isBind = false;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        if (!isBind) {
            Intent intent = new Intent(MainActivity.this, MyMessangerService.class);
            bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
            isBind = true;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isBind) {
            unbindService(serviceConnection);
            isBind = false;
        }
    }
}
