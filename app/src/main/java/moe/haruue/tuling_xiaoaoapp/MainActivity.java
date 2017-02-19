package moe.haruue.tuling_xiaoaoapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import moe.haruue.tuling_xiaoaoapp.ChatMessage.Type;

public class MainActivity extends AppCompatActivity {

    private Button send;   //发送消息按钮

    private ImageButton tool; //工具按钮

    private ListView mChatView;   //展示消息列表

    private EditText mMsg;  //文本输入

    private List<ChatMessage> mDatas = new ArrayList<>();  //聊天消息集合

    private ChatMessageAdapter mAdapter; //适配器

    private Handler mHandler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            ChatMessage from = (ChatMessage) msg.obj;
            mDatas.add(from);
            mAdapter.notifyDataSetChanged();    //刷新界面
            mChatView.setSelection(mDatas.size() - 1);    //让ListView定位到指定Item的位置
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_chatting);

        initView();  //调用初始化方法

        mAdapter = new ChatMessageAdapter(this, mDatas);
        mChatView.setAdapter(mAdapter);

        mChatView.setSelection(mDatas.size() - 1); //ListView定位

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String msg = mMsg.getText().toString();
                if (TextUtils.isEmpty(msg))   //判断消息输入是否为空
                {
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.emptyTips), Toast.LENGTH_SHORT).show();
                    return;
                }

                ChatMessage to = new ChatMessage(Type.OUTPUT, msg);
                to.setDate(new Date());
                mDatas.add(to);

                mAdapter.notifyDataSetChanged();
                mChatView.setSelection(mDatas.size() - 1);

                mMsg.setText("");

                new Thread()
                {
                    public void run()
                    {
                        ChatMessage from = null;
                        try
                        {
                            from = HttpUtils.sendMsg(msg);
                        } catch (Exception e)
                        {
                            from = new ChatMessage(Type.INPUT, getResources().getString(R.string.netError));
                        }
                        Message message = Message.obtain();
                        message.obj = from;
                        mHandler.sendMessage(message);
                    };
                }.start();
            }
        });

        tool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initView()
    {
        send = (Button) findViewById(R.id.id_chat_send);
        tool = (ImageButton) findViewById(R.id.tool);
        mChatView = (ListView) findViewById(R.id.id_chat_listView);
        mMsg = (EditText) findViewById(R.id.id_chat_msg);
        mDatas.add(new ChatMessage(Type.INPUT, getResources().getString(R.string.welcome)));  //初始化消息

        SharedPreferences datas = getSharedPreferences("data", MODE_PRIVATE);
        String json = datas.getString("data", null);  //数据恢复
        if (json != null) {
            Gson gson = new Gson();
            java.lang.reflect.Type type = new TypeToken<List<ChatMessage>>(){}.getType();
            mDatas = gson.fromJson(json, type);
        }

    }

    @Override
    protected void onDestroy() {    //进行数据保存
        super.onDestroy();
        SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
        Gson gson = new Gson();
        String json = gson.toJson(mDatas);
        editor.putString("data", json);
        editor.apply();
    }
}