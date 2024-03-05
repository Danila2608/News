package com.example.news.service;

import com.example.news.entity.Source;
import com.example.news.repository.SourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SourceService {

    @Autowired
    private SourceRepository sourceRepository;

    public List<Source> getAllSources() {
        return sourceRepository.findAll();
    }

    public Source getSourceById(Long id) {
        Optional<Source> source = sourceRepository.findById(id);
        return source.orElse(null);
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
