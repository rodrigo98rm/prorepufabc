package mayer.rodrigo.prorepufabc.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import mayer.rodrigo.prorepufabc.BuildConfig;
import mayer.rodrigo.prorepufabc.MainActivity;
import mayer.rodrigo.prorepufabc.R;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private final int REQUEST_IMAGE_CAPTURE = 1, REQUEST_IMAGE_FROM_GALLERY = 2;

    //Views
    private TextView txtName, txtEmail;
    private CircularImageView imageViewprofilePic;
    private ProgressBar progressBar;

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private FirebaseStorage storage;

    private String uid;
    private Uri currentPhotoPath;

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
        imageViewprofilePic = findViewById(R.id.imageView_profilePic_Home);
        progressBar = findViewById(R.id.progressBar_Home);

        //Listeners
        imageViewprofilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(ProfileActivity.this, R.style.CustomAlertDialogTheme)
                        .setTitle("Foto de Perfil")
                        .setMessage("Tirar foto ou escolher da galeria?")
                        .setPositiveButton("Tirar foto", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                takePicture();
                            }
                        })
                        .setNegativeButton("Galeria de fotos", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                selectImageFromGallery();
                            }
                        })
                        .show();
            }
        });

        getSupportActionBar().setTitle("Meu perfil");
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
                           Picasso.with(getApplicationContext()).load(data.getString("imgUrl")).error(R.drawable.ufabc).into(imageViewprofilePic);
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

        startActivityForResult(chooserIntent, REQUEST_IMAGE_FROM_GALLERY);
    }

    private void takePicture(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                currentPhotoPath = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, currentPhotoPath);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {

            if (requestCode == REQUEST_IMAGE_FROM_GALLERY && data != null && data.getData() != null) {

                Uri imageUri = data.getData();

                uploadImage(getBitmapFromUri(imageUri));
            }

            if(requestCode == REQUEST_IMAGE_CAPTURE){
                uploadImage(getBitmapFromUri(currentPhotoPath));
            }

        }
    }

    private Bitmap getBitmapFromUri(Uri imageUri){
        Bitmap image = null;
        try {
            image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        return image;
    }

    private void uploadImage(Bitmap image){

        String fileName = uid;
        final StorageReference storageRef = storage.getReference().child("images").child(fileName);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        //Reduce image dimensions
        Bitmap newImage = Bitmap.createScaledBitmap(image, image.getWidth()/3, image.getHeight()/3, true);

        //Reduce image quality
        newImage.compress(Bitmap.CompressFormat.JPEG, 35, byteArrayOutputStream);

        //Upload to Storage
        UploadTask uploadTask = storageRef.putBytes(byteArrayOutputStream.toByteArray());

        //Use this to upload full size image
        //UploadTask uploadTask = storageRef.putFile(imageUri);

        image.recycle();
        newImage.recycle();

        try {
            byteArrayOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

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
                        Picasso.with(getApplicationContext()).load(uri).error(R.drawable.ufabc).into(imageViewprofilePic);

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
