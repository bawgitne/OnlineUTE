package com.bangcompany.onlineute.Service;

import com.bangcompany.onlineute.Model.Entity.Mark;
import java.util.Optional;

public interface MarkService {
    Mark saveMark(Mark mark);
    
    void deleteMark(Mark mark);
    
    Optional<Mark> getMarkById(Long id);
    
    Optional<Mark> getMarkByRegistration(Long registrationId);
    
    void calculateGrade(Mark mark);
}
