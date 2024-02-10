package fr.isen.perigot.educscan.ui.paramètre;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.FragmentNavigator;


import android.util.Log;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import fr.isen.perigot.educscan.LoginActivity;
import fr.isen.perigot.educscan.R;
import fr.isen.perigot.educscan.User;

import fr.isen.perigot.educscan.databinding.FragmentNotificationsBinding;

public class ParametresFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        PrametresViewModel parametresViewModel =
                new ViewModelProvider(this).get(PrametresViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        String name = getActivity().getIntent().getStringExtra("name");
        String email = getActivity().getIntent().getStringExtra("email");
        String username = getActivity().getIntent().getStringExtra("username");

       DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("user");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //User user = dataSnapshot.getValue(User.class);
                
                binding.nom.setText(name);
                binding.mail.setText(email);
                binding.id.setText(username);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        // Récupérer la référence du bouton de déconnexion
        ImageButton logoutButton = root.findViewById(R.id.logout_button);

        // Ajouter un écouteur de clic sur le bouton de déconnexion
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Mettre ici la logique de déconnexion
                // Par exemple, vous pouvez démarrer une nouvelle activité d'authentification
                // ou vider les informations d'identification et rediriger vers l'écran de connexion.

                // Exemple de déconnexion avec Firebase Auth
                FirebaseAuth.getInstance().signOut();

                // Rediriger vers l'écran de connexion
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();  // Facultatif : pour fermer l'écran actuel après la déconnexion
            }
        });

        return root;

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}