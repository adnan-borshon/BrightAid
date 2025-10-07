import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { ChevronRight, MoreVertical, Users, Briefcase, Plus, Download, Camera } from 'lucide-react';
import Sidebar from './DashSidebar';
import { useApp } from '../context/AppContext';
import StudentEnrollmentModal from './Modal/StudentEnrollmentModal';
import ProjectCreateModal from './Modal/ProjectCreateModal';
import SchoolProjectCard from './SchoolProjectCard';

export default function SchoolDashboard() {
  const { schoolId } = useParams();
  const navigate = useNavigate();
  const { schoolData, studentsData, projectsData, loading, refreshData, API_BASE_URL } = useApp();
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [ProjectModalOpen, setProjectModalOpen] = useState(false);
  useEffect(() => {
    if (schoolId) {
      refreshData(schoolId);
    }
  }, [schoolId]);

  // Process data from context
  const processedStudents = studentsData
    .filter(student => (student.schoolId || student.school_id) == schoolId)
    .slice(0, 3)
    .map(student => ({
      student_id: student.studentId || student.student_id,
      student_name: student.studentName || student.student_name,
      class_level: student.classLevel || student.class_level,
      scholarship_amount: student.scholarshipAmount || student.scholarship_amount || 0,
      total_amount: student.totalAmount || student.total_amount || 0,
      risk_status: student.riskStatus || student.risk_status || 'Low Risk',
      attendance_rate: student.attendanceRate || student.attendance_rate || 0,
      performance_score: student.performanceScore || student.performance_score || 0
    }));

  const processedProjects = projectsData
    .filter(project => (project.schoolId || project.school_id) == schoolId)
    .slice(0, 3);
    // Don't transform the data, pass it as-is from backend

  const highRiskStudents = studentsData
    .filter(student => {
      const riskStatus = student.riskStatus || student.risk_status;
      return (student.schoolId || student.school_id) == schoolId && 
             riskStatus && (riskStatus.toLowerCase().includes('high') || riskStatus.toLowerCase().includes('at risk'));
    })
    .slice(0, 4)
    .map(student => ({
      student_id: student.studentId || student.student_id,
      student_name: student.studentName || student.student_name,
      class_level: student.classLevel || student.class_level,
      attendance_rate: student.attendanceRate || student.attendance_rate || 0,
      performance_score: student.performanceScore || student.performance_score || 0,
      risk_score: student.riskScore || student.risk_score || 0
    }));

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

  const formatCurrency = (amount) => {
    return `$${Math.abs(amount).toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`;
  };

  const formatClassLevel = (classLevel) => {
    return classLevel ? classLevel.replace('_', ' ').replace(/\b\w/g, l => l.toUpperCase()) : 'N/A';
  };
  
  const mapClassLevel = (classLevel) => {
    const classMap = {
      '1': 'ONE', '2': 'TWO', '3': 'THREE', '4': 'FOUR', '5': 'FIVE',
      '6': 'SIX', '7': 'SEVEN', '8': 'EIGHT', '9': 'NINE', '10': 'TEN',
      '11': 'ELEVEN', '12': 'TWELVE'
    };
    return classMap[classLevel] || classLevel;
  };

  const getStatusColor = (status) => {
    const statusMap = {
      'active': 'bg-green-100 text-green-700',
      'in_progress': 'bg-blue-100 text-blue-700',
      'funded': 'bg-green-600 text-white',
      'completed': 'bg-green-100 text-green-700',
      'pending': 'bg-orange-100 text-orange-700',
      'draft': 'bg-gray-100 text-gray-700',
    };
    return statusMap[status?.toLowerCase()] || 'bg-gray-100 text-gray-700';
  };

  const getDonorLogo = (name) => {
    const firstChar = name?.charAt(0).toUpperCase() || '?';
    if (name?.toLowerCase().includes('spotify')) return '🎵';
    if (name?.toLowerCase().includes('coffee')) return '☕';
    return firstChar;
  };

  const handleStudentClick = (studentId) => {
    console.log('Viewing student details for ID:', studentId);
  };

  const handleProjectClick = (projectId) => {
    console.log('Viewing project details for ID:', projectId);
    navigate(`/project-details/${projectId}`);
  };

  const handleProjectCreation = async (formData) => {
    try {
      // Only send fields that exist in SchoolProjectDto
      const jsonData = {
        schoolId: parseInt(schoolId),
        projectTitle: formData.get('project_title'),
        projectDescription: formData.get('project_description'),
        projectTypeId: parseInt(formData.get('project_type_id'))
      };
      
      console.log('Sending project data:', jsonData);
      
      const response = await fetch(`${API_BASE_URL}/school-projects`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(jsonData)
      });
      
      if (response.ok) {
        const projectData = await response.json();
        console.log('Project created successfully:', projectData);
        
        const projectImage = formData.get('project_image');
        if (projectImage && projectImage.size > 0) {
          const imageFormData = new FormData();
          imageFormData.append('image', projectImage);
          
          await fetch(`${API_BASE_URL}/school-projects/${projectData.projectId}/image`, {
            method: 'POST',
            body: imageFormData,
          }).catch(error => console.error('Image upload failed:', error));
        }
        
        setProjectModalOpen(false);
        refreshData(schoolId);
      } else {
        const errorData = await response.text();
        console.error('Project creation failed:', errorData);
        alert('Failed to create project: ' + errorData);
      }
    } catch (error) {
      console.error('Error creating project:', error);
      alert('Error creating project: ' + error.message);
    }
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center h-screen bg-gray-50">
        <div className="text-center">
          <div className="w-16 h-16 border-4 border-green-600 border-t-transparent rounded-full animate-spin mx-auto mb-4"></div>
          <p className="text-gray-600">Loading...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="flex h-screen bg-gray-50">
            <StudentEnrollmentModal
              isOpen={isModalOpen}
              onClose={() => setIsModalOpen(false)}
              onSubmit={handleEnrollStudent}
            />

                 <ProjectCreateModal
              isOpen={ProjectModalOpen}
              onClose={() => setProjectModalOpen(false)}
              onSubmit={handleProjectCreation}
            />
      <Sidebar schoolData={schoolData} />

      {/* Main Content */}
      <div className="flex-1 overflow-auto">

        {/* Hero Banner */}
        <div className="bg-gradient-to-r from-green-50 to-green-100 m-6 rounded-2xl p-8 flex items-center justify-between relative overflow-hidden">
          <div className="flex-1 z-10">
            <div className="text-lg text-gray-600 mb-1">Welcome back,</div>
            <div className="text-3xl font-bold text-gray-800 mb-2 flex items-center gap-2">
              {schoolData?.schoolName || schoolData?.school_name || `School ${schoolId}`}
              {schoolData && <span className="text-blue-500">✓</span>}
            </div>
            <div className="text-sm text-gray-600 mb-2">
              {schoolData ? 'Your dedication is transforming education and reducing dropouts in your community' : 'Welcome to your school dashboard'}
            </div>
            <div className="text-xs text-gray-500 flex items-center gap-1">
              <span>📍</span>
              {schoolData?.address || 'Address not available'}
            </div>
          </div>
          <div className="w-96 h-48 rounded-xl overflow-hidden shadow-lg relative group">
            <div className="w-full h-full bg-gradient-to-br from-green-600 to-green-800 flex items-center justify-center">
              <div className="text-white text-6xl opacity-20">🏫</div>
            </div>
            <div className="absolute inset-0 bg-black bg-opacity-50 opacity-0 group-hover:opacity-100 transition-opacity flex items-center justify-center">
              <label className="cursor-pointer">
                <input
                  type="file"
                  accept="image/*"
                  onChange={(e) => {
                    const file = e.target.files[0];
                    if (file) {
                      const formData = new FormData();
                      formData.append('image', file);
                      const authData = JSON.parse(localStorage.getItem('authData') || '{}');
                      const userId = authData.user?.userId;
                      if (userId) {
                        fetch(`http://localhost:8081/api/images/user/${userId}`, {
                          method: 'POST',
                          body: formData,
                        })
                        .then(response => response.json())
                        .then(data => {
                          console.log('User image updated:', data.imagePath);
                          refreshData(schoolId);
                        })
                        .catch(error => console.error('Error uploading image:', error));
                      }
                    }
                  }}
                  className="hidden"
                />
                <div className="bg-white bg-opacity-90 rounded-full p-3 hover:bg-opacity-100 transition-all">
                  <Camera className="w-6 h-6 text-gray-700" />
                </div>
              </label>
            </div>
          </div>
        </div>

        {/* Overview Cards */}
        <div className="px-6 mb-8">
          <div className="mb-4">
            <h2 className="text-2xl font-bold text-gray-800">Overview</h2>
            <p className="text-sm text-gray-500">View your key stats at a glance</p>
          </div>
          
          <div className="grid grid-cols-3 gap-6">
            <div className="bg-white rounded-xl p-6 shadow-sm border border-gray-100">
              <div className="flex items-start justify-between mb-4">
                <div className="w-10 h-10 bg-green-100 rounded-lg flex items-center justify-center">
                  <span className="text-green-600">💰</span>
                </div>
                <MoreVertical className="w-4 h-4 text-gray-400" />
              </div>
              <div className="text-sm text-gray-500 mb-1">Funds Received</div>
              <div className="text-3xl font-bold text-gray-800 mb-2">
                {schoolData?.total_received ? formatCurrency(schoolData.total_received) : '$0.00'}
              </div>
              <div className="text-xs text-green-600">+ {processedProjects.length} active projects</div>
              <button className=" mt-4 text-sm font-medium flex items-center gap-1">
                View Account <ChevronRight className="w-4 h-4" />
              </button>
            </div>

            <div className="bg-white rounded-xl p-6 shadow-sm border border-gray-100">
              <div className="flex items-start justify-between mb-4">
                <div className="w-10 h-10 bg-green-100 rounded-lg flex items-center justify-center">
                  <Users className="w-5 h-5 text-green-600" />
                </div>
                <MoreVertical className="w-4 h-4 text-gray-400" />
              </div>
              <div className="text-sm text-gray-500 mb-1">Total Students</div>
              <div className="text-3xl font-bold text-gray-800 mb-2">{studentsData.filter(s => (s.schoolId || s.school_id) == schoolId).length}</div>
              <div className="text-xs text-red-600">+ {highRiskStudents.length} High Risk Students</div>
              <button className="secondary !border-0 hover:!bg-gray-50 hover:!text-[#0E792E] mt-4 font-medium flex items-center gap-1">
                View students <ChevronRight className="w-4 h-4" />
              </button>
            </div>

            <div className="bg-white rounded-xl p-6 shadow-sm border border-gray-100">
              <div className="flex items-start justify-between mb-4">
                <div className="w-10 h-10 bg-green-100 rounded-lg flex items-center justify-center">
                  <Briefcase className="w-5 h-5 text-green-600" />
                </div>
                <MoreVertical className="w-4 h-4 text-gray-400" />
              </div>
              <div className="text-sm text-gray-500 mb-1">Active Projects</div>
              <div className="text-3xl font-bold text-gray-800 mb-2">{processedProjects.length}</div>
              <div className="text-xs text-red-600">+ 2 Report Pending</div>
              <button className="secondary !border-0 hover:!bg-gray-50 hover:!text-[#0E792E] mt-4 text-sm  font-medium flex items-center gap-1">
                View projects <ChevronRight className="w-4 h-4" />
              </button>
            </div>
          </div>
        </div>

        {/* Active Students */}
        <div className="px-6 mb-8">
          <div className="flex items-center justify-between mb-4">
            <div className="flex items-center gap-2">
              <span className="text-green-600">👨‍🎓</span>
              <h2 className="text-xl font-bold text-gray-800">Active Students</h2>
            </div>
            <button 
              onClick={() => navigate(`/students/${schoolId}`)}
              className="secondary !border-0 hover:!bg-gray-50 hover:!text-[#0E792E] text-sm font-medium flex items-center gap-1"
            >
              View All <ChevronRight className="w-4 h-4" />
            </button>
          </div>

          <div className="grid grid-cols-4 gap-4">
            <div 
           onClick={() => setIsModalOpen(true)}
            className="bg-white rounded-xl p-6 border-2 border-dashed border-gray-300 flex flex-col items-center justify-center min-h-[280px] hover:border-green-400 transition-colors cursor-pointer">
              <Plus className="w-12 h-12 text-gray-400 mb-4" />
              <div className="text-sm font-semibold text-gray-800 mb-1">Add New Student</div>
              <div className="text-xs text-gray-500 text-center">Add student details to ensure accurate records and streamline every learner</div>
            </div>

            {processedStudents.length > 0 ? processedStudents.map((student) => (
              <div key={student.student_id} className="bg-white rounded-xl overflow-hidden shadow-sm border border-gray-100 cursor-pointer hover:shadow-md transition-shadow" onClick={() => handleStudentClick(student.student_id)}>
                <div className="relative h-40  ">
                  <span className="absolute top-3 right-3 bg-white px-2 py-1 rounded-full text-xs font-medium text-orange-600">
                    {student.risk_status || 'At Risk'}
                  </span>
                
                </div>
                <div className="p-4">
                  <div className="text-xs text-green-600 mb-1">Scholarship from @anisha3228</div>
                  <div className="text-sm font-semibold text-gray-800 mb-2">
                    {student.student_name}, {formatClassLevel(student.class_level)}
                  </div>
                  <div className="text-xs text-gray-500 mb-4">
                    Fund Received: {formatCurrency(student.scholarship_amount || 0)} / {formatCurrency(student.total_amount || 0)}
                  </div>
                  <button className="secondary !border-0 hover:!bg-gray-50 hover:!text-[#0E792E] w-full py-2 text-sm rounded-lg hover:bg-green-50 transition-colors" onClick={(e) => { e.stopPropagation(); console.log('Update report for student:', student.student_id); }}>
                    Update Report
                  </button>
                </div>
              </div>
            )) : (
              <div className="col-span-3 text-center py-12 text-gray-500">
                <div className="flex flex-col items-center gap-3">
                  <span className="text-4xl">👨‍🎓</span>
                  <span className="text-lg font-medium">No students registered yet</span>
                  <span className="text-sm">Add students to start tracking their progress</span>
                </div>
              </div>
            )}
          </div>
        </div>

        {/* Active Projects */}
        <div className="px-6 mb-8">
          <div className="flex items-center justify-between mb-4">
            <div className="flex items-center gap-2">
              <span className="text-blue-600">📚</span>
              <h2 className="text-xl font-bold text-gray-800">Active Projects</h2>
            </div>
            <button 
              onClick={() => navigate(`/projects/${schoolId}`)}
              className="secondary !border-0 hover:!bg-gray-50 hover:!text-[#0E792E] text-sm font-medium flex items-center gap-1"
            >
              View All <ChevronRight className="w-4 h-4" />
            </button>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
            <div
            onClick={() => setProjectModalOpen(true)}
            className="bg-white rounded-xl p-6 border-2 border-dashed border-gray-300 flex flex-col items-center justify-center min-h-[280px] hover:border-green-400 transition-colors cursor-pointer">
              <Plus className="w-12 h-12 text-gray-400 mb-4" />
              <div className="text-sm font-semibold text-gray-800 mb-1">Create New Project</div>
              <div className="text-xs text-gray-500 text-center">Add request details to ensure accurate records and support for every learner</div>
            </div>

            {processedProjects.length > 0 ? processedProjects.map((project) => (
              <SchoolProjectCard
                key={project.projectId || project.project_id}
                project={project}
                onViewDetails={handleProjectClick}
                showAllButtons={false}
              />
            )) : (
              <div className="col-span-3 text-center py-12 text-gray-500">
                <div className="flex flex-col items-center gap-3">
                  <span className="text-4xl">📚</span>
                  <span className="text-lg font-medium">No projects created yet</span>
                  <span className="text-sm">Create projects to start receiving donations</span>
                </div>
              </div>
            )}
          </div>
        </div>

        {/* Bottom Section */}
        <div className="px-6 pb-8 grid grid-cols-3 gap-6">
          {/* Recent Donations */}
          <div className="col-span-2 bg-white rounded-xl p-6 shadow-sm border border-gray-100">
            <div className="flex items-center justify-between mb-4">
              <h2 className="text-lg font-bold text-gray-800">Recent Donations</h2>
              <div className="flex gap-2">
                <button className="secondary !border-0 hover:!bg-gray-50 hover:!text-[#0E792E] px-3 py-1.5 text-sm rounded-lg flex items-center gap-1">
                  <Download className="w-4 h-4" />
                  Download
                </button>
                <button className="secondary !border-0 hover:!bg-gray-50 hover:!text-[#0E792E] px-3 py-1.5 text-sm rounded-lg ">
                  View report
                </button>
              </div>
            </div>

            <div className="overflow-x-auto">
              <table className="w-full">
                <thead>
                  <tr className="border-b border-gray-200">
                    <th className="text-left text-xs font-medium text-gray-500 pb-3 min-w-[120px]">Donor</th>
                    <th className="text-left text-xs font-medium text-gray-500 pb-3 min-w-[100px]">Reference</th>
                    <th className="text-left text-xs font-medium text-gray-500 pb-3 min-w-[80px]">Amount</th>
                    <th className="text-left text-xs font-medium text-gray-500 pb-3 min-w-[100px]">Date</th>
                    <th className="text-left text-xs font-medium text-gray-500 pb-3 min-w-[80px]">Status</th>
                  </tr>
                </thead>
                <tbody>
                  <tr>
                    <td colSpan="5" className="py-8 text-center text-gray-500">
                      <div className="flex flex-col items-center gap-2">
                        <span className="text-2xl">💰</span>
                        <span className="text-sm">No donations yet</span>
                      </div>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>

            <div className="flex items-center justify-between mt-4 text-sm text-gray-600">
              <span>Page 1 of 10</span>
              <div className="flex gap-2">
                <button className="secondary !border-0 hover:!bg-gray-50 hover:!text-[#0E792E] px-3 py-1 rounded-lg">Previous</button>
                <button className="secondary !border-0 hover:!bg-gray-50 hover:!text-[#0E792E] px-3 py-1  rounded-lg ">Next</button>
              </div>
            </div>
          </div>

          {/* High Risk Students */}
          <div className="bg-white rounded-xl p-6 shadow-sm border border-gray-100">
            <h2 className="text-lg font-bold text-gray-800 mb-4">High Risk Students</h2>
            
            <div className="space-y-3">
              {highRiskStudents.length > 0 ? highRiskStudents.map((student) => (
                <div key={student.student_id} className="border border-red-200 rounded-lg p-4 bg-red-50 hover:bg-red-100 transition-colors cursor-pointer" onClick={() => handleStudentClick(student.student_id)}>
                  <div className="text-sm font-semibold text-gray-800 mb-3 flex justify-between items-center">
                    <span>{student.student_name}, {formatClassLevel(student.class_level)}</span>
              
                  </div>
                  <div className="space-y-2 mb-3">
                    <div className="grid grid-cols-1 gap-1 text-xs">
                      <span className="text-gray-600">📊 Attendance: {student.attendance_rate || 0}%</span>
                      <span className="text-gray-600">📈 Performance: {student.performance_score || 0}%</span>
                    </div>
                    <div className="text-xs text-red-600 font-medium">
                      Risk Score: {student.risk_score || 'High'}
                    </div>
                  </div>
                  <button className="secondary !border-0 hover:!bg-gray-50 hover:!text-[#0E792E] w-full py-1.5 text-xs rounded-lg transition-colors flex items-center justify-center gap-1">
                    Take Action <ChevronRight className="w-3 h-3" />
                  </button>
                </div>
              )) : (
                <div className="text-center py-8 text-gray-500">
                  <div className="flex flex-col items-center gap-2">
                    <span className="text-2xl">✅</span>
                    <span className="text-sm">No high-risk students</span>
                    <span className="text-xs">All students are performing well</span>
                  </div>
                </div>
              )}
            </div>

            <button className="secondary !border-0 hover:!bg-gray-50 hover:!text-[#0E792E] w-full mt-4 text-sm font-medium flex items-center justify-center gap-1">
              View All <ChevronRight className="w-4 h-4" />
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}