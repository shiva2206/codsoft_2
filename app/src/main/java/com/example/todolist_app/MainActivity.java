package com.example.todolist_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.todolist_app.AddNewTask;
import com.example.todolist_app.DatabaseHandler;
import com.example.todolist_app.DialogCloseListener;
import com.example.todolist_app.R;
import com.example.todolist_app.RecylcerItemTouchHelper;
import com.example.todolist_app.custom_adap;
import com.example.todolist_app.model;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DialogCloseListener {
    public RecyclerView taskrecy;
    public FloatingActionButton flo;
    public custom_adap adap;
    public List<model> tasklst;
    public DatabaseHandler db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db=new DatabaseHandler(this);

        db.openDatabase();

        tasklst=db.getALltasks();
        taskrecy = findViewById(R.id.recy);
        flo = (FloatingActionButton) findViewById(R.id.fab);
        taskrecy.setLayoutManager(new LinearLayoutManager(this));
        adap=new custom_adap(MainActivity.this,tasklst,db);
        taskrecy.setAdapter(adap);


        Collections.reverse(tasklst);
        adap.notifyDataSetChanged();

        flo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewTask.newInstance().show(getSupportFragmentManager(),AddNewTask.TAG);
//                Toast.makeText(MainActivity.this, tasklst.size()+"", Toast.LENGTH_SHORT).show();
            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecylcerItemTouchHelper(adap));
        itemTouchHelper.attachToRecyclerView(taskrecy);

    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        tasklst = db.getALltasks();
        Collections.reverse(tasklst);
        adap.settasks(tasklst);
        adap.notifyDataSetChanged();

    }
}