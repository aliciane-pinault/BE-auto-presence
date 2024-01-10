package fr.isen.perigot.educscan.ui.dashboard;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import fr.isen.perigot.educscan.R;
import fr.isen.perigot.educscan.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textDashboard;
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        // Appeler la fonction generateQRCode (a chaque démarrage de l'activité)
        //remplacer les infos par celle de la bdd
        // ajouter un truc hashé de la bdd ???
        //generateQRCode("Nom: Alciane, Age: 26");

        //pour démarrer le QRcode dynamqiue basique (change toutes les 30s en contenant un nombre aléatoire plus) (Démarrer la mise à jour du QR code)
        //startUpdatingQRCode();

        //regénérer QRcode avec fontion de hash dedans :
        startUpdatingQRCode_with_hash_function();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // affichage QRcode statique

    public void generateQRCode(String text) {
        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, 512, 512);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            // Utilisation du View Binding pour accéder à l'ImageView
            binding.imageViewQrCode.setImageBitmap(bmp);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    public Handler handler = new Handler();
    public Runnable updateQRCodeRunnable;
    public void startUpdatingQRCode() {
        updateQRCodeRunnable = new Runnable() {
            @Override
            public void run() {
                // Générer un nombre aléatoire
                Random random = new Random();
                int randomContent = random.nextInt(100); // Générer un nombre entre 0 et 99

                // Concaténer les informations (modifier pour utilise celle de firre base)
                String qrCodeText = "Nom : " + " Alciane, " +"Numéro aléatoire : " + randomContent;
                generateQRCode(qrCodeText);

                // Planifier la prochaine mise à jour après 30 secondes
                handler.postDelayed(this, 30000);
            }
        };

        // Lancer la première mise à jour immédiatement
        handler.post(updateQRCodeRunnable);
    }

    public String hashNumber(int number) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(Integer.toString(number).getBytes());

            StringBuilder hexString = new StringBuilder(2 * encodedhash.length);
            for (int i = 0; i < encodedhash.length; i++) {
                String hex = Integer.toHexString(0xff & encodedhash[i]);
                if(hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void startUpdatingQRCode_with_hash_function() {
        updateQRCodeRunnable = new Runnable() {
            @Override
            public void run() {
                // Générer un nombre aléatoire
                Random random = new Random();
                int randomNumber = random.nextInt(100); // Nombre entre 0 et 99

                // Hacher le nombre aléatoire
                String hashedNumber = hashNumber(randomNumber);

                // Vérifier si le hachage a réussi
                if (hashedNumber != null) {
                    String qrCodeText = "Nom : " + " Alciane, " + "Hashed Number: " + hashedNumber;
                    generateQRCode(qrCodeText);
                }

                // Planifier la prochaine mise à jour
                handler.postDelayed(this, 30000);
            }
        };

        // Lancer la première mise à jour
        handler.post(updateQRCodeRunnable);
    }
}