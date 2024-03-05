package com.example.news.controller;

import com.example.news.entity.Source;
import com.example.news.service.SourceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sources")
public class SourceController {

    private final SourceService sourceService;

    public SourceController(SourceService sourceService) {
        this.sourceService = sourceService;
    }

    @GetMapping
    public List<Source> getAllSources() {
        return sourceService.getAllSources();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Source> getSourceById(@PathVariable Long id) {
        Source source = sourceService.getSourceById(id);
        if (source != null) {
            return ResponseEntity.ok(source);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSource(@PathVariable Long id) {
        sourceService.deleteSource(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<Source> createSource(@RequestBody Source source) {
        Source createdSource = sourceService.createSource(source);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSource);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Source> updateSource(@PathVariable Long id, @RequestBody Source source) {
        Source updatedSource = sourceService.updateSource(id, source);
        if (updatedSource != null) {
            return ResponseEntity.ok(updatedSource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
