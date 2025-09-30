import React, { useState, useEffect } from 'react';
import { ChevronRight, MoreVertical, Users, Briefcase, Plus, Download } from 'lucide-react';
import Sidebar from './DashSidebar';

// Mock data as fallback
const mockData = {
  school: {
    school_name: "Rampur Government Primary School",
    address: "Rampur, Teknaf, Cox's Bazar",
    total_students: 14,
    active_projects: 5,
    total_received: 1280
  },
  students: [
    { student_id: 1, student_name: 'Jannatun Pinki', class_level: 'class_2', scholarship_amount: 50000, total_amount: 150000, risk_status: 'At Risk' },
    { student_id: 2, student_name: 'Rahima Khatun', class_level: 'class_2', scholarship_amount: 50000, total_amount: 150000, risk_status: 'High Risk' },
    { student_id: 3, student_name: 'Abdul Karim', class_level: 'class_2', scholarship_amount: 50000, total_amount: 150000, risk_status: 'At Risk' },
  ],
  projects: [
    { project_id: 1, project_title: 'Library Book Collection', raised_amount: 50000, required_amount: 150000, status: 'active', progress: 70 },
    { project_id: 2, project_title: 'Library Book Collection', raised_amount: 50000, required_amount: 150000, status: 'in_progress', progress: 70 },
    { project_id: 3, project_title: 'Library Book Collection', raised_amount: 50000, required_amount: 150000, status: 'funded', progress: 70 },
  ],
  donations: [
    { donation_id: 1, donor_name: 'Spotify', amount: -18.99, donated_at: 'Wed 1:00pm', payment_status: 'completed', transaction_reference: '1111111' },
    { donation_id: 2, donor_name: 'A Coffee', amount: -4.50, donated_at: 'Wed 7:20am', payment_status: 'pending', transaction_reference: '1111111' },
    { donation_id: 3, donor_name: 'Stripe', amount: 88.00, donated_at: 'Wed 2:45am', payment_status: 'completed', transaction_reference: '1111111' },
    { donation_id: 4, donor_name: 'Figma', amount: -15.00, donated_at: 'Tue 6:10pm', payment_status: 'completed', transaction_reference: '1111111' },
    { donation_id: 5, donor_name: 'TBF Bakery', amount: -12.50, donated_at: 'Tue 7:52am', payment_status: 'pending', transaction_reference: '1111111' },
    { donation_id: 6, donor_name: 'Fresh F&V', amount: -40.20, donated_at: 'Tue 12:15pm', payment_status: 'pending', transaction_reference: '1111111' },
    { donation_id: 7, donor_name: 'Stripe', amount: 88.00, donated_at: 'Tue 9:40pm', payment_status: 'pending', transaction_reference: '1111111' },
  ],
  highRiskStudents: [
    { student_id: 1, student_name: 'Rahima Khatun', class_level: 'class_5', attendance_rate: 45, performance_score: 52, risk_score: 45 },
    { student_id: 2, student_name: 'Rahima Khatun', class_level: 'class_5', attendance_rate: 45, performance_score: 52, risk_score: 45 },
    { student_id: 3, student_name: 'Rahima Khatun', class_level: 'class_5', attendance_rate: 45, performance_score: 52, risk_score: 45 },
    { student_id: 4, student_name: 'Rahima Khatun', class_level: 'class_5', attendance_rate: 45, performance_score: 52, risk_score: 45 },
  ]
};

export default function SchoolDashboard() {
  const [activeNav, setActiveNav] = useState('Dashboard');
  const [loading, setLoading] = useState(true);
  const [data, setData] = useState(mockData);
  const [apiError, setApiError] = useState(false);
  
  // API Base URL - Update this to your backend URL
  const API_BASE_URL = 'http://localhost:8080/api';

  useEffect(() => {
    fetchDashboardData();
  }, []);

  const fetchDashboardData = async () => {
    setLoading(true);
    try {
      // Fetch all data in parallel
      const [schoolRes, studentsRes, projectsRes, donationsRes, riskStudentsRes] = await Promise.all([
        fetch(`${API_BASE_URL}/schools/dashboard`).catch(() => null),
        fetch(`${API_BASE_URL}/students?limit=3`).catch(() => null),
        fetch(`${API_BASE_URL}/projects?status=active&limit=3`).catch(() => null),
        fetch(`${API_BASE_URL}/donations?limit=7`).catch(() => null),
        fetch(`${API_BASE_URL}/students/high-risk?limit=4`).catch(() => null),
      ]);

      const newData = { ...mockData };
      
      if (schoolRes && schoolRes.ok) {
        const schoolData = await schoolRes.json();
        newData.school = schoolData;
      }
      
      if (studentsRes && studentsRes.ok) {
        const studentsData = await studentsRes.json();
        newData.students = studentsData.data || studentsData;
      }
      
      if (projectsRes && projectsRes.ok) {
        const projectsData = await projectsRes.json();
        newData.projects = projectsData.data || projectsData;
      }
      
      if (donationsRes && donationsRes.ok) {
        const donationsData = await donationsRes.json();
        newData.donations = donationsData.data || donationsData;
      }
      
      if (riskStudentsRes && riskStudentsRes.ok) {
        const riskData = await riskStudentsRes.json();
        newData.highRiskStudents = riskData.data || riskData;
      }

      setData(newData);
      setApiError(false);
    } catch (error) {
      console.error('Error fetching dashboard data:', error);
      setApiError(true);
      setData(mockData);
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
              {data.school?.school_name || 'Rampur Government Primary School'}
              <span className="text-blue-500">✓</span>
            </div>
            <div className="text-sm text-gray-600 mb-2">Your dedication is transforming education and reducing dropouts in your community</div>
            <div className="text-xs text-gray-500 flex items-center gap-1">
              <span>📍</span>
              {data.school?.address || "Rampur, Teknaf, Cox's Bazar"}
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
                {formatCurrency(data.school?.total_received || 1280)}
              </div>
              <div className="text-xs text-green-600">+ {data.school?.active_projects || 3} active projects</div>
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
              <div className="text-3xl font-bold text-gray-800 mb-2">{data.school?.total_students || 14}</div>
              <div className="text-xs text-red-600">+ {data.highRiskStudents?.length || 3} High Risk Students</div>
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
              <div className="text-3xl font-bold text-gray-800 mb-2">{data.school?.active_projects || 5}</div>
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

            {data.students.slice(0, 3).map((student) => (
              <div key={student.student_id} className="bg-white rounded-xl overflow-hidden shadow-sm border border-gray-100">
                <div className="relative h-40 bg-gradient-to-br from-green-400 to-green-600">
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
                    Fund Received: {formatCurrency(student.scholarship_amount || 50000)} / {formatCurrency(student.total_amount || 150000)}
                  </div>
                  <button className="secondary !border-0 hover:!bg-gray-50 hover:!text-[#0E792E] w-full py-2 text-sm rounded-lg hover:bg-green-50 transition-colors">
                    Update Report
                  </button>
                </div>
              </div>
            ))}
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

            {data.projects.slice(0, 3).map((project) => {
              const progress = Math.round((project.raised_amount / project.required_amount) * 100) || project.progress || 70;
              return (
                <div key={project.project_id} className="bg-white rounded-xl overflow-hidden shadow-sm border border-gray-100">
                  <div className="relative h-40 bg-gradient-to-br from-green-700 to-green-900">
                    <span className={`absolute top-3 right-3 px-2 py-1 rounded-full text-xs font-medium ${getStatusColor(project.status)}`}>
                      • {project.status?.replace('_', ' ').toUpperCase() || 'ACTIVE'}
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
                    <button className="secondary !border-0 hover:!bg-gray-50 hover:!text-[#0E792E] w-full py-2 text-sm rounded-lg hover:bg-green-50 transition-colors">
                      Update Report
                    </button>
                  </div>
                </div>
              );
            })}
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

            <table className="w-full">
              <thead>
                <tr className="border-b border-gray-200">
                  <th className="text-left text-xs font-medium text-gray-500 pb-3">Donor</th>
                  <th className="text-left text-xs font-medium text-gray-500 pb-3">Project/Student ID</th>
                  <th className="text-left text-xs font-medium text-gray-500 pb-3">Amount</th>
                  <th className="text-left text-xs font-medium text-gray-500 pb-3">Date</th>
                  <th className="text-left text-xs font-medium text-gray-500 pb-3">Status</th>
                </tr>
              </thead>
              <tbody>
                {data.donations.map((donation) => (
                  <tr key={donation.donation_id} className="border-b border-gray-100">
                    <td className="py-3 flex items-center gap-2">
                      <div className={`w-8 h-8 rounded-full flex items-center justify-center text-sm font-semibold ${
                        donation.donor_name?.toLowerCase().includes('spotify') ? 'bg-green-100 text-green-700' :
                        donation.donor_name?.toLowerCase().includes('stripe') ? 'bg-purple-100 text-purple-700' :
                        donation.donor_name?.toLowerCase().includes('figma') ? 'bg-gray-800 text-white' :
                        'bg-gray-100 text-gray-700'
                      }`}>
                        {getDonorLogo(donation.donor_name)}
                      </div>
                      <span className="text-sm text-gray-800">{donation.donor_name || 'Anonymous'}</span>
                    </td>
                    <td className="text-sm text-gray-600">{donation.transaction_reference}</td>
                    <td className={`text-sm font-medium ${donation.amount >= 0 ? 'text-green-600' : 'text-gray-800'}`}>
                      {donation.amount >= 0 ? '+' : '-'} {formatCurrency(donation.amount)}
                    </td>
                    <td className="text-sm text-gray-600">{donation.donated_at}</td>
                    <td>
                      <span className={`text-xs px-2 py-1 rounded-full ${getStatusColor(donation.payment_status)}`}>
                        • {donation.payment_status?.toUpperCase() || 'PENDING'}
                      </span>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>

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
              {data.highRiskStudents.map((student) => (
                <div key={student.student_id} className="border border-red-200 rounded-lg p-4 bg-red-50">
                  <div className="text-sm font-semibold text-gray-800 mb-3">
                    {student.student_name}, {formatClassLevel(student.class_level)}
                  </div>
                  <div className="space-y-2 mb-3">
                    <div className="flex items-center gap-2 text-xs">
                      <span className="text-gray-600">📊 Attendance: {student.attendance_rate}%</span>
                      <span className="text-gray-600">📈 Performance: {student.performance_score}%</span>
                    </div>
                    <div className="text-xs text-red-600 font-medium">Risk Score: {student.risk_score}</div>
                  </div>
                  <button className="secondary !border-0 hover:!bg-gray-50 hover:!text-[#0E792E] w-full py-1.5 text-xs  rounded-lg  transition-colors flex items-center justify-center gap-1">
                    Take Action <ChevronRight className="w-3 h-3" />
                  </button>
                </div>
              ))}
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