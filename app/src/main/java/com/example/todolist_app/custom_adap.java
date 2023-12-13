package com.example.todolist_app;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class custom_adap extends RecyclerView.Adapter<custom_adap.Viehholder>{

    List<model> tasklast;
//    Context context;
    MainActivity activity;
    DatabaseHandler db;
    public void settasks(List<model> tkls){
        tasklast=tkls;
        notifyDataSetChanged();

    }

    public void edititem(int pos){
        model tp = tasklast.get(pos);
        Bundle bundle = new Bundle();
        bundle.putInt("id",tp.getId());
        bundle.putString("task",tp.getTask());
        AddNewTask frag = new AddNewTask();
        frag.setArguments(bundle);

        frag.show(activity.getSupportFragmentManager(), AddNewTask.TAG);


    }
    public custom_adap(MainActivity activity, List<model> tasklast, DatabaseHandler dbb) {
        this.tasklast = tasklast;
        this.db = dbb;
        this.activity=activity;

    }
    public Context getContext(){
        return activity;
    }
    public void deleteitem(int pos){
        model item = tasklast.get(pos);
        db.deleteTask(item.getId());
        tasklast.remove(pos);
        notifyItemRemoved(pos);
    }

    @NonNull
    @Override
    public Viehholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task,parent,false);
        return new Viehholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viehholder holder, int position) {
        db.openDatabase();
        model task = tasklast.get(position);
        holder.checkBox.setChecked((task.getStatus()==1)? true:false);
        holder.task.setText(task.getTask());
//        Toast.makeText(activity, "hello", Toast.LENGTH_SHORT).show();
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    db.updateStatus(task.getId(),1);

                }else{
                    db.updateStatus(task.getId(),0);

                }
                Toast.makeText(activity, (compoundButton.isChecked())? "Marked as Completed":"Unmarked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return tasklast.size();
    }

    public static class Viehholder extends RecyclerView.ViewHolder{
        public CheckBox checkBox;
        public TextView task;
        public Viehholder(@NonNull View itemView) {
            super(itemView);

            checkBox= (CheckBox) itemView.findViewById(R.id.check);
            task = (TextView) itemView.findViewById(R.id.tasktxt);

        }
    }
}
