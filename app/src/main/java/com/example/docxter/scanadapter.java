package com.example.docxter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class scanadapter extends RecyclerView.Adapter<scanadapter.scanviewholder>{
    Cursor mcursor;
    Context mcontext;
    public scanadapter(Context context,Cursor cursor)
    {
     mcursor=cursor;
     mcontext=context;
    }
    public class scanviewholder extends RecyclerView.ViewHolder
    {
        TextView mtextview;
        public scanviewholder(@NonNull View itemView) {
            super(itemView);
            mtextview=itemView.findViewById(R.id.individualScan);
        }
    }

    @NonNull
    @Override
    public scanviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(mcontext);
        View view=inflater.inflate(R.layout.singlehistoryitem,parent,false);
        return new scanviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull scanviewholder holder, int position) {
        if(!mcursor.moveToPosition(position)){
            return;
        }
        String name=mcursor.getString(mcursor.getColumnIndex(detailtable.detailtableinner.Column_Name));
        long id=mcursor.getLong(mcursor.getColumnIndex(detailtable.detailtableinner._ID));
        holder.mtextview.setText(name);
        holder.itemView.setTag(id);
    }

    @Override
    public int getItemCount() {
        return mcursor.getCount();
    }
    public void swap(Cursor newcursor)
    {
        if (mcursor!=null)
        {
            mcursor.close();
        }
        mcursor=newcursor;
        if(newcursor!=null)
        {
            notifyDataSetChanged();
        }
    }
}
