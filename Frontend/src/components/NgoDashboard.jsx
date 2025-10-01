import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { ChevronRight, MoreVertical, Briefcase, School, Plus, Trash2, Edit, Eye, Download } from 'lucide-react';
import Sidebar from './DashSidebar';
import { API_BASE_URL } from '../config/api';

const emptyData = {
  ngo: null,
  ngoProjects: [],
  assignedSchools: [],
  projectUpdates: [],
  budgetSummary: { totalBudget: 0, utilizedBudget: 0, remainingBudget: 0 }
};

export default function NgoDashboard() {
  const { ngoId } = useParams();
  const [activeNav, setActiveNav] = useState('Dashboard');
  const [loading, setLoading] = useState(true);
  const [data, setData] = useState(emptyData);
  const [apiError, setApiError] = useState(false);
  const [showAddProject, setShowAddProject] = useState(false);
  const [selectedProject, setSelectedProject] = useState(null);

  useEffect(() => {
    if (ngoId) {
      fetchNgoData();
    }
  }, [ngoId]);

  const fetchNgoData = async () => {
    setLoading(true);
    try {
      const [ngoRes, projectsRes, schoolsRes, updatesRes] = await Promise.all([
        fetch(`${API_BASE_URL}/ngos/${ngoId}`).catch(() => null),
        fetch(`${API_BASE_URL}/ngo-projects`).catch(() => null),
        fetch(`${API_BASE_URL}/ngo-project-schools`).catch(() => null),
        fetch(`${API_BASE_URL}/project-updates`).catch(() => null),
      ]);

      const newData = { ...emptyData };
      
      if (ngoRes && ngoRes.ok) {
        newData.ngo = await ngoRes.json();
      }
      
      if (projectsRes && projectsRes.ok) {
        const projectsData = await projectsRes.json();
        const allProjects = Array.isArray(projectsData) ? projectsData : projectsData.data || [];
        newData.ngoProjects = allProjects.filter(p => (p.ngoId || p.ngo_id) == ngoId);
      }
      
      if (schoolsRes && schoolsRes.ok) {
        const schoolsData = await schoolsRes.json();
        const allSchools = Array.isArray(schoolsData) ? schoolsData : schoolsData.data || [];
        const projectIds = newData.ngoProjects.map(p => p.ngoProjectId || p.ngo_project_id);
        newData.assignedSchools = allSchools.filter(s => 
          projectIds.includes(s.ngoProjectId || s.ngo_project_id)
        );
      }
      
      if (updatesRes && updatesRes.ok) {
        const updatesData = await updatesRes.json();
        newData.projectUpdates = Array.isArray(updatesData) ? updatesData : updatesData.data || [];
      }
      
      const totalBudget = newData.ngoProjects.reduce((sum, p) => sum + (p.budget || 0), 0);
      const utilizedBudget = newData.assignedSchools.reduce((sum, s) => sum + (s.utilizedBudget || s.utilized_budget || 0), 0);
      newData.budgetSummary = {
        totalBudget,
        utilizedBudget,
        remainingBudget: totalBudget - utilizedBudget
      };
      
      setData(newData);
      setApiError(false);
    } catch (error) {
      console.error('Error fetching NGO data:', error);
      setApiError(true);
    } finally {
      setLoading(false);
    }
  };

  const formatCurrency = (amount) => {
    return `৳${Math.abs(amount || 0).toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`;
  };

  const getStatusColor = (status) => {
    const statusMap = {
      'planned': 'bg-blue-100 text-blue-700',
      'active': 'bg-green-100 text-green-700',
      'in_progress': 'bg-orange-100 text-orange-700',
      'completed': 'bg-green-600 text-white',
      'paused': 'bg-gray-100 text-gray-700',
      'cancelled': 'bg-red-100 text-red-700',
    };
    return statusMap[status?.toLowerCase()] || 'bg-gray-100 text-gray-700';
  };

  const handleDeleteProject = async (projectId) => {
    if (window.confirm('Are you sure you want to delete this project? This action cannot be undone.')) {
      try {
        const response = await fetch(`${API_BASE_URL}/ngo-projects/${projectId}`, {
          method: 'DELETE',
        });
        if (response.ok) {
          fetchNgoData();
          alert('Project deleted successfully');
        } else {
          alert('Failed to delete project');
        }
      } catch (error) {
        console.error('Error deleting project:', error);
        alert('Error deleting project');
      }
    }
  };

  const handleAddProject = () => {
    setShowAddProject(true);
  };

  const handleViewProject = (project) => {
    setSelectedProject(project);
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

  const ngoData = {
    schoolName: data.ngo?.ngoName || data.ngo?.ngo_name || `NGO ${ngoId}`,
    total_students: data.ngoProjects.length,
    active_projects: data.assignedSchools.length,
  };

  return (
    <div className="flex h-screen bg-gray-50">
      <Sidebar activeNav={activeNav} setActiveNav={setActiveNav} schoolData={ngoData} />

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
              {data.ngo?.ngoName || data.ngo?.ngo_name || `NGO ${ngoId}`}
              {data.ngo?.verificationStatus === 'verified' && <span className="text-green-500">✓</span>}
            </div>
            <div className="text-sm text-gray-600 mb-2">
              Managing impactful projects across multiple schools to reduce dropout rates
            </div>
            <div className="text-xs text-gray-500 flex items-center gap-2">
              <span>🏛️</span>
              Reg. No: {data.ngo?.registrationNumber || data.ngo?.registration_number || 'N/A'}
            </div>
          </div>
          <div className="w-96 h-48 rounded-xl overflow-hidden shadow-lg">
            <div className="w-full h-full bg-gradient-to-br from-blue-600 to-blue-800 flex items-center justify-center text-white">
              <Briefcase className="w-24 h-24 opacity-20" />
            </div>
          </div>
        </div>

        {/* Overview Cards */}
        <div className="px-6 mb-8">
          <div className="mb-4">
            <h2 className="text-2xl font-bold text-gray-800">Overview</h2>
            <p className="text-sm text-gray-500">Track your projects and budget utilization</p>
          </div>
          
          <div className="grid grid-cols-3 gap-6">
            <div className="bg-white rounded-xl p-6 shadow-sm border border-gray-100">
              <div className="flex items-start justify-between mb-4">
                <div className="w-10 h-10 bg-blue-100 rounded-lg flex items-center justify-center">
                  <Briefcase className="w-5 h-5 text-blue-600" />
                </div>
                <MoreVertical className="w-4 h-4 text-gray-400" />
              </div>
              <div className="text-sm text-gray-500 mb-1">Active Projects</div>
              <div className="text-3xl font-bold text-gray-800 mb-2">{data.ngoProjects.length}</div>
              <div className="text-xs text-green-600">
                {data.ngoProjects.filter(p => (p.status || '').toLowerCase() === 'active').length} in progress
              </div>
            </div>

            <div className="bg-white rounded-xl p-6 shadow-sm border border-gray-100">
              <div className="flex items-start justify-between mb-4">
                <div className="w-10 h-10 bg-green-100 rounded-lg flex items-center justify-center">
                  <School className="w-5 h-5 text-green-600" />
                </div>
                <MoreVertical className="w-4 h-4 text-gray-400" />
              </div>
              <div className="text-sm text-gray-500 mb-1">Schools Reached</div>
              <div className="text-3xl font-bold text-gray-800 mb-2">{data.assignedSchools.length}</div>
              <div className="text-xs text-green-600">Across all projects</div>
            </div>

            <div className="bg-white rounded-xl p-6 shadow-sm border border-gray-100">
              <div className="flex items-start justify-between mb-4">
                <div className="w-10 h-10 bg-orange-100 rounded-lg flex items-center justify-center">
                  <span className="text-orange-600 text-lg">💰</span>
                </div>
                <MoreVertical className="w-4 h-4 text-gray-400" />
              </div>
              <div className="text-sm text-gray-500 mb-1">Budget Utilization</div>
              <div className="text-3xl font-bold text-gray-800 mb-2">
                {data.budgetSummary.totalBudget > 0 
                  ? Math.round((data.budgetSummary.utilizedBudget / data.budgetSummary.totalBudget) * 100)
                  : 0}%
              </div>
              <div className="text-xs text-gray-600">
                {formatCurrency(data.budgetSummary.utilizedBudget)} of {formatCurrency(data.budgetSummary.totalBudget)}
              </div>
            </div>
          </div>
        </div>

        {/* NGO Projects Management */}
        <div className="px-6 mb-8">
          <div className="flex items-center justify-between mb-4">
            <div>
              <h2 className="text-xl font-bold text-gray-800 flex items-center gap-2">
                <span className="text-blue-600">📋</span>
                Your Projects
              </h2>
              <p className="text-sm text-gray-500">Manage and monitor your NGO projects</p>
            </div>
            <button 
              onClick={handleAddProject}
              className="secondary !border-0 hover:!bg-gray-50 hover:!text-[#0E792E] text-sm font-medium flex items-center gap-2 bg-green-600 text-white px-4 py-2 rounded-lg hover:bg-green-700"
            >
              <Plus className="w-4 h-4" />
              Add Project
            </button>
          </div>

          <div className="grid grid-cols-3 gap-4">
            {data.ngoProjects.length > 0 ? data.ngoProjects.map((project) => (
              <div key={project.ngoProjectId || project.ngo_project_id} className="bg-white rounded-xl overflow-hidden shadow-sm border border-gray-100">
                <div className="relative h-40 bg-gradient-to-br from-blue-700 to-blue-900">
                  <span className={`absolute top-3 right-3 px-2 py-1 rounded-full text-xs font-medium ${getStatusColor(project.status)}`}>
                    • {(project.status || 'planned').toUpperCase()}
                  </span>
                  <span className="absolute top-3 left-3 bg-black bg-opacity-50 text-white px-2 py-1 rounded text-xs">
                    ID: {project.ngoProjectId || project.ngo_project_id}
                  </span>
                </div>
                <div className="p-4">
                  <div className="text-sm font-semibold text-gray-800 mb-2">
                    {project.projectName || project.project_name || 'Untitled Project'}
                  </div>
                  <div className="text-xs text-gray-600 mb-3 line-clamp-2">
                    {project.projectDescription || project.project_description || 'No description'}
                  </div>
                  <div className="text-xs text-gray-500 mb-3">
                    Budget: {formatCurrency(project.budget || 0)}
                  </div>
                  <div className="text-xs text-gray-500 mb-4">
                    Duration: {project.startDate || project.start_date || 'N/A'} - {project.endDate || project.end_date || 'Ongoing'}
                  </div>
                  <div className="flex gap-2">
                    <button 
                      onClick={() => handleViewProject(project)}
                      className="flex-1 secondary !border-0 hover:!bg-gray-50 hover:!text-[#0E792E] py-2 text-sm rounded-lg hover:bg-blue-50 transition-colors flex items-center justify-center gap-1"
                    >
                      <Eye className="w-3 h-3" />
                      View
                    </button>
                    <button 
                      onClick={() => handleDeleteProject(project.ngoProjectId || project.ngo_project_id)}
                      className="px-3 py-2 text-sm rounded-lg bg-red-50 text-red-600 hover:bg-red-100 transition-colors"
                    >
                      <Trash2 className="w-3 h-3" />
                    </button>
                  </div>
                </div>
              </div>
            )) : (
              <div className="col-span-3 text-center py-12 text-gray-500 bg-white rounded-xl">
                <div className="flex flex-col items-center gap-3">
                  <span className="text-4xl">📋</span>
                  <span className="text-lg font-medium">No projects created yet</span>
                  <span className="text-sm">Click "Add Project" to create your first project</span>
                  <button 
                    onClick={handleAddProject}
                    className="mt-4 secondary !border-0 hover:!bg-gray-50 hover:!text-[#0E792E] bg-green-600 text-white px-6 py-2 rounded-lg hover:bg-green-700 transition-colors flex items-center gap-2"
                  >
                    <Plus className="w-4 h-4" />
                    Create Project
                  </button>
                </div>
              </div>
            )}
          </div>
        </div>

        {/* Assigned Schools & Budget Breakdown */}
        <div className="px-6 pb-8 grid grid-cols-2 gap-6">
          {/* Assigned Schools */}
          <div className="bg-white rounded-xl p-6 shadow-sm border border-gray-100">
            <div className="flex items-center justify-between mb-4">
              <h2 className="text-lg font-bold text-gray-800">Assigned Schools</h2>
              <button className="secondary !border-0 hover:!bg-gray-50 hover:!text-[#0E792E] px-3 py-1.5 text-sm rounded-lg">
                View All
              </button>
            </div>

            <div className="space-y-3">
              {data.assignedSchools.length > 0 ? data.assignedSchools.slice(0, 5).map((school, idx) => (
                <div key={idx} className="flex items-center justify-between p-3 bg-gray-50 rounded-lg hover:bg-gray-100 transition-colors">
                  <div className="flex items-center gap-3">
                    <School className="w-8 h-8 text-green-600" />
                    <div>
                      <div className="text-sm font-medium text-gray-800">
                        {school.projectNameForSchool || school.project_name_for_school || 'School Project'}
                      </div>
                      <div className="text-xs text-gray-500">
                        Budget: {formatCurrency(school.allocatedBudget || school.allocated_budget || 0)}
                      </div>
                    </div>
                  </div>
                  <span className={`text-xs px-2 py-1 rounded-full ${getStatusColor(school.participationStatus || school.participation_status)}`}>
                    {(school.participationStatus || school.participation_status || 'selected').toUpperCase()}
                  </span>
                </div>
              )) : (
                <div className="text-center py-8 text-gray-500">
                  <span className="text-2xl">🏫</span>
                  <p className="text-sm mt-2">No schools assigned yet</p>
                </div>
              )}
            </div>
          </div>

          {/* Budget Breakdown */}
          <div className="bg-white rounded-xl p-6 shadow-sm border border-gray-100">
            <div className="flex items-center justify-between mb-4">
              <h2 className="text-lg font-bold text-gray-800">Budget Breakdown</h2>
              <button className="secondary !border-0 hover:!bg-gray-50 hover:!text-[#0E792E] px-3 py-1.5 text-sm rounded-lg flex items-center gap-1">
                <Download className="w-4 h-4" />
                Report
              </button>
            </div>

            <div className="space-y-4">
              <div>
                <div className="flex justify-between text-sm mb-2">
                  <span className="text-gray-600">Total Budget</span>
                  <span className="font-semibold text-gray-800">{formatCurrency(data.budgetSummary.totalBudget)}</span>
                </div>
                <div className="w-full bg-gray-200 rounded-full h-2">
                  <div className="bg-blue-600 h-2 rounded-full" style={{ width: '100%' }}></div>
                </div>
              </div>

              <div>
                <div className="flex justify-between text-sm mb-2">
                  <span className="text-gray-600">Utilized Budget</span>
                  <span className="font-semibold text-green-600">{formatCurrency(data.budgetSummary.utilizedBudget)}</span>
                </div>
                <div className="w-full bg-gray-200 rounded-full h-2">
                  <div 
                    className="bg-green-600 h-2 rounded-full" 
                    style={{ 
                      width: `${data.budgetSummary.totalBudget > 0 
                        ? (data.budgetSummary.utilizedBudget / data.budgetSummary.totalBudget) * 100 
                        : 0}%` 
                    }}
                  ></div>
                </div>
              </div>

              <div>
                <div className="flex justify-between text-sm mb-2">
                  <span className="text-gray-600">Remaining Budget</span>
                  <span className="font-semibold text-orange-600">{formatCurrency(data.budgetSummary.remainingBudget)}</span>
                </div>
                <div className="w-full bg-gray-200 rounded-full h-2">
                  <div 
                    className="bg-orange-600 h-2 rounded-full" 
                    style={{ 
                      width: `${data.budgetSummary.totalBudget > 0 
                        ? (data.budgetSummary.remainingBudget / data.budgetSummary.totalBudget) * 100 
                        : 0}%` 
                    }}
                  ></div>
                </div>
              </div>

              <div className="pt-4 border-t border-gray-200">
                <div className="text-xs text-gray-500 text-center">
                  {data.ngoProjects.filter(p => (p.status || '').toLowerCase() === 'active').length} active projects • 
                  {data.assignedSchools.length} schools reached
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
