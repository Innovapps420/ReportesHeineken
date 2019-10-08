package com.bringsolutions.reportes.ui.reportes;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bringsolutions.reportes.R;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

public class ReportesFragment extends Fragment {
	View view;
	TextView tvMuestraDireccion;
	CardView btnObtenerDireccion, btnFotoInicial;
	ImageView vistaFotoInicial;
	TextView tvTextoFotoInicial;
	
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_reportes, container, false);
		inicializarElementos();
		clicks();
		
		
		return view;
	}
	
	private void clicks() {
		btnObtenerDireccion.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				obtenerDireccion();
				
			}
		});
		
		btnFotoInicial.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				obtenerFotoIncial();
			}
		});
		
		
		
	}
	
	private void obtenerFotoIncial() {
		verificarPermisosCamaraMemoria();
		
	}
	
	private void inicializarElementos() {
		tvMuestraDireccion = view.findViewById(R.id.tvUbicacion);
		btnObtenerDireccion = view.findViewById(R.id.imgButtonObtenerUbicacion);
		btnFotoInicial = view.findViewById(R.id.imgButtonFotoInicial);
		vistaFotoInicial = view.findViewById(R.id.imgFotoInicialVista);
		tvTextoFotoInicial  = view.findViewById(R.id.tvFotoInicial);
	}
	
	private void verificarPermisosCamaraMemoria() {
		if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,}, 2);
			ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 3);
		} else {
			abrirCamara();
			
		}
		
	}
	
	private void abrirCamara() {
		Intent intent =new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(intent,6);
		
		/*
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		
		Uri imageUri;
		if (Build.VERSION.SDK_INT >=  Build.VERSION_CODES.N) {
			imageUri = Uri.parse(fileImagen);
		} else{
			imageUri = Uri.fromFile(new File(fileImagen));
		}
		
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri /*Uri.fromFile(fileImagen));
		//startActivityForResult(intent, 5);
		*/
		
		
	}
	
	private void obtenerDireccion() {
		verificarPermisosUbicacionTelefono();
	}
	
	private void verificarPermisosUbicacionTelefono() {
		if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1);
		} else {
			extraerUbicacion();
		}
		
	}
	
	private void extraerUbicacion() {
		LocationManager mlocManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
		final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		if (!gpsEnabled) {
			Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			startActivity(settingsIntent);
		}
		if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
			return;
		}
		
		Location location = mlocManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		
		try {
			Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
			List<Address> list = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
			if (!list.isEmpty()) {
				Address DirCalle = list.get(0);
				tvMuestraDireccion.setText(DirCalle.getAddressLine(0));
			}
		} catch (IOException e) {
			e.printStackTrace();
			Log.e("Error ubicación", e.getLocalizedMessage());
		}
		
		
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		Bitmap bitmap = (Bitmap) data.getExtras().get("data");
		tvTextoFotoInicial.setVisibility(View.GONE);
		vistaFotoInicial.setVisibility(View.VISIBLE);
		vistaFotoInicial.setMinimumHeight(800);
		vistaFotoInicial.setImageBitmap(bitmap);
		
		
	}
	
	
}
	
