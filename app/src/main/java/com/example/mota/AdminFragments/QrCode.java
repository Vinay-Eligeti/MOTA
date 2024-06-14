package com.example.mota.AdminFragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.mota.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class QrCode extends Fragment {
    ImageView iv_qr;
    String examID;
    Button backBtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_qr_code, container, false);
        iv_qr = view.findViewById(R.id.iv_qr);
        backBtn=view.findViewById(R.id.back_button);
        backBtn.setOnClickListener(v->{
            AdminTestSchedule destinationFragment = new AdminTestSchedule();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, destinationFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        examID = getArguments().getString("examID");
        generateQR();
        return view;
    }
    private void generateQR()
    {
        String text = examID.trim();
        MultiFormatWriter writer = new MultiFormatWriter();
        try
        {
            BitMatrix matrix = writer.encode(text, BarcodeFormat.QR_CODE,700,700);
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(matrix);
            //set data image to imageview
            iv_qr.setImageBitmap(bitmap);

        } catch (WriterException e)        {
            e.printStackTrace();
        }
    }
}