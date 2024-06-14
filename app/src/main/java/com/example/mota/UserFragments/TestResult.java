package com.example.mota.UserFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mota.R;
import com.example.mota.UserFragments.TestResults.MyAdapter;
import com.example.mota.UserFragments.TestResults.Result;
import com.example.mota.UserFragments.TestSchedules.TestList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class TestResult extends Fragment {
    String examID="",RollNo = "";
    private FirebaseFirestore db;
    TextView examName,noOfCorrectAnswer,totalAnswer;
    Button closeBtn;
    private static final String TAG = "TestResult";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_test_result, container, false);
        examName=view.findViewById(R.id.exam_name);
        noOfCorrectAnswer=view.findViewById(R.id.no_correct_answer);
        totalAnswer=view.findViewById(R.id.total_answer);
        closeBtn=view.findViewById(R.id.close);
        closeBtn.setOnClickListener(v->{
            UserHome destinationFragment = new UserHome();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, destinationFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });
        if(getArguments().getString("ExamID")!=null){
            examID=getArguments().getString("ExamID");
        }
        if(getArguments().getString("RollNo")!=null){
            RollNo=getArguments().getString("RollNo");
        }
        db= FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Exams").document(examID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        examName.setText(document.getString("examName"));
                    } else {
                        Toast.makeText(getContext(), "No such Exam_name", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getContext(), "" + task.getException(), Toast.LENGTH_LONG).show();
                }
            }
        });
        RecyclerView recyclerView = view.findViewById(R.id.recycler_test_result);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        final List<Result> items = new ArrayList<>();
        db.collection("Exam_Results").document(examID).collection("Students").document(RollNo).collection("Results").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    int i=1;
                    for (QueryDocumentSnapshot document : task.getResult()){
                       String question=document.getString("Question");
                       String chosenAnswer=document.getString("selectedOption");
                       String correctAnswer=document.getString("CorrectAnswer");
                       String checkCorrectOrNot;
                       if(chosenAnswer.equals(correctAnswer)){
                           checkCorrectOrNot="✓";
                       }else{
                           checkCorrectOrNot="✕";
                       }

                        items.add(new Result("Q"+i+". "+question,"Chosen Answer: "+chosenAnswer, ""+checkCorrectOrNot, "Correct Answer: "+correctAnswer));
                        i++;
                    }
                    recyclerView.setAdapter(new MyAdapter(getContext(), items));
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
        db.collection("Exam_Results").document(examID).collection("Students").document(RollNo).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        noOfCorrectAnswer.setText(document.getString("Score"));
                    } else {
                        Toast.makeText(getContext(), "No such Exam_name", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getContext(), "" + task.getException(), Toast.LENGTH_LONG).show();
                }
            }
        });
        CollectionReference collection = db.collection("Exam_Results").document(examID).collection("Students").document(RollNo).collection("Results");
        collection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot != null) {
                        int count = querySnapshot.size();
                        totalAnswer.setText(String.valueOf(count));
                    } else {
                        Toast.makeText(getContext(), "No documents found", Toast.LENGTH_LONG).show();
                    }
                } else {
                    // Handle errors
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();
                }
            }
        });
        // Inflate the layout for this fragment
        return view;
    }
}