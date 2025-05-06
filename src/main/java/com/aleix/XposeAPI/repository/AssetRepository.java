package com.aleix.XposeAPI.repository;

import com.aleix.XposeAPI.model.Artist;
import com.aleix.XposeAPI.model.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface AssetRepository extends JpaRepository<Asset, Long>, JpaSpecificationExecutor<Asset> {

}