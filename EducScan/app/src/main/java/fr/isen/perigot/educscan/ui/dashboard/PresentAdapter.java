package fr.isen.perigot.educscan.ui.dashboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import fr.isen.perigot.educscan.R;

public class PresentAdapter extends RecyclerView.Adapter<PresentAdapter.ViewHolder> {

    private List<Intervention> mListPresent;

    // Constructeur prenant la liste d'interventions présentes
    public PresentAdapter(List<Intervention> listPresent) {
        mListPresent = listPresent;
    }

    // Méthode pour créer une nouvelle vue
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_present, parent, false);
        return new ViewHolder(view);
    }

    // Méthode pour lier les données à la vue
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Intervention intervention = mListPresent.get(position);
        // Mettez à jour la vue avec les données de l'intervention
        holder.bind(intervention);
    }

    // Méthode pour obtenir le nombre total d'éléments dans la liste
    @Override
    public int getItemCount() {
        return mListPresent.size();
    }

    // Classe ViewHolder pour contenir les vues des éléments de la liste
    public static class ViewHolder extends RecyclerView.ViewHolder {

        // Déclarez les vues à utiliser dans chaque élément de la liste
        private TextView itemPresent;
        private TextView heureArrive;
        private TextView textViewDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialisez les vues
            itemPresent = itemView.findViewById(R.id.itemPresent);
            heureArrive = itemView.findViewById(R.id.heure_arrive);
        }


        // Méthode pour lier les données à la vue
        public void bind(Intervention intervention) {
            itemPresent.setText(intervention.getId_apprenant());
            heureArrive.setText(String.valueOf(intervention.getId_absent()));
        }
    }
}
