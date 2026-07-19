import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import { AuthProvider, useAuth } from '@/contexts/AuthContext'
import { ToastProvider } from '@/contexts/ToastContext'
import { AppLayout } from '@/components/layout/AppLayout'
import { AdminLayout } from '@/components/layout/AdminLayout'
import { LandingPage } from '@/pages/LandingPage'
import { LoginPage } from '@/pages/LoginPage'
import { RegisterPage } from '@/pages/RegisterPage'
import { VerifyEmailPage } from '@/pages/VerifyEmailPage'
import { DashboardPage } from '@/pages/DashboardPage'
import { LibraryPage } from '@/pages/LibraryPage'
import { UploadPage } from '@/pages/UploadPage'
import { SharedPage } from '@/pages/SharedPage'
import { SearchPage } from '@/pages/SearchPage'
import { DocumentDetailPage } from '@/pages/DocumentDetailPage'
import { PublicSharePage } from '@/pages/PublicSharePage'
import { ProfilePage } from '@/pages/ProfilePage'
import { DigitizationPage } from '@/pages/DigitizationPage'
import { TagsPage } from '@/pages/TagsPage'
import { AdminEntitiesPage } from '@/pages/AdminEntitiesPage'
import { AcceptInvitationPage } from '@/pages/AcceptInvitationPage'
import { DepartmentsPage } from '@/pages/DepartmentsPage'
import { AuditLogPage } from '@/pages/AuditLogPage'
import { OrgCreatePage } from '@/pages/OrgCreatePage'
import { OrgSelectPage } from '@/pages/OrgSelectPage'
import { AdminTenantsPage } from '@/pages/admin/TenantsPage'
import { AdminTenantDetailPage } from '@/pages/admin/TenantDetailPage'
import { AdminUsersPage } from '@/pages/admin/UsersPage'

function ProtectedRoute({ children }: { children: React.ReactNode }) {
  const { isAuthenticated } = useAuth()
  if (!isAuthenticated) return <Navigate to="/login" replace />
  return <>{children}</>
}

function OrgGuard({ children }: { children: React.ReactNode }) {
  const { orgId, authReady, orgs } = useAuth()
  if (!authReady) return null
  if (!orgId) {
    if (orgs.length > 0) return <Navigate to="/org/select" replace />
    return <Navigate to="/org/create" replace />
  }
  return <>{children}</>
}

function PublicRoute({ children }: { children: React.ReactNode }) {
  const { isAuthenticated } = useAuth()
  return isAuthenticated ? <Navigate to="/dashboard" replace /> : <>{children}</>
}

function LandingRoute({ children }: { children: React.ReactNode }) {
  const { isAuthenticated } = useAuth()
  return isAuthenticated ? <Navigate to="/dashboard" replace /> : <>{children}</>
}

function AdminRoute({ children }: { children: React.ReactNode }) {
  const { user, isAuthenticated } = useAuth()
  if (!isAuthenticated) return <Navigate to="/login" replace />
  if (user?.role !== 'ROLE_ADMIN') return <Navigate to="/dashboard" replace />
  return <>{children}</>
}

function AppRoutes() {
  return (
    <Routes>
      <Route path="/" element={<LandingRoute><LandingPage /></LandingRoute>} />
      <Route path="/login" element={<PublicRoute><LoginPage /></PublicRoute>} />
      <Route path="/register" element={<PublicRoute><RegisterPage /></PublicRoute>} />
      <Route path="/verify-email" element={<VerifyEmailPage />} />
      <Route path="/org/create" element={<ProtectedRoute><OrgCreatePage /></ProtectedRoute>} />
      <Route path="/org/select" element={<ProtectedRoute><OrgSelectPage /></ProtectedRoute>} />
      <Route path="/share/:token" element={<PublicSharePage />} />
      <Route path="/invite/:token" element={<AcceptInvitationPage />} />

      <Route element={<AdminRoute><AdminLayout /></AdminRoute>}>
        <Route path="admin/tenants" element={<AdminTenantsPage />} />
        <Route path="admin/tenants/:id" element={<AdminTenantDetailPage />} />
        <Route path="admin/users" element={<AdminUsersPage />} />
      </Route>

      <Route element={<ProtectedRoute><OrgGuard><AppLayout /></OrgGuard></ProtectedRoute>}>
        <Route path="dashboard" element={<DashboardPage />} />
        <Route path="library" element={<LibraryPage />} />
        <Route path="upload" element={<UploadPage />} />
        <Route path="shared" element={<SharedPage />} />
        <Route path="search" element={<SearchPage />} />
        <Route path="digitize" element={<DigitizationPage />} />
        <Route path="tags" element={<TagsPage />} />
        <Route path="departments" element={<AdminEntitiesPage />} />
        <Route path="members" element={<AdminEntitiesPage />} />
        <Route path="roles" element={<AdminEntitiesPage />} />
        <Route path="preview/:id" element={<DocumentDetailPage />} />
        <Route path="audit" element={<AuditLogPage />} />
        <Route path="profile" element={<ProfilePage />} />
      </Route>
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  )
}

export default function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <ToastProvider>
          <AppRoutes />
        </ToastProvider>
      </AuthProvider>
    </BrowserRouter>
  )
}
