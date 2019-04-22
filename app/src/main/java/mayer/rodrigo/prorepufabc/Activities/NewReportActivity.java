package mayer.rodrigo.prorepufabc.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import mayer.rodrigo.prorepufabc.MainActivity;
import mayer.rodrigo.prorepufabc.R;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class NewReportActivity extends AppCompatActivity {

    //Views
    private Button buttonSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_report);

        //Views
        buttonSend = findViewById(R.id.button_send_NewReport);

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
    }

    private void send(){

        AlertDialog.Builder builder = new AlertDialog.Builder(NewReportActivity.this, R.style.CustomAlertDialogTheme);

        builder.setTitle("Atenção")
                .setMessage("Deseja publicar este relato?")
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getApplicationContext(), MyReportsActivity.class);
                        finish();
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        builder.create().show();
    }

}
