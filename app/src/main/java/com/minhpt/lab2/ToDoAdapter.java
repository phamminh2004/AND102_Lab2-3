package com.minhpt.lab2;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Cảnh báo");
                builder.setIcon(R.drawable.ic_warning);
                builder.setMessage("Bạn có chắc chắn muốn xóa không?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

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
                }).setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
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
        holder.btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToDo toDo = list.get(holder.getAdapterPosition());
                DialogUpdate(toDo);
            }
        });
    }

    public void DialogUpdate(ToDo toDo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.update, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        EditText edtTitle = view.findViewById(R.id.edtTitle);
        EditText edtContent = view.findViewById(R.id.edtContent);
        EditText edtDate = view.findViewById(R.id.edtDate);
        EditText edtType = view.findViewById(R.id.edtType);
        Button btnUpdate = view.findViewById(R.id.btnUpdate);
        Button btnCancel = view.findViewById(R.id.btnCancel);
        edtTitle.setText(toDo.getTitle());
        edtContent.setText(toDo.getContent());
        edtDate.setText(toDo.getDate());
        edtType.setText(toDo.getType());
        edtType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] mucDoCV = {"Dễ", "Bình thường", "Khó"};
                new android.app.AlertDialog.Builder(context).setTitle("Chọn mức độ công việc").setItems(mucDoCV, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        edtType.setText(mucDoCV[which]);
                    }
                }).show();
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = edtTitle.getText().toString();
                String content = edtContent.getText().toString();
                String date = edtDate.getText().toString();
                String type = edtType.getText().toString();
                ToDo toDo1 = new ToDo(toDo.getId(), title, content, date, type, toDo.getStatus());
                boolean check = toDoDAO.updateToDo(toDo1);
                if (check) {
                    list.clear();
                    list = toDoDAO.getListToDo();
                    notifyDataSetChanged();
                    dialog.dismiss();
                    Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
