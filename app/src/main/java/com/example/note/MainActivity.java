package com.example.note;

import static android.Manifest.permission.READ_MEDIA_IMAGES;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Contact contact;

    CalendarView calendarView;
    EditText noteEditText;
    Button saveButton;
    String fileName;
    private int year,month,dayOfMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences pref = getPreferences(MODE_PRIVATE);
        pref.edit().putInt("year", year).apply();
        pref.edit().putInt("month", month).apply();
        pref.edit().putInt("dayOfMonth", dayOfMonth).apply();

        int savedYear = pref.getInt("year", 0);
        if(savedYear!=0){
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR,savedYear);
            cal.set(Calendar.MONTH,pref.getInt("month", 0));
            cal.set(Calendar.DAY_OF_MONTH,pref.getInt("dayOfMonth", 0));
            calendarView.setDate(cal.getTimeInMillis());
        }

        calendarView = findViewById(R.id.calendarView);
        noteEditText = findViewById(R.id.noteEditText);
        saveButton = findViewById(R.id.saveButton);

        calendarView.setOnDateChangeListener((calendarView, year, month, dayOfMonth) -> {
            fileName = String.format("%02d_%02d_%04d", dayOfMonth, month + 1, year);
            noteEditText.setText("");
            try {
                FileInputStream fis = openFileInput(fileName);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader bufferedReader = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                fis.close();
                noteEditText.setText(sb);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        saveButton.setOnClickListener(view -> {
            String noteContent = noteEditText.getText().toString();
            try {
                // Ghi nội dung vào file
                FileOutputStream fos = openFileOutput(fileName, MODE_PRIVATE);
                fos.write(noteContent.getBytes());
                fos.close();
                Toast.makeText(this, "Đã lưu ghi chú", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Lỗi khi lưu ghi chú", Toast.LENGTH_LONG).show();
            }
        });
        

    }
}