package com.usuario.red;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.loopj.android.http.TextHttpResponseHandler;

public class ConexionAsincrona extends AppCompatActivity implements View.OnClickListener {

    EditText edtUrl;
    RadioButton rbtnJava, rbtnApache, rbtnAAHC;
    Button btnConectar;
    WebView webvWeb;
    TextView txvResultado;
    public static final String JAVA = "Java";
    public static final String APACHE = "Apache";
    long inicio, fin;
    TareaAsincrona miTarea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conexion_asincrona);

        iniciar();
    }

    private void iniciar() {
        edtUrl = (EditText) findViewById(R.id.edt_url);
        rbtnJava = (RadioButton) findViewById(R.id.rbtn_java);
        rbtnApache = (RadioButton) findViewById(R.id.rbtn_apache);
        rbtnAAHC = (RadioButton) findViewById(R.id.rbtn_AAHC);
        btnConectar = (Button) findViewById(R.id.btn_conectar);
        btnConectar.setOnClickListener(this);
        webvWeb = (WebView) findViewById(R.id.webv_web);
        txvResultado = (TextView) findViewById(R.id.txv_resultado);
        //StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
    }

    @Override
    public void onClick(View v) {
        String texto = edtUrl.getText().toString();
        String tipo = APACHE;
        Resultado resultado;

        if (v == btnConectar) {
            inicio = System.currentTimeMillis();

            if (rbtnAAHC.isChecked())
                AAHC();
            else {
                if (rbtnJava.isChecked())
                    tipo = JAVA;

                miTarea = new TareaAsincrona(this);
                miTarea.execute(tipo, texto);
            }
            txvResultado.setText("Esperando...");
        }
    }


    public class TareaAsincrona extends AsyncTask<String, Integer, Resultado> {
        private ProgressDialog progreso;
        private Context context;

        public TareaAsincrona(Context context) {
            this.context = context;
        }

        protected void onPreExecute() {
            progreso = new ProgressDialog(context);
            progreso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progreso.setMessage("Conectando . . .");
            progreso.setCancelable(true);

            progreso.setOnCancelListener(new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface dialog) {
                    TareaAsincrona.this.cancel(true);
                }
            });

            progreso.show();
        }

        //Los (...) al lado del tipo significa que es array, pero no sirve poner "[]".
        protected Resultado doInBackground(String... cadena) {
            Resultado resultado;
            int i = 1;

            try {
                // operaciones en el hilo secundario
                publishProgress(i++);

                if (cadena[0].equals(JAVA))
                    resultado = Conexion.conectarJava(cadena[1]);
                else
                    resultado = Conexion.conectarApache(cadena[1]);

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
                resultado = new Resultado();
                resultado.setCodigo(false);
                resultado.setMensaje(e.getMessage());
                cancel(true);
            }

            return resultado;
        }

        protected void onProgressUpdate(Integer... progress) {
            progreso.setMessage("Conectando " + Integer.toString(progress[0]));
        }

        protected void onPostExecute(Resultado resultado) {
            progreso.dismiss();
            fin = System.currentTimeMillis();

            // mostrar el resultado
            if (resultado.isCodigo())
                webvWeb.loadDataWithBaseURL(null, resultado.getContenido(), "text/html", "UTF-8", null);
            else
                webvWeb.loadDataWithBaseURL(null, resultado.getMensaje(), "text/html", "UTF-8", null);

            txvResultado.setText("Duración: " + String.valueOf(fin - inicio) + " milisegundos");
        }

        protected void
        onCancelled() {
            progreso.dismiss();
            fin = System.currentTimeMillis();

            // mostrar cancelación
            webvWeb.loadDataWithBaseURL(null, "Cancelado", "text/html", "UTF-8", null);
            txvResultado.setText("Duración: " + String.valueOf(fin - inicio) + " milisegundos");
        }
    }


    //Esto es el tercer ejercicio de ejemplo
    //
    private void AAHC() {
        final String texto = edtUrl.getText().toString();
        final long inicio;
        final long[] fin = new long[1];
        final ProgressDialog progreso = new ProgressDialog(ConexionAsincrona.this);

        inicio = System.currentTimeMillis();

        RestClient.get(texto, new TextHttpResponseHandler() {
            @Override
            public void onStart() {
                // called before request is started
                progreso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progreso.setMessage("Conectando . . .");
                //progreso.setCancelable(false);
                progreso.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    public void onCancel(DialogInterface dialog) {
                        RestClient.cancelRequests(getApplicationContext(), true);
                    }
                });
                progreso.show();
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                fin[0] = System.currentTimeMillis();
                progreso.dismiss();
                webvWeb.loadDataWithBaseURL(texto, "Fallo: " + responseString + " " + throwable.getMessage(), "text/html", "UTF-8", null);
                txvResultado.setText("Duración: " + String.valueOf(fin[0] - inicio) + " milisegundos");
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                // called when response HTTP status is "200"
                fin[0] = System.currentTimeMillis();
                progreso.dismiss();
                webvWeb.loadDataWithBaseURL(texto, responseString, "text/html", "UTF-8", null);
                txvResultado.setText("Duración: " + String.valueOf(fin[0] - inicio) + " milisegundos");
            }
        });
    }
}

