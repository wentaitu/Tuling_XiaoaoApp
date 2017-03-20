package moe.haruue.tuling_xiaoaoapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button send;   //发送消息
    private ImageButton tool; //工具
    private RecyclerView rChatView; //消息列表
    private EditText mMsg;  //文本输入
    private List<ChatMessage> mDatas = new ArrayList<>();  //消息集合
    private ChatMessageRecyclerViewAdapter rAdapter; //数据适配器

    private Handler mHandler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            ChatMessage from = (ChatMessage) msg.obj;
            mDatas.add(from);
            rAdapter.notifyDataSetChanged();
            rChatView.smoothScrollToPosition(mDatas.size() - 1); //定位
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_chatting);

        initView();  //初始化

        rAdapter = new ChatMessageRecyclerViewAdapter(mDatas);
        rChatView.setAdapter(rAdapter);
        rChatView.smoothScrollToPosition(mDatas.size() - 1);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String msg = mMsg.getText().toString();
                if (TextUtils.isEmpty(msg)) {   //判断消息输入是否为空
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.emptyTips), Toast.LENGTH_SHORT).show();
                    return;
                }

                ChatMessage to = new ChatMessage(false, msg);
                to.setDate(new Date());
                mDatas.add(to);

                rAdapter.notifyDataSetChanged();
                rChatView.smoothScrollToPosition(mDatas.size() - 1);

                mMsg.setText("");

                new Thread()
                {
                    public void run()
                    {
                        ChatMessage from = null;
                        try {
                            from = HttpUtils.sendMsg(msg);
                        } catch (Exception e) {
                            from = new ChatMessage(true, getResources().getString(R.string.netError));
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

    private void initView() {
        send = (Button) findViewById(R.id.id_chat_send);
        tool = (ImageButton) findViewById(R.id.tool);
        rChatView = (RecyclerView) findViewById(R.id.id_chat_recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(MainActivity.this);
        rChatView.setLayoutManager(manager);
        mMsg = (EditText) findViewById(R.id.id_chat_msg);

        Connector.getDatabase();
        DataSupport.saveAll(mDatas);

        mDatas.add(new ChatMessage(true, getResources().getString(R.string.welcome)));  //消息初始化
        if (DataSupport.findAll(ChatMessage.class) != null && DataSupport.findAll(ChatMessage.class).size() > 0) {
            mDatas = DataSupport.findAll(ChatMessage.class);
        }
    }
}