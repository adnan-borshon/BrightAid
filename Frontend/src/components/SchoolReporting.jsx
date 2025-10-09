import { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import { Search, DollarSign, Users, TrendingUp, FileText, CheckCircle, XCircle, AlertCircle, Briefcase } from "lucide-react";
import { useApp } from '../context/AppContext';
import DashSidebar from './DashSidebar';

export default function SchoolReporting() {
  const { schoolId } = useParams();
  const { API_BASE_URL } = useApp();
  const [searchTerm, setSearchTerm] = useState("");
  const [filterStatus, setFilterStatus] = useState("all");
  const [activeTab, setActiveTab] = useState("all");
  const [loading, setLoading] = useState(true);
  const [donationsData, setDonationsData] = useState([]);
  const [studentsData, setStudentsData] = useState([]);
  const [projectsData, setProjectsData] = useState([]);

  useEffect(() => {
    fetchSchoolReportingData();
  }, [schoolId]);

  const fetchSchoolReportingData = async () => {
    try {
      setLoading(true);
      
      // Fetch donations for this school using backend filtering
      const donationsResponse = await fetch(`${API_BASE_URL}/donations/school/${schoolId}`);
      const schoolDonations = donationsResponse.ok ? await donationsResponse.json() : [];
      
      // Fetch students and projects for counts only
      const studentsResponse = await fetch(`${API_BASE_URL}/students`);
      const allStudents = studentsResponse.ok ? await studentsResponse.json() : [];
      
      const projectsResponse = await fetch(`${API_BASE_URL}/school-projects`);
      const allProjects = projectsResponse.ok ? await projectsResponse.json() : [];
      
      const schoolStudents = allStudents.filter(s => (s.schoolId || s.school_id) == schoolId);
      const schoolProjects = allProjects.filter(p => (p.schoolId || p.school_id) == schoolId);
      
      setDonationsData(schoolDonations);
      setStudentsData(schoolStudents);
      setProjectsData(schoolProjects);
      
    } catch (error) {
      console.error('Error fetching school reporting data:', error);
    } finally {
      setLoading(false);
    }
  };

  // Map backend donation status to frontend status
  const mapDonationStatus = (backendStatus) => {
    switch (backendStatus) {
      case 'COMPLETED':
        return 'completed';
      case 'PENDING':
        return 'pending';
      case 'FAILED':
        return 'failed';
      default:
        return 'pending';
    }
  };

  // Process donations data - now comes pre-processed from backend
  const processedDonations = donationsData.map(donation => ({
    id: donation.donationId,
    date: donation.donatedAt || new Date().toISOString(),
    recipientName: donation.recipientName || 'Unknown',
    amount: donation.amount || 0,
    status: mapDonationStatus(donation.paymentStatus),
    transactionRef: donation.transactionRef,
    donorName: donation.donorName || 'Anonymous Donor',
  }));

  const completedDonations = processedDonations.filter((d) => d.status === "completed");
  const pendingDonations = processedDonations.filter((d) => d.status === "pending");
  const failedDonations = processedDonations.filter((d) => d.status === "failed");

  const totalReceived = completedDonations.reduce((sum, d) => sum + d.amount, 0);
  const pendingAmount = pendingDonations.reduce((sum, d) => sum + d.amount, 0);

  // Calculate this month's donations
  const currentMonth = new Date().getMonth();
  const currentYear = new Date().getFullYear();
  const thisMonthDonations = processedDonations.filter(donation => {
    const donationDate = new Date(donation.date);
    return donationDate.getMonth() === currentMonth && donationDate.getFullYear() === currentYear;
  });
  const thisMonthAmount = thisMonthDonations.reduce((sum, d) => sum + d.amount, 0);

  const filterDonations = () => {
    let donations = processedDonations;
    if (activeTab === "completed") donations = completedDonations;
    if (activeTab === "pending") donations = pendingDonations;
    if (activeTab === "failed") donations = failedDonations;

    return donations.filter((donation) => {
      const matchesSearch =
        donation.recipientName.toLowerCase().includes(searchTerm.toLowerCase()) ||
        donation.transactionRef.toLowerCase().includes(searchTerm.toLowerCase()) ||
        donation.donorName.toLowerCase().includes(searchTerm.toLowerCase());
      const matchesStatus = filterStatus === "all" || donation.status === filterStatus;
      return matchesSearch && matchesStatus;
    });
  };

  const getStatusIcon = (status) => {
    switch (status) {
      case "completed":
        return <CheckCircle size={16} className="text-green-600" />;
      case "pending":
        return <AlertCircle size={16} className="text-yellow-600" />;
      case "failed":
        return <XCircle size={16} className="text-red-600" />;
      default:
        return null;
    }
  };

  const getStatusBadge = (status) => {
    switch (status) {
      case "completed":
        return "bg-green-100 text-green-700";
      case "pending":
        return "bg-yellow-100 text-yellow-700";
      case "failed":
        return "bg-red-100 text-red-700";
      default:
        return "bg-gray-100 text-gray-700";
    }
  };

  if (loading) {
    return (
      <div className="flex h-screen bg-gray-50">
        <DashSidebar />
        <div className="flex-1 flex items-center justify-center">
          <div className="text-lg">Loading...</div>
        </div>
      </div>
    );
  }

  return (
    <div className="flex h-screen bg-gray-50">
      <DashSidebar />
      <div className="flex-1 overflow-auto bg-white">
        <div className="border-b">
          <div className="p-6">
            <h1 className="text-2xl font-bold mb-1">School Funding Report</h1>
            <p className="text-gray-600 text-sm">
              Track all donations received for your school, students, and projects
            </p>

            <div className="grid grid-cols-4 gap-6 mt-6">
              <div className="bg-gray-50 rounded-lg p-4">
                <div className="flex items-center justify-between mb-2">
                  <span className="text-sm text-gray-600">Total Received</span>
                  <div className="w-8 h-8 bg-green-100 rounded-lg flex items-center justify-center">
                    <DollarSign size={16} className="text-green-600" />
                  </div>
                </div>
                <div className="text-2xl font-bold">Tk {totalReceived.toLocaleString()}</div>
                <div className="text-xs text-gray-500 mt-1">
                  {completedDonations.length} successful donations
                </div>
              </div>

              <div className="bg-gray-50 rounded-lg p-4">
                <div className="flex items-center justify-between mb-2">
                  <span className="text-sm text-gray-600">Pending</span>
                  <div className="w-8 h-8 bg-yellow-100 rounded-lg flex items-center justify-center">
                    <AlertCircle size={16} className="text-yellow-600" />
                  </div>
                </div>
                <div className="text-2xl font-bold">Tk {pendingAmount.toLocaleString()}</div>
                <div className="text-xs text-gray-500 mt-1">
                  {pendingDonations.length} donation{pendingDonations.length !== 1 ? "s" : ""} processing
                </div>
              </div>

              <div className="bg-gray-50 rounded-lg p-4">
                <div className="flex items-center justify-between mb-2">
                  <span className="text-sm text-gray-600">This Month</span>
                  <div className="w-8 h-8 bg-blue-100 rounded-lg flex items-center justify-center">
                    <TrendingUp size={16} className="text-blue-600" />
                  </div>
                </div>
                <div className="text-2xl font-bold">Tk {thisMonthAmount.toLocaleString()}</div>
                <div className="text-xs text-gray-500 mt-1">
                  {thisMonthDonations.length} donations this month
                </div>
              </div>

              <div className="bg-gray-50 rounded-lg p-4">
                <div className="flex items-center justify-between mb-2">
                  <span className="text-sm text-gray-600">Active Projects</span>
                  <div className="w-8 h-8 bg-purple-100 rounded-lg flex items-center justify-center">
                    <Briefcase size={16} className="text-purple-600" />
                  </div>
                </div>
                <div className="text-2xl font-bold">{projectsData.length}</div>
                <div className="text-xs text-gray-500 mt-1">
                  {studentsData.length} students enrolled
                </div>
              </div>
            </div>
          </div>
        </div>

        <div className="p-6">
          <div className="mb-6">
            <div className="flex gap-6 border-b mb-4">
              <button
                onClick={() => setActiveTab("all")}
                className={`pb-2 font-semibold ${
                  activeTab === "all"
                    ? "text-green-600 border-b-2 border-green-600"
                    : "text-gray-500"
                }`}
              >
                All ({processedDonations.length})
              </button>
              <button
                onClick={() => setActiveTab("completed")}
                className={`pb-2 font-semibold ${
                  activeTab === "completed"
                    ? "text-green-600 border-b-2 border-green-600"
                    : "text-gray-500"
                }`}
              >
                Completed ({completedDonations.length})
              </button>
              <button
                onClick={() => setActiveTab("pending")}
                className={`pb-2 font-semibold ${
                  activeTab === "pending"
                    ? "text-green-600 border-b-2 border-green-600"
                    : "text-gray-500"
                }`}
              >
                Pending ({pendingDonations.length})
              </button>
              <button
                onClick={() => setActiveTab("failed")}
                className={`pb-2 font-semibold ${
                  activeTab === "failed"
                    ? "text-green-600 border-b-2 border-green-600"
                    : "text-gray-500"
                }`}
              >
                Failed ({failedDonations.length})
              </button>
            </div>

            <div className="flex gap-4">
              <div className="relative flex-1">
                <Search
                  className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400"
                  size={18}
                />
                <input
                  type="text"
                  placeholder="Search by recipient, donor, or transaction..."
                  className="w-full pl-10 pr-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-green-500"
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                />
              </div>
              <select
                className="px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-green-500"
                value={filterStatus}
                onChange={(e) => setFilterStatus(e.target.value)}
              >
                <option value="all">All Status</option>
                <option value="completed">Completed</option>
                <option value="pending">Pending</option>
                <option value="failed">Failed</option>
              </select>
            </div>
          </div>

          <div className="bg-white rounded-lg border">
            <div className="overflow-x-auto">
              <table className="w-full">
                <thead className="bg-gray-50">
                  <tr>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Date
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Donor
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Recipient
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Amount
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Status
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Transaction Ref
                    </th>
                  </tr>
                </thead>
                <tbody className="bg-white divide-y divide-gray-200">
                  {filterDonations().length > 0 ? (
                    filterDonations().map((donation) => (
                      <tr key={donation.id} className="hover:bg-gray-50">
                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                          {new Date(donation.date).toLocaleDateString()}
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                          {donation.donorName}
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                          {donation.recipientName}
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                          Tk {donation.amount.toLocaleString()}
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap">
                          <div className="flex items-center gap-2">
                            {getStatusIcon(donation.status)}
                            <span
                              className={`px-2 py-1 text-xs font-medium rounded-full ${getStatusBadge(
                                donation.status
                              )}`}
                            >
                              {donation.status.charAt(0).toUpperCase() + donation.status.slice(1)}
                            </span>
                          </div>
                        </td>
                        <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                          {donation.transactionRef}
                        </td>
                      </tr>
                    ))
                  ) : (
                    <tr>
                      <td colSpan="6" className="px-6 py-12 text-center text-gray-500">
                        <FileText size={48} className="mx-auto mb-4 text-gray-300" />
                        <p className="text-lg font-medium mb-2">No donations found</p>
                        <p className="text-sm">
                          {searchTerm || filterStatus !== "all"
                            ? "Try adjusting your search or filter criteria"
                            : "Donations will appear here once received"}
                        </p>
                      </td>
                    </tr>
                  )}
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}