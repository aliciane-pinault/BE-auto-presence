package fr.isen.perigot.educscan.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.androidplot.pie.PieChart;
import com.androidplot.pie.Segment;
import com.androidplot.pie.SegmentFormatter;

import fr.isen.perigot.educscan.R;
import fr.isen.perigot.educscan.databinding.FragmentCamembertBinding;


public class CamembertFragment extends Fragment {
    private FragmentCamembertBinding binding;
    PieChart camembert;
    Segment s1, s2;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CamembertViewModel camembertViewModel =
                new ViewModelProvider(this).get(CamembertViewModel.class);

        binding = FragmentCamembertBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //final TextView textView = binding.textHome;
        //homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        camembert = root.findViewById(R.id.PieChart);

        s1 = new Segment("Présents", 20);
        s2 = new Segment("Absents", 10);

        SegmentFormatter sf1 = new SegmentFormatter(getResources().getColor(R.color.bordeau));
        SegmentFormatter sf2 = new SegmentFormatter(getResources().getColor(R.color.black));

        camembert.addSegment(s1, sf1);
        camembert.addSegment(s2, sf2);



        return root;
    }

    @Override
    public void onDestroyView() {
       super.onDestroyView();
        binding = null;
    }

}
