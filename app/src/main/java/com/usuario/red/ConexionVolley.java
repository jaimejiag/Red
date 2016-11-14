package com.usuario.red;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Network;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import okhttp3.OkHttpClient;

public class ConexionVolley extends AppCompatActivity implements View.OnClickListener {
    EditText edtUrl;
    Button btnNavegar;
    WebView webWeb;
    public static final String TAG = "MyTag";
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conexion_volley);

        edtUrl = (EditText) findViewById(R.id.edt_urlVoley);
        webWeb = (WebView) findViewById(R.id.web_webVoley);

        btnNavegar = (Button) findViewById(R.id.btn_navegarVoley);
        btnNavegar.setOnClickListener(this);

        requestQueue = MySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
    }

    @Override
    public void onClick(View v) {
        String url = edtUrl.getText().toString();

        //Comentar y descomentar para comprobar los distintos tipos de conexión Voley.
        if (v == btnNavegar) {
            //makeSimpleRequest(url);
            //makeNetworkRequest(url);
            //makeOkHttpRequest(url);
            makeRequest(url);
        }
    }

    public void makeSimpleRequest(String url) {
        final String enlace = url;
        // Instantiate the RequestQueue.
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        webWeb.loadDataWithBaseURL(enlace, response, "text/html", "utf-8", null);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String message = "";
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            message = "Timeout Error " + error.getMessage();
                        } else if (error instanceof AuthFailureError) {
                            message = "AuthFailure Error " + error.getMessage();
                        } else if (error instanceof ServerError) {
                            message = "Server Error " + error.getMessage();
                        } else if (error instanceof NetworkError) {
                            message = "Network Error " + error.getMessage();
                        } else if (error instanceof ParseError) {
                            message = "Parse Error " + error.getMessage();
                        }
                        webWeb.loadDataWithBaseURL(null, message, "text/html", "utf-8", null);
                    }
                });

        // Set the tag on the request.
        stringRequest.setTag(TAG);
        // Set retry policy
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000, 1, 1));
        // Add the request to the RequestQueue.
        mRequestQueue.add(stringRequest);
    }

    public void makeNetworkRequest(String url) {
        final String enlace = url;
        RequestQueue mRequestQueue;
        // Instantiate the cache
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());
        // Instantiate the RequestQueue with the cache and network.
        mRequestQueue = new RequestQueue(cache, network);
        // Start the queue
        mRequestQueue.start();
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        webWeb.loadDataWithBaseURL(enlace, response, "text/html", "utf-8", null);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String message = "";
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            message = "Timeout Error " + error.getMessage();
                        } else if (error instanceof AuthFailureError) {
                            message = "AuthFailure Error " + error.getMessage();
                        } else if (error instanceof ServerError) {
                            message = "Server Error " + error.getMessage();
                        } else if (error instanceof NetworkError) {
                            message = "Network Error " + error.getMessage();
                        } else if (error instanceof ParseError) {
                            message = "Parse Error " + error.getMessage();
                        }
                        webWeb.loadDataWithBaseURL(null, message, "text/html", "utf-8", null);
                    }
                });

        // Set the tag on the request.
        stringRequest.setTag(TAG);
        // Set retry policy
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000, 1, 1));
        // Add the request to the RequestQueue.
        mRequestQueue.add(stringRequest);
    }

    public void makeOkHttpRequest(String url) {
        final String enlace = url;
        // Instantiate the RequestQueue.
        OkHttpClient myOkHttpClient = new OkHttpClient();
        OkHttp3Stack myOkHttp3Stack = new OkHttp3Stack(myOkHttpClient);
        requestQueue = Volley.newRequestQueue(this, myOkHttp3Stack);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        webWeb.loadDataWithBaseURL(enlace, response, "text/html", "utf-8", null);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String message = "";
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            message = "Timeout Error " + error.getMessage();
                        } else if (error instanceof AuthFailureError) {
                            message = "AuthFailure Error " + error.getMessage();
                        } else if (error instanceof ServerError) {
                            message = "Server Error " + error.getMessage();
                        } else if (error instanceof NetworkError) {
                            message = "Network Error " + error.getMessage();
                        } else if (error instanceof ParseError) {
                            message = "Parse Error " + error.getMessage();
                        }
                        webWeb.loadDataWithBaseURL(null, message, "text/html", "utf-8", null);
                    }
                });

        // Set the tag on the request.
        stringRequest.setTag(TAG);
        // Set retry policy
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000, 1, 1));
        // Add the request to the RequestQueue.
        requestQueue.add(stringRequest);
    }

    public void makeRequest(String url) {
        final String enlace = url;
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        webWeb.loadDataWithBaseURL(enlace, response, "text/html", "utf-8", null);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String message = "";
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            message = "Timeout Error " + error.getMessage();
                        } else if (error instanceof AuthFailureError) {
                            message = "AuthFailure Error " + error.getMessage();
                        } else if (error instanceof ServerError) {
                            message = "Server Error " + error.getMessage();
                        } else if (error instanceof NetworkError) {
                            message = "Network Error " + error.getMessage();
                        } else if (error instanceof ParseError) {
                            message = "Parse Error " + error.getMessage();
                        }
                        webWeb.loadDataWithBaseURL(null, message, "text/html", "utf-8", null);
                    }
                });

        // Set the tag on the request.
        stringRequest.setTag(TAG);
        // Set retry policy
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000, 1, 1));
        // Add the request to the RequestQueue.
        requestQueue.add(stringRequest);
    }


    @Override
    protected void onStop() {
        super.onStop();

        if(requestQueue!=null) {
            requestQueue.cancelAll(TAG);
        }
    }
}
