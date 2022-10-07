package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;

import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class UpdateActivity extends AppCompatActivity {

    private File root;
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
        root = new File(Environment.getExternalStorageDirectory()+"/Documents/.myNotes");
        if (!root.exists()) {
            root.mkdirs();
        }
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
            case R.id.delete:
                Toast.makeText(getApplicationContext(),"Note Deleted",Toast.LENGTH_SHORT).show();
                deleteNote();
                return true;
            case R.id.cancel:
                Toast.makeText(getApplicationContext(),"Nothing Changed",Toast.LENGTH_SHORT).show();
                finish();
                return true;
            case R.id.share:
                shareNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void deleteNote() {
        String titleLast=title.getText().toString();
        String descriptionLast=data.getText().toString();

        Intent intent=new Intent();
        intent.putExtra("titleLast",titleLast);
        intent.putExtra("descriptionLast",descriptionLast);
        if(noteId!=-1){
            intent.putExtra("noteId",noteId);
            setResult(404,intent);
            finish();
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

    private void shareNote(){
        final CharSequence[] items = {"Share as Text file", "Share as pdf file"};
        final int[] selectedItem = {-1};
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateActivity.this);
        builder.setTitle("Select file type");
        builder.setSingleChoiceItems(items, -1, (dialog, item) -> selectedItem[0] =item);
        builder.setPositiveButton("Share",
                (dialog, id) -> {
                    if(selectedItem[0]==0){
                        shareNoteAsText();
                    }else if(selectedItem[0]==1){
                        generatePDF();
                    }else{
                        Toast.makeText(UpdateActivity.this, "Please select file type", Toast.LENGTH_SHORT).show();
                    }
                });
        builder.setNegativeButton("Cancel",
                (dialog, id) -> Toast.makeText(UpdateActivity.this, "Cancelled!!", Toast.LENGTH_SHORT).show());
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void shareNoteAsText() {
        String note_title=title.getText().toString();
        String note_description=data.getText().toString();
        File file = new File(root+"/"+note_title+".txt");
        Uri uri= FileProvider.getUriForFile(UpdateActivity.this,
                getApplicationContext().getPackageName() + ".provider", file);
        try {
            FileWriter writer = new FileWriter(file);
            writer.append(note_title).append("\n\n").append(note_description);
            writer.flush();
            writer.close();
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/*");
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            startActivity(Intent.createChooser(intent, "Share using ..."));
        }catch (Exception e){
            Toast.makeText(this, "Something went wrong!!", Toast.LENGTH_SHORT).show();
        }
    }

    private void shareNoteAsPdf(File file) {
        Uri uri= FileProvider.getUriForFile(UpdateActivity.this,
                getApplicationContext().getPackageName() + ".provider", file);
        Intent share = new Intent();
        share.setAction(Intent.ACTION_SEND);
        share.setType("application/pdf");
        share.putExtra(Intent.EXTRA_STREAM, uri);
        try {
            startActivity(Intent.createChooser(share,"share using ..."));
        }catch (Exception e){
            Log.d("TAG",""+e);
        }

    }

    private void generatePDF() {
        String note_title=title.getText().toString();
        String note_description=data.getText().toString();
        PdfDocument pdfDocument = new PdfDocument();
        if(!root.exists()){
            root.mkdir();
        }
        File file = new File(root+"/"+note_title+".pdf");
        if(file.exists()){
            file.delete();
        }
        try {
            pdfDocument.writeTo(new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Document doc = new Document();
            PdfWriter.getInstance(doc, new FileOutputStream(file));
            doc.open();
            Font regular = new Font(Font.FontFamily.HELVETICA, 18);
            Font bold = new Font(Font.FontFamily.HELVETICA, 24, Font.BOLD);
            Paragraph header= new Paragraph(note_title+"\n\n",bold);
            header.setAlignment(Element.ALIGN_CENTER);
            doc.add(header);
            doc.add(new Paragraph(note_description,regular));
            doc.close();
            shareNoteAsPdf(file);
        }catch (Exception e){
            Toast.makeText(this, "Something went wrong!!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (root.isDirectory())
        {
            String[] children = root.list();
            for (String child : children != null ? children : new String[0]) {
                new File(root, child).delete();
            }
            root.delete();
        }
    }
}