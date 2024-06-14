package com.example.mota.AdminFragments.TestScheduleAdmin;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mota.AdminFragments.ScheduleEdit;
import com.example.mota.AdminFragments.ScheduleRemove;
import com.example.mota.R;
import com.example.mota.Utility;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class MyAdpater extends FirestoreRecyclerAdapter<TestList, MyAdpater.TestViewHolder> {

    private SharedPreferences sharedPreferences;
    Context context;
    private OnItemClickListener listener;


    public MyAdpater(@NonNull FirestoreRecyclerOptions<TestList> options, Context context,OnItemClickListener listener) {
        super(options);
        this.context = context;
        this.listener=listener;
    }

    @Override
    protected void onBindViewHolder(@NonNull TestViewHolder holder, int position, @NonNull TestList testList) {
        holder.timeStampTextView.setText(Utility.timeStampToString(testList.created_timestamp));
        holder.examNameTextView.setText(testList.examName);
        holder.instructionTextView.setText(testList.instructions);
        holder.itemView.setOnClickListener(v->{
            String examID=this.getSnapshots().getSnapshot(position).getId();
            listener.onItemClick(examID);
        });
        holder.optionButton.setOnClickListener(v->{
            String examName=holder.examNameTextView.getText().toString();
            String examID=this.getSnapshots().getSnapshot(position).getId();
            sharedPreferences=context.getSharedPreferences("my_preferences", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("examID", examID);
            editor.putString("examName", examName);
            editor.apply();
            ShowDialogBottom();
        });
    }
    public interface OnItemClickListener {
        void onItemClick(String examID);
    }


    @NonNull
    @Override
    public TestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_admin_schedule, parent, false);
        return new TestViewHolder(view);
    }

    class TestViewHolder extends RecyclerView.ViewHolder {
        TextView examNameTextView, instructionTextView, timeStampTextView;
        Button optionButton;

        public TestViewHolder(@NonNull View itemView) {
            super(itemView);
            examNameTextView = itemView.findViewById(R.id.Exam_Name);
            instructionTextView = itemView.findViewById(R.id.instruction);
            timeStampTextView = itemView.findViewById(R.id.created_timestamp);
            optionButton=itemView.findViewById(R.id.Edit_Button);
        }
    }


    public void ShowDialogBottom() {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheetlayout);

        LinearLayout removeForm = dialog.findViewById(R.id.Remove);
        LinearLayout editForm = dialog.findViewById(R.id.Edit);
        ImageView cancelButton = dialog.findViewById(R.id.cancelButton);
        TextView remove, edit;
        remove = dialog.findViewById(R.id.remove_textview);
        edit = dialog.findViewById(R.id.edit_textview);
        remove.setText("Remove Form");
        edit.setText("Edit Form");

        removeForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (context instanceof FragmentActivity) {
                    FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_layout, new ScheduleRemove());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            }
        });

        editForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (context instanceof FragmentActivity) {
                    FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frame_layout, new ScheduleEdit());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
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
