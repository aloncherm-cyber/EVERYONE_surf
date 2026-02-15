package com.example.everyone_surf.Adapters;

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




    public interface OnLessonClickListener {
        void onLessonClick(Lesson lesson);
        void onLongLessonClick(Lesson lesson);
    }

    private final List<Lesson> lessonList;
    private final OnLessonClickListener onLessonClickListener;



    public LessonAdapter(List<Lesson> lessonList, OnLessonClickListener onLessonClickListener) {
        this.lessonList = lessonList;
        this.onLessonClickListener = onLessonClickListener;
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
        holder.tvName.setText("מדריך: " + lesson.getInstructor().getFname());
        holder.tvDateTime.setText(lesson.getDate() + " | " + lesson.getTime());
        holder.tvPrice.setText("מחיר: " + lesson.getPrice() + " ש\"ח");
        // כאן תוכל להוסיף לוגיקה לכפתור התקשרות

        holder.itemView.setOnClickListener(v -> {
            if (onLessonClickListener != null) {
                onLessonClickListener.onLessonClick(lesson);
            }
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (onLessonClickListener != null) {
                onLessonClickListener.onLongLessonClick(lesson);
            }
            return true;
        });

    }

    @Override
    public int getItemCount() {
        return lessonList.size();
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