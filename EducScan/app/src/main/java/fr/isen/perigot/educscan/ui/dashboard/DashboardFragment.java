package fr.isen.perigot.educscan.ui.dashboard;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.media3.common.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.Random;
import java.util.Calendar;

import fr.isen.perigot.educscan.R;
import fr.isen.perigot.educscan.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment {

    // Déclaration des références Firebase
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;

    //stockage de l'utilisateur
    private String currentUserUsername;


    // Déclaration des variables pour le chronomètre
    private TextView countdownTimer;
    private long timeLeftInMillis;
    private CountDownTimer countDownTimer;
    private boolean timerRunning;
    private FragmentDashboardBinding binding;

    @SuppressLint("CutPasteId")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        currentUserUsername = getActivity().getIntent().getStringExtra("username");

        final TextView textView = binding.textDashboard;
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        //initialiser le QRcode QR code dynamique avec info de firebase :
        dynamiqueQRCode();

        // Récupérer la référence du TextView "textClock" à partir de la mise en page XML
        TextView textDashboard = root.findViewById(R.id.textClock);

        // Initialisation du TextView pour le chronomètre
        countdownTimer = root.findViewById(R.id.textClock); // Modifier cette ligne

        // Durée du chrono en millisecondes (ici, 10 secondes)
        timeLeftInMillis = 10000;

        // Démarrer le chronomètre
        startTimer();

        return root;
    }

    public void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountdownText();
            }

            @Override
            public void onFinish() {
                //QR code dynamique avec info de firebase :
                dynamiqueQRCode();
                timeLeftInMillis = 10000;
                startTimer();
            }
        }.start();
        timerRunning = true;
    }

    // Méthode pour mettre à jour le texte du chronomètre
    public void updateCountdownText() {
        int seconds = (int) (timeLeftInMillis / 1000);
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", seconds / 60, seconds % 60);
        countdownTimer.setText(timeLeftFormatted);
    }

    // Creation d'un QRcode statique
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

    public String hashNumber(int number) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(Integer.toString(number).getBytes());
            StringBuilder hexString = new StringBuilder(2 * encodedhash.length);
            for (int i = 0; i < encodedhash.length; i++) {
                String hex = Integer.toHexString(0xff & encodedhash[i]);
                if (hex.length() == 1) {
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

    public void dynamiqueQRCode() {
        updateQRCodeRunnable = new Runnable() {
            @Override
            public void run() {
                // Obtenir l'heure actuelle
                Calendar calendrier = Calendar.getInstance();
                int heure = calendrier.get(Calendar.HOUR_OF_DAY);
                int minute = calendrier.get(Calendar.MINUTE);
                int seconde = calendrier.get(Calendar.SECOND);

                // Formatage de l'heure
                @SuppressLint("DefaultLocale") String heureFormattee = String.format("%02d:%02d:%02d", heure, minute, seconde);

                // Utilisation de l'heure actuelle dans le QRcode
                String qrCodeText = "Username : " + currentUserUsername + " ; Heure : " + heureFormattee;

                // Mettre à jour le QRcode
                generateQRCode(qrCodeText);

                // Planifier la prochaine mise à jour
                handler.postDelayed(this, 10000);
            }
        };
        // Lancer la première mise à jour
        handler.post(updateQRCodeRunnable);
    }
}