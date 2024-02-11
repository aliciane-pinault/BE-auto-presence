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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class ChangepwdActivity extends AppCompatActivity {

    private EditText oldPasswordEditText, newPasswordEditText;
    private Button changePasswordButton;

    String currentUserUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepwd);

        oldPasswordEditText = findViewById(R.id.old_password_editText);
        newPasswordEditText = findViewById(R.id.new_password_editText);
        //confirmPassword = findViewById(R.id.confirmPassword);
        changePasswordButton = findViewById(R.id.change_password_button);


        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateOldPassword() | !validateNewPassword()) {
                    // Validation failed
                } else {
                    changePassword();
                }
            }
        });

        /*
        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });*/
    }

    private Boolean validateOldPassword() {
        String val = oldPasswordEditText.getText().toString().trim();
        if (val.isEmpty()) {
            oldPasswordEditText.setError("Old password cannot be empty");
            return false;
        } else {
            oldPasswordEditText.setError(null);
            return true;
        }
    }


    private Boolean validateNewPassword() {
        String val = newPasswordEditText.getText().toString().trim();
        // Implement additional validation if needed
        if (val.isEmpty()) {
            newPasswordEditText.setError("New password cannot be empty");
            return false;
        } else {
            newPasswordEditText.setError(null);
            return true;
        }
    }

    /*private Boolean validateConfirmPassword() {
        String val = confirmPassword.getText().toString().trim();
        String newPasswordVal = newPassword.getText().toString().trim();
        if (val.isEmpty()) {
            confirmPassword.setError("Confirm password cannot be empty");
            return false;
        } else if (!val.equals(newPasswordVal)) {
            confirmPassword.setError("Passwords do not match");
            return false;
        } else {
            confirmPassword.setError(null);
            return true;
        }
    }*/

    private void changePassword() {
        String userOldPassword = oldPasswordEditText.getText().toString().trim();
        String userNewPassword = newPasswordEditText.getText().toString().trim();
        //String userConfirmPassword = confirmPassword.getText().toString().trim();

        // Fetch the user's current password from Firebase
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("username").equalTo(currentUserUsername);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String passwordFromDB = snapshot.child(currentUserUsername).child("password").getValue(String.class);

                    // Verify old password using BCrypt
                    if (BCrypt.verifyer().verify(userOldPassword.toCharArray(), passwordFromDB).verified) {
                        // Old password is correct, proceed to update the password
                        String newPasswordHash = BCrypt.withDefaults().hashToString(12, userNewPassword.toCharArray());
                        reference.child(currentUserUsername).child("password").setValue(newPasswordHash);

                        // Password changed successfully, you can show a success message or navigate to another activity
                        Toast.makeText(ChangepwdActivity.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        // Old password is incorrect
                        // Old password is incorrect, show error message
                        Toast.makeText(ChangepwdActivity.this, "Incorrect old password", Toast.LENGTH_SHORT).show();
                        oldPasswordEditText.setError("Incorrect old password");
                        oldPasswordEditText.requestFocus();
                    }
                } else {
                    // User not found
                    Toast.makeText(ChangepwdActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error if needed
            }
        });
    }
}
    /*
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
*/

