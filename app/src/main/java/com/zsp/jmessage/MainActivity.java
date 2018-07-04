package com.zsp.jmessage;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.event.ConversationRefreshEvent;
import cn.jpush.im.android.api.event.MessageEvent;
import cn.jpush.im.android.api.event.MessageReceiptStatusChangeEvent;
import cn.jpush.im.android.api.event.MessageRetractEvent;
import cn.jpush.im.android.api.event.OfflineMessageEvent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;

public class MainActivity extends AppCompatActivity {

    private TextView show;
    private EditText ed;
    private Button send;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        JMessageClient.registerEventReceiver(this);
        List<Conversation> conversations = JMessageClient.getConversationList();
        Log.d(TAG, "onCreate: " + conversations);
        if (conversations!=null) {
            conversations.get(0).getAllMessage();
            getMessage(conversations.get(0));
        }
        findID();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        JMessageClient.unRegisterEventReceiver(this);
    }

    private void findID() {
        show = findViewById(R.id.show);
        ed = findViewById(R.id.ed);
        send = findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendHandler.sendEmptyMessage(0);
            }
        });
    }

    @SuppressLint("HandlerLeak")
    private Handler sendHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    //创建一个消息对象
                    Message m = JMessageClient.createSingleTextMessage("zsp1993", ed.getText().toString());
                    //发送消息
                    JMessageClient.sendMessage(m);
                    break;
            }
        }
    };

    private void getMessage(Conversation conversation) {
        try {
            List<Message> messages = conversation.getAllMessage();
            Log.d(TAG, "getMessage: " + messages.get(0).getContent().toJson());
            for (int i = 0; i < messages.size(); i++) {
                JSONObject object = new JSONObject(messages.get(i).getContent().toJson());
                show.setText(object.getString("text"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 收到消息
     */
    public void onEvent(MessageEvent event) {
        JSONObject object = null;
        try {
            object = new JSONObject(event.getMessage().getContent().toJson());
            show.setText(object.getString("text"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 接收离线消息
     *
     * @param event 离线消息事件
     */
    public void onEvent(OfflineMessageEvent event) {
    }

    /**
     * 消息撤回
     */
    public void onEvent(MessageRetractEvent event) {
    }

    /**
     * 消息已读事件
     */
    public void onEventMainThread(MessageReceiptStatusChangeEvent event) {
    }

    /**
     * 消息漫游完成事件
     *
     * @param event 漫游完成后， 刷新会话事件
     */
    public void onEvent(ConversationRefreshEvent event) {
    }


    private static final String TAG = "MainActivity";
}
