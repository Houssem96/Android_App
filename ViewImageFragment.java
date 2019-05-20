package com.example.ayed.home;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by VisH on 05-02-2017.
 */

public class ViewImageFragment extends Fragment{
    View rootView;
    EditText name;
    Button ViewImage;
    String url =null;
    NetworkImageView previewImage;
    ImageLoader imageLoader;
    Activity mActivity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setRetainInstance(true);
        rootView = inflater.inflate(R.layout.view_image_layout, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        previewImage = (NetworkImageView)rootView.findViewById(R.id.previewImage);
        name = (EditText)rootView.findViewById(R.id.name);
        ViewImage = (Button)rootView.findViewById(R.id.viewButton);
        if (getArguments() != null) {
            name.setText(getArguments().getString("params"));
        }
        imageLoader = CustomVolleyRequest.getInstance(getActivity().getApplicationContext())
                .getImageLoader();

        ViewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FetchImage();
            }
        });
        return rootView;

    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }
    public void FetchImage() {
        {
            StringRequest stringRequest = new StringRequest(Request.Method.POST,"http://192.168.1.100/fetch_image.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {


                            try {
                                JSONObject res = new JSONObject(response);
                                JSONArray thread = res.getJSONArray("image");
                                for (int i = 0; i < thread.length(); i++) {
                                    JSONObject obj = thread.getJSONObject(i);
                                    url  = obj.getString("photo");


                                }

                                imageLoader.get(url, ImageLoader.getImageListener(previewImage
                                        ,0,android.R.drawable
                                                .ic_dialog_alert));
                                previewImage.setImageUrl(url, imageLoader);



                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("name",name.getText().toString());
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(mActivity);

            //Adding request to the queue
            requestQueue.add(stringRequest);
        }

    }
}
