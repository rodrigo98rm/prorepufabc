package mayer.rodrigo.prorepufabc.Activities;

import androidx.appcompat.app.AppCompatActivity;
import mayer.rodrigo.prorepufabc.ProfileActivity;
import mayer.rodrigo.prorepufabc.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class SettingsActivity extends AppCompatActivity {

    //Views
    private LinearLayout profile, about, quit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //Views
        profile = findViewById(R.id.linearLayout_myProfile_Settings);

        //Listeners
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });

        getSupportActionBar().setTitle("Configurações");

    }

}
