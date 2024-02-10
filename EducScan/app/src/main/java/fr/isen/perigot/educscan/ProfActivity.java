package fr.isen.perigot.educscan;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import fr.isen.perigot.educscan.databinding.ActivityMainBinding;
import fr.isen.perigot.educscan.databinding.ActivityProfBinding;

public class ProfActivity extends AppCompatActivity {

    private ActivityProfBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = ActivityProfBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_prof);
        AppBarConfiguration appBarConfiguration;

        Intent intent = getIntent();
        boolean isStudent = intent.getBooleanExtra("isStudent", false);

        BottomNavigationView navViewProf = findViewById(R.id.nav_view_prof);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_camembert, R.id.navigation_listes, R.id.navigation_parametres)
                .build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navViewProf, navController);
    }
}
