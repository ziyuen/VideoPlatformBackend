package com.example.model;

import lombok.*;

import javax.persistence.*;

@Data
@NoArgsConstructor
@ToString
@Entity // This tells Hibernate to make a table out of this class
@Table(name="VIDEOCHUNK")
public class VideoChunk {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    private String hash;

    private Integer fileIndex;

    private String fileHash;

    public VideoChunk(String hash, Integer fileIndex, String fileHash) {
        this.hash = hash.toUpperCase();
        this.fileIndex = fileIndex;
        this.fileHash = fileHash;
    }
}
