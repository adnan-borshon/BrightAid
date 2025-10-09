import { useState, useEffect } from "react";
import { useParams } from 'react-router-dom';
import { Search, DollarSign, Users, Building2, TrendingUp, FileText, CheckCircle, Download, Eye, Calendar, Filter, Target, Award, GraduationCap } from "lucide-react";
import DashSidebar from './DashSidebar';

export default function SchoolReporting() {
  const { schoolId } = useParams();
  const [searchTerm, setSearchTerm] = useState("");
  const [filterPurpose, setFilterPurpose] = useState("all");
  const [activeTab, setActiveTab] = useState("overview");
  const [dateRange, setDateRange] = useState("all");
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchSchoolData();
  }, [schoolId]);

  const [donationData, setDonationData] = useState([]);
  const [projectData, setProjectData] = useState([]);
  const [studentData, setStudentData] = useState([]);

  const fetchSchoolData = async () => {
    try {
      setLoading(true);
      
      // Fetch donations received by this school
      const donationsResponse = await fetch(`http://localhost:8081/api/donations/school/${schoolId}`);
      const donations = donationsResponse.ok ? await donationsResponse.json() : [];
      
      // Fetch school projects
      const projectsResponse = await fetch(`http://localhost:8081/api/school-projects/school/${schoolId}`);
      const projects = projectsResponse.ok ? await projectsResponse.json() : [];
      
      // Fetch school students
      const studentsResponse = await fetch(`http://localhost:8081/api/students/school/${schoolId}`);
      const students = studentsResponse.ok ? await studentsResponse.json() : [];
      
      setDonationData(donations);
      setProjectData(projects);
      setStudentData(students);
      
    } catch (err) {
      console.error('Error fetching school data:', err);
      setDonationData([]);
      setProjectData([]);
      setStudentData([]);
    } finally {
      setLoading(false);
    }
  };

  const totalReceived = donationData.reduce((sum, d) => sum + parseFloat(d.amount || 0), 0);
  const completedDonations = donationData.filter(d => d.paymentStatus === 'COMPLETED');
  const totalCompleted = completedDonations.reduce((sum, d) => sum + parseFloat(d.amount || 0), 0);
  
  // Calculate beneficiaries and impact
  const totalBeneficiaries = studentData.length;
  const activeProjects = projectData.filter(p => p.projectStatus === 'ACTIVE' || p.projectStatus === 'APPROVED').length;
  const completedProjects = projectData.filter(p => p.projectStatus === 'COMPLETED').length;
  
  // Calculate donations by purpose
  const purposeBreakdown = donationData.reduce((acc, item) => {
    const purpose = item.purpose || 'GENERAL_SUPPORT';
    acc[purpose] = (acc[purpose] || 0) + parseFloat(item.amount || 0);
    return acc;
  }, {});

  // Calculate high-risk students
  const highRiskStudents = studentData.filter(s => s.dropoutRisk === 'HIGH').length;

  const filterDonations = () => {
    return donationData.filter((donation) => {
      const projectName = donation.project?.projectTitle || '';
      const studentName = donation.student ? `${donation.student.firstName} ${donation.student.lastName}` : '';
      const donorName = donation.donor?.donorName || 'Anonymous';
      const purpose = donation.purpose || 'GENERAL_SUPPORT';
      
      const matchesSearch = projectName.toLowerCase().includes(searchTerm.toLowerCase()) ||
                           studentName.toLowerCase().includes(searchTerm.toLowerCase()) ||
                           donorName.toLowerCase().includes(searchTerm.toLowerCase());
      const matchesPurpose = filterPurpose === "all" || purpose === filterPurpose;
      return matchesSearch && matchesPurpose;
    });
  };

  const getStatusBadge = (status) => {
    switch (status) {
      case "COMPLETED":
        return "bg-green-100 text-green-700";
      case "PENDING":
        return "bg-yellow-100 text-yellow-700";
      case "FAILED":
        return "bg-red-100 text-red-700";
      default:
        return "bg-gray-100 text-gray-700";
    }
  };

  const getPurposeColor = (purpose) => {
    const colors = {
      'SCHOOL_PROJECT': 'bg-blue-500',
      'STUDENT_SPONSORSHIP': 'bg-green-500',
      'NGO_PROJECT': 'bg-purple-500',
      'GENERAL_SUPPORT': 'bg-orange-500'
    };
    return colors[purpose] || 'bg-gray-500';
  };

  const getPurposeLabel = (purpose) => {
    const labels = {
      'SCHOOL_PROJECT': 'School Project',
      'STUDENT_SPONSORSHIP': 'Student Sponsorship',
      'NGO_PROJECT': 'NGO Project',
      'GENERAL_SUPPORT': 'General Support'
    };
    return labels[purpose] || 'Unknown';
  };

  const getPurposeIcon = (purpose) => {
    const icons = {
      'SCHOOL_PROJECT': Building2,
      'STUDENT_SPONSORSHIP': GraduationCap,
      'NGO_PROJECT': Target,
      'GENERAL_SUPPORT': Award
    };
    return icons[purpose] || Award;
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center h-screen bg-gray-50">
        <div className="text-center">
          <div className="w-16 h-16 border-4 border-green-600 border-t-transparent rounded-full animate-spin mx-auto mb-4"></div>
          <p className="text-gray-600">Loading school funding reports...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="flex bg-gray-50 min-h-screen">
      <DashSidebar />
      <div className="flex-1 bg-white">
        {/* Header */}
        <div className="border-b bg-white">
          <div className="p-6">
            <div className="flex items-center justify-between mb-4">
              <div>
                <h1 className="text-2xl font-bold mb-1">School Funding Reports</h1>
                <p className="text-gray-600 text-sm">
                  Track donations received, project funding, and student support impact
                </p>
              </div>
              <button className="flex items-center gap-2 px-4 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700">
                <Download size={16} />
                Export Report
              </button>
            </div>

            {/* Impact Summary Cards */}
            <div className="grid grid-cols-4 gap-6">
              <div className="bg-gradient-to-r from-green-50 to-green-100 rounded-lg p-4">
                <div className="flex items-center justify-between mb-2">
                  <span className="text-sm text-green-700">Total Received</span>
                  <div className="w-8 h-8 bg-green-200 rounded-lg flex items-center justify-center">
                    <DollarSign size={16} className="text-green-700" />
                  </div>
                </div>
                <div className="text-2xl font-bold text-green-800">Tk {totalReceived.toLocaleString()}</div>
                <div className="text-xs text-green-600 mt-1">
                  From {donationData.length} donations
                </div>
              </div>

              <div className="bg-gradient-to-r from-blue-50 to-blue-100 rounded-lg p-4">
                <div className="flex items-center justify-between mb-2">
                  <span className="text-sm text-blue-700">Active Projects</span>
                  <div className="w-8 h-8 bg-blue-200 rounded-lg flex items-center justify-center">
                    <Building2 size={16} className="text-blue-700" />
                  </div>
                </div>
                <div className="text-2xl font-bold text-blue-800">{activeProjects}</div>
                <div className="text-xs text-blue-600 mt-1">
                  {completedProjects} completed projects
                </div>
              </div>

              <div className="bg-gradient-to-r from-purple-50 to-purple-100 rounded-lg p-4">
                <div className="flex items-center justify-between mb-2">
                  <span className="text-sm text-purple-700">Students Enrolled</span>
                  <div className="w-8 h-8 bg-purple-200 rounded-lg flex items-center justify-center">
                    <Users size={16} className="text-purple-700" />
                  </div>
                </div>
                <div className="text-2xl font-bold text-purple-800">{totalBeneficiaries}</div>
                <div className="text-xs text-purple-600 mt-1">
                  {highRiskStudents} high-risk students
                </div>
              </div>

              <div className="bg-gradient-to-r from-orange-50 to-orange-100 rounded-lg p-4">
                <div className="flex items-center justify-between mb-2">
                  <span className="text-sm text-orange-700">Success Rate</span>
                  <div className="w-8 h-8 bg-orange-200 rounded-lg flex items-center justify-center">
                    <TrendingUp size={16} className="text-orange-700" />
                  </div>
                </div>
                <div className="text-2xl font-bold text-orange-800">
                  {totalReceived > 0 ? ((totalCompleted / totalReceived) * 100).toFixed(1) : '0'}%
                </div>
                <div className="text-xs text-orange-600 mt-1">
                  Payment completion rate
                </div>
              </div>
            </div>
          </div>
        </div>

        {/* Navigation Tabs */}
        <div className="px-6 pt-6">
          <div className="flex gap-6 border-b mb-6">
            <button
              onClick={() => setActiveTab("overview")}
              className={`pb-2 font-semibold ${
                activeTab === "overview"
                  ? "text-green-600 border-b-2 border-green-600"
                  : "text-gray-500"
              }`}
            >
              Overview
            </button>
            <button
              onClick={() => setActiveTab("donations")}
              className={`pb-2 font-semibold ${
                activeTab === "donations"
                  ? "text-green-600 border-b-2 border-green-600"
                  : "text-gray-500"
              }`}
            >
              Donations Received
            </button>
            <button
              onClick={() => setActiveTab("impact")}
              className={`pb-2 font-semibold ${
                activeTab === "impact"
                  ? "text-green-600 border-b-2 border-green-600"
                  : "text-gray-500"
              }`}
            >
              Impact Analysis
            </button>
          </div>
        </div>

        {/* Content based on active tab */}
        <div className="px-6 pb-6">
          {activeTab === "overview" && (
            <div className="grid grid-cols-3 gap-6">
              {/* Funding Breakdown by Purpose */}
              <div className="col-span-2 bg-white border border-gray-200 rounded-xl p-6">
                <h3 className="text-lg font-bold mb-4">Funding Distribution by Purpose</h3>
                <div className="space-y-4">
                  {Object.entries(purposeBreakdown).map(([purpose, amount]) => {
                    const percentage = ((amount / totalReceived) * 100).toFixed(1);
                    const IconComponent = getPurposeIcon(purpose);
                    return (
                      <div key={purpose} className="flex items-center justify-between p-3 bg-gray-50 rounded-lg">
                        <div className="flex items-center gap-3">
                          <div className={`w-10 h-10 rounded-lg flex items-center justify-center ${getPurposeColor(purpose)}`}>
                            <IconComponent size={20} className="text-white" />
                          </div>
                          <div>
                            <div className="font-medium">{getPurposeLabel(purpose)}</div>
                            <div className="text-sm text-gray-500">{percentage}% of total funding</div>
                          </div>
                        </div>
                        <div className="text-right">
                          <div className="text-xl font-bold text-gray-800">Tk {amount.toLocaleString()}</div>
                          <div className="text-sm text-gray-500">
                            {donationData.filter(d => d.purpose === purpose).length} donations
                          </div>
                        </div>
                      </div>
                    );
                  })}
                </div>
              </div>

              {/* School Performance Metrics */}
              <div className="bg-white border border-gray-200 rounded-xl p-6">
                <h3 className="text-lg font-bold mb-4">School Performance</h3>
                <div className="space-y-4">
                  <div className="text-center p-4 bg-green-50 rounded-lg">
                    <div className="text-2xl font-bold text-green-600">
                      {((totalBeneficiaries - highRiskStudents) / totalBeneficiaries * 100).toFixed(1)}%
                    </div>
                    <div className="text-sm text-green-700">Student Retention Rate</div>
                  </div>
                  <div className="space-y-3">
                    <div className="flex justify-between items-center">
                      <span className="text-sm font-medium">Total Students</span>
                      <span className="text-sm font-bold text-blue-600">{totalBeneficiaries}</span>
                    </div>
                    <div className="flex justify-between items-center">
                      <span className="text-sm font-medium">High Risk</span>
                      <span className="text-sm font-bold text-red-600">{highRiskStudents}</span>
                    </div>
                    <div className="flex justify-between items-center">
                      <span className="text-sm font-medium">Active Projects</span>
                      <span className="text-sm font-bold text-purple-600">{activeProjects}</span>
                    </div>
                    <div className="w-full bg-gray-200 rounded-full h-2">
                      <div 
                        className="bg-green-600 h-2 rounded-full transition-all duration-300"
                        style={{ width: `${totalBeneficiaries > 0 ? ((totalBeneficiaries - highRiskStudents) / totalBeneficiaries) * 100 : 0}%` }}
                      ></div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          )}

          {activeTab === "donations" && (
            <div>
              {/* Filters */}
              <div className="flex gap-4 mb-6">
                <div className="relative flex-1">
                  <Search className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400" size={18} />
                  <input
                    type="text"
                    placeholder="Search donations, projects, students..."
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                    className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-green-500"
                  />
                </div>
                <select
                  value={filterPurpose}
                  onChange={(e) => setFilterPurpose(e.target.value)}
                  className="px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-green-500"
                >
                  <option value="all">All Purposes</option>
                  <option value="SCHOOL_PROJECT">School Projects</option>
                  <option value="STUDENT_SPONSORSHIP">Student Sponsorship</option>
                  <option value="NGO_PROJECT">NGO Projects</option>
                  <option value="GENERAL_SUPPORT">General Support</option>
                </select>
              </div>

              {/* Donations Table */}
              <div className="bg-white border border-gray-200 rounded-xl overflow-hidden">
                <table className="w-full">
                  <thead className="bg-gray-50 border-b border-gray-200">
                    <tr>
                      <th className="text-left py-3 px-4 text-sm font-semibold text-gray-700">Donation Details</th>
                      <th className="text-left py-3 px-4 text-sm font-semibold text-gray-700">Donor</th>
                      <th className="text-left py-3 px-4 text-sm font-semibold text-gray-700">Purpose</th>
                      <th className="text-left py-3 px-4 text-sm font-semibold text-gray-700">Amount</th>
                      <th className="text-left py-3 px-4 text-sm font-semibold text-gray-700">Date</th>
                      <th className="text-left py-3 px-4 text-sm font-semibold text-gray-700">Status</th>
                      <th className="text-left py-3 px-4 text-sm font-semibold text-gray-700">Actions</th>
                    </tr>
                  </thead>
                  <tbody>
                    {filterDonations().map((donation) => {
                      const amount = parseFloat(donation.amount || 0);
                      const purpose = donation.purpose || 'GENERAL_SUPPORT';
                      const IconComponent = getPurposeIcon(purpose);
                      
                      return (
                        <tr key={donation.donationId} className="border-b border-gray-200 last:border-0 hover:bg-gray-50">
                          <td className="py-3 px-4">
                            <div className="flex items-center gap-3">
                              <div className={`w-8 h-8 rounded-lg flex items-center justify-center ${getPurposeColor(purpose)}`}>
                                <IconComponent size={16} className="text-white" />
                              </div>
                              <div>
                                <div className="font-semibold">
                                  {donation.project?.projectTitle || 
                                   (donation.student ? `${donation.student.firstName} ${donation.student.lastName}` : 'General Support')}
                                </div>
                                <div className="text-sm text-gray-500">
                                  {donation.donorMessage ? donation.donorMessage.substring(0, 50) + '...' : 'No message'}
                                </div>
                              </div>
                            </div>
                          </td>
                          <td className="py-3 px-4">
                            <div className="font-semibold">
                              {donation.donor?.donorName || 'Anonymous Donor'}
                            </div>
                            <div className="text-sm text-gray-500">
                              {donation.isAnonymous ? 'Anonymous' : 'Public'}
                            </div>
                          </td>
                          <td className="py-3 px-4">
                            <span className={`inline-flex items-center px-2 py-1 rounded-full text-xs font-medium text-white ${getPurposeColor(purpose)}`}>
                              {getPurposeLabel(purpose)}
                            </span>
                          </td>
                          <td className="py-3 px-4">
                            <div className="font-bold text-lg">Tk {amount.toLocaleString()}</div>
                            <div className="text-xs text-gray-500">{donation.donationType?.toLowerCase()}</div>
                          </td>
                          <td className="py-3 px-4">
                            <div className="text-sm">
                              {new Date(donation.donatedAt || donation.createdAt).toLocaleDateString()}
                            </div>
                          </td>
                          <td className="py-3 px-4">
                            <span className={`inline-flex items-center px-3 py-1 rounded-full text-xs font-semibold ${getStatusBadge(donation.paymentStatus)}`}>
                              {donation.paymentStatus?.toLowerCase()}
                            </span>
                          </td>
                          <td className="py-3 px-4">
                            <button className="text-green-600 hover:text-green-800 font-medium text-sm">
                              View Details
                            </button>
                          </td>
                        </tr>
                      );
                    })}
                  </tbody>
                </table>
              </div>
            </div>
          )}

          {activeTab === "impact" && (
            <div className="grid grid-cols-2 gap-6">
              {/* Impact Metrics */}
              <div className="bg-white border border-gray-200 rounded-xl p-6">
                <h3 className="text-lg font-bold mb-4">Educational Impact</h3>
                <div className="space-y-4">
                  <div className="flex justify-between items-center p-3 bg-gray-50 rounded-lg">
                    <span className="font-medium">Students Supported</span>
                    <span className="text-xl font-bold text-green-600">{totalBeneficiaries}</span>
                  </div>
                  <div className="flex justify-between items-center p-3 bg-gray-50 rounded-lg">
                    <span className="font-medium">Projects Funded</span>
                    <span className="text-xl font-bold text-blue-600">{projectData.length}</span>
                  </div>
                  <div className="flex justify-between items-center p-3 bg-gray-50 rounded-lg">
                    <span className="font-medium">Dropout Prevention</span>
                    <span className="text-xl font-bold text-purple-600">{totalBeneficiaries - highRiskStudents}</span>
                  </div>
                  <div className="flex justify-between items-center p-3 bg-gray-50 rounded-lg">
                    <span className="font-medium">Success Rate</span>
                    <span className="text-xl font-bold text-orange-600">
                      {totalBeneficiaries > 0 ? ((totalBeneficiaries - highRiskStudents) / totalBeneficiaries * 100).toFixed(1) : '0'}%
                    </span>
                  </div>
                </div>
              </div>

              {/* Cost Efficiency */}
              <div className="bg-white border border-gray-200 rounded-xl p-6">
                <h3 className="text-lg font-bold mb-4">Funding Efficiency</h3>
                <div className="space-y-4">
                  <div className="text-center p-4 bg-green-50 rounded-lg">
                    <div className="text-2xl font-bold text-green-600">
                      Tk {totalBeneficiaries > 0 ? Math.round(totalCompleted / totalBeneficiaries).toLocaleString() : '0'}
                    </div>
                    <div className="text-sm text-green-700">Cost per student supported</div>
                  </div>
                  <div className="space-y-3">
                    {Object.entries(purposeBreakdown).map(([purpose, amount]) => {
                      const purposeStudents = purpose === 'STUDENT_SPONSORSHIP' ? 
                        donationData.filter(d => d.purpose === purpose).length : 
                        Math.ceil(amount / 5000); // Estimate based on average project cost
                      const costPerStudent = purposeStudents > 0 ? Math.round(amount / purposeStudents) : 0;
                      
                      return (
                        <div key={purpose} className="flex justify-between items-center">
                          <span className="text-sm font-medium">{getPurposeLabel(purpose)}</span>
                          <span className="text-sm font-bold">Tk {costPerStudent.toLocaleString()}/student</span>
                        </div>
                      );
                    })}
                  </div>
                </div>
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}