package mayer.rodrigo.prorepufabc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class CreateAccountActivity extends AppCompatActivity {

    //Views
    private EditText txtEmail, txtPassword;
    private Button buttonSignup;
    private ProgressBar progressBar;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        txtEmail = findViewById(R.id.editText_email_CreateAccount);
        txtPassword = findViewById(R.id.editText_password_CreateAccount);
        buttonSignup = findViewById(R.id.button_signup_CreateAccount);
        progressBar = findViewById(R.id.progressBar_CreateAccount);

        auth = FirebaseAuth.getInstance();

        //Listeners
        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup();
            }
        });


    }

    private void signup(){

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

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }else{

                    try {
                        throw task.getException();
                    } catch(FirebaseAuthWeakPasswordException e){
                        Toast.makeText(getApplicationContext(), "Senha muito fraca",
                                Toast.LENGTH_LONG).show();
                    } catch(FirebaseAuthInvalidCredentialsException e){
                        Toast.makeText(getApplicationContext(), "Formato de email inválido",
                                Toast.LENGTH_LONG).show();
                    } catch(FirebaseAuthUserCollisionException e){
                        Toast.makeText(getApplicationContext(), "Esse endereço de email já foi usado",
                                Toast.LENGTH_LONG).show();
                    } catch (Exception e){
                        Toast.makeText(getApplicationContext(), "Ocorreu um erro",
                                Toast.LENGTH_LONG).show();
                    }

                    updateProgressViews(false);
                }
            }
        });
    }

    private void updateProgressViews(boolean loading){
        if(loading){
            buttonSignup.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }else{
            buttonSignup.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}
