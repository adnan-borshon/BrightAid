import { useState } from "react";
import { Search, DollarSign, Clock, TrendingUp, FileText, CheckCircle, XCircle, AlertCircle } from "lucide-react";

const mockAllDonations = [
  {
    id: "1",
    date: "2025-01-15",
    projectName: "Clean Water Infrastructure",
    amount: 25000,
    status: "completed",
    transactionRef: "TXN001234567",
  },
  {
    id: "2",
    date: "2025-01-10",
    projectName: "Student Sponsorship - Rahim Ahmed",
    amount: 15000,
    status: "completed",
    transactionRef: "TXN001234566",
  },
  {
    id: "3",
    date: "2025-01-08",
    projectName: "School Library Books",
    amount: 8000,
    status: "pending",
    transactionRef: "TXN001234565",
  },
  {
    id: "4",
    date: "2025-01-05",
    projectName: "Digital Classroom Equipment",
    amount: 50000,
    status: "completed",
    transactionRef: "TXN001234564",
  },
  {
    id: "5",
    date: "2025-01-02",
    projectName: "Emergency Roof Repair",
    amount: 30000,
    status: "completed",
    transactionRef: "TXN001234563",
  },
  {
    id: "6",
    date: "2024-12-28",
    projectName: "School Meal Program",
    amount: 100000,
    status: "completed",
    transactionRef: "TXN001234562",
  },
  {
    id: "7",
    date: "2024-12-20",
    projectName: "Teacher Training Workshop",
    amount: 20000,
    status: "failed",
    transactionRef: "TXN001234561",
  },
  {
    id: "8",
    date: "2024-12-15",
    projectName: "Student Sponsorship - Fatima Begum",
    amount: 12000,
    status: "completed",
    transactionRef: "TXN001234560",
  },
];

export default function DonorReporting() {
  const [searchTerm, setSearchTerm] = useState("");
  const [filterStatus, setFilterStatus] = useState("all");
  const [activeTab, setActiveTab] = useState("all");

  const completedDonations = mockAllDonations.filter((d) => d.status === "completed");
  const pendingDonations = mockAllDonations.filter((d) => d.status === "pending");
  const failedDonations = mockAllDonations.filter((d) => d.status === "failed");

  const totalDonated = completedDonations.reduce((sum, d) => sum + d.amount, 0);
  const pendingAmount = pendingDonations.reduce((sum, d) => sum + d.amount, 0);

  const filterDonations = () => {
    let donations = mockAllDonations;
    if (activeTab === "completed") donations = completedDonations;
    if (activeTab === "pending") donations = pendingDonations;
    if (activeTab === "failed") donations = failedDonations;

    return donations.filter((donation) => {
      const matchesSearch =
        donation.projectName.toLowerCase().includes(searchTerm.toLowerCase()) ||
        donation.transactionRef.toLowerCase().includes(searchTerm.toLowerCase());
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

  return (
    <div className="bg-white min-h-screen">
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
              <div className="text-2xl font-bold">৳{totalDonated.toLocaleString()}</div>
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
              <div className="text-2xl font-bold">৳{pendingAmount.toLocaleString()}</div>
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
                ৳{(25000 + 15000 + 8000 + 50000 + 30000).toLocaleString()}
              </div>
              <div className="text-xs text-gray-500 mt-1">5 donations in January</div>
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
              All ({mockAllDonations.length})
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
              <option value="completed">Completed</option>
              <option value="pending">Pending</option>
              <option value="failed">Failed</option>
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
                    {new Date(donation.date).toLocaleDateString()}
                  </td>
                  <td className="py-3 px-4 text-sm font-semibold">{donation.projectName}</td>
                  <td className="py-3 px-4 text-sm text-gray-600 font-mono">
                    {donation.transactionRef}
                  </td>
                  <td className="py-3 px-4 text-sm font-bold">
                    ৳{donation.amount.toLocaleString()}
                  </td>
                  <td className="py-3 px-4">
                    <span
                      className={`inline-flex items-center gap-1 px-3 py-1 rounded-full text-xs font-semibold ${getStatusBadge(
                        donation.status
                      )}`}
                    >
                      {getStatusIcon(donation.status)}
                      {donation.status}
                    </span>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}
