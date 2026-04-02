package com.bangcompany.onlineute.DAO;

import com.bangcompany.onlineute.Model.DTO.PageRequest;
import com.bangcompany.onlineute.Model.DTO.PagedResult;
import com.bangcompany.onlineute.Model.Entity.Announcement;
import java.util.List;

public interface AnnouncementDAO {
    void create(Announcement announcement);
    List<Announcement> findAll();
    PagedResult<Announcement> search(String keyword, PageRequest pageRequest);
    List<Announcement> findAnnouncementsForStudent(Long studentId);
    List<Announcement> findByTargetType(String targetType);
}
