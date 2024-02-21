package fr.isen.perigot.educscan;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fr.isen.perigot.educscan.ui.dashboard.Presences;
import fr.isen.perigot.educscan.ui.dashboard.PresentAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;

public interface ApiService {
    @GET("presences")
    Call<List<Presences>> getPresences();

}