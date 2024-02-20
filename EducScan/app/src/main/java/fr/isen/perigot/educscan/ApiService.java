package fr.isen.perigot.educscan;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService<Intervention> {
    @GET("autopresence.json")
    Call<List<Intervention>> getInterventions();
}
