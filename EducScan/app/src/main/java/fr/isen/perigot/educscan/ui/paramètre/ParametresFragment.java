package fr.isen.perigot.educscan.ui.paramètre;

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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

        return root;

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}