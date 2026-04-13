package com.example.phasmatic.ui.notes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phasmatic.R;
import com.example.phasmatic.data.model.Note;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    public interface OnNoteClickListener {
        void onNoteClick(Note note);
    }

    private Context context;
    private List<Note> notesList;
    private DatabaseReference notesRef;
    private OnNoteClickListener listener;

    public MyAdapter(Context context, List<Note> notesList, OnNoteClickListener listener) {
        this.context = context;
        this.notesList = notesList;
        this.listener = listener;

        FirebaseDatabase firebaseDb = FirebaseDatabase.getInstance(
                "https://mega-5a5b4-default-rtdb.europe-west1.firebasedatabase.app"
        );
        notesRef = firebaseDb.getReference("notes");
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Note note = notesList.get(position);

        String noteTitle = note.getTitle();
        if (note.isPinned()) {
            noteTitle = "📌 " + noteTitle;
        }

        holder.titleOutput.setText(noteTitle);
        holder.descriptionOutput.setText(note.getDescription());

        String formattedTime = DateFormat.getDateTimeInstance().format(note.getCreatedTime());
        holder.timeOutput.setText(formattedTime);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onNoteClick(note);
            }
        });

        holder.itemView.setOnLongClickListener(v -> {
            PopupMenu menu = new PopupMenu(context, v);
            menu.getMenu().add("DELETE");

            String pinTitle = note.isPinned() ? "UNPIN" : "PIN";
            menu.getMenu().add(pinTitle);

            menu.setOnMenuItemClickListener(item -> {
                String menuAction = item.getTitle().toString();

                if ("DELETE".equals(menuAction)) {
                    if (note.getId() != null) {
                        notesRef.child(note.getId()).removeValue()
                                .addOnSuccessListener(unused ->
                                        Toast.makeText(context, "Note deleted", Toast.LENGTH_SHORT).show()
                                )
                                .addOnFailureListener(e ->
                                        Toast.makeText(context, "Delete failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                                );
                    }
                    return true;
                } else if ("PIN".equals(menuAction)) {
                    if (note.getId() != null) {
                        notesRef.child(note.getId()).child("pinned").setValue(true)
                                .addOnSuccessListener(unused ->
                                        Toast.makeText(context, "Note pinned", Toast.LENGTH_SHORT).show()
                                )
                                .addOnFailureListener(e ->
                                        Toast.makeText(context, "Pin failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                                );
                    }
                    return true;
                } else if ("UNPIN".equals(menuAction)) {
                    if (note.getId() != null) {
                        notesRef.child(note.getId()).child("pinned").setValue(false)
                                .addOnSuccessListener(unused ->
                                        Toast.makeText(context, "Note unpinned", Toast.LENGTH_SHORT).show()
                                )
                                .addOnFailureListener(e ->
                                        Toast.makeText(context, "Unpin failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                                );
                    }
                    return true;
                }

                return false;
            });

            menu.show();
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return notesList != null ? notesList.size() : 0;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView titleOutput, descriptionOutput, timeOutput;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            titleOutput = itemView.findViewById(R.id.titleoutput);
            descriptionOutput = itemView.findViewById(R.id.descriptionoutput);
            timeOutput = itemView.findViewById(R.id.timeoutput);
        }
    }
}