package com.example.courses;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class home extends AppCompatActivity implements CourseRVAdapter.CourseClickInterface {

    private RecyclerView courseRV;
    private ProgressBar loadingPB;
    private FloatingActionButton addFAB;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ArrayList<CourseRVModal> courseRVModalArrayList;
    private RelativeLayout bottomSheetRL;
    private CourseRVAdapter courseRVAdapter;
    private FirebaseAuth mAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize views
        courseRV = findViewById(R.id.idRVCourses);
        loadingPB = findViewById(R.id.idPBLoading);
        addFAB = findViewById(R.id.idAddFAB);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Courses");
        courseRVModalArrayList = new ArrayList<>();
        bottomSheetRL = findViewById(R.id.idRLBSheet);
        mAuth = FirebaseAuth.getInstance();

        // Set up RecyclerView
        courseRVAdapter = new CourseRVAdapter(courseRVModalArrayList, this, this);
        courseRV.setLayoutManager(new LinearLayoutManager(this));
        courseRV.setAdapter(courseRVAdapter);

        // Add new course
        addFAB.setOnClickListener(view -> startActivity(new Intent(home.this, AddCourseActivity.class)));

        // Fetch courses from Firebase
        getAllCourses();
    }

    private void getAllCourses() {
        loadingPB.setVisibility(View.VISIBLE);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                courseRVModalArrayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Fetch course data, ensure no null values
                    String courseName = snapshot.child("courseName").getValue(String.class);
                    String courseDescription = snapshot.child("courseDescription").getValue(String.class);
                    String coursePrice = snapshot.child("coursePrice").getValue(String.class);
                    String bestSuitedFor = snapshot.child("bestSuitedFor").getValue(String.class);
                    String courseImg = snapshot.child("courseImg").getValue(String.class);
                    String courseID = snapshot.child("courseID").getValue(String.class);

                    // Validate the course fields before adding to the list
                    if (courseName != null && !courseName.isEmpty()) {
                        CourseRVModal course = new CourseRVModal(courseName, courseDescription, coursePrice, bestSuitedFor, courseImg, courseID);
                        courseRVModalArrayList.add(course);
                    }
                }
                courseRVAdapter.notifyDataSetChanged();
                loadingPB.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                loadingPB.setVisibility(View.GONE);
                // Show user-friendly error message in case of failure
                Toast.makeText(home.this, "Failed to load courses. Please check your network connection.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCourseClick(int position) {
        // Get the clicked course
        CourseRVModal courseRVModal = courseRVModalArrayList.get(position);

        // Display bottom sheet for more actions
        displayBottomSheet(courseRVModal);
    }

    private void displayBottomSheet(CourseRVModal courseRVModal) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View layout = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_dialog, bottomSheetRL);
        bottomSheetDialog.setContentView(layout);
        bottomSheetDialog.setCancelable(true);
        bottomSheetDialog.show();

        TextView courseNameTV = layout.findViewById(R.id.idTVCourseName);
        TextView courseDescTV = layout.findViewById(R.id.idTVDescription);
        TextView courseSuitedForTV = layout.findViewById(R.id.idTVSuitedFor);
        TextView coursePriceTV = layout.findViewById(R.id.idTVPrice);
        ImageView courseIV = layout.findViewById(R.id.idIVCourse);
        Button editBtn = layout.findViewById(R.id.idBtnEdit);

        // Set values in bottom sheet view
        courseNameTV.setText(courseRVModal.getCourseName());
        courseDescTV.setText(courseRVModal.getCourseDescription());
        courseSuitedForTV.setText(courseRVModal.getBestSuitedFor());
        coursePriceTV.setText("DT. " + courseRVModal.getCoursePrice());

        // Load course image or show placeholder
        if (courseRVModal.getCourseImg() != null && !courseRVModal.getCourseImg().isEmpty()) {
            Picasso.get().load(courseRVModal.getCourseImg()).into(courseIV);
        } else {
            courseIV.setImageResource(R.drawable.image_placeholder); // Set a default image
        }

        // Edit button click to go to EditCourseActivity
        editBtn.setOnClickListener(view -> {
            Intent intent = new Intent(home.this, EditCourseActivity.class);
            intent.putExtra("course", courseRVModal); // Pass course data to EditCourseActivity
            startActivity(intent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            mAuth.signOut();
            Toast.makeText(this, "Logged out successfully!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(home.this, login.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}