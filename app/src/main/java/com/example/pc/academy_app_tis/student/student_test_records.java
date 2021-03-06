package com.example.pc.academy_app_tis.student;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.pc.academy_app_tis.R;
import com.example.pc.academy_app_tis.teacher.Feed_Adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class student_test_records extends AppCompatActivity implements Test_Marks_Adapter.Test_Marks_AdapterOnClickHandler {
    String username;
    RecyclerView recyclerView;
    Test_Marks_Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_test_records);
        recyclerView=(RecyclerView)findViewById(R.id.recycler_102);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
      //  Toast.makeText(student_test_records.this,pref.getString("username", null),Toast.LENGTH_SHORT).show();

        username=pref.getString("username", null);
        new Background_getting_marks().execute();
    }

    @Override
    public void onClick(int x) {

    }


    class Background_getting_marks extends AsyncTask<Void,Void,String>
    {   String json_url="https://tisabcd12.000webhostapp.com/student/getting_marks.php?username="+username;

        @Override
        protected void onPreExecute() {
            //   Toast.makeText(login_signup.this,"Hey",Toast.LENGTH_SHORT).show();
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(String JSON_STRING) {
            JSONObject jsonObject;
            JSONArray jsonArray;
            //Toast.makeText(getApplicationContext(),JSON_STRING,Toast.LENGTH_LONG).show();




            try {
                jsonObject=new JSONObject(JSON_STRING);
                int count=0;


                jsonArray=jsonObject.getJSONArray("server response");
                int size=jsonArray.length();
                String[] title_array = new String[size];
                String[] message_Array=new String[size];
                String[] fwion_array=new String[size];
                String[] marks=new String[size];
                while(count<jsonArray.length())
                {
                    JSONObject JO=jsonArray.getJSONObject(count);
                    fwion_array[count]=JO.getString("batch_subject");
                    title_array[count]=JO.getString("test_name");
                    message_Array[count]=JO.getString("marks_obtained");
                    marks[count]=JO.getString("total_marks");


                    count++;


                }
                LinearLayoutManager layoutManager=new LinearLayoutManager(student_test_records.this);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setHasFixedSize(true);
                adapter=new Test_Marks_Adapter(student_test_records.this);
                recyclerView.setAdapter(adapter);
                adapter.swapCursor(getApplicationContext(),fwion_array,title_array,message_Array,marks);







            } catch (JSONException e) {
                e.printStackTrace();
            }


            super.onPostExecute(JSON_STRING);
        }



        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(Void... voids) {
            String json_string;
            try {
                URL url=new URL(json_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder=new StringBuilder();
                while((json_string=bufferedReader.readLine())!=null)
                {
                    stringBuilder.append(json_string+"\n");

                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }
    }
}
