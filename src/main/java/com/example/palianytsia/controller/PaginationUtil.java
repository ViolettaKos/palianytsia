package com.example.palianytsia.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.palianytsia.controller.Constants.*;


@Slf4j
public class PaginationUtil {

    public <T> Map<String, Object> paginationObj(Page<T> tPage, String direction) {
        List<T> content = tPage.getContent();
        Map<String, Object> response = new HashMap<>();

        response.put(CONTENT, content);
        response.put(CURR_PAGE, tPage.getNumber());
        response.put(TOTAL_PAGES, tPage.getTotalPages());
        response.put(RECORDS_PER_PAGE, tPage.getPageable().getPageSize());
        response.put(SIZE, tPage.getTotalElements());
        response.put(SORT, tPage.getSort().toString().split(":")[0]);
        response.put(DIR, direction);
        return response;
    }
}
