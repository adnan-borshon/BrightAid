import { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import { Search, MapPin, GraduationCap, Calendar, X, FileText, User, Home as HomeIcon, Phone, Award } from "lucide-react";
import { useDonor } from "@/context/DonorContext";
import Sidebar from './DonorDashSidebar';

export default function DonorStudentsView() {
  const { id: userId } = useParams(); // URL param is now userId
  const { donorData, sponsoredStudentsData, loading, refreshDonorData, API_BASE_URL } = useDonor();
  const [studentsData, setStudentsData] = useState([]);
  const [searchTerm, setSearchTerm] = useState("");
  const [selectedStudent, setSelectedStudent] = useState(null);

  useEffect(() => {
    const userId = localStorage.getItem('userId');
    if (userId) {
      refreshDonorData(userId);
    }
  }, []);

  useEffect(() => {
    // Use sponsored students data from context instead of fetching all students
    setStudentsData(sponsoredStudentsData || []);
  }, [sponsoredStudentsData]);

  const fetchAllStudents = async () => {
    try {
      const response = await fetch(`${API_BASE_URL}/students`);
      if (response.ok) {
        const data = await response.json();
        setStudentsData(Array.isArray(data) ? data : []);
      }
    } catch (error) {
      console.error('Error fetching students:', error);
    }
  };

  const filterStudents = () => {
    return studentsData.filter(
      (student) =>
        (student.studentName || student.name || '').toLowerCase().includes(searchTerm.toLowerCase()) ||
        (student.schoolId || '').toString().includes(searchTerm.toLowerCase())
    );
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

  const handleViewProgress = (student) => {
    setSelectedStudent(student);
  };

  const closeStudentView = () => {
    setSelectedStudent(null);
  };

  return (
    <div className="flex h-screen bg-gray-50">
      <Sidebar />
      <div className="flex-1 overflow-auto bg-white">
      <div className="border-b">
        <div className="p-6">
          <h1 className="text-2xl font-bold mb-1">Sponsored Students</h1>
          <p className="text-gray-600 text-sm">
            Students you have sponsored through donations
          </p>

          <div className="relative mt-6 max-w-md">
            <Search
              className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400"
              size={18}
            />
            <input
              type="text"
              placeholder="Search students or schools..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-green-500"
              data-testid="input-search-students"
            />
          </div>
        </div>
      </div>

      <div className="p-6">
        <div className="flex gap-6">
          {/* Students List - Left Side */}
          <div className={`${selectedStudent ? "w-1/3" : "w-full"} transition-all duration-300`}>
            <div className="grid gap-4">
              {filterStudents().length === 0 ? (
                <div className="text-center py-12">
                  <p className="text-gray-500">No sponsored students found.</p>
                  <p className="text-sm text-gray-400 mt-2">Students you sponsor will appear here.</p>
                </div>
              ) : (
                filterStudents().map((student) => (
                <div
                  key={student.id}
                  className={`border rounded-lg p-4 cursor-pointer transition-all ${
                    selectedStudent?.id === student.id
                      ? "border-green-600 bg-green-50"
                      : "border-gray-200 hover:border-green-300 hover:bg-gray-50"
                  }`}
                  onClick={() => handleViewProgress(student)}
                  data-testid={`student-card-${student.id}`}
                >
                  <div className="flex gap-4">
                    <img
                      src={student.profileImage ? `http://localhost:8081${student.profileImage}` : 'https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=300&h=300&fit=crop'}
                      alt={student.studentName || student.name}
                      className="w-20 h-20 rounded-lg object-cover"
                      onError={(e) => {
                        e.target.src = 'https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=300&h=300&fit=crop';
                      }}
                    />
                    <div className="flex-1">
                      <h3 className="font-semibold text-lg">{student.studentName || student.name}</h3>
                      <p className="text-sm text-gray-600 mb-2">Class {student.studentClass || student.class}</p>
                      <div className="flex items-center gap-2 text-sm text-gray-500">
                        <MapPin size={14} />
                        <span className="truncate">School ID: {student.schoolId}</span>
                      </div>
                      <div className="flex items-center gap-2 text-sm text-gray-500 mt-1">
                        <Calendar size={14} />
                        <span>{student.age || 'N/A'} years old</span>
                      </div>
                    </div>
                    <button
                      onClick={(e) => {
                        e.stopPropagation();
                        handleViewProgress(student);
                      }}
                      className="bg-green-600 text-white px-4 py-2 rounded-lg font-semibold hover:bg-green-700 h-fit"
                      data-testid={`button-view-progress-${student.id}`}
                    >
                      View Progress
                    </button>
                  </div>
                </div>
                ))
              )}
            </div>
          </div>

          {/* Student Details Panel - Right Side */}
          {selectedStudent && (
            <div className="w-2/3 border-l pl-6 transition-all duration-300">
              <div className="flex justify-between items-center mb-6">
                <h2 className="text-2xl font-bold">Student Profile & Progress</h2>
                <button
                  onClick={closeStudentView}
                  className="text-gray-500 hover:text-gray-700 p-2"
                  data-testid="button-close-student-view"
                >
                  <X size={24} />
                </button>
              </div>

              <div className="space-y-6">
                {/* Student Header */}
                <div className="flex gap-6 bg-gradient-to-r from-green-50 to-emerald-50 rounded-xl p-6">
                  <img
                    src={selectedStudent.profileImage ? `http://localhost:8081${selectedStudent.profileImage}` : 'https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=300&h=300&fit=crop'}
                    alt={selectedStudent.studentName || selectedStudent.name}
                    className="w-32 h-32 rounded-lg object-cover"
                    onError={(e) => {
                      e.target.src = 'https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=300&h=300&fit=crop';
                    }}
                  />
                  <div className="flex-1">
                    <h3 className="text-2xl font-bold mb-2">{selectedStudent.studentName || selectedStudent.name}</h3>
                    <p className="text-gray-600 mb-4">Class {selectedStudent.studentClass || selectedStudent.class}</p>
                    <div className="grid grid-cols-2 gap-3 text-sm">
                      <div className="flex items-center gap-2 text-gray-600">
                        <MapPin size={16} />
                        <span>School ID: {selectedStudent.schoolId}</span>
                      </div>
                      <div className="flex items-center gap-2 text-gray-600">
                        <GraduationCap size={16} />
                        <span>Age: {selectedStudent.age || 'N/A'}</span>
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
                  <div className="grid grid-cols-2 gap-4">
                    <div>
                      <p className="text-sm text-gray-500">Student Name</p>
                      <p className="font-semibold">{selectedStudent.studentName || selectedStudent.name}</p>
                    </div>
                    <div>
                      <p className="text-sm text-gray-500">Class</p>
                      <p className="font-semibold">Class {selectedStudent.studentClass || selectedStudent.class}</p>
                    </div>
                    <div>
                      <p className="text-sm text-gray-500">School ID</p>
                      <p className="font-semibold flex items-center gap-1">
                        <GraduationCap size={14} />
                        {selectedStudent.schoolId}
                      </p>
                    </div>
                    <div>
                      <p className="text-sm text-gray-500">Age</p>
                      <p className="font-semibold">{selectedStudent.age || 'N/A'} years</p>
                    </div>
                    <div className="col-span-2">
                      <p className="text-sm text-gray-500">Gender</p>
                      <p className="font-semibold flex items-center gap-1">
                        <User size={14} />
                        {selectedStudent.gender || 'Not specified'}
                      </p>
                    </div>
                  </div>
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
                      <p className="text-lg font-bold text-blue-600">
                        Active
                      </p>
                    </div>
                    <div className="text-center p-4 bg-green-50 rounded-lg">
                      <p className="text-sm text-gray-600 mb-1">Class</p>
                      <p className="text-2xl font-bold text-green-600">
                        {selectedStudent.studentClass || 'N/A'}
                      </p>
                    </div>
                    <div className="text-center p-4 bg-purple-50 rounded-lg">
                      <p className="text-sm text-gray-600 mb-1">School</p>
                      <p className="text-lg font-bold text-purple-600">
                        ID: {selectedStudent.schoolId}
                      </p>
                    </div>
                  </div>
                </div>

                {/* Latest Marksheet Preview */}
                <div className="bg-white border border-gray-200 rounded-xl p-6">
                  <h4 className="font-bold text-lg mb-4 flex items-center gap-2">
                    <FileText size={20} className="text-green-600" />
                    Latest Marksheet
                  </h4>
                  <div className="bg-gray-50 rounded-lg p-4">
                    <div className="text-center py-8">
                      <FileText size={48} className="mx-auto text-gray-400 mb-4" />
                      <p className="text-gray-500">No marksheet available</p>
                      <p className="text-sm text-gray-400 mt-2">
                        Academic records will be displayed here when available
                      </p>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          )}
        </div>
      </div>
      </div>
    </div>
  );
}
