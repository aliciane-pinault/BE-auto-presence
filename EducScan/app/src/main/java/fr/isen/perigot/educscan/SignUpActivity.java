package fr.isen.perigot.educscan;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class SignUpActivity extends AppCompatActivity {
    EditText signupName, signupUsername, signupEmail, signupPassword;
    TextView loginRedirectText;
    Button signupButton;
    FirebaseDatabase database;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        signupName = findViewById(R.id.signup_name);
        signupEmail = findViewById(R.id.signup_email);
        signupUsername = findViewById(R.id.signup_username);
        signupPassword = findViewById(R.id.signup_password);
        loginRedirectText = findViewById(R.id.loginRedirectText);
        signupButton = findViewById(R.id.signup_button);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database = FirebaseDatabase.getInstance();
                reference = database.getReference("users");
                String name = signupName.getText().toString();
                String email = signupEmail.getText().toString();
                String username = signupUsername.getText().toString();
                String password = signupPassword.getText().toString();

                // Vérifier si tous les champs sont remplis
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                    // Afficher un message d'erreur si un champ est vide
                    Toast.makeText(SignUpActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return; // Sortir de la fonction onClick si un champ est vide
                }

                // Validation de l'adresse e-mail
                if (isValidEmail(email)) {
                    // Extraire le prénom et le nom de l'adresse e-mail
                    String[] emailParts = email.split("@")[0].split("\\.");
                    if (emailParts.length == 2) {
                        String firstName = emailParts[0];
                        String lastName = emailParts[1];

                        // Formez le nom d'utilisateur (prénom_nom)
                        String generatedUsername = firstName + "_" + lastName;

                        // Vérifier si l'username saisi correspond au format attendu
                        if (!username.equals(generatedUsername)) {
                            // Afficher un message d'erreur si l'username ne correspond pas au format attendu
                            signupUsername.setError("Use : first_last");
                            Toast.makeText(SignUpActivity.this, "Invalid username format", Toast.LENGTH_SHORT).show();
                            return; // Sortir de la fonction onClick si l'username ne correspond pas
                        }

                        // Hachez le mot de passe avec BCrypt
                        String passwordHash = hashPassword(password);

                        // Vérifie si l'e-mail correspond à un étudiant ou à un professeur
                        boolean isStudent = email.endsWith("isen.yncrea.fr");

                        HelperClass helperClass = new HelperClass(firstName + " " + lastName, email, generatedUsername, passwordHash, isStudent);
                        reference.child(username).setValue(helperClass);

                        Toast.makeText(SignUpActivity.this, "You have signed up successfully!", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                        startActivity(intent);
                    } else {
                        // Afficher un message d'erreur si l'adresse e-mail n'est pas au format attendu
                        Toast.makeText(SignUpActivity.this, "Invalid email address format", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Afficher un message d'erreur si l'adresse e-mail n'est pas valide
                    signupEmail.setError("Use YNCREA mail");
                    Toast.makeText(SignUpActivity.this, "Invalid email address", Toast.LENGTH_SHORT).show();
                }
                /*
                // Validation de l'adresse e-mail
                if (isValidEmail(email)) {
                    // Hachez le mot de passe avec BCrypt
                    String passwordHash = hashPassword(password);

                    // Vérifie si l'e-mail correspond à un étudiant ou à un professeur
                    boolean isStudent = email.endsWith("isen.yncrea.fr");

                    HelperClass helperClass = new HelperClass(name, email, username, passwordHash, isStudent);
                    reference.child(username).setValue(helperClass);

                    Toast.makeText(SignUpActivity.this, "You have signed up successfully!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    // Afficher un message d'erreur si l'adresse e-mail n'est pas valide
                    signupEmail.setError("Use YNCREA mail");
                    Toast.makeText(SignUpActivity.this, "Invalid email address", Toast.LENGTH_SHORT).show();
                }*/

                //HelperClass helperClass = new HelperClass(name, email, username, password);
               // reference.child(username).setValue(helperClass);
               // Toast.makeText(SignUpActivity.this, "You have signup successfully!", Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                //startActivity(intent);
            }
        });
        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    // Fonction pour valider une adresse e-mail avec une expression régulière
    private boolean isValidEmail(String email) {
        String emailPattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:isen\\.)?yncrea\\.fr$";
        return email.matches(emailPattern);
    }

    // Fonction pour hacher le mot de passe avec BCrypt
    private String hashPassword(String password) {
        // Le coût détermine le nombre d'itérations de hachage (10 est une valeur raisonnable)
        int cost = 10;
        return BCrypt.withDefaults().hashToString(cost, password.toCharArray());
    }

}


