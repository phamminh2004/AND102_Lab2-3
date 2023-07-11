package com.minhpt.lab2;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ToDoAdapter extends RecyclerView.Adapter<ViewHolder> {
    private Context context;
    private ArrayList<ToDo> list;
    private ToDoDAO toDoDAO;

    public ToDoAdapter(Context context, ArrayList<ToDo> list) {
        this.context = context;
        this.list = list;
        toDoDAO = new ToDoDAO(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv_title.setText(list.get(position).getTitle());
        holder.tv_date.setText(list.get(position).getDate());

        if (list.get(position).getStatus() == 1) {
            holder.cb_status.setChecked(true);
            holder.tv_title.setPaintFlags(holder.tv_title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.cb_status.setChecked(false);
            holder.tv_title.setPaintFlags(holder.tv_title.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = list.get(holder.getAdapterPosition()).getId();
                boolean check = toDoDAO.deleteToDo(id);
                if (check) {
                    Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                    list.clear();
                    list = toDoDAO.getListToDo();
                    notifyItemRemoved(holder.getAdapterPosition());
                } else {
                    Toast.makeText(context, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.cb_status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int id = list.get(holder.getAdapterPosition()).getId();
                boolean check = toDoDAO.updateStatus(id, holder.cb_status.isChecked());
                if (check) {
                    Toast.makeText(context, "Update status thành công", Toast.LENGTH_SHORT).show();
                    list.clear();
                    list = toDoDAO.getListToDo();
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(context, "Update status thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
