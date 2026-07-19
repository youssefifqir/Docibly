package com.docibly.dms.bean.core.document;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.annotations.SQLRestriction;
import com.docibly.dms.common.entity.BaseEntity;
import com.docibly.dms.bean.core.document.Document;
import com.docibly.dms.bean.core.user.User;
import com.docibly.dms.bean.core.enums.OcrStatus;

@Entity
@Table(name = "app_documentversion")
@JsonInclude(JsonInclude.Include.NON_NULL)
@SQLRestriction("deleted_at IS NULL")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(of = "id", callSuper = false)
@ToString(of = "id")
public class DocumentVersion extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer versionNumber;
    @Column(length = 500)
    private String label;
    @Column(length = 500)
    private String changeNote;
    @Column(length = 500, nullable = false)
    private String originalFilename;
    @Column(length = 500, nullable = false, unique = true)
    private String storedFilename;
    @Column(length = 500, nullable = false, unique = true)
    private String storageKey;
    @Column(nullable = false)
    private Long fileSizeBytes;
    @Column(length = 500, nullable = false)
    private String mimeType;
    @Column(length = 500)
    private String checksum;
    private Boolean isCurrentVersion;
    @Enumerated(EnumType.STRING)
    private OcrStatus ocrStatus;
    @Column(length = 500)
    private String ocrText;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "document_id", nullable = false)
    private Document document;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "uploaded_by_user_id", nullable = false)
    private User uploadedBy;

}

