package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Insert;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class UpdateActivity extends AppCompatActivity {

    private EditText title,data;
    int noteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setBackgroundDrawable(getDrawable(R.drawable.custom_actionbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit Note");
        setContentView(R.layout.activity_update);

        title=findViewById(R.id.etTitleUpdate);
        data=findViewById(R.id.etDataUpdate);

        getData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.save:
                updateNote();
                Toast.makeText(getApplicationContext(),"Note Updated",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.cancel:
                Toast.makeText(getApplicationContext(),"Nothing Changed",Toast.LENGTH_SHORT).show();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void updateNote() {
        String titleLast=title.getText().toString();
        String descriptionLast=data.getText().toString();

        Intent intent=new Intent();
        intent.putExtra("titleLast",titleLast);
        intent.putExtra("descriptionLast",descriptionLast);
        if(noteId!=-1){
            intent.putExtra("noteId",noteId);
            setResult(RESULT_OK,intent);
            finish();
        }
    }

    public void getData(){
        Intent i=getIntent();
        noteId=i.getIntExtra("id",-1);
        String noteTitle=i.getStringExtra("title");
        String noteDescription=i.getStringExtra("description");

        title.setText(noteTitle);
        data.setText(noteDescription);
    }

}