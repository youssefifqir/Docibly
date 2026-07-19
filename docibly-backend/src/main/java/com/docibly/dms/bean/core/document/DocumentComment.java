package com.docibly.dms.bean.core.document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.LinkedHashSet;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.annotations.SQLRestriction;
import com.docibly.dms.common.entity.BaseEntity;
import com.docibly.dms.bean.core.document.Document;
import com.docibly.dms.bean.core.document.DocumentComment;
import com.docibly.dms.bean.core.document.DocumentComment;
import com.docibly.dms.bean.core.user.User;

@Entity
@Table(name = "app_documentcomment")
@JsonInclude(JsonInclude.Include.NON_NULL)
@SQLRestriction("deleted_at IS NULL")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(of = "id", callSuper = false)
@ToString(of = "id")
public class DocumentComment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500, nullable = false)
    private String content;
    private Boolean isResolved;
    private LocalDateTime resolvedAt;
    private Integer pageNumber;
    @Builder.Default
    private BigDecimal positionX = BigDecimal.ZERO;
    @Builder.Default
    private BigDecimal positionY = BigDecimal.ZERO;
    private Boolean isEdited;
    private LocalDateTime editedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "document_id", nullable = false)
    private Document document;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private DocumentComment parentComment;
     @OneToMany(mappedBy = "parentComment", cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.LAZY)
     @Builder.Default
     private Set<DocumentComment> replies = new LinkedHashSet<>();
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_user_id", nullable = false)
    private User author;

}

