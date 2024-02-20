package fr.isen.perigot.educscan.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import fr.isen.perigot.educscan.R;

public class PresentFragment extends Fragment {

    private RecyclerView recyclerViewPresent;
    private PresentAdapter presentAdapter;


    public PresentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_present, container, false);

        // Initialisez la RecyclerView
        recyclerViewPresent = view.findViewById(R.id.recyclerViewPresent);
        recyclerViewPresent.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialisez l'adaptateur avec une liste vide au début
        presentAdapter = new PresentAdapter(new ArrayList<>());
        recyclerViewPresent.setAdapter(presentAdapter);

        return inflater.inflate(R.layout.fragment_present, container, false);
    }
}