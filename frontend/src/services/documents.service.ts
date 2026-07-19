import api from './api';
import type { DocumentDto, DocumentVersionDto, PageResponse } from '@/types/api';

export const documentService = {
  async findAll(page = 0, size = 20, sortBy = 'createdDate', sortDir = 'desc'): Promise<PageResponse<DocumentDto>> {
    const res = await api.get('/user/documents', { params: { page, size, sortBy, sortDir } });
    return res.data;
  },

  async findById(id: number): Promise<DocumentDto> {
    const res = await api.get(`/user/documents/${id}`);
    return res.data;
  },

  async upload(
    file: File,
    title: string,
    description?: string,
    folderId?: number,
    tagIds?: number[],
    digitizeFormat = 'NONE',
  ): Promise<DocumentDto> {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('title', title);
    if (description) formData.append('description', description);
    if (folderId != null) formData.append('folderId', folderId.toString());
    if (tagIds?.length) tagIds.forEach(id => formData.append('tagIds', id.toString()));
    formData.append('digitizeFormat', digitizeFormat);
    const res = await api.post('/user/documents/upload', formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    });
    return res.data;
  },

  async update(id: number, data: Partial<DocumentDto>): Promise<DocumentDto> {
    const res = await api.put(`/user/documents/${id}`, data);
    return res.data;
  },

  async delete(id: number): Promise<void> {
    await api.delete(`/user/documents/${id}`);
  },

  async download(id: number): Promise<Blob> {
    const res = await api.get(`/user/documents/${id}/download`, { responseType: 'blob' });
    return res.data;
  },

  async getPresignedUrl(id: number): Promise<string> {
    const res = await api.get(`/user/documents/${id}/presigned-url`);
    return res.data;
  },

  async getVersions(documentId: number): Promise<DocumentVersionDto[]> {
    const res = await api.get(`/user/documentversions/by-document/${documentId}`);
    return res.data;
  },

  async reupload(documentId: number, file: File, label?: string, changeNote?: string): Promise<DocumentVersionDto> {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('documentId', documentId.toString());
    if (label) formData.append('label', label);
    if (changeNote) formData.append('changeNote', changeNote);
    const res = await api.post('/user/documentversions/reupload', formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    });
    return res.data;
  },
};
