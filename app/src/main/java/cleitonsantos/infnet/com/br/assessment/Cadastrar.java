package cleitonsantos.infnet.com.br.assessment;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


public class Cadastrar extends AppCompatActivity {

    Boolean valid;

    TextInputEditText nomeField;
    TextInputEditText emailField;
    TextInputEditText senhaField;
    TextInputEditText confirmarField;
    TextInputEditText cpfField;
    TextInputLayout nomeLayout;
    TextInputLayout emailLayout;
    TextInputLayout senhaLayout;
    TextInputLayout confirmarLayout;
    TextInputLayout cpfLayout;
    String nomeBody;
    String emailBody;
    String senhaBody;
    String cpfBody;
    List<TextInputEditText> textEditList;
    private FirebaseAuth mAuth;
    private FirebaseUser user = null;
    TextView statusText;
    String stringText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar);
        mAuth = FirebaseAuth.getInstance();
        //pega referências para as views na interface
        nomeField = findViewById(R.id.nome_edit_text);
        emailField = findViewById(R.id.email_edit_text);
        senhaField = findViewById(R.id.senha_edit_text);
        confirmarField = findViewById(R.id.confirmar_edit_text);
        cpfField = findViewById(R.id.cpf_edit_text);
        nomeLayout = findViewById(R.id.nome_text_input);
        emailLayout = findViewById(R.id.email_text_input);
        senhaLayout = findViewById(R.id.senha_text_input);
        confirmarLayout = findViewById(R.id.confirmar_text_input);
        cpfLayout = findViewById(R.id.cpf_text_input);
        statusText = findViewById(R.id.status_text);
    }


    public void saveContato(View view){
        validCheck();
        if(valid == true) {
            nomeBody = nomeField.getText().toString();
            emailBody = emailField.getText().toString();
            senhaBody = senhaField.getText().toString();
            cpfBody = cpfField.getText().toString();
            createAccount(emailBody,senhaBody);
            clearForm(view);
        }
    }

    public void voltar(View view){
        navegarMain();
    }

    public void clearForm(View view) {
        textEditList = new ArrayList<>();
        textEditList.add(0,nomeField);
        textEditList.add(1,emailField);
        textEditList.add(2,senhaField);
        textEditList.add(3,confirmarField);
        textEditList.add(3,cpfField);
        for(int i = 0; i<5;i++){
            TextInputEditText v = textEditList.get(i);
            v.setText("");
        }
        nomeLayout.setError(null);
        emailLayout.setError(null);
        senhaLayout.setError(null);
        confirmarLayout.setError(null);
        cpfLayout.setError(null);
        nomeField.requestFocus();
    }
    //verificando se há campos inválidos
    private void validCheck() {
        valid = true;
        TextInputEditText focus = nomeField;
        nomeLayout.setError(null);
        emailLayout.setError(null);
        senhaLayout.setError(null);
        confirmarLayout.setError(null);
        cpfLayout.setError(null);
        if (!isCpfValid(cpfField.getText())) {
            focus = cpfField;
            cpfLayout.setError("Preencha um cpf valido! xxx.xxx.xxx-xx");
            cpfLayout.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorError)));
            valid = false;
        }
        if (!isConfirmarValid(senhaField.getText(), confirmarField.getText())) {
            focus = confirmarField;
            confirmarLayout.setError("As senhas não são iguais!");
            confirmarLayout.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorError)));
            valid = false;
        }
        if (!isSenhaValid(senhaField.getText())) {
            focus = senhaField;
            senhaLayout.setError("Preencha a senha com mínimo de 8 caracteres!");
            senhaLayout.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorError)));
            valid = false;
        }
        if (!isEmailValid(emailField.getText())) {
            focus = emailField;
            emailLayout.setError("Preencha Um email Válido! xxx@xxx.xxx");
            emailLayout.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorError)));
            valid = false;
        }
        if (!isNomeValid(nomeField.getText())) {
            focus = nomeField;
            nomeLayout.setError("Preencha Um Nome Válido!Sem caracteres especiais.");
            nomeLayout.setErrorTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorError)));
            valid = false;
        }

        focus.requestFocus();

    }

    // [END on_start_check_user]

   private void createAccount(String email, String password) {
        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            createUserDatabase();
                            mAuth.signOut();
                            navegarMain();

                            // Sign in success, update UI with the signed-in user's information
                        } else {
                            // If sign in fails, display a message to the user.
                            stringText = "Erro! Usuário já Cadastrado!";
                            statusText.setText(stringText);
                            user = null;
                        }
                    }
                });
        // [END create_user_with_email]
    }

    private void createUserDatabase(){
            // pega os valores necessários para construir uma QuestionCard a partir das views
            final User user = new User(nomeBody,emailBody,senhaBody,cpfBody);
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ActiveContatos");
            databaseReference = databaseReference.push();
            databaseReference.setValue(user);
            createAccount(emailBody,senhaBody);
            stringText = "Usuário Cadastrado com Sucesso!";
            statusText.setText(stringText);
    }



    private boolean isValid(@Nullable Editable text) {
        return text != null && text.length() > 0 ;
    }

    private boolean isEmailValid(@Nullable Editable text) {
        return isValid(text)&& Patterns.EMAIL_ADDRESS.matcher(text).matches();

    }
    private boolean isNomeValid(@Nullable Editable text) {
        return isValid(text)
                && Pattern.compile("[a-zA-Z][a-zA-Z\\ \\.\\-]{0,64}").matcher(text).matches();
    }
    private boolean isSenhaValid(@Nullable Editable text) {
        return isValid(text)&& text.length() >= 8;
    }


    private boolean isConfirmarValid(@Nullable Editable text, @Nullable Editable conf) {
        return isValid(text)&& isValid(conf) && conf.toString().equals(text.toString());
    }

    private boolean isCpfValid(@Nullable Editable text) {
        return isValid(text)&& Pattern.compile(
                "[0-9]{3,3}" +
                        "\\." +
                        "[0-9]{3,3}" +
                        "\\." +
                        "[0-9]{3,3}" +
                        "\\-" +
                        "[0-9]{2,2}").matcher(text).matches();
    }

    public void navegarMain(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("status_text", stringText);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

}
