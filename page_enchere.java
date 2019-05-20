package com.example.ayed.home;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

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

public class page_enchere extends AppCompatActivity {
    Button bid;
    ViewFlipper v_flipper;
    TextView title,announcer,textView23,category,city,brand,desc,currentPrice;
    EditText ajout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        final String r = i.getStringExtra(Welcome.USERNAME);
        final String v = i.getStringExtra(vehicule.myproduct);
        setContentView(R.layout.activity_page_enchere);
        title = (TextView)findViewById(R.id.title);
        announcer = (TextView)findViewById(R.id.announcer);
        textView23 = (TextView)findViewById(R.id.textView23);
        category = (TextView)findViewById(R.id.category);
        city = (TextView)findViewById(R.id.city);
        brand = (TextView)findViewById(R.id.brand);
        desc = (TextView)findViewById(R.id.description);
        currentPrice = (TextView)findViewById(R.id.currentPrice);
        ajout = (EditText)findViewById(R.id.ajout);
        title.setText(v);
        //announcer.setText(r);
        bid = (Button) findViewById(R.id.bid);






        getJSON("http://192.168.1.100/WantIT/page_enchere.php");

        bid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String type = "participate";
                page_enchere.BackgroundWorker backgroundWorker = new page_enchere.BackgroundWorker(page_enchere.this);
                backgroundWorker.execute(type,announcer.getText().toString(),v,r,ajout.getText().toString(),
                        currentPrice.getText().toString());

                Toast.makeText(getApplicationContext(), "Biding ...", Toast.LENGTH_LONG).show();

            }
        });
    }
    public void view_image(View view)
    {
        Intent i = getIntent();
        String r = i.getStringExtra(Welcome.USERNAME);
        String v = i.getStringExtra(vehicule.myproduct);
        Intent intent = new Intent(this, com.example.ayed.home.View.class);
        intent.putExtra(Welcome.USERNAME,r);
        intent.putExtra(vehicule.myproduct,v);
        startActivity(intent);
    }

    public void  flipperImages(int image )
    {
        ImageView imageView= new ImageView(this);
        imageView.setBackgroundResource(image);

        v_flipper.addView(imageView);
        v_flipper.setFlipInterval(1500);
        v_flipper.setAutoStart(true);


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return  super.onCreateOptionsMenu(menu) ;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent i = getIntent();
        String r = i.getStringExtra(Welcome.USERNAME);
        if(id==R.id.myProfil)
        {
            Intent intentProfil = new Intent(this, profile.class);
            intentProfil.putExtra(Welcome.USERNAME,r);
            startActivity(intentProfil);
            return true ;
        }
        if(id==R.id.newArticle)
        {
            Intent intentArticle = new Intent(this, product_form.class);
            intentArticle.putExtra(Welcome.USERNAME,r);
            startActivity(intentArticle);
            return true ;
        }
        if(id==R.id.home)
        {
            Intent intentArticle = new Intent(this, MainActivity.class);
            intentArticle.putExtra(Welcome.USERNAME,r);
            startActivity(intentArticle);
            return true ;
        }
        if (id == R.id.Mes_Produits) {
            Intent intentArticle = new Intent(this, MesProduits.class);
            intentArticle.putExtra(Welcome.USERNAME,r);
            startActivity(intentArticle);
            return true;
        }
        if(id==R.id.logOut)
        {
            Intent intentArticle = new Intent(this, Welcome.class);
            startActivity(intentArticle);
            return true ;
        }
        if(id==R.id.aboutInfo)
        {
            Intent intentArticle = new Intent(this, aboutUs.class);
            intentArticle.putExtra(Welcome.USERNAME,r);
            startActivity(intentArticle);
            return true ;
        }



        return super.onOptionsItemSelected(item);
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
                    String v = i.getStringExtra(vehicule.myproduct);
                    //creating a URL
                    URL url = new URL(urlWebService);

                    //Opening the URL using HttpURLConnection
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");
                    OutputStream os=con.getOutputStream();

                    //WRITE
                    BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
                    bw.write(new DataPackager1(v).packData());

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
        String[] categorie = new String[jsonArray.length()];
        String[] title = new String[jsonArray.length()];
        String[] description = new String[jsonArray.length()];
        String[] initialprice = new String[jsonArray.length()];
        String[] currentprice = new String[jsonArray.length()];
        String[] location = new String[jsonArray.length()];
        String[] phone = new String[jsonArray.length()];
        String[] username = new String[jsonArray.length()];

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            ids[i] = obj.getString("id");
            categorie[i] = obj.getString("categorie");
            title[i] = obj.getString("title");
            description[i] = obj.getString("description");
            initialprice[i] = obj.getString("initialprice");
            currentprice[i] = obj.getString("currentprice");
            location[i] = obj.getString("location");
            phone[i] = obj.getString("phone");
            username[i] = obj.getString("username");

        }

        //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, heroes);
        //listView.setAdapter(arrayAdapter);

        textView23.setText(initialprice[0]);
        announcer.setText(username[0]);
        category.setText(categorie[0]);
        city.setText(location[0]);
        brand.setText(phone[0]);
        desc.setText(description[0]);
        currentPrice.setText(currentprice[0]);
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
            String login_url = "http://192.168.1.100/WantIT/participate.php";
            if (type.equals("participate")) {
                try {
                    String announcer = params[1];
                    String title = params[2];
                    String participant = params[3];
                    String prix = params[4];
                    String currentprice = params[5];
                    URL url = new URL(login_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("announcer", "UTF-8") + "=" + URLEncoder.encode(announcer, "UTF-8") + "&" +
                            URLEncoder.encode("title", "UTF-8") + "=" + URLEncoder.encode(title, "UTF-8") + "&" +
                            URLEncoder.encode("participant", "UTF-8") + "=" + URLEncoder.encode(participant, "UTF-8") + "&" +
                            URLEncoder.encode("prix", "UTF-8") + "=" + URLEncoder.encode(prix, "UTF-8") + "&" +
                            URLEncoder.encode("currentprice", "UTF-8") + "=" + URLEncoder.encode(currentprice, "UTF-8");
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
            alertDialog.setTitle("Biding ... ");
        }

        @Override
        protected void onPostExecute (String result){
            alertDialog.setMessage(result);
            alertDialog.show();
            if(result.equals("Success"))
            {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        Intent i = getIntent();
                        String r = i.getStringExtra(Welcome.USERNAME);
                        Intent intent = new Intent(page_enchere.this, page_enchere.class);
                        intent.putExtra(Welcome.USERNAME,r);
                        intent.putExtra(vehicule.myproduct,title.getText().toString());
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
