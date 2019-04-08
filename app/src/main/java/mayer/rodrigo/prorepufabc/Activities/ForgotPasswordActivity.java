package mayer.rodrigo.prorepufabc.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import mayer.rodrigo.prorepufabc.R;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText txtEmail;
    private Button buttonSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        //Views
        txtEmail = findViewById(R.id.editText_email_ForgotPassword);
        buttonSend = findViewById(R.id.button_send_ForgotPassword);

        //Listeners
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });

        configureActionBar();
    }

    private void configureActionBar(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.recuperar_senha));
    }

    private void resetPassword(){

        String emailAddress = txtEmail.getText().toString().trim();

        if(!isValidEmail(emailAddress)){
            Snackbar.make(txtEmail, "Email inválido", Snackbar.LENGTH_SHORT).show();
            return;
        }

        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Snackbar.make(txtEmail, "Email enviado", Snackbar.LENGTH_LONG).show();
                        }else{
                            Snackbar.make(txtEmail, "Email não cadastrado", Snackbar.LENGTH_LONG).show();
                        }
                    }

                });
    }

    private boolean isValidEmail(String email){
        if(email.contains("@") && email.contains(".com")){
            return true;
        }
        return false;
    }

}
