package mayer.rodrigo.prorepufabc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import mayer.rodrigo.prorepufabc.Activities.HomeActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateAccountActivity extends AppCompatActivity {

    //Views
    private EditText txtName, txtEmail, txtPassword, txtConfirmPassword;
    private Button buttonSignup;
    private ProgressBar progressBar;

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        txtName = findViewById(R.id.editText_name_CreateAccount);
        txtEmail = findViewById(R.id.editText_email_CreateAccount);
        txtPassword = findViewById(R.id.editText_password_CreateAccount);
        txtConfirmPassword = findViewById(R.id.editText_confirmPassword_CreateAccount);
        buttonSignup = findViewById(R.id.button_signup_CreateAccount);
        progressBar = findViewById(R.id.progressBar_CreateAccount);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        //Listeners
        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup();
            }
        });

        setupActionBar();
    }

    private void signup(){

        updateProgressViews(true);

        final String email = txtEmail.getText().toString().trim();
        String password = txtPassword.getText().toString().trim();
        String passwordConfirmation = txtConfirmPassword.getText().toString().trim();

        if(email.isEmpty()){
            Snackbar.make(txtPassword, "Preencha o email", Snackbar.LENGTH_LONG).show();
            updateProgressViews(false);
            return;
        }

        if(password.isEmpty()){
            Snackbar.make(txtPassword, "Preencha a senha", Snackbar.LENGTH_LONG).show();
            updateProgressViews(false);
            return;
        }

        if(!password.equals(passwordConfirmation)){
            Snackbar.make(txtPassword, "As senhas não correspondem", Snackbar.LENGTH_LONG).show();
            updateProgressViews(false);
            return;
        }



        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if(user != null){
                        insertUserOnDb(user, email);
                    }
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

    private void insertUserOnDb(FirebaseUser fbUser, String email){
        Map<String, Object> user = new HashMap<>();

        user.put("name", txtName.getText().toString().trim());
        user.put("email", email);

        db.collection("users").document(fbUser.getUid())
                .set(user)
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

    private void updateProgressViews(boolean loading){
        if(loading){
            buttonSignup.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }else{
            buttonSignup.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void setupActionBar(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.cadastro));
    }
}
