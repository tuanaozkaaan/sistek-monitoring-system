package com.sistek.sos.analysis_dashboard.repositories;

import com.sistek.sos.analysis_dashboard.entities.LineLog;
import com.sistek.sos.analysis_dashboard.entities.LineLogId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LineLogRepository extends JpaRepository<LineLog, LineLogId> {
    Optional<LineLog> findTopByLineIdOrderBySeqNoDesc(String lineId);
}
