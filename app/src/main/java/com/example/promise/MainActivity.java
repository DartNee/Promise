package com.example.promise;

import static com.example.promise.retrofit.IPaddress.IPADRESS;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.promise.retrofit.RetrofitAPI;
import com.example.promise.retrofit.User_Model_dto;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {
    TextView user_login_id;
    int there_is_group = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button create_group_btn = (Button) findViewById(R.id.btn_create_group);

        user_login_id = (TextView) findViewById(R.id.user_id);

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        String user_login_id = sharedPref.getString("user_login_id", "");
        this.user_login_id.setText(user_login_id + "님 환영합니다.");


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(IPADRESS)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        //username으로 user객체 불러오기, user객체의 group_id가 null 인지 조회
        //   만약 null이면 생성, 아니면 "이미 그룹이 있습니다." 출력


                Call<User_Model_dto> call = retrofitAPI.findByUser_login_id_dto(user_login_id);
                call.enqueue(new Callback<User_Model_dto>() {
                    @Override
                    public void onResponse(Call<User_Model_dto> call, Response<User_Model_dto> response) {
                        if (!response.isSuccessful()) {
                            Log.e("연결이 비정상적 : ", "error code : " + response.code());
                            return;
                        }

                        User_Model_dto user_target = response.body();
                        Log.e("user_target", response.body().toString());
//                        Log.e("response.body().getGroup_model()",response.body().getGroup_model().toString());
//                        Log.e("user_target.getGroup_model()", user_target.getGroup_model().toString());
//                        Log.e("user_target.getGroup_model().getGroup_id()", user_target.getGroup_model().getId().toString());
                        if (response.body().getGroup_id() != null) {
                            // group이 이미 있음.
                            there_is_group = 1;
                        }
                        Log.e("there_is_group",there_is_group+"");
                    }

                    @Override
                    public void onFailure(Call<User_Model_dto> call, Throwable t) {
                        Log.e("연결실패", t.getMessage());
                    }
                });

        create_group_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (there_is_group == 1) {
                    Toast.makeText(getApplicationContext(), "이미 그룹이 있습니다.", Toast.LENGTH_SHORT).show();
                }
                else if(there_is_group == 0){
                    Log.e("there_is_group2번째",there_is_group+"");
                    Toast.makeText(getApplicationContext(), "그룹을 생성할 수 있습니다.", Toast.LENGTH_SHORT).show();
                    Log.e("그룹아이디", there_is_group + "");

                    Intent intent = new Intent(getApplicationContext(), Creating_Group.class);
                    startActivity(intent);
                }
            }
        });

        Button participate_group_btn = (Button) findViewById(R.id.btn_participe_group);
        participate_group_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (there_is_group == 1) {
                    Toast.makeText(getApplicationContext(), "이미 그룹이 있습니다.", Toast.LENGTH_SHORT).show();
                }
                else if(there_is_group == 0){
                    Log.e("there_is_group2번째",there_is_group+"");
                    Toast.makeText(getApplicationContext(), "그룹에 참가할 수 있습니다.", Toast.LENGTH_SHORT).show();
                    Log.e("그룹아이디", there_is_group + "");

                Intent intent = new Intent(getApplicationContext(), Participating_Group.class);
                startActivity(intent);
            }
        }});

        Button management_group_btn = (Button) findViewById(R.id.btn_group_management);
        management_group_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Management_Group.class);
                startActivity(intent);
            }
        });

        Button management_schedule_btn = (Button) findViewById(R.id.schedule_management);
        management_schedule_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Management_Schedule.class);
                startActivity(intent);
            }
        });


    }
}