package com.example.cal;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

public class SubActivity extends AppCompatActivity {
    private EditText titleEditText, descEditText;
    private DatePickerDialog datePickerDialog;
    private Button dateButton;
    private Note selectedNote;
    private Button deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        initWidgets();
        checkForEditNote();
        initDatePicker();
        dateButton = findViewById(R.id.datePicerButton);
        dateButton.setText(getTodaysDate());
    }

    // 각 텍스트 뷰 선언
    private void initWidgets() {
        titleEditText = findViewById(R.id.titleEditText);
        descEditText = findViewById(R.id.descriptionEditText);
        deleteButton = findViewById(R.id.deleteNoteButton);
    }

    //note수정
    private void checkForEditNote() {
        Intent previousIntent = getIntent();

        int passedNoteID = previousIntent.getIntExtra(Note.NOTE_EDIT_EXTRA, -1);
        selectedNote = Note.getNoteForID(passedNoteID);

        if (selectedNote != null)
        {
            titleEditText.setText(selectedNote.getTitle());
            descEditText.setText(selectedNote.getDescription());
        }
        else
        {
            deleteButton.setVisibility(View.INVISIBLE);
        }
    }
    //note 저장
    public void saveNote(View view) {
        SQLiteManager sqLiteManager = SQLiteManager.instanceOFDatabase(this);
        String title = String.valueOf(titleEditText.getText());
        String desc = String.valueOf(descEditText.getText());

        if (selectedNote == null)
        {
            int id = Note.noteArrayList.size();
            Note newNote = new Note(id, title, desc);
            Note.noteArrayList.add(newNote);
            sqLiteManager.addNoteTODatabase(newNote);

        } else {
            selectedNote.setTitle(title);
            selectedNote.setDescription(desc);
            sqLiteManager.updateNoteInDB(selectedNote);
        }

        finish();
    }




    private String getTodaysDate()
    {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day,month,year);
    }

    private  void initDatePicker()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month +1;
                String date = makeDateString(day, month, year);
                dateButton.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }

    private String makeDateString(int day, int month, int year)

    {
    return getMonthFormat(month) + " " + day + " " + year;

    }

    private String getMonthFormat(int month)
    {
        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "APR";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JUL";
        if(month == 8)
            return "AUG";
        if(month == 9)
            return "SEP";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DEC";

            return "JAN";


    }

    public void openDatePicker(View view)
    {
        datePickerDialog.show();
    }

    public void deleteNote(View view)
    {
        selectedNote.setDeleted(new Date());
        SQLiteManager sqLiteManager = SQLiteManager.instanceOFDatabase(this);
        sqLiteManager.updateNoteInDB(selectedNote);
        finish();
    }
}