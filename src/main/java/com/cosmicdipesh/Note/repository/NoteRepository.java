package com.cosmicdipesh.Note.repository;

import com.cosmicdipesh.Note.entity.Note;
import com.cosmicdipesh.Note.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note,Integer> {
    List<Note> findAllByUser(User user);
    List<Note> findAllByUserId(Integer userId);

}
