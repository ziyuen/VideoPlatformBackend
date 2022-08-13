package com.example.repository;

import com.example.model.Video;
import com.example.model.VideoStateType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

public interface VideoRepository extends CrudRepository<Video, Integer> {
    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query("select v from Video v where v.hash = :hash")
    Optional<Video> findVideoByHashForUpdate(String hash);
    Optional<Video> findVideoByHash(String hash);

//    @Lock(value = LockModeType.PESSIMISTIC_READ)
    List<Video> findByState(VideoStateType state);
}
