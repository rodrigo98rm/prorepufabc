package mayer.rodrigo.prorepufabc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import mayer.rodrigo.prorepufabc.Activities.ForgotPasswordActivity;
import mayer.rodrigo.prorepufabc.Activities.HomeActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    //Views
    private EditText txtEmail, txtPassword;
    private Button buttonLogin;
    private TextView txtForgotPassword, txtCreateAccount;
    private ProgressBar progressBar;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();

        //Views
        txtEmail = findViewById(R.id.editText_email_Main);
        txtPassword = findViewById(R.id.editText_password_Main);
        buttonLogin = findViewById(R.id.button_login_Main);
        txtForgotPassword = findViewById(R.id.textView_forgotPassword_Main);
        txtCreateAccount = findViewById(R.id.textView_createAccount_Main);
        progressBar = findViewById(R.id.progressBar_Main);

        //Listeners
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        txtCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CreateAccountActivity.class);
                startActivity(intent);
            }
        });

    }

    private void signIn(){

        updateProgressViews(true);

        String email = txtEmail.getText().toString().trim();
        String password = txtPassword.getText().toString().trim();

        if(email.isEmpty()){
            Toast.makeText(getApplicationContext(), "Preencha o email",
                    Toast.LENGTH_SHORT).show();
            updateProgressViews(false);
            return;
        }

        if(password.isEmpty()){
            Toast.makeText(getApplicationContext(), "Preencha a senha",
                    Toast.LENGTH_SHORT).show();
            updateProgressViews(false);
            return;
        }

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "Dados incorretos",
                            Toast.LENGTH_SHORT).show();
                    updateProgressViews(false);
                }
            }
        });
    }

    private void updateProgressViews(boolean loading){
        if(loading){
            buttonLogin.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }else{
            buttonLogin.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = auth.getCurrentUser();

        if(user != null){
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
            finish();
        }else{
            Log.i("USER INFO", "User is not logged in");
        }

    }
}
