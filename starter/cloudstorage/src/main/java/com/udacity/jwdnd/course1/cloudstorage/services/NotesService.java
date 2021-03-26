package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.NoteForm;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotesService {
    private NoteMapper noteMapper;

    public NotesService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public void createNote(NoteForm noteForm) {
        Note newNote = new Note(noteForm.getNoteTitle(), noteForm.getNoteDescription());
        newNote.setUserId(noteForm.getUserId());
        noteMapper.addNote(newNote);
    }

    public void updateNote(NoteForm noteForm) {
        Note note = noteMapper.findById(noteForm.getNoteId().intValue());
        if (note != null) {
            note.setNoteTitle(noteForm.getNoteTitle());
            note.setNoteDescription(noteForm.getNoteDescription());
            noteMapper.updateNote(note);
        } else {
            throw new RuntimeException(String.format("Unable to find note \"%s\"", noteForm.getNoteTitle()));
        }
    }

    public void deleteNote(Integer noteId) {
        this.noteMapper.delete(noteId);
    }

    public List<Note> getAllNotes(Integer userId) {
        return noteMapper.findAllByUserId(userId);
    }
}
