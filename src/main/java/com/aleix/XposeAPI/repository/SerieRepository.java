package com.aleix.XposeAPI.repository;

import com.aleix.XposeAPI.model.Artist;
import com.aleix.XposeAPI.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface SerieRepository extends JpaRepository<Serie, Long>, JpaSpecificationExecutor<Serie> {

}
