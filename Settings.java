package com.example.ayed.home;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class Settings extends AppCompatActivity {
    EditText et_name,et_surname,et_password,et_conf_password,et_phone;
    TextView et_username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        et_username = (TextView) findViewById(R.id.et_username);
        et_name = (EditText)findViewById(R.id.et_name);
        et_surname = (EditText)findViewById(R.id.et_surname);
        et_password = (EditText)findViewById(R.id.et_password);
        et_conf_password = (EditText)findViewById(R.id.et_conf_password);
        et_phone = (EditText)findViewById(R.id.et_phone);
        getJSON("http://192.168.1.100/WantIT/profile.php");
    }
    public void update(View view) {
        String type = "update";
        Settings.BackgroundWorker backgroundWorker = new Settings.BackgroundWorker(Settings.this);
        backgroundWorker.execute(type, et_username.getText().toString(), et_name.getText().toString(),
                et_surname.getText().toString(), et_password.getText().toString()
                , et_phone.getText().toString());

        Toast.makeText(getApplicationContext(), "Updating Information ...", Toast.LENGTH_LONG).show();
    }

    private void getJSON(final String urlWebService) {
        /*
         * As fetching the json string is a network operation
         * And we cannot perform a network operation in main thread
         * so we need an AsyncTask
         * The constrains defined here are
         * Void -&gt; We are not passing anything
         * Void -&gt; Nothing at progress update as well
         * String -&gt; After completion it should return a string and it will be the json string
         * */
        class GetJSON extends AsyncTask<Void, Void, String> {

            //this method will be called before execution
            //you can display a progress bar or something
            //so that user can understand that he should wait
            //as network operation may take some time
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            //this method will be called after execution
            //so here we are displaying a toast with the json string
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                try {
                    loadIntoListView(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            //in this method we are fetching the json string
            @Override
            protected String doInBackground(Void... voids) {



                try {
                    Intent i = getIntent();
                    String r = i.getStringExtra(Welcome.USERNAME);
                    //creating a URL
                    URL url = new URL(urlWebService);

                    //Opening the URL using HttpURLConnection
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");
                    OutputStream os=con.getOutputStream();

                    //WRITE
                    BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
                    bw.write(new DataPackager(r).packData());

                    bw.flush();

                    //RELEASE RES
                    bw.close();
                    os.close();

                    //StringBuilder object to read the string from the service
                    StringBuilder sb = new StringBuilder();

                    //We will use a buffered reader to read the string from service
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    //A simple string to read values from each line
                    String json;

                    //reading until we don't find null
                    while ((json = bufferedReader.readLine()) != null) {

                        //appending it to string builder
                        sb.append(json + "\n");
                    }

                    //finally returning the read string
                    return sb.toString().trim();
                } catch (Exception e) {
                    return null;
                }

            }
        }

        //creating asynctask object and executing it
        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }

    private void loadIntoListView(String json) throws JSONException {

        JSONArray jsonArray = new JSONArray(json);
        String[] ids = new String[jsonArray.length()];
        String[] username = new String[jsonArray.length()];
        String[] name = new String[jsonArray.length()];
        String[] surname = new String[jsonArray.length()];
        String[] password = new String[jsonArray.length()];
        String[] phone = new String[jsonArray.length()];

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            ids[i] = obj.getString("id");
            username[i] = obj.getString("username");
            name[i] = obj.getString("name");
            surname[i] = obj.getString("surname");
            password[i] = obj.getString("password");
            phone[i] = obj.getString("phone");

        }

        //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, heroes);
        //listView.setAdapter(arrayAdapter);
        et_username.setText(username[0]);
        et_name.setText(name[0]);
        et_surname.setText(surname[0]);
        et_password.setText(password[0]);
        et_conf_password.setText(password[0]);
        et_phone.setText(phone[0]);

    }
    private class BackgroundWorker extends AsyncTask<String, Void, String> {
        Context context;
        AlertDialog alertDialog;

        BackgroundWorker(Context ctx) {
            context = ctx;
        }


        @Override
        protected String doInBackground(String... params) {
            String type = params[0];
            String login_url = "http://192.168.1.100/WantIT/update.php";
            if (type.equals("update")) {
                try {
                    String username = params[1];
                    String name = params[2];
                    String surname = params[3];
                    String password = params[4];
                    String phone = params[5];
                    URL url = new URL(login_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8") + "&" +
                            URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8") + "&" +
                            URLEncoder.encode("surname", "UTF-8") + "=" + URLEncoder.encode(surname, "UTF-8") + "&" +
                            URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8") + "&" +
                            URLEncoder.encode("phone", "UTF-8") + "=" + URLEncoder.encode(phone, "UTF-8");
                    bufferedWriter.write(post_data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                    String result = "";
                    String line = "";
                    while ((line = bufferedReader.readLine()) != null) {
                        result += line;
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return result;

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPreExecute () {
            alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setTitle("Updating informations ");
        }

        @Override
        protected void onPostExecute (String result){
            alertDialog.setMessage(result);
            alertDialog.show();
            if(result.equals("Done"))
            {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        Intent i = getIntent();
                        String r = i.getStringExtra(Welcome.USERNAME);
                        Intent intent = new Intent(Settings.this, profile.class);
                        intent.putExtra(Welcome.USERNAME,r);
                        startActivity(intent);
                    }
                }, 2000);


            }
        }

        @Override
        protected void onProgressUpdate (Void...values){
            super.onProgressUpdate(values);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater() ;
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent i = getIntent();
        String r = i.getStringExtra(Welcome.USERNAME);
        if (id == R.id.myProfil) {
            Intent intentProfil = new Intent(this, profile.class);
            intentProfil.putExtra(Welcome.USERNAME,r);
            startActivity(intentProfil);
            return true;
        }
        if (id == R.id.newArticle) {
            Intent intentArticle = new Intent(this, product_form.class);
            intentArticle.putExtra(Welcome.USERNAME,r);
            startActivity(intentArticle);
            return true;
        }
        if (id == R.id.home) {
            Intent intentArticle = new Intent(this, MainActivity.class);
            intentArticle.putExtra(Welcome.USERNAME, r);
            startActivity(intentArticle);
            return true;
        }
        if (id == R.id.Mes_Produits) {
            Intent intentArticle = new Intent(this, MesProduits.class);
            intentArticle.putExtra(Welcome.USERNAME,r);
            startActivity(intentArticle);
            return true;
        }
        if (id == R.id.logOut) {
            Intent intentArticle = new Intent(this, Welcome.class);
            startActivity(intentArticle);
            return true;
        }
        if (id == R.id.aboutInfo) {
            Intent intentArticle = new Intent(this, aboutUs.class);
            intentArticle.putExtra(Welcome.USERNAME, r);
            startActivity(intentArticle);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
}
