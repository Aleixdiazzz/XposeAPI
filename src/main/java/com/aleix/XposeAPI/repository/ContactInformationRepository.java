package com.aleix.XposeAPI.repository;

import com.aleix.XposeAPI.model.ContactInformation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactInformationRepository extends JpaRepository<ContactInformation, Long> {

}

