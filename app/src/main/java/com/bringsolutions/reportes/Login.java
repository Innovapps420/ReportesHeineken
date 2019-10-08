package com.bringsolutions.reportes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

public class Login extends AppCompatActivity {
	
	Button btnInciarSesion;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		inicializarElementos();
		clicks();
	}
	
	private void inicializarElementos() {
		btnInciarSesion = findViewById(R.id.btnIniciarSesion);
	}
	
	private void clicks(){
		btnInciarSesion.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(Login.this, MainActivity.class));
				
			}
		});
	}


}
