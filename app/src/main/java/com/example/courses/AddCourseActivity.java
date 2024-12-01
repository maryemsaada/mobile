package com.example.courses;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddCourseActivity extends AppCompatActivity {
    private EditText courseNameEdt, courseDescEdt, coursePriceEdt, courseSuitedForEdt, courseImgEdt;
    private Button addCourseBtn;
    private ProgressBar loadingPB;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String courseID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_course);

        courseNameEdt = findViewById(R.id.idEdtCourseName);
        coursePriceEdt = findViewById(R.id.idEdtCoursePrice);
        courseSuitedForEdt = findViewById(R.id.idEdtCourseSuitedFor);
        courseImgEdt = findViewById(R.id.idEdtCourseImageLink);
        courseDescEdt = findViewById(R.id.idEdtCourseDesc);
        addCourseBtn = findViewById(R.id.idBtnAddCourse);
        loadingPB = findViewById(R.id.idPBLoading);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Courses");

        addCourseBtn.setOnClickListener(v -> {
            loadingPB.setVisibility(View.VISIBLE);

            String courseName = courseNameEdt.getText().toString();
            String coursePrice = coursePriceEdt.getText().toString();
            String suitedFor = courseSuitedForEdt.getText().toString();
            String courseImg = courseImgEdt.getText().toString();
            String courseDesc = courseDescEdt.getText().toString();

            courseID = courseName;
            CourseRVModal courseRVModal = new CourseRVModal(courseName, courseDesc, coursePrice, suitedFor, courseImg, courseID);

            databaseReference.child(courseID).setValue(courseRVModal).addOnCompleteListener(task -> {
                loadingPB.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    Toast.makeText(AddCourseActivity.this, "Course Added Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AddCourseActivity.this, home.class));
                } else {
                    Toast.makeText(AddCourseActivity.this, "Failed to Add Course", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
