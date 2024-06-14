// LogoutDialogFragment.java

package com.example.mota;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LogoutDialogFragment extends DialogFragment {

    private SharedPreferences sharedPreferences;

    TextView roll_no_textview, name_text_view;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_logout, null);
        roll_no_textview=view.findViewById(R.id.RollNo);
        name_text_view=view.findViewById(R.id.Name);
        sharedPreferences = getContext().getSharedPreferences("my_preferences", getContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(sharedPreferences.getString("Role", "").equals("Student")){
            String rollNo=sharedPreferences.getString("RollNo", "");
            String name=sharedPreferences.getString("Name", "");
            roll_no_textview.setText(""+rollNo);
            name_text_view.setText(""+name);
        }else{
            String email=sharedPreferences.getString("Email", "");
            String name=sharedPreferences.getString("AdminName", "");
            roll_no_textview.setText(""+email);
            name_text_view.setText(""+name);
        }

        // Get buttons from the layout
        Button btnLogout = view.findViewById(R.id.btnLogout);
        Button btnCancel = view.findViewById(R.id.btnCancel);
        btnLogout.setOnClickListener(v-> {
            // Handle logout action
            // You can add your logout logic here
            // dismiss();  // Close the dialog after logout
            editor.putString("Role", "Student");
            editor.putString("RollNo", "");
            editor.putString("Name", "");
            editor.putString("Email", "");
            editor.putString("AdminName", "");
            editor.apply();
            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // User cancelled the dialog
                dismiss();  // Close the dialog after cancel
            }
        });

        builder.setView(view);


        // Create the AlertDialog object and set its gravity to right-top
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setGravity(Gravity.TOP | Gravity.RIGHT);

        // Set the size of the AlertDialog window
        int dialogWidth = getResources().getDimensionPixelSize(R.dimen.dialog_width);
        alertDialog.getWindow().setLayout(dialogWidth, ViewGroup.LayoutParams.WRAP_CONTENT);

        // Set margins to position the dialog at the right-top corner
        int margin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, margin, margin, 0);
        alertDialog.getWindow().getDecorView().setLayoutParams(layoutParams);

        return alertDialog;
    }
}
