import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { createBrowserRouter, RouterProvider } from 'react-router-dom'
import './index.css'
import App from './App.jsx'
import { AppProvider } from './context/AppContext.jsx'
import { DonorProvider } from './context/DonorContext.jsx'
import SignUp from './components/Authentication/Signup/SignUp.jsx'
import SignUp1 from './components/Authentication/SignUp1.jsx'
import SchoolProfilePage from './components/Authentication/Signup/SchoolProfilePage.jsx'
import DocumentVerificationPage from './components/Authentication/Signup/DocumentVerificationPage.jsx'
import ApprovalPage from './components/Authentication/Signup/ApprovalPage.jsx'
import SchoolDashboard from './components/SchoolDashboard'
import SchoolStudents from './components/SchoolStudents'
import SchoolProjects from './components/SchoolProjects'
import ProjectDetails from './components/ProjectDetails'
import DonorDashboard from './components/DonorDashboard'
import DonorProjectView from './components/DonorProjectView'
import DonorStudentsView from './components/DonorStudentsView'
import Reporting from './components/Reporting'
import NgoDashboard from './components/NgoDashboard'
import DonorProfile from './components/DonorProfile'
import NgoProfile from './components/NgoProfile'
import Login from './components/Authentication/Login'


const router = createBrowserRouter([
  { 
    path: "/", 
    element: <App /> 
  },
    { 
    path: "/login", 
    element: <Login /> 
  },
  { 
    path: "/signup1", 
    element: <SignUp1 /> 
  },

  { 
    path: "/signup", 
    element: <SignUp /> 
  },
  { 
    path: "/school-profile", 
    element: <SignUp /> 
  },
  { 
    path: "/document-verification", 
    element: <SignUp /> 
  },
  { 
    path: "/approval", 
    element: <SignUp /> 
  },
  { 
    path: "/dashboard/:schoolId", 
    element: <SchoolDashboard /> 
  },
  { 
    path: "/students/:schoolId", 
    element: <SchoolStudents /> 
  },
  { 
    path: "/projects/:schoolId", 
    element: <SchoolProjects /> 
  },
  { 
    path: "/project-details/:projectId", 
    element: <ProjectDetails /> 
  },
  { 
    path: "/reporting/:schoolId", 
    element: <div className="p-8 text-center"><h1 className="text-2xl font-bold">Reporting</h1><p className="text-gray-600 mt-2">Coming Soon...</p></div> 
  },
  { 
    path: "/account/:schoolId", 
    element: <div className="p-8 text-center"><h1 className="text-2xl font-bold">Account</h1><p className="text-gray-600 mt-2">Coming Soon...</p></div> 
  },
  { 
    path: "/support/:schoolId", 
    element: <div className="p-8 text-center"><h1 className="text-2xl font-bold">Support</h1><p className="text-gray-600 mt-2">Coming Soon...</p></div> 
  },
  { 
    path: "/settings/:schoolId", 
    element: <div className="p-8 text-center"><h1 className="text-2xl font-bold">Settings</h1><p className="text-gray-600 mt-2">Coming Soon...</p></div> 
  },
  { 
    path: "/donor-dashboard/:donorId", 
    element: <DonorDashboard /> 
  },
  { 
    path: "/donor-projects/:donorId", 
    element: <DonorProjectView /> 
  },
  { 
    path: "/donor-students/:donorId", 
    element: <DonorStudentsView /> 
  },
  { 
    path: "/donor-reporting/:donorId", 
    element: <Reporting /> 
  },
  { 
    path: "/ngo-dashboard/:ngoId", 
    element: <NgoDashboard /> 
  },
  { 
    path: "/admin-dashboard/:adminId", 
    element: <div className="p-8 text-center"><h1 className="text-2xl font-bold">Admin Dashboard</h1><p className="text-gray-600 mt-2">Coming Soon...</p></div> 
  },
  { 
    path: "/donor-profile", 
    element: <DonorProfile /> 
  },
  { 
    path: "/ngo-profile", 
    element: <NgoProfile /> 
  },
])
createRoot(document.getElementById('root')).render(
  <StrictMode>
    <AppProvider>
      <DonorProvider>
        <RouterProvider router={router} />
      </DonorProvider>
    </AppProvider>
  </StrictMode>,
);
