import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Search, AlertTriangle, Users as UsersIcon, Layers } from 'lucide-react';
import Sidebar from './DashSidebar';
import { useApp } from '../context/AppContext';
import SchoolProjectCard from './SchoolProjectCard';

export default function SchoolProjects() {
  const { schoolId } = useParams();
  const navigate = useNavigate();
  const { schoolData, projectsData, loading, refreshData } = useApp();
  
  // Filters
  const [searchQuery, setSearchQuery] = useState('');
  const [statusFilter, setStatusFilter] = useState('All');
  const [riskFilter, setRiskFilter] = useState('All');
  
  // Pagination
  const [currentPage, setCurrentPage] = useState(1);
  const projectsPerPage = 12;

  useEffect(() => {
    if (schoolId) {
      refreshData(schoolId);
    }
  }, [schoolId]);

  // Filter projects for this school (don't transform the data)
  const processedProjects = projectsData
    .filter(project => (project.schoolId || project.school_id) == schoolId);

  const totalCount = processedProjects.length;
  const highRiskCount = processedProjects.filter(p => 
    p.risk_status && (p.risk_status.toLowerCase().includes('high') || p.risk_status.toLowerCase().includes('at risk'))
  ).length;
  const scholarshipPendingCount = processedProjects.filter(p => 
    p.status && p.status.toLowerCase() === 'unpaid'
  ).length;

  const formatCurrency = (amount) => {
    return `$${Math.abs(amount).toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`;
  };

  const formatProjectType = (projectType) => {
    return projectType ? projectType.replace('_', ' ').replace(/\b\w/g, l => l.toUpperCase()) : 'N/A';
  };

  const getStatusBadge = (status) => {
    const badges = {
      'active': { bg: 'bg-green-100', text: 'text-green-700', label: 'Active' },
      'high risk': { bg: 'bg-red-100', text: 'text-red-700', label: 'High Risk' },
      'unpaid': { bg: 'bg-orange-100', text: 'text-orange-700', label: 'Unpaid' },
      'at risk': { bg: 'bg-red-100', text: 'text-red-700', label: 'High Risk' }
    };
    
    const key = status?.toLowerCase() || 'active';
    const badge = badges[key] || badges['active'];
    return badge;
  };

  // Filter projects
  const filteredProjects = processedProjects.filter(project => {
    const projectName = project.projectTitle || project.project_name || '';
    const matchesSearch = projectName.toLowerCase().includes(searchQuery.toLowerCase());
    const matchesStatus = statusFilter === 'All' || (project.status && project.status.toLowerCase() === statusFilter.toLowerCase());
    const riskStatus = project.riskStatus || project.risk_status || '';
    const matchesRisk = riskFilter === 'All' || 
      (riskFilter === 'High Risk' && (riskStatus.toLowerCase().includes('high') || riskStatus.toLowerCase().includes('at risk'))) ||
      (riskFilter === 'Low Risk' && riskStatus.toLowerCase().includes('low'));
    
    return matchesSearch && matchesStatus && matchesRisk;
  });

  // Pagination
  const totalPages = Math.ceil(filteredProjects.length / projectsPerPage);
  const indexOfLastProject = currentPage * projectsPerPage;
  const indexOfFirstProject = indexOfLastProject - projectsPerPage;
  const currentProjects = filteredProjects.slice(indexOfFirstProject, indexOfLastProject);

  const handleProjectClick = (projectId) => {
    console.log('Viewing project details for ID:', projectId);
    navigate(`/project-details/${projectId}`);
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center h-screen bg-gray-50">
        <div className="text-center">
          <div className="w-16 h-16 border-4 border-green-600 border-t-transparent rounded-full animate-spin mx-auto mb-4"></div>
          <p className="text-gray-600">Loading projects...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="flex h-screen bg-gray-50">
      {/* <Sidebar activeNav={activeNav} setActiveNav={setActiveNav} schoolData={data.school} /> */}
 <Sidebar />
      <div className="flex-1 overflow-auto">

        <div className="p-6">
          {/* Header */}
          <div className="mb-6">
            <h1 className="text-2xl font-bold text-gray-800">Project Overview</h1>
            <p className="text-sm text-gray-500">View your key stats at a glance</p>
          </div>

          {/* Stats Cards */}
          <div className="grid grid-cols-3 gap-6 mb-8">
            <div className="bg-white rounded-xl p-6 shadow-sm border border-gray-100">
              <div className="flex items-center gap-3 mb-3">
                <div className="w-12 h-12 bg-green-100 rounded-full flex items-center justify-center">
                  <UsersIcon className="w-6 h-6 text-green-600" />
                </div>
                <div>
                  <div className="text-sm text-gray-500">Total Projects</div>
                  <div className="text-3xl font-bold text-gray-800">{totalCount}</div>
                </div>
              </div>
            </div>

            <div className="bg-white rounded-xl p-6 shadow-sm border border-gray-100">
              <div className="flex items-center gap-3 mb-3">
                <div className="w-12 h-12 bg-red-100 rounded-full flex items-center justify-center">
                  <AlertTriangle className="w-6 h-6 text-red-600" />
                </div>
                <div>
                  <div className="text-sm text-gray-500">High Risk Projects</div>
                  <div className="text-3xl font-bold text-gray-800">{highRiskCount}</div>
                </div>
              </div>
            </div>

            <div className="bg-white rounded-xl p-6 shadow-sm border border-gray-100">
              <div className="flex items-center gap-3 mb-3">
                <div className="w-12 h-12 bg-orange-100 rounded-full flex items-center justify-center">
                  <Layers className="w-6 h-6 text-orange-600" />
                </div>
                <div>
                  <div className="text-sm text-gray-500">Pending Scholarships</div>
                  <div className="text-3xl font-bold text-gray-800">{scholarshipPendingCount}</div>
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
                    placeholder="Search projects..."
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
                <option value="All">All Status</option>
                <option value="Active">Active</option>
                <option value="High Risk">High Risk</option>
                <option value="Unpaid">Unpaid</option>
              </select>
              
              <select
                value={riskFilter}
                onChange={(e) => setRiskFilter(e.target.value)}
                className="px-4 py-2 border border-gray-200 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent"
              >
                <option value="All">All Risk Levels</option>
                <option value="High Risk">High Risk</option>
                <option value="Low Risk">Low Risk</option>
              </select>
            </div>
          </div>

          {/* Projects Grid */}
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 mb-6">
            {currentProjects.map((project) => (
              <SchoolProjectCard
                key={project.projectId || project.project_id}
                project={project}
                onViewDetails={handleProjectClick}
                onRecordExpense={(project) => console.log('Record expense for:', project.projectTitle || project.project_name)}
                onPostUpdate={(project) => console.log('Post update for:', project.projectTitle || project.project_name)}
              />
            ))}
          </div>

          {/* Pagination */}
          {totalPages > 1 && (
            <div className="flex justify-center items-center gap-2">
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
  );
}