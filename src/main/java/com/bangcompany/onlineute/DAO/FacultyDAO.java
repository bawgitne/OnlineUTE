package com.bangcompany.onlineute.DAO;

import com.bangcompany.onlineute.Model.Entity.Faculty;

import java.util.List;

public interface FacultyDAO {
    List<Faculty> findAll();
    Faculty save(Faculty faculty);
}
