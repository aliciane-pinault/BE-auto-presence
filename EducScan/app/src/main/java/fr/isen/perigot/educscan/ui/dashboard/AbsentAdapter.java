package fr.isen.perigot.educscan.ui.dashboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fr.isen.perigot.educscan.R;

public class AbsentAdapter extends RecyclerView.Adapter<AbsentAdapter.ViewHolder> {

    private List<Intervention> mListAbsent;

    // Constructeur prenant la liste d'interventions présentes
    public AbsentAdapter(List<Intervention> listAbsent) {
        mListAbsent = listAbsent;
    }

    // Méthode pour créer une nouvelle vue
    @NonNull
    @Override
    public AbsentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_absent, parent, false);
        return new AbsentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    // Méthode pour lier les données à la vue
    public void onBindViewHolder(@NonNull PresentAdapter.ViewHolder holder, int position) {
        Intervention intervention = mListAbsent.get(position);
        // Mettez à jour la vue avec les données de l'intervention
        holder.bind(intervention);
    }

    // Méthode pour obtenir le nombre total d'éléments dans la liste
    @Override
    public int getItemCount() {
        return mListAbsent.size();
    }

    // Classe ViewHolder pour contenir les vues des éléments de la liste
    public static class ViewHolder extends RecyclerView.ViewHolder {

        // Déclarez les vues à utiliser dans chaque élément de la liste
        private TextView itemAbsent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialisez les vues
            itemAbsent = itemView.findViewById(R.id.itemAbsent);

        }


        // Méthode pour lier les données à la vue
        public void bind(Intervention intervention) {
            itemAbsent.setText(intervention.getId_apprenant());
        }
    }
}
