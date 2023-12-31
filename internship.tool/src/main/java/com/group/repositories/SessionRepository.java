package com.group.repositories;

import com.group.entities.Activity;
import com.group.entities.Session;
import com.group.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, Session.SessionID> {

    List<Session> findByUserAndActivity(User user, Activity activity);

    @Query("SELECT s FROM Session s WHERE s.date = :date")
    List<Session> findSessionsByDate(String date);
}
