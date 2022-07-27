package com.example.notebaguion;

import android.app.Dialog;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.os.Bundle;
        import android.view.LayoutInflater;
        import android.widget.EditText;

        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.appcompat.app.AlertDialog;
        import androidx.fragment.app.DialogFragment;

public class EditNoteDialogFragment extends DialogFragment {
    EditNoteDialogListener listener;
    String note;

    public EditNoteDialogFragment(String note){

        this.note = note;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (EditNoteDialogListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(getActivity().toString() + "Must Implement EditDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setMessage("Note Editor " + note)
                .setView(inflater.inflate(R.layout.editdialoge_layout, null))
                .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.onEditListenerMethod(EditNoteDialogFragment.this);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.onCancelListenerMethod(EditNoteDialogFragment.this);
                    }
                });
        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        EditText etEdit = this.getDialog().findViewById(R.id.etEdit);
        etEdit.setText(note);
    }

    public interface EditNoteDialogListener{
        public void onEditListenerMethod(DialogFragment dialog);
        public void onCancelListenerMethod(DialogFragment dialog);
    }
}