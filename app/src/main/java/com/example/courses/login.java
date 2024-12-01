package com.example.courses;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {
    private EditText userNameEdt, pwdEdt;
    private TextView registerTV;
    private Button loginBtn;
    private ProgressBar lodingPB;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        userNameEdt = findViewById(R.id.idEdtUserName);
        pwdEdt = findViewById(R.id.idEdtPwd);
        registerTV = findViewById(R.id.idTVRegister);
        lodingPB = findViewById(R.id.idPBLoading);
        loginBtn = findViewById(R.id.idBtnLogin);

        mAuth = FirebaseAuth.getInstance();

        registerTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(login.this, register.class);
                startActivity(i);
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lodingPB.setVisibility(View.VISIBLE);
                String userName = userNameEdt.getText().toString();
                String pwd = pwdEdt.getText().toString();
                if (TextUtils.isEmpty(userName) && TextUtils.isEmpty(pwd)) {
                    Toast.makeText(login.this, "Please enter your credentials ", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    mAuth.signInWithEmailAndPassword(userName, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                lodingPB.setVisibility(View.GONE);
                                Toast.makeText(login.this, "Login successful ", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(login.this, home.class);
                                startActivity(i);
                                finish();

                            }else{
                                lodingPB.setVisibility(View.GONE);
                                Toast.makeText(login.this, "Failed to login ", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
            }
        });
    }


}