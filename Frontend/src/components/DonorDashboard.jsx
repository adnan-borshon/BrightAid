import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { ChevronRight, MoreVertical, DollarSign, TrendingUp, Award, Download, Eye, Filter } from 'lucide-react';
import Sidebar from './DashSidebar';
import { API_BASE_URL } from '../config/api';

const emptyData = {
  donor: null,
  donations: [],
  fundUtilization: [],
  fundTransparency: [],
  transactions: [],
  gamification: null
};

export default function DonorDashboard() {
  const { donorId } = useParams();
  const [activeNav, setActiveNav] = useState('Dashboard');
  const [loading, setLoading] = useState(true);
  const [data, setData] = useState(emptyData);
  const [apiError, setApiError] = useState(false);
  const [selectedFilter, setSelectedFilter] = useState('all');

  useEffect(() => {
    if (donorId) {
      fetchDonorData();
    }
  }, [donorId]);

  const fetchDonorData = async () => {
    setLoading(true);
    try {
      const [donorRes, donationsRes, transactionsRes, utilizationRes, transparencyRes, gamificationRes] = await Promise.all([
        fetch(`${API_BASE_URL}/donors/${donorId}`).catch(() => null),
        fetch(`${API_BASE_URL}/donations`).catch(() => null),
        fetch(`${API_BASE_URL}/payment-transactions`).catch(() => null),
        fetch(`${API_BASE_URL}/fund-utilization`).catch(() => null),
        fetch(`${API_BASE_URL}/fund-transparency`).catch(() => null),
        fetch(`${API_BASE_URL}/donor-gamification/${donorId}`).catch(() => null),
      ]);

      const newData = { ...emptyData };
      
      if (donorRes && donorRes.ok) {
        newData.donor = await donorRes.json();
      }
      
      if (donationsRes && donationsRes.ok) {
        const donationsData = await donationsRes.json();
        const allDonations = Array.isArray(donationsData) ? donationsData : donationsData.data || [];
        newData.donations = allDonations.filter(d => (d.donorId || d.donor_id) == donorId);
      }
      
      if (transactionsRes && transactionsRes.ok) {
        const transactionsData = await transactionsRes.json();
        const allTransactions = Array.isArray(transactionsData) ? transactionsData : transactionsData.data || [];
        newData.transactions = allTransactions.filter(t => (t.donorId || t.donor_id) == donorId);
      }
      
      if (utilizationRes && utilizationRes.ok) {
        const utilizationData = await utilizationRes.json();
        const allUtilization = Array.isArray(utilizationData) ? utilizationData : utilizationData.data || [];
        newData.fundUtilization = allUtilization;
      }
      
      if (transparencyRes && transparencyRes.ok) {
        const transparencyData = await transparencyRes.json();
        newData.fundTransparency = Array.isArray(transparencyData) ? transparencyData : transparencyData.data || [];
      }
      
      if (gamificationRes && gamificationRes.ok) {
        newData.gamification = await gamificationRes.json();
      }
      
      setData(newData);
      setApiError(false);
    } catch (error) {
      console.error('Error fetching donor data:', error);
      setApiError(true);
    } finally {
      setLoading(false);
    }
  };

  const formatCurrency = (amount) => {
    return `৳${Math.abs(amount || 0).toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`;
  };

  const getTotalDonated = () => {
    return data.donations.reduce((sum, d) => sum + (d.amount || 0), 0);
  };

  const getSuccessfulTransactions = () => {
    return data.transactions.filter(t => (t.status || '').toLowerCase() === 'success').length;
  };

  const getLevelInfo = () => {
    const level = data.gamification?.currentLevel || data.gamification?.current_level || 'Bronze';
    const points = data.gamification?.totalPoints || data.gamification?.total_points || 0;
    const badges = data.gamification?.badgesEarned || data.gamification?.badges_earned || [];
    const badgeCount = Array.isArray(badges) ? badges.length : (badges ? JSON.parse(badges).length : 0);
    
    return { level, points, badgeCount };
  };

  const getStatusColor = (status) => {
    const statusMap = {
      'success': 'bg-green-100 text-green-700',
      'pending': 'bg-orange-100 text-orange-700',
      'processing': 'bg-blue-100 text-blue-700',
      'failed': 'bg-red-100 text-red-700',
      'cancelled': 'bg-gray-100 text-gray-700',
      'refunded': 'bg-purple-100 text-purple-700',
    };
    return statusMap[status?.toLowerCase()] || 'bg-gray-100 text-gray-700';
  };

  const getTransactionIcon = (type) => {
    return type?.toLowerCase() === 'refund' ? '↩️' : '💸';
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

  const donorData = {
    schoolName: data.donor?.organizationName || data.donor?.organization_name || `Donor ${donorId}`,
    total_students: data.donations.length,
    active_projects: getSuccessfulTransactions(),
  };

  return (
    <div className="flex h-screen bg-gray-50">
      <Sidebar activeNav={activeNav} setActiveNav={setActiveNav} schoolData={donorData} />

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
              {data.donor?.organizationName || data.donor?.organization_name || `Donor ${donorId}`}
              {data.donor && <span className="text-green-500">✓</span>}
            </div>
            <div className="text-sm text-gray-600 mb-2">
              Thank you for your generosity! Your contributions are transforming lives and reducing dropout rates.
            </div>
            <div className="text-xs text-gray-500 flex items-center gap-2">
              <Award className="w-4 h-4 text-yellow-500" />
              <span>{getLevelInfo().level} Donor • {getLevelInfo().points} Points • {getLevelInfo().badgeCount} Badges</span>
            </div>
          </div>
          <div className="w-96 h-48 rounded-xl overflow-hidden shadow-lg">
            <div className="w-full h-full bg-gradient-to-br from-green-600 to-green-800 flex items-center justify-center text-white">
              <Award className="w-24 h-24 opacity-20" />
            </div>
          </div>
        </div>

        {/* Overview Cards */}
        <div className="px-6 mb-8">
          <div className="mb-4">
            <h2 className="text-2xl font-bold text-gray-800">Overview</h2>
            <p className="text-sm text-gray-500">Track your impact and contributions</p>
          </div>
          
          <div className="grid grid-cols-3 gap-6">
            <div className="bg-white rounded-xl p-6 shadow-sm border border-gray-100">
              <div className="flex items-start justify-between mb-4">
                <div className="w-10 h-10 bg-green-100 rounded-lg flex items-center justify-center">
                  <DollarSign className="w-5 h-5 text-green-600" />
                </div>
                <MoreVertical className="w-4 h-4 text-gray-400" />
              </div>
              <div className="text-sm text-gray-500 mb-1">Total Donated</div>
              <div className="text-3xl font-bold text-gray-800 mb-2">
                {formatCurrency(getTotalDonated())}
              </div>
              <div className="text-xs text-green-600">+ {data.donations.length} donations made</div>
            </div>

            <div className="bg-white rounded-xl p-6 shadow-sm border border-gray-100">
              <div className="flex items-start justify-between mb-4">
                <div className="w-10 h-10 bg-blue-100 rounded-lg flex items-center justify-center">
                  <TrendingUp className="w-5 h-5 text-blue-600" />
                </div>
                <MoreVertical className="w-4 h-4 text-gray-400" />
              </div>
              <div className="text-sm text-gray-500 mb-1">Successful Transactions</div>
              <div className="text-3xl font-bold text-gray-800 mb-2">{getSuccessfulTransactions()}</div>
              <div className="text-xs text-green-600">
                {data.transactions.filter(t => (t.status || '').toLowerCase() === 'pending').length} pending
              </div>
            </div>

            <div className="bg-white rounded-xl p-6 shadow-sm border border-gray-100">
              <div className="flex items-start justify-between mb-4">
                <div className="w-10 h-10 bg-yellow-100 rounded-lg flex items-center justify-center">
                  <Award className="w-5 h-5 text-yellow-600" />
                </div>
                <MoreVertical className="w-4 h-4 text-gray-400" />
              </div>
              <div className="text-sm text-gray-500 mb-1">Impact Level</div>
              <div className="text-3xl font-bold text-gray-800 mb-2">{getLevelInfo().level}</div>
              <div className="text-xs text-green-600">{getLevelInfo().points} points earned</div>
            </div>
          </div>
        </div>

        {/* Fund Transparency Section */}
        <div className="px-6 mb-8">
          <div className="flex items-center justify-between mb-4">
            <div>
              <h2 className="text-xl font-bold text-gray-800 flex items-center gap-2">
                <span className="text-green-600">🔍</span>
                Fund Transparency
              </h2>
              <p className="text-sm text-gray-500">See exactly how your donations are being used</p>
            </div>
            <button className="secondary !border-0 hover:!bg-gray-50 hover:!text-[#0E792E] text-sm font-medium flex items-center gap-1">
              View All <ChevronRight className="w-4 h-4" />
            </button>
          </div>

          <div className="grid grid-cols-3 gap-4">
            {data.fundTransparency.length > 0 ? data.fundTransparency.slice(0, 3).map((transparency, idx) => (
              <div key={idx} className="bg-white rounded-xl p-4 shadow-sm border border-gray-100">
                <div className="flex items-center justify-between mb-3">
                  <span className="text-xs font-semibold text-green-600">VERIFIED ✓</span>
                  <Eye className="w-4 h-4 text-gray-400" />
                </div>
                <div className="text-sm font-semibold text-gray-800 mb-2">
                  Project Update #{transparency.utilizationId || transparency.utilization_id || idx + 1}
                </div>
                <div className="text-xs text-gray-600 mb-3">
                  {transparency.beneficiaryFeedback || transparency.beneficiary_feedback || 'Funds utilized successfully for project improvements'}
                </div>
                <div className="flex gap-2 mb-3">
                  <div className="w-full h-20 bg-gray-200 rounded-lg flex items-center justify-center text-xs text-gray-500">
                    Before Photo
                  </div>
                  <div className="w-full h-20 bg-green-100 rounded-lg flex items-center justify-center text-xs text-green-700">
                    After Photo
                  </div>
                </div>
                <div className="text-xs text-gray-500">
                  {transparency.quantityPurchased || transparency.quantity_purchased || 'N/A'} {transparency.unitMeasurement || transparency.unit_measurement || 'units'} @ {formatCurrency(transparency.unitCost || transparency.unit_cost || 0)}
                </div>
              </div>
            )) : (
              <div className="col-span-3 text-center py-12 text-gray-500 bg-white rounded-xl">
                <div className="flex flex-col items-center gap-3">
                  <span className="text-4xl">🔍</span>
                  <span className="text-lg font-medium">No transparency records yet</span>
                  <span className="text-sm">Records will appear as funds are utilized</span>
                </div>
              </div>
            )}
          </div>
        </div>

        {/* Fund Utilization & Transactions */}
        <div className="px-6 pb-8 grid grid-cols-2 gap-6">
          {/* Fund Utilization */}
          <div className="bg-white rounded-xl p-6 shadow-sm border border-gray-100">
            <div className="flex items-center justify-between mb-4">
              <h2 className="text-lg font-bold text-gray-800">Fund Utilization</h2>
              <button className="secondary !border-0 hover:!bg-gray-50 hover:!text-[#0E792E] px-3 py-1.5 text-sm rounded-lg flex items-center gap-1">
                <Download className="w-4 h-4" />
                Export
              </button>
            </div>

            <div className="space-y-3">
              {data.fundUtilization.length > 0 ? data.fundUtilization.slice(0, 5).map((util, idx) => (
                <div key={idx} className="flex items-center justify-between p-3 bg-gray-50 rounded-lg hover:bg-gray-100 transition-colors">
                  <div className="flex-1">
                    <div className="text-sm font-medium text-gray-800">
                      {util.specificPurpose || util.specific_purpose || 'Educational materials'}
                    </div>
                    <div className="text-xs text-gray-500">
                      {util.vendorName || util.vendor_name || 'Vendor'} • {util.utilizationDate || util.utilization_date || new Date().toLocaleDateString()}
                    </div>
                  </div>
                  <div className="text-sm font-semibold text-gray-800">
                    {formatCurrency(util.amountUsed || util.amount_used || 0)}
                  </div>
                </div>
              )) : (
                <div className="text-center py-8 text-gray-500">
                  <span className="text-2xl">📊</span>
                  <p className="text-sm mt-2">No utilization records yet</p>
                </div>
              )}
            </div>
          </div>

          {/* Recent Transactions */}
          <div className="bg-white rounded-xl p-6 shadow-sm border border-gray-100">
            <div className="flex items-center justify-between mb-4">
              <h2 className="text-lg font-bold text-gray-800">Recent Transactions</h2>
              <button className="secondary !border-0 hover:!bg-gray-50 hover:!text-[#0E792E] px-3 py-1.5 text-sm rounded-lg">
                <Filter className="w-4 h-4" />
              </button>
            </div>

            <div className="space-y-3">
              {data.transactions.length > 0 ? data.transactions.slice(0, 5).map((transaction) => (
                <div key={transaction.transactionId || transaction.transaction_id} className="flex items-center justify-between p-3 bg-gray-50 rounded-lg">
                  <div className="flex items-center gap-3 flex-1">
                    <div className="text-xl">{getTransactionIcon(transaction.transactionType || transaction.transaction_type)}</div>
                    <div>
                      <div className="text-sm font-medium text-gray-800">
                        {transaction.transactionReference || transaction.transaction_reference || 'N/A'}
                      </div>
                      <div className="text-xs text-gray-500">
                        {transaction.paymentMethod || transaction.payment_method || 'N/A'} • {transaction.initiatedAt || transaction.initiated_at || 'N/A'}
                      </div>
                    </div>
                  </div>
                  <div className="flex items-center gap-2">
                    <div className="text-sm font-semibold text-gray-800">
                      {formatCurrency(transaction.amount || 0)}
                    </div>
                    <span className={`text-xs px-2 py-1 rounded-full ${getStatusColor(transaction.status)}`}>
                      {(transaction.status || 'pending').toUpperCase()}
                    </span>
                  </div>
                </div>
              )) : (
                <div className="text-center py-8 text-gray-500">
                  <span className="text-2xl">💳</span>
                  <p className="text-sm mt-2">No transactions yet</p>
                </div>
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
