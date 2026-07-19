package com.docibly.dms.bean.core.document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.LinkedHashSet;
import java.util.HashSet;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.annotations.SQLRestriction;
import com.docibly.dms.common.entity.BaseEntity;
import com.docibly.dms.bean.core.folder.Folder;
import com.docibly.dms.bean.core.organization.Organization;
import com.docibly.dms.bean.core.document.DocumentVersion;
import com.docibly.dms.bean.core.document.DocumentShare;
import com.docibly.dms.bean.core.document.DocumentComment;
import com.docibly.dms.bean.core.tag.Tag;
import com.docibly.dms.bean.core.enums.DigitizeFormat;
import com.docibly.dms.bean.core.enums.DocumentStatus;
import com.docibly.dms.bean.core.enums.DocumentVisibility;
import com.docibly.dms.bean.core.enums.OcrStatus;

@Entity
@Table(name = "app_document")
@JsonInclude(JsonInclude.Include.NON_NULL)
@SQLRestriction("deleted_at IS NULL")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(of = "id", callSuper = false)
@ToString(of = "id")
public class Document extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500, nullable = false)
    private String title;
    @Column(length = 500)
    private String description;
    @Column(length = 500, nullable = false)
    private String originalFilename;
    @Column(length = 500, nullable = false, unique = true)
    private String storedFilename;
    @Column(length = 500, nullable = false)
    private String mimeType;
    @Column(nullable = false)
    private Long fileSizeBytes;
    @Column(length = 500, nullable = false)
    private String storageBucket;
    @Column(length = 500, nullable = false, unique = true)
    private String storageKey;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DocumentStatus status;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DocumentVisibility visibility;
    private Integer currentVersionNumber;
    private Integer downloadCount;
    private Integer viewCount;
    private Boolean isPasswordProtected;
    @Column(length = 500)
    private String passwordHash;
    private LocalDateTime expiresAt;
    private LocalDateTime archivedAt;
    @Enumerated(EnumType.STRING)
    private DigitizeFormat digitizeFormat;
    @Column(length = 500)
    private String checksum;
    @Enumerated(EnumType.STRING)
    private OcrStatus ocrStatus;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String ocrText;
    private LocalDateTime ocrProcessedAt;
    @Column(length = 500)
    private String ocrLanguage;
    @Builder.Default
    private BigDecimal ocrConfidenceScore = BigDecimal.ZERO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "folder_id")
    private Folder folder;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;
     @OneToMany(mappedBy = "document", cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.LAZY)
     @Builder.Default
     private Set<DocumentVersion> versions = new LinkedHashSet<>();
     @OneToMany(mappedBy = "document", cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.LAZY)
     @Builder.Default
     private Set<DocumentShare> shares = new LinkedHashSet<>();
     @OneToMany(mappedBy = "document", cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.LAZY)
     @Builder.Default
     private Set<DocumentComment> comments = new LinkedHashSet<>();
    @ManyToMany
    @JoinTable(
        name = "app_document_tags",
        joinColumns = @JoinColumn(name = "document_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
     @Builder.Default
     private Set<Tag> tags = new HashSet<>();

}

