import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { createBrowserRouter, RouterProvider } from 'react-router-dom'
import './index.css'
import App from './App.jsx'
import { AppProvider } from './context/AppContext.jsx'
import { DonorProvider } from './context/DonorContext.jsx'
import { NgoProvider } from './context/NgoContext.jsx'
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
import DonorReporting from './components/DonorReporting'
import SchoolReporting from './components/SchoolReporting'
import NgoDashboard from './components/NgoDashboard'
import NgoProjects from './components/NgoProjects'
import NgoDonations from './components/NgoDonations'
import NgoReporting from './components/NgoReporting'
import NgoDonationHistory from './components/NgoDonationHistory'
import NgoGamification from './components/NgoGamification'
import DonorProfile from './components/DonorProfile'
import NgoProfile from './components/NgoProfile'
import DonorDonations from './components/DonorDonations'
import DonorGamification from './components/DonorGamification'
import Login from './components/Authentication/Login'
import PaymentSuccess from './components/PaymentSuccess'
import PaymentClose from './components/PaymentClose'
import StudentProfile from './components/StudentProfile'


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
    path: "/student-profile/:schoolId/:studentId", 
    element: <StudentProfile /> 
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
    element: <SchoolReporting /> 
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
    path: "/donor-dashboard/:id", 
    element: <DonorDashboard /> 
  },
  { 
    path: "/donor-projects/:id", 
    element: <DonorProjectView /> 
  },
  { 
    path: "/donor-students/:id", 
    element: <DonorStudentsView /> 
  },
  { 
    path: "/donor-donations/:id", 
    element: <DonorDonations /> 
  },
  { 
    path: "/donor-gamification/:id", 
    element: <DonorGamification /> 
  },
  { 
    path: "/donor-reporting/:id", 
    element: <DonorReporting /> 
  },
  { 
    path: "/ngo-dashboard/:ngoId", 
    element: <NgoDashboard /> 
  },
  { 
    path: "/ngo-projects/:ngoId", 
    element: <NgoProjects /> 
  },
  { 
    path: "/ngo-donations/:ngoId", 
    element: <NgoDonations /> 
  },
  { 
    path: "/ngo-reporting/:ngoId", 
    element: <NgoReporting /> 
  },
  { 
    path: "/ngo-gamification/:ngoId", 
    element: <NgoGamification /> 
  },
  { 
    path: "/ngo-account/:ngoId", 
    element: <div className="p-8 text-center"><h1 className="text-2xl font-bold">NGO Account</h1><p className="text-gray-600 mt-2">Coming Soon...</p></div> 
  },
  { 
    path: "/ngo-support/:ngoId", 
    element: <div className="p-8 text-center"><h1 className="text-2xl font-bold">NGO Support</h1><p className="text-gray-600 mt-2">Coming Soon...</p></div> 
  },
  { 
    path: "/ngo-settings/:ngoId", 
    element: <div className="p-8 text-center"><h1 className="text-2xl font-bold">NGO Settings</h1><p className="text-gray-600 mt-2">Coming Soon...</p></div> 
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
  { 
    path: "/payment-success", 
    element: <PaymentSuccess /> 
  },
  { 
    path: "/payment-close", 
    element: <PaymentClose /> 
  },
])
createRoot(document.getElementById('root')).render(
  <StrictMode>
    <NgoProvider>
      <DonorProvider>
        <AppProvider>
          <RouterProvider router={router} />
        </AppProvider>
      </DonorProvider>
    </NgoProvider>
  </StrictMode>,
);
