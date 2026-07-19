package com.docibly.dms.bean.core.notification;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.annotations.SQLRestriction;
import com.docibly.dms.common.entity.BaseEntity;
import com.docibly.dms.bean.core.user.User;
import com.docibly.dms.bean.core.enums.NotificationType;

@Entity
@Table(name = "app_notification")
@JsonInclude(JsonInclude.Include.NON_NULL)
@SQLRestriction("deleted_at IS NULL")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(of = "id", callSuper = false)
@ToString(of = "id")
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;
    @Column(length = 500, nullable = false)
    private String title;
    @Column(length = 500, nullable = false)
    private String message;
    private Boolean read;
    private LocalDateTime readAt;
    @Column(length = 500)
    private String targetUrl;
    @Column(length = 500)
    private String relatedEntityType;
    @Column(length = 500)
    private String relatedEntityId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recipient_user_id", nullable = false)
    private User recipient;

}

