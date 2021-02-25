package com.msi.upload.repository.ms;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.msi.upload.model.ms.DmsGroupDoc;

@Repository
public interface DmsGroupDocRepository extends JpaRepository<DmsGroupDoc, String> {

}
