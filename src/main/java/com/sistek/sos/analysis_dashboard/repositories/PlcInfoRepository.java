package com.sistek.sos.analysis_dashboard.repositories;

import com.sistek.sos.analysis_dashboard.entities.PlcInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlcInfoRepository extends JpaRepository<PlcInfo, String> {
    // Additional query methods can be defined here if needed
}

