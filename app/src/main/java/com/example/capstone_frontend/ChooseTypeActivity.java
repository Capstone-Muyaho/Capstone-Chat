package com.example.capstone_frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ImageButton;

public class ChooseTypeActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton button1 = findViewById(R.id.btnGrandma);
        ImageButton button2 = findViewById(R.id.btnSon);
        button1.setOnClickListener(new View.OnClickListener() {//버튼 이벤트 처리
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChooseTypeActivity.this, MainActivity.class);
                Toast.makeText(getApplicationContext(),"버튼 클릭 성공",Toast.LENGTH_SHORT).show();
                //버튼 클릭시 Toast 메세지"버튼 클릭 성공" 출력
                startActivity(intent);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {//버튼 이벤트 처리
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChooseTypeActivity.this, MainActivity.class);
                Toast.makeText(getApplicationContext(),"버튼 클릭 성공",Toast.LENGTH_SHORT).show();
                //버튼 클릭시 Toast 메세지"버튼 클릭 성공" 출력
                startActivity(intent);
            }
        });
    }
}
