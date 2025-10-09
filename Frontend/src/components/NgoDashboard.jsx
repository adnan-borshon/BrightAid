import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { ChevronRight, MoreVertical, Briefcase, School, Plus, Trash2, Edit, Eye, Download, Heart, Users, TrendingUp, Trophy, Award } from 'lucide-react';
import NgoDashSidebar from './NgoDashSidebar';
import ProjectCreateModal from './Modal/ProjectCreateModal';
import DonorGamificationCard from './DonorGamificationCard';
import { useNgo } from '../context/NgoContext';

const mockStudentsForSponsorship = [
  {
    id: "1",
    name: "Student 1",
    class: "Class 2",
    status: "Active",
    image: "https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=300&h=300&fit=crop",
  },
  {
    id: "2",
    name: "Student 2",
    class: "Class 3",
    status: "Active",
    image: "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=300&h=300&fit=crop",
  },
  {
    id: "3",
    name: "Student 3",
    class: "Class 4",
    status: "Active",
    image: "https://images.unsplash.com/photo-1524504388940-b1c1722653e1?w=300&h=300&fit=crop",
  },
  {
    id: "4",
    name: "Student 4",
    class: "Class 5",
    status: "Active",
    image: "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=300&h=300&fit=crop",
  },
];

const emptyData = {
  ngo: null,
  schoolProjects: [],
  assignedSchools: [],
  projectUpdates: [],
  budgetSummary: { totalBudget: 0, utilizedBudget: 0, remainingBudget: 0 }
};

export default function NgoDashboard() {
  const { ngoId } = useParams();
  const navigate = useNavigate();
  const { 
    ngoData, 
    projectsData, 
    donationsData, 
    gamificationData, 
    loading, 
    error, 
    fetchNgoData, 
    refreshData 
  } = useNgo();
  
  const [data, setData] = useState(emptyData);
  const [apiError, setApiError] = useState(false);
  const [showAddProject, setShowAddProject] = useState(false);
  const [selectedProject, setSelectedProject] = useState(null);
  const [ngoStats, setNgoStats] = useState({
    totalDonated: 0,
    studentsHelped: 0,
    schoolProjectsCount: 0,
    schoolsReached: 0
  });
  const [donationDialogOpen, setDonationDialogOpen] = useState(false);
  const [donationDialogData, setDonationDialogData] = useState(null);

  useEffect(() => {
    if (ngoId) {
      console.log('NgoDashboard: Initializing for NGO ID:', ngoId);
      fetchNgoData(ngoId);
      // Delay additional data fetch to ensure context is loaded first
      setTimeout(() => fetchAdditionalData(), 500);
    }
  }, [ngoId]);

  // Refresh data when context data changes
  useEffect(() => {
    if (ngoData || projectsData || donationsData) {
      updateLocalData();
    }
  }, [ngoData, projectsData, donationsData, gamificationData]);

  // Refresh stats when donations or projects change
  useEffect(() => {
    if (ngoId && (donationsData || projectsData)) {
      fetchAdditionalData();
    }
  }, [donationsData, projectsData]);

  // Fetch additional data not covered by context
  const fetchAdditionalData = async () => {
    try {
      const API_BASE_URL = 'http://localhost:8081/api';
      const [schoolProjectsRes, statsRes] = await Promise.all([
        fetch(`${API_BASE_URL}/school-projects`).catch(() => null),
        fetch(`${API_BASE_URL}/ngos/${ngoId}/stats`).catch(() => null),
      ]);

      const newData = { ...data };
      
      // Fetch available school projects for donation
      if (schoolProjectsRes && schoolProjectsRes.ok) {
        const projectsData = await schoolProjectsRes.json();
        const allProjects = Array.isArray(projectsData) ? projectsData : [];
        newData.schoolProjects = allProjects.slice(0, 6); // Limit to 6 for dashboard
      }
      
      // Fetch stats from backend using native queries
      if (statsRes && statsRes.ok) {
        const stats = await statsRes.json();
        console.log('Fetched NGO stats:', stats); // Debug log
        setNgoStats({
          totalDonated: stats.totalDonated || 0,
          studentsHelped: stats.studentsHelped || 0,
          schoolProjectsCount: stats.schoolProjectsCount || 0,
          schoolsReached: stats.schoolsReached || 0
        });
      } else {
        console.warn('Failed to fetch stats, response status:', statsRes?.status);
        // Use context data as fallback
        const contextDonationsTotal = (donationsData || []).reduce((sum, d) => sum + (d.amount || 0), 0);
        const fallbackStats = {
          totalDonated: contextDonationsTotal,
          studentsHelped: (donationsData || []).length, // Approximate
          schoolProjectsCount: (projectsData || []).length,
          schoolsReached: Math.min((projectsData || []).length, 5) // Approximate
        };
        console.log('Using fallback stats:', fallbackStats);
        setNgoStats(fallbackStats);
      }
      
      setData(newData);
      setApiError(false);
    } catch (error) {
      console.error('Error fetching additional data:', error);
      // Use context data as fallback
      const contextDonationsTotal = (donationsData || []).reduce((sum, d) => sum + (d.amount || 0), 0);
      const fallbackStats = {
        totalDonated: contextDonationsTotal,
        studentsHelped: (donationsData || []).length,
        schoolProjectsCount: (projectsData || []).length,
        schoolsReached: Math.min((projectsData || []).length, 5)
      };
      console.log('Error fallback stats:', fallbackStats);
      setNgoStats(fallbackStats);
      setApiError(true);
    }
  };

  // Update local data when context data changes
  const updateLocalData = () => {
    const newData = { ...emptyData };
    
    // Use context data
    newData.ngo = ngoData;
    newData.ngoProjects = projectsData || [];
    
    // Calculate budget from NGO's own projects
    const totalBudget = (newData.ngoProjects || []).reduce((sum, p) => sum + (p.budget || 0), 0);
    const utilizedBudget = (newData.ngoProjects || []).reduce((sum, p) => sum + (p.raisedAmount || 0), 0);
    newData.budgetSummary = {
      totalBudget,
      utilizedBudget,
      remainingBudget: totalBudget - utilizedBudget
    };
    
    // Set assigned schools from NGO projects
    newData.assignedSchools = (newData.ngoProjects || []).map(project => ({
      projectNameForSchool: project.projectName,
      allocatedBudget: project.budget,
      participationStatus: project.status || 'active'
    }));
    
    // Keep existing school projects data
    newData.schoolProjects = data.schoolProjects || [];
    
    setData(newData);
  };

  const formatCurrency = (amount) => {
    return `Tk ${Math.abs(amount || 0).toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`;
  };

  const handleProjectCreation = async (submitData) => {
    try {
      const jsonData = {
        ngoId: parseInt(ngoId),
        projectName: submitData.projectTitle,
        projectDescription: submitData.projectDescription,
        projectTypeId: submitData.projectTypeId,
        budget: submitData.requiredAmount || 0,
        status: 'ACTIVE'
      };
      
      console.log('Creating project from dashboard:', jsonData);
      
      const response = await fetch('http://localhost:8081/api/ngo-projects', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(jsonData)
      });
      
      if (response.ok) {
        setShowAddProject(false);
        // Refresh context data and additional data
        refreshData(ngoId);
        fetchAdditionalData();
        console.log('Project created successfully from dashboard');
      } else {
        const errorData = await response.text();
        console.error('Project creation failed:', errorData);
        alert('Failed to create project: ' + errorData);
      }
    } catch (error) {
      console.error('Error creating project:', error);
      alert('Error creating project: ' + error.message);
    }
  };

  const getStatusColor = (status) => {
    const statusMap = {
      'planned': 'bg-green-100 text-green-700',
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
        const response = await fetch(`http://localhost:8081/api/ngo-projects/${projectId}`, {
          method: 'DELETE',
        });
        if (response.ok) {
          // Refresh context data and additional data
          refreshData(ngoId);
          fetchAdditionalData();
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

  const ngoDisplayData = {
    ngoName: ngoData?.ngoName || data.ngo?.ngoName || `NGO ${ngoId}`,
    registrationNumber: ngoData?.registrationNumber || data.ngo?.registrationNumber || 'N/A',
    verificationStatus: ngoData?.verificationStatus || data.ngo?.verificationStatus || 'PENDING',
    totalStudents: ngoStats.studentsHelped,
    activeProjects: ngoStats.schoolsReached,
  };
  
  const gamificationInfo = {
    currentLevel: gamificationData?.totalPoints >= 1000 ? "Champion" : 
                 gamificationData?.totalPoints >= 500 ? "Expert" : 
                 gamificationData?.totalPoints >= 200 ? "Achiever" : 
                 gamificationData?.totalPoints >= 100 ? "Starter" : "Beginner",
    totalPoints: gamificationData?.totalPoints || 0,
    badgesEarned: gamificationData?.badgesEarned ? 
      (typeof gamificationData.badgesEarned === 'string' ? 
        JSON.parse(gamificationData.badgesEarned.replace(/\\"/g, '"')) : 
        gamificationData.badgesEarned) : ["New NGO"],
    rankingPosition: 1
  };

  return (
    <div className="flex h-screen bg-gray-50">
      <ProjectCreateModal
        isOpen={showAddProject}
        onClose={() => setShowAddProject(false)}
        onSubmit={handleProjectCreation}
      />
      <NgoDashSidebar />

      <div className="flex-1 overflow-auto">
        {apiError && (
          <div className="m-6 p-4 bg-yellow-50 border border-yellow-200 rounded-lg text-sm text-yellow-800">
            ⚠ Using demo data. Connect to backend at <code className="bg-yellow-100 px-2 py-1 rounded">http://localhost:8081/api</code>
          </div>
        )}

        {/* Debug Panel - Remove in production */}
        <div className="m-6 p-4 bg-blue-50 border border-blue-200 rounded-lg text-sm">
          <div className="flex items-center justify-between mb-2">
            <span className="font-medium text-blue-800">Debug Info (NGO {ngoId})</span>
            <button 
              onClick={() => {
                console.log('Current stats:', ngoStats);
                console.log('Context data:', { ngoData, projectsData, donationsData, gamificationData });
                fetchAdditionalData();
              }}
              className="px-2 py-1 bg-blue-600 text-white text-xs rounded hover:bg-blue-700"
            >
              Refresh Stats
            </button>
          </div>
          <div className="text-blue-700 text-xs">
            Stats: Donated={ngoStats.totalDonated}, Students={ngoStats.studentsHelped}, Projects={ngoStats.schoolProjectsCount}, Schools={ngoStats.schoolsReached}
          </div>
        </div>

        {/* Hero Banner */}
        <div className="bg-gradient-to-r from-green-50 to-green-100 m-6 rounded-2xl p-8 flex items-center justify-between relative overflow-hidden">
          <div className="flex-1 z-10">
            <div className="text-lg text-gray-600 mb-1">Welcome back,</div>
            <div className="text-3xl font-bold text-gray-800 mb-2 flex items-center gap-2">
              {ngoDisplayData.ngoName}
              {ngoDisplayData.verificationStatus === 'VERIFIED' && <span className="text-green-500">✓</span>}
            </div>
            <div className="text-sm text-gray-600 mb-2">
              Managing impactful projects across multiple schools to reduce dropout rates
            </div>
            <div className="text-xs text-gray-500 flex items-center gap-2">
              <span>🏛️</span>
              Reg. No: {ngoDisplayData.registrationNumber}
            </div>
          </div>
          <div className="w-96 h-48 rounded-xl overflow-hidden shadow-lg">
            <div className="w-full h-full bg-gradient-to-br from-green-600 to-green-800 flex items-center justify-center text-white">
              <Briefcase className="w-24 h-24 opacity-20" />
            </div>
          </div>
        </div>

        {/* Overview Cards */}
        <div className="px-6 mb-8">
          <div className="mb-4">
            <h2 className="text-2xl font-bold text-gray-800">Overview</h2>
            <p className="text-sm text-gray-500">Track your impact and donations</p>
          </div>
          
          <div className="grid grid-cols-4 gap-6">
            <div className="bg-white rounded-xl p-6 shadow-sm border border-gray-100">
              <div className="flex items-start justify-between mb-4">
                <div className="w-10 h-10 bg-green-100 rounded-lg flex items-center justify-center">
                  <Heart className="w-5 h-5 text-green-600" />
                </div>
                <MoreVertical className="w-4 h-4 text-gray-400" />
              </div>
              <div className="text-sm text-gray-500 mb-1">Total Donated</div>
              <div className="text-3xl font-bold text-gray-800 mb-2" title={`Raw value: ${ngoStats.totalDonated}`}>{formatCurrency(ngoStats.totalDonated)}</div>
              <div className="text-xs text-green-600">{(donationsData || []).length} donations made</div>
              <button 
                onClick={() => navigate(`/ngo-donations/${ngoId}`)}
                className="secondary !border-0 hover:!bg-gray-50 hover:!text-[#0E792E] mt-4 text-sm font-medium flex items-center gap-1"
              >
                View Donations <ChevronRight className="w-4 h-4" />
              </button>
            </div>

            <div className="bg-white rounded-xl p-6 shadow-sm border border-gray-100">
              <div className="flex items-start justify-between mb-4">
                <div className="w-10 h-10 bg-purple-100 rounded-lg flex items-center justify-center">
                  <Users className="w-5 h-5 text-purple-600" />
                </div>
                <MoreVertical className="w-4 h-4 text-gray-400" />
              </div>
              <div className="text-sm text-gray-500 mb-1">Students Helped</div>
              <div className="text-3xl font-bold text-gray-800 mb-2" title={`Raw value: ${ngoStats.studentsHelped}`}>{ngoStats.studentsHelped}</div>
              <div className="text-xs text-green-600">Direct sponsorships</div>
              <button 
                onClick={() => navigate(`/ngo-reporting/${ngoId}`)}
                className="secondary !border-0 hover:!bg-gray-50 hover:!text-[#0E792E] mt-4 text-sm font-medium flex items-center gap-1"
              >
                View Impact <ChevronRight className="w-4 h-4" />
              </button>
            </div>

            <div className="bg-white rounded-xl p-6 shadow-sm border border-gray-100">
              <div className="flex items-start justify-between mb-4">
                <div className="w-10 h-10 bg-blue-100 rounded-lg flex items-center justify-center">
                  <Briefcase className="w-5 h-5 text-blue-600" />
                </div>
                <MoreVertical className="w-4 h-4 text-gray-400" />
              </div>
              <div className="text-sm text-gray-500 mb-1">Projects Created</div>
              <div className="text-3xl font-bold text-gray-800 mb-2" title={`Projects from context: ${(projectsData || []).length}`}>{(projectsData || []).length}</div>
              <div className="text-xs text-green-600">Active projects</div>
              <button 
                onClick={() => navigate(`/ngo-projects/${ngoId}`)}
                className="secondary !border-0 hover:!bg-gray-50 hover:!text-[#0E792E] mt-4 text-sm font-medium flex items-center gap-1"
              >
                View Projects <ChevronRight className="w-4 h-4" />
              </button>
            </div>

            <div className="bg-white rounded-xl p-6 shadow-sm border border-gray-100">
              <div className="flex items-start justify-between mb-4">
                <div className="w-10 h-10 bg-orange-100 rounded-lg flex items-center justify-center">
                  <TrendingUp className="w-5 h-5 text-orange-600" />
                </div>
                <MoreVertical className="w-4 h-4 text-gray-400" />
              </div>
              <div className="text-sm text-gray-500 mb-1">Schools Reached</div>
              <div className="text-3xl font-bold text-gray-800 mb-2" title={`Raw value: ${ngoStats.schoolsReached}`}>{ngoStats.schoolsReached}</div>
              <div className="text-xs text-green-600">Through donations</div>
              <button 
                onClick={() => navigate(`/ngo-gamification/${ngoId}`)}
                className="secondary !border-0 hover:!bg-gray-50 hover:!text-[#0E792E] mt-4 text-sm font-medium flex items-center gap-1"
              >
                View Ranking <ChevronRight className="w-4 h-4" />
              </button>
            </div>
          </div>
        </div>

        {/* Start Scholarship Section */}
        <div className="px-6 mb-8">
          <div className="grid grid-cols-3 gap-6">
            <div className="col-span-2">
              <div className="bg-gradient-to-r from-green-50 to-emerald-50 rounded-2xl p-8 mb-6">
                <div className="flex justify-between items-start">
                  <div>
                    <p className="text-green-700 mb-2">Welcome, NGO</p>
                    <h2 className="text-4xl font-bold text-gray-900 mb-2">
                      Start Your
                      <br />
                      Scholarship Today
                    </h2>
                    <p className="text-gray-600 mb-6">
                      Support students in Government Primary
                      <br />
                      Schools throughout Bangladesh
                    </p>
                    <button 
                      onClick={() => {
                        setDonationDialogData({
                          donationType: "student",
                          title: "Sponsor a Student",
                          description: "Help provide education opportunities for students in need",
                          itemData: null
                        });
                        setDonationDialogOpen(true);
                      }}
                      className="bg-green-600 text-white px-6 py-3 rounded-lg font-semibold hover:bg-green-700"
                    >
                      Start Now
                    </button>
                  </div>
                  <div className="grid grid-cols-2 gap-3">
                    {mockStudentsForSponsorship.map((student, idx) => (
                      <div key={idx} className="bg-white rounded-lg overflow-hidden shadow-sm">
                        <div className="relative">
                          <img src={student.image} alt={student.name} className="w-full h-32 object-cover" />
                          <span className="absolute top-2 right-2 bg-white text-green-600 text-xs px-2 py-1 rounded-full font-semibold">
                            {student.status}
                          </span>
                        </div>
                        <div className="p-3">
                          <p className="text-xs text-gray-500 mb-1">Looking for Scholarship</p>
                          <p className="font-semibold text-sm">{student.name}, {student.class}</p>
                        </div>
                      </div>
                    ))}
                  </div>
                </div>
              </div>
            </div>
            
            <div className="space-y-6">
              <DonorGamificationCard 
                currentLevel={gamificationInfo.currentLevel}
                totalPoints={gamificationInfo.totalPoints}
                badgesEarned={gamificationInfo.badgesEarned}
                rankingPosition={gamificationInfo.rankingPosition}
                pointsToNextLevel={gamificationData?.pointsToNextLevel}
                progressPercentage={gamificationData?.progressPercentage}
              />
            </div>
          </div>
        </div>

        {/* School Projects Available */}
        <div className="px-6 mb-8">
          <div className="flex items-center justify-between mb-4">
            <div>
              <h2 className="text-xl font-bold text-gray-800 flex items-center gap-2">
                <span className="text-green-600">📚</span>
                School Projects Available
              </h2>
              <p className="text-sm text-gray-500">Support school projects that need funding</p>
            </div>
            <button 
              onClick={() => navigate(`/ngo-projects/${ngoId}`)}
              className="secondary !border-0 hover:!bg-gray-50 hover:!text-[#0E792E] text-sm font-medium flex items-center gap-1"
            >
              View All <ChevronRight className="w-4 h-4" />
            </button>
          </div>

          <div className="grid grid-cols-3 gap-4">
            {data.schoolProjects.length > 0 ? data.schoolProjects.slice(0, 6).map((project) => (
              <div key={project.projectId || project.project_id} className="bg-white rounded-xl overflow-hidden shadow-sm border border-gray-100">
                <div className="relative h-40 bg-gradient-to-br from-blue-100 to-blue-200">
                  <span className={`absolute top-3 right-3 px-2 py-1 rounded-full text-xs font-medium ${getStatusColor(project.status)}`}>
                    • {(project.status || 'active').toUpperCase()}
                  </span>
                  <div className="absolute bottom-3 left-3 text-white">
                    <div className="text-xs opacity-80">Required</div>
                    <div className="text-lg font-bold">{formatCurrency(project.requiredAmount || 0)}</div>
                  </div>
                </div>
                <div className="p-4">
                  <div className="text-sm font-semibold text-gray-800 mb-2">
                    {project.projectTitle || project.project_title || 'School Project'}
                  </div>
                  <div className="text-xs text-gray-600 mb-3 line-clamp-2">
                    {project.projectDescription || project.project_description || 'Help support this school project'}
                  </div>
                  <div className="text-xs text-gray-500 mb-3">
                    Raised: {formatCurrency(project.raisedAmount || 0)} / {formatCurrency(project.requiredAmount || 0)}
                  </div>
                  <div className="w-full bg-gray-200 rounded-full h-2 mb-4">
                    <div 
                      className="bg-green-600 h-2 rounded-full" 
                      style={{ 
                        width: `${project.requiredAmount > 0 ? ((project.raisedAmount || 0) / project.requiredAmount) * 100 : 0}%` 
                      }}
                    ></div>
                  </div>
                  <div className="flex gap-2">
                    <button 
                      onClick={() => {
                        setDonationDialogData({
                          donationType: "project",
                          title: "Support School Project",
                          description: "Help fund this school project",
                          itemData: project
                        });
                        setDonationDialogOpen(true);
                      }}
                      className="flex-1 bg-green-600 text-white py-2 text-sm rounded-lg hover:bg-green-700 transition-colors flex items-center justify-center gap-1"
                    >
                      <Heart className="w-3 h-3" />
                      Donate
                    </button>
                    <button 
                      onClick={() => handleViewProject(project)}
                      className="px-3 py-2 text-sm rounded-lg bg-gray-50 text-gray-600 hover:bg-gray-100 transition-colors"
                    >
                      <Eye className="w-3 h-3" />
                    </button>
                  </div>
                </div>
              </div>
            )) : (
              <div className="col-span-3 text-center py-12 text-gray-500 bg-white rounded-xl">
                <div className="flex flex-col items-center gap-3">
                  <span className="text-4xl">📚</span>
                  <span className="text-lg font-medium">No school projects available</span>
                  <span className="text-sm">Check back later for new projects to support</span>
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
                  {data.schoolProjects.length} school projects • 
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
