package mayer.rodrigo.prorepufabc.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import mayer.rodrigo.prorepufabc.BuildConfig;
import mayer.rodrigo.prorepufabc.MainActivity;
import mayer.rodrigo.prorepufabc.ProfileActivity;
import mayer.rodrigo.prorepufabc.R;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class SettingsActivity extends AppCompatActivity {

    //Views
    private LinearLayout profile, about, quit;
    private TextView txtVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //Views
        profile = findViewById(R.id.linearLayout_myProfile_Settings);
        quit = findViewById(R.id.linearLayout_quit_Settings);
        txtVersion = findViewById(R.id.textView_version_Settings);

        //Listeners
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });

        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        configure();

    }

    private void configure(){
        getSupportActionBar().setTitle("Configurações");
        txtVersion.setText(BuildConfig.VERSION_NAME);
    }

    private void logout(){

        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this, R.style.CustomAlertDialogTheme);
        builder.setMessage("Fazer logout?")
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
