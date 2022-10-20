package com.example.notes;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity /*implements itemAdapter.itemAdapterEvents*/{

    private Button addBtn;
    private RecyclerView recyclerView;

    ActivityResultLauncher<Intent> activityResultLauncherForAddNote;
    ActivityResultLauncher<Intent> activityResultLauncherForUpdateNote;

//    private ArrayList<String> title=new ArrayList<>();
//    private ArrayList<String> data=new ArrayList<>();

    private NoteViewModel noteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().hide();
        getSupportActionBar().setBackgroundDrawable(getDrawable(R.drawable.custom_actionbar));
        setContentView(R.layout.activity_main);

        registerActivityForAddNote();
        registerActivityForUpdateNote();
        
        intialize();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        itemAdapter adapter=new itemAdapter();
        recyclerView.setAdapter(adapter);

        noteViewModel=new ViewModelProvider.AndroidViewModelFactory(getApplication())
                .create(NoteViewModel.class);
        noteViewModel.getALlData().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                //Recycler view
                adapter.setNotes(notes);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                noteViewModel.delete(adapter.getNotes(viewHolder.getAdapterPosition()));
                Toast.makeText(getApplicationContext(),"Note Deleted",Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new itemAdapter.onItemClickListener() {
            @Override
            public void onItemClick(Note note) {
                Intent intent=new Intent(getApplicationContext(),UpdateActivity.class);
                intent.putExtra("id",note.getId());
                intent.putExtra("title",note.getTitle());
                intent.putExtra("description",note.getDescription());
                activityResultLauncherForUpdateNote.launch(intent);
            }
        });

//        title=fileHelper.readTitle(this);
//        data=fileHelper.readData(this);

//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(new itemAdapter(this,title,data,this));



        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),newData.class);
//                startActivityForResult(intent,1);
                activityResultLauncherForAddNote.launch(intent);
            }
        });

    }

    public void registerActivityForUpdateNote(){
        activityResultLauncherForUpdateNote=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        int resultCode=result.getResultCode();
                        Intent data=result.getData();

                        if(data!=null){
                            String title=data.getStringExtra("titleLast");
                            String description=data.getStringExtra("descriptionLast");
                            int id=data.getIntExtra("noteId",-1);

                            Note note=new Note(title,description);
                            note.setId(id);

                            if(resultCode==RESULT_OK){
                                noteViewModel.update(note);
                            }else if(resultCode==404){
                                noteViewModel.delete(note);
                            }
                        }
                    }
                });
    }

    public void registerActivityForAddNote(){
        activityResultLauncherForAddNote=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        int resultCode=result.getResultCode();
                        Intent data=result.getData();

                        if(resultCode==RESULT_OK && data!=null){
                            String title=data.getStringExtra("noteTitle");
                            String description=data.getStringExtra("noteDescription");

                            Note note=new Note(title,description);
                            noteViewModel.insert(note);
                        }
                    }
                });
    }
    
    public void intialize(){
        addBtn = findViewById(R.id.addbtn);
        recyclerView = findViewById(R.id.items);
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1 && requestCode==RESULT_OK && data!=null){
            String title=data.getStringExtra("noteTitle");
            String description=data.getStringExtra("noteDescription");

            Note note=new Note(title,description);
            noteViewModel.insert(note);
        }
    }*/

    //    public void setData(){
//        Intent intent=getIntent();
//        String Title = intent.getStringExtra("key title");
//        String Data = intent.getStringExtra("key data");
//
//        if((Title!=null && !Title.isEmpty()) || (Data!=null && !Data.isEmpty())) {
//            title.add(Title);
//            data.add(Data);
//        }
//        fileHelper.writeTitle(getApplicationContext(),title);
//        fileHelper.writeData(getApplicationContext(),data);
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        setData();
//    }
//
//    @Override
//    public void onItemClicked(String Title,String Data) {
//        Toast.makeText(this,"selected "+Title,Toast.LENGTH_SHORT).show();
//        Intent intent=new Intent(getApplicationContext(),newData.class);
//        intent.putExtra("key updated title",Title);
//        intent.putExtra("key updated data",Data);
//        startActivity(intent);
//        finish();
//    }
}
