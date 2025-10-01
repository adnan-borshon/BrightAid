import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { createBrowserRouter, RouterProvider } from 'react-router-dom'
import './index.css'
import App from './App.jsx'
import SignUp from './components/Authentication/Signup/SignUp.jsx'
import SignUp1 from './components/Authentication/SignUp1.jsx'
import SchoolProfilePage from './components/Authentication/Signup/SchoolProfilePage.jsx'
import DocumentVerificationPage from './components/Authentication/Signup/DocumentVerificationPage.jsx'
import ApprovalPage from './components/Authentication/Signup/ApprovalPage.jsx'
import SchoolDashboard from './components/SchoolDashboard'
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
    path: "/donor-dashboard/:donorId", 
    element: <div className="p-8 text-center"><h1 className="text-2xl font-bold">Donor Dashboard</h1><p className="text-gray-600 mt-2">Coming Soon...</p></div> 
  },
  { 
    path: "/ngo-dashboard/:ngoId", 
    element: <div className="p-8 text-center"><h1 className="text-2xl font-bold">NGO Dashboard</h1><p className="text-gray-600 mt-2">Coming Soon...</p></div> 
  },
  { 
    path: "/admin-dashboard/:adminId", 
    element: <div className="p-8 text-center"><h1 className="text-2xl font-bold">Admin Dashboard</h1><p className="text-gray-600 mt-2">Coming Soon...</p></div> 
  },
  { 
    path: "/donor-profile", 
    element: <div className="p-8 text-center"><h1 className="text-2xl font-bold">Complete Donor Profile</h1><p className="text-gray-600 mt-2">Coming Soon...</p></div> 
  },
  { 
    path: "/ngo-profile", 
    element: <div className="p-8 text-center"><h1 className="text-2xl font-bold">Complete NGO Profile</h1><p className="text-gray-600 mt-2">Coming Soon...</p></div> 
  },
])
createRoot(document.getElementById('root')).render(
  <StrictMode>
    <RouterProvider router={router} />
  </StrictMode>,
);
