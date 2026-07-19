package com.docibly.dms.bean.core.document;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.annotations.SQLRestriction;
import com.docibly.dms.common.entity.BaseEntity;
import com.docibly.dms.bean.core.document.Document;
import com.docibly.dms.bean.core.user.User;
import com.docibly.dms.bean.core.user.User;
import com.docibly.dms.bean.core.enums.SharePermission;

@Entity
@Table(name = "app_documentshare")
@JsonInclude(JsonInclude.Include.NON_NULL)
@SQLRestriction("deleted_at IS NULL")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(of = "id", callSuper = false)
@ToString(of = "id")
public class DocumentShare extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500, nullable = false, unique = true)
    private String shareToken;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SharePermission permission;
    @Column(length = 500)
    private String sharedWithEmail;
    private Boolean isPublicLink;
    private LocalDateTime expiresAt;
    private Boolean isRevoked;
    private LocalDateTime revokedAt;
    private Integer accessCount;
    private LocalDateTime lastAccessedAt;
    private Boolean requiresPassword;
    @Column(length = 500)
    private String passwordHash;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "document_id", nullable = false)
    private Document document;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "shared_by_user_id", nullable = false)
    private User sharedBy;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shared_with_user_id")
    private User sharedWith;

}

