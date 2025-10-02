import { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import { DollarSign, TrendingUp, FileText, Search } from "lucide-react";
import { useDonor } from "@/context/DonorContext";
import Sidebar from './DonorDashSidebar';
import DonorProjectCard from './DonorProjectCard';
import DonorDonationDialog from './Dialog/DonorDonationDialog';
import DonorProjectDialog from './Dialog/DonorProjectDialog';

export default function DonorProjectView() {
  const { id: userId } = useParams(); // URL param is now userId
  const { projectsData, donationsData, loading, refreshDonorData, fetchProjectsData } = useDonor();
  const [searchTerm, setSearchTerm] = useState("");
  const [activeTab, setActiveTab] = useState("all");
  const [donationDialogOpen, setDonationDialogOpen] = useState(false);
  const [donationDialogData, setDonationDialogData] = useState(null);
  const [selectedProject, setSelectedProject] = useState(null);
  const [projectDetailsOpen, setProjectDetailsOpen] = useState(false);
  const [totalProjectContributions, setTotalProjectContributions] = useState(0);

  useEffect(() => {
    const userId = localStorage.getItem('userId');
    if (userId) {
      refreshDonorData(userId);
      fetchProjectContributions();
    }
  }, []);
  
  const fetchProjectContributions = async () => {
    try {
      const donorId = localStorage.getItem('donorId');
      if (donorId) {
        const response = await fetch(`http://localhost:8081/api/donor-gamifications/donor/${donorId}/project-contributions`);
        if (response.ok) {
          const amount = await response.json();
          setTotalProjectContributions(amount);
        }
      }
    } catch (error) {
      console.error('Error fetching project contributions:', error);
    }
  };



  // Get projects that the donor has donated to
  const donatedProjectIds = [...new Set(donationsData
    .filter(d => d.projectId || d.project?.projectId)
    .map(d => d.projectId || d.project?.projectId))];
  
  const donatedProjects = projectsData.filter(p => 
    donatedProjectIds.includes(p.projectId || p.id)
  );
  const completedProjects = projectsData.filter((p) => p.status === "COMPLETED");

  const filterProjects = () => {
    let projects = projectsData;
    if (activeTab === "donated") projects = donatedProjects;
    if (activeTab === "completed") projects = completedProjects;

    return projects.filter(
      (project) =>
        (project.projectTitle || project.title || '').toLowerCase().includes(searchTerm.toLowerCase()) ||
        (project.schoolId || '').toString().includes(searchTerm.toLowerCase())
    );
  };

  const totalContributed = totalProjectContributions;
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
                  <span className="text-sm text-gray-600">Donated Projects</span>
                  <div className="w-8 h-8 bg-green-100 rounded-lg flex items-center justify-center">
                    <FileText size={16} className="text-green-600" />
                  </div>
                </div>
                <div className="text-2xl font-bold">{donatedProjects.length}</div>
                <div className="text-xs text-gray-500 mt-1">
                  Projects you've supported
                </div>
              </div>
            </div>
          </div>
        </div>

        <div className="p-6">
          <div className="mb-6">
            <div className="flex gap-6 border-b mb-4">
              <button
                onClick={() => setActiveTab("donated")}
                className={`pb-2 font-semibold ${
                  activeTab === "donated"
                    ? "text-green-600 border-b-2 border-green-600"
                    : "text-gray-500"
                }`}
              >
                Donated ({donatedProjects.length})
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

          <DonorDonationDialog
            open={donationDialogOpen}
            onOpenChange={setDonationDialogOpen}
            donationType={donationDialogData?.donationType}
            title={donationDialogData?.title}
            description={donationDialogData?.description}
            itemData={donationDialogData?.itemData}
          />
          <DonorProjectDialog
            open={projectDetailsOpen}
            onOpenChange={setProjectDetailsOpen}
            project={selectedProject}
            onDonate={() => setDonationDialogOpen(true)}
          />

          <div className="space-y-6">
            {filterProjects().length === 0 ? (
              <div className="text-center py-12">
                <p className="text-gray-500">No projects found matching your criteria.</p>
                <p className="text-sm text-gray-400 mt-2">Total projects available: {projectsData.length}</p>
              </div>
            ) : (
              filterProjects().map((project) => (
                <DonorProjectCard 
                  key={project.projectId || project.id}
                  project={project}
                  onProjectClick={(project) => {
                    setSelectedProject(project);
                    setProjectDetailsOpen(true);
                  }}
                  onDonateClick={(project) => {
                    setDonationDialogData({
                      donationType: "project",
                      title: "Project Donation",
                      description: "Support this specific project to help students and schools",
                      itemData: project
                    });
                    setDonationDialogOpen(true);
                  }}
                />
              ))
            )}
          </div>
        </div>
      </div>
    </div>
  );
}