import { useState, useEffect } from "react";
import { useParams } from 'react-router-dom';
import { Search, DollarSign, Users, GraduationCap, Building2, Heart, TrendingUp, FileText, CheckCircle, Download, Eye, Calendar, Filter, Target, Award } from "lucide-react";
import DonorDashSidebar from './DonorDashSidebar';

export default function DonorReporting() {
  const { id: donorId } = useParams();
  const [searchTerm, setSearchTerm] = useState("");
  const [filterPurpose, setFilterPurpose] = useState("all");
  const [activeTab, setActiveTab] = useState("overview");
  const [dateRange, setDateRange] = useState("all");
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchDonationData();
  }, [donorId]);

  const [donationData, setDonationData] = useState([]);

  const fetchDonationData = async () => {
    try {
      setLoading(true);
      const response = await fetch(`http://localhost:8081/api/donations/donor/${donorId}`);
      if (response.ok) {
        const data = await response.json();
        setDonationData(data);
      } else {
        console.error('Failed to fetch donation data');
        setDonationData([]);
      }
    } catch (err) {
      console.error('Error fetching donation data:', err);
      setDonationData([]);
    } finally {
      setLoading(false);
    }
  };

  const totalDonated = donationData.reduce((sum, d) => sum + parseFloat(d.amount || 0), 0);
  const completedDonations = donationData.filter(d => d.paymentStatus === 'COMPLETED');
  const totalCompleted = completedDonations.reduce((sum, d) => sum + parseFloat(d.amount || 0), 0);
  
  // Calculate beneficiaries based on donation purpose
  const totalBeneficiaries = donationData.reduce((sum, d) => {
    if (d.purpose === 'STUDENT_SPONSORSHIP') return sum + 1;
    if (d.project?.beneficiaryCount) return sum + d.project.beneficiaryCount;
    return sum + 1; // Default for general support
  }, 0);
  
  const uniqueSchools = [...new Set(donationData.map(d => d.project?.school?.schoolName || d.student?.school?.schoolName).filter(Boolean))].length;
  const uniqueStudents = [...new Set(donationData.filter(d => d.student).map(d => d.student.studentId))].length;

  const purposeBreakdown = donationData.reduce((acc, item) => {
    const purpose = item.purpose || 'GENERAL_SUPPORT';
    acc[purpose] = (acc[purpose] || 0) + parseFloat(item.amount || 0);
    return acc;
  }, {});

  const filterDonations = () => {
    return donationData.filter((donation) => {
      const projectName = donation.project?.projectTitle || '';
      const studentName = donation.student ? `${donation.student.firstName} ${donation.student.lastName}` : '';
      const schoolName = donation.project?.school?.schoolName || donation.student?.school?.schoolName || '';
      const purpose = donation.purpose || 'GENERAL_SUPPORT';
      
      const matchesSearch = projectName.toLowerCase().includes(searchTerm.toLowerCase()) ||
                           studentName.toLowerCase().includes(searchTerm.toLowerCase()) ||
                           schoolName.toLowerCase().includes(searchTerm.toLowerCase());
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
      'NGO_PROJECT': Heart,
      'GENERAL_SUPPORT': Target
    };
    return icons[purpose] || Target;
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center h-screen bg-gray-50">
        <div className="text-center">
          <div className="w-16 h-16 border-4 border-green-600 border-t-transparent rounded-full animate-spin mx-auto mb-4"></div>
          <p className="text-gray-600">Loading donation impact reports...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="flex bg-gray-50 min-h-screen">
      <DonorDashSidebar />
      <div className="flex-1 bg-white">
        {/* Header */}
        <div className="border-b bg-white">
          <div className="p-6">
            <div className="flex items-center justify-between mb-4">
              <div>
                <h1 className="text-2xl font-bold mb-1">Donation Impact Reports</h1>
                <p className="text-gray-600 text-sm">
                  Track your donations across projects, students, and their real-world impact
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
                  <span className="text-sm text-green-700">Total Donated</span>
                  <div className="w-8 h-8 bg-green-200 rounded-lg flex items-center justify-center">
                    <DollarSign size={16} className="text-green-700" />
                  </div>
                </div>
                <div className="text-2xl font-bold text-green-800">Tk {totalDonated.toLocaleString()}</div>
                <div className="text-xs text-green-600 mt-1">
                  Across {donationData.length} donations
                </div>
              </div>

              <div className="bg-gradient-to-r from-blue-50 to-blue-100 rounded-lg p-4">
                <div className="flex items-center justify-between mb-2">
                  <span className="text-sm text-blue-700">Students Sponsored</span>
                  <div className="w-8 h-8 bg-blue-200 rounded-lg flex items-center justify-center">
                    <GraduationCap size={16} className="text-blue-700" />
                  </div>
                </div>
                <div className="text-2xl font-bold text-blue-800">{uniqueStudents}</div>
                <div className="text-xs text-blue-600 mt-1">
                  Direct student sponsorships
                </div>
              </div>

              <div className="bg-gradient-to-r from-purple-50 to-purple-100 rounded-lg p-4">
                <div className="flex items-center justify-between mb-2">
                  <span className="text-sm text-purple-700">Schools Supported</span>
                  <div className="w-8 h-8 bg-purple-200 rounded-lg flex items-center justify-center">
                    <Building2 size={16} className="text-purple-700" />
                  </div>
                </div>
                <div className="text-2xl font-bold text-purple-800">{uniqueSchools}</div>
                <div className="text-xs text-purple-600 mt-1">
                  Educational institutions
                </div>
              </div>

              <div className="bg-gradient-to-r from-orange-50 to-orange-100 rounded-lg p-4">
                <div className="flex items-center justify-between mb-2">
                  <span className="text-sm text-orange-700">Total Impact</span>
                  <div className="w-8 h-8 bg-orange-200 rounded-lg flex items-center justify-center">
                    <Users size={16} className="text-orange-700" />
                  </div>
                </div>
                <div className="text-2xl font-bold text-orange-800">{totalBeneficiaries.toLocaleString()}</div>
                <div className="text-xs text-orange-600 mt-1">
                  Lives positively impacted
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
              My Donations
            </button>
            <button
              onClick={() => setActiveTab("impact")}
              className={`pb-2 font-semibold ${
                activeTab === "impact"
                  ? "text-green-600 border-b-2 border-green-600"
                  : "text-gray-500"
              }`}
            >
              Impact Stories
            </button>
          </div>
        </div>

        {/* Content based on active tab */}
        <div className="px-6 pb-6">
          {activeTab === "overview" && (
            <div className="grid grid-cols-3 gap-6">
              {/* Donation Breakdown by Purpose */}
              <div className="col-span-2 bg-white border border-gray-200 rounded-xl p-6">
                <h3 className="text-lg font-bold mb-4">Donation Distribution by Purpose</h3>
                <div className="space-y-4">
                  {Object.entries(purposeBreakdown).map(([purpose, amount]) => {
                    const percentage = ((amount / totalDonated) * 100).toFixed(1);
                    const IconComponent = getPurposeIcon(purpose);
                    return (
                      <div key={purpose} className="flex items-center justify-between p-3 bg-gray-50 rounded-lg">
                        <div className="flex items-center gap-3">
                          <div className={`w-10 h-10 rounded-lg flex items-center justify-center ${getPurposeColor(purpose)}`}>
                            <IconComponent size={20} className="text-white" />
                          </div>
                          <div>
                            <div className="font-medium">{getPurposeLabel(purpose)}</div>
                            <div className="text-sm text-gray-500">{percentage}% of total donations</div>
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

              {/* Payment Status Overview */}
              <div className="bg-white border border-gray-200 rounded-xl p-6">
                <h3 className="text-lg font-bold mb-4">Payment Status</h3>
                <div className="space-y-4">
                  <div className="text-center p-4 bg-green-50 rounded-lg">
                    <div className="text-2xl font-bold text-green-600">
                      {completedDonations.length}
                    </div>
                    <div className="text-sm text-green-700">Completed Donations</div>
                  </div>
                  <div className="space-y-3">
                    <div className="flex justify-between items-center">
                      <span className="text-sm font-medium">Completed</span>
                      <span className="text-sm font-bold text-green-600">Tk {totalCompleted.toLocaleString()}</span>
                    </div>
                    <div className="flex justify-between items-center">
                      <span className="text-sm font-medium">Pending</span>
                      <span className="text-sm font-bold text-yellow-600">
                        Tk {(totalDonated - totalCompleted).toLocaleString()}
                      </span>
                    </div>
                    <div className="w-full bg-gray-200 rounded-full h-2">
                      <div 
                        className="bg-green-600 h-2 rounded-full transition-all duration-300"
                        style={{ width: `${totalDonated > 0 ? (totalCompleted / totalDonated) * 100 : 0}%` }}
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
                      <th className="text-left py-3 px-4 text-sm font-semibold text-gray-700">Purpose</th>
                      <th className="text-left py-3 px-4 text-sm font-semibold text-gray-700">Amount</th>
                      <th className="text-left py-3 px-4 text-sm font-semibold text-gray-700">Beneficiary</th>
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
                                  {donation.project?.school?.schoolName || donation.student?.school?.schoolName || 'N/A'}
                                </div>
                              </div>
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
                            <div className="font-semibold">
                              {donation.purpose === 'STUDENT_SPONSORSHIP' ? '1 Student' : 
                               donation.project?.beneficiaryCount ? `${donation.project.beneficiaryCount} Students` : '1 Person'}
                            </div>
                          </td>
                          <td className="py-3 px-4">
                            <div className="text-sm">
                              {new Date(donation.donatedAt || donation.createdAt).toLocaleDateString()}
                            </div>
                          </td>
                          <td className="py-3 px-4">
                            <span className={`inline-flex items-center px-3 py-1 rounded-full text-xs font-semibold ${
                              donation.paymentStatus === 'COMPLETED' ? 'bg-green-100 text-green-700' :
                              donation.paymentStatus === 'PENDING' ? 'bg-yellow-100 text-yellow-700' :
                              'bg-red-100 text-red-700'
                            }`}>
                              {donation.paymentStatus?.toLowerCase()}
                            </span>
                          </td>
                          <td className="py-3 px-4">
                            <button className="text-green-600 hover:text-green-800 font-medium text-sm">
                              View Impact
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
            <div className="grid grid-cols-1 gap-6">
              {filterDonations().filter(d => d.paymentStatus === 'COMPLETED').map((donation) => {
                const IconComponent = getPurposeIcon(donation.purpose);
                return (
                  <div key={donation.donationId} className="bg-white border border-gray-200 rounded-xl p-6">
                    <div className="flex items-start justify-between mb-4">
                      <div className="flex items-start gap-4">
                        <div className={`w-12 h-12 rounded-xl flex items-center justify-center ${getPurposeColor(donation.purpose)}`}>
                          <IconComponent size={24} className="text-white" />
                        </div>
                        <div>
                          <h3 className="text-lg font-bold">
                            {donation.project?.projectTitle || 
                             (donation.student ? `Sponsoring ${donation.student.firstName} ${donation.student.lastName}` : 'General Support')}
                          </h3>
                          <p className="text-gray-600">
                            {donation.project?.school?.schoolName || donation.student?.school?.schoolName || 'Community Support'}
                          </p>
                          <div className="flex items-center gap-4 mt-2 text-sm text-gray-500">
                            <span>Donated: Tk {parseFloat(donation.amount || 0).toLocaleString()}</span>
                            <span>•</span>
                            <span>Date: {new Date(donation.donatedAt || donation.createdAt).toLocaleDateString()}</span>
                            <span>•</span>
                            <span>{getPurposeLabel(donation.purpose)}</span>
                          </div>
                        </div>
                      </div>
                      <span className={`inline-flex items-center px-3 py-1 rounded-full text-xs font-semibold bg-green-100 text-green-700`}>
                        Active Impact
                      </span>
                    </div>
                    
                    <div className="grid grid-cols-3 gap-4 mt-6">
                      <div className="bg-blue-50 rounded-lg p-4 text-center">
                        <div className="text-2xl font-bold text-blue-600">
                          {donation.purpose === 'STUDENT_SPONSORSHIP' ? '1' : donation.project?.beneficiaryCount || '1'}
                        </div>
                        <div className="text-sm text-blue-700">Students Helped</div>
                      </div>
                      
                      <div className="bg-green-50 rounded-lg p-4 text-center">
                        <div className="text-2xl font-bold text-green-600">
                          {donation.project?.school?.schoolName || donation.student?.school?.schoolName ? '1' : '0'}
                        </div>
                        <div className="text-sm text-green-700">Schools Supported</div>
                      </div>
                      
                      <div className="bg-purple-50 rounded-lg p-4 text-center">
                        <div className="text-2xl font-bold text-purple-600">
                          {Math.round(parseFloat(donation.amount || 0) / (donation.project?.beneficiaryCount || 1))}
                        </div>
                        <div className="text-sm text-purple-700">Tk per Beneficiary</div>
                      </div>
                    </div>
                    
                    {donation.donorMessage && (
                      <div className="mt-4 p-4 bg-gray-50 rounded-lg">
                        <h4 className="font-semibold mb-2">Your Message</h4>
                        <p className="text-gray-700 text-sm italic">"{donation.donorMessage}"</p>
                      </div>
                    )}
                  </div>
                );
              })}
              
              {filterDonations().filter(d => d.paymentStatus === 'COMPLETED').length === 0 && (
                <div className="text-center py-12">
                  <Award className="w-16 h-16 text-gray-300 mx-auto mb-4" />
                  <h3 className="text-lg font-semibold text-gray-600 mb-2">No Impact Stories Yet</h3>
                  <p className="text-gray-500">Complete your donations to see their real-world impact here.</p>
                </div>
              )}
            </div>
          )}
        </div>
      </div>
    </div>
  );
}