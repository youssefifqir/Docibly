package com.docibly.dms.bean.core.folder;

import java.util.Set;
import java.util.LinkedHashSet;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.annotations.SQLRestriction;
import com.docibly.dms.common.entity.BaseEntity;
import com.docibly.dms.bean.core.folder.Folder;
import com.docibly.dms.bean.core.folder.Folder;
import com.docibly.dms.bean.core.organization.Organization;
import com.docibly.dms.bean.core.document.Document;

@Entity
@Table(name = "app_folder")
@JsonInclude(JsonInclude.Include.NON_NULL)
@SQLRestriction("deleted_at IS NULL")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(of = "id", callSuper = false)
@ToString(of = "id")
public class Folder extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500, nullable = false)
    private String name;
    @Column(length = 500)
    private String description;
    @Column(length = 500)
    private String color;
    @Column(length = 500)
    private String iconName;
    private Boolean isShared;
    private Integer documentCount;
    private Long totalSizeBytes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_folder_id")
    private Folder parentFolder;
     @OneToMany(mappedBy = "parentFolder", cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.LAZY)
     @Builder.Default
     private Set<Folder> subFolders = new LinkedHashSet<>();
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;
     @OneToMany(mappedBy = "folder", cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.LAZY)
     @Builder.Default
     private Set<Document> documents = new LinkedHashSet<>();

}

