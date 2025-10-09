import { useState, useEffect } from "react";
import { useParams } from 'react-router-dom';
import { Search, DollarSign, Clock, TrendingUp, FileText, CheckCircle, XCircle, AlertCircle } from "lucide-react";
import DonorDashSidebar from './DonorDashSidebar';

export default function DonorDonations() {
  const { id: donorId } = useParams();
  const [searchTerm, setSearchTerm] = useState("");
  const [filterStatus, setFilterStatus] = useState("all");
  const [activeTab, setActiveTab] = useState("all");
  const [donations, setDonations] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchDonations();
  }, [donorId]);

  const fetchDonations = async () => {
    try {
      setLoading(true);
      const response = await fetch(`http://localhost:8081/api/donations/donor/${donorId}`);
      if (response.ok) {
        const data = await response.json();
        setDonations(data);
      } else {
        setError('Failed to fetch donations');
      }
    } catch (err) {
      setError('Error fetching donations: ' + err.message);
    } finally {
      setLoading(false);
    }
  };

  const completedDonations = donations.filter((d) => d.paymentStatus === "COMPLETED");
  const pendingDonations = donations.filter((d) => d.paymentStatus === "PENDING");
  const failedDonations = donations.filter((d) => d.paymentStatus === "FAILED");

  const totalDonated = completedDonations.reduce((sum, d) => sum + parseFloat(d.amount || 0), 0);
  const pendingAmount = pendingDonations.reduce((sum, d) => sum + parseFloat(d.amount || 0), 0);
  const thisMonthAmount = donations
    .filter(d => {
      const donationDate = new Date(d.donatedAt || d.createdAt);
      const now = new Date();
      return donationDate.getMonth() === now.getMonth() && donationDate.getFullYear() === now.getFullYear();
    })
    .reduce((sum, d) => sum + parseFloat(d.amount || 0), 0);

  const filterDonations = () => {
    let filteredDonations = donations;
    if (activeTab === "completed") filteredDonations = completedDonations;
    if (activeTab === "pending") filteredDonations = pendingDonations;
    if (activeTab === "failed") filteredDonations = failedDonations;

    return filteredDonations.filter((donation) => {
      const projectName = donation.projectName || donation.recipientName || 'General Donation';
      const transactionRef = donation.transactionRef || donation.donationId;
      const matchesSearch =
        projectName.toLowerCase().includes(searchTerm.toLowerCase()) ||
        transactionRef.toString().toLowerCase().includes(searchTerm.toLowerCase());
      const matchesStatus = filterStatus === "all" || donation.paymentStatus === filterStatus;
      return matchesSearch && matchesStatus;
    });
  };

  const getStatusIcon = (status) => {
    switch (status) {
      case "COMPLETED":
        return <CheckCircle size={16} className="text-green-600" />;
      case "PENDING":
        return <AlertCircle size={16} className="text-yellow-600" />;
      case "FAILED":
        return <XCircle size={16} className="text-red-600" />;
      default:
        return null;
    }
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

  if (loading) {
    return (
      <div className="flex items-center justify-center h-screen bg-gray-50">
        <div className="text-center">
          <div className="w-16 h-16 border-4 border-green-600 border-t-transparent rounded-full animate-spin mx-auto mb-4"></div>
          <p className="text-gray-600">Loading donations...</p>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="flex items-center justify-center h-screen bg-gray-50">
        <div className="text-center">
          <p className="text-red-600">{error}</p>
          <button onClick={fetchDonations} className="mt-4 px-4 py-2 bg-green-600 text-white rounded-lg">Retry</button>
        </div>
      </div>
    );
  }

  return (
    <div className="flex bg-gray-50 min-h-screen">
      <DonorDashSidebar />
      <div className="flex-1 bg-white">
        <div className="border-b">
        <div className="p-6">
          <h1 className="text-2xl font-bold mb-1">Donation History</h1>
          <p className="text-gray-600 text-sm">
            Track all your contributions and transaction history
          </p>

          {/* Summary Stats */}
          <div className="grid grid-cols-3 gap-6 mt-6">
            <div className="bg-gray-50 rounded-lg p-4" data-testid="stat-total-donated">
              <div className="flex items-center justify-between mb-2">
                <span className="text-sm text-gray-600">Total Donated</span>
                <div className="w-8 h-8 bg-green-100 rounded-lg flex items-center justify-center">
                  <DollarSign size={16} className="text-green-600" />
                </div>
              </div>
              <div className="text-2xl font-bold">Tk {totalDonated.toLocaleString()}</div>
              <div className="text-xs text-gray-500 mt-1">
                {completedDonations.length} successful transactions
              </div>
            </div>

            <div className="bg-gray-50 rounded-lg p-4" data-testid="stat-pending">
              <div className="flex items-center justify-between mb-2">
                <span className="text-sm text-gray-600">Pending</span>
                <div className="w-8 h-8 bg-green-100 rounded-lg flex items-center justify-center">
                  <Clock size={16} className="text-green-600" />
                </div>
              </div>
              <div className="text-2xl font-bold">Tk {pendingAmount.toLocaleString()}</div>
              <div className="text-xs text-gray-500 mt-1">
                {pendingDonations.length} transaction{pendingDonations.length !== 1 ? "s" : ""}{" "}
                processing
              </div>
            </div>

            <div className="bg-gray-50 rounded-lg p-4" data-testid="stat-this-month">
              <div className="flex items-center justify-between mb-2">
                <span className="text-sm text-gray-600">This Month</span>
                <div className="w-8 h-8 bg-green-100 rounded-lg flex items-center justify-center">
                  <TrendingUp size={16} className="text-green-600" />
                </div>
              </div>
              <div className="text-2xl font-bold">
                Tk {thisMonthAmount.toLocaleString()}
              </div>
              <div className="text-xs text-gray-500 mt-1">{donations.filter(d => {
                const donationDate = new Date(d.donatedAt || d.createdAt);
                const now = new Date();
                return donationDate.getMonth() === now.getMonth() && donationDate.getFullYear() === now.getFullYear();
              }).length} donations this month</div>
            </div>
          </div>
        </div>
      </div>

      <div className="p-6">
        {/* Tabs and Search */}
        <div className="mb-6">
          <div className="flex gap-6 border-b mb-4">
            <button
              onClick={() => setActiveTab("all")}
              className={`pb-2 font-semibold ${
                activeTab === "all"
                  ? "text-green-600 border-b-2 border-green-600"
                  : "text-gray-500"
              }`}
              data-testid="tab-all"
            >
              All ({donations.length})
            </button>
            <button
              onClick={() => setActiveTab("completed")}
              className={`pb-2 font-semibold ${
                activeTab === "completed"
                  ? "text-green-600 border-b-2 border-green-600"
                  : "text-gray-500"
              }`}
              data-testid="tab-completed"
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
              data-testid="tab-pending"
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
              data-testid="tab-failed"
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
                placeholder="Search by project or transaction ID..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-green-500"
                data-testid="input-search-donations"
              />
            </div>
            <select
              value={filterStatus}
              onChange={(e) => setFilterStatus(e.target.value)}
              className="px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-green-500"
              data-testid="select-filter-status"
            >
              <option value="all">All Status</option>
              <option value="COMPLETED">Completed</option>
              <option value="PENDING">Pending</option>
              <option value="FAILED">Failed</option>
            </select>
          </div>
        </div>

        {/* Donations Table */}
        <div className="bg-white border border-gray-200 rounded-xl overflow-hidden">
          <table className="w-full">
            <thead className="bg-gray-50 border-b border-gray-200">
              <tr>
                <th className="text-left py-3 px-4 text-sm font-semibold text-gray-700">
                  Date
                </th>
                <th className="text-left py-3 px-4 text-sm font-semibold text-gray-700">
                  Project Name
                </th>
                <th className="text-left py-3 px-4 text-sm font-semibold text-gray-700">
                  Transaction ID
                </th>
                <th className="text-left py-3 px-4 text-sm font-semibold text-gray-700">
                  Amount
                </th>
                <th className="text-left py-3 px-4 text-sm font-semibold text-gray-700">
                  Status
                </th>
              </tr>
            </thead>
            <tbody>
              {filterDonations().map((donation) => (
                <tr
                  key={donation.id}
                  className="border-b border-gray-200 last:border-0 hover:bg-gray-50"
                  data-testid={`donation-row-${donation.id}`}
                >
                  <td className="py-3 px-4 text-sm text-gray-600">
                    {new Date(donation.donatedAt || donation.createdAt).toLocaleDateString()}
                  </td>
                  <td className="py-3 px-4 text-sm font-semibold">
                    {donation.projectName || donation.recipientName || 'General Donation'}
                  </td>
                  <td className="py-3 px-4 text-sm text-gray-600 font-mono">
                    {donation.transactionRef || `DON${donation.donationId}`}
                  </td>
                  <td className="py-3 px-4 text-sm font-bold">
                    Tk {parseFloat(donation.amount || 0).toLocaleString()}
                  </td>
                  <td className="py-3 px-4">
                    <span
                      className={`inline-flex items-center gap-1 px-3 py-1 rounded-full text-xs font-semibold ${getStatusBadge(
                        donation.paymentStatus
                      )}`}
                    >
                      {getStatusIcon(donation.paymentStatus)}
                      {donation.paymentStatus?.toLowerCase()}
                    </span>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
      </div>
    </div>
  );
}