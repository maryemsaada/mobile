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

public class register extends AppCompatActivity {

    private EditText userNameEdt,pwdEdt,cnfPwdEdt;
    private TextView loginTV;
    private Button registerBtn;
    private ProgressBar lodingPB;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        userNameEdt = findViewById(R.id.idEdtUserName);
        pwdEdt = findViewById(R.id.idEdtPwd);
        cnfPwdEdt = findViewById(R.id.idedtCnfPwd);
        loginTV = findViewById(R.id.login_link);
        lodingPB =findViewById(R.id.idPBLoading);
        registerBtn=findViewById(R.id.idBtnRegister);
        mAuth=FirebaseAuth.getInstance();
        loginTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(  register.this, login.class);
                startActivity(i);
            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lodingPB.setVisibility(View.VISIBLE);
                String userName=userNameEdt.getText().toString();
                String pwd= pwdEdt.getText().toString();
                String cnfPwd = cnfPwdEdt.getText().toString();
                if(!pwd.equals(cnfPwd)){
                    Toast.makeText(register.this,"Please enter a valid password ",Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(userName) && TextUtils.isEmpty(pwd) && TextUtils.isEmpty(cnfPwd)  ) {
                    Toast.makeText(register.this, "Please enter your credentials ", Toast.LENGTH_SHORT).show();
                }  else {
                    mAuth.createUserWithEmailAndPassword(userName,pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                lodingPB.setVisibility(view.GONE);
                                Toast.makeText(register.this,"User registered ",Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(  register.this, login.class);
                                startActivity(i);
                                finish();
                            }
                            else{
                                lodingPB.setVisibility((View.GONE));
                                Toast.makeText(register.this,"Failed to register user ",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }


        });
    }
}