package com.example.courses;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class EditCourseActivity extends AppCompatActivity {
    private EditText courseNameEdt, courseDescEdt, coursePriceEdt, courseSuitedForEdt, courseImgEdt;
    private Button updateCourseBtn, deleteCourseBtn;
    private ProgressBar loadingPB;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String courseID;
    private CourseRVModal courseRVModal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);

        // Initialize views
        firebaseDatabase = FirebaseDatabase.getInstance();
        courseNameEdt = findViewById(R.id.idEdtCourseName);
        coursePriceEdt = findViewById(R.id.idEdtCoursePrice);
        courseSuitedForEdt = findViewById(R.id.idEdtCourseSuitedFor);
        courseImgEdt = findViewById(R.id.idEdtCourseImageLink);
        courseDescEdt = findViewById(R.id.idEdtCourseDesc);
        updateCourseBtn = findViewById(R.id.idBtnUpdateCourse);
        deleteCourseBtn = findViewById(R.id.idBtnDeleteCourse);
        loadingPB = findViewById(R.id.idPBLoading);

        // Get the course data passed via Intent
        courseRVModal = getIntent().getParcelableExtra("course");

        // Check if the course data is not null
        if (courseRVModal != null) {
            Log.d("EditCourseActivity", "Course Name: " + courseRVModal.getCourseName());
            Log.d("EditCourseActivity", "Course Price: " + courseRVModal.getCoursePrice());
            Log.d("EditCourseActivity", "Course Suited For: " + courseRVModal.getBestSuitedFor());
            Log.d("EditCourseActivity", "Course Description: " + courseRVModal.getCourseDescription());

            // Set the data to the respective fields
            courseNameEdt.setText(courseRVModal.getCourseName());
            coursePriceEdt.setText(courseRVModal.getCoursePrice());
            courseSuitedForEdt.setText(courseRVModal.getBestSuitedFor());
            courseImgEdt.setText(courseRVModal.getCourseImg());
            courseDescEdt.setText(courseRVModal.getCourseDescription());
            courseID = courseRVModal.getCourseID();
        } else {
            Toast.makeText(this, "Error: Course data not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize Firebase reference
        databaseReference = firebaseDatabase.getReference("Courses").child(courseID);

        // Update course button action
        updateCourseBtn.setOnClickListener(view -> updateCourse());

        // Delete course button action
        deleteCourseBtn.setOnClickListener(view -> deleteCourse());
    }

    private void updateCourse() {
        loadingPB.setVisibility(View.VISIBLE);

        // Get the updated values from the form
        String courseName = courseNameEdt.getText().toString();
        String coursePrice = coursePriceEdt.getText().toString();
        String suitedFor = courseSuitedForEdt.getText().toString();
        String courseImg = courseImgEdt.getText().toString();
        String courseDesc = courseDescEdt.getText().toString();

        // Log the values to ensure correct mapping
        Log.d("EditCourseActivity", "Updated Course Name: " + courseName);
        Log.d("EditCourseActivity", "Updated Course Price: " + coursePrice);
        Log.d("EditCourseActivity", "Updated Suited For: " + suitedFor);
        Log.d("EditCourseActivity", "Updated Course Image: " + courseImg);
        Log.d("EditCourseActivity", "Updated Course Description: " + courseDesc);

        // Create a map to update course data in Firebase
        Map<String, Object> map = new HashMap<>();
        map.put("courseName", courseName);
        map.put("coursePrice", coursePrice);
        map.put("bestSuitedFor", suitedFor);
        map.put("courseImg", courseImg);
        map.put("courseDescription", courseDesc);

        // Update the course data in Firebase
        databaseReference.updateChildren(map).addOnCompleteListener(task -> {
            loadingPB.setVisibility(View.GONE);
            if (task.isSuccessful()) {
                Toast.makeText(EditCourseActivity.this, "Course Updated Successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(EditCourseActivity.this, home.class));
                finish();
            } else {
                Toast.makeText(EditCourseActivity.this, "Failed to Update Course", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteCourse() {
        loadingPB.setVisibility(View.VISIBLE);

        // Delete the course from Firebase
        databaseReference.removeValue().addOnCompleteListener(task -> {
            loadingPB.setVisibility(View.GONE);
            if (task.isSuccessful()) {
                Toast.makeText(this, "Course Deleted Successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(EditCourseActivity.this, home.class));
                finish();
            } else {
                Toast.makeText(this, "Failed to Delete Course", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
