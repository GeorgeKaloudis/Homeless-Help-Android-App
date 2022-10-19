package com.example.homelessapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.homelessapp.DTO.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class SignupTabFragment extends Fragment {


    String rUsername,rFirstname,rLastname,rEmail,rPassword;
    Button registerButton;
    EditText username,firstname,lastname,email,password;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private FirebaseAuth mAuth;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.signup_tab_fragment,container,false);


        username = root.findViewById(R.id.registerUsername);
        firstname = root.findViewById(R.id.registerFirstname);
        lastname = root.findViewById(R.id.registerLastname);
        email = root.findViewById(R.id.registerEmail);
        password = root.findViewById(R.id.registerPassword);
        registerButton = root.findViewById(R.id.registerButton);

        mAuth = FirebaseAuth.getInstance();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rUsername = username.getText().toString().trim();
                rFirstname = firstname.getText().toString().trim();
                rLastname = lastname.getText().toString().trim();
                rEmail = email.getText().toString().trim();
                rPassword = password.getText().toString().trim();

                //Check for empty input
                if (TextUtils.isEmpty(rUsername) || rUsername.contains(" ")) {
                    //Toast.makeText(getApplicationContext(), "Please enter the Username", Toast.LENGTH_SHORT).show();
                    username.setError("Invalid Username.");
                    return;
                }
                if (TextUtils.isEmpty(rFirstname)) {
                    //Toast.makeText(getApplicationContext(), "Please enter the First Name", Toast.LENGTH_SHORT).show();
                    firstname.setError("Invalid First Name.");
                    return;
                }
                if (TextUtils.isEmpty(rLastname)) {
                    //Toast.makeText(getApplicationContext(), "Please enter your Last Name", Toast.LENGTH_SHORT).show();
                    lastname.setError("Invalid Last Name.");
                    return;
                }
                if (TextUtils.isEmpty(rEmail)){
                    //Toast.makeText(getApplicationContext(), "Please enter your Email", Toast.LENGTH_SHORT).show();
                    email.setError("Invalid Email.");
                    return;
                }
                if (TextUtils.isEmpty(rPassword)){
                    //Toast.makeText(getApplicationContext(), "Please enter your Password", Toast.LENGTH_SHORT).show();
                    password.setError("Invalid Password.");
                    return;
                }

                //Create user and save the information in firebase
                mAuth.createUserWithEmailAndPassword(rEmail, rPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String userid = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

                            User user = new User(rUsername,rFirstname,rLastname,rEmail,userid);
                            database = FirebaseDatabase.getInstance("https://homelessapp-57cb8-default-rtdb.firebaseio.com/");
                            ref = database.getReference("Users");

                            ref.push().setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getActivity(), "Registration Successful!", Toast.LENGTH_LONG).show();
                                        Intent i = new Intent(getActivity(), MainActivity.class);
                                        startActivity(i);
                                    } else {
                                        String errorMessage = Objects.requireNonNull(task.getException()).getMessage();
                                        Toast.makeText(getActivity(), "Error: " + errorMessage, Toast.LENGTH_LONG).show();
                                    }
                                }
                            });


                        } else {
                            Toast.makeText(getActivity(), "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });
        return root;

    }
}
