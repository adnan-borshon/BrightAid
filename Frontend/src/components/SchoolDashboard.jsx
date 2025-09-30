import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { ChevronRight, MoreVertical, Users, Briefcase, Plus, Download } from 'lucide-react';
import Sidebar from './DashSidebar';
import SchoolStudents from './SchoolStudents';

// Empty data structure
const emptyData = {
  school: null,
  students: [],
  projects: [],
  donations: [],
  highRiskStudents: []
};

export default function SchoolDashboard() {
  const { schoolId } = useParams();
  const [activeNav, setActiveNav] = useState('Dashboard');
  const [loading, setLoading] = useState(true);
  const [data, setData] = useState(emptyData);
  const [apiError, setApiError] = useState(false);
  const [selectedStudent, setSelectedStudent] = useState(null);
  const [selectedProject, setSelectedProject] = useState(null);
  
  // API Base URL - Update this to your backend URL
  const API_BASE_URL = 'http://localhost:8081/api';

  useEffect(() => {
    if (schoolId) {
      fetchDashboardData();
    }
  }, [schoolId]);

  const fetchDashboardData = async () => {
    setLoading(true);
    try {
      console.log('Fetching data for school ID:', schoolId);
      
      // Fetch all data in parallel - using available endpoints
      const [schoolRes, studentsRes, projectsRes, donationsRes] = await Promise.all([
        fetch(`${API_BASE_URL}/schools/${schoolId}`).catch(() => null),
        fetch(`${API_BASE_URL}/students`).catch(() => null),
        fetch(`${API_BASE_URL}/school-projects`).catch(() => null),
        fetch(`${API_BASE_URL}/donations`).catch(() => null),
      ]);

      const newData = { ...emptyData };
      
      if (schoolRes && schoolRes.ok) {
        const schoolData = await schoolRes.json();
        newData.school = {
          ...schoolData,
          total_students: schoolData.totalStudents || schoolData.total_students || 0,
          active_projects: schoolData.activeProjects || schoolData.active_projects || 0,
          total_received: schoolData.totalReceived || schoolData.total_received || 0
        };
      }
      
      if (studentsRes && studentsRes.ok) {
        const studentsData = await studentsRes.json();
        const students = Array.isArray(studentsData) ? studentsData : studentsData.data || [];
        
        // Filter students by school ID and map data
        const schoolStudents = students.filter(student => 
          (student.schoolId || student.school_id) == schoolId
        );
        
        newData.students = schoolStudents.slice(0, 3).map(student => ({
          student_id: student.studentId || student.student_id,
          student_name: student.studentName || student.student_name,
          class_level: student.classLevel || student.class_level,
          scholarship_amount: student.scholarshipAmount || student.scholarship_amount || 0,
          total_amount: student.totalAmount || student.total_amount || 0,
          risk_status: student.riskStatus || student.risk_status || 'Low Risk',
          attendance_rate: student.attendanceRate || student.attendance_rate || 0,
          performance_score: student.performanceScore || student.performance_score || 0
        }));
        
        // Filter high risk students for this school
        newData.highRiskStudents = schoolStudents.filter(student => {
          const riskStatus = student.riskStatus || student.risk_status;
          return riskStatus && (riskStatus.toLowerCase().includes('high') || riskStatus.toLowerCase().includes('at risk'));
        }).slice(0, 4).map(student => ({
          student_id: student.studentId || student.student_id,
          student_name: student.studentName || student.student_name,
          class_level: student.classLevel || student.class_level,
          attendance_rate: student.attendanceRate || student.attendance_rate || 0,
          performance_score: student.performanceScore || student.performance_score || 0,
          risk_score: student.riskScore || student.risk_score || 0
        }));
      }
      
      if (projectsRes && projectsRes.ok) {
        const projectsData = await projectsRes.json();
        const projects = Array.isArray(projectsData) ? projectsData : projectsData.data || [];
        
        // Filter projects by school ID
        const schoolProjects = projects.filter(project => 
          (project.schoolId || project.school_id) == schoolId
        );
        
        newData.projects = schoolProjects.slice(0, 3).map(project => ({
          project_id: project.projectId || project.project_id,
          project_title: project.projectTitle || project.project_title,
          raised_amount: project.raisedAmount || project.raised_amount || 0,
          required_amount: project.requiredAmount || project.required_amount || 0,
          status: project.status || 'active',
          progress: project.progress || Math.round(((project.raisedAmount || project.raised_amount || 0) / (project.requiredAmount || project.required_amount || 1)) * 100)
        }));
      }
      
      if (donationsRes && donationsRes.ok) {
        const donationsData = await donationsRes.json();
        const donations = Array.isArray(donationsData) ? donationsData : donationsData.data || [];
        
        // Filter donations by school ID (assuming donations have school reference)
        const schoolDonations = donations.filter(donation => 
          (donation.schoolId || donation.school_id) == schoolId ||
          (donation.recipientId || donation.recipient_id) == schoolId
        );
        
        newData.donations = schoolDonations.slice(0, 7).map(donation => ({
          donation_id: donation.donationId || donation.donation_id,
          donor_name: donation.donorName || donation.donor_name || 'Anonymous',
          amount: donation.amount || 0,
          donated_at: donation.donatedAt || donation.donated_at || new Date().toLocaleDateString(),
          payment_status: donation.paymentStatus || donation.payment_status || 'pending',
          transaction_reference: donation.transactionReference || donation.transaction_reference || 'N/A'
        }));
      }

      // Update school data with calculated counts
      if (newData.school) {
        newData.school.total_students = newData.students.length;
        newData.school.active_projects = newData.projects.length;
      }
      
      setData(newData);
      setApiError(false);
    } catch (error) {
      console.error('Error fetching dashboard data:', error);
      setApiError(true);
      setData(emptyData);
    } finally {
      setLoading(false);
    }
  };

  const formatCurrency = (amount) => {
    return `$${Math.abs(amount).toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`;
  };

  const formatClassLevel = (classLevel) => {
    return classLevel ? classLevel.replace('_', ' ').replace(/\b\w/g, l => l.toUpperCase()) : 'N/A';
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
    setSelectedStudent(studentId);
    // You can navigate to student detail page or show modal
    // navigate(`/student/${studentId}`);
  };

  const handleProjectClick = (projectId) => {
    console.log('Viewing project details for ID:', projectId);
    setSelectedProject(projectId);
    // You can navigate to project detail page or show modal
    // navigate(`/project/${projectId}`);
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center h-screen bg-gray-50">
        <div className="text-center">
          <div className="w-16 h-16 border-4 border-green-600 border-t-transparent rounded-full animate-spin mx-auto mb-4"></div>
          <p className="text-gray-600">Loading dashboard...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="flex h-screen bg-gray-50">
      <Sidebar activeNav={activeNav} setActiveNav={setActiveNav} schoolData={data.school} />

      {/* Main Content */}
      <div className="flex-1 overflow-auto">
        {activeNav === 'Students' ? (
          <SchoolStudents schoolId={schoolId} />
        ) : (
          <>
            {apiError && (
              <div className="m-6 p-4 bg-yellow-50 border border-yellow-200 rounded-lg text-sm text-yellow-800">
                ⚠ Using demo data. Connect to backend at <code className="bg-yellow-100 px-2 py-1 rounded">{API_BASE_URL}</code>
              </div>
            )}

        {/* Hero Banner */}
        <div className="bg-gradient-to-r from-green-50 to-green-100 m-6 rounded-2xl p-8 flex items-center justify-between relative overflow-hidden">
          <div className="flex-1 z-10">
            <div className="text-lg text-gray-600 mb-1">Welcome back,</div>
            <div className="text-3xl font-bold text-gray-800 mb-2 flex items-center gap-2">
              {data.school?.schoolName || data.school?.school_name || `School ${schoolId}`}
              {data.school && <span className="text-blue-500">✓</span>}
          
            </div>
            <div className="text-sm text-gray-600 mb-2">
              {data.school ? 'Your dedication is transforming education and reducing dropouts in your community' : 'Welcome to your school dashboard'}
            </div>
            <div className="text-xs text-gray-500 flex items-center gap-1">
              <span>📍</span>
              {data.school?.address || 'Address not available'}
            </div>
          </div>
          <div className="w-96 h-48 rounded-xl overflow-hidden shadow-lg">
            <div className="w-full h-full bg-gradient-to-br from-green-600 to-green-800"></div>
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
                {data.school?.total_received ? formatCurrency(data.school.total_received) : '$0.00'}
              </div>
              <div className="text-xs text-green-600">+ {data.school?.active_projects || 0} active projects</div>
              <button className="secondary !border-0 hover:!bg-gray-50 hover:!text-[#0E792E] mt-4 text-sm text-green-600 hover:text-green-700 font-medium flex items-center gap-1">
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
              <div className="text-3xl font-bold text-gray-800 mb-2">{data.school?.total_students || 0}</div>
              <div className="text-xs text-red-600">+ {data.highRiskStudents?.length || 0} High Risk Students</div>
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
              <div className="text-3xl font-bold text-gray-800 mb-2">{data.school?.active_projects || 0}</div>
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
            <button className="secondary !border-0 hover:!bg-gray-50 hover:!text-[#0E792E] text-sm font-medium flex items-center gap-1">
              View All <ChevronRight className="w-4 h-4" />
            </button>
          </div>

          <div className="grid grid-cols-4 gap-4">
            <div className="bg-white rounded-xl p-6 border-2 border-dashed border-gray-300 flex flex-col items-center justify-center min-h-[280px] hover:border-green-400 transition-colors cursor-pointer">
              <Plus className="w-12 h-12 text-gray-400 mb-4" />
              <div className="text-sm font-semibold text-gray-800 mb-1">Add New Student</div>
              <div className="text-xs text-gray-500 text-center">Add student details to ensure accurate records and streamline every learner</div>
            </div>

            {data.students.length > 0 ? data.students.slice(0, 3).map((student) => (
              <div key={student.student_id} className="bg-white rounded-xl overflow-hidden shadow-sm border border-gray-100 cursor-pointer hover:shadow-md transition-shadow" onClick={() => handleStudentClick(student.student_id)}>
                <div className="relative h-40  ">
                  <span className="absolute top-3 right-3 bg-white px-2 py-1 rounded-full text-xs font-medium text-orange-600">
                    {student.risk_status || 'At Risk'}
                  </span>
                  <span className="absolute top-3 left-3 bg-black bg-opacity-50 text-white px-2 py-1 rounded text-xs">
                    ID: {student.student_id}
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
            <button className="secondary !border-0 hover:!bg-gray-50 hover:!text-[#0E792E] text-sm font-medium flex items-center gap-1">
              View All <ChevronRight className="w-4 h-4" />
            </button>
          </div>

          <div className="grid grid-cols-4 gap-4">
            <div className="bg-white rounded-xl p-6 border-2 border-dashed border-gray-300 flex flex-col items-center justify-center min-h-[280px] hover:border-green-400 transition-colors cursor-pointer">
              <Plus className="w-12 h-12 text-gray-400 mb-4" />
              <div className="text-sm font-semibold text-gray-800 mb-1">Create New Project</div>
              <div className="text-xs text-gray-500 text-center">Add request details to ensure accurate records and support for every learner</div>
            </div>

            {data.projects.length > 0 ? data.projects.slice(0, 3).map((project) => {
              const progress = Math.round((project.raised_amount / project.required_amount) * 100) || project.progress || 0;
              return (
                <div key={project.project_id} className="bg-white rounded-xl overflow-hidden shadow-sm border border-gray-100 cursor-pointer hover:shadow-md transition-shadow" onClick={() => handleProjectClick(project.project_id)}>
                  <div className="relative h-40 bg-gradient-to-br from-green-700 to-green-900">
                    <span className={`absolute top-3 right-3 px-2 py-1 rounded-full text-xs font-medium ${getStatusColor(project.status)}`}>
                      • {project.status?.replace('_', ' ').toUpperCase() || 'ACTIVE'}
                    </span>
                    <span className="absolute top-3 left-3 bg-black bg-opacity-50 text-white px-2 py-1 rounded text-xs">
                      ID: {project.project_id}
                    </span>
                  </div>
                  <div className="p-4">
                    <div className="text-xs text-gray-600 mb-1">Sponsored by @anisha3228</div>
                    <div className="text-sm font-semibold text-gray-800 mb-2">{project.project_title}</div>
                    <div className="text-xs text-gray-500 mb-3">
                      Fund Received: {formatCurrency(project.raised_amount)} / {formatCurrency(project.required_amount)}
                    </div>
                    <div className="mb-4">
                      <div className="flex justify-between text-xs text-gray-500 mb-1">
                        <span>{progress}%</span>
                      </div>
                      <div className="w-full bg-gray-200 rounded-full h-2">
                        <div className="bg-green-600 h-2 rounded-full" style={{ width: `${progress}%` }}></div>
                      </div>
                    </div>
                    <button className="secondary !border-0 hover:!bg-gray-50 hover:!text-[#0E792E] w-full py-2 text-sm rounded-lg hover:bg-green-50 transition-colors" onClick={(e) => { e.stopPropagation(); console.log('Update report for project:', project.project_id); }}>
                      Update Report
                    </button>
                  </div>
                </div>
              );
            }) : (
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
                  {data.donations.length > 0 ? data.donations.map((donation) => (
                    <tr key={donation.donation_id} className="border-b border-gray-100 hover:bg-gray-50">
                      <td className="py-3">
                        <div className="flex items-center gap-2">
                          <div className={`w-8 h-8 rounded-full flex items-center justify-center text-sm font-semibold ${
                            donation.donor_name?.toLowerCase().includes('spotify') ? 'bg-green-100 text-green-700' :
                            donation.donor_name?.toLowerCase().includes('stripe') ? 'bg-purple-100 text-purple-700' :
                            donation.donor_name?.toLowerCase().includes('figma') ? 'bg-gray-800 text-white' :
                            'bg-gray-100 text-gray-700'
                          }`}>
                            {getDonorLogo(donation.donor_name)}
                          </div>
                          <span className="text-sm text-gray-800 truncate">{donation.donor_name || 'Anonymous'}</span>
                        </div>
                      </td>
                      <td className="text-sm text-gray-600 truncate">{donation.transaction_reference || 'N/A'}</td>
                      <td className={`text-sm font-medium ${donation.amount >= 0 ? 'text-green-600' : 'text-gray-800'}`}>
                        {donation.amount >= 0 ? '+' : ''}{formatCurrency(donation.amount)}
                      </td>
                      <td className="text-sm text-gray-600">
                        {new Date(donation.donated_at).toLocaleDateString() || donation.donated_at}
                      </td>
                      <td>
                        <span className={`text-xs px-2 py-1 rounded-full ${getStatusColor(donation.payment_status)}`}>
                          {donation.payment_status?.toUpperCase() || 'PENDING'}
                        </span>
                      </td>
                    </tr>
                  )) : (
                    <tr>
                      <td colSpan="5" className="py-8 text-center text-gray-500">
                        <div className="flex flex-col items-center gap-2">
                          <span className="text-2xl">💰</span>
                          <span className="text-sm">No donations yet</span>
                        </div>
                      </td>
                    </tr>
                  )}
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
              {data.highRiskStudents.length > 0 ? data.highRiskStudents.map((student) => (
                <div key={student.student_id} className="border border-red-200 rounded-lg p-4 bg-red-50 hover:bg-red-100 transition-colors cursor-pointer" onClick={() => handleStudentClick(student.student_id)}>
                  <div className="text-sm font-semibold text-gray-800 mb-3 flex justify-between items-center">
                    <span>{student.student_name}, {formatClassLevel(student.class_level)}</span>
                    <span className="text-xs bg-red-200 px-2 py-1 rounded">ID: {student.student_id}</span>
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
          </>
        )}
      </div>
    </div>
  );
}