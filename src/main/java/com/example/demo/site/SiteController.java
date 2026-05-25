package com.example.demo.site;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/v1/sites")
@AllArgsConstructor
public class SiteController {

    private final SiteService siteService;

    @GetMapping
    public List<Site> getSites() {
        return siteService.getAllSites();
    }

    @PostMapping
    public Site createSite(@Valid @RequestBody Site site) {
        return siteService.addSite(site);
    }

    @DeleteMapping("{siteId}")
    public void deleteSite(@PathVariable Long siteId) {
        siteService.deleteSite(siteId);
    }
}