package mayer.rodrigo.prorepufabc.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import mayer.rodrigo.prorepufabc.Adapters.ReportPhotosAdapter;
import mayer.rodrigo.prorepufabc.Model.Report;
import mayer.rodrigo.prorepufabc.Model.User;
import mayer.rodrigo.prorepufabc.R;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ReportDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final String EXTRA_REPORT_ID = "report_id";

    //Views
    private TextView txtTitle, txtUserName, txtDate, txtUpvotes, txtDescription, txtResolvido;
    private LinearLayout layoutResolvido;
    private CircularImageView imgUser;
    private GridView photosGrid;

    private GoogleMap googleMap;
    private double lat, lng;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_details);

        db = FirebaseFirestore.getInstance();

        //Views
        txtTitle = findViewById(R.id.textView_title_Details);
        txtUserName = findViewById(R.id.textView_user_Details);
        txtDate = findViewById(R.id.textView_date_Details);
        txtUpvotes = findViewById(R.id.textView_upvotes_Details);
        txtResolvido = findViewById(R.id.textView_resolvido_ReportDetails);
        layoutResolvido = findViewById(R.id.linearLayout_resolvido_ReportDetails);
        txtDescription = findViewById(R.id.textView_description_Details);
        imgUser = findViewById(R.id.circularImageView_userImg_Details);
        photosGrid = findViewById(R.id.gridView_photos_Details);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fillUpViews();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        this.googleMap = googleMap;

        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setZoomGesturesEnabled(false);
        googleMap.getUiSettings().setScrollGesturesEnabled(false);

//        moveMap();
    }

    private void moveMap(){

        if(googleMap != null && lat != 0 && lng != 0){

            LatLng location = new LatLng(lat, lng);
            googleMap.addMarker(new MarkerOptions().position(location));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 18));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void fillUpViews(){

        getSupportActionBar().setTitle("Detalhes");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent incoming = getIntent();
        String reportId = incoming.getStringExtra(EXTRA_REPORT_ID);

        db.collection("reports").document(reportId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){

                            final DocumentSnapshot document = task.getResult();

                            //User
                            String userId = document.getString("user_id");

                            db.collection("users").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if(task.isSuccessful()){
                                        User user = task.getResult().toObject(User.class);

                                        Report report = document.toObject(Report.class);
                                        report.setId(document.getId());
                                        report.setUser(user);

                                        lat = report.getLatitude();
                                        lng = report.getLongitude();

                                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/YYYY");

                                        txtTitle.setText(report.getTitle());
                                        txtUserName.setText(report.getUser().getName());
                                        txtDate.setText("• " + formatter.format(new Date(report.getTimestamp())));
                                        txtUpvotes.setText(String.valueOf(report.getUpvotes()));
                                        txtDescription.setText(report.getDescription());
                                        Picasso.with(getApplicationContext()).load(report.getUser().getImgUrl()).into(imgUser);

                                        if(report.getResolvedUsers() != null){
                                            if(report.getResolvedUsers().size() > 0){
                                                layoutResolvido.setVisibility(View.VISIBLE);
                                                txtResolvido.setText(report.getResolvedUsers().size() + " pessoas marcaram este problema como resolvido");
                                            }
                                        }

                                        //Photos Grid View
                                        ReportPhotosAdapter photosAdapter = new ReportPhotosAdapter(report.getImgs(), getApplicationContext());
                                        photosGrid.setAdapter(photosAdapter);

                                        moveMap();
                                    }
                                }
                            });
                        }
                    }
                });

    }

}
