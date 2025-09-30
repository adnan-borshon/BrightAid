import { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import { Check, Delete, DeleteIcon, Trash, Trash2, TrashIcon, Upload, X } from "lucide-react";

const DocumentVerificationPage = () => {
  const [uploadedFiles, setUploadedFiles] = useState({});
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [schoolData, setSchoolData] = useState(null);
  const [userData, setUserData] = useState(null);
  const navigate = useNavigate(); 

  const documents = [
    {
      id: "schoolRegistration",
      title: "School Registration Certificate",
      required: true,
      icon: Check,
      color: "text-green-600",
    },
    {
      id: "headmasterCertification",
      title: "Headmaster's NID/Verification",
      required: true,
      icon: Check,
      color: "text-green-600",
    },
  ];
  const uploadFields = [
    {
      id: "schoolRegistrationCertificate",
      label: "School Registration Certificate",
      type: "INFRASTRUCTURE",
      size: "2MB",
    },
    {
      id: "headmasterNIDVerification",
      label: "Headmaster's NID/Verification",
      type: "STAFF",
      size: "2MB",
    },
    {
      id: "headmasterElectricityBill",
      label: "Headmaster's Electricity Bill",
      type: "OTHER",
      size: "2MB",
    },
  ];

  useEffect(() => {
    // Get school and user data from storage
    const schoolInfo = sessionStorage.getItem("schoolInfo");
    const userInfo = sessionStorage.getItem("userInfo");
    
    if (schoolInfo) {
      setSchoolData(JSON.parse(schoolInfo));
    }
    
    if (userInfo) {
      setUserData(JSON.parse(userInfo));
    }
    
    // If no school data, redirect back
    if (!schoolInfo) {
      setError("School information not found. Please complete school profile first.");
    }
  }, []);

  // Handle file upload
  const handleFileChange = (e, field) => {
    const file = e.target.files[0];
    if (!file) return;

    // Validate file size (2MB limit)
    if (file.size > 2 * 1024 * 1024) {
      setError(`File ${file.name} is too large. Maximum size is 2MB.`);
      return;
    }

    // create file metadata to match SCHOOL_DOCUMENTS schema
    const fileMeta = {
      documentType: field.type,
      documentTitle: field.label,
      documentDescription: `${field.label} for school verification`,
      fileUrl: `uploads/${file.name}`, // In real app, upload to server first
      uploadDate: new Date().toISOString().split("T")[0],
      uploadedBy: userData?.userId || 1,
      isCurrent: true,
      file,
    };

    setUploadedFiles((prev) => ({
      ...prev,
      [field.id]: fileMeta,
    }));
    
    setError(""); // Clear any previous errors
  };

  // Remove uploaded file
  const handleRemoveFile = (fieldId) => {
    setUploadedFiles((prev) => {
      const newFiles = { ...prev };
      delete newFiles[fieldId];
      return newFiles;
    });
  };

  // Submit documents to API
  const handleSubmit = async () => {
    if (!schoolData || !userData) {
      setError("Missing school or user information. Please try again.");
      return;
    }

    if (Object.keys(uploadedFiles).length === 0) {
      setError("Please upload at least one document before submitting.");
      return;
    }

    setLoading(true);
    setError("");

    try {
      // Upload each document to the API
      const uploadPromises = Object.values(uploadedFiles).map(async (fileData) => {
        const payload = {
          schoolId: schoolData.schoolId,
          documentType: fileData.documentType,
          documentTitle: fileData.documentTitle,
          documentDescription: fileData.documentDescription,
          fileUrl: fileData.fileUrl,
          uploadDate: fileData.uploadDate,
          uploadedBy: fileData.uploadedBy,
          isCurrent: fileData.isCurrent
        };

        const response = await fetch("http://localhost:8081/api/school-documents", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify(payload),
        });

        if (!response.ok) {
          const errorData = await response.json();
          throw new Error(errorData.message || "Failed to upload document");
        }

        return await response.json();
      });

      // Wait for all uploads to complete
      await Promise.all(uploadPromises);
      
      console.log("All documents uploaded successfully");
      
      // Navigate to approval page
      navigate("/approval");
      
    } catch (err) {
      setError(err.message || "Failed to upload documents. Please try again.");
      console.error("Error uploading documents:", err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="w-full max-w-2xl">
      {/* Header */}
      <div className="text-center mb-8">
        <div className="flex items-center justify-center mb-6">
          <div className="w-8 h-8 bg-green-600 rounded-full flex items-center justify-center mr-3">
            <div className="w-4 h-4 bg-white rounded-full"></div>
          </div>
          <span className="text-2xl font-bold text-gray-900">
            BrightAid <span className="font-light">Institute</span>
          </span>
        </div>

        <h2 className="text-2xl font-bold text-gray-900 mb-2">
          Upload Documents for Verification
        </h2>
        <p className="text-gray-600 text-sm">
          Submit official documents to verify your school and ensure platform
          transparency
        </p>
      </div>

      <div className="bg-white rounded-2xl shadow-lg p-8">
        <div className="mb-8">
          <div className="grid grid-cols-1 gap-4">
            {documents.map((doc) => {
              const Icon = doc.icon;
              return (
                <div key={doc.id} className="flex items-center space-x-3">
                  <div className="w-6 h-6 bg-green-100 rounded-full flex items-center justify-center">
                    <Icon className={`w-4 h-4 ${doc.color}`} />
                  </div>
                  <span className="text-sm text-gray-700">{doc.title}</span>
                </div>
              );
            })}
          </div>
        </div>

        {/* Upload Fields */}
        <div className="space-y-4">
          {uploadFields.map((field) => (
            <div
              key={field.id}
              className="border-2 border-dashed border-gray-200 rounded-lg p-4 relative"
            >
              <div className="flex items-center justify-between">
                <div className="flex items-center space-x-3">
                  <div className="w-8 h-8 bg-green-100 rounded-lg flex items-center justify-center">
                    <Upload className="w-4 h-4 text-green-600" />
                  </div>
                  <div>
                    <p className="text-sm font-medium text-gray-700">
                      {field.label}
                    </p>
                    <p className="text-xs text-gray-500">{field.size}</p>
                  </div>
                </div>

                {/* If file is uploaded, show filename */}
                {uploadedFiles[field.id] ? (
                  <div className="flex items-center space-x-2">
                    <span className="text-xs bg-green-100 text-green-700 px-2 py-1 rounded">
                      {uploadedFiles[field.id].file.name}
                    </span>
                    <button
                      onClick={() => handleRemoveFile(field.id)}
                      className="w-7 h-7 bg-red-100 rounded-full flex items-center justify-center"
                    >
                      <Trash2 className="w-3 h-3 text-white" />
                    </button>
                  </div>
                ) : (
                  <label className="cursor-pointer">
                    <input
                      type="file"
                      accept=".pdf,.jpg,.jpeg,.png"
                      className="hidden"
                      onChange={(e) => handleFileChange(e, field)}
                    />
                    <span className="w-6 h-6 bg-gray-100 rounded-full flex items-center justify-center">
                      <Upload className="w-3 h-3 text-gray-600" />
                    </span>
                  </label>
                )}
              </div>
            </div>
          ))}
        </div>

        {/* Error Message */}
        {error && (
          <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">
            {error}
          </div>
        )}

        {/* Submit */}
        <button
          onClick={handleSubmit}
          disabled={loading || Object.keys(uploadedFiles).length === 0}
          className={`w-full py-3 px-4 rounded-lg font-medium transition-colors mt-6 ${
            loading || Object.keys(uploadedFiles).length === 0
              ? "bg-gray-400 cursor-not-allowed"
              : "bg-green-600 hover:bg-green-700"
          } text-white`}
        >
          {loading ? "Uploading Documents..." : "Submit Documents"}
        </button>

        <div className="text-center mt-4">
          <Link to="/" className="text-sm text-gray-500 hover:text-gray-700">
            ← Back to Sign in
          </Link>
        </div>
      </div>
    </div>
  );
};

export default DocumentVerificationPage;
