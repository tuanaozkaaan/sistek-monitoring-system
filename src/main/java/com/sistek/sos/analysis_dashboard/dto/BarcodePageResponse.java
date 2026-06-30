package com.sistek.sos.analysis_dashboard.dto;

import com.sistek.sos.analysis_dashboard.entities.BarcodeData;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class BarcodePageResponse {
    private List<BarcodeData> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean hasMore;

    public static BarcodePageResponse from(Page<BarcodeData> page) {
        BarcodePageResponse response = new BarcodePageResponse();
        response.setContent(page.getContent());
        response.setPage(page.getNumber());
        response.setSize(page.getSize());
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        response.setHasMore(page.hasNext());
        return response;
    }
}
