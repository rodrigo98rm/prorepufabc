package mayer.rodrigo.prorepufabc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

public class HomeActivity extends AppCompatActivity {

    //Views
    private TextView txtName, txtEmail;
    private Button buttonLogout;
    private CircularImageView imageViewprofilePic;
    private ProgressBar progressBar;

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        //Views
        txtName = findViewById(R.id.textView_name_Home);
        txtEmail = findViewById(R.id.textView_email_Home);
        buttonLogout = findViewById(R.id.button_logout_Home);
        imageViewprofilePic = findViewById(R.id.imageView_profilePic_Home);
        progressBar = findViewById(R.id.progressBar_Home);

        //Listeners
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = auth.getCurrentUser();
        if(user == null){
            logout();
            return;
        }

       db.collection("users")
               .whereEqualTo("uid", user.getUid())
               .get()
               .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                   @Override
                   public void onComplete(@NonNull Task<QuerySnapshot> task) {
                       progressBar.setVisibility(View.GONE);
                       if(task.isSuccessful()){
                           DocumentSnapshot data = task.getResult().getDocuments().get(0);
                           txtName.setText(data.getString("name"));
                           txtEmail.setText(data.getString("email"));
                           Picasso.with(getApplicationContext()).load("https://scontent.fcgh17-1.fna.fbcdn.net/v/t1.0-9/42107418_1970018699722564_356815154123374592_n.jpg?_nc_cat=106&_nc_oc=AQnlYVzN0Fbplge0ZVXPoR2deDfUptRdE1M6K8ymguOtOLbLL96y6dMmHSZ0QES-07o&_nc_ht=scontent.fcgh17-1.fna&oh=adf171071da4eebeea3e3d0c7e02109c&oe=5CDCE85F").into(imageViewprofilePic);
                       }
                   }
               });

    }

    private void logout(){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }
}
