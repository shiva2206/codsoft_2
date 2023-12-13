package com.example.todolist_app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class RecylcerItemTouchHelper extends ItemTouchHelper.SimpleCallback {
    private custom_adap adp;
    public RecylcerItemTouchHelper(custom_adap adp) {
        super(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT);
        this.adp = adp;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        final int pos = viewHolder.getAdapterPosition();
        if(direction==ItemTouchHelper.LEFT){
            AlertDialog.Builder builder = new AlertDialog.Builder(adp.getContext());
            builder.setTitle("Delete Task");
            builder.setMessage("Are You Sure You Want To delete This Task");
            builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                        adp.deleteitem(pos);
                }
            });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    adp.notifyItemChanged(viewHolder.getAdapterPosition());
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else{
            adp.edititem(pos);
        }

    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        Drawable icon;
        ColorDrawable background;

        View itemview = viewHolder.itemView;
        int backoff = 20;
        if(dX>0){
            icon = ContextCompat.getDrawable(adp.getContext(),R.drawable.baseline_edit_24);
            background = new ColorDrawable(ContextCompat.getColor(adp.getContext(), com.google.android.material.R.color.design_default_color_primary));
        }else{
            icon = ContextCompat.getDrawable(adp.getContext(),R.drawable.baseline_delete_24);
            background = new ColorDrawable(Color.RED);
        }

        int iconmar = (itemview.getHeight() - icon.getIntrinsicHeight())/2;
        int icontop = itemview.getTop() + (itemview.getHeight() -icon.getIntrinsicHeight());
        int iconbot = icontop + icon.getIntrinsicHeight();

        if(dX>0){
            int iconleft = itemview.getLeft()+iconmar;
            int iconrigh = itemview.getLeft() + iconmar+ background.getIntrinsicWidth();
            icon.setBounds(iconleft,icontop,iconrigh,iconbot);

            background.setBounds(itemview.getLeft(),itemview.getTop(),itemview.getLeft()+((int)dX) + backoff ,itemview.getBottom());

        }else if(dX<0){
            int iconleft = itemview.getRight()-iconmar- background.getIntrinsicWidth();
            int iconrigh = itemview.getRight()  - iconmar ;
            icon.setBounds(iconleft,icontop,iconrigh,iconbot);

            background.setBounds(itemview.getRight() +((int)(dX))-backoff,itemview.getTop(),itemview.getRight() ,itemview.getBottom());

        }else{
            background.setBounds(0,0,0,0);
        }
        background.draw(c);
        icon.draw(c);
    }

}