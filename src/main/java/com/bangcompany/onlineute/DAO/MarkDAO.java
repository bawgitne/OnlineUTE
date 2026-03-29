package com.bangcompany.onlineute.DAO;

import com.bangcompany.onlineute.Model.Entity.Mark;
import java.util.Optional;

public interface MarkDAO {
    Mark save(Mark mark);
    
    Mark update(Mark mark);
    
    void delete(Mark mark);
    
    Optional<Mark> findById(Long id);
    
    Optional<Mark> findByRegistrationId(Long registrationId);
}
