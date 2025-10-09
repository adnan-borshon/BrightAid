import { useState, useEffect } from "react";
import { useParams } from 'react-router-dom';
import { Search, DollarSign, PieChart, BarChart3, TrendingUp, FileText, CheckCircle, Download, Eye, Calendar, Filter, Users, Target } from "lucide-react";
import NgoDashSidebar from './NgoDashSidebar';



export default function NgoReporting() {
  const { ngoId } = useParams();
  const [searchTerm, setSearchTerm] = useState("");
  const [filterCategory, setFilterCategory] = useState("all");
  const [activeTab, setActiveTab] = useState("overview");
  const [dateRange, setDateRange] = useState("all");
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchFundUtilizationData();
  }, [ngoId]);

  const [fundUtilizationData, setFundUtilizationData] = useState([]);

  const fetchFundUtilizationData = async () => {
    try {
      setLoading(true);
      const response = await fetch(`http://localhost:8081/api/fund-utilization/ngo/${ngoId}`);
      if (response.ok) {
        const data = await response.json();
        setFundUtilizationData(data);
      } else {
        console.error('Failed to fetch fund utilization data');
        setFundUtilizationData([]);
      }
    } catch (err) {
      console.error('Error fetching fund utilization data:', err);
      setFundUtilizationData([]);
    } finally {
      setLoading(false);
    }
  };

  const totalReceived = fundUtilizationData.reduce((sum, d) => sum + parseFloat(d.donation?.amount || 0), 0);
  const totalUsed = fundUtilizationData.reduce((sum, d) => sum + parseFloat(d.amountUsed || 0), 0);
  const totalBeneficiaries = fundUtilizationData.reduce((sum, d) => sum + (d.project?.beneficiaryCount || 1), 0);
  const utilizationRate = totalReceived > 0 ? ((totalUsed / totalReceived) * 100).toFixed(1) : '0.0';
  const uniqueSchools = [...new Set(fundUtilizationData.map(d => d.project?.school?.schoolName).filter(Boolean))].length;

  const categoryBreakdown = fundUtilizationData.reduce((acc, item) => {
    const category = item.project?.projectType?.typeName || 'General';
    acc[category] = (acc[category] || 0) + parseFloat(item.amountUsed || 0);
    return acc;
  }, {});

  const filterProjects = () => {
    return fundUtilizationData.filter((utilization) => {
      const projectName = utilization.project?.projectTitle || 'General Fund';
      const description = utilization.detailedDescription || utilization.specificPurpose || '';
      const schoolName = utilization.project?.school?.schoolName || '';
      const category = utilization.project?.projectType?.typeName || 'General';
      
      const matchesSearch = projectName.toLowerCase().includes(searchTerm.toLowerCase()) ||
                           description.toLowerCase().includes(searchTerm.toLowerCase()) ||
                           schoolName.toLowerCase().includes(searchTerm.toLowerCase());
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
          <p className="text-gray-600">Loading fund utilization reports...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="flex bg-gray-50 min-h-screen">
      <NgoDashSidebar />
      <div className="flex-1 bg-white">
        {/* Header */}
        <div className="border-b bg-white">
          <div className="p-6">
            <div className="flex items-center justify-between mb-4">
              <div>
                <h1 className="text-2xl font-bold mb-1">Fund Utilization Reports</h1>
                <p className="text-gray-600 text-sm">
                  Track how received funds are being utilized across projects and their impact
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
                  From donors & grants
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
                  <span className="text-sm text-purple-700">Beneficiaries</span>
                  <div className="w-8 h-8 bg-purple-200 rounded-lg flex items-center justify-center">
                    <Users size={16} className="text-purple-700" />
                  </div>
                </div>
                <div className="text-2xl font-bold text-purple-800">{totalBeneficiaries.toLocaleString()}</div>
                <div className="text-xs text-purple-600 mt-1">
                  Students & families helped
                </div>
              </div>

              <div className="bg-gradient-to-r from-orange-50 to-orange-100 rounded-lg p-4">
                <div className="flex items-center justify-between mb-2">
                  <span className="text-sm text-orange-700">Schools Reached</span>
                  <div className="w-8 h-8 bg-orange-200 rounded-lg flex items-center justify-center">
                    <Target size={16} className="text-orange-700" />
                  </div>
                </div>
                <div className="text-2xl font-bold text-orange-800">{uniqueSchools}</div>
                <div className="text-xs text-orange-600 mt-1">
                  Educational institutions
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
              Project Utilization
            </button>
            <button
              onClick={() => setActiveTab("transparency")}
              className={`pb-2 font-semibold ${
                activeTab === "transparency"
                  ? "text-green-600 border-b-2 border-green-600"
                  : "text-gray-500"
              }`}
            >
              Transparency & Receipts
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
              {/* Fund Allocation Chart */}
              <div className="col-span-2 bg-white border border-gray-200 rounded-xl p-6">
                <h3 className="text-lg font-bold mb-4">Fund Utilization by Category</h3>
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
                <h3 className="text-lg font-bold mb-4">Fund Utilization</h3>
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
                      <span>Available: Tk {(totalReceived - totalUsed).toLocaleString()}</span>
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
                    placeholder="Search projects or schools..."
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
                  {[...new Set(fundUtilizationData.map(u => u.project?.projectType?.typeName).filter(Boolean))].map(category => (
                    <option key={category} value={category}>{category}</option>
                  ))}
                </select>
              </div>

              {/* Projects Table */}
              <div className="bg-white border border-gray-200 rounded-xl overflow-hidden">
                <table className="w-full">
                  <thead className="bg-gray-50 border-b border-gray-200">
                    <tr>
                      <th className="text-left py-3 px-4 text-sm font-semibold text-gray-700">Project & School</th>
                      <th className="text-left py-3 px-4 text-sm font-semibold text-gray-700">Category</th>
                      <th className="text-left py-3 px-4 text-sm font-semibold text-gray-700">Received</th>
                      <th className="text-left py-3 px-4 text-sm font-semibold text-gray-700">Utilized</th>
                      <th className="text-left py-3 px-4 text-sm font-semibold text-gray-700">Beneficiaries</th>
                      <th className="text-left py-3 px-4 text-sm font-semibold text-gray-700">Status</th>
                      <th className="text-left py-3 px-4 text-sm font-semibold text-gray-700">Actions</th>
                    </tr>
                  </thead>
                  <tbody>
                    {filterProjects().map((utilization) => {
                      const receivedAmount = parseFloat(utilization.donation?.amount || 0);
                      const usedAmount = parseFloat(utilization.amountUsed || 0);
                      const category = utilization.project?.projectType?.typeName || 'General';
                      
                      return (
                        <tr key={utilization.utilizationId} className="border-b border-gray-200 last:border-0 hover:bg-gray-50">
                          <td className="py-3 px-4">
                            <div className="font-semibold">{utilization.project?.projectTitle || 'General Fund'}</div>
                            <div className="text-sm text-gray-500">{utilization.project?.school?.schoolName || 'N/A'}</div>
                            <div className="text-xs text-gray-400">{utilization.detailedDescription || utilization.specificPurpose}</div>
                          </td>
                          <td className="py-3 px-4">
                            <span className={`inline-flex items-center px-2 py-1 rounded-full text-xs font-medium text-white ${getCategoryColor(category)}`}>
                              {category}
                            </span>
                          </td>
                          <td className="py-3 px-4 font-bold">Tk {receivedAmount.toLocaleString()}</td>
                          <td className="py-3 px-4">
                            <div className="font-bold">Tk {usedAmount.toLocaleString()}</div>
                            <div className="text-sm text-gray-500">
                              {receivedAmount > 0 ? ((usedAmount / receivedAmount) * 100).toFixed(1) : '0'}% used
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

          {activeTab === "transparency" && (
            <div className="grid grid-cols-1 gap-6">
              {filterProjects().map((utilization) => (
                <div key={utilization.utilizationId} className="bg-white border border-gray-200 rounded-xl p-6">
                  <div className="flex items-start justify-between mb-4">
                    <div>
                      <h3 className="text-lg font-bold">{utilization.project?.projectTitle || 'General Fund'}</h3>
                      <p className="text-gray-600">{utilization.project?.school?.schoolName || 'N/A'}</p>
                      <p className="text-sm text-gray-500">{utilization.detailedDescription || utilization.specificPurpose}</p>
                      <div className="flex items-center gap-4 mt-2 text-sm text-gray-500">
                        <span>Amount Utilized: Tk {parseFloat(utilization.amountUsed || 0).toLocaleString()}</span>
                        <span>•</span>
                        <span>Date: {new Date(utilization.utilizationDate || utilization.createdAt).toLocaleDateString()}</span>
                        <span>•</span>
                        <span>{utilization.project?.beneficiaryCount || 1} beneficiaries</span>
                      </div>
                    </div>
                    <span className={`inline-flex items-center px-3 py-1 rounded-full text-xs font-semibold ${getStatusBadge(utilization.utilizationStatus)}`}>
                      {utilization.utilizationStatus?.toLowerCase()}
                    </span>
                  </div>
                  
                  <div>
                    <h4 className="font-semibold mb-3">Supporting Documents & Receipts</h4>
                    {utilization.receiptImageUrl ? (
                      <div className="grid grid-cols-3 gap-3">
                        <div className="flex items-center gap-3 p-3 border border-gray-200 rounded-lg hover:bg-gray-50">
                          <FileText className="w-8 h-8 text-gray-400" />
                          <div className="flex-1">
                            <div className="font-medium text-sm">Receipt - {utilization.billInvoiceNumber || 'N/A'}</div>
                            <div className="text-xs text-gray-500">Image Document</div>
                            {utilization.vendorName && (
                              <div className="text-xs text-gray-400">Vendor: {utilization.vendorName}</div>
                            )}
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

          {activeTab === "impact" && (
            <div className="grid grid-cols-2 gap-6">
              {/* Impact Metrics */}
              <div className="bg-white border border-gray-200 rounded-xl p-6">
                <h3 className="text-lg font-bold mb-4">Impact Metrics</h3>
                <div className="space-y-4">
                  <div className="flex justify-between items-center p-3 bg-gray-50 rounded-lg">
                    <span className="font-medium">Total Students Benefited</span>
                    <span className="text-xl font-bold text-green-600">{totalBeneficiaries}</span>
                  </div>
                  <div className="flex justify-between items-center p-3 bg-gray-50 rounded-lg">
                    <span className="font-medium">Schools Supported</span>
                    <span className="text-xl font-bold text-blue-600">{uniqueSchools}</span>
                  </div>
                  <div className="flex justify-between items-center p-3 bg-gray-50 rounded-lg">
                    <span className="font-medium">Active Projects</span>
                    <span className="text-xl font-bold text-purple-600">{fundUtilizationData.filter(p => p.utilizationStatus === 'APPROVED' || p.utilizationStatus === 'PENDING').length}</span>
                  </div>
                  <div className="flex justify-between items-center p-3 bg-gray-50 rounded-lg">
                    <span className="font-medium">Completed Projects</span>
                    <span className="text-xl font-bold text-orange-600">{fundUtilizationData.filter(p => p.utilizationStatus === 'COMPLETED').length}</span>
                  </div>
                </div>
              </div>

              {/* Cost Per Beneficiary */}
              <div className="bg-white border border-gray-200 rounded-xl p-6">
                <h3 className="text-lg font-bold mb-4">Cost Efficiency</h3>
                <div className="space-y-4">
                  <div className="text-center p-4 bg-green-50 rounded-lg">
                    <div className="text-2xl font-bold text-green-600">
                      Tk {Math.round(totalUsed / totalBeneficiaries).toLocaleString()}
                    </div>
                    <div className="text-sm text-green-700">Cost per beneficiary</div>
                  </div>
                  <div className="space-y-3">
                    {Object.entries(categoryBreakdown).map(([category, amount]) => {
                      const categoryBeneficiaries = fundUtilizationData
                        .filter(p => (p.project?.projectType?.typeName || 'General') === category)
                        .reduce((sum, p) => sum + (p.project?.beneficiaryCount || 1), 0);
                      const costPerBeneficiary = categoryBeneficiaries > 0 ? Math.round(amount / categoryBeneficiaries) : 0;
                      
                      return (
                        <div key={category} className="flex justify-between items-center">
                          <span className="text-sm font-medium">{category}</span>
                          <span className="text-sm font-bold">Tk {costPerBeneficiary.toLocaleString()}/person</span>
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