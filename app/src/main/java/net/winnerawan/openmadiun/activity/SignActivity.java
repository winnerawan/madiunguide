package net.winnerawan.openmadiun.activity;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import net.winnerawan.openmadiun.R;
import net.winnerawan.openmadiun.helper.AppInterface;
import net.winnerawan.openmadiun.helper.AppRequest;
import net.winnerawan.openmadiun.helper.SQLiteHandler;
import net.winnerawan.openmadiun.response.Response;
import net.winnerawan.openmadiun.helper.SessionManager;
import net.winnerawan.openmadiun.response.UserResponse;

import retrofit2.Call;
import retrofit2.Callback;

public class SignActivity extends AppCompatActivity {

    private static final String TAG = SignActivity.class.getSimpleName();

    private TextView signUp;
    private EditText txtEmail, txtPassword;
    private ImageView btnSignIn;
    private CoordinatorLayout signCoordinator;
    private AppInterface api;
    private SessionManager session;
    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        AppRequest request = new AppRequest();
        api = request.Guide().create(AppInterface.class);

        session = new SessionManager(getApplicationContext());
        db = new SQLiteHandler(getApplicationContext());

        initLayout();


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.image_click));
                goToSignUpActivity();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.image_click));
                String email = txtEmail.getText().toString().trim();
                String password = txtPassword.getText().toString().trim();
                if (!email.isEmpty() && !password.isEmpty()) {
                    authUser(email, password);
                    Log.e(TAG, "LOG");
                } else {
                    Snackbar snackbar = Snackbar
                            .make(signCoordinator, "CHECK EMAIL OR PASSWORD!", Snackbar.LENGTH_LONG);

                    snackbar.show();
                }
            }
        });

    }

    private void initLayout() {
        this.signUp = (TextView) findViewById(R.id.signUp);
        this.txtEmail = (EditText) findViewById(R.id.txtEmail);
        this.txtPassword = (EditText) findViewById(R.id.txtPassword);
        this.btnSignIn = (ImageView) findViewById(R.id.next);
        this.signCoordinator = (CoordinatorLayout) findViewById(R.id.signCoordinator);
    }

    private void authUser(String email, String password) throws NullPointerException {
        Call<UserResponse> sign = api.signIn(email, password);
        sign.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, retrofit2.Response<UserResponse> response) {
                int status = response.body().getStatus();
                Log.e(TAG, "LOG"+response.body());
                if (response!=null && status==200) {
                    db = new SQLiteHandler(getApplicationContext());
                    session = new SessionManager(getApplicationContext());
                    String username = response.body().getUser().getUsername();
                    String email = response.body().getUser().getEmail();
                    String telephone = response.body().getUser().getTelephone();
                    String photo = response.body().getUser().getPhoto();
                    db.addUser(username, email, telephone, photo);
                    session.setLogin(true);
                    Intent i = new Intent(SignActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                } else if (response.body()==null){
                    Snackbar snackbar = Snackbar
                            .make(signCoordinator, "WRONG EMAIL OR PASSWORD", Snackbar.LENGTH_LONG);

                    snackbar.show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.e(TAG, "LOG"+t.getMessage());
            }
        });
    }

    private void goToSignUpActivity() {
        startActivity(new Intent(SignActivity.this, SignUpActivity.class));
    }
}
