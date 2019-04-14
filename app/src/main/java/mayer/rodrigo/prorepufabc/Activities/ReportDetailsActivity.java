package mayer.rodrigo.prorepufabc.Activities;

import androidx.appcompat.app.AppCompatActivity;
import mayer.rodrigo.prorepufabc.Model.Report;
import mayer.rodrigo.prorepufabc.Model.User;
import mayer.rodrigo.prorepufabc.R;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ReportDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    //Views
    private TextView txtTitle, txtUserName, txtDate, txtUpvotes, txtDescription;
    private CircularImageView imgUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_details);

        //Views
        txtTitle = findViewById(R.id.textView_title_Details);
        txtUserName = findViewById(R.id.textView_user_Details);
        txtDate = findViewById(R.id.textView_date_Details);
        txtUpvotes = findViewById(R.id.textView_upvotes_Details);
        txtDescription = findViewById(R.id.textView_description_Details);
        imgUser = findViewById(R.id.circularImageView_userImg_Details);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fillUpViews();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setZoomGesturesEnabled(false);
        googleMap.getUiSettings().setScrollGesturesEnabled(false);


        LatLng sydney = new LatLng(-23.645296, -46.527828);
        googleMap.addMarker(new MarkerOptions().position(sydney)
                .title("Marker in Sydney"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 18));
    }

    private void fillUpViews(){
        //TODO: Get Report from Cloud Firestore
        User user = new User("Rodrigo Rominho Mayer", "https://firebasestorage.googleapis.com/v0/b/prorepufabc.appspot.com/o/images%2FIz5K1w1F8AQPrwZpJBPCWwMOKQg1.jpg?alt=media&token=4f27659d-acb0-42ca-8943-29df12551307");
        Report report = new Report(user, "Bebedouro quebrado", "Lorem ipsum", 1345, 1555268610000L);

        SimpleDateFormat formatter = new SimpleDateFormat("dd/mm/yyyy");

        txtTitle.setText(report.getTitle());
        txtUserName.setText(report.getUser().getName());
        txtDate.setText("• " + formatter.format(new Date(report.getTimestamp())));
        txtUpvotes.setText(String.valueOf(report.getUpvotes()));
        txtDescription.setText(report.getDescription());
        Picasso.with(getApplicationContext()).load(report.getUser().getImgUrl()).into(imgUser);

    }

}
