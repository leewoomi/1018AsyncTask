package com.example.a503_08.a1018asynctask;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Random;

public class LooperActivity extends AppCompatActivity {

    ListView evenListView, oddListView;

    ArrayList<String> evendata;
    ArrayList<String> odddata;


    ArrayAdapter<String> evenadapter;
    ArrayAdapter<String> oddadapter;

    OneThread oneThread;
    TwoThread twoThread;


    Handler handler;

    class OneThread extends Thread {
        //스레드 내부에 핸들러 만들기
        Handler oneHandler;
        public void run(){
            //스레드 내부에서 핸들러를 만들 때는 Looper 이용
            Looper.prepare();
            oneHandler = new Handler(){
                @Override
                public void handleMessage(Message message){
                    //1초 대기
                    SystemClock.sleep(1000);
                    //메시지의 전송 내용 가져오기
                    final int data = message.arg1;
                    //메시지를 구분
                    if(message.what == 0){
                        //다른 작업이 없을 때 처리
                        handler.post(new Runnable(){
                            @Override
                            public void run(){
                                //데이터를 추가하고 리스트 뷰 다시 출력
                                evendata.add("even:" + data);
                                evenadapter.notifyDataSetChanged();
                            }
                        });
                    }else{
                        //다른 작업이 없을 때 처리
                        handler.post(new Runnable(){
                            @Override
                            public void run(){
                                //데이터를 추가하고 리스트 뷰 다시 출력
                                odddata.add("odd:" + data);
                                oddadapter.notifyDataSetChanged();
                            }
                        });
                    }
                }
            };
            //루퍼를 반복
            Looper.loop();
        }
    }


    //랜덤한 정소를 생성해서 oneThread에게 메시지를 전송하는 스레드 클래스
    class TwoThread extends Thread{
        @Override
        public void run() {
            Random r = new Random();
            for(int i=0; i<10; i=i+1){
                SystemClock.sleep(100);
                int data = r.nextInt(10);
                Message message = new Message();
                if(data % 2 == 0 ){
                    message.what =0;
                }else {
                    message.what =1;
                }

                message.arg1 = data;
                oneThread.oneHandler.sendMessage(message);
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_looper);

        evenListView = (ListView) findViewById(R.id.evenListView);
        oddListView = (ListView) findViewById(R.id.oddListView);



        evendata = new ArrayList<>();
        odddata = new ArrayList<>();

        evenadapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,evendata);
        oddadapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,odddata);

        evenListView.setAdapter(evenadapter);
        oddListView.setAdapter(oddadapter);

        handler = new Handler();

        oneThread = new OneThread();
        oneThread.start();


        twoThread = new TwoThread();
        twoThread.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        oneThread.oneHandler.getLooper().quit();
    }
}
