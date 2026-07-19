package com.docibly.dms.bean.core.auditlog;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.annotations.SQLRestriction;
import com.docibly.dms.common.entity.BaseEntity;
import com.docibly.dms.bean.core.enums.AuditAction;

@Entity
@Table(name = "app_auditlog")
@JsonInclude(JsonInclude.Include.NON_NULL)
@SQLRestriction("deleted_at IS NULL")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(of = "id", callSuper = false)
@ToString(of = "id")
public class AuditLog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuditAction action;
    @Column(length = 500)
    private String actorUserId;
    @Column(length = 500)
    private String actorEmail;
    @Column(length = 500)
    private String targetEntityType;
    @Column(length = 500)
    private String targetEntityId;
    private Long organizationId;
    @Column(length = 500)
    private String metadata;
    @Column(length = 500)
    private String ipAddress;
    @Column(length = 500)
    private String userAgent;


}

