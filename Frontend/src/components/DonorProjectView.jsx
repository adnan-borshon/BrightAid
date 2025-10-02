import { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import { DollarSign, TrendingUp, FileText, Search, MapPin, Calendar } from "lucide-react";
import { useDonor } from "@/context/DonorContext";
import Sidebar from './DonorDashSidebar';

export default function DonorProjectView() {
  const { donorId } = useParams();
  const { projectsData, donationsData, loading, refreshDonorData, fetchProjectsData } = useDonor();
  const [searchTerm, setSearchTerm] = useState("");
  const [activeTab, setActiveTab] = useState("all");

  useEffect(() => {
    const userId = localStorage.getItem('userId');
    if (userId) {
      refreshDonorData(userId);
    }
  }, []);



  // Show all projects from all schools
  const activeProjects = projectsData.filter((p) => p.status === "ACTIVE");
  const completedProjects = projectsData.filter((p) => p.status === "COMPLETED");

  const filterProjects = () => {
    let projects = projectsData;
    if (activeTab === "active") projects = activeProjects;
    if (activeTab === "completed") projects = completedProjects;

    return projects.filter(
      (project) =>
        (project.projectTitle || project.title || '').toLowerCase().includes(searchTerm.toLowerCase()) ||
        (project.schoolId || '').toString().includes(searchTerm.toLowerCase())
    );
  };

  const totalContributed = donationsData.reduce((sum, d) => sum + (d.amount || 0), 0);
  const totalUtilized = projectsData.reduce((sum, p) => sum + (p.raisedAmount || 0), 0);

  if (loading) {
    return (
      <div className="flex h-screen bg-gray-50">
        <Sidebar />
        <div className="flex-1 flex items-center justify-center">
          <div className="text-lg">Loading...</div>
        </div>
      </div>
    );
  }

  return (
    <div className="flex h-screen bg-gray-50">
      <Sidebar />
      <div className="flex-1 overflow-auto bg-white">
        <div className="border-b">
          <div className="p-6">
            <h1 className="text-2xl font-bold mb-1">All Projects</h1>
            <p className="text-gray-600 text-sm">
              Browse and donate to projects from all schools
            </p>

            <div className="grid grid-cols-3 gap-6 mt-6">
              <div className="bg-gray-50 rounded-lg p-4">
                <div className="flex items-center justify-between mb-2">
                  <span className="text-sm text-gray-600">Total Contributed</span>
                  <div className="w-8 h-8 bg-green-100 rounded-lg flex items-center justify-center">
                    <DollarSign size={16} className="text-green-600" />
                  </div>
                </div>
                <div className="text-2xl font-bold">৳{totalContributed.toLocaleString()}</div>
                <div className="text-xs text-gray-500 mt-1">
                  Across {projectsData.length} projects
                </div>
              </div>

              <div className="bg-gray-50 rounded-lg p-4">
                <div className="flex items-center justify-between mb-2">
                  <span className="text-sm text-gray-600">Funds Utilized</span>
                  <div className="w-8 h-8 bg-green-100 rounded-lg flex items-center justify-center">
                    <TrendingUp size={16} className="text-green-600" />
                  </div>
                </div>
                <div className="text-2xl font-bold">৳{totalUtilized.toLocaleString()}</div>
                <div className="text-xs text-gray-500 mt-1">
                  {totalContributed > 0 ? ((totalUtilized / totalContributed) * 100).toFixed(0) : 0}% of contributions
                </div>
              </div>

              <div className="bg-gray-50 rounded-lg p-4">
                <div className="flex items-center justify-between mb-2">
                  <span className="text-sm text-gray-600">Active Projects</span>
                  <div className="w-8 h-8 bg-green-100 rounded-lg flex items-center justify-center">
                    <FileText size={16} className="text-green-600" />
                  </div>
                </div>
                <div className="text-2xl font-bold">{activeProjects.length}</div>
                <div className="text-xs text-gray-500 mt-1">
                  {completedProjects.length} completed
                </div>
              </div>
            </div>
          </div>
        </div>

        <div className="p-6">
          <div className="mb-6">
            <div className="flex gap-6 border-b mb-4">
              <button
                onClick={() => setActiveTab("active")}
                className={`pb-2 font-semibold ${
                  activeTab === "active"
                    ? "text-green-600 border-b-2 border-green-600"
                    : "text-gray-500"
                }`}
              >
                Active ({activeProjects.length})
              </button>
              <button
                onClick={() => setActiveTab("completed")}
                className={`pb-2 font-semibold ${
                  activeTab === "completed"
                    ? "text-green-600 border-b-2 border-green-600"
                    : "text-gray-500"
                }`}
              >
                Completed ({completedProjects.length})
              </button>
              <button
                onClick={() => setActiveTab("all")}
                className={`pb-2 font-semibold ${
                  activeTab === "all"
                    ? "text-green-600 border-b-2 border-green-600"
                    : "text-gray-500"
                }`}
              >
                All ({projectsData.length})
              </button>
            </div>

            <div className="flex gap-4">
              <div className="relative flex-1 max-w-md">
                <Search
                  className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400"
                  size={18}
                />
                <input
                  type="text"
                  placeholder="Search projects or schools..."
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-green-500"
                />
              </div>
              <button
                onClick={() => fetchProjectsData()}
                className="px-4 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700"
              >
                Refresh
              </button>
            </div>
          </div>

          <div className="space-y-6">
            {filterProjects().length === 0 ? (
              <div className="text-center py-12">
                <p className="text-gray-500">No projects found matching your criteria.</p>
                <p className="text-sm text-gray-400 mt-2">Total projects available: {projectsData.length}</p>
              </div>
            ) : (
              filterProjects().map((project) => (
                <div
                  key={project.projectId || project.id}
                  className="bg-white border border-gray-200 rounded-xl p-6"
                >
                  <div className="flex justify-between items-start mb-4">
                    <div>
                      <h3 className="text-xl font-bold mb-1">{project.projectTitle || project.title}</h3>
                      <div className="flex items-center gap-2 text-sm text-gray-600">
                        <MapPin size={14} />
                        <span>School ID: {project.schoolId}</span>
                      </div>
                    </div>
                    <span className="px-3 py-1 rounded-full text-sm font-semibold bg-green-100 text-green-700">
                      {project.status || 'Active'}
                    </span>
                  </div>

                  <p className="text-gray-600 mb-4">{project.projectDescription || 'No description available'}</p>

                  <div className="grid grid-cols-2 gap-4 mb-4">
                    <div className="flex items-center gap-2 text-sm text-gray-600">
                      <Calendar size={14} />
                      <span>Created: {new Date(project.createdAt || Date.now()).toLocaleDateString()}</span>
                    </div>
                    <div className="text-sm text-gray-600">
                      Type: {project.projectType || 'General'}
                    </div>
                  </div>

                  <div className="mb-3">
                    <div className="flex justify-between text-sm mb-2">
                      <span className="font-semibold">
                        ৳{(project.raisedAmount || 0).toLocaleString()} raised
                      </span>
                      <span className="text-gray-500">
                        of ৳{(project.requiredAmount || 100000).toLocaleString()}
                      </span>
                    </div>
                    <div className="w-full bg-gray-200 rounded-full h-2">
                      <div
                        className="bg-green-600 h-2 rounded-full"
                        style={{ width: `${((project.raisedAmount || 0) / (project.requiredAmount || 100000)) * 100}%` }}
                      ></div>
                    </div>
                    <div className="text-sm text-gray-600 mt-1">
                      {(((project.raisedAmount || 0) / (project.requiredAmount || 100000)) * 100).toFixed(1)}% funded
                    </div>
                  </div>
                </div>
              ))
            )}
          </div>
        </div>
      </div>
    </div>
  );
}