package com.docibly.dms.bean.core.searchindex;

import com.docibly.dms.bean.core.enums.DocumentVisibility;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "documents")
public class DocumentIndex {

    @Id
    private Long documentId;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String title;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String description;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String content;

    @Field(type = FieldType.Keyword)
    private String originalFilename;

    @Field(type = FieldType.Keyword)
    private String mimeType;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String tags;

    @Field(type = FieldType.Long)
    private Long organizationId;

    @Field(type = FieldType.Keyword)
    private DocumentVisibility visibility;

    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second_millis)
    private LocalDateTime createdDate;

    @Field(type = FieldType.Keyword)
    private String ownerId;
}
