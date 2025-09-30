import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { Search, Plus, AlertTriangle, Users as UsersIcon, Layers } from 'lucide-react';


const emptyData = {
  school: null,
  projects: [],
  totalCount: 0,
  highRiskCount: 0,
  scholarshipPendingCount: 0
};

export default function SchoolProjects() {
  const { schoolId } = useParams();
  const [activeNav, setActiveNav] = useState('Projects');
  const [loading, setLoading] = useState(true);
  const [data, setData] = useState(emptyData);
  const [apiError, setApiError] = useState(false);
  
  // Filters
  const [searchQuery, setSearchQuery] = useState('');
  const [statusFilter, setStatusFilter] = useState('All');
  const [genderFilter, setGenderFilter] = useState('All');
  const [riskFilter, setRiskFilter] = useState('All');
  
  // Pagination
  const [currentPage, setCurrentPage] = useState(1);
  const projectsPerPage = 8;
  
  const API_BASE_URL = 'http://localhost:8081/api';

  useEffect(() => {
    if (schoolId) {
      fetchProjectsData();
    }
  }, [schoolId]);

  const fetchProjectsData = async () => {
    setLoading(true);
    try {
      console.log('Fetching projects for school ID:', schoolId);
      
      const [schoolRes, projectsRes] = await Promise.all([
        fetch(`${API_BASE_URL}/schools/${schoolId}`).catch(() => null),
        fetch(`${API_BASE_URL}/school-projects`).catch(() => null),
      ]);

      const newData = { ...emptyData };
      
      if (schoolRes && schoolRes.ok) {
        const schoolData = await schoolRes.json();
        newData.school = {
          ...schoolData,
          total_students: schoolData.totalStudents || schoolData.total_students || 0,
          active_projects: schoolData.activeProjects || schoolData.active_projects || 0,
          total_received: schoolData.totalReceived || schoolData.total_received || 0
        };
      }
      
      if (projectsRes && projectsRes.ok) {
        const projectsData = await projectsRes.json();
        const projects = Array.isArray(projectsData) ? projectsData : projectsData.data || [];
        
        const schoolProjects = projects
          .filter(project => (project.schoolId || project.school_id) == schoolId)
          .map(project => ({
            project_id: project.projectId || project.project_id,
            project_name: project.projectName || project.project_name,
            project_type: project.projectType || project.project_type,
            scholarship_amount: project.scholarshipAmount || project.scholarship_amount || 0,
            total_amount: project.totalAmount || project.total_amount || 0,
            risk_status: project.riskStatus || project.risk_status || 'Low Risk',
            completion_rate: project.completionRate || project.completion_rate || 0,
            performance_score: project.performanceScore || project.performance_score || 0,
            category: project.category || 'General',
            status: project.status || 'active',
            donor_username: project.donorUsername || project.donor_username || '@anisha3208'
          }));
        
        newData.projects = schoolProjects;
        newData.totalCount = schoolProjects.length;
        newData.highRiskCount = schoolProjects.filter(p => 
          p.risk_status && (p.risk_status.toLowerCase().includes('high') || p.risk_status.toLowerCase().includes('at risk'))
        ).length;
        newData.scholarshipPendingCount = schoolProjects.filter(p => 
          p.status && p.status.toLowerCase() === 'unpaid'
        ).length;
      }
      
      setData(newData);
      setApiError(false);
    } catch (error) {
      console.error('Error fetching projects data:', error);
      setApiError(true);
      setData(emptyData);
    } finally {
      setLoading(false);
    }
  };

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
  const filteredProjects = data.projects.filter(project => {
    const matchesSearch = project.project_name.toLowerCase().includes(searchQuery.toLowerCase());
    const matchesStatus = statusFilter === 'All' || project.status?.toLowerCase() === statusFilter.toLowerCase();
    const matchesCategory = genderFilter === 'All' || project.category?.toLowerCase() === genderFilter.toLowerCase();
    const matchesRisk = riskFilter === 'All' || 
      (riskFilter === 'High Risk' && (project.risk_status?.toLowerCase().includes('high') || project.risk_status?.toLowerCase().includes('at risk'))) ||
      (riskFilter === 'Low Risk' && project.risk_status?.toLowerCase().includes('low'));
    
    return matchesSearch && matchesStatus && matchesCategory && matchesRisk;
  });

  // Pagination
  const totalPages = Math.ceil(filteredProjects.length / projectsPerPage);
  const indexOfLastProject = currentPage * projectsPerPage;
  const indexOfFirstProject = indexOfLastProject - projectsPerPage;
  const currentProjects = filteredProjects.slice(indexOfFirstProject, indexOfLastProject);

  const handleProjectClick = (projectId) => {
    console.log('Viewing project details for ID:', projectId);
    // Navigate to project detail page
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

      <div className="flex-1 overflow-auto">
        {apiError && (
          <div className="m-6 p-4 bg-yellow-50 border border-yellow-200 rounded-lg text-sm text-yellow-800">
            ⚠ Using demo data. Connect to backend at <code className="bg-yellow-100 px-2 py-1 rounded">{API_BASE_URL}</code>
          </div>
        )}

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
                  <div className="text-3xl font-bold text-gray-800">{data.totalCount}</div>
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
                  <div className="text-3xl font-bold text-gray-800">{data.highRiskCount}</div>
                </div>
              </div>
            </div>

            <div className="bg-white rounded-xl p-6 shadow-sm border border-gray-100">
              <div className="flex items-center gap-3 mb-3">
                <div className="w-12 h-12 bg-blue-100 rounded-full flex items-center justify-center">
                  <Layers className="w-6 h-6 text-blue-600" />
                </div>
                <div>
                  <div className="text-sm text-gray-500">Scholarship Pending</div>
                  <div className="text-3xl font-bold text-gray-800">{data.scholarshipPendingCount}</div>
                </div>
              </div>
            </div>
          </div>

          {/* Find Projects Section */}
          <div className="bg-green-50 rounded-xl p-6 mb-6">
            <div className="flex items-center justify-between mb-4">
              <h2 className="text-xl font-bold text-gray-800">Find Projects</h2>
              <button className="bg-green-600 text-white px-4 py-2 rounded-lg flex items-center gap-2 hover:bg-green-700 transition-colors">
                <Plus className="w-4 h-4" />
                Add Project
              </button>
            </div>

            {/* Filters */}
            <div className="grid grid-cols-4 gap-4">
              <div className="relative">
                <Search className="w-4 h-4 absolute left-3 top-3 text-gray-400" />
                <input
                  type="text"
                  placeholder="Search"
                  value={searchQuery}
                  onChange={(e) => setSearchQuery(e.target.value)}
                  className="w-full pl-10 pr-3 py-2 bg-white rounded-lg text-sm border border-gray-200 focus:outline-none focus:ring-2 focus:ring-green-300"
                />
              </div>

              <select
                value={statusFilter}
                onChange={(e) => setStatusFilter(e.target.value)}
                className="px-3 py-2 bg-white rounded-lg text-sm border border-gray-200 focus:outline-none focus:ring-2 focus:ring-green-300"
              >
                <option value="All">• All</option>
                <option value="active">Active</option>
                <option value="unpaid">Unpaid</option>
              </select>

              <select
                value={genderFilter}
                onChange={(e) => setGenderFilter(e.target.value)}
                className="px-3 py-2 bg-white rounded-lg text-sm border border-gray-200 focus:outline-none focus:ring-2 focus:ring-green-300"
              >
                <option value="All">All Categories</option>
                <option value="infrastructure">Infrastructure</option>
                <option value="education">Education</option>
                <option value="health">Health</option>
                <option value="general">General</option>
              </select>

              <select
                value={riskFilter}
                onChange={(e) => setRiskFilter(e.target.value)}
                className="px-3 py-2 bg-white rounded-lg text-sm border border-gray-200 focus:outline-none focus:ring-2 focus:ring-green-300"
              >
                <option value="All">All</option>
                <option value="High Risk">High Risk</option>
                <option value="Low Risk">Low Risk</option>
              </select>
            </div>
          </div>

          {/* Projects Grid */}
          <div className="grid grid-cols-4 gap-4 mb-6">
            {currentProjects.length > 0 ? currentProjects.map((project) => {
              const badge = getStatusBadge(project.risk_status);
              return (
                <div
                  key={project.project_id}
                  className="bg-white rounded-xl overflow-hidden shadow-sm border border-gray-100 cursor-pointer hover:shadow-md transition-shadow"
                  onClick={() => handleProjectClick(project.project_id)}
                >
                  <div className="relative h-40 bg-gradient-to-br from-green-400 to-green-600">
                    <span className={`absolute top-3 right-3 ${badge.bg} ${badge.text} px-2 py-1 rounded-full text-xs font-medium`}>
                      • {badge.label}
                    </span>
                  </div>
                  <div className="p-4">
                    <div className="text-xs text-green-600 mb-1">Funded by {project.donor_username}</div>
                    <div className="text-sm font-semibold text-gray-800 mb-2">
                      {project.project_name}, {formatProjectType(project.project_type)}
                    </div>
                    <div className="text-xs text-gray-500 mb-4">
                      Fund Received: {formatCurrency(project.scholarship_amount)} / {formatCurrency(project.total_amount)}
                    </div>
                    <button
                      className="w-full py-2 text-sm text-green-600 bg-green-50 rounded-lg hover:bg-green-100 transition-colors"
                      onClick={(e) => {
                        e.stopPropagation();
                        console.log('Update report for project:', project.project_id);
                      }}
                    >
                      Update Report
                    </button>
                  </div>
                </div>
              );
            }) : (
              <div className="col-span-4 text-center py-12 text-gray-500">
                <div className="flex flex-col items-center gap-3">
                  <span className="text-4xl">🏗️</span>
                  <span className="text-lg font-medium">No projects found</span>
                  <span className="text-sm">Try adjusting your filters</span>
                </div>
              </div>
            )}
          </div>

          {/* Pagination */}
          {totalPages > 1 && (
            <div className="flex items-center justify-between">
              <button
                onClick={() => setCurrentPage(p => Math.max(1, p - 1))}
                disabled={currentPage === 1}
                className="px-4 py-2 text-sm text-gray-600 bg-white rounded-lg border border-gray-200 hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
              >
                ← Previous
              </button>

              <div className="flex gap-2">
                {[...Array(Math.min(10, totalPages))].map((_, i) => {
                  const pageNum = i + 1;
                  return (
                    <button
                      key={pageNum}
                      onClick={() => setCurrentPage(pageNum)}
                      className={`w-8 h-8 rounded-lg text-sm ${
                        currentPage === pageNum
                          ? 'bg-green-600 text-white'
                          : 'bg-white text-gray-600 border border-gray-200 hover:bg-gray-50'
                      }`}
                    >
                      {pageNum}
                    </button>
                  );
                })}
                {totalPages > 10 && <span className="px-2 py-1">...</span>}
                {totalPages > 10 && (
                  <button
                    onClick={() => setCurrentPage(totalPages)}
                    className={`w-8 h-8 rounded-lg text-sm ${
                      currentPage === totalPages
                        ? 'bg-green-600 text-white'
                        : 'bg-white text-gray-600 border border-gray-200 hover:bg-gray-50'
                    }`}
                  >
                    {totalPages}
                  </button>
                )}
              </div>

              <button
                onClick={() => setCurrentPage(p => Math.min(totalPages, p + 1))}
                disabled={currentPage === totalPages}
                className="px-4 py-2 text-sm text-gray-600 bg-white rounded-lg border border-gray-200 hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
              >
                Next →
              </button>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}