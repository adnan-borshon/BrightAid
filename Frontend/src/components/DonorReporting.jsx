import { useState, useEffect } from "react";
import { useParams } from 'react-router-dom';
import { Search, DollarSign, PieChart, BarChart3, TrendingUp, FileText, CheckCircle, Download, Eye, Calendar, Filter } from "lucide-react";
import DonorDashSidebar from './DonorDashSidebar';



export default function DonorReporting() {
  const { id: donorId } = useParams();
  const [searchTerm, setSearchTerm] = useState("");
  const [filterCategory, setFilterCategory] = useState("all");
  const [activeTab, setActiveTab] = useState("overview");
  const [dateRange, setDateRange] = useState("all");
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchFundUsageData();
  }, [donorId]);

  const [fundUsageData, setFundUsageData] = useState([]);

  const fetchFundUsageData = async () => {
    try {
      setLoading(true);
      const response = await fetch(`http://localhost:8081/api/fund-utilization/donor/${donorId}`);
      if (response.ok) {
        const data = await response.json();
        setFundUsageData(data);
      } else {
        console.error('Failed to fetch fund usage data');
        setFundUsageData([]);
      }
    } catch (err) {
      console.error('Error fetching fund usage data:', err);
      setFundUsageData([]);
    } finally {
      setLoading(false);
    }
  };

  const totalDonated = fundUsageData.reduce((sum, d) => sum + parseFloat(d.donation?.amount || 0), 0);
  const totalUsed = fundUsageData.reduce((sum, d) => sum + parseFloat(d.amountUsed || 0), 0);
  const totalBeneficiaries = fundUsageData.reduce((sum, d) => sum + (d.project?.beneficiaryCount || 1), 0);
  const utilizationRate = totalDonated > 0 ? ((totalUsed / totalDonated) * 100).toFixed(1) : '0.0';

  const categoryBreakdown = fundUsageData.reduce((acc, item) => {
    const category = item.project?.projectType?.typeName || 'General';
    acc[category] = (acc[category] || 0) + parseFloat(item.amountUsed || 0);
    return acc;
  }, {});

  const filterProjects = () => {
    return fundUsageData.filter((utilization) => {
      const projectName = utilization.project?.projectTitle || 'General Fund';
      const description = utilization.detailedDescription || utilization.specificPurpose || '';
      const category = utilization.project?.projectType?.typeName || 'General';
      
      const matchesSearch = projectName.toLowerCase().includes(searchTerm.toLowerCase()) ||
                           description.toLowerCase().includes(searchTerm.toLowerCase());
      const matchesCategory = filterCategory === "all" || category === filterCategory;
      return matchesSearch && matchesCategory;
    });
  };

  const getStatusBadge = (status) => {
    switch (status) {
      case "completed":
        return "bg-green-100 text-green-700";
      case "active":
        return "bg-blue-100 text-blue-700";
      case "pending":
        return "bg-yellow-100 text-yellow-700";
      default:
        return "bg-gray-100 text-gray-700";
    }
  };

  const getCategoryColor = (category) => {
    const colors = {
      'Education': 'bg-blue-500',
      'Infrastructure': 'bg-green-500',
      'Technology': 'bg-purple-500',
      'Nutrition': 'bg-orange-500',
      'Healthcare': 'bg-red-500'
    };
    return colors[category] || 'bg-gray-500';
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center h-screen bg-gray-50">
        <div className="text-center">
          <div className="w-16 h-16 border-4 border-green-600 border-t-transparent rounded-full animate-spin mx-auto mb-4"></div>
          <p className="text-gray-600">Loading fund usage reports...</p>
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
                <h1 className="text-2xl font-bold mb-1">Fund Usage Reports</h1>
                <p className="text-gray-600 text-sm">
                  Track how your donations are being utilized and their impact
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
                  Across {fundUsageData.length} projects
                </div>
              </div>

              <div className="bg-gradient-to-r from-blue-50 to-blue-100 rounded-lg p-4">
                <div className="flex items-center justify-between mb-2">
                  <span className="text-sm text-blue-700">Funds Utilized</span>
                  <div className="w-8 h-8 bg-blue-200 rounded-lg flex items-center justify-center">
                    <TrendingUp size={16} className="text-blue-700" />
                  </div>
                </div>
                <div className="text-2xl font-bold text-blue-800">Tk {totalUsed.toLocaleString()}</div>
                <div className="text-xs text-blue-600 mt-1">
                  {utilizationRate}% utilization rate
                </div>
              </div>

              <div className="bg-gradient-to-r from-purple-50 to-purple-100 rounded-lg p-4">
                <div className="flex items-center justify-between mb-2">
                  <span className="text-sm text-purple-700">People Helped</span>
                  <div className="w-8 h-8 bg-purple-200 rounded-lg flex items-center justify-center">
                    <CheckCircle size={16} className="text-purple-700" />
                  </div>
                </div>
                <div className="text-2xl font-bold text-purple-800">{totalBeneficiaries.toLocaleString()}</div>
                <div className="text-xs text-purple-600 mt-1">
                  Direct beneficiaries
                </div>
              </div>

              <div className="bg-gradient-to-r from-orange-50 to-orange-100 rounded-lg p-4">
                <div className="flex items-center justify-between mb-2">
                  <span className="text-sm text-orange-700">Transparency</span>
                  <div className="w-8 h-8 bg-orange-200 rounded-lg flex items-center justify-center">
                    <FileText size={16} className="text-orange-700" />
                  </div>
                </div>
                <div className="text-2xl font-bold text-orange-800">{fundUsageData.filter(p => p.receiptImageUrl).length}</div>
                <div className="text-xs text-orange-600 mt-1">
                  Receipts & documents
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
              onClick={() => setActiveTab("projects")}
              className={`pb-2 font-semibold ${
                activeTab === "projects"
                  ? "text-green-600 border-b-2 border-green-600"
                  : "text-gray-500"
              }`}
            >
              Project Details
            </button>
            <button
              onClick={() => setActiveTab("receipts")}
              className={`pb-2 font-semibold ${
                activeTab === "receipts"
                  ? "text-green-600 border-b-2 border-green-600"
                  : "text-gray-500"
              }`}
            >
              Receipts & Proof
            </button>
          </div>
        </div>

        {/* Content based on active tab */}
        <div className="px-6 pb-6">
          {activeTab === "overview" && (
            <div className="grid grid-cols-3 gap-6">
              {/* Fund Allocation Chart */}
              <div className="col-span-2 bg-white border border-gray-200 rounded-xl p-6">
                <h3 className="text-lg font-bold mb-4">Fund Allocation by Category</h3>
                <div className="space-y-4">
                  {Object.entries(categoryBreakdown).map(([category, amount]) => {
                    const percentage = ((amount / totalUsed) * 100).toFixed(1);
                    return (
                      <div key={category} className="flex items-center justify-between">
                        <div className="flex items-center gap-3">
                          <div className={`w-4 h-4 rounded ${getCategoryColor(category)}`}></div>
                          <span className="font-medium">{category}</span>
                        </div>
                        <div className="text-right">
                          <div className="font-bold">Tk {amount.toLocaleString()}</div>
                          <div className="text-sm text-gray-500">{percentage}%</div>
                        </div>
                      </div>
                    );
                  })}
                </div>
              </div>

              {/* Utilization Progress */}
              <div className="bg-white border border-gray-200 rounded-xl p-6">
                <h3 className="text-lg font-bold mb-4">Utilization Progress</h3>
                <div className="space-y-4">
                  <div className="text-center">
                    <div className="text-3xl font-bold text-green-600">{utilizationRate}%</div>
                    <div className="text-sm text-gray-500">Funds Utilized</div>
                  </div>
                  <div className="w-full bg-gray-200 rounded-full h-4">
                    <div 
                      className="bg-green-600 h-4 rounded-full transition-all duration-300"
                      style={{ width: `${utilizationRate}%` }}
                    ></div>
                  </div>
                  <div className="text-sm text-gray-600">
                    <div className="flex justify-between">
                      <span>Used: Tk {totalUsed.toLocaleString()}</span>
                      <span>Remaining: Tk {(totalDonated - totalUsed).toLocaleString()}</span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          )}

          {activeTab === "projects" && (
            <div>
              {/* Filters */}
              <div className="flex gap-4 mb-6">
                <div className="relative flex-1">
                  <Search className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400" size={18} />
                  <input
                    type="text"
                    placeholder="Search projects..."
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                    className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-green-500"
                  />
                </div>
                <select
                  value={filterCategory}
                  onChange={(e) => setFilterCategory(e.target.value)}
                  className="px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-green-500"
                >
                  <option value="all">All Categories</option>
                  {[...new Set(fundUsageData.map(u => u.project?.projectType?.typeName).filter(Boolean))].map(category => (
                    <option key={category} value={category}>{category}</option>
                  ))}
                </select>
              </div>

              {/* Projects Table */}
              <div className="bg-white border border-gray-200 rounded-xl overflow-hidden">
                <table className="w-full">
                  <thead className="bg-gray-50 border-b border-gray-200">
                    <tr>
                      <th className="text-left py-3 px-4 text-sm font-semibold text-gray-700">Project</th>
                      <th className="text-left py-3 px-4 text-sm font-semibold text-gray-700">Category</th>
                      <th className="text-left py-3 px-4 text-sm font-semibold text-gray-700">Donated</th>
                      <th className="text-left py-3 px-4 text-sm font-semibold text-gray-700">Used</th>
                      <th className="text-left py-3 px-4 text-sm font-semibold text-gray-700">Beneficiaries</th>
                      <th className="text-left py-3 px-4 text-sm font-semibold text-gray-700">Status</th>
                      <th className="text-left py-3 px-4 text-sm font-semibold text-gray-700">Actions</th>
                    </tr>
                  </thead>
                  <tbody>
                    {filterProjects().map((utilization) => {
                      const donatedAmount = parseFloat(utilization.donation?.amount || 0);
                      const usedAmount = parseFloat(utilization.amountUsed || 0);
                      const category = utilization.project?.projectType?.typeName || 'General';
                      
                      return (
                        <tr key={utilization.utilizationId} className="border-b border-gray-200 last:border-0 hover:bg-gray-50">
                          <td className="py-3 px-4">
                            <div className="font-semibold">{utilization.project?.projectTitle || 'General Fund'}</div>
                            <div className="text-sm text-gray-500">{utilization.detailedDescription || utilization.specificPurpose}</div>
                          </td>
                          <td className="py-3 px-4">
                            <span className={`inline-flex items-center px-2 py-1 rounded-full text-xs font-medium text-white ${getCategoryColor(category)}`}>
                              {category}
                            </span>
                          </td>
                          <td className="py-3 px-4 font-bold">Tk {donatedAmount.toLocaleString()}</td>
                          <td className="py-3 px-4">
                            <div className="font-bold">Tk {usedAmount.toLocaleString()}</div>
                            <div className="text-sm text-gray-500">
                              {donatedAmount > 0 ? ((usedAmount / donatedAmount) * 100).toFixed(1) : '0'}% used
                            </div>
                          </td>
                          <td className="py-3 px-4 font-semibold">{utilization.project?.beneficiaryCount || 1}</td>
                          <td className="py-3 px-4">
                            <span className={`inline-flex items-center px-3 py-1 rounded-full text-xs font-semibold ${getStatusBadge(utilization.utilizationStatus)}`}>
                              {utilization.utilizationStatus?.toLowerCase()}
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

          {activeTab === "receipts" && (
            <div className="grid grid-cols-1 gap-6">
              {filterProjects().map((utilization) => (
                <div key={utilization.utilizationId} className="bg-white border border-gray-200 rounded-xl p-6">
                  <div className="flex items-start justify-between mb-4">
                    <div>
                      <h3 className="text-lg font-bold">{utilization.project?.projectTitle || 'General Fund'}</h3>
                      <p className="text-gray-600">{utilization.detailedDescription || utilization.specificPurpose}</p>
                      <div className="flex items-center gap-4 mt-2 text-sm text-gray-500">
                        <span>Amount Used: Tk {parseFloat(utilization.amountUsed || 0).toLocaleString()}</span>
                        <span>•</span>
                        <span>Date: {new Date(utilization.utilizationDate || utilization.createdAt).toLocaleDateString()}</span>
                      </div>
                    </div>
                    <span className={`inline-flex items-center px-3 py-1 rounded-full text-xs font-semibold ${getStatusBadge(utilization.utilizationStatus)}`}>
                      {utilization.utilizationStatus?.toLowerCase()}
                    </span>
                  </div>
                  
                  <div>
                    <h4 className="font-semibold mb-3">Supporting Documents</h4>
                    {utilization.receiptImageUrl ? (
                      <div className="grid grid-cols-3 gap-3">
                        <div className="flex items-center gap-3 p-3 border border-gray-200 rounded-lg hover:bg-gray-50">
                          <FileText className="w-8 h-8 text-gray-400" />
                          <div className="flex-1">
                            <div className="font-medium text-sm">Receipt - {utilization.billInvoiceNumber || 'N/A'}</div>
                            <div className="text-xs text-gray-500">Image Document</div>
                          </div>
                          <button 
                            onClick={() => window.open(utilization.receiptImageUrl, '_blank')}
                            className="text-green-600 hover:text-green-800"
                          >
                            <Eye size={16} />
                          </button>
                        </div>
                      </div>
                    ) : (
                      <p className="text-gray-500 text-sm">No receipts available</p>
                    )}
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  );
}