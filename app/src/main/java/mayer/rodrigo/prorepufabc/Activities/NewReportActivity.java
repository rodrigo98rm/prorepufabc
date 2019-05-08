package mayer.rodrigo.prorepufabc.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import mayer.rodrigo.prorepufabc.R;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewReportActivity extends AppCompatActivity {

    //Views
    private Button buttonAddPicture, buttonAddLocation, buttonSend;
    private TextInputLayout txtTitle, txtDescription;
    private TextView txtLocationLabel;

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    private double lat = 0, lng = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_report);

        //Views
        buttonAddPicture = findViewById(R.id.button_addPicture_NewReport);
        buttonAddLocation = findViewById(R.id.button_addLocation_NewReport);
        buttonSend = findViewById(R.id.button_send_NewReport);
        txtTitle = findViewById(R.id.txtLayout_title_NewReport);
        txtLocationLabel = findViewById(R.id.textView_locationMessage_NewReport);
        txtDescription = findViewById(R.id.txtLayout_description_NewReport);

        buttonAddLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(lat == 0){
                    getLocation();
                }else{
                    removeLocation();
                }
            }
        });

        //Lsiteners
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send();
            }
        });

        configure();
    }

    private void configure() {
        getSupportActionBar().setTitle("Novo relato");
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    private void send() {

        AlertDialog.Builder builder = new AlertDialog.Builder(NewReportActivity.this, R.style.CustomAlertDialogTheme);

        builder.setTitle("Atenção")
                .setMessage("Deseja publicar este relato?")
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        saveReportToDb();
                    }
                })
                .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        builder.create().show();
    }

    private void getLocation(){
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
                requestPermissions(permissions, 1);
                return;
            }
        }

        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        lat = location.getLatitude();
        lng = location.getLongitude();

        txtLocationLabel.setText("Latitude: " + lat + " Longitude: " + lng);
        buttonAddLocation.setText("Remover localização");

    }

    private void removeLocation(){
        lat = 0;
        lng = 0;

        txtLocationLabel.setText("Localização não adicionada");
        buttonAddLocation.setText("Adicionar localização");

    }

    private void saveReportToDb() {

        Map<String, Object> report = new HashMap<>();

        report.put("user_id", auth.getCurrentUser().getUid());
        report.put("title", txtTitle.getEditText().getText().toString().trim());
        report.put("description", txtDescription.getEditText().getText().toString().trim());
        report.put("upvotes", 0);
        report.put("timestamp", new Date().getTime());

        report.put("latitude", lat);
        report.put("longitude", lng);

        List<String> images = new ArrayList<>();
        images.add("https://i.imgur.com/Ls8jpOim.jpg");
        images.add("https://i.imgur.com/sVqliwHm.jpg");
        report.put("imgs", images);
        List<String> resolvedUsers = new ArrayList<>();
        report.put("resolvedUsers", resolvedUsers);

        db.collection("reports").document()
                .set(report)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Ocorreu um erro",
                                Toast.LENGTH_LONG).show();
                    }
                });

    }

}
