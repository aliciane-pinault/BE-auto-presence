package fr.isen.perigot.educscan;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import at.favre.lib.crypto.bcrypt.BCrypt;
import fr.isen.perigot.educscan.databinding.ActivityLoginBinding;
import android.app.Application;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    EditText loginUsername, loginPassword;
    Button loginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginUsername = findViewById(R.id.username);
        loginPassword = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateUsername() | !validatePassword()) {
                } else {
                    checkUser();
                }
            }
        });

    }
    public Boolean validateUsername() {
        String val = loginUsername.getText().toString();
        if (val.isEmpty()) {
            loginUsername.setError("Username cannot be empty");
            return false;
        } else {
            loginUsername.setError(null);
            return true;
        }
    }
    public Boolean validatePassword(){
        String val = loginPassword.getText().toString();
        if (val.isEmpty()) {
            loginPassword.setError("Password cannot be empty");
            return false;
        } else {
            loginPassword.setError(null);
            return true;
        }
    }
    public void checkUser(){
        String userUsername = loginUsername.getText().toString().trim();
        String userPassword = loginPassword.getText().toString().trim();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("username").equalTo(userUsername);
        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    loginUsername.setError(null);
                    String passwordFromDB = snapshot.child(userUsername).child("password").getValue(String.class);
                    boolean isStudentFromDB = snapshot.child(userUsername).child("isStudent").getValue(Boolean.class);
                    if (BCrypt.verifyer().verify(userPassword.toCharArray(), passwordFromDB).verified) {
                        loginUsername.setError(null);
                        String nameFromDB = snapshot.child(userUsername).child("name").getValue(String.class);
                        String emailFromDB = snapshot.child(userUsername).child("email").getValue(String.class);
                        String usernameFromDB = snapshot.child(userUsername).child("username").getValue(String.class);
                        HelperClass helper = new HelperClass(nameFromDB,emailFromDB,usernameFromDB,passwordFromDB,true);
                        if(isStudentFromDB == true) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("name", nameFromDB);
                        intent.putExtra("email", emailFromDB);
                        intent.putExtra("username", usernameFromDB);
                        intent.putExtra("password", passwordFromDB);
                       // intent.putExtra("isStudent", isStudentFromDB);
                        startActivity(intent); }
                        else {
                            Intent intent = new Intent(LoginActivity.this, ProfActivity.class);
                            intent.putExtra("name", nameFromDB);
                            intent.putExtra("email", emailFromDB);
                            intent.putExtra("username", usernameFromDB);
                            intent.putExtra("password", passwordFromDB);
                        //    intent.putExtra("isStudent", isStudentFromDB);
                            startActivity(intent);
                        }
                    } else {
                        loginPassword.setError("Invalid Credentials");
                        loginPassword.requestFocus();
                    }
                } else {
                    loginUsername.setError("User does not exist");
                    loginUsername.requestFocus();
                }
                    /*if (passwordFromDB.equals(userPassword)) {
                        loginUsername.setError(null);
                        String nameFromDB = snapshot.child(userUsername).child("name").getValue(String.class);
                        String emailFromDB = snapshot.child(userUsername).child("email").getValue(String.class);
                        String usernameFromDB = snapshot.child(userUsername).child("username").getValue(String.class);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("name", nameFromDB);
                        intent.putExtra("email", emailFromDB);
                        intent.putExtra("username", usernameFromDB);
                        intent.putExtra("password", passwordFromDB);
                        startActivity(intent);
                    } else {
                        loginPassword.setError("Invalid Credentials");
                        loginPassword.requestFocus();
                    }
                } else {
                    loginUsername.setError("User does not exist");
                    loginUsername.requestFocus();
                }*/
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}


/* //Authentification avec Firebase Authentification (users)
public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectionUser();
            }
        });


    }

    private void connectionUser() {
        EditText emailEditText = findViewById(R.id.email);
        EditText passwordEditText = findViewById(R.id.password);

        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            String userId = user.getUid();

                            // Save user ID to SharedPreferences
                            getSharedPreferences("user_id", Context.MODE_PRIVATE)
                                    .edit()
                                    .putString("user_id", userId)
                                    .apply();

                            Log.d("TAG", "User is connected: " + user.toString());
                            Toast.makeText(LoginActivity.this, "Click Login", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    } else {
                        Log.e("TAG", "Connection error: " + task.getException());
                        Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                        // Display an error message in case of failure
                        if (task.getException() instanceof FirebaseAuthException) {
                            String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                            Log.e("TAG", "Error code: " + errorCode);
                        }
                    }
                });
    }

}*/



/*
public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private FirebaseAuth auth;
    private EditText email;
    private EditText password;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);

        // Vérifier la connexion


        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectionUser();
            }
        });
    }
        /*
        binding.forgetpassword.setOnclickListener(new View.OnClickListener() {
            @Override
            public void onCLick(View v) {
                Toast.makeText(LoginActivity.this, "Click Forget Password", Toast.LENGTH_SHORT).show();
            }
        });*/
/*
        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usernameToCheck = binding.username.getText().toString();
                String passwordToCheck = binding.password.getText().toString();

                boolean loginSuccessful = false;

                binding.loginButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (binding.username.getText().toString().equals("user") &&
                                binding.password.getText().toString().equals("1234")) {
                            Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "Login Failed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });


            // Initialiser les vues
            username = binding.username;
            password = binding.password;
            loginButton = binding.loginButton;
        }
    }
*/
/*
    private void connectionUser() {
        String user = email.getText().toString();
        String pass = password.getText().toString();

        /*auth.signInWithEmailAndPassword(user, pass)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser username = auth.getCurrentUser();
                        if (user != null) {
                            String userId = user.trim();

                            // Enregistrez l'ID de l'utilisateur dans les préférences partagées
                            SharedPreferences sharedPreferences = getSharedPreferences("user_id", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("user_id", userId);
                            editor.apply();

                            Log.d(ContentValues.TAG, "L'utilisateur est connecté: " + user);
                            Toast.makeText(LoginActivity.this, "Login Failed!", Toast.LENGTH_SHORT).show();

                            // Redirigez vers WallActivity après une connexion réussie
                            //Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                           // startActivity(intent);
                        }
                    } else {
                        // En cas d'échec, affichez un message d'erreur
                        Log.e(ContentValues.TAG, "Erreur de connexion: " + task.getException());

                        // Obtenez le code d'erreur
                        String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                        Log.e(ContentValues.TAG, "Code d'erreur: " + errorCode);
                    }
                });*/
/*
        DatabaseReference credentialsRef = FirebaseDatabase.getInstance().getReference("credentials");

        credentialsRef.orderByChild("username").equalTo(user).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        String savedPassword = userSnapshot.child("password").getValue().toString();

                        if (pass.equals(savedPassword)) {
                            // Mot de passe correct, connexion réussie
                            String userId = userSnapshot.child("id").getValue().toString();

                            // Enregistrez l'ID de l'utilisateur dans les préférences partagées
                            SharedPreferences sharedPreferences = getSharedPreferences("user_id", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("user_id", userId);
                            editor.apply();

                            Log.d(ContentValues.TAG, "Mot de passe correct");
                            Log.d(ContentValues.TAG, "L'utilisateur est connecté: " + user);
                            Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();

                            // Redirigez vers WallActivity après une connexion réussie
                            //Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            //startActivity(intent);
                        } else {
                            // Mot de passe incorrect
                            Log.e(ContentValues.TAG, "Mot de passe incorrect");
                            Toast.makeText(LoginActivity.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    // Utilisateur non trouvé dans la base de données
                    Log.e(ContentValues.TAG, "Utilisateur non trouvé");
                    Toast.makeText(LoginActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(ContentValues.TAG, "Erreur de base de données: " + databaseError.getMessage());
                Log.e(ContentValues.TAG, "Details de l'erreur: " + databaseError.getDetails());
                Log.e(ContentValues.TAG, "Code d'erreur: " + databaseError.getCode());
            }
        });
    }


}
*/