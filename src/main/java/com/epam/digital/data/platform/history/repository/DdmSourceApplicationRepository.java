package com.epam.digital.data.platform.history.repository;

import com.epam.digital.data.platform.history.model.DdmSourceApplication;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface DdmSourceApplicationRepository extends CrudRepository<DdmSourceApplication, UUID> {
    List<DdmSourceApplication> findByApplicationIdIn(List<UUID> ids);
}
