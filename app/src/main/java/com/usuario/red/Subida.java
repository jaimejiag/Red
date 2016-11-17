package com.usuario.red;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.File;
import java.io.FileNotFoundException;

import cz.msebera.android.httpclient.Header;

public class Subida extends AppCompatActivity implements View.OnClickListener {
    public final static String WEB = "http://192.168.3.17/acceso/subida/upload.php";
    EditText edtTexto;
    Button btnSubir;
    TextView txvResultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subida);

        edtTexto = (EditText) findViewById(R.id.edt_fichero_subida);
        txvResultado = (TextView) findViewById(R.id.txv_resultado_subida);

        btnSubir = (Button) findViewById(R.id.btn_subir);
        btnSubir.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnSubir) {
            subida();
        }
    }

    private void subida() {
        String fichero = edtTexto.getText().toString();
        final ProgressDialog progreso = new ProgressDialog(this);
        File myFile;
        Boolean existe = true;
        myFile = new File(Environment.getExternalStorageDirectory(), fichero);
        //File myFile = new File("/path/to/file.png");
        RequestParams params = new RequestParams();
        try {
            params.put("fileToUpload", myFile);
        } catch (FileNotFoundException e) {
            existe = false;
            txvResultado.setText("Error en el fichero: " + e.getMessage());
            //Toast.makeText(this, "Error en el fichero: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        if (existe)
            RestClient.post(WEB, params, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                    progreso.dismiss();
                    txvResultado.setText("Fallo: " + statusCode + "\n" + responseString + "\n" + throwable.getMessage());
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    // called when response HTTP status is "200 OK"
                    progreso.dismiss();
                }

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

            });
    }
}
