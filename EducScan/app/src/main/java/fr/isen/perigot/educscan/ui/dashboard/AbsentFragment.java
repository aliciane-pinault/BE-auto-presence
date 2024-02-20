package fr.isen.perigot.educscan.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import fr.isen.perigot.educscan.R;
import fr.isen.perigot.educscan.databinding.FragmentListesBinding;


public class AbsentFragment extends Fragment {
    private AbsentFragment binding;
    public AbsentFragment() {
        // Required empty public constructor

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_absent, container, false);
    }
}