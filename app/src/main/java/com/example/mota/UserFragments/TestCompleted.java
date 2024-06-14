package com.example.mota.UserFragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.mota.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestCompleted extends Fragment {

    private FirebaseFirestore db;
    private JSONArray jsonArray;
    private SharedPreferences sharedPreferences;
    JSONObject currentQuestion;
    private int currentQuestionIndex;
    TextView exam_name, question, clearSelection;
    RadioButton option1, option2, option3, option4;
    RadioGroup radioGroup;
    Button nextBtn, previousBtn, submitBtn, viewResultBtn;
    //score
    int score = 0, iteration = 0;
    ;
    String examID = "", examName, instruction, RollNo = "";

    boolean submited = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_test_completed, container, false);
        db = FirebaseFirestore.getInstance();
        nextBtn = view.findViewById(R.id.next_btn);
        previousBtn = view.findViewById(R.id.previous_btn);
        submitBtn = view.findViewById(R.id.submit_btn);
        viewResultBtn = view.findViewById(R.id.view_result);
        radioGroup = view.findViewById(R.id.radio_group);
        option1 = view.findViewById(R.id.radioButton);
        option2 = view.findViewById(R.id.radioButton2);
        option3 = view.findViewById(R.id.radioButton3);
        option4 = view.findViewById(R.id.radioButton4);
        exam_name = view.findViewById(R.id.exam_name);
        question = view.findViewById(R.id.textView3);
        clearSelection = view.findViewById(R.id.clear_selection);
        sharedPreferences = getContext().getSharedPreferences("my_preferences", getContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        RollNo = sharedPreferences.getString("RollNo", "");
        jsonArray = new JSONArray();
        if (getArguments().getString("examID") != null) {
            String scannedData = getArguments().getString("examID");
            examID = scannedData;
        }

        while (!ScreenPinningUtil.isScreenPinningEnabled(getActivity())) {
            try {
                if (iteration == 4) {
                    Intent startMain = new Intent(Intent.ACTION_MAIN);
                    startMain.addCategory(Intent.CATEGORY_HOME);
                    startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(startMain);
                } else {
                    ScreenPinningUtil.startScreenPinning(getActivity());
                    Thread.sleep(2000);
                }
                iteration++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        db.collection("Exams").document(examID).collection("Questions")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("Question", documentSnapshot.getString("Question"));
                            jsonObject.put("CorrectAnswer", documentSnapshot.getString("CorrectAnswer"));
                            List<String> optionsList = (List<String>) documentSnapshot.get("Options");
                            JSONArray optionsArray = new JSONArray(optionsList);
                            jsonObject.put("Options", optionsArray);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            nothingForExam("There is some Technical issue");
                        }
                        jsonArray.put(jsonObject);
                    }
                    // Display the first question
                    showQuestion(0);
                    if (jsonArray.length() == 0) {
                        nothingForExam("There is nothing to show???");
                    } else if (jsonArray.length() == 1) {
                        previousBtn.setVisibility(View.VISIBLE);
                        nextBtn.setVisibility(View.GONE);
                        submitBtn.setVisibility(View.VISIBLE);
                    }
                });
        DocumentReference docRef = db.collection("Exams").document(examID);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        examName = document.getString("examName");
                        instruction = document.getString("instructions");
                        exam_name.setText(examName);
                    } else {
                        Toast.makeText(getContext(), "No such Exam_name", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getContext(), "" + task.getException(), Toast.LENGTH_LONG).show();
                }
            }
        });

        viewResultBtn.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("ExamID", examID);
            bundle.putString("RollNo", RollNo);
            TestResult destinationFragment = new TestResult();
            destinationFragment.setArguments(bundle);
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, destinationFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        option1.setOnClickListener(view1 -> {
            clearSelection.setText("Clear Selection");
        });
        option2.setOnClickListener(view1 -> {
            clearSelection.setText("Clear Selection");
        });
        option3.setOnClickListener(view1 -> {
            clearSelection.setText("Clear Selection");
        });
        option4.setOnClickListener(view1 -> {
            clearSelection.setText("Clear Selection");
        });
        clearSelection.setOnClickListener(v -> {
            radioGroup.clearCheck();
            try {
                currentQuestion = jsonArray.getJSONObject(currentQuestionIndex);
                currentQuestion.put("selectedOption", "");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            clearSelection.setText("");
        });
        nextBtn.setOnClickListener(v -> {
            clearSelection.setText("");
            String selectedOption = "";
            if (option1.isChecked()) {
                selectedOption = option1.getText().toString();
            } else if (option2.isChecked()) {
                selectedOption = option2.getText().toString();
            } else if (option3.isChecked()) {
                selectedOption = option3.getText().toString();
            } else if (option4.isChecked()) {
                selectedOption = option4.getText().toString();
            }
            if (selectedOption != "") {
                selectedOption = selectedOption.substring(3);
            }
            try {
                // Get the current question's correct answer from the jsonArray
                currentQuestion = jsonArray.getJSONObject(currentQuestionIndex);
                String correctAnswer = currentQuestion.getString("CorrectAnswer");
                currentQuestion.put("selectedOption", selectedOption);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                currentQuestion = jsonArray.getJSONObject(currentQuestionIndex);
                String selectedOption1 = currentQuestion.getString("selectedOption");
                JSONArray optionsArray = currentQuestion.getJSONArray("Options");
                for (int i = 0; i < optionsArray.length(); i++) {
                    if (selectedOption1.equals(optionsArray.getString(i))) {
                        int number = i + 1;
                        String optionName = "option" + number;
                        if ("option1".equals(optionName)) {
                            option1.setChecked(true);
                        } else if ("option2".equals(optionName)) {
                            option2.setChecked(true);
                        } else if ("option3".equals(optionName)) {
                            option3.setChecked(true);
                        } else if ("option4".equals(optionName)) {
                            option4.setChecked(true);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (currentQuestionIndex < jsonArray.length() - 1) {
                currentQuestionIndex++;
                showQuestion(currentQuestionIndex);
            }
            try {
                currentQuestion = jsonArray.getJSONObject(currentQuestionIndex);
                String selectedOption1 = currentQuestion.getString("selectedOption");
                JSONArray optionsArray = currentQuestion.getJSONArray("Options");
                for (int i = 0; i < optionsArray.length(); i++) {
                    if (selectedOption1.equals(optionsArray.getString(i))) {
                        int number = i + 1;
                        String optionName = "option" + number;
                        if ("option1".equals(optionName)) {
                            option1.setChecked(true);
                        } else if ("option2".equals(optionName)) {
                            option2.setChecked(true);
                        } else if ("option3".equals(optionName)) {
                            option3.setChecked(true);
                        } else if ("option4".equals(optionName)) {
                            option4.setChecked(true);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        previousBtn.setOnClickListener(v -> {
            clearSelection.setText("");
            String selectedOption = "";
            if (option1.isChecked()) {
                selectedOption = option1.getText().toString();
            } else if (option2.isChecked()) {
                selectedOption = option2.getText().toString();
            } else if (option3.isChecked()) {
                selectedOption = option3.getText().toString();
            } else if (option4.isChecked()) {
                selectedOption = option4.getText().toString();
            }
            if (selectedOption != "") {
                selectedOption = selectedOption.substring(3);
            }
            try {
                // Get the current question's correct answer from the jsonArray
                currentQuestion = jsonArray.getJSONObject(currentQuestionIndex);
                String correctAnswer = currentQuestion.getString("CorrectAnswer");
                currentQuestion.put("selectedOption", selectedOption);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                currentQuestion = jsonArray.getJSONObject(currentQuestionIndex);
                String selectedOption1 = currentQuestion.getString("selectedOption");
                JSONArray optionsArray = currentQuestion.getJSONArray("Options");
                for (int i = 0; i < optionsArray.length(); i++) {
                    if (selectedOption1.equals(optionsArray.getString(i))) {
                        int number = i + 1;
                        String optionName = "option" + number;
                        if ("option1".equals(optionName)) {
                            option1.setChecked(true);
                        } else if ("option2".equals(optionName)) {
                            option2.setChecked(true);
                        } else if ("option3".equals(optionName)) {
                            option3.setChecked(true);
                        } else if ("option4".equals(optionName)) {
                            option4.setChecked(true);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (currentQuestionIndex > 0) {
                currentQuestionIndex--;
                showQuestion(currentQuestionIndex);
            }
            try {
                currentQuestion = jsonArray.getJSONObject(currentQuestionIndex);
                String selectedOption1 = currentQuestion.getString("selectedOption");
                JSONArray optionsArray = currentQuestion.getJSONArray("Options");
                for (int i = 0; i < optionsArray.length(); i++) {
                    if (selectedOption1.equals(optionsArray.getString(i))) {
                        int number = i + 1;
                        String optionName = "option" + number;
                        if ("option1".equals(optionName)) {
                            option1.setChecked(true);
                        } else if ("option2".equals(optionName)) {
                            option2.setChecked(true);
                        } else if ("option3".equals(optionName)) {
                            option3.setChecked(true);
                        } else if ("option4".equals(optionName)) {
                            option4.setChecked(true);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
        submitBtn.setOnClickListener(v -> {
            submited = true;
            ScreenPinningUtil.stopScreenPinning(getActivity());
            clearSelection.setText("");
            String selectedOption = "";
            if (option1.isChecked()) {
                selectedOption = option1.getText().toString();
            } else if (option2.isChecked()) {
                selectedOption = option2.getText().toString();
            } else if (option3.isChecked()) {
                selectedOption = option3.getText().toString();
            } else if (option4.isChecked()) {
                selectedOption = option4.getText().toString();
            }
            if (selectedOption != "") {
                selectedOption = selectedOption.substring(3);
            }
            try {
                // Get the current question's correct answer from the jsonArray
                currentQuestion = jsonArray.getJSONObject(currentQuestionIndex);
                currentQuestion.put("selectedOption", selectedOption);
                for (int i = 0; i <= jsonArray.length() - 1; i++) {
                    currentQuestion = jsonArray.getJSONObject(i);
                    if (currentQuestion.getString("CorrectAnswer").equals(currentQuestion.getString("selectedOption"))) {
                        score = score + 1;
                    }
                }
                // Check if the selected option matches the correct answer
            } catch (JSONException e) {
                e.printStackTrace();
            }
            CollectionReference resultsCollection = db.collection("Exam_Results").document(examID)
                    .collection("Students").document(RollNo).collection("Results");
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String question = jsonObject.getString("Question");
                    String selectedOption1 = jsonObject.getString("selectedOption");
                    String correctAnswer = jsonObject.getString("CorrectAnswer");
                    JSONArray optionsArray = jsonObject.getJSONArray("Options");
                    List<String> Options;
                    Gson gson = new Gson();
                    Options = gson.fromJson(optionsArray.toString(), List.class);
                    Map<String, Object> data = new HashMap<>();
                    data.put("Question", question);
                    data.put("selectedOption", selectedOption1);
                    data.put("CorrectAnswer", correctAnswer);
                    data.put("Options", Options);
                    DocumentReference documentReference = resultsCollection.document("" + i);
                    documentReference.set(data);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            //Adding score
            DocumentReference documentReference = db.collection("Exam_Results").document(examID)
                    .collection("Students").document(RollNo);

            Map<String, Object> data = new HashMap<>();
            data.put("Score", "" + score);
            data.put("created_timestamp", FieldValue.serverTimestamp());
            documentReference.set(data);

            Map<String, Object> data2 = new HashMap<>();
            data2.put("examName", "" + examName);
            data2.put("instructions", "" + instruction);
            db.collection("Exam_Results").document(examID).set(data2);
            nothingForExam("You have Completed Your Exam Successfully!!");
        });

        return view;
    }

    private void nothingForExam(String message) {
        if (message.equals("You have Completed Your Exam Successfully!!")) {
            exam_name.setText(message);
            question.setVisibility(View.GONE);
            option1.setVisibility(View.GONE);
            option2.setVisibility(View.GONE);
            option3.setVisibility(View.GONE);
            option4.setVisibility(View.GONE);
            nextBtn.setVisibility(View.GONE);
            previousBtn.setVisibility(View.GONE);
            submitBtn.setVisibility(View.GONE);
            viewResultBtn.setVisibility(View.VISIBLE);

        } else if (message.equals("Please Enable the Screen Pinning")) {
            exam_name.setText(message);
            question.setVisibility(View.GONE);
            option1.setVisibility(View.GONE);
            option2.setVisibility(View.GONE);
            option3.setVisibility(View.GONE);
            option4.setVisibility(View.GONE);
            nextBtn.setVisibility(View.GONE);
            previousBtn.setVisibility(View.GONE);
            submitBtn.setVisibility(View.GONE);
            viewResultBtn.setVisibility(View.VISIBLE);
            viewResultBtn.setText("Back");
            viewResultBtn.setOnClickListener(v -> {
                UserHome destinationFragment = new UserHome();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, destinationFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            });
        }

    }

    private void showQuestion(int index) {
        try {
            if (index == 0) {
                previousBtn.setVisibility(View.INVISIBLE);
                submitBtn.setVisibility(View.GONE);
                nextBtn.setVisibility(View.VISIBLE);
            } else if (index < jsonArray.length() - 1) {
                previousBtn.setVisibility(View.VISIBLE);
                submitBtn.setVisibility(View.GONE);
                nextBtn.setVisibility(View.VISIBLE);
            } else if (index == jsonArray.length() - 1) {
                previousBtn.setVisibility(View.VISIBLE);
                nextBtn.setVisibility(View.GONE);
                submitBtn.setVisibility(View.VISIBLE);
            }
            JSONObject questionObject = jsonArray.getJSONObject(index);
            String question1 = questionObject.getString("Question");
            String correctAnswer = questionObject.getString("CorrectAnswer");
            JSONArray optionsArray = questionObject.getJSONArray("Options");
            String optionA = optionsArray.getString(0);
            String optionB = optionsArray.getString(1);
            String optionC = optionsArray.getString(2);
            String optionD = optionsArray.getString(3);

            // Update the UI with the question and options
            int questionNumber = index + 1;
            question.setText("Q" + questionNumber + ". " + question1);
            option1.setText("1. " + optionA);
            option2.setText("2. " + optionB);
            option3.setText("3. " + optionC);
            option4.setText("4. " + optionD);


            // Clear previous selection
            radioGroup.clearCheck();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!submited) {
            while (!ScreenPinningUtil.isScreenPinningEnabled(getActivity())) {
                try {
                    if (iteration == 4) {
                        Intent startMain = new Intent(Intent.ACTION_MAIN);
                        startMain.addCategory(Intent.CATEGORY_HOME);
                        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(startMain);
                    } else {
                        ScreenPinningUtil.startScreenPinning(getActivity());
                        Thread.sleep(2000);
                    }
                    iteration++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}