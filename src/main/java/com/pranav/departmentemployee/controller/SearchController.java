package com.pranav.departmentemployee.controller;

import com.pranav.departmentemployee.dto.response.SearchResponse;
import com.pranav.departmentemployee.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping
    public ResponseEntity<SearchResponse> search(
            @RequestParam String keyword) {

        return ResponseEntity.ok(
                searchService.search(keyword)
        );
    }
}
