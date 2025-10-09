import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { Search, Filter, Download, Heart, Users, Briefcase, Calendar } from 'lucide-react';
import NgoDashSidebar from './NgoDashSidebar';

export default function NgoDonationHistory() {
  const { ngoId } = useParams();
  const [loading, setLoading] = useState(true);
  const [donationsData, setDonationsData] = useState([]);
  const [filteredDonations, setFilteredDonations] = useState([]);
  const [searchQuery, setSearchQuery] = useState('');
  const [statusFilter, setStatusFilter] = useState('all');
  const [typeFilter, setTypeFilter] = useState('all');
  const [currentPage, setCurrentPage] = useState(1);
  const donationsPerPage = 10;

  useEffect(() => {
    if (ngoId) {
      fetchDonationHistory();
    }
  }, [ngoId]);

  const fetchDonationHistory = async () => {
    setLoading(true);
    try {
      const API_BASE_URL = 'http://localhost:8081/api';
      const [studentDonationsRes, projectDonationsRes] = await Promise.all([
        fetch(`${API_BASE_URL}/ngo-student-donations`).catch(() => null),
        fetch(`${API_BASE_URL}/ngo-project-donations`).catch(() => null),
      ]);

      let allDonations = [];
      
      if (studentDonationsRes && studentDonationsRes.ok) {
        const studentDonations = await studentDonationsRes.json();
        const ngoStudentDonations = Array.isArray(studentDonations) 
          ? studentDonations.filter(d => d.ngoId == ngoId).map(d => ({
              ...d, 
              type: 'student',
              id: d.studentDonationId,
              recipient: `Student ID: ${d.studentId}`,
              date: d.donatedAt || d.createdAt
            }))
          : [];
        allDonations = [...allDonations, ...ngoStudentDonations];
      }
      
      if (projectDonationsRes && projectDonationsRes.ok) {
        const projectDonations = await projectDonationsRes.json();
        const ngoProjectDonations = Array.isArray(projectDonations) 
          ? projectDonations.filter(d => d.ngoId == ngoId).map(d => ({
              ...d, 
              type: 'project',
              id: d.projectDonationId,
              recipient: `Project ID: ${d.projectId}`,
              date: d.donatedAt || d.createdAt
            }))
          : [];
        allDonations = [...allDonations, ...ngoProjectDonations];
      }

      // Sort by date (newest first)
      allDonations.sort((a, b) => new Date(b.date) - new Date(a.date));
      
      setDonationsData(allDonations);
      setFilteredDonations(allDonations);
    } catch (error) {
      console.error('Error fetching donation history:', error);
    } finally {
      setLoading(false);
    }
  };

  // Filter donations based on search and filters
  useEffect(() => {
    let filtered = donationsData;

    // Search filter
    if (searchQuery) {
      filtered = filtered.filter(d => 
        d.recipient.toLowerCase().includes(searchQuery.toLowerCase()) ||
        (d.donorMessage || d.message || '').toLowerCase().includes(searchQuery.toLowerCase())
      );
    }

    // Status filter
    if (statusFilter !== 'all') {
      filtered = filtered.filter(d => d.paymentStatus === statusFilter);
    }

    // Type filter
    if (typeFilter !== 'all') {
      filtered = filtered.filter(d => d.type === typeFilter);
    }

    setFilteredDonations(filtered);
    setCurrentPage(1); // Reset to first page when filtering
  }, [donationsData, searchQuery, statusFilter, typeFilter]);

  const formatCurrency = (amount) => {
    return `Tk ${Math.abs(amount || 0).toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`;
  };

  const getStatusColor = (status) => {
    switch (status) {
      case 'COMPLETED': return 'bg-green-100 text-green-700';
      case 'PENDING': return 'bg-yellow-100 text-yellow-700';
      case 'FAILED': return 'bg-red-100 text-red-700';
      default: return 'bg-gray-100 text-gray-700';
    }
  };

  const getTypeColor = (type) => {
    return type === 'student' ? 'bg-green-100 text-green-700' : 'bg-purple-100 text-purple-700';
  };

  // Pagination
  const totalPages = Math.ceil(filteredDonations.length / donationsPerPage);
  const indexOfLastDonation = currentPage * donationsPerPage;
  const indexOfFirstDonation = indexOfLastDonation - donationsPerPage;
  const currentDonations = filteredDonations.slice(indexOfFirstDonation, indexOfLastDonation);

  // Statistics
  const totalDonated = filteredDonations.reduce((sum, d) => sum + (d.amount || 0), 0);
  const completedDonations = filteredDonations.filter(d => d.paymentStatus === 'COMPLETED');
  const studentDonations = filteredDonations.filter(d => d.type === 'student');
  const projectDonations = filteredDonations.filter(d => d.type === 'project');

  if (loading) {
    return (
      <div className="flex items-center justify-center h-screen bg-gray-50">
        <div className="text-center">
          <div className="w-16 h-16 border-4 border-green-600 border-t-transparent rounded-full animate-spin mx-auto mb-4"></div>
          <p className="text-gray-600">Loading donation history...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="flex h-screen bg-gray-50">
      <NgoDashSidebar />
      
      <div className="flex-1 overflow-auto">
        <div className="p-6">
          {/* Header */}
          <div className="mb-6">
            <h1 className="text-2xl font-bold text-gray-800">Donation History</h1>
            <p className="text-sm text-gray-500">Track all your donations and their impact</p>
          </div>

          {/* Summary Stats */}
          <div className="grid grid-cols-4 gap-6 mb-6">
            <div className="bg-white rounded-xl p-4 shadow-sm border border-gray-100">
              <div className="flex items-center gap-3">
                <div className="w-10 h-10 bg-green-100 rounded-lg flex items-center justify-center">
                  <Heart className="w-5 h-5 text-green-600" />
                </div>
                <div>
                  <div className="text-sm text-gray-500">Total Donated</div>
                  <div className="text-xl font-bold text-gray-800">{formatCurrency(totalDonated)}</div>
                </div>
              </div>
            </div>

            <div className="bg-white rounded-xl p-4 shadow-sm border border-gray-100">
              <div className="flex items-center gap-3">
                <div className="w-10 h-10 bg-green-100 rounded-lg flex items-center justify-center">
                  <Users className="w-5 h-5 text-green-600" />
                </div>
                <div>
                  <div className="text-sm text-gray-500">Student Donations</div>
                  <div className="text-xl font-bold text-gray-800">{studentDonations.length}</div>
                </div>
              </div>
            </div>

            <div className="bg-white rounded-xl p-4 shadow-sm border border-gray-100">
              <div className="flex items-center gap-3">
                <div className="w-10 h-10 bg-purple-100 rounded-lg flex items-center justify-center">
                  <Briefcase className="w-5 h-5 text-purple-600" />
                </div>
                <div>
                  <div className="text-sm text-gray-500">Project Donations</div>
                  <div className="text-xl font-bold text-gray-800">{projectDonations.length}</div>
                </div>
              </div>
            </div>

            <div className="bg-white rounded-xl p-4 shadow-sm border border-gray-100">
              <div className="flex items-center gap-3">
                <div className="w-10 h-10 bg-orange-100 rounded-lg flex items-center justify-center">
                  <Calendar className="w-5 h-5 text-orange-600" />
                </div>
                <div>
                  <div className="text-sm text-gray-500">Completed</div>
                  <div className="text-xl font-bold text-gray-800">{completedDonations.length}</div>
                </div>
              </div>
            </div>
          </div>

          {/* Filters */}
          <div className="bg-white rounded-xl p-6 shadow-sm border border-gray-100 mb-6">
            <div className="flex flex-wrap gap-4 items-center">
              <div className="flex-1 min-w-64">
                <div className="relative">
                  <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 w-4 h-4" />
                  <input
                    type="text"
                    placeholder="Search donations..."
                    value={searchQuery}
                    onChange={(e) => setSearchQuery(e.target.value)}
                    className="w-full pl-10 pr-4 py-2 border border-gray-200 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent"
                  />
                </div>
              </div>
              
              <select
                value={statusFilter}
                onChange={(e) => setStatusFilter(e.target.value)}
                className="px-4 py-2 border border-gray-200 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent"
              >
                <option value="all">All Status</option>
                <option value="COMPLETED">Completed</option>
                <option value="PENDING">Pending</option>
                <option value="FAILED">Failed</option>
              </select>
              
              <select
                value={typeFilter}
                onChange={(e) => setTypeFilter(e.target.value)}
                className="px-4 py-2 border border-gray-200 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent"
              >
                <option value="all">All Types</option>
                <option value="student">Student Donations</option>
                <option value="project">Project Donations</option>
              </select>

              <button className="flex items-center gap-2 px-4 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700 transition-colors">
                <Download className="w-4 h-4" />
                Export
              </button>
            </div>
          </div>

          {/* Donations Table */}
          <div className="bg-white rounded-xl shadow-sm border border-gray-100">
            <div className="overflow-x-auto">
              <table className="w-full">
                <thead className="bg-gray-50">
                  <tr>
                    <th className="text-left text-xs font-medium text-gray-500 uppercase px-6 py-3">Date</th>
                    <th className="text-left text-xs font-medium text-gray-500 uppercase px-6 py-3">Type</th>
                    <th className="text-left text-xs font-medium text-gray-500 uppercase px-6 py-3">Recipient</th>
                    <th className="text-left text-xs font-medium text-gray-500 uppercase px-6 py-3">Amount</th>
                    <th className="text-left text-xs font-medium text-gray-500 uppercase px-6 py-3">Status</th>
                    <th className="text-left text-xs font-medium text-gray-500 uppercase px-6 py-3">Message</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-gray-200">
                  {currentDonations.length > 0 ? currentDonations.map((donation) => (
                    <tr key={donation.id} className="hover:bg-gray-50">
                      <td className="px-6 py-4 text-sm text-gray-900">
                        {new Date(donation.date).toLocaleDateString()}
                      </td>
                      <td className="px-6 py-4">
                        <span className={`inline-flex px-2 py-1 text-xs font-medium rounded-full ${getTypeColor(donation.type)}`}>
                          {donation.type === 'student' ? 'Student' : 'Project'}
                        </span>
                      </td>
                      <td className="px-6 py-4 text-sm text-gray-900">
                        {donation.recipient}
                      </td>
                      <td className="px-6 py-4 text-sm font-semibold text-gray-900">
                        {formatCurrency(donation.amount)}
                      </td>
                      <td className="px-6 py-4">
                        <span className={`inline-flex px-2 py-1 text-xs font-medium rounded-full ${getStatusColor(donation.paymentStatus)}`}>
                          {donation.paymentStatus || 'PENDING'}
                        </span>
                      </td>
                      <td className="px-6 py-4 text-sm text-gray-500 max-w-xs truncate">
                        {donation.donorMessage || donation.message || 'No message'}
                      </td>
                    </tr>
                  )) : (
                    <tr>
                      <td colSpan="6" className="px-6 py-12 text-center text-gray-500">
                        <div className="flex flex-col items-center gap-2">
                          <Heart className="w-8 h-8 text-gray-300" />
                          <span>No donations found</span>
                        </div>
                      </td>
                    </tr>
                  )}
                </tbody>
              </table>
            </div>

            {/* Pagination */}
            {totalPages > 1 && (
              <div className="flex justify-center items-center gap-2 p-6 border-t border-gray-200">
                <button
                  onClick={() => setCurrentPage(prev => Math.max(prev - 1, 1))}
                  disabled={currentPage === 1}
                  className="px-4 py-2 border border-gray-200 rounded-lg disabled:opacity-50 disabled:cursor-not-allowed hover:bg-gray-50"
                >
                  Previous
                </button>
                
                <span className="px-4 py-2 text-sm text-gray-600">
                  Page {currentPage} of {totalPages}
                </span>
                
                <button
                  onClick={() => setCurrentPage(prev => Math.min(prev + 1, totalPages))}
                  disabled={currentPage === totalPages}
                  className="px-4 py-2 border border-gray-200 rounded-lg disabled:opacity-50 disabled:cursor-not-allowed hover:bg-gray-50"
                >
                  Next
                </button>
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}