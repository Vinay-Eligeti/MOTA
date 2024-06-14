package com.example.mota.AdminFragments;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mota.AdminFragments.TestScheduleAdmin.MyAdpater;
import com.example.mota.AdminFragments.TestScheduleAdmin.TestList;
import com.example.mota.R;
import com.example.mota.Utility;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.HashMap;
import java.util.Map;

public class AdminTestSchedule extends Fragment implements MyAdpater.OnItemClickListener {

    private SharedPreferences sharedPreferences;
    RecyclerView recyclerView;
    MyAdpater myAdpater;
    Utility utility;
    String examID;
    private FirebaseFirestore db;
    private static final String TAG = "AdminTestSchedule";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_test_schedule, container, false);

        db = FirebaseFirestore.getInstance();

        recyclerView = view.findViewById(R.id.recyclerview1);
        setupRecyclerView();


        Button addUser = view.findViewById(R.id.add_test);
        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getContext() instanceof FragmentActivity) {
                    FragmentManager fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_layout, new ScheduleAdd());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    void setupRecyclerView() {
        Query query = db.collection("Exams").orderBy("created_timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<TestList> options = new FirestoreRecyclerOptions.Builder<TestList>()
                .setQuery(query, TestList.class).build();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        myAdpater = new MyAdpater(options, getContext(), this);
        recyclerView.setAdapter(myAdpater);
    }

    @Override
    public void onStart() {
        super.onStart();
        myAdpater.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        myAdpater.stopListening();
    }

    @Override
    public void onResume() {
        super.onResume();
        myAdpater.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(String examid) {
        examID = examid;
        IntentIntegrator integrator = IntentIntegrator.forSupportFragment(AdminTestSchedule.this);

        integrator.setOrientationLocked(false);
        integrator.setPrompt("");
        integrator.setBeepEnabled(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(getContext(), "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                utility=new Utility();
                String scanned_data = utility.decrypt(result.getContents(),3);
                String rollNo = "" + scanned_data.split(",")[1];
                if (examID.equals("" + scanned_data.split(",")[0])) {
                    ShowDialogBottom(examID, rollNo);
                } else {
                    Toast.makeText(getContext(), "Student and Teacher please click correct exam.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public void ShowDialogBottom(String exam_id, String rollNo) {
        sharedPreferences = getContext().getSharedPreferences("my_preferences", getContext().MODE_PRIVATE);
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheetlayout);

        LinearLayout removeForm = dialog.findViewById(R.id.Remove);
        LinearLayout editForm = dialog.findViewById(R.id.Edit);
        ImageView cancelButton = dialog.findViewById(R.id.cancelButton);
        TextView remove, edit;
        remove = dialog.findViewById(R.id.remove_textview);
        edit = dialog.findViewById(R.id.edit_textview);
        remove.setText("Approve");
        edit.setText("Deny");


        //Approve
        removeForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //add empid
                DocumentReference documentReference=db.collection("Attendance").document(exam_id);
                Map<String, Object> user = new HashMap<>();
                user.put("empid",sharedPreferences.getString("Email",""));
                documentReference.set(user)
                        .addOnSuccessListener(aVoid -> System.out.println("DocumentSnapshot successfully written!"))
                        .addOnFailureListener(e -> System.err.println("Error writing document: " + e));
                DocumentReference documentReference1=documentReference.collection("Students").document(rollNo);
                Map<String, Object> user1 = new HashMap<>();
                user1.put("status",true);
                user1.put("created_timestamp", Timestamp.now());
                documentReference1.set(user1)
                        .addOnSuccessListener(aVoid -> Toast.makeText(getContext(), "Approved Successfully", Toast.LENGTH_LONG).show())
                        .addOnFailureListener(e -> System.err.println("Error writing document: " + e));
            }
        });

        //Deny
        editForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
}