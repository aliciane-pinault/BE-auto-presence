package fr.isen.perigot.educscan.ui.paramètre;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import fr.isen.perigot.educscan.LoginActivity;
import fr.isen.perigot.educscan.R;
import fr.isen.perigot.educscan.SignUpActivity;
import fr.isen.perigot.educscan.databinding.FragmentNotificationsBinding;
import fr.isen.perigot.educscan.ui.dashboard.DashboardFragment;

public class ParametresFragment extends Fragment {

    private FragmentNotificationsBinding binding;

    EditText loginUsername, signupName;
    FirebaseDatabase database;
    DatabaseReference reference;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        PrametresViewModel parametresViewModel =
                new ViewModelProvider(this).get(PrametresViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //TextView buttonID = findViewById(R.id.textView2);
        //buttonID.setOnClickListener(this::onClick);


        return root;

    }
    private void onClick(View view) {
        //Intent intent = new Intent(ParametresFragment.this, LoginActivity.class);
        //startActivity(intent);
        }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}