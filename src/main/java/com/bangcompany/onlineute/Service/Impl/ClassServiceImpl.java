package com.bangcompany.onlineute.Service.Impl;

import com.bangcompany.onlineute.DAO.ClassDAO;
import com.bangcompany.onlineute.Model.Entity.Class;
import com.bangcompany.onlineute.Service.ClassService;
import java.util.List;

public class ClassServiceImpl implements ClassService {
    private final ClassDAO classDAO;

    public ClassServiceImpl(ClassDAO classDAO) {
        this.classDAO = classDAO;
    }

    @Override
    public List<Class> getAllClasses() {
        return classDAO.findAll();
    }
}
