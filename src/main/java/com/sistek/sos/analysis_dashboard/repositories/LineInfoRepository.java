package com.sistek.sos.analysis_dashboard.repositories;

import com.sistek.sos.analysis_dashboard.entities.LineInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LineInfoRepository extends JpaRepository<LineInfo, String> {
    // Additional query methods can be defined here if needed
}
