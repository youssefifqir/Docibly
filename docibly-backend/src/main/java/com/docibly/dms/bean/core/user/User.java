package com.docibly.dms.bean.core.user;

import com.docibly.dms.bean.core.role.Role;
import com.docibly.dms.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import com.docibly.dms.bean.core.organization.OrganizationRequest;
import com.docibly.dms.bean.core.organization.OrganizationMember;
import com.docibly.dms.bean.core.organization.OrganizationInvitation;
import com.docibly.dms.bean.core.document.DocumentVersion;
import com.docibly.dms.bean.core.document.DocumentShare;
import com.docibly.dms.bean.core.document.DocumentComment;
import com.docibly.dms.bean.core.notification.Notification;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(of = "id", callSuper = false)
@ToString(of = "id")
@Table(name = "users")
public class User extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "is_enabled")
    private boolean enabled;

    @Column(name = "is_locked")
    private boolean locked;

    @Column(name = "is_credentials_expired")
    private boolean credentialsExpired;

    @Column(name = "is_email_verified")
    private boolean emailVerified;

    @Column(name = "is_phone_verified")
    private boolean phoneVerified;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "bio")
    private String bio;

    @Column(name = "job_title")
    private String jobTitle;

    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    @Column(name = "storage_used_bytes")
    private Long storageUsedBytes;

    @Column(name = "storage_quota_bytes")
    private Long storageQuotaBytes;

    @Column(name = "last_seen_at")
    private LocalDateTime lastSeenAt;

    @Column(name = "total_documents_uploaded")
    private Integer totalDocumentsUploaded;

    @Column(name = "is_suspended")
    private Boolean isSuspended;

    @Column(name = "suspended_reason")
    private String suspendedReason;

    @Column(name = "plan_tier")
    private String planTier;

    @Column(name = "ai_credits_used")
    private Integer aiCreditsUsed;

    @Column(name = "ai_credits_reset_date")
    private LocalDate aiCreditsResetDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "auth_provider", nullable = false, length = 50)
    @Builder.Default
    private AuthProvider authProvider = AuthProvider.LOCAL;

    @Column(name = "provider_subject", unique = true)
    private String providerSubject;

    @ManyToMany(
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.EAGER
    )
    @JoinTable(
            name = "users_roles",
            joinColumns = {
                    @JoinColumn(name = "users_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "roles_id")
            }
    )
    private List<Role> roles;

    @OneToMany(mappedBy = "requestedBy", orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<OrganizationRequest> organizationrequests = new ArrayList<>();
    @OneToMany(mappedBy = "user", orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<OrganizationMember> organizationmembers = new ArrayList<>();
    @OneToMany(mappedBy = "invitedBy", orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<OrganizationInvitation> organizationinvitations = new ArrayList<>();
    @OneToMany(mappedBy = "uploadedBy", orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<DocumentVersion> documentversions = new ArrayList<>();
    @OneToMany(mappedBy = "sharedBy", orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<DocumentShare> documentshares = new ArrayList<>();
    @OneToMany(mappedBy = "author", orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<DocumentComment> documentcomments = new ArrayList<>();
    @OneToMany(mappedBy = "recipient", orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Notification> notifications = new ArrayList<>();

    public void addRole(final Role role) {
        this.roles.add(role);
        role.getUsers().add(this);
    }

    public void removeRole(final Role role) {
        this.roles.remove(role);
        role.getUsers().remove(this);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (CollectionUtils.isEmpty(this.roles)) {
            return List.of();
        }
        return this.roles.stream()
                .map(r -> new SimpleGrantedAuthority(r.getName()))
                .toList();
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled && this.emailVerified;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !this.credentialsExpired;
    }

    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }
}
