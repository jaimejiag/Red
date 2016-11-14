package com.usuario.red;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class Descarga extends AppCompatActivity implements View.OnClickListener{
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

        }
    }
}
