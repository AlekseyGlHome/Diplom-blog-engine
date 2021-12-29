package com.skillbox.engine.repository;

import com.skillbox.engine.model.DTO.CalendarDatePostCount;
import com.skillbox.engine.model.DTO.CalendarYearDTO;
import com.skillbox.engine.model.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    @Query("SELECT p " +
            "FROM Post p " +
            "LEFT JOIN User u ON u.id = p.user " +
            "LEFT JOIN PostVotes pv on pv.post = p.id and pv.value = 1 " +
            "WHERE p.isActive = 1 AND p.moderationStatus = 'ACCEPTED' AND p.time <= CURRENT_DATE() " +
            "GROUP BY p.id " +
            "ORDER BY COUNT(pv) DESC"
    )
    Page<Post> findPostsOrderByLikes(Pageable pageable);

    @Query("SELECT p " +
            "FROM Post p " +
            "LEFT JOIN User u ON u.id = p.user " +
            "LEFT JOIN PostComment pc ON pc.post = p.id " +
            "WHERE p.isActive = 1 AND p.moderationStatus = 'ACCEPTED' AND p.time <= CURRENT_DATE() " +
            "GROUP BY p.id " +
            "ORDER BY COUNT(pc) DESC"
    )
    Page<Post> findPostsOrderByComment(Pageable pageable);

    @Query("SELECT p " +
            "FROM Post p " +
            "LEFT JOIN User u ON u.id = p.user " +
            "WHERE p.isActive = 1 AND p.moderationStatus = 'ACCEPTED' AND p.time <= CURRENT_DATE() " +
            "GROUP BY p.id " +
            "ORDER BY p.time DESC"
    )
    Page<Post> findPostsOrderByDateDesc(Pageable pageable);

    @Query("SELECT p " +
            "FROM Post p " +
            "LEFT JOIN User u ON u.id = p.user " +
            "WHERE p.isActive = 1 AND p.moderationStatus = 'ACCEPTED' AND p.time <= CURRENT_DATE() " +
            "GROUP BY p.id " +
            "ORDER BY p.time ASC"
    )
    Page<Post> findPostsOrderByDateAsc(Pageable pageable);

    @Query("SELECT p " +
            "FROM Post p " +
            "LEFT JOIN User u ON u.id = p.user " +
            "WHERE p.isActive = 1 AND p.moderationStatus = 'ACCEPTED' AND p.time <= CURRENT_DATE() " +
            "and (p.text like %:query% or p.title like %:query%) " +
            "GROUP BY p.id " +
            "ORDER BY p.time DESC"
    )
    Page<Post> SearchesByPostsSortByDateDesc(Pageable pageable, String query);

    @Query("SELECT YEAR(p.time) AS year FROM Post p " +
            "WHERE p.isActive = 1 AND p.moderationStatus = 'ACCEPTED' AND p.time <= CURRENT_DATE() "+
            "GROUP BY year ORDER BY year DESC")
    List<CalendarYearDTO> getYearsOfPosts();

    @Query("SELECT DATE_FORMAT(p.time,'%Y-%m-%d') as date, COUNT(p.id) as count FROM Post p "+
            "WHERE p.isActive = 1 AND p.moderationStatus = 'ACCEPTED' AND p.time <= CURRENT_DATE()  " +
            "AND YEAR(p.time)=:year "+
            "GROUP BY date ORDER BY date DESC")
    List<CalendarDatePostCount> getTheCountOfPostsByDateOfPosts(int year);

    @Query("SELECT p " +
            "FROM Post p " +
            "LEFT JOIN User u ON u.id = p.user " +
            "WHERE p.isActive = 1 AND p.moderationStatus = 'ACCEPTED' AND p.time <= CURRENT_DATE() " +
            "AND DATE_FORMAT(p.time,'%Y-%m-%d')>=:date AND DATE_FORMAT(p.time,'%Y-%m-%d')<=:date " +
            "GROUP BY p.id " +
            "ORDER BY p.time DESC"
    )
    Page<Post> findAllPostsOnTheDate(Pageable pageable, String date);

    @Query("SELECT p FROM Tag2Post tp "+
            "LEFT JOIN Tag t ON t.id  = tp.tag "+
            "LEFT JOIN Post p ON p.id =tp.post "+
            "WHERE t.name =:tag AND p.isActive = 1 AND p.moderationStatus ='ACCEPTED' AND p.time <= CURRENT_DATE() "+
            "GROUP BY p.id " +
            "ORDER BY p.time DESC"
    )
    Page<Post> findPostsByTag(Pageable pageable, String tag);

}
