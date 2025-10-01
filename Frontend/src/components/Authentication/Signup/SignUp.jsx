import React from "react";
import { useLocation } from "react-router-dom";
import Sidebar from "./SideBar.jsx";
import RegistrationPage from "./RegistrationPage";
import SchoolProfilePage from "./SchoolProfilePage";
import DocumentVerificationPage from "./DocumentVerificationPage";
import ApprovalPage from "./ApprovalPage";

const SignUp = () => {
  const location = useLocation();
  const currentPath = location.pathname;

  const renderPage = () => {
    switch (currentPath) {
      case "/school-profile":
        return <SchoolProfilePage />;
      case "/document-verification":
        return <DocumentVerificationPage />;
      case "/approval":
        return <ApprovalPage />;
      default: 
        return <RegistrationPage />;
    }
  };

  return (
    <div className="min-h-screen bg-gray-50 flex">
      {/* ✅ pass currentPath to Sidebar */}
      <Sidebar currentPath={currentPath} />
      <div className="flex-1 flex items-center justify-center p-8">
        {renderPage()}
      </div>
    </div>
  );
};

export default SignUp;
