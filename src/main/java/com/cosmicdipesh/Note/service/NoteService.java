package com.cosmicdipesh.Note.service;

import com.cosmicdipesh.Note.ExceptionHandler.ValidationException;
import com.cosmicdipesh.Note.entity.Note;
import com.cosmicdipesh.Note.entity.User;
import com.cosmicdipesh.Note.repository.NoteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NoteService {

    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }


    public Note save(Note note, User user) {
         Note savedNote = new Note();
         savedNote.setDescription(note.getDescription());
         savedNote.setTitle(note.getTitle());
         savedNote.setUser(user);
         savedNote.setCreatedAt(note.getCreatedAt());
         savedNote.setIsLocked(note.getIsLocked() != null ? note.getIsLocked(): false);
         savedNote.setIsFavorite(note.getIsFavorite() != null ? note.getIsFavorite(): false);
        noteRepository.save(savedNote);

        return savedNote;
    }

    public Optional<Note> updateNote(Integer noteId, Note updatedNote) {
        return noteRepository.findById(noteId)
                .map(existingNote -> {
                    existingNote.setTitle(updatedNote.getTitle());
                    existingNote.setIsFavorite(updatedNote.getIsFavorite() != null ? updatedNote.getIsFavorite() : existingNote.getIsFavorite());
                    existingNote.setIsLocked(updatedNote.getIsLocked() != null ? updatedNote.getIsLocked() : existingNote.getIsLocked());
                    // Don't update the user or createdAt fields
                    return noteRepository.save(existingNote);
                });

    }

    public List<Note> getAllNotes(Integer userId) {
       return noteRepository.findAllByUserId(userId);
    }

    public  Boolean deleteNote(Integer id) {
        noteRepository.deleteById(id);
        return  true;
    }


//    public Boolean toggleLockStatus(Integer id, Boolean lockStatus) {
//        Optional<Note> note = noteRepository.findById(id);
//        if (note.isPresent()) {
//            Note editedNote = note.get();
//            editedNote.setIsLocked(lockStatus);
//            noteRepository.save(editedNote);
//
//            return true;
//        }else{
//            return false;
//        }
//
//    }

}
