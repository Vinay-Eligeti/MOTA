package com.example.mota.UserFragments;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.mota.R;
import com.example.mota.Utility;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class QR_Code extends Fragment {
    private SharedPreferences sharedPreferences;
    Utility utility;
    ImageView iv_qr;
    String examID, rollNo;
    Button backBtn;

    //update
    private FirebaseFirestore db;
    private DocumentReference statusRef;
    private ListenerRegistration statusListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_q_r__code, container, false);
        iv_qr = view.findViewById(R.id.image_qr);
        backBtn = view.findViewById(R.id.back_btn);
        sharedPreferences = getContext().getSharedPreferences("my_preferences", getContext().MODE_PRIVATE);
        db = FirebaseFirestore.getInstance();
        backBtn.setOnClickListener(v -> {
            TestSchedule destinationFragment = new TestSchedule();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, destinationFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });
        examID = getArguments().getString("examID");
        rollNo = sharedPreferences.getString("RollNo", "");
        generateQR();
        statusRef = db.collection("Attendance")
                .document(examID)
                .collection("Students")
                .document(rollNo);
        statusListener = statusRef.addSnapshotListener((documentSnapshot, e) -> {
            if (documentSnapshot != null && documentSnapshot.exists()) {
                boolean status = documentSnapshot.getBoolean("status");
                if (status) {
                    Bundle bundle = new Bundle();
                    bundle.putString("examID", examID);
                    // Navigate to TestCompleted.java
                    TestCompleted destinationFragment = new TestCompleted();
                    destinationFragment.setArguments(bundle);
                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame_layout, destinationFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        });
        return view;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Remove the listener when the fragment is destroyed
        if (statusListener != null) {
            statusListener.remove();
        }
    }

    private void generateQR() {
        utility=new Utility();
        String text = utility.encrypt("" + examID.trim() + "," + rollNo,3);
        MultiFormatWriter writer = new MultiFormatWriter();
        try {
            BitMatrix matrix = writer.encode(text, BarcodeFormat.QR_CODE, 700, 700);
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(matrix);
            //set data image to imageview
            iv_qr.setImageBitmap(bitmap);

        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
}