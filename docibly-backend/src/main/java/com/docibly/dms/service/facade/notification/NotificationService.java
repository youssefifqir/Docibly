package com.docibly.dms.service.facade.notification;

import com.docibly.dms.bean.core.notification.Notification;
import com.docibly.dms.dao.criteria.core.notification.NotificationCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import java.util.List;
import java.util.Optional;

@Validated
public interface NotificationService {

    Notification create(Notification t);
    Notification update(Notification t);
    List<Notification> update(List<Notification> ts, boolean createIfNotExist);
    Optional<Notification> findById(Long id);
    Notification save(Notification entity);
    void deleteById(Long id);
    Optional<Notification> findAndDeleteById(Long id);
    Notification findOrSave(Notification t);
    Notification findByReferenceEntity(Notification t);
    Notification findWithAssociatedLists(Long id);
    List<Notification> findAll();
    List<Notification> findByCriteria(NotificationCriteria criteria);
    Page<Notification> findPaginatedByCriteria(NotificationCriteria criteria, Pageable pageable);
    int getDataSize(NotificationCriteria criteria);
    List<Notification> delete(List<Notification> ts);
    Notification findByRef(String ref);
}

