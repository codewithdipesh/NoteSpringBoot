package com.cosmicdipesh.Note.controller;

import com.cosmicdipesh.Note.ExceptionHandler.ValidationException;
import com.cosmicdipesh.Note.entity.ApiResponse;
import com.cosmicdipesh.Note.entity.ChangePasswordRequest;
import com.cosmicdipesh.Note.entity.Note;
import com.cosmicdipesh.Note.entity.User;
import com.cosmicdipesh.Note.repository.NoteRepository;
import com.cosmicdipesh.Note.repository.UserRepository;
import com.cosmicdipesh.Note.service.NoteService;
import com.cosmicdipesh.Note.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/note")
public class NoteController {

    @Autowired
    private NoteService noteService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NoteRepository noteRepository;

    @PostMapping("/")
    public ResponseEntity<ApiResponse<Note>> createNewNote(@RequestBody Note note) {

        if(note.getTitle() == null || note.getDescription()== null){
            throw new ValidationException("Title and description are required");
        }else{
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            User User = userRepository.findByUsername(username).orElse(null);

            if(User == null){
                throw new ValidationException("UnAuthorized");
            }
            Note savedNote = noteService.save(note,User);

            return new ResponseEntity<>(new ApiResponse<>("Note Created Successfully",savedNote,HttpStatus.CREATED.value())
            ,HttpStatus.CREATED);

        }

    }

    @GetMapping("/")
    public ResponseEntity<ApiResponse<List<Note>>> getAllNotes() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User User = userRepository.findByUsername(username).orElse(null);

        if(User == null){
            throw new ValidationException("UnAuthorized");
        }
        return new ResponseEntity<>(new ApiResponse<>("Notes fetched Successfully",User.getNotes(),HttpStatus.OK.value())
                ,HttpStatus.OK);
    }

    @PutMapping("/{noteId}")
    public ResponseEntity<ApiResponse<Boolean>> updateNote(@PathVariable Integer noteId, @RequestBody Note note) {
        if(noteId == null){
            throw new ValidationException("Note id is required");
        }
        if(note.getTitle() == null || note.getDescription()== null){
            throw new ValidationException("Title and description are required");
        }else{
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            Note previousNote = noteRepository.findById(noteId).orElse(null);
            if(previousNote == null ) throw  new ValidationException("Note not found");
            //note owner is different
            if(!Objects.equals(username, previousNote.getUser().getUsername())){
               throw new ValidationException("UnAuthorized");
            }
            Note updated = noteService.updateNote(noteId,note).orElse(null);
            if(updated == null) throw new ValidationException("Note not found");
            return new ResponseEntity<>(new ApiResponse<>("Note Updated Successfully",true,HttpStatus.OK.value()),
                    HttpStatus.OK);
        }
    }
    @DeleteMapping("/{noteId}")
    public ResponseEntity<ApiResponse<Boolean>> deleteNote(@PathVariable Integer noteId) {
        if(noteId == null){
            throw new ValidationException("Note id is required");
        }
        else{
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            Note previousNote = noteRepository.findById(noteId).orElse(null);
            if(previousNote == null ) throw  new ValidationException("Note not found");
            //note owner is different
            if(!Objects.equals(username, previousNote.getUser().getUsername())){
                throw new ValidationException("UnAuthorized");
            }
            Boolean deleted = noteService.deleteNote(noteId);
            return new ResponseEntity<>(new ApiResponse<>("Note Deleted Successfully",deleted,HttpStatus.OK.value()),
                    HttpStatus.OK);
        }
    }


}
