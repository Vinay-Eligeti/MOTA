package com.example.mota.UserFragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.mota.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class UserHome extends Fragment {

    Button scan_btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_home, container, false);
        scan_btn=view.findViewById(R.id.scan_button);
        scan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = IntentIntegrator.forSupportFragment(UserHome.this);

                integrator.setOrientationLocked(false);
                integrator.setPrompt("");
                integrator.setBeepEnabled(false);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
                integrator.initiateScan();
            }
        });
        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(getContext(), "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                String scanned_data=result.getContents();
                Bundle bundle=new Bundle();
                bundle.putString("scannedData", scanned_data);
                TestCompleted destinationFragment = new TestCompleted();
                destinationFragment.setArguments(bundle);
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, destinationFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        }
    }
}
