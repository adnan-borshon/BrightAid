
import { useState } from "react";
import { Link } from "react-router-dom";
import { Check, Upload } from "lucide-react";
const ApprovalPage = () => {
  return (
    <div className="w-full max-w-md">
      <div className="text-center mb-8">
        <div className="flex items-center justify-center mb-6">
          <div className="w-8 h-8 bg-green-600 rounded-full flex items-center justify-center mr-3">
            <div className="w-4 h-4 bg-white rounded-full"></div>
          </div>
          <span className="text-2xl font-bold text-gray-900">BrightAid <span className="font-light">Institute</span></span>
        </div>
      </div>
      
      <div className="bg-white rounded-2xl shadow-lg p-8 text-center">
        <div className="w-16 h-16 bg-green-100 rounded-full flex items-center justify-center mx-auto mb-6">
          <Check className="w-8 h-8 text-green-600" />
        </div>
        
        <h2 className="text-2xl font-bold text-gray-900 mb-4">You're Registration Has been Approved</h2>
        <p className="text-gray-600 mb-8">Thank you for registering!</p>
        
        <button className="w-full bg-green-600 text-white py-3 px-4 rounded-lg font-medium hover:bg-green-700 transition-colors flex items-center justify-center">
          Explore Dashboard
          <span className="ml-2">→</span>
        </button>
      </div>
    </div>
  );
};
export default ApprovalPage;