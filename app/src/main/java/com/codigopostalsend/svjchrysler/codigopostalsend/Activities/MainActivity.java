package com.codigopostalsend.svjchrysler.codigopostalsend.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.codigopostalsend.svjchrysler.codigopostalsend.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnLoginEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        configInit();
    }

    private void configInit() {
        configComponents();
        configEvents();
        configText();
    }

    private void configText() {
        this.setTitle(R.string.titleIniciarSesion);
    }

    private void configComponents() {
        btnLoginEmail = (Button) findViewById(R.id.btnLoginEmail);
    }

    private void configEvents() {
        btnLoginEmail.setOnClickListener(this);
    }

    private void redirectLoginEmail() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLoginEmail:
                redirectLoginEmail();
                break;
        }
    }
}