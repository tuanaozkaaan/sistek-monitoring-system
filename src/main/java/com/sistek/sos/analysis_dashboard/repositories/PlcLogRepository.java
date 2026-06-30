package com.sistek.sos.analysis_dashboard.repositories;

import com.sistek.sos.analysis_dashboard.entities.PlcLog;
import com.sistek.sos.analysis_dashboard.entities.PlcLogId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlcLogRepository extends JpaRepository<PlcLog, PlcLogId> {
    Optional<PlcLog> findTopByPlcIdOrderBySeqNoDesc(String plcId);
}
