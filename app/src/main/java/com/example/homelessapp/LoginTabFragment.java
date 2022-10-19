package com.example.homelessapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class LoginTabFragment extends Fragment {

    private static final int RC_SIGN_IN =123 ;
    private FirebaseAuth mAuth;
    GoogleSignInClient gsc;
    TextView forgotPassword;
    EditText email,password;
    Button loginButton;
    ImageView fb,gm;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    float v = 0;


    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.login_tab_fragment,container,false);

        //createRequest();

        loginButton = (Button) root.findViewById(R.id.loginButton);
        email = (EditText) root.findViewById(R.id.loginEmail);
        password = (EditText) root.findViewById(R.id.loginPassword);
//      forgotPassword = (TextView) root.findViewById(R.id.forgotPassword);
        mAuth = FirebaseAuth.getInstance();

        email.setTranslationX(800);
        password.setTranslationX(800);
        loginButton.setTranslationX(800);
//      forgotPassword.setTranslationX(800);

        email.setAlpha(v);
        password.setAlpha(v);
        loginButton.setAlpha(v);
//      forgotPassword.setAlpha(v);

        email.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(300).start();
        password.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(500).start();
//      forgotPassword.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(500).start();
        loginButton.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(700).start();

        SharedPreferences pref = getContext().getSharedPreferences("userDetails", Context.MODE_PRIVATE);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser !=null){
            FirebaseAuth.getInstance().signOut();
        }


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(email.getText().toString())) {
                    email.setError("Email ID is Required.");
                    return;
                }
                if (TextUtils.isEmpty(password.getText().toString())) {
                    password.setError("Password is Required.");
                    return;
                }
                if (password.length() < 6) {
                    password.setError("Password Must be more than 6 Characters");
                    return;
                }
                //Check from firebase if email and password are correct
                mAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            database = FirebaseDatabase.getInstance("https://homelessapp-57cb8-default-rtdb.firebaseio.com/");
                            ref = database.getReference("Users");
                            Query query = ref.orderByChild("userid").equalTo(mAuth.getCurrentUser().getUid());
                            query.addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                    //When the user login the user key from firebase is saved on Shared Preferences for using it in other activities
                                    String myParentNode = snapshot.getKey();
                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.putString("user_key", myParentNode);
                                    editor.apply();
                                }

                                @Override
                                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                }

                                @Override
                                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                                }

                                @Override
                                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
//                          Toast.makeText(getContext(), mAuth.getCurrentUser().getUid().toString(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getActivity(), MainMenu.class);
                            startActivity(intent);
                            getActivity().finish();
                        }else {
                            Toast.makeText(getActivity(), "error", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });


        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                AuthCredential credentials = GoogleAuthProvider.getCredential(account.getIdToken(),null);
                mAuth.signInWithCredential(credentials).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Intent intent = new Intent(getActivity(), MainMenu.class);
                            startActivity(intent);
                        }else {
                            Log.d("tag", "signInResult:failed code=");
                        }
                    }
                });
                // Signed in successfully, show authenticated UI.
            } catch (ApiException e) {
                // The ApiException status code indicates the detailed failure reason.
                // Please refer to the GoogleSignInStatusCodes class reference for more information.
                Log.d("tag", "signInResult:failed code=" + e.getStatusCode());
            }
        }
    }


//    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
//    }
//
//    private void createRequest(){
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build();
//
//        gsc = GoogleSignIn.getClient(getActivity(),gso);
//    }

    private void signIn(){
        Intent sigInIntent = gsc.getSignInIntent();
        startActivityForResult(sigInIntent,RC_SIGN_IN);
    }
}
