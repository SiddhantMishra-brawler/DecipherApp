package com.example.docxter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

public class Scanhistory extends AppCompatActivity {
    String scanResult="";
    SharedPreferences mpref;
    private scanadapter scanadapter;
    private SQLiteDatabase mdatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanhistory);
        mpref=getApplicationContext().getSharedPreferences("mypref",MODE_PRIVATE);
        RecyclerView recyclerView=findViewById(R.id.myscanhistory);
        dbhelper mdbhelper=new dbhelper(getApplicationContext());
        mdatabase=mdbhelper.getWritableDatabase();
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        scanadapter=new scanadapter(Scanhistory.this,getallitems());
        recyclerView.setAdapter(scanadapter);
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                removeItem((long)viewHolder.itemView.getTag());
            }
        }).attachToRecyclerView(recyclerView);
        scanResult=mpref.getString("result","");
        if(scanResult!=null)
        {
            add(scanResult);
        }
    }

    private void add(String scanResult) {
        if(scanResult.trim().length()==0)
        {
            return;
        }
        ContentValues cv=new ContentValues();
        cv.put(detailtable.detailtableinner.Column_Name,scanResult);
        mdatabase.insert(detailtable.detailtableinner.Table_Name,null,cv);
        scanadapter.swap(getallitems());
        mpref.edit().remove("result").commit();
    }
    private void removeItem(long id)
    {
        mdatabase.delete(detailtable.detailtableinner.Table_Name,
                detailtable.detailtableinner._ID+"="+id,null);
        scanadapter.swap(getallitems());
    }
    private Cursor getallitems(){
        return mdatabase.query(
                detailtable.detailtableinner.Table_Name,
                null,
                null,
                null,
                null,
                null,
                detailtable.detailtableinner.timestamp+" DESC"
        );
    }
}
