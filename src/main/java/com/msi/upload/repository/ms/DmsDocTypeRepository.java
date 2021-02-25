package com.msi.upload.repository.ms;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.msi.upload.model.ms.DmsDocType;

@Repository
public interface DmsDocTypeRepository extends JpaRepository<DmsDocType, String>{

}
