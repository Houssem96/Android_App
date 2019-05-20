package com.example.ayed.home;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class vehicule extends AppCompatActivity {
    ListView listView;
    TextView title;
    private ListViewAdapter adapter;
    public static final String myproduct = "com.example.ayed.home.myproduct";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicule);
        listView = (ListView) findViewById(R.id.listView);
        title = (TextView)findViewById(R.id.title);
        getJSON("http://192.168.1.100/WantIT/vehicule.php");
    }
    class Produit{
        private String title,prix;

        public Produit(String title, String prix) {
            this.title = title;
            this.prix = prix;
        }
        public String getTitle() {return title;}
        public String getPrix() { return prix; }
    }
    public class ListViewAdapter extends BaseAdapter {
        Context c;
        ArrayList<Produit> produits;

        public ListViewAdapter(Context c, ArrayList<Produit> produits) {
            this.c = c;
            this.produits = produits;
        }
        @Override
        public int getCount() {return produits.size();}
        @Override
        public Object getItem(int i) {return produits.get(i);}
        @Override
        public long getItemId(int i) {return i;}
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if(view==null)
            {
                view= LayoutInflater.from(c).inflate(R.layout.row_model,viewGroup,false);
            }

            TextView title = view.findViewById(R.id.title);
            TextView prix = view.findViewById(R.id.prix);

            final Produit produit= (Produit) this.getItem(i);

            title.setText(produit.getTitle());
            prix.setText(produit.getPrix());

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(c, teacher.getName(), Toast.LENGTH_SHORT).show();
                    String ppp = produit.getTitle();
                    //Toast.makeText(vehicule.this,listItem.toString(),Toast.LENGTH_SHORT ).show();
                    Intent i = getIntent();
                    String r = i.getStringExtra(Welcome.USERNAME);
                    Intent intent = new Intent(vehicule.this, page_enchere.class);
                    intent.putExtra(Welcome.USERNAME,r);
                    intent.putExtra(vehicule.myproduct,ppp);
                    startActivity(intent);
                }
            });

            return view;
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
            Intent intentArticle = new Intent(vehicule.this, MainActivity.class);
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
            Intent intentArticle = new Intent(vehicule.this, Welcome.class);
            startActivity(intentArticle);
            return true;
        }
        if (id == R.id.aboutInfo) {
            Intent intentArticle = new Intent(vehicule.this, aboutUs.class);
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
        final ArrayList<Produit> produits = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(json);
        JSONObject jo;
        Produit produit;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                jo = jsonArray.getJSONObject(i);

                String title = jo.getString("title");
                String prix = jo.getString("currentprice");
                //String imageURL = jo.getString("teacher_image_url");

                produit = new Produit(title, prix);
                produits.add(produit);
            }

            //SET TO SPINNER
            adapter = new ListViewAdapter(this.getApplicationContext(), produits);
            listView.setAdapter(adapter);

        } catch (JSONException e) {
            //myProgressBar.setVisibility(View.GONE);
            Toast.makeText(this.getApplicationContext(), "GOOD RESPONSE BUT JAVA CAN'T PARSE JSON IT RECEIEVED. " + e.getMessage(), Toast.LENGTH_LONG).show();
        }


    }


}