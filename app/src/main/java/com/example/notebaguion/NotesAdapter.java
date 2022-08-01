package com.example.notebaguion;

import static com.example.notebaguion.Note.*;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NotesAdapter extends ArrayAdapter<Note> {
    int resource;
    List<Note> notes;
    FragmentManager fm;
    Note current;
    NotesOpenHelper helper;

    public NotesAdapter(@NonNull Context context, int resource, @NonNull List<Note> objects, FragmentManager fm, NotesOpenHelper helper) {
        super(context, resource, objects);
        this.resource = resource;
        this.notes = objects;
        this.fm = fm;
        this.helper = helper;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LinearLayout noteView;
        Note note = getItem(position);
        String act_note = note.getNote();
        Date act_created = note.getCreated();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String timeString = sdf.format(act_created);

        if (convertView == null) {
            noteView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater li = (LayoutInflater) getContext().getSystemService(inflater);
            li.inflate(resource, noteView, true);
        } else {
            noteView = (LinearLayout) convertView;
        }

        TextView tvNote = noteView.findViewById(R.id.tvNote);
        TextView tvTime = noteView.findViewById(R.id.tvTime);
        if (note.important) {
            tvNote.setText(act_note + " ★");
        }else{
            tvNote.setText(act_note);
        }
        tvTime.setText(timeString);

        ImageButton btnDelete = noteView.findViewById(R.id.btnClear);
        btnDelete.setImageResource(R.drawable.ic_launcher_foreground);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = note.id;
                // String selector = KEY_ID + "=?";
                // String selectorArgs[] = {id+""};
                String selector = KEY_ID + "=" + id;
                String selectorArgs[] = null;
//                SQLiteDatabase db = helper.getWritableDatabase();
//                db.delete(NotesOpenHelper.DATABASE_TABLE, selector, selectorArgs);
                ContentResolver cr = getContext().getContentResolver();
                cr.delete(NotesContentProvider.CONTENT_URI, selector, selectorArgs);
                notes.remove(note);
                notifyDataSetChanged();
            }
        });
        noteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditNoteDialogFragment dialog = new EditNoteDialogFragment(act_note);
                dialog.show(fm, "Alert");
                current = note;
            }
        });
        return noteView;

    }
    public void onEditListenerMethod(DialogFragment dialog) {
        EditText etEdit = dialog.getDialog().findViewById(R.id.etEdit);
        CheckBox cbEditImp = dialog.getDialog().findViewById(R.id.cbEditImp);
        boolean important = cbEditImp.isChecked();
        String new_note = etEdit.getText().toString();
        current.setNote(new_note);
        notifyDataSetChanged();
        ContentValues cv = new ContentValues();
        current.important = important;
        cv.put(KEY_NOTE_COLUMN, new_note);

        String selector = KEY_ID + "=" + current.id;
        String selectorArgs[] = null;
        cv.put(KEY_NOTE_IMPORTANT_COLUMN, important);

//        SQLiteDatabase db = helper.getWritableDatabase();
//        db.update(NotesOpenHelper.DATABASE_TABLE, cv, selector, selectorArgs);
        ContentResolver cr = getContext().getContentResolver();
        Uri rowUri = ContentUris.withAppendedId(NotesContentProvider.CONTENT_URI, current.id);
        cr.update(rowUri, cv, null, null);
        current = null;

    }
    public void onCancelListenerMethod(DialogFragment dialog) {
        current = null;
    }
}

