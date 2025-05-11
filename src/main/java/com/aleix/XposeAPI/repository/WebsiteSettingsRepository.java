package com.aleix.XposeAPI.repository;

import com.aleix.XposeAPI.model.WebsiteSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WebsiteSettingsRepository extends JpaRepository<WebsiteSettings, Long> {
    // Basic CRUD operations are provided by JpaRepository
}