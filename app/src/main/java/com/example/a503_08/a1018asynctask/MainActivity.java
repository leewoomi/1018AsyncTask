package com.example.a503_08.a1018asynctask;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

class AsyncTaskEx
        extends AsyncTask<Integer, Integer, Integer> {
    //MainActivity 의 데이터를 저장하기 위한 변수
    ProgressBar progressBar;
    TextView textView;
    int value;

    //생성자 - MainActivity 에서 데이터를 넘겨받기 위해 생성
    public AsyncTaskEx(
            ProgressBar progressBar, TextView textView,
            int value) {
        this.progressBar = progressBar;
        this.textView = textView;
        this.value = value;

        Log.e("생성자","생성자");
    }


    @Override
    //인스턴스가 생성되면 가장 먼저 호출되는 메소드
    //메인 스레드에서 수행 : 화면을 갱신하는 코드를 작성해도 됩니다.
    protected void onPreExecute() {
        super.onPreExecute();
        //UI 초기화
        value = 0;
        progressBar.setProgress(value);
    }

    @Override
    //비동기 적인 작업을 처리하는 메소드
    //매개변수는 클래스를 생성할 때 적용한 제너릭의 두번째 자료형
    //과 일치해야 합니다.
    //리턴 타입은 세번째 자료형과 일치해야 합니다.
    //메인스레드에서 동작하지 않음
    //UI 갱신하는 코드는 작성할 수 없음
    protected Integer doInBackground(Integer... integers) {
        while (isCancelled() == false) {
            value = value + 1;
            if (value >= 100) {
                break;
            } else {
                //onProgressUpdate 호출
                publishProgress(value);
            }
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                Log.e("문제 발생", e.getMessage());
            }

        }
        return value;
    }

    //doInBackground에서 publishProgress를 호출하면
    //자동으로 호출되는 메소드
    //메인스레드에서 수행
    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        progressBar.setProgress(value);
        textView.setText(
                String.format("현재 값:%d", value));
    }

    //인스턴스가 cancelled를 호출했을 때 호출되는 메소드
    //메인 스레드에서 실행
    @Override
    protected void onCancelled(Integer integer) {
        super.onCancelled(integer);
        textView.setText("스레드 중지");
    }

    //doInBackground가 작업을 종료했을 때 호출되는 메소드
    //메인 스레드에서 실행
    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
        value = 0;
        progressBar.setProgress(value);
        textView.setText("스레드 종료");
    }
}


public class MainActivity extends AppCompatActivity {


    Button start, end;

    int value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView textView = (TextView) findViewById(R.id.textView);
        Button start = (Button) findViewById(R.id.start);
        Button end = (Button) findViewById(R.id.end);
        final ProgressBar progress = (ProgressBar) findViewById(R.id.progress);



      View.OnClickListener clickListener = new View.OnClickListener() {
          AsyncTaskEx task = null;
          @Override
          public void onClick(View v) {
         switch (v.getId()){
             case R.id.start :
                 task = new AsyncTaskEx(progress,textView,value);
                 task.execute(100);
                 break;

             case R.id.end:
                 task.cancel(true);
                 break;



         }

          }
      };
      start.setOnClickListener(clickListener);
      end.setOnClickListener(clickListener);

    }
}
