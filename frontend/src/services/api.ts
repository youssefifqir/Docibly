import axios from 'axios';

declare module 'axios' {
  export interface AxiosRequestConfig {
    suppressErrorToast?: boolean;
  }
}

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api/v1';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: { 'Content-Type': 'application/json' },
});

api.interceptors.request.use((config) => {
  const raw = localStorage.getItem('ged_user');
  if (raw) {
    try {
      const parsed = JSON.parse(raw);
      if (parsed.accessToken) {
        config.headers.Authorization = `Bearer ${parsed.accessToken}`;
      }
      if (parsed.orgId) {
        config.headers['X-Org-Id'] = parsed.orgId;
      }
    } catch {}
  }
  return config;
});

api.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;

    // Backend's JwtFilter never sets auth for a missing/expired/invalid token and
    // falls through to Spring Security's default entry point, which returns 403 —
    // not 401. Treat both as "session needs refresh or re-login".
    // But explicit permission/access denials must NOT trigger a token refresh loop.
    const denialCodes = new Set(['DEPT_PERMISSION_DENIED', 'ORG_ACCESS_DENIED', 'ORG_MEMBER_NOT_FOUND'])
    const errorCode = error.response?.data?.code as string | undefined
    if ((error.response?.status === 401 || error.response?.status === 403)
        && !originalRequest._retry
        && !denialCodes.has(errorCode ?? '')) {
      originalRequest._retry = true;
      const raw = localStorage.getItem('ged_user');
      const parsed = raw ? (() => { try { return JSON.parse(raw); } catch { return null; } })() : null;

      if (parsed?.refreshToken) {
        try {
          const res = await axios.post(`${API_BASE_URL}/auth/refresh`, {
            refreshToken: parsed.refreshToken,
          });
          const accessToken = res.data.access_token || res.data.accessToken;
          localStorage.setItem('ged_user', JSON.stringify({ ...parsed, accessToken }));
          originalRequest.headers.Authorization = `Bearer ${accessToken}`;
          return api(originalRequest);
        } catch {
          localStorage.removeItem('ged_user');
          localStorage.removeItem('ged_org_id');
          window.location.href = '/login';
          return Promise.reject(error);
        }
      } else if (parsed) {
        localStorage.removeItem('ged_user');
        localStorage.removeItem('ged_org_id');
        window.location.href = '/login';
        return Promise.reject(error);
      }
    }

    if (!originalRequest?.suppressErrorToast) {
      const msg = error.response?.data?.message
        || error.response?.data?.error
        || error.message
        || 'Something went wrong';

      window.dispatchEvent(new CustomEvent('toast:error', { detail: msg }));
    }
    return Promise.reject(error);
  }
);

export default api;
export { API_BASE_URL };
