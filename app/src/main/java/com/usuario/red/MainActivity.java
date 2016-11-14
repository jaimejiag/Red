package com.usuario.red;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button btn1, btn2, btn3, btn4, btn5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn1 = (Button) findViewById(R.id.btn_conexionHTTP);
        btn2 = (Button) findViewById(R.id.btn_conexionAsincrona);
        btn3 = (Button) findViewById(R.id.btn_conexionVolley);
        btn4 = (Button) findViewById(R.id.btn_descarga);
        btn5 = (Button) findViewById(R.id.btn_subida);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;

        if (v == btn1)
            intent = new Intent(this, ConexionHTTP.class);
        if (v == btn2)
            intent = new Intent(this, ConexionAsincrona.class);
        if (v == btn3)
            intent = new Intent(this, ConexionVolley.class);
        if (v == btn4)
            intent = new Intent(this, Descarga.class);
        if (v == btn5)
            intent = new Intent(this, Subida.class);

        if (intent != null)
            startActivity(intent);
    }
}
