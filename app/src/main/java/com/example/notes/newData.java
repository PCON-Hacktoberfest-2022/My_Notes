package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.service.quicksettings.Tile;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class newData extends AppCompatActivity {

    private EditText title,data;
//    public String Title,Data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().hide();
        getSupportActionBar().setBackgroundDrawable(getDrawable(R.drawable.custom_actionbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Note");
        setContentView(R.layout.activity_new_data);

        title=findViewById(R.id.etTitle);
        data=findViewById(R.id.etData);

//        Intent intent=getIntent();
//        title.setText(intent.getStringExtra("key updated title"));
//        data.setText(intent.getStringExtra("key updated data"));


    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
////        Title=title.getText().toString().trim();
////        Data=data.getText().toString().trim();
////        takeData(Title,Data);
//
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.save:
                saveNote();
                Toast.makeText(getApplicationContext(),"Note Saved",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.cancel:
                Toast.makeText(getApplicationContext(),"Nothing Saved",Toast.LENGTH_SHORT).show();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void saveNote(){
        String noteTitle=title.getText().toString();
        String noteDescription=data.getText().toString();

        Intent i=new Intent();
        i.putExtra("noteTitle",noteTitle);
        i.putExtra("noteDescription",noteDescription);
        setResult(RESULT_OK,i);
        finish();
    }

    //    public void takeData(String Title,String Data){
//        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
//        intent.putExtra("key title",Title);
//        intent.putExtra("key data",Data);
//        startActivity(intent);
//        this.finish();
//    }
}