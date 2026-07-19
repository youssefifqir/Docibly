package com.docibly.dms.ws.converter.folder;

import org.springframework.stereotype.Component;
import com.docibly.dms.bean.core.folder.Folder;
import com.docibly.dms.ws.dto.folder.request.CreateFolderRequest;
import com.docibly.dms.ws.dto.folder.request.UpdateFolderRequest;
import com.docibly.dms.ws.dto.folder.response.FolderResponse;

@Component
public class FolderConverter {

    public FolderResponse toResponse(Folder entity) {
        if (entity == null) return null;
        FolderResponse response = new FolderResponse();
        response.setId(entity.getId());
        response.setRef(entity.getRef());
        response.setCreatedDate(entity.getCreatedDate());
        response.setLastModifiedDate(entity.getLastModifiedDate());
        response.setName(entity.getName());
        response.setDescription(entity.getDescription());
        response.setColor(entity.getColor());
        response.setIconName(entity.getIconName());
        response.setIsShared(entity.getIsShared());
        response.setDocumentCount(entity.getDocumentCount());
        response.setTotalSizeBytes(entity.getTotalSizeBytes());
        if (entity.getParentFolder() != null) {
            response.setParentFolderId(entity.getParentFolder().getId());
            response.setParentFolderRef(entity.getParentFolder().getRef());
        }
        if (entity.getOrganization() != null) {
            response.setOrganizationId(entity.getOrganization().getId());
            response.setOrganizationRef(entity.getOrganization().getRef());
        }
        return response;
    }

    public Folder toEntity(CreateFolderRequest request) {
        if (request == null) return null;
        Folder entity = new Folder();
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setColor(request.getColor());
        entity.setIconName(request.getIconName());
        entity.setIsShared(request.getIsShared());
        entity.setDocumentCount(request.getDocumentCount());
        entity.setTotalSizeBytes(request.getTotalSizeBytes());
        return entity;
    }

    public Folder toEntity(UpdateFolderRequest request) {
        if (request == null) return null;
        Folder entity = new Folder();
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setColor(request.getColor());
        entity.setIconName(request.getIconName());
        entity.setIsShared(request.getIsShared());
        entity.setDocumentCount(request.getDocumentCount());
        entity.setTotalSizeBytes(request.getTotalSizeBytes());
        return entity;
    }
}

