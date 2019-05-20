package com.example.ayed.home;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

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
import java.util.ArrayList;
import java.util.List;


public class product_form extends AppCompatActivity {
    EditText title, description, price, location, phone;
    String user;
    public Spinner spinner;
    public Button btn_apply;
    public static final String myproduct = "com.example.ayed.home.myproduct";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_form);
        Intent i = getIntent();
        title = (EditText) findViewById(R.id.title);
        description = (EditText) findViewById(R.id.description);
        price = (EditText) findViewById(R.id.price);
        location = (EditText) findViewById(R.id.location);
        phone = (EditText) findViewById(R.id.phone);
        user = i.getStringExtra(Welcome.USERNAME);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btn_apply = (Button) findViewById(R.id.btn_apply);
        btn_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String type = "add";
                BackgroundWorker backgroundWorker = new BackgroundWorker(product_form.this);
                backgroundWorker.execute(type,spinner.getSelectedItem().toString(), title.getText().toString(), description.getText().toString(),
                        price.getText().toString(), location.getText().toString()
                        , phone.getText().toString(), user);

                Toast.makeText(getApplicationContext(), "Adding product ...", Toast.LENGTH_LONG).show();

            }
        });
        spinner = (Spinner) findViewById(R.id.spinner);

        List<String> list = new ArrayList<>();
        list.add("vehicules");
        list.add("buildings");
        list.add("home appliances");
        list.add("leisures");
        list.add("multimedia");
        list.add("other");


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String itemValue = parent.getItemAtPosition(position).toString();
                Toast.makeText(product_form.this, "Selected: " + itemValue, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
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
            Intent intentArticle = new Intent(product_form.this, MainActivity.class);
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
            Intent intentArticle = new Intent(product_form.this, Welcome.class);
            startActivity(intentArticle);
            return true;
        }
        if (id == R.id.aboutInfo) {
            Intent intentArticle = new Intent(product_form.this, aboutUs.class);
            intentArticle.putExtra(Welcome.USERNAME, r);
            startActivity(intentArticle);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    public void Open(View view) {
        startActivity(new Intent(this, MainActivity.class));

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
            String login_url = "http://192.168.1.100/WantIT/add.php";
            if (type.equals("add")) {
                try {
                    String catégorie = params[1];
                    String title = params[2];
                    String description = params[3];
                    String initialprice = params[4];
                    String location = params[5];
                    String phone = params[6];
                    String username = params[7];
                    URL url = new URL(login_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("catégorie", "UTF-8") + "=" + URLEncoder.encode(catégorie, "UTF-8") + "&" +
                            URLEncoder.encode("title", "UTF-8") + "=" + URLEncoder.encode(title, "UTF-8") + "&" +
                            URLEncoder.encode("description", "UTF-8") + "=" + URLEncoder.encode(description, "UTF-8") + "&" +
                            URLEncoder.encode("initialprice", "UTF-8") + "=" + URLEncoder.encode(initialprice, "UTF-8") + "&" +
                            URLEncoder.encode("location", "UTF-8") + "=" + URLEncoder.encode(location, "UTF-8") + "&" +
                            URLEncoder.encode("phone", "UTF-8") + "=" + URLEncoder.encode(phone, "UTF-8") + "&" +
                            URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
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
                alertDialog.setTitle("Adding Product ");
            }

            @Override
            protected void onPostExecute (String result){
                alertDialog.setMessage(result);
                alertDialog.show();
                if(result.equals("Adding Product Done"))
                {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            Intent i = getIntent();
                            String r = i.getStringExtra(Welcome.USERNAME);
                            Intent intent = new Intent(product_form.this, Change.class);
                            intent.putExtra(Welcome.USERNAME,r);
                            intent.putExtra(product_form.myproduct,title.getText().toString());
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
    }

