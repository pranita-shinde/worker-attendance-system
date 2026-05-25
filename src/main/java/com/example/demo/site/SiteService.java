package com.example.demo.site;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class SiteService {

    private final SiteRepository siteRepository;

    public List<Site> getAllSites() {
        return siteRepository.findAll();
    }

    public Site addSite(Site site) {
        return siteRepository.save(site);
    }

    public Site getSite(Long id) {
        return siteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Site not found"));
    }

    public void deleteSite(Long id) {
        if (!siteRepository.existsById(id)) {
            throw new RuntimeException("Site not found");
        }
        siteRepository.deleteById(id);
    }
}