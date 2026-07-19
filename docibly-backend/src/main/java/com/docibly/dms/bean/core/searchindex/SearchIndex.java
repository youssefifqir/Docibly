package com.docibly.dms.bean.core.searchindex;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.annotations.SQLRestriction;
import com.docibly.dms.common.entity.BaseEntity;
import com.docibly.dms.bean.core.enums.DocumentVisibility;

@Entity
@Table(name = "app_searchindex")
@JsonInclude(JsonInclude.Include.NON_NULL)
@SQLRestriction("deleted_at IS NULL")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(of = "id", callSuper = false)
@ToString(of = "id")
public class SearchIndex extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long documentId;
    @Column(length = 500)
    private String documentTitle;
    @Column(columnDefinition = "TEXT")
    private String fullText;
    @Column(columnDefinition = "TEXT")
    private String ocrText;
    @Column(length = 1000)
    private String tags;
    @Column(length = 500)
    private String mimeType;
    private Long organizationId;
    @Column(length = 500)
    private String ownerId;
    @Enumerated(EnumType.STRING)
    private DocumentVisibility visibility;
    private LocalDateTime indexedAt;


}

