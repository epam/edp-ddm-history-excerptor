package com.epam.digital.data.platform.history.repository;

import com.epam.digital.data.platform.history.model.DdmSourceSystem;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface DdmSourceSystemRepository extends CrudRepository<DdmSourceSystem, UUID> {
    List<DdmSourceSystem> findBySystemIdIn(List<UUID> ids);
}
