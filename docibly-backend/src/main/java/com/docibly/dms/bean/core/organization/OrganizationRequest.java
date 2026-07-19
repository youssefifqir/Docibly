package com.docibly.dms.bean.core.organization;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.annotations.SQLRestriction;
import com.docibly.dms.common.entity.BaseEntity;
import com.docibly.dms.bean.core.user.User;
import com.docibly.dms.bean.core.user.User;
import com.docibly.dms.bean.core.enums.OrgRequestStatus;

@Entity
@Table(name = "app_organizationrequest")
@JsonInclude(JsonInclude.Include.NON_NULL)
@SQLRestriction("deleted_at IS NULL")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(of = "id", callSuper = false)
@ToString(of = "id")
public class OrganizationRequest extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500, nullable = false)
    private String requestedName;
    @Column(length = 500, nullable = false)
    private String requestedSlug;
    @Column(length = 500)
    private String description;
    @Column(length = 500)
    private String intendedUse;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrgRequestStatus status;
    private LocalDateTime reviewedAt;
    @Column(length = 500)
    private String rejectionReason;
    private Long createdOrganizationId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "requested_by_user_id", nullable = false)
    private User requestedBy;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewed_by_user_id")
    private User reviewedBy;

}

