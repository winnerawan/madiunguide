package net.winnerawan.openmadiun.activity;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;

import net.winnerawan.openmadiun.R;
import net.winnerawan.openmadiun.helper.AppInterface;
import net.winnerawan.openmadiun.helper.AppRequest;
import net.winnerawan.openmadiun.response.UserResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    private EditText txtUsername,txtEmail,txtPassword;
    private ImageView btnBack,btnSignUp;
    private AppInterface api;
    private CoordinatorLayout signCoordinator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        overridePendingTransition(R.anim.anim_pop_left, R.anim.anim_push_left);
        initLayout();

        AppRequest request = new AppRequest();
        api = request.Guide().create(AppInterface.class);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.image_click));
                finishAction();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.image_click));
                String username = txtUsername.getText().toString().trim();
                String email = txtEmail.getText().toString().trim();
                String password = txtPassword.getText().toString().trim();
                if (!username.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                    signUp(username, email, password);
                } else {
                    Snackbar snackbar = Snackbar
                            .make(signCoordinator, "Check Username / Email / Password", Snackbar.LENGTH_LONG);

                    snackbar.show();
                }
            }
        });
    }

    private void initLayout() {
        this.btnBack = (ImageView) findViewById(R.id.back);
        this.btnSignUp = (ImageView) findViewById(R.id.btnSignUp);
        this.txtUsername = (EditText) findViewById(R.id.txtUsername);
        this.txtEmail = (EditText) findViewById(R.id.txtEmail);
        this.txtPassword = (EditText) findViewById(R.id.txtPassword);
        this.signCoordinator = (CoordinatorLayout) findViewById(R.id.signCoordinator);
    }

    private void signUp(String username, String email, String password) {
        Call<UserResponse> sign = api.signUp(username, email, password);
        sign.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response!=null && response.body().getStatus()==200) {
                    Snackbar snackbar = Snackbar
                            .make(signCoordinator, "Sign Up Success...", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    Intent i = new Intent(SignUpActivity.this, SignActivity.class);
                    startActivity(i);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {

            }
        });
    }

    private void finishAction() {
        finish();
        overridePendingTransition(R.anim.anim_pop_right, R.anim.anim_push_right);
    }

    public void onBackPressed() {
        finishAction();
    }
}
