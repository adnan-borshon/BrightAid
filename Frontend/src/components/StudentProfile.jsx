import { useState, useEffect } from "react";
import { useParams, useNavigate, useLocation } from "react-router-dom";
import { 
  User, 
  Edit, 
  Trash2, 
  FileText, 
  Upload, 
  Download, 
  X, 
  Save, 
  Calendar, 
  Phone, 
  MapPin, 
  GraduationCap,
  Award,
  Home as HomeIcon,
  Plus
} from "lucide-react";
import Sidebar from './DashSidebar';
import { useApp } from "@/context/AppContext";

export default function StudentProfile() {
  const { schoolId, studentId } = useParams();
  const navigate = useNavigate();
  const location = useLocation();
  const { API_BASE_URL } = useApp();
  
  const [student, setStudent] = useState(null);
  const [documents, setDocuments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [isEditing, setIsEditing] = useState(false);
  const [editForm, setEditForm] = useState({});
  const [showDeleteConfirm, setShowDeleteConfirm] = useState(false);
  const [uploadingDoc, setUploadingDoc] = useState(false);

  useEffect(() => {
    fetchStudentData();
    fetchDocuments();
  }, [studentId]);

  const fetchStudentData = async () => {
    try {
      const response = await fetch(`${API_BASE_URL}/students/${studentId}`);
      if (response.ok) {
        const data = await response.json();
        setStudent(data);
        setEditForm({
          studentName: data.studentName || '',
          gender: data.gender || '',
          dateOfBirth: data.dateOfBirth || '',
          classLevel: data.classLevel || '',
          fatherName: data.fatherName || '',
          motherName: data.motherName || '',
          guardianPhone: data.guardianPhone || '',
          address: data.address || '',
          familyMonthlyIncome: data.familyMonthlyIncome || ''
        });
      }
    } catch (error) {
      console.error('Error fetching student:', error);
    }
    setLoading(false);
  };

  const fetchDocuments = async () => {
    try {
      const response = await fetch(`${API_BASE_URL}/school-documents`);
      if (response.ok) {
        const data = await response.json();
        const studentDocs = data.filter(doc => 
          doc.schoolId == schoolId &&
          doc.documentType === 'PERFORMANCE' && 
          doc.documentDescription?.includes(`Student ID: ${studentId}`)
        );
        setDocuments(studentDocs);
      }
    } catch (error) {
      console.error('Error fetching documents:', error);
    }
  };

  const handleUpdateStudent = async () => {
    try {
      const updateData = {
        studentId: parseInt(studentId),
        schoolId: parseInt(schoolId),
        studentIdNumber: student.studentIdNumber, // Required field
        hasScholarship: student.hasScholarship || false, // Required field
        fatherAlive: student.fatherAlive !== undefined ? student.fatherAlive : true, // Required field
        motherAlive: student.motherAlive !== undefined ? student.motherAlive : true, // Required field
        ...editForm,
        familyMonthlyIncome: editForm.familyMonthlyIncome ? parseFloat(editForm.familyMonthlyIncome) : null
      };
      
      const response = await fetch(`${API_BASE_URL}/students/${studentId}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(updateData)
      });
      
      if (response.ok) {
        await fetchStudentData();
        setIsEditing(false);
      } else {
        const errorText = await response.text();
        console.error('Update failed:', response.status, errorText);
        alert(`Update failed: ${errorText}`);
      }
    } catch (error) {
      console.error('Error updating student:', error);
      alert(`Error updating student: ${error.message}`);
    }
  };

  const handleDeleteStudent = async () => {
    try {
      const response = await fetch(`${API_BASE_URL}/students/${studentId}`, {
        method: 'DELETE'
      });
      
      if (response.ok) {
        navigate(`/students/${schoolId}`);
      }
    } catch (error) {
      console.error('Error deleting student:', error);
    }
  };

  const handleDocumentUpload = async (event) => {
    const file = event.target.files[0];
    if (!file) return;

    setUploadingDoc(true);
    const formData = new FormData();
    formData.append('file', file);
    formData.append('documentTitle', `${student.studentName} - Marksheet`);
    formData.append('documentDescription', `Performance document for Student ID: ${studentId}`);
    formData.append('documentType', 'PERFORMANCE');
    formData.append('schoolId', schoolId);

    try {
      const response = await fetch(`${API_BASE_URL}/school-documents`, {
        method: 'POST',
        body: formData
      });
      
      if (response.ok) {
        await fetchDocuments();
      } else {
        console.error('Upload failed:', await response.text());
      }
    } catch (error) {
      console.error('Error uploading document:', error);
    }
    setUploadingDoc(false);
  };

  const handleDeleteDocument = async (docId) => {
    try {
      const response = await fetch(`${API_BASE_URL}/school-documents/${docId}`, {
        method: 'DELETE'
      });
      
      if (response.ok) {
        await fetchDocuments();
      }
    } catch (error) {
      console.error('Error deleting document:', error);
    }
  };

  const formatClassLevel = (classLevel) => {
    return classLevel ? classLevel.replace('_', ' ').replace(/\b\w/g, l => l.toUpperCase()) : 'N/A';
  };

  if (loading) {
    return (
      <div className="flex h-screen bg-gray-50">
        <Sidebar />
        <div className="flex-1 flex items-center justify-center">
          <div className="text-lg">Loading...</div>
        </div>
      </div>
    );
  }

  if (!student) {
    return (
      <div className="flex h-screen bg-gray-50">
        <Sidebar />
        <div className="flex-1 flex items-center justify-center">
          <div className="text-center">
            <h2 className="text-xl font-bold mb-2">Student Not Found</h2>
            <button 
              onClick={() => navigate(`/students/${schoolId}`)}
              className="bg-green-600 text-white px-4 py-2 rounded-lg"
            >
              Back to Students
            </button>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="flex h-screen bg-gray-50">
      <Sidebar />
      <div className="flex-1 overflow-auto bg-white">
        <div className="border-b">
          <div className="p-6">
            <div className="flex items-center justify-between mb-4">
              <div>
                <h1 className="text-2xl font-bold mb-1">Student Profile</h1>
                <p className="text-gray-600 text-sm">
                  Manage student information and documents
                </p>
              </div>
              <div className="flex gap-3">
                <button
                  onClick={() => {
                    const fromDashboard = location.state?.from === 'dashboard' || document.referrer.includes(`/dashboard/${schoolId}`);
                    navigate(fromDashboard ? `/dashboard/${schoolId}` : `/students/${schoolId}`);
                  }}
                  className="px-4 py-2 text-gray-600 bg-gray-100 rounded-lg hover:bg-gray-200"
                >
                  {location.state?.from === 'dashboard' || document.referrer.includes(`/dashboard/${schoolId}`) ? 'Back to Dashboard' : 'Back to Students'}
                </button>
                {!isEditing ? (
                  <>
                    <button
                      onClick={() => setIsEditing(true)}
                      className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 flex items-center gap-2"
                    >
                      <Edit size={16} />
                      Edit
                    </button>
                    <button
                      onClick={() => setShowDeleteConfirm(true)}
                      className="px-4 py-2 bg-red-600 text-white rounded-lg hover:bg-red-700 flex items-center gap-2"
                    >
                      <Trash2 size={16} />
                      Delete
                    </button>
                  </>
                ) : (
                  <>
                    <button
                      onClick={handleUpdateStudent}
                      className="px-4 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700 flex items-center gap-2"
                    >
                      <Save size={16} />
                      Save
                    </button>
                    <button
                      onClick={() => setIsEditing(false)}
                      className="px-4 py-2 bg-gray-600 text-white rounded-lg hover:bg-gray-700"
                    >
                      Cancel
                    </button>
                  </>
                )}
              </div>
            </div>
          </div>
        </div>

        <div className="p-6">
          <div className="space-y-6">
            {/* Student Header */}
            <div className="flex gap-6 bg-gradient-to-r from-green-50 to-emerald-50 rounded-xl p-6">
              <div className="relative w-32 h-32 group">
                <img
                  src={student.profileImage ? `${API_BASE_URL}${student.profileImage}?t=${Date.now()}` : '/student.png'}
                  alt={student.studentName}
                  className="w-32 h-32 rounded-lg object-cover"
                  onError={(e) => {
                    console.log('Image failed to load:', e.target.src);
                    console.log('Student profileImage value:', student.profileImage);
                    console.log('Constructed URL:', student.profileImage ? `${API_BASE_URL}${student.profileImage}` : '/student.png');
                    e.target.src = '/student.png'; // Fallback to default image
                  }}
                  onLoad={() => {
                    console.log('Image loaded successfully:', student.profileImage ? `${API_BASE_URL}${student.profileImage}` : '/student.png');
                  }}
                />
                {isEditing && (
                  <div className="absolute inset-0 bg-black bg-opacity-50 opacity-0 group-hover:opacity-100 transition-opacity flex items-center justify-center rounded-lg">
                    <label className="cursor-pointer">
                      <input
                        type="file"
                        accept="image/*"
                        onChange={async (e) => {
                          const file = e.target.files[0];
                          if (file) {
                            console.log('Uploading file:', file.name, 'Size:', file.size);
                            const formData = new FormData();
                            formData.append('image', file);
                            try {
                              const response = await fetch(`${API_BASE_URL}/students/${studentId}/image`, {
                                method: 'POST',
                                body: formData,
                              });
                              if (response.ok) {
                                const result = await response.json();
                                console.log('Image uploaded successfully. Response:', result);
                                // Force refresh the student data to get updated image path
                                await fetchStudentData();
                                // Force browser to reload the image by adding timestamp
                                const imgElement = document.querySelector(`img[alt="${student.studentName}"]`);
                                if (imgElement && result.imagePath) {
                                  const newSrc = `${API_BASE_URL}${result.imagePath}?t=${Date.now()}`;
                                  imgElement.src = newSrc;
                                  console.log('Updated image src to:', newSrc);
                                }
                              } else {
                                const errorText = await response.text();
                                console.error('Upload failed:', response.status, errorText);
                                alert(`Upload failed: ${errorText}`);
                              }
                            } catch (error) {
                              console.error('Error uploading image:', error);
                              alert(`Error: ${error.message}`);
                            }
                          }
                        }}
                        className="hidden"
                      />
                      <div className="bg-white bg-opacity-90 rounded-full p-2 hover:bg-opacity-100 transition-all">
                        <Upload size={20} className="text-gray-700" />
                      </div>
                    </label>
                  </div>
                )}
              </div>
              <div className="flex-1">
                <h3 className="text-2xl font-bold mb-2">{student.studentName}</h3>
                <p className="text-gray-600 mb-4">Class {formatClassLevel(student.classLevel)}</p>
                <div className="grid grid-cols-2 gap-3 text-sm">
                  <div className="flex items-center gap-2 text-gray-600">
                    <GraduationCap size={16} />
                    <span>Student ID: {student.studentId}</span>
                  </div>
                  <div className="flex items-center gap-2 text-gray-600">
                    <Calendar size={16} />
                    <span>DOB: {student.dateOfBirth || 'N/A'}</span>
                  </div>
                </div>
              </div>
            </div>

            {/* Personal Information */}
            <div className="bg-white border border-gray-200 rounded-xl p-6">
              <h4 className="font-bold text-lg mb-4 flex items-center gap-2">
                <User size={20} className="text-green-600" />
                Personal Information
              </h4>
              
              {isEditing ? (
                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label className="text-sm text-gray-500 block mb-1">Student Name</label>
                    <input
                      type="text"
                      value={editForm.studentName}
                      onChange={(e) => setEditForm({...editForm, studentName: e.target.value})}
                      className="w-full p-2 border rounded-lg"
                    />
                  </div>
                  <div>
                    <label className="text-sm text-gray-500 block mb-1">Gender</label>
                    <select
                      value={editForm.gender}
                      onChange={(e) => setEditForm({...editForm, gender: e.target.value})}
                      className="w-full p-2 border rounded-lg"
                    >
                      <option value="MALE">Male</option>
                      <option value="FEMALE">Female</option>
                    </select>
                  </div>
                  <div>
                    <label className="text-sm text-gray-500 block mb-1">Date of Birth</label>
                    <input
                      type="date"
                      value={editForm.dateOfBirth}
                      onChange={(e) => setEditForm({...editForm, dateOfBirth: e.target.value})}
                      className="w-full p-2 border rounded-lg"
                    />
                  </div>
                  <div>
                    <label className="text-sm text-gray-500 block mb-1">Class Level</label>
                    <select
                      value={editForm.classLevel}
                      onChange={(e) => setEditForm({...editForm, classLevel: e.target.value})}
                      className="w-full p-2 border rounded-lg"
                    >
                      {['ONE', 'TWO', 'THREE', 'FOUR', 'FIVE', 'SIX', 'SEVEN', 'EIGHT', 'NINE', 'TEN'].map(level => (
                        <option key={level} value={level}>{formatClassLevel(level)}</option>
                      ))}
                    </select>
                  </div>
                  <div>
                    <label className="text-sm text-gray-500 block mb-1">Father's Name</label>
                    <input
                      type="text"
                      value={editForm.fatherName}
                      onChange={(e) => setEditForm({...editForm, fatherName: e.target.value})}
                      className="w-full p-2 border rounded-lg"
                    />
                  </div>
                  <div>
                    <label className="text-sm text-gray-500 block mb-1">Mother's Name</label>
                    <input
                      type="text"
                      value={editForm.motherName}
                      onChange={(e) => setEditForm({...editForm, motherName: e.target.value})}
                      className="w-full p-2 border rounded-lg"
                    />
                  </div>
                  <div>
                    <label className="text-sm text-gray-500 block mb-1">Guardian Phone</label>
                    <input
                      type="text"
                      value={editForm.guardianPhone}
                      onChange={(e) => setEditForm({...editForm, guardianPhone: e.target.value})}
                      className="w-full p-2 border rounded-lg"
                    />
                  </div>
                  <div>
                    <label className="text-sm text-gray-500 block mb-1">Family Monthly Income</label>
                    <input
                      type="number"
                      value={editForm.familyMonthlyIncome}
                      onChange={(e) => setEditForm({...editForm, familyMonthlyIncome: e.target.value})}
                      className="w-full p-2 border rounded-lg"
                    />
                  </div>
                  <div className="col-span-2">
                    <label className="text-sm text-gray-500 block mb-1">Address</label>
                    <textarea
                      value={editForm.address}
                      onChange={(e) => setEditForm({...editForm, address: e.target.value})}
                      className="w-full p-2 border rounded-lg"
                      rows="2"
                    />
                  </div>
                </div>
              ) : (
                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <p className="text-sm text-gray-500">Student Name</p>
                    <p className="font-semibold">{student.studentName}</p>
                  </div>
                  <div>
                    <p className="text-sm text-gray-500">Gender</p>
                    <p className="font-semibold">{student.gender || 'Not specified'}</p>
                  </div>
                  <div>
                    <p className="text-sm text-gray-500">Date of Birth</p>
                    <p className="font-semibold">{student.dateOfBirth || 'N/A'}</p>
                  </div>
                  <div>
                    <p className="text-sm text-gray-500">Class</p>
                    <p className="font-semibold">Class {formatClassLevel(student.classLevel)}</p>
                  </div>
                  <div>
                    <p className="text-sm text-gray-500">Father's Name</p>
                    <p className="font-semibold">{student.fatherName || 'N/A'}</p>
                  </div>
                  <div>
                    <p className="text-sm text-gray-500">Mother's Name</p>
                    <p className="font-semibold">{student.motherName || 'N/A'}</p>
                  </div>
                  <div>
                    <p className="text-sm text-gray-500">Guardian Phone</p>
                    <p className="font-semibold flex items-center gap-1">
                      <Phone size={14} />
                      {student.guardianPhone || 'N/A'}
                    </p>
                  </div>
                  <div>
                    <p className="text-sm text-gray-500">Family Monthly Income</p>
                    <p className="font-semibold">${student.familyMonthlyIncome || 'N/A'}</p>
                  </div>
                  <div className="col-span-2">
                    <p className="text-sm text-gray-500">Address</p>
                    <p className="font-semibold flex items-center gap-1">
                      <MapPin size={14} />
                      {student.address || 'Not specified'}
                    </p>
                  </div>
                </div>
              )}
            </div>

            {/* Academic Progress */}
            <div className="bg-white border border-gray-200 rounded-xl p-6">
              <h4 className="font-bold text-lg mb-4 flex items-center gap-2">
                <Award size={20} className="text-green-600" />
                Academic Progress
              </h4>
              <div className="grid grid-cols-3 gap-4">
                <div className="text-center p-4 bg-blue-50 rounded-lg">
                  <p className="text-sm text-gray-600 mb-1">Enrollment Status</p>
                  <p className="text-lg font-bold text-blue-600">Active</p>
                </div>
                <div className="text-center p-4 bg-green-50 rounded-lg">
                  <p className="text-sm text-gray-600 mb-1">Class</p>
                  <p className="text-2xl font-bold text-green-600">
                    {formatClassLevel(student.classLevel)}
                  </p>
                </div>
                <div className="text-center p-4 bg-purple-50 rounded-lg">
                  <p className="text-sm text-gray-600 mb-1">Scholarship</p>
                  <p className="text-lg font-bold text-purple-600">
                    {student.hasScholarship ? 'Yes' : 'No'}
                  </p>
                </div>
              </div>
            </div>

            {/* Performance Documents */}
            <div className="bg-white border border-gray-200 rounded-xl p-6">
              <div className="flex justify-between items-center mb-4">
                <h4 className="font-bold text-lg flex items-center gap-2">
                  <FileText size={20} className="text-green-600" />
                  Performance Documents
                </h4>
                {isEditing && (
                  <div className="flex gap-2">
                    <input
                      type="file"
                      accept=".pdf,.doc,.docx,.jpg,.jpeg,.png"
                      onChange={handleDocumentUpload}
                      className="hidden"
                      id="document-upload"
                    />
                    <label
                      htmlFor="document-upload"
                      className={`px-4 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700 flex items-center gap-2 cursor-pointer ${uploadingDoc ? 'opacity-50' : ''}`}
                    >
                      <Plus size={16} />
                      {uploadingDoc ? 'Uploading...' : 'Add Document'}
                    </label>
                  </div>
                )}
              </div>
              
              {documents.length > 0 ? (
                <div className="space-y-3">
                  {documents.map((doc) => (
                    <div key={doc.documentId} className="flex items-center justify-between p-3 bg-gray-50 rounded-lg">
                      <div className="flex items-center gap-3">
                        <FileText size={20} className="text-blue-600" />
                        <div>
                          <p className="font-semibold">{doc.documentTitle}</p>
                          <p className="text-sm text-gray-500">
                            Uploaded: {new Date(doc.uploadDate).toLocaleDateString()}
                          </p>
                        </div>
                      </div>
                      <div className="flex gap-2">
                        <button
                          onClick={() => window.open(doc.fileUrl, '_blank')}
                          className="p-2 text-blue-600 hover:bg-blue-50 rounded"
                        >
                          <Download size={16} />
                        </button>
                        {isEditing && (
                          <button
                            onClick={() => handleDeleteDocument(doc.documentId)}
                            className="p-2 text-red-600 hover:bg-red-50 rounded"
                          >
                            <Trash2 size={16} />
                          </button>
                        )}
                      </div>
                    </div>
                  ))}
                </div>
              ) : (
                <div className="bg-gray-50 rounded-lg p-8 text-center">
                  <FileText size={48} className="mx-auto text-gray-400 mb-4" />
                  <p className="text-gray-500">No performance documents uploaded</p>
                  <p className="text-sm text-gray-400 mt-2">
                    Upload marksheets and academic records here
                  </p>
                </div>
              )}
            </div>
          </div>
        </div>
      </div>

      {/* Delete Confirmation Modal */}
      {showDeleteConfirm && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white rounded-lg p-6 max-w-md w-full mx-4">
            <h3 className="text-lg font-bold mb-4">Delete Student</h3>
            <p className="text-gray-600 mb-6">
              Are you sure you want to delete {student.studentName}? This action cannot be undone.
            </p>
            <div className="flex gap-3 justify-end">
              <button
                onClick={() => setShowDeleteConfirm(false)}
                className="px-4 py-2 text-gray-600 bg-gray-100 rounded-lg hover:bg-gray-200"
              >
                Cancel
              </button>
              <button
                onClick={handleDeleteStudent}
                className="px-4 py-2 bg-red-600 text-white rounded-lg hover:bg-red-700"
              >
                Delete
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}