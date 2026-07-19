package com.docibly.dms.bean.core.organization;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.LinkedHashSet;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.annotations.SQLRestriction;
import com.docibly.dms.common.entity.BaseEntity;
import com.docibly.dms.bean.core.organization.OrganizationMember;
import com.docibly.dms.bean.core.organization.OrganizationInvitation;
import com.docibly.dms.bean.core.folder.Folder;
import com.docibly.dms.bean.core.document.Document;
import com.docibly.dms.bean.core.tag.Tag;
import com.docibly.dms.bean.core.department.Department;

@Entity
@Table(name = "app_organization")
@JsonInclude(JsonInclude.Include.NON_NULL)
@SQLRestriction("deleted_at IS NULL")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(of = "id", callSuper = false)
@ToString(of = "id")
public class Organization extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500, nullable = false)
    private String name;
    @Column(length = 500, nullable = false, unique = true)
    private String slug;
    @Column(length = 500)
    private String description;
    @Column(length = 500)
    private String logoUrl;
    @Column(length = 500)
    private String website;
    private Long storageUsedBytes;
    private Long storageQuotaBytes;
    private Integer maxMembers;
    private Boolean isActive;
    @Column(length = 500)
    private String billingEmail;
    @Column(length = 500)
    private String planTier;
    private LocalDateTime trialEndsAt;

     @OneToMany(mappedBy = "organization", orphanRemoval = true, fetch = FetchType.LAZY)
     @Builder.Default
     private Set<OrganizationMember> organizationmembers = new LinkedHashSet<>();
     @OneToMany(mappedBy = "organization", orphanRemoval = true, fetch = FetchType.LAZY)
     @Builder.Default
     private Set<OrganizationInvitation> organizationinvitations = new LinkedHashSet<>();
     @OneToMany(mappedBy = "organization", orphanRemoval = true, fetch = FetchType.LAZY)
     @Builder.Default
     private Set<Folder> folders = new LinkedHashSet<>();
     @OneToMany(mappedBy = "organization", orphanRemoval = true, fetch = FetchType.LAZY)
     @Builder.Default
     private Set<Document> documents = new LinkedHashSet<>();
     @OneToMany(mappedBy = "organization", orphanRemoval = true, fetch = FetchType.LAZY)
     @Builder.Default
     private Set<Tag> tags = new LinkedHashSet<>();
     @OneToMany(mappedBy = "organization", orphanRemoval = true, fetch = FetchType.LAZY)
     @Builder.Default
     private Set<Department> departments = new LinkedHashSet<>();

}

