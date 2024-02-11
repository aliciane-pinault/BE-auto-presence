package fr.isen.perigot.educscan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class ChangepwdActivity extends AppCompatActivity {

    private EditText oldPasswordEditText, newPasswordEditText;
    private Button changePasswordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepwd);

        oldPasswordEditText = findViewById(R.id.old_password_editText);
        newPasswordEditText = findViewById(R.id.new_password_editText);
        changePasswordButton = findViewById(R.id.change_password_button);

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });
    }

    private void changePassword() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            String oldPassword = oldPasswordEditText.getText().toString().trim();
            String newPassword = newPasswordEditText.getText().toString().trim();
            getPasswordFromFirebase(user.getUid(), new OnPasswordRetrievedListener() {
                @Override
                public void onPasswordRetrieved(String storedPasswordFromDB) {
                    if (BCrypt.verifyer().verify(oldPassword.toCharArray(), storedPasswordFromDB).verified) {
                        // Ancien mot de passe correct, mise à jour avec le nouveau mot de passe
                        user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ChangepwdActivity.this, "Mot de passe changé avec succès", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(ChangepwdActivity.this, "Échec du changement de mot de passe", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(ChangepwdActivity.this, "Ancien mot de passe incorrect", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onPasswordRetrieveFailed() {
                    Toast.makeText(ChangepwdActivity.this, "Échec de récupération du mot de passe", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // L'utilisateur n'est pas connecté, gérer en conséquence
            Toast.makeText(ChangepwdActivity.this, "Utilisateur non connecté", Toast.LENGTH_SHORT).show();
        }
    }

    // Utilisez votre logique pour récupérer le mot de passe depuis Firebase avec BCrypt
    private void getPasswordFromFirebase(String userId, OnPasswordRetrievedListener listener) {
        // Remplacez cela par votre logique réelle pour récupérer le mot de passe haché depuis Firebase
        // Assurez-vous d'appeler onPasswordRetrieved avec le mot de passe haché récupéré
        // ou onPasswordRetrieveFailed en cas d'échec
        String passwordFromDB = "$2a$10$yMN3D6./PT7vexYl4czQYuhK.nQonr1i9MyaP4.RzhP4vN0m2tac6"; // Mot de passe: "password"
        listener.onPasswordRetrieved(passwordFromDB);
    }

    // Interface pour gérer le résultat de la récupération du mot de passe
    private interface OnPasswordRetrievedListener {
        void onPasswordRetrieved(String storedPasswordFromDB);

        void onPasswordRetrieveFailed();
    }

}
