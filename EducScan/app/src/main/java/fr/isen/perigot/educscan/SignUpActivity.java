package fr.isen.perigot.educscan;

import android.content.Intent;
import android.os.Bundle;
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
                //Regarde si mail yncrea.fr ou isen.yncrea.fr
                //Condition if
                //Si etudiant, envoie en tant que etudiant
                //Sinon en tant que prof
                String username = signupUsername.getText().toString();
                String password = signupPassword.getText().toString();

                // Validation de l'adresse e-mail
                if (isValidEmail(email)) {
                    // Vérifie si l'e-mail correspond à un étudiant ou à un professeur
                    boolean isStudent = email.endsWith("isen.yncrea.fr");
                    HelperClass helperClass = new HelperClass(name, email, username, password, isStudent);
                    reference.child(username).setValue(helperClass);

                    Toast.makeText(SignUpActivity.this, "You have signed up successfully!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    // Afficher un message d'erreur si l'adresse e-mail n'est pas valide
                    Toast.makeText(SignUpActivity.this, "Invalid email address", Toast.LENGTH_SHORT).show();
                }

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

}


