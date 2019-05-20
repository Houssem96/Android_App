package com.example.ayed.home;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Welcome extends AppCompatActivity {

    EditText  editPassword, editUsername;
    Button btnLogin, btnRegister;
    public static final String USERNAME = "com.example.ayed.home.USERNAME";
    String URL= "http://192.168.1.100/WantIT/index.php";

    Login_Register loginRegister =new Login_Register();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        editUsername=(EditText)findViewById(R.id.etUsername);
        editPassword=(EditText)findViewById(R.id.etPassword);

        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnRegister = (Button)findViewById(R.id.btnRegister);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Welcome.AttemptLogin welcomeLogin= new Welcome.AttemptLogin();
                welcomeLogin.execute(editUsername.getText().toString(),editPassword.getText().toString());
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Welcome.super.getApplicationContext(), Register.class);
                startActivity(intent);
            }
        });

    }

    private class AttemptLogin extends AsyncTask<String, String, JSONObject> {

        @Override

        protected void onPreExecute() {

            super.onPreExecute();

        }

        @Override

        protected JSONObject doInBackground(String... args) {


            String password = args[1];
            String username= args[0];

            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("username", username));
            params.add(new BasicNameValuePair("password", password));

            JSONObject json = loginRegister.makeHttpRequest(URL, "POST", params);
            return json;

        }

        protected void onPostExecute(JSONObject result) {
            try {
                if (result != null) {
                    Toast.makeText(getApplicationContext(),result.getString("message"),Toast.LENGTH_LONG).show();
                    if(result.getString("message").equals("Successfully logged in"))
                    {
                        Intent i = new Intent(Welcome.this,MainActivity.class);
                        i.putExtra(USERNAME,editUsername.getText().toString());
                        startActivity(i);

                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Unable to retrieve any data from server", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

    }
}
