package com.cosmicdipesh.Note.repository;

import com.cosmicdipesh.Note.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<Note,Integer> {


}
