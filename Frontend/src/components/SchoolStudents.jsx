import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Search, Plus, AlertTriangle, Users as UsersIcon, Layers, Camera } from 'lucide-react';
import Sidebar from './DashSidebar';
import { useApp } from '../context/AppContext';
import StudentEnrollmentModal from './Modal/StudentEnrollmentModal';


export default function SchoolStudents() {
  const { schoolId } = useParams();
  const navigate = useNavigate();
  const { schoolData, studentsData, loading, refreshData, API_BASE_URL } = useApp();
  const [isModalOpen, setIsModalOpen] = useState(false);
  // Filters
  const [searchQuery, setSearchQuery] = useState('');
  const [statusFilter, setStatusFilter] = useState('All');
  const [genderFilter, setGenderFilter] = useState('All');
  const [riskFilter, setRiskFilter] = useState('All');
  
  // Pagination
  const [currentPage, setCurrentPage] = useState(1);
  const studentsPerPage = 12;
  


  useEffect(() => {
    if (schoolId) {
      refreshData(schoolId);
    }
  }, [schoolId]);

  // Process students data for display
  const processedStudents = studentsData
    .filter(student => (student.schoolId || student.school_id) == schoolId)
    .map(student => ({
      student_id: student.studentId || student.student_id,
      student_name: student.studentName || student.student_name,
      class_level: student.classLevel || student.class_level,
      scholarship_amount: student.scholarshipAmount || student.scholarship_amount || 0,
      total_amount: student.totalAmount || student.total_amount || 0,
      risk_status: student.riskStatus || student.risk_status || 'Low Risk',
      attendance_rate: student.attendanceRate || student.attendance_rate || 0,
      performance_score: student.performanceScore || student.performance_score || 0,
      gender: student.gender || 'Not specified',
      status: student.status || 'active',
      donor_username: student.donorUsername || student.donor_username || '',
      profile_image: student.profileImage || student.profile_image
    }));

  const totalCount = processedStudents.length;
  const highRiskCount = processedStudents.filter(s => 
    s.risk_status && (s.risk_status.toLowerCase().includes('high') || s.risk_status.toLowerCase().includes('at risk'))
  ).length;
  const scholarshipPendingCount = processedStudents.filter(s => 
    s.status && s.status.toLowerCase() === 'unpaid'
  ).length;

  const handleEnrollStudent = async (formData) => {
    try {
      // Create student with JSON data first
      const jsonData = {
        schoolId: parseInt(schoolId),
        studentName: formData.get('student_name'),
        studentIdNumber: formData.get('student_id_number'),
        gender: formData.get('gender').toUpperCase(),
        dateOfBirth: formData.get('date_of_birth'),
        classLevel: mapClassLevel(formData.get('class_level')),
        fatherName: formData.get('father_name') || null,
        fatherAlive: formData.get('father_alive') === 'true',
        fatherOccupation: formData.get('father_occupation') || null,
        motherName: formData.get('mother_name') || null,
        motherAlive: formData.get('mother_alive') === 'true',
        motherOccupation: formData.get('mother_occupation') || null,
        guardianPhone: formData.get('guardian_phone'),
        address: formData.get('address') || null,
        familyMonthlyIncome: formData.get('family_monthly_income') ? parseFloat(formData.get('family_monthly_income')) : null,
        hasScholarship: false
      };
      
      const response = await fetch(`${API_BASE_URL}/students`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(jsonData)
      });
      
      if (response.ok) {
        const studentData = await response.json();
        console.log('Student created successfully:', studentData);
        
        // Upload image separately if provided
        const profileImage = formData.get('profile_image');
        if (profileImage && profileImage.size > 0) {
          const imageFormData = new FormData();
          imageFormData.append('image', profileImage);
          
          const imageResponse = await fetch(`${API_BASE_URL}/students/${studentData.studentId}/image`, {
            method: 'POST',
            body: imageFormData,
          });
          
          if (!imageResponse.ok) {
            console.error('Image upload failed:', await imageResponse.text());
          }
        }

        setIsModalOpen(false);
        refreshData(schoolId);
      } else {
        const errorData = await response.text();
        console.error('Student creation failed:', errorData);
        alert('Failed to create student: ' + errorData);
      }
    } catch (error) {
      console.error('Error enrolling student:', error);
    }
  };

  const mapClassLevel = (classLevel) => {
    const mapping = {
      '1': 'ONE', '2': 'TWO', '3': 'THREE', '4': 'FOUR', '5': 'FIVE',
      '6': 'SIX', '7': 'SEVEN', '8': 'EIGHT', '9': 'NINE', '10': 'TEN'
    };
    return mapping[classLevel] || 'ONE';
  };
  const formatCurrency = (amount) => {
    return `$${Math.abs(amount).toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`;
  };

  const formatClassLevel = (classLevel) => {
    return classLevel ? classLevel.replace('_', ' ').replace(/\b\w/g, l => l.toUpperCase()) : 'N/A';
  };

  const getStatusBadge = (status) => {
    const badges = {
      'active': { bg: 'bg-green-100', text: 'text-green-700', label: 'Active' },
      'high risk': { bg: 'bg-red-100', text: 'text-red-700', label: 'High Risk' },
      'unpaid': { bg: 'bg-orange-100', text: 'text-orange-700', label: 'Unpaid' },
      'at risk': { bg: 'bg-red-100', text: 'text-red-700', label: 'High Risk' }
    };
    
    const key = status?.toLowerCase() || 'active';
    const badge = badges[key] || badges['active'];
    return badge;
  };

  // Filter students
  const filteredStudents = processedStudents.filter(student => {
    const matchesSearch = student.student_name.toLowerCase().includes(searchQuery.toLowerCase());
    const matchesStatus = statusFilter === 'All' || student.status?.toLowerCase() === statusFilter.toLowerCase();
    const matchesGender = genderFilter === 'All' || student.gender?.toLowerCase() === genderFilter.toLowerCase();
    const matchesRisk = riskFilter === 'All' || 
      (riskFilter === 'High Risk' && (student.risk_status?.toLowerCase().includes('high') || student.risk_status?.toLowerCase().includes('at risk'))) ||
      (riskFilter === 'Low Risk' && student.risk_status?.toLowerCase().includes('low'));
    
    return matchesSearch && matchesStatus && matchesGender && matchesRisk;
  });

  // Pagination
  const totalPages = Math.ceil(filteredStudents.length / studentsPerPage);
  const indexOfLastStudent = currentPage * studentsPerPage;
  const indexOfFirstStudent = indexOfLastStudent - studentsPerPage;
  const currentStudents = filteredStudents.slice(indexOfFirstStudent, indexOfLastStudent);

  const handleStudentClick = (studentId) => {
    navigate(`/student-profile/${schoolId}/${studentId}`);
  };

  const handleImageUpdate = (studentId, imagePath) => {
    // Refresh data to get updated image
    refreshData(schoolId);
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center h-screen bg-gray-50">
        <div className="text-center">
          <div className="w-16 h-16 border-4 border-green-600 border-t-transparent rounded-full animate-spin mx-auto mb-4"></div>
          <p className="text-gray-600">Loading students...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="flex h-screen bg-gray-50">
      <Sidebar />

      <div className="flex-1 overflow-auto">

        <div className="p-6">
          {/* Header */}
          <div className="mb-6">
            <h1 className="text-2xl font-bold text-gray-800">Student Overview</h1>
            <p className="text-sm text-gray-500">View your key stats at a glance</p>
          </div>

          {/* Stats Cards */}
          <div className="grid grid-cols-3 gap-6 mb-8">
            <div className="bg-white rounded-xl p-6 shadow-sm border border-gray-100">
              <div className="flex items-center gap-3 mb-3">
                <div className="w-12 h-12 bg-green-100 rounded-full flex items-center justify-center">
                  <UsersIcon className="w-6 h-6 text-green-600" />
                </div>
                <div>
                  <div className="text-sm text-gray-500">Total Students</div>
                  <div className="text-3xl font-bold text-gray-800">{totalCount}</div>
                </div>
              </div>
            </div>

            <div className="bg-white rounded-xl p-6 shadow-sm border border-gray-100">
              <div className="flex items-center gap-3 mb-3">
                <div className="w-12 h-12 bg-red-100 rounded-full flex items-center justify-center">
                  <AlertTriangle className="w-6 h-6 text-red-600" />
                </div>
                <div>
                  <div className="text-sm text-gray-500">High Risk Students</div>
                  <div className="text-3xl font-bold text-gray-800">{highRiskCount}</div>
                </div>
              </div>
            </div>

            <div className="bg-white rounded-xl p-6 shadow-sm border border-gray-100">
              <div className="flex items-center gap-3 mb-3">
                <div className="w-12 h-12 bg-blue-100 rounded-full flex items-center justify-center">
                  <Layers className="w-6 h-6 text-blue-600" />
                </div>
                <div>
                  <div className="text-sm text-gray-500">Scholarship Pending</div>
                  <div className="text-3xl font-bold text-gray-800">{scholarshipPendingCount}</div>
                </div>
              </div>
            </div>
          </div>

          {/* Find Students Section */}
          <div className="bg-green-50 rounded-xl p-6 mb-6">
            <div className="flex items-center justify-between mb-4">
              <h2 className="text-xl font-bold text-gray-800">Find Students</h2>
              <button
                onClick={() => setIsModalOpen(true)}
              className="bg-green-600 text-white px-4 py-2 rounded-lg flex items-center gap-2 hover:bg-green-700 transition-colors">
                <Plus className="w-4 h-4" />
                Add Student
              </button>
            </div>

            {/* Filters */}
            <div className="grid grid-cols-4 gap-4">
              <div className="relative">
                <Search className="w-4 h-4 absolute left-3 top-3 text-gray-400" />
                <input
                  type="text"
                  placeholder="Search"
                  value={searchQuery}
                  onChange={(e) => setSearchQuery(e.target.value)}
                  className="w-full pl-10 pr-3 py-2 bg-white rounded-lg text-sm border border-gray-200 focus:outline-none focus:ring-2 focus:ring-green-300"
                />
              </div>

              <select
                value={statusFilter}
                onChange={(e) => setStatusFilter(e.target.value)}
                className="px-3 py-2 bg-white rounded-lg text-sm border border-gray-200 focus:outline-none focus:ring-2 focus:ring-green-300"
              >
                <option value="All">• All</option>
                <option value="active">Active</option>
                <option value="unpaid">Unpaid</option>
              </select>

              <select
                value={genderFilter}
                onChange={(e) => setGenderFilter(e.target.value)}
                className="px-3 py-2 bg-white rounded-lg text-sm border border-gray-200 focus:outline-none focus:ring-2 focus:ring-green-300"
              >
                <option value="All">All</option>
                <option value="male">Male</option>
                <option value="female">Female</option>
              </select>

              <select
                value={riskFilter}
                onChange={(e) => setRiskFilter(e.target.value)}
                className="px-3 py-2 bg-white rounded-lg text-sm border border-gray-200 focus:outline-none focus:ring-2 focus:ring-green-300"
              >
                <option value="All">All</option>
                <option value="High Risk">High Risk</option>
                <option value="Low Risk">Low Risk</option>
              </select>
            </div>
          </div>

          {/* Students Grid */}
          <div className="grid grid-cols-4 gap-4 mb-6">
            {currentStudents.length > 0 ? currentStudents.map((student) => {
              const badge = getStatusBadge(student.risk_status);
              return (
                <div
                  key={student.student_id}
                  className="bg-white rounded-xl overflow-hidden shadow-sm border border-gray-100 cursor-pointer hover:shadow-md transition-shadow"
                  onClick={() => handleStudentClick(student.student_id)}
                >
               <div className="relative h-40 bg-gray-100 flex items-center justify-center group">
  {student.profile_image ? (
    <img 
      src={`http://localhost:8081${student.profile_image}`}
      alt={student.student_name}
      className="w-full h-full object-cover"
      onError={(e) => {
        e.target.style.display = 'none';
        e.target.nextSibling.style.display = 'flex';
      }}
    />
  ) : null}
  <div 
    className={`w-full h-full flex items-center justify-center text-gray-400 ${student.profile_image ? 'hidden' : 'flex'}`}
    style={{ display: student.profile_image ? 'none' : 'flex' }}
  >
    <div className="text-center">
      <Camera className="w-8 h-8 mx-auto mb-2" />
      <span className="text-sm">No Image</span>
    </div>
  </div>
  <span className={`absolute top-3 right-3 ${badge.bg} ${badge.text} px-2 py-1 rounded-full text-xs font-medium`}>
    • {badge.label}
  </span>

</div>
                  <div className="p-4">
                    <div className="text-xs text-green-600 mb-1">Scholarship from {student.donor_username}</div>
                    <div className="text-sm font-semibold text-gray-800 mb-2">
                      {student.student_name}, {formatClassLevel(student.class_level)}
                    </div>
                    <div className="text-xs text-gray-500 mb-4">
                      Fund Received: {formatCurrency(student.scholarship_amount)} / {formatCurrency(student.total_amount)}
                    </div>
                    <button
                      className="w-full py-2 text-sm text-green-600 bg-white rounded-lg hover:bg-green-100 transition-colors"
                      onClick={(e) => {
                        e.stopPropagation();
                        navigate(`/student-profile/${schoolId}/${student.student_id}`);
                      }}
                    >
                     View Details
                    </button>
                  </div>
                </div>
              );
            }) : (
              <div className="col-span-4 text-center py-12 text-gray-500">
                <div className="flex flex-col items-center gap-3">
                  <span className="text-4xl">👨‍🎓</span>
                  <span className="text-lg font-medium">No students found</span>
                  <span className="text-sm">Try adjusting your filters</span>
                </div>
              </div>
            )}
          </div>

          {/* Pagination */}
          {totalPages > 1 && (
            <div className="flex items-center justify-between">
              <button
                onClick={() => setCurrentPage(p => Math.max(1, p - 1))}
                disabled={currentPage === 1}
                className="px-4 py-2 text-sm text-gray-600 bg-white rounded-lg border border-gray-200 hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
              >
                ← Previous
              </button>

              <div className="flex gap-2">
                {[...Array(Math.min(10, totalPages))].map((_, i) => {
                  const pageNum = i + 1;
                  return (
                    <button
                      key={pageNum}
                      onClick={() => setCurrentPage(pageNum)}
                      className={`w-8 h-8 rounded-lg text-sm ${
                        currentPage === pageNum
                          ? 'bg-green-600 text-white'
                          : 'bg-white text-gray-600 border border-gray-200 hover:bg-gray-50'
                      }`}
                    >
                      {pageNum}
                    </button>
                  );
                })}
                {totalPages > 10 && <span className="px-2 py-1">...</span>}
                {totalPages > 10 && (
                  <button
                    onClick={() => setCurrentPage(totalPages)}
                    className={`w-8 h-8 rounded-lg text-sm ${
                      currentPage === totalPages
                        ? 'bg-green-600 text-white'
                        : 'bg-white text-gray-600 border border-gray-200 hover:bg-gray-50'
                    }`}
                  >
                    {totalPages}
                  </button>
                )}
              </div>

              <button
                onClick={() => setCurrentPage(p => Math.min(totalPages, p + 1))}
                disabled={currentPage === totalPages}
                className="px-4 py-2 text-sm text-gray-600 bg-white rounded-lg border border-gray-200 hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
              >
                Next →
              </button>
            </div>
          )}
        </div>
      </div>
      <StudentEnrollmentModal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        onSubmit={handleEnrollStudent}
      />
    </div>
  );
}