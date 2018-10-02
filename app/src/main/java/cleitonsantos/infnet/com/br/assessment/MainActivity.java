package cleitonsantos.infnet.com.br.assessment;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;


public class MainActivity extends AppCompatActivity {
    TextInputLayout passwordTextInput;
    TextInputEditText passwordEditText;
    TextInputLayout emailTextInput;
    TextInputEditText emailEditText;
    TextView statusText;
    private FirebaseAuth mAuth;
    private FirebaseUser user = null;
    CallbackManager callFace;
    LoginButton loginButton;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        emailTextInput = findViewById(R.id.email_text_input);
        emailEditText = findViewById(R.id.email_edit_text);
        passwordTextInput = findViewById(R.id.password_text_input);
        passwordEditText = findViewById(R.id.password_edit_text);
        statusText = findViewById(R.id.status_text);
        mAuth = FirebaseAuth.getInstance();
        intent = getIntent();
        statusText.setText(intent.getStringExtra("status_text"));

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        callFace = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.facebook_button);


        // Verificar validação de email em tempo de digitação.
        emailEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (isEmailValid(emailEditText.getText())) {
                    emailTextInput.setError(null); //Clear the error
                }
                return false;
            }
        });
        // Verificar validação de senha em tempo de digitação.
        passwordEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (isPasswordValid(passwordEditText.getText())) {
                    passwordTextInput.setError(null); //Clear the error
                }
                return false;
            }
        });

        face();

    }


    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        intent = getIntent();
        statusText.setText(intent.getStringExtra("status_text"));
        user = currentUser;
        if (user != null){
            navegarApiPage();
        }
    }

    private void signIn(String email, String password) {

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            user = mAuth.getCurrentUser();
                            navegarApiPage();
                        } else {
                            statusText.setText("Usuário Invalido! Login não realizado!");
                            user = null;
                        }
                    }
                });
        // [END sign_in_with_email]
    }

    private void face(){
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(callFace,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        statusText.setText(" Algo deu errado. Cancelado!");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        statusText.setText(" Algo deu errado com FaceBook!");
                    }
                });
    }

    private void handleFacebookAccessToken(AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            user = mAuth.getCurrentUser();
                            navegarApiPage();
                        } else {
                            // If sign in fails, display a message to the user.
                            statusText.setText(" Login pelo Face não realizado!");
                            user = null;
                        }

                    }
                });
    }



    // ...

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        callFace.onActivityResult(requestCode, resultCode, data);
    }

    public void startOn(View view) {
        if (!isEmailValid(emailEditText.getText())) {
            emailTextInput.setError("Digite um email válido!");
        }else {
            if (!isPasswordValid(passwordEditText.getText())) {
                passwordTextInput.setError("Digite uma Senha válida!");
            }else {
                signIn(emailEditText.getText().toString(), passwordEditText.getText().toString());
            }
        }
    }

    public void navegarCadastro(View view){
        Intent intent = new Intent(this, Cadastrar.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void navegarApiPage(){
        Intent intent = new Intent(this, ApiPage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private boolean isPasswordValid(@Nullable Editable text) {
        return text != null && text.length() >= 8;
    }
    private boolean isEmailValid(@Nullable Editable text) {
        return text != null &&  Patterns.EMAIL_ADDRESS.matcher(text).matches();
    }

}
