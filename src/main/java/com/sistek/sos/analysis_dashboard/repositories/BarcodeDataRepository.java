package com.sistek.sos.analysis_dashboard.repositories;

import com.sistek.sos.analysis_dashboard.entities.BarcodeData;
import com.sistek.sos.analysis_dashboard.entities.BarcodeDataId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BarcodeDataRepository extends JpaRepository<BarcodeData, BarcodeDataId> {
    List<BarcodeData> findByLineIdOrderByCreDateDesc(String lineId);
}
