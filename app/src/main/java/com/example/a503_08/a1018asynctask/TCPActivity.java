package com.example.a503_08.a1018asynctask;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;

public class TCPActivity extends AppCompatActivity {

    EditText ip,msg,port;
    TextView result;
    Button send;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Toast.makeText(TCPActivity.this,msg.obj.toString(),Toast.LENGTH_SHORT).show();
        }
    };

    //서버에게 메시지를 전송하고 받아올 스레드
    class ThreadEx extends Thread {
        @Override
        public void run() {
            String ipaddr = ip.getText().toString();
            String portnumber= port.getText().toString();
            String str = msg.getText().toString();

            try {
                //소켓 생성
                Socket socket = new Socket(ipaddr,Integer.parseInt(portnumber));
                //문자열을 전송하기 위한 스트림을 생성
                PrintWriter pw = new PrintWriter(socket.getOutputStream());
                pw.println(str);
                pw.flush();

                //문자열을 입력 받기 위한 스트림을 생성
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                Message message = new Message();
                message.obj = br.readLine();
                handler.sendMessage(message);

                br.close();
                pw.close();

            } catch (IOException e) {
                Log.e("소켓",e.getMessage());
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tcp);


        ip= (EditText)findViewById(R.id.ip);
        port = (EditText)findViewById(R.id.port);
        msg = (EditText)findViewById(R.id.msg);

        result = (TextView)findViewById(R.id.result);

        send=(Button)findViewById(R.id.send);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThreadEx threadEx = new ThreadEx();
                threadEx.start();
            }
        });

    }
}
