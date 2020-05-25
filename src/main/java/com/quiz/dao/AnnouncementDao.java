package com.quiz.dao;

import com.quiz.dao.mapper.AnnouncementMapper;
import com.quiz.entities.Announcement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AnnouncementDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final static String GET_ANNON_FOR_AUTH_BY_ID = "SELECT date, text, generated FROM system_announcements " +
            "INNER JOIN friends ON sender_id = friend_id " +
            "WHERE user_id = ? " +
            "ORDER BY date desc";
    private final static String GET_ANNON_FOR_ANONIM = "SELECT date, text, generated FROM system_announcements " +
            "WHERE generated = false " +
            "ORDER BY date desc";

    public List<Announcement> getAnnouncementsByUserId(int userId) {
        return jdbcTemplate.query(GET_ANNON_FOR_AUTH_BY_ID,
                new Object[]{userId}, new AnnouncementMapper());
    }

    public List<Announcement> getAnnouncements() {
        return jdbcTemplate.query(GET_ANNON_FOR_ANONIM,
                new AnnouncementMapper());
    }
}
