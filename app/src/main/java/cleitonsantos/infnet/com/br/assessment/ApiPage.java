package cleitonsantos.infnet.com.br.assessment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class ApiPage extends AppCompatActivity {

    public ContatosAdapter adapter;
    public RecyclerView recyclerView;
    private FirebaseAuth mAuth;
    private FirebaseUser user = null;
    String string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api_page);
        mAuth = FirebaseAuth.getInstance();
        RetrofitClientInstance client = new RetrofitClientInstance();
        Retrofit retrofit = client.getInstance();
        QuestionsService servise = retrofit.create(QuestionsService.class);
        //textview.setVisibility(View.GONE);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //adapter = new ContatosAdapter(items.toString());
       // recyclerView.setAdapter(adapter);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                TextView textview = findViewById(R.id.no_contact);
                textview.setVisibility(View.VISIBLE);
            }
        }, 5000);

    }


    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        user = currentUser;
        if (user == null) {
            navegarMain();
        }
    }

    public void signOut(View view) {
        mAuth.signOut();
        LoginManager.getInstance().logOut();
        AccessToken.getCurrentAccessToken();
        Profile.getCurrentProfile();
        user = null;
        navegarMain();
    }


    public void navegarMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

}