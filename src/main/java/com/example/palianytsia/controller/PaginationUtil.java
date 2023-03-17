package com.example.palianytsia.controller;

import com.example.palianytsia.model.Item;
import com.example.palianytsia.model.ItemType;
import com.example.palianytsia.repository.ItemRepository;
import com.example.palianytsia.service.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.example.palianytsia.controller.Constants.*;


@Slf4j
public class PaginationUtil {



    public Map<String, Object> pagination(Page<Item> itemPage, String direction) {
        List<Item> items = itemPage.getContent();
        Map<String, Object> response = new HashMap<>();

        response.put(ITEMS, items);
        response.put(CURR_PAGE, itemPage.getNumber());
        response.put(TOTAL_PAGES, itemPage.getTotalPages());
        response.put(RECORDS_PER_PAGE, itemPage.getPageable().getPageSize());
        response.put(SIZE, itemPage.getTotalElements());
        response.put(SORT, itemPage.getSort().toString().split(":")[0]);
        response.put(DIR, direction);
        return response;
    }
}
