package com.example.ayed.home;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

public class Produit extends AppCompatActivity {
    ListView listView;
    String num;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produit);
        listView = (ListView)findViewById(R.id.listView);
        getJSON("http://192.168.1.100/WantIT/mon_produit.php");
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Object listItem = listView.getItemAtPosition(position);
                //Toast.makeText(Produit.this,listItem.toString(),Toast.LENGTH_SHORT ).show();
                Intent i = getIntent();
                final String r = i.getStringExtra(Welcome.USERNAME);
                final String p = i.getStringExtra(MesProduits.product);
                AlertDialog.Builder builder;
                Context context = Produit.this;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(context);
                }
                builder.setTitle("Mr "+r+" :")
                        .setMessage("Do you want to close the auction sale of "+p+" to "+listItem.toString()+" ?")
                        .setPositiveButton("Close & Call", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String type = "close";
                                Produit.BackgroundWorker backgroundWorker = new Produit.BackgroundWorker(Produit.this);
                                backgroundWorker.execute(type,p,listItem.toString());

                                Toast.makeText(getApplicationContext(), "Closing ...", Toast.LENGTH_LONG).show();

                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i = getIntent();
                                String r = i.getStringExtra(Welcome.USERNAME);
                                Intent intent = new Intent(Produit.this, MesProduits.class);
                                intent.putExtra(Welcome.USERNAME,r);
                                startActivity(intent);
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();


            }
        });
    }
    private void dialContactPhone(final String phoneNumber) {
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
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
        if (id == R.id.Mes_Produits) {
            Intent intentArticle = new Intent(this, MesProduits.class);
            intentArticle.putExtra(Welcome.USERNAME,r);
            startActivity(intentArticle);
            return true;
        }
        if (id == R.id.home) {
            Intent intentArticle = new Intent(Produit.this, MainActivity.class);
            intentArticle.putExtra(Welcome.USERNAME, r);
            startActivity(intentArticle);
            return true;
        }
        if (id == R.id.logOut) {
            Intent intentArticle = new Intent(Produit.this, Welcome.class);
            startActivity(intentArticle);
            return true;
        }
        if (id == R.id.aboutInfo) {
            Intent intentArticle = new Intent(Produit.this, aboutUs.class);
            intentArticle.putExtra(Welcome.USERNAME, r);
            startActivity(intentArticle);
            return true;
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
                    String v = i.getStringExtra(MesProduits.product);
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
        String[] announcer = new String[jsonArray.length()];
        String[] title = new String[jsonArray.length()];
        String[] partcipant = new String[jsonArray.length()];
        String[] prix = new String[jsonArray.length()];

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            ids[i] = obj.getString("id");
            announcer[i] = obj.getString("announcer");
            title[i] = obj.getString("title");
            partcipant[i] = obj.getString("partcipant");
            prix[i] = obj.getString("prix");

        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, partcipant);
        listView.setAdapter(arrayAdapter);

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
            String login_url = "http://192.168.1.100/WantIT/close.php";
            if (type.equals("close")) {
                try {
                    String title = params[1];
                    String participant = params[2];
                    URL url = new URL(login_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("title", "UTF-8") + "=" + URLEncoder.encode(title, "UTF-8") + "&" +
                            URLEncoder.encode("participant", "UTF-8") + "=" + URLEncoder.encode(participant, "UTF-8");
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
            alertDialog.setTitle("Closing ... ");
        }

        @Override
        protected void onPostExecute (String result){
            Intent i = getIntent();
            String r = i.getStringExtra(Welcome.USERNAME);
            Intent intent = new Intent(Produit.this, MesProduits.class);
            intent.putExtra(Welcome.USERNAME,r);
            startActivity(intent);
            dialContactPhone(result);


        }

        @Override
        protected void onProgressUpdate (Void...values){
            super.onProgressUpdate(values);
        }
    }

}

