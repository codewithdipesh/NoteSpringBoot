package com.cosmicdipesh.Note.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Table(name = "Note_table")
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer Id;

    @Column(name = "title")
    @NonNull
    private String title;

    @Column(name = "description")
    @NonNull
    private String description;

    @Column(name = "isLocked")
    @NonNull
    private Boolean isLocked =false;

    @Column(name = "isFavorite")
    @NonNull
    private Boolean isFavorite = false;

    @CreatedDate
    private String createdAt = String.valueOf(LocalDate.now());

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;


}