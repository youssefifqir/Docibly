package com.docibly.dms.dao.facade.core.notification;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import com.docibly.dms.bean.core.notification.Notification;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationDao extends JpaRepository<Notification, Long>, JpaSpecificationExecutor<Notification> {

    Notification findByRef(String ref);
    int deleteByRef(String ref);

    @EntityGraph(attributePaths = {"recipient"})
    @Override
    Page<Notification> findAll(@Nullable Specification<Notification> spec, Pageable pageable);

    @EntityGraph(attributePaths = {"recipient"})
    @Query("SELECT e FROM Notification e WHERE e.id = :id")
    Optional<Notification> findWithAssociationsById(@Param("id") Long id);

    List<Notification> findByRecipient_IdOrderByCreatedDateDesc(String recipientId);

    long countByRecipient_IdAndReadFalse(String recipientId);
}

