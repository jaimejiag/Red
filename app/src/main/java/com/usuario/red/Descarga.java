package com.usuario.red;

import android.app.ProgressDialog;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

import java.io.File;

import cz.msebera.android.httpclient.Header;

public class Descarga extends AppCompatActivity implements View.OnClickListener {
    public static final String NOMBRE_FICHERO = "prueba.png";
    EditText texto;
    Button botonImagen, botonFichero;
    ImageView imagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descarga_imagen);
        //http://i.imgur.com/hlWzRAQ.jpg
        texto = (EditText) findViewById(R.id.edt_url_descarga);
        botonImagen = (Button) findViewById(R.id.btn_descargarImagen);
        botonImagen.setOnClickListener(this);
        botonFichero = (Button) findViewById(R.id.btn_descargar_fichero);
        botonFichero.setOnClickListener(this);
        imagen = (ImageView) findViewById(R.id.imageView);
    }

    @Override
    public void onClick(View v) {
        String url;

        if (v == botonImagen) {
            url = texto.getText().toString();
            Picasso.with(getApplicationContext())
                    .load(url)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error)
                    .resize(300, 400)
                    .rotate(45)
                    .into(imagen);
        }

        if (v == botonFichero) {
            descarga(texto.getText().toString());
        }
    }

    private void descarga(String url) {
        final ProgressDialog progreso = new ProgressDialog(this);
        File miFichero = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), NOMBRE_FICHERO);

        RestClient.get(url, new FileAsyncHttpResponseHandler(miFichero) {
            @Override
            public void onStart() {
                super.onStart();
                progreso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progreso.setMessage("Conectando . . .");
                progreso.setCancelable(false);
                progreso.show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                progreso.dismiss();
                Toast.makeText(getApplicationContext(), "Fallo: " + throwable.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, File file) {
                progreso.dismiss();
                Toast.makeText(getApplicationContext(), "Descarga OK\n " + file.getPath(), Toast.LENGTH_LONG).show();
            }
        });
    }
}




