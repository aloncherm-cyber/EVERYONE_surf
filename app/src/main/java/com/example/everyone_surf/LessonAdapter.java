package com.example.everyone_surf; // שים לב ל-A גדולה אם ככה קראת לתיקייה

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.everyone_surf.R;
import com.example.everyone_surf.model.Lesson;
import java.util.List;

public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.LessonViewHolder> {

    private List<Lesson> lessonList;

    public LessonAdapter(List<Lesson> lessonList) {
        this.lessonList = lessonList;
    }

    @NonNull
    @Override
    public LessonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lesson, parent, false);
        return new LessonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LessonViewHolder holder, int position) {
        Lesson lesson = lessonList.get(position);
        holder.tvName.setText("מדריך: " + lesson.getInstructorName());
        holder.tvDateTime.setText(lesson.getDate() + " | " + lesson.getTime());
        holder.tvPrice.setText("מחיר: " + lesson.getPrice() + " ש\"ח");
    }

    @Override
    public int getItemCount() {
        return lessonList != null ? lessonList.size() : 0;
    }

    public static class LessonViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDateTime, tvPrice;
        public LessonViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvInstructorName);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
            tvPrice = itemView.findViewById(R.id.tvPrice);
        }
    }
}