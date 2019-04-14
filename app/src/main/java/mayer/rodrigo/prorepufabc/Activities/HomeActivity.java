package mayer.rodrigo.prorepufabc.Activities;

import androidx.appcompat.app.AppCompatActivity;
import mayer.rodrigo.prorepufabc.ProfileActivity;
import mayer.rodrigo.prorepufabc.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeActivity extends AppCompatActivity {

    //Views
    private ImageButton imageButtonMenu;
    private FloatingActionButton fabNewReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Views
        imageButtonMenu = findViewById(R.id.imageButton_menu_Home);
        fabNewReport = findViewById(R.id.fab_newReport_Home);

        //Listeners
        imageButtonMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(getApplicationContext(), imageButtonMenu);
                popupMenu.getMenuInflater().inflate(R.menu.home_popup_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        switch(menuItem.getItemId()){
                            case R.id.myreports:
                                Intent intentToMyReports = new Intent(getApplicationContext(), MyReportsActivity.class);
                                startActivity(intentToMyReports);
                                break;
                            case R.id.settings:
                                Intent intentToSettings = new Intent(getApplicationContext(), SettingsActivity.class);
                                startActivity(intentToSettings);
                                break;
                        }

                        return true;
                    }
                });
                popupMenu.show();
            }
        });

        fabNewReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), NewReportActivity.class);
                Intent intent = new Intent(getApplicationContext(), ReportDetailsActivity.class);
                startActivity(intent);
            }
        });

    }

}
