package mayer.rodrigo.prorepufabc.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import mayer.rodrigo.prorepufabc.Adapters.ReportPhotosAdapter;
import mayer.rodrigo.prorepufabc.BuildConfig;
import mayer.rodrigo.prorepufabc.R;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewReportActivity extends AppCompatActivity {

    private final int REQUEST_IMAGE_CAPTURE = 1, REQUEST_IMAGE_FROM_GALLERY = 2;
    private Uri currentPhotoPath;

    //Views
    private Button buttonAddPicture, buttonAddLocation, buttonSend;
    private TextInputLayout txtTitle, txtDescription;
    private TextView txtLocationLabel;
    private LinearLayout photosEmptyView;
    private GridView photosGrid;

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private FirebaseStorage storage;

    private ArrayList<Uri> imgUris = new ArrayList<>();
    private ArrayList<String> imgUrls = new ArrayList<>();

    private ReportPhotosAdapter photosAdapter;

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
        photosEmptyView = findViewById(R.id.photosEmptyView_NewReport);
        photosGrid = findViewById(R.id.gridView_photos_NewReport);

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

        buttonAddPicture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if(checkCameraPermissions()){
                    new AlertDialog.Builder(NewReportActivity.this, R.style.CustomAlertDialogTheme)
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

    private boolean checkCameraPermissions(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissions, 1);
                return false;
            }
        }

        return true;
    }

    private void configure() {
        getSupportActionBar().setTitle("Novo relato");
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        photosAdapter = new ReportPhotosAdapter(getApplicationContext(), imgUris);
        photosGrid.setAdapter(photosAdapter);
        photosGrid.setEmptyView(photosEmptyView);
    }

    private void send() {

        if(txtTitle.getEditText().getText().toString().isEmpty() || txtDescription.getEditText().getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(), "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(NewReportActivity.this, R.style.CustomAlertDialogTheme);

        builder.setTitle("Atenção")
                .setMessage("Deseja publicar este relato?")
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        prepareToSaveToDb();
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

        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

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

    private void prepareToSaveToDb() {

        Map<String, Object> report = new HashMap<>();

        report.put("user_id", auth.getCurrentUser().getUid());
        report.put("title", txtTitle.getEditText().getText().toString().trim());
        report.put("description", txtDescription.getEditText().getText().toString().trim());
        report.put("upvotes", 0);
        report.put("timestamp", new Date().getTime());

        report.put("latitude", lat);
        report.put("longitude", lng);

        List<String> resolvedUsers = new ArrayList<>();
        report.put("resolvedUsers", resolvedUsers);

        //Upload images
        for (Uri imgUri: imgUris) {
            uploadImage(getBitmapFromUri(imgUri), report);
        }

    }

    private void saveToDb(final Map<String, Object> report){

        //Salva no DB apenas quando todas as imgs ja subiram para o Cloud Storage
        if(imgUrls.size() != imgUris.size()){
            return;
        }

        report.put("imgs", imgUrls);

        db.collection("reports").document()
                .set(report)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        sendEmail(report);

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

    private void sendEmail(Map<String, Object> report){

        String imgsParameter = "";

        for(String imgUrl: imgUrls){
            imgsParameter += imgUrl + ",";
        }

        String baseUrl = "https://us-central1-prorepufabc.cloudfunctions.net/sendMail";

        AndroidNetworking.get(baseUrl)
                .addQueryParameter("dest", "mayerrodrigo98@gmail.com")
                .addQueryParameter("title", (String) report.get("title"))
                .addQueryParameter("description", (String) report.get("description"))
                .addQueryParameter("latitude", String.valueOf(report.get("latitude")))
                .addQueryParameter("longitude", String.valueOf(report.get("longitude")))
                .addQueryParameter("imgs", imgsParameter)
                .setPriority(Priority.HIGH)
                .build().getAsString(new StringRequestListener() {
            @Override
            public void onResponse(String response) {

            }

            @Override
            public void onError(ANError anError) {

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
                imgUris.add(imageUri);
            }

            if(requestCode == REQUEST_IMAGE_CAPTURE){
                imgUris.add(currentPhotoPath);
            }
            photosAdapter.notifyDataSetChanged();
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

    private void uploadImage(Bitmap image, final Map<String, Object> report){

        String fileName = auth.getCurrentUser().getUid() + "_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
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
                        imgUrls.add(uri.toString());
                        saveToDb(report);
                    }
                });
            }
        });

    }


}
