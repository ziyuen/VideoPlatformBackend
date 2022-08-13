package com.example.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@ToString
@Entity // This tells Hibernate to make a table out of this class
@Table(name="VIDEO")
public class Video {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER,
            orphanRemoval = true)
    private List<VideoChunk> chunks;

    @Column
    private String title;

    @Column
    private String fileName;

    @Column
    private String description;

    @Column
    private String videoUrl;

    @Column
    private String avatarImg;

    @Column(unique = true)
    private String hash;

    @Column
    private Integer size;

    @Enumerated(EnumType.ORDINAL) // default is INCOMPLETE
    private VideoStateType state = VideoStateType.INCOMPLETE;

}