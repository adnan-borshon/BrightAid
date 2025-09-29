import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { createBrowserRouter, RouterProvider } from 'react-router-dom'
import './index.css'
import App from './App.jsx'
import SignUp from './components/Authentication/Signup/SignUp.jsx'
import Login from './components/Authentication/Login.jsx'
import SchoolProfilePage from './components/Authentication/Signup/SchoolProfilePage.jsx'
import DocumentVerificationPage from './components/Authentication/Signup/DocumentVerificationPage.jsx'
import ApprovalPage from './components/Authentication/Signup/ApprovalPage.jsx'


const router = createBrowserRouter([
  {
    path: "/",
    element: <App />,
    children: [
      { 
        path: "/", 
        element: <App /> 
      },
      { 
        path: "/signup", 
        element: <SignUp/> 
      },
      { 
        path: "/school-profile", 
        element: <SchoolProfilePage /> 
      },
      { 
        path: "/document-verification", 
        element: <DocumentVerificationPage /> 
      },
      { 
        path: "/approval", 
        element: <ApprovalPage /> 
      },
    ],
  }
])
createRoot(document.getElementById('root')).render(
  <StrictMode>
    <RouterProvider router={router} />
  </StrictMode>,
);
