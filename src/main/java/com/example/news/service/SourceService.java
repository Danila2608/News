package com.example.news.service;

import com.example.news.entity.Source;
import com.example.news.repository.SourceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SourceService {

    private final SourceRepository sourceRepository;

    public SourceService(SourceRepository sourceRepository) {
        this.sourceRepository = sourceRepository;
    }

    public List<Source> getAllSources() {
        return sourceRepository.findAll();
    }

    public Source getSourceById(Long id) {
        return sourceRepository.findById(id).orElse(null);
    }

    public void deleteSource(Long id) {
        sourceRepository.deleteById(id);
    }

    public Source createSource(Source source) {
        return sourceRepository.save(source);
    }

    public Source updateSource(Long id, Source source) {
        Source existingSource = sourceRepository.findById(id).orElse(null);
        if (existingSource != null) {
            existingSource.setName(source.getName());
            // Обновление остальных полей...
            return sourceRepository.save(existingSource);
        }
        return null;
    }
}
