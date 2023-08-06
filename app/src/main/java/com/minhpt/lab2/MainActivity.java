package com.minhpt.lab2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    EditText edt_id, edt_title, edt_content, edt_date, edt_type;
    Button btn_add;
    RecyclerView rv_list;
    ToDoDAO toDoDAO;
    ToDoAdapter toDoAdapter;
    ArrayList<ToDo> list;
    Context context = this;
    FirebaseFirestore database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edt_id = findViewById(R.id.edt_id);
        edt_title = findViewById(R.id.edt_title);
        edt_content = findViewById(R.id.edt_content);
        edt_date = findViewById(R.id.edt_date);
        edt_type = findViewById(R.id.edt_type);
        btn_add = findViewById(R.id.btn_add);
        rv_list = findViewById(R.id.rv_list);
        database = FirebaseFirestore.getInstance();

        toDoDAO = new ToDoDAO(context);
        list = toDoDAO.getListToDo();
        toDoAdapter = new ToDoAdapter(context, list);
        rv_list.setAdapter(toDoAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rv_list.setLayoutManager(layoutManager);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = edt_title.getText().toString();
                String content = edt_content.getText().toString();
                String date = edt_content.getText().toString();
                String type = edt_type.getText().toString();
                String id = UUID.randomUUID().toString();

                ToDo toDo = new ToDo(id, title, content, date, type, 0);

                HashMap<String, Object> mapTodo = toDo.convertHashMap();

                database.collection("TODO").document().set(mapTodo).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        toDoDAO.addToDo(toDo);
                        list.clear();
                        list = toDoDAO.getListToDo();
                        toDoAdapter = new ToDoAdapter(context, list);
                        rv_list.setAdapter(toDoAdapter);
                        Toast.makeText(context, "Thêm thành công", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        edt_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] mucDoCV = {"Dễ", "Bình thường", "Khó"};
                new AlertDialog.Builder(context).setTitle("Chọn mức độ công việc").setItems(mucDoCV, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        edt_type.setText(mucDoCV[which]);
                    }
                }).show();
            }
        });
    }
}