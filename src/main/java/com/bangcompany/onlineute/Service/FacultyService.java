package com.bangcompany.onlineute.Service;

import com.bangcompany.onlineute.Model.Entity.Faculty;

import java.util.List;

public interface FacultyService {
    List<Faculty> getAllFaculties();
    Faculty createFaculty(Faculty faculty);
}
