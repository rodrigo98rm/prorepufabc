package mayer.rodrigo.prorepufabc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    //Views
    private TextView txtName, txtEmail;
    private Button buttonLogout;
    private CircularImageView imageViewprofilePic;
    private ProgressBar progressBar;

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private FirebaseStorage storage;

    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

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
        imageViewprofilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImageFromGallery();
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

        uid = user.getUid();

        //Get user data to fill up the views
        db.collection("users").document(uid)
               .get()
               .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                   @Override
                   public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                       if(task.isSuccessful()){
                           DocumentSnapshot data = task.getResult();
                           txtName.setText(data.getString("name"));
                           txtEmail.setText(data.getString("email"));
                           Picasso.with(getApplicationContext()).load(data.getString("imgUrl")).into(imageViewprofilePic);
                       }
                   }
               });

    }

    private void selectImageFromGallery(){
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Selecione uma imagem de perfil");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

        startActivityForResult(chooserIntent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {

            if (requestCode == 1 && data != null && data.getData() != null) {

                // currImageURI is the global variable I'm using to hold the content:// URI of the image
                Uri imageUri = data.getData();

                uploadImage(imageUri);

            }
        }
    }

    private void uploadImage(Uri imageUri){

        String fileName = uid + ".jpg";
        final StorageReference storageRef = storage.getReference().child("images").child(fileName);

        UploadTask uploadTask = storageRef.putFile(imageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        //Update imageview with new url
                        Picasso.with(getApplicationContext()).load(uri).into(imageViewprofilePic);

                        //Save new img url to db
                        Map<String, Object> user = new HashMap<>();
                        db.collection("users").document(uid)
                                .update("imgUrl", uri.toString())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getApplicationContext(), "Imagem de perfil atualizada",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });

                        Log.d("HERE IMG URL", uri.toString());
                    }
                });
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
