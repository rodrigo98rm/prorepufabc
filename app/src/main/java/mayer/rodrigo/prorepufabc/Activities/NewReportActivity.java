package mayer.rodrigo.prorepufabc.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import mayer.rodrigo.prorepufabc.MainActivity;
import mayer.rodrigo.prorepufabc.R;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
    private Button buttonSend;
    private TextInputLayout txtTitle, txtDescription;

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_report);

        //Views
        buttonSend = findViewById(R.id.button_send_NewReport);
        txtTitle = findViewById(R.id.txtLayout_title_NewReport);
        txtDescription = findViewById(R.id.txtLayout_description_NewReport);


        //Lsiteners
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send();
            }
        });

        configure();
    }

    private void configure(){
        getSupportActionBar().setTitle("Novo relato");
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    private void send(){

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

    private void saveReportToDb(){
        Map<String, Object> report = new HashMap<>();

        report.put("user_id", auth.getCurrentUser().getUid());
        report.put("title", txtTitle.getEditText().getText().toString().trim());
        report.put("description", txtDescription.getEditText().getText().toString().trim());
        report.put("upvotes", 0);
        report.put("timestamp", new Date().getTime());
        List<String> images = new ArrayList<>();
        images.add("https://i.imgur.com/Ls8jpOim.jpg");
        images.add("https://i.imgur.com/sVqliwHm.jpg");
        report.put("imgs", images);

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
