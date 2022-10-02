package com.example.notes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class itemAdapter extends RecyclerView.Adapter<itemAdapter.itemHolder> {

//    Context context;
    private List<Note> notes=new ArrayList<>();
    private onItemClickListener listener;

//    private ArrayList<String> title;
//    private ArrayList<String> data;
//    private itemAdapterEvents itemAdapterEvents;



//    public itemAdapter(Context context, ArrayList<String> title, ArrayList<String> data,itemAdapterEvents itemAdapterEvents) {
//        this.itemAdapterEvents = itemAdapterEvents;
//        this.context = context;
//        this.title = title;
//        this.data = data;
//    }

    @NonNull
    @Override
    public itemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.items_view,parent,false);
        return new itemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull itemHolder holder, int position) {
        Note currentNote=notes.get(position);
        holder.titleView.setText(currentNote.getTitle());
        holder.dataView.setText(currentNote.getDescription());
//        Integer pos=position;
//        String Title=title.get(pos);
//        String Data=data.get(pos);
//        if(Title.isEmpty()) {
//            holder.titleView.setText("No Title");
//            holder.dataView.setText(Data);
//        }
//        else {
//            holder.titleView.setText(Title);
//            holder.dataView.setText(Data);
//        }
////        holder.titleView.setText(Title);

    }

    @Override
    public int getItemCount() {
        return notes.size();//title.size();
    }

    public void setNotes(List<Note> notes){
        this.notes=notes;
        notifyDataSetChanged();
    }

    public Note getNotes(int position){
        return notes.get(position);
    }

    public class itemHolder extends RecyclerView.ViewHolder /*implements View.OnClickListener,View.OnLongClickListener*/ {
        TextView titleView, dataView;
        CardView item;

        public itemHolder(@NonNull View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.itemTitle);
            dataView = itemView.findViewById(R.id.itemData);
            item = itemView.findViewById(R.id.cardView);

//            item.setOnClickListener(this);
//            item.setOnLongClickListener(this);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=getAdapterPosition();
                    if(listener!=null && position!=RecyclerView.NO_POSITION){
                        listener.onItemClick(notes.get(position));
                    }
                }
            });
        }
    }

    public interface onItemClickListener{
        void onItemClick(Note note);
    }

    public void setOnItemClickListener(onItemClickListener listener){
        this.listener=listener;
    }

//        @Override
//        public boolean onLongClick(View view) {
//            int pos=getAdapterPosition();
//
//            AlertDialog.Builder alert=new AlertDialog.Builder(view.getContext());
//            alert.setTitle("Delete")
//                    .setMessage("Do you want to delete this item from the list")
//                    .setCancelable(false)
//                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            dialogInterface.cancel();
//                        }
//                    }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    title.remove(pos);
//                    data.remove(pos);
//                    notifyItemRemoved(pos);
//                    fileHelper.writeTitle(view.getContext(),title);
//                    fileHelper.writeData(view.getContext(),data);
//                }
//            });
//            AlertDialog alertDialog=alert.create();
//            alertDialog.show();
//            return true;
//        }
//
//        @Override
//        public void onClick(View view) {
//            int pos=getAdapterPosition();
//            itemAdapterEvents.onItemClicked(title.get(pos),data.get(pos));
//            title.remove(pos);
//            data.remove(pos);
//            notifyItemRemoved(pos);
//            fileHelper.writeTitle(view.getContext(),title);
//            fileHelper.writeData(view.getContext(),data);
//        }
//    }
//
//    public interface itemAdapterEvents{
//        void onItemClicked(String Title,String Data);
//    }
}
