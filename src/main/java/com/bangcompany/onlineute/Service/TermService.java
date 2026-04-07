package com.bangcompany.onlineute.Service;

import com.bangcompany.onlineute.Model.Entity.Term;
import java.util.List;
import java.util.Optional;

public interface TermService {
    // lấy toàn bộ học kỳ
    List<Term> getAllTerms();
    
    // lấy học kỳ theo ID
    Optional<Term> getTermById(Long id);
    
    // lấy học kỳ hiện tại
    Optional<Term> getCurrentTerm();
    
    // tạo học kỳ mới
    Term createTerm(Term term);

    // cập nhật học kỳ
    Term updateTerm(Term term);

    // xóa học kỳ
    void deleteTerm(Term term);
}
