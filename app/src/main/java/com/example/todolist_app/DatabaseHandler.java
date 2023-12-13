package com.example.todolist_app;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    public static final int VERSION=1;
    public static final String NAME = "todolistdatabase";
    public static final String TODO_TABLE = "todo";
    public static final String ID = "id";
    public static final String STATUS = "status";
    public static final String TASK="task";
    public static final String CREATE_TODO_TABLE = "CREATE TABLE " +TODO_TABLE+"("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+TASK+" TEXT, "+STATUS+" INTEGER);";
    private SQLiteDatabase db;

    public DatabaseHandler(@Nullable Context context) {
        super(context, NAME, null, VERSION);
    }





    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TODO_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+ TODO_TABLE);
        onCreate(db);
    }
    public void  openDatabase(){db=this.getWritableDatabase();}

    public void insertTask(model task){
        ContentValues cv = new ContentValues();
        cv.put(TASK,task.getTask());
        cv.put(STATUS,0);
        db.insert(TODO_TABLE,null,cv);

    }

    @SuppressLint("Range")
    public List<model> getALltasks(){
        List<model> taklst = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        try{
            cur=db.query(TODO_TABLE,null,null,null,null,null,null);
            if(cur!=null){
                if(cur.moveToFirst()){
                    do{
                        model tk = new model();
                        tk.setId(cur.getInt(cur.getColumnIndex(ID)));
                        tk.setTask(cur.getString(cur.getColumnIndex(TASK)));
                        tk.setStatus((cur.getInt(cur.getColumnIndex(STATUS))));
                        taklst.add(tk);
                    }while (cur.moveToNext());
                }
            }
        }finally {
            db.endTransaction();
            cur.close();
        }
        return taklst;
    }
    public void updateStatus(int id,int stat){
        ContentValues cv = new ContentValues();
        cv.put(STATUS,stat);
        db.update(TODO_TABLE,cv,ID+"=?",new String[]{String.valueOf(id)});
    }
    public void updateTask(int id,String tak){
        ContentValues cv = new ContentValues();
        cv.put(TASK,tak);
        db.update(TODO_TABLE,cv,ID+"=?",new String[]{String.valueOf(id)});

    }
    public void deleteTask(int id){
        db.delete(TODO_TABLE,ID+"=?",new String[]{String.valueOf(id)});
    }
}
