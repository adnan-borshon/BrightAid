import { useState } from "react";
import { Link } from "react-router-dom";
import { Check, Delete, DeleteIcon, Trash, Trash2, TrashIcon, Upload, X } from "lucide-react";

const DocumentVerificationPage = () => {
  const [uploadedFiles, setUploadedFiles] = useState({}); 

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
      type: "infrastructure",
      size: "2MB",
    },
    {
      id: "headmasterNIDVerification",
      label: "Headmaster's NID/Verification",
      type: "staff",
      size: "2MB",
    },
    {
      id: "headmasterElectricityBill",
      label: "Headmaster's Electricity Bill",
      type: "other",
      size: "2MB",
    },
  ];

  // Handle file upload
  const handleFileChange = (e, field) => {
    const file = e.target.files[0];
    if (!file) return;

    // create file metadata to match SCHOOL_DOCUMENTS schema
    const fileMeta = {
      document_type: field.type,
      document_title: field.label,
      document_description: "",
      file_url: URL.createObjectURL(file), // preview purpose only
      file_hash: null, // backend will generate
      upload_date: new Date().toISOString().split("T")[0],
      uploaded_by: 1, // temp user_id, replace with logged-in user
      is_current: true,
      created_at: new Date().toISOString(),
      file,
    };

    setUploadedFiles((prev) => ({
      ...prev,
      [field.id]: fileMeta,
    }));
  };

  // Remove uploaded file
  const handleRemoveFile = (fieldId) => {
    setUploadedFiles((prev) => {
      const newFiles = { ...prev };
      delete newFiles[fieldId];
      return newFiles;
    });
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

        {/* Submit */}
        <Link
          to="/approval"
          className="w-full bg-green-600 text-white py-3 px-4 rounded-lg font-medium hover:bg-green-700 transition-colors mt-6 block text-center"
        >
          Submit
        </Link>

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
