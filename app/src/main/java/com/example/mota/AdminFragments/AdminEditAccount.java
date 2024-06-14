package com.example.mota.AdminFragments;

import static android.content.ContentValues.TAG;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.mota.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AdminEditAccount extends Fragment implements AdapterView.OnItemSelectedListener {

    EditText name, mobileNumber;
    Spinner genderSpinner, departmentSpinner;
    Button Close, Edit;
    FirebaseFirestore db;
    CollectionReference collectionReference;
    private SharedPreferences sharedPreferences;

    String emailAdmin = "", selectedGenderValue = "", selectedDepartmentValue = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        sharedPreferences = getContext().getSharedPreferences("my_preferences", getContext().MODE_PRIVATE);
        emailAdmin = sharedPreferences.getString("Email", "");
        collectionReference = db.collection("Admin");
    }

    private void initializeSpinners() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.gender, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);
        genderSpinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> departmentAdapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.department, android.R.layout.simple_spinner_item);
        departmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        departmentSpinner.setAdapter(departmentAdapter);
        departmentSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_edit_account, container, false);
        Close = view.findViewById(R.id.close_btn);
        Edit = view.findViewById(R.id.change_btn);
        name = view.findViewById(R.id.edit_text_admin_name);
        mobileNumber = view.findViewById(R.id.edit_text_admin_mobile_number);
        genderSpinner = view.findViewById(R.id.edit_text_admin_gender);
        departmentSpinner = view.findViewById(R.id.edit_text_admin_department);
        getAdminDetails();

        initializeSpinners();
        Edit.setOnClickListener(v -> {
            Map<String, Object> updates = new HashMap<>();
            updates.put("name", "" + name.getText().toString());
            updates.put("mobileNumber", "" + mobileNumber.getText().toString());
            updates.put("gender", "" + "" + selectedGenderValue);
            updates.put("department", "" + "" + selectedDepartmentValue);
            updates.put("modified_timestamp", Timestamp.now());
            collectionReference
                    .document("" + emailAdmin)
                    .update(updates)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot successfully updated!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error updating document", e);
                        }
                    });
            FragmentManager fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, new AdminAccountSetting());
            fragmentTransaction.commit();
        });

        Close.setOnClickListener(v -> {
            FragmentManager fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, new AdminAccountSetting());
            fragmentTransaction.commit();
        });
        return view;
    }

    private void getAdminDetails() {
        collectionReference.document("" + emailAdmin).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    name.setText("" + document.getString("name"));
                    mobileNumber.setText("" + document.getString("mobileNumber"));
                    String[] genders = getResources().getStringArray(R.array.gender);
                    String selectedGender = document.getString("gender");
                    int index = -1;
                    for (int i = 0; i < genders.length; i++) {
                        if (genders[i].equals(selectedGender)) {
                            index = i;
                            break;
                        }
                    }
                    if (index != -1) {
                        genderSpinner.setSelection(index);
                    }
                    String[] departments = getResources().getStringArray(R.array.department);
                    String selectedDepartment = document.getString("department");
                    int index1 = -1;
                    for (int i = 0; i < genders.length; i++) {
                        if (departments[i].equals(selectedDepartment)) {
                            index1 = i;
                            break;
                        }
                    }
                    if (index1 != -1) {
                        departmentSpinner.setSelection(index1);
                    }
                }
            }
        });
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
        Spinner spinner = (Spinner) parent;
        if (spinner.getId() == R.id.edit_text_admin_gender) {
            selectedGenderValue = "" + parent.getItemAtPosition(position).toString();
        } else if (spinner.getId() == R.id.edit_text_admin_department) {
            selectedDepartmentValue = "" + parent.getItemAtPosition(position).toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}