package com.example.courses;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CourseRVAdapter extends RecyclerView.Adapter<CourseRVAdapter.ViewHolder> {
    private int lastpos = -1;
    private ArrayList<CourseRVModal> courseRVModalArrayList;
    private Context context;
    private CourseClickInterface courseClickInterface;


    public CourseRVAdapter(ArrayList<CourseRVModal> courseRVModalArrayList, Context context, CourseClickInterface courseClickInterface) {
        this.courseRVModalArrayList = courseRVModalArrayList;
        this.context = context;
        this.courseClickInterface = courseClickInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.course_rv_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        CourseRVModal courseRVModal = courseRVModalArrayList.get(position);
        holder.courseNameTV.setText(courseRVModal.getCourseName());
        holder.coursePriceTV.setText("DT. " + courseRVModal.getCoursePrice());

        // Handle null or empty courseImg
        if (courseRVModal.getCourseImg() != null && !courseRVModal.getCourseImg().isEmpty()) {
            Picasso.get().load(courseRVModal.getCourseImg()).into(holder.courseIV);
        } else {
            holder.courseIV.setImageResource(R.drawable.image_placeholder); // use a placeholder image
        }

        setAnimation(holder.itemView, position);
        holder.itemView.setOnClickListener(v -> courseClickInterface.onCourseClick(position));
    }

    private void setAnimation(View itemView, int position) {
        if (position > lastpos) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            itemView.setAnimation(animation);
            lastpos = position;
        }
    }

    @Override
    public int getItemCount() {
        return courseRVModalArrayList.size();
    }

    // ViewHolder class to hold the views
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView courseNameTV, coursePriceTV;
        private ImageView courseIV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            courseNameTV = itemView.findViewById(R.id.idTVCourseName);
            coursePriceTV = itemView.findViewById(R.id.idTVPrice);
            courseIV = itemView.findViewById(R.id.idIVCourse);
        }
    }

    // Interface to handle clicks on RecyclerView items
    public interface CourseClickInterface {
        void onCourseClick(int position);
    }
}
