package com.example.pingpong;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Register extends AppCompatActivity {

    EditText mEmailEditText;

    EditText mPasswordEditText;

    EditText mConfirmPasswordText;

    Button mRegisterButton;

    TextView mLoginTextView;

    DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEmailEditText = findViewById(R.id.register_email);
        mPasswordEditText = findViewById(R.id.register_password);
        mConfirmPasswordText = findViewById(R.id.register_confirm_password);
        mRegisterButton = findViewById(R.id.register_button);
        mLoginTextView = findViewById(R.id.login_text);

        mDatabaseHelper = new DatabaseHelper(this);

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmailEditText.getText().toString().trim();
                String password = mPasswordEditText.getText().toString().trim();
                String confirmpassword = mConfirmPasswordText.getText().toString().trim();


                if(TextUtils.isEmpty(email)){
                    Toast.makeText(Register.this, "Digite o seu Email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)){
                    Toast.makeText(Register.this, "Digite sua Senha", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(confirmpassword)){
                    Toast.makeText(Register.this, "Por Favor confirme sua senha", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.equals(confirmpassword)){
                    Toast.makeText(Register.this, "As senhas não conferem!", Toast.LENGTH_SHORT).show();
                    return;
                }
                boolean userExists = mDatabaseHelper.checkUser(email, password);

                if (userExists){
                    Toast.makeText(Register.this, "Usuário com este email ja existe", Toast.LENGTH_SHORT).show();
                    return;
                }

                long id = mDatabaseHelper.createUser(email, password);
                if (id > 0){
                    Toast.makeText(Register.this, "Cadastro Realizado!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Register.this, Login.class);
                    startActivity(intent);

                    finish();

                }
                else {
                    Toast.makeText(Register.this, "Cadastro Negado, Usuário ja existe", Toast.LENGTH_SHORT).show();
                }

            }
        });

        mLoginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);

                finish();
            }
        });

    }
}