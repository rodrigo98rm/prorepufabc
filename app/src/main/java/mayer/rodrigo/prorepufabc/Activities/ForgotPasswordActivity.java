package mayer.rodrigo.prorepufabc.Activities;

import androidx.appcompat.app.AppCompatActivity;
import mayer.rodrigo.prorepufabc.R;

import android.os.Bundle;

public class ForgotPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        configureActionBar();
    }

    private void configureActionBar(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.recuperar_senha));
    }

}
