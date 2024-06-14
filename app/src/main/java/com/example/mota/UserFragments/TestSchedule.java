package com.example.mota.UserFragments;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mota.R;
import com.example.mota.UserFragments.TestSchedules.MyAdapter;
import com.example.mota.UserFragments.TestSchedules.TestList;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class TestSchedule extends Fragment implements View.OnClickListener {
    int iteration = 0;
    String RollNo = "";
    private SharedPreferences sharedPreferences;
    private FirebaseFirestore db;
    private static final String TAG = "TestSchedule";
    Button assigned, pastDue, completed;
    RecyclerView recyclerView;
    JSONArray jsonArray;
    List<TestList> items;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_test_schedule, container, false);
        db = FirebaseFirestore.getInstance();
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        sharedPreferences = getContext().getSharedPreferences("my_preferences", getContext().MODE_PRIVATE);
        RollNo = sharedPreferences.getString("RollNo", "");
        items = new ArrayList<>();
        jsonArray = new JSONArray();
        loadDataAndSetupRecyclerView();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        assigned = view.findViewById(R.id.assigned);
        pastDue = view.findViewById(R.id.past_due);
        completed = view.findViewById(R.id.completed);
        assigned.setOnClickListener(this);
        pastDue.setOnClickListener(this);
        completed.setOnClickListener(this);
        assigned.performClick();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.assigned:
                if (iteration == 0) {
                    loadDataAndSetupRecyclerView();
                    iteration++;
                }
                filterItems("Assigned");
                assigned.setBackgroundColor(Color.parseColor("#4EA5D6"));
                pastDue.setBackgroundColor(Color.parseColor("#EAE9E9"));
                completed.setBackgroundColor(Color.parseColor("#EAE9E9"));
                break;
            case R.id.past_due:
                if (iteration == 0) {
                    loadDataAndSetupRecyclerView();
                    iteration++;
                }
                filterItems("Past Due");
                pastDue.setBackgroundColor(Color.parseColor("#FF0000"));
                assigned.setBackgroundColor(Color.parseColor("#EAE9E9"));
                completed.setBackgroundColor(Color.parseColor("#EAE9E9"));
                break;
            case R.id.completed:
                if (iteration == 0) {
                    loadDataAndSetupRecyclerView();
                    iteration++;
                }
                filterItems("Completed");
                completed.setBackgroundColor(Color.parseColor("#4EA5D6"));
                pastDue.setBackgroundColor(Color.parseColor("#EAE9E9"));
                assigned.setBackgroundColor(Color.parseColor("#EAE9E9"));
                break;
        }
    }

    private void loadDataAndSetupRecyclerView() {
        db.collection("Exams").addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (e != null) {
                Log.w(TAG, "Listen failed.", e);
                return;
            }

            items.clear(); // Clear existing data
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                JSONObject jsonObject = new JSONObject();
                if (RollNo.substring(0, 6).equals(document.getId().substring(0, 6))) {
                    String date = new SimpleDateFormat("dd MMMM yyyy,EEEE").format(document.getTimestamp("created_timestamp").toDate());
                    String examName = document.getString("examName");
                    String subjectName = document.getString("instructions");
                    String status = "Assigned";
                    try {
                        jsonObject.put("examID", document.getId());
                        jsonObject.put("created_timestamp", date);
                        jsonObject.put("examName", examName);
                        jsonObject.put("instructions", subjectName);
                        jsonObject.put("status", status);
                        jsonArray.put(jsonObject);
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                }
            }
            onDataLoaded();

            // Handle document removal
            for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                if (dc.getType() == DocumentChange.Type.REMOVED) {
                    String removedId = dc.getDocument().getId();
                    // Remove the exam with the removedId from your local data
                    removeExam(removedId);
                }
            }
        });
    }

    private void removeExam(String removedId) {
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject exam = jsonArray.getJSONObject(i);
                String examID = exam.getString("examID");
                if (examID.equals(removedId)) {
                    jsonArray.remove(i);
                    break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        onDataLoaded();
    }


    private void onDataLoaded() {
        items.clear();
        try {
            jsonArray = sortAndRemoveAssignedExams(jsonArray);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject exam = jsonArray.getJSONObject(i);
                String date = exam.getString("created_timestamp");
                String examName = exam.getString("examName");
                String subjectName = exam.getString("instructions");
                String myStatus = exam.getString("status");
                String examId=exam.getString("examID");
                items.add(new TestList(date, examName, subjectName, myStatus, examId));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Update RecyclerView
        recyclerView.setAdapter(new MyAdapter(getActivity(), items));
    }

    private void filterItems(String status) {
        List<TestList> filteredItems = new ArrayList<>();
        for (TestList item : items) {
            if (item.getStatus().equals(status)) {
                filteredItems.add(item);
            }
        }
        recyclerView.setAdapter(new MyAdapter(getActivity(), filteredItems));
    }

    private static JSONArray sortAndRemoveAssignedExams(JSONArray jsonArray) throws JSONException {
        JSONArray sortedArray = new JSONArray();
        JSONArray unsortedArray = new JSONArray();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject exam = jsonArray.getJSONObject(i);
            String status = exam.getString("status");
            if (status.equals("Completed")) {
                sortedArray.put(exam);
            } else {
                unsortedArray.put(exam);
            }
        }
        for (int i = 0; i < unsortedArray.length(); i++) {
            JSONObject exam = unsortedArray.getJSONObject(i);
            String examID = exam.getString("examID");
            String status = exam.getString("status");
            if (status.equals("Assigned")) {
                boolean hasCompletedExam = false;
                for (int j = 0; j < sortedArray.length(); j++) {
                    JSONObject completedExam = sortedArray.getJSONObject(j);
                    if (completedExam.getString("examID").equals(examID)) {
                        hasCompletedExam = true;
                        break;
                    }
                }
                if (!hasCompletedExam) {
                    sortedArray.put(exam);
                }
            }
        }
        return sortedArray;
    }
}