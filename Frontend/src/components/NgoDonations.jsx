import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { Heart, Users, Briefcase, TrendingUp, Plus } from 'lucide-react';
import NgoDashSidebar from './NgoDashSidebar';

export default function NgoDonations() {
  const { ngoId } = useParams();
  const [loading, setLoading] = useState(true);
  const [studentDonations, setStudentDonations] = useState([]);
  const [projectDonations, setProjectDonations] = useState([]);
  const [activeTab, setActiveTab] = useState('all');

  useEffect(() => {
    if (ngoId) {
      fetchDonations();
    }
  }, [ngoId]);

  const fetchDonations = async () => {
    setLoading(true);
    try {
      const API_BASE_URL = 'http://localhost:8081/api';
      
      // Fetch both student and project donations for this NGO
      const [studentRes, projectRes] = await Promise.all([
        fetch(`${API_BASE_URL}/ngo-student-donations/ngo/${ngoId}`).catch(() => null),
        fetch(`${API_BASE_URL}/ngo-project-donations/ngo/${ngoId}`).catch(() => null)
      ]);

      let studentDonations = [];
      let projectDonations = [];

      if (studentRes && studentRes.ok) {
        const studentData = await studentRes.json();
        studentDonations = Array.isArray(studentData) ? studentData : [];
      }

      if (projectRes && projectRes.ok) {
        const projectData = await projectRes.json();
        projectDonations = Array.isArray(projectData) ? projectData : [];
      }

      console.log('Fetched donations:', { studentDonations, projectDonations });
      setStudentDonations(studentDonations);
      setProjectDonations(projectDonations);
      
    } catch (error) {
      console.error('Error fetching donations:', error);
    } finally {
      setLoading(false);
    }
  };

  const formatCurrency = (amount) => {
    return `Tk ${Math.abs(amount || 0).toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`;
  };

  const totalDonated = [...studentDonations, ...projectDonations].reduce((sum, d) => sum + parseFloat(d.amount || 0), 0);
  const uniqueStudents = new Set(studentDonations.filter(d => d.student).map(d => d.student.studentId)).size;
  const uniqueProjects = new Set(projectDonations.filter(d => d.project).map(d => d.project.projectId)).size;

  const getFilteredDonations = () => {
    if (activeTab === 'students') return studentDonations.map(d => ({...d, type: 'student'}));
    if (activeTab === 'projects') return projectDonations.map(d => ({...d, type: 'project'}));
    return [...studentDonations.map(d => ({...d, type: 'student'})), ...projectDonations.map(d => ({...d, type: 'project'}))];
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

  return (
    <div className="flex h-screen bg-gray-50">
      <NgoDashSidebar />
      
      <div className="flex-1 overflow-auto">
        <div className="p-6">
          <div className="mb-6">
            <h1 className="text-2xl font-bold text-gray-800">My Donations</h1>
            <p className="text-sm text-gray-500">Track your donations and impact</p>
          </div>

          {/* Summary Cards */}
          <div className="grid grid-cols-4 gap-6 mb-8">
            <div className="bg-white rounded-xl p-6 shadow-sm border border-gray-100">
              <div className="flex items-center gap-3 mb-3">
                <div className="w-12 h-12 bg-green-100 rounded-full flex items-center justify-center">
                  <Heart className="w-6 h-6 text-green-600" />
                </div>
                <div>
                  <div className="text-sm text-gray-500">Total Donated</div>
                  <div className="text-2xl font-bold text-gray-800">{formatCurrency(totalDonated)}</div>
                </div>
              </div>
            </div>

            <div className="bg-white rounded-xl p-6 shadow-sm border border-gray-100">
              <div className="flex items-center gap-3 mb-3">
                <div className="w-12 h-12 bg-green-100 rounded-full flex items-center justify-center">
                  <Users className="w-6 h-6 text-green-600" />
                </div>
                <div>
                  <div className="text-sm text-gray-500">Students Helped</div>
                  <div className="text-2xl font-bold text-gray-800">{uniqueStudents}</div>
                </div>
              </div>
            </div>

            <div className="bg-white rounded-xl p-6 shadow-sm border border-gray-100">
              <div className="flex items-center gap-3 mb-3">
                <div className="w-12 h-12 bg-green-100 rounded-full flex items-center justify-center">
                  <Briefcase className="w-6 h-6 text-green-600" />
                </div>
                <div>
                  <div className="text-sm text-gray-500">Projects Supported</div>
                  <div className="text-2xl font-bold text-gray-800">{uniqueProjects}</div>
                </div>
              </div>
            </div>

            <div className="bg-white rounded-xl p-6 shadow-sm border border-gray-100">
              <div className="flex items-center gap-3 mb-3">
                <div className="w-12 h-12 bg-green-100 rounded-full flex items-center justify-center">
                  <TrendingUp className="w-6 h-6 text-green-600" />
                </div>
                <div>
                  <div className="text-sm text-gray-500">Total Donations</div>
                  <div className="text-2xl font-bold text-gray-800">{studentDonations.length + projectDonations.length}</div>
                </div>
              </div>
            </div>
          </div>

          {/* Donations Table */}
          <div className="bg-white rounded-xl shadow-sm border border-gray-100">
            <div className="border-b px-6 py-4">
              <div className="flex gap-6">
                <button
                  onClick={() => setActiveTab('all')}
                  className={`pb-2 font-semibold ${
                    activeTab === 'all'
                      ? 'text-green-600 border-b-2 border-green-600'
                      : 'text-gray-500'
                  }`}
                >
                  All Donations
                </button>
                <button
                  onClick={() => setActiveTab('students')}
                  className={`pb-2 font-semibold ${
                    activeTab === 'students'
                      ? 'text-green-600 border-b-2 border-green-600'
                      : 'text-gray-500'
                  }`}
                >
                  Student Donations
                </button>
                <button
                  onClick={() => setActiveTab('projects')}
                  className={`pb-2 font-semibold ${
                    activeTab === 'projects'
                      ? 'text-green-600 border-b-2 border-green-600'
                      : 'text-gray-500'
                  }`}
                >
                  Project Donations
                </button>
              </div>
            </div>

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
                  {getFilteredDonations().length > 0 ? getFilteredDonations().map((donation, index) => (
                    <tr key={index} className="hover:bg-gray-50">
                      <td className="px-6 py-4 text-sm text-gray-900">
                        {donation.donatedAt ? new Date(donation.donatedAt).toLocaleDateString() : 
                         donation.createdAt ? new Date(donation.createdAt).toLocaleDateString() : 'N/A'}
                      </td>
                      <td className="px-6 py-4">
                        <span className={`inline-flex px-2 py-1 text-xs font-medium rounded-full ${
                          donation.type === 'student' 
                            ? 'bg-green-100 text-green-700' 
                            : 'bg-purple-100 text-purple-700'
                        }`}>
                          {donation.type === 'student' ? 'Student' : 'Project'}
                        </span>
                      </td>
                      <td className="px-6 py-4 text-sm text-gray-900">
                        {donation.type === 'student' 
                          ? `Student ID: ${donation.studentId || 'N/A'}`
                          : `Project ID: ${donation.projectId || 'N/A'}`}
                      </td>
                      <td className="px-6 py-4 text-sm font-semibold text-gray-900">
                        {formatCurrency(parseFloat(donation.amount || 0))}
                      </td>
                      <td className="px-6 py-4">
                        <span className={`inline-flex px-2 py-1 text-xs font-medium rounded-full ${
                          donation.paymentStatus === 'COMPLETED' 
                            ? 'bg-green-100 text-green-700' 
                            : donation.paymentStatus === 'PENDING'
                            ? 'bg-yellow-100 text-yellow-700'
                            : 'bg-red-100 text-red-700'
                        }`}>
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
          </div>
        </div>
      </div>
    </div>
  );
}