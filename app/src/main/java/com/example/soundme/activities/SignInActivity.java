package com.example.soundme.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.soundme.R;
import com.example.soundme.constant.GlobalFuntion;
import com.example.soundme.databinding.ActivitySignInBinding;
import com.example.soundme.utils.StringUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class SignInActivity extends AppCompatActivity {
    private ActivitySignInBinding mActivitySignInBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivitySignInBinding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(mActivitySignInBinding.getRoot());

        initListener();
    }

    private void initListener() {
        mActivitySignInBinding.layoutSignUp.setOnClickListener(
                v -> GlobalFuntion.startActivity(SignInActivity.this, SignUpActivity.class));

        mActivitySignInBinding.btnSignIn.setOnClickListener(v -> onClickValidateSignIn());
        mActivitySignInBinding.tvForgotPassword.setOnClickListener(v -> onClickForgotPassword());
    }

    private void onClickForgotPassword() {
        GlobalFuntion.startActivity(this, ForgotPasswordActivity.class);
    }

    private void onClickValidateSignIn() {
        String strEmail = mActivitySignInBinding.edtEmail.getText().toString().trim();
        String strPassword = mActivitySignInBinding.edtPassword.getText().toString().trim();
        if (StringUtil.isEmpty(strEmail)) {
            Toast.makeText(SignInActivity.this, getString(R.string.msg_email_require), Toast.LENGTH_SHORT).show();
        } else if (StringUtil.isEmpty(strPassword)) {
            Toast.makeText(SignInActivity.this, getString(R.string.msg_password_require), Toast.LENGTH_SHORT).show();
        } else if (!StringUtil.isValidEmail(strEmail)) {
            Toast.makeText(SignInActivity.this, getString(R.string.msg_email_invalid), Toast.LENGTH_SHORT).show();
        } else {
            signInUser(strEmail, strPassword);
        }
    }

    private void signInUser(String email, String password) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {
//                            User userObject = new User(user.getEmail(), password);
//                            DataStoreManager.setUser(userObject);
                            GlobalFuntion.startActivity(SignInActivity.this, MainActivity.class);
                            finishAffinity();
                        }
                    } else {
                        Toast.makeText(SignInActivity.this, getString(R.string.msg_sign_in_error),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}