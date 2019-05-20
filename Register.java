package com.example.ayed.home;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Register extends AppCompatActivity {

    EditText  editUsername,editName, editSurname, editPassword, edit_Conf_Password, editPhone;
    Button btnRegister;

    String URL= "http://192.168.1.100/WantIT/index.php";

    Login_Register loginRegister =new Login_Register();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        editUsername=(EditText)findViewById(R.id.et_username);
        editName=(EditText)findViewById(R.id.et_name);
        editSurname=(EditText)findViewById(R.id.et_surname);
        editPassword=(EditText)findViewById(R.id.et_password);
        edit_Conf_Password=(EditText)findViewById(R.id.et_conf_password);
        editPhone=(EditText)findViewById(R.id.et_phone);

        btnRegister=(Button)findViewById(R.id.btn_register);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editPassword.getText().toString().equals(edit_Conf_Password.getText().toString())) {

                    Register.AttemptLogin attemptRegister = new Register.AttemptLogin();
                    attemptRegister.execute(editUsername.getText().toString(), editName.getText().toString(),
                            editSurname.getText().toString(), editPassword.getText().toString()
                            , editPhone.getText().toString());
                }else{
                    Toast.makeText(getApplicationContext(),"Champs manquants ou mots de passes non identiquent !",Toast.LENGTH_LONG).show();
                }
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

            String username = args[0];
            String name= args[1];
            String surname = args[2];
            String password = args[3];
            String phone= args[4];

            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("username", username));
            params.add(new BasicNameValuePair("name", name));
            params.add(new BasicNameValuePair("surname", surname));
            params.add(new BasicNameValuePair("password", password));
            params.add(new BasicNameValuePair("phone", phone));

            JSONObject json = loginRegister.makeHttpRequest(URL, "POST", params);


            return json;

        }

        protected void onPostExecute(JSONObject result) {

            try {
                if (result != null) {
                    Toast.makeText(getApplicationContext(),result.getString("message"),Toast.LENGTH_LONG).show();
                    if(result.getString("message").equals("Successfully registered the user"))
                    {
                        Intent intent = new Intent(Register.super.getApplicationContext(),Welcome.class);
                        startActivity(intent);
                        editUsername.setText("");
                        editName.setText("");
                        editSurname.setText("");
                        editPassword.setText("");
                        edit_Conf_Password.setText("");
                        editPhone.setText("");
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
