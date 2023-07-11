package com.minhpt.lab2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText edt_title, edt_content, edt_date, edt_type;
    Button btn_add;
    RecyclerView rv_list;
    ToDoDAO toDoDAO;
    ToDoAdapter toDoAdapter;
    ArrayList<ToDo> list;
    Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edt_title = findViewById(R.id.edt_title);
        edt_content = findViewById(R.id.edt_content);
        edt_date = findViewById(R.id.edt_date);
        edt_type = findViewById(R.id.edt_type);
        btn_add = findViewById(R.id.btn_add);
        rv_list = findViewById(R.id.rv_list);

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
                ToDo toDo = new ToDo();
                toDo.setId(toDo.getId());
                toDo.setTitle(edt_title.getText().toString());
                toDo.setContent(edt_content.getText().toString());
                toDo.setDate(edt_date.getText().toString());
                toDo.setType(edt_type.getText().toString());
                toDo.setStatus(0);

                toDoDAO.addToDo(toDo);
                list.clear();
                list = toDoDAO.getListToDo();
                toDoAdapter = new ToDoAdapter(context, list);
                rv_list.setAdapter(toDoAdapter);
            }
        });
    }
}