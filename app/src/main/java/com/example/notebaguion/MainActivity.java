package com.example.notebaguion;

import static com.example.notebaguion.Note.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements EditNoteDialogFragment.EditNoteDialogListener {

    ArrayList<Note> notes;
    NotesAdapter  notes_adapter;
    NotesOpenHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        helper = new NotesOpenHelper(this, NotesOpenHelper.DATABASE_NAME,
                null, NotesOpenHelper.DATABASE_VERSION);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query(NotesOpenHelper.DATABASE_TABLE, null, null,
                null,null, null, null);


        setListAdapterMethod();
        btnAddListenerMethod();
        etNoteEnterListenerMethod();

        int INDEX_NOTE = cursor.getColumnIndexOrThrow(KEY_NOTE_COLUMN);
        int INDEX_ID = cursor.getColumnIndexOrThrow(KEY_ID);
        int INDEX_CREATED = cursor.getColumnIndexOrThrow(KEY_NOTE_CREATED_COLUMN);
        int INDEX_IMPORTANT = cursor.getColumnIndexOrThrow(KEY_NOTE_IMPORTANT_COLUMN);
        while (cursor.moveToNext()) {
            String note = cursor.getString(INDEX_NOTE);
            int id = cursor.getInt(INDEX_ID);
            int int_important = cursor.getInt(INDEX_IMPORTANT);
            long date = cursor.getLong(INDEX_CREATED);
            Note n = new Note(note);
            n.id = id;
            n.setCreated(new Date(date));
            n.important = int_important == 1;
            notes.add(n);
        }

    }
    private void etNoteEnterListenerMethod(){
        EditText etNote = findViewById(R.id.etNote);
        etNote.setOnKeyListener(new View.OnKeyListener(){
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent){
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN ||
                        keyEvent.getAction() == KeyEvent.KEYCODE_ENTER ||
                        keyEvent.getAction() == KeyEvent.KEYCODE_NUMPAD_ENTER){
                    addNoteMethod();
                    return true;
            }
            return false;
            }
        });
    }

    public void addNoteMethod() {
        EditText etNote = findViewById(R.id.etNote);
        String note = etNote.getText().toString();
        CheckBox cbImportant = findViewById(R.id.cbImportant);
        etNote.setText("");
        boolean important = cbImportant.isChecked();

        ContentValues cv = new ContentValues();
        cv.put(KEY_NOTE_COLUMN, note);
        cv.put(KEY_NOTE_CREATED_COLUMN, System.currentTimeMillis());
        cv.put(KEY_NOTE_IMPORTANT_COLUMN, important ? 1:0);

        SQLiteDatabase db = helper.getWritableDatabase();
        int id = (int) db.insert(NotesOpenHelper.DATABASE_TABLE, null, cv);

        Note n = new Note(note);
        n.important = important;
        n.id = id;
        notes.add(n);
        notes_adapter.notifyDataSetChanged();
    }

    private void btnAddListenerMethod(){
        Button btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNoteMethod();
            }
        });
    }

    private void setListAdapterMethod ()  {
        ListView lvList = findViewById(R.id.lvList);
        notes = new ArrayList<>();

        notes_adapter = new NotesAdapter(getBaseContext(),R.layout.note_layout, notes, getSupportFragmentManager(), helper);
        lvList.setAdapter(notes_adapter);


    }

    public void onEditListenerMethod(DialogFragment dialog) {
        notes_adapter.onEditListenerMethod(dialog);
    }


    public void onCancelListenerMethod(DialogFragment dialog) {
        notes_adapter.onCancelListenerMethod(dialog);
    }
}
