package com.bangcompany.onlineute.Service.Impl;

import com.bangcompany.onlineute.DAO.StudyProgramDAO;
import com.bangcompany.onlineute.Model.Entity.StudyProgram;
import com.bangcompany.onlineute.Service.StudyProgramService;
import java.util.List;

public class StudyProgramServiceImpl implements StudyProgramService {
    private final StudyProgramDAO studyProgramDAO;

    public StudyProgramServiceImpl(StudyProgramDAO studyProgramDAO) {
        this.studyProgramDAO = studyProgramDAO;
    }

    @Override
    public List<StudyProgram> getAllStudyPrograms() {
        return studyProgramDAO.findAll();
    }
}
