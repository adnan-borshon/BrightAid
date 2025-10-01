import { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import {
  DollarSign,
  School,
  Users,
  TrendingUp,
  Plus,
  Search,
  MapPin,
  Calendar,
  Award,
  Info,
} from "lucide-react";
import { useDonor } from "@/context/DonorContext";
import DonorBrowseSchoolDialog from "@/components/Dialog/DonorBrowseSchoolDialog";
import DonorDonationDialog from "@/components/Dialog/DonorDonationDialog";
import DonorProjectDialog from "@/components/Dialog/DonorProjectDialog";
import DonorDonationChart from "@/components/DonorDonationChart";
import DonorDonationHistoryTable from "@/components/DonorDonationHistory";
import DonorGamificationCard from "@/components/DonorGamificationCard";
import DonorRecentActivityFeed from "@/components/DonorRecentActivity";
import Sidebar from './DonorDashSidebar';

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

export default function DonorDashboard() {
  const { donorId } = useParams();
  const { 
    donorData, 
    donationsData, 
    projectsData, 
    schoolsData, 
    loading, 
    refreshDonorData 
  } = useDonor();
  
  const [activeTab, setActiveTab] = useState("available");
  const [browseSchoolsOpen, setBrowseSchoolsOpen] = useState(false);
  const [donationDialogOpen, setDonationDialogOpen] = useState(false);
  const [selectedProject, setSelectedProject] = useState(null);
  const [projectDetailsOpen, setProjectDetailsOpen] = useState(false);

  useEffect(() => {
    const userId = localStorage.getItem('userId');
    if (userId) {
      refreshDonorData(userId);
    }
  }, []);

  const totalDonated = donationsData.reduce((sum, donation) => sum + (donation.amount || 0), 0);
  const uniqueSchools = new Set(projectsData.map(p => p.schoolId)).size;
  const activeProjects = projectsData.filter(p => p.status === 'ACTIVE').length;
  
  const stats = [
    {
      label: "Total Donated",
      value: `৳${totalDonated.toLocaleString()}`,
      change: "+12%",
      period: "Last 30 days",
      icon: DollarSign,
      color: "green",
    },
    {
      label: "Schools Supported",
      value: uniqueSchools.toString(),
      change: "+8%",
      period: "Active partnerships",
      icon: School,
      color: "green",
    },
    {
      label: "Students Sponsored",
      value: "0",
      change: "",
      period: "Direct beneficiaries",
      icon: Users,
      color: "green",
    },
    {
      label: "Active Projects",
      value: activeProjects.toString(),
      change: "+3%",
      period: "Ongoing initiatives",
      icon: TrendingUp,
      color: "green",
    },
  ];

  const handleViewDetails = (project) => {
    setSelectedProject(project);
    setProjectDetailsOpen(true);
  };

  const handleDonate = (project) => {
    setDonationDialogOpen(true);
  };

  const chartData = donationsData.slice(-6).map((donation, index) => ({
    month: new Date(donation.createdAt || Date.now()).toLocaleDateString('en-US', { month: 'short' }),
    amount: donation.amount || 0
  }));

  const gamificationData = {
    currentLevel: "Gold",
    totalPoints: 8500,
    pointsToNextLevel: 1500,
    earnedBadges: ["School Supporter", "Child Guardian", "Top Contributor", "Early Adopter"],
    rankingPosition: 12
  };

  const recentActivities = donationsData.slice(-4).map((donation, index) => ({
    id: donation.id || index,
    type: "donation",
    title: "Donation Successful",
    description: `Your donation of ৳${(donation.amount || 0).toLocaleString()} was processed`,
    timestamp: new Date(donation.createdAt || Date.now()).toLocaleDateString()
  }));

  if (loading) {
    return <div className="p-6">Loading...</div>;
  }

  return (
    <div className="flex h-screen bg-gray-50">
      <Sidebar />
      
      <div className="flex-1 overflow-auto">
        <DonorBrowseSchoolDialog
          open={browseSchoolsOpen}
          onOpenChange={setBrowseSchoolsOpen}
        />
        <DonorDonationDialog
          open={donationDialogOpen}
          onOpenChange={setDonationDialogOpen}
        />
        <DonorProjectDialog
          open={projectDetailsOpen}
          onOpenChange={setProjectDetailsOpen}
          project={selectedProject}
          onDonate={() => setDonationDialogOpen(true)}
        />

        <div className="bg-white border-b">
          <div className="p-6">
            <h1 className="text-2xl font-bold mb-1">Impact Overview</h1>
            <div className="grid grid-cols-4 gap-6 mt-6">
              {stats.map((stat, idx) => (
                <div key={idx} className="bg-gray-50 rounded-lg p-4">
                  <div className="flex items-center justify-between mb-2">
                    <span className="text-sm text-gray-600">{stat.label}</span>
                    <div className="w-8 h-8 bg-green-100 rounded-lg flex items-center justify-center">
                      <stat.icon size={16} className="text-green-600" />
                    </div>
                  </div>
                  <div className="flex items-baseline gap-2">
                    <span className="text-2xl font-bold">{stat.value}</span>
                    {stat.change && (
                      <span className="text-sm text-green-600">{stat.change}</span>
                    )}
                  </div>
                  <div className="text-xs text-gray-500 mt-1">{stat.period}</div>
                </div>
              ))}
            </div>
          </div>
        </div>

        <div className="p-6">
          <div className="grid grid-cols-3 gap-6">
            <div className="col-span-2">
              <div className="bg-gradient-to-r from-green-50 to-emerald-50 rounded-2xl p-8 mb-6">
                <div className="flex justify-between items-start">
                  <div>
                    <p className="text-green-700 mb-2">Welcome, Donor</p>
                    <h2 className="text-4xl font-bold text-gray-900 mb-2">
                      Start Your
                      <br />
                      Scholarship Today
                    </h2>
                    <p className="text-gray-600 mb-6">
                      Directly for students in Government Primary
                      <br />
                      Schools throughout Bangladesh
                    </p>
                    <button className="bg-green-600 text-white px-6 py-3 rounded-lg font-semibold hover:bg-green-700">
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

              <div className="mb-6">
                <h3 className="text-lg font-bold mb-4">Quick Actions</h3>
                <div className="flex gap-3">
                  <button
                    onClick={() => setBrowseSchoolsOpen(true)}
                    className="bg-green-600 text-white px-4 py-2 rounded-lg font-semibold flex items-center gap-2 hover:bg-green-700"
                  >
                    <School size={18} />
                    Browse Schools
                  </button>
                  <button
                    onClick={() => setDonationDialogOpen(true)}
                    className="bg-white border border-gray-300 px-4 py-2 rounded-lg font-semibold flex items-center gap-2 hover:bg-gray-50"
                  >
                    <DollarSign size={18} />
                    Make Donation
                  </button>
                </div>
              </div>

              <div className="bg-white rounded-xl shadow-sm">
                <div className="border-b px-6 py-4">
                  <div className="flex gap-6">
                    <button
                      onClick={() => setActiveTab("available")}
                      className={`pb-2 font-semibold ${
                        activeTab === "available"
                          ? "text-green-600 border-b-2 border-green-600"
                          : "text-gray-500"
                      }`}
                    >
                      Available Projects
                    </button>
                    <button
                      onClick={() => setActiveTab("history")}
                      className={`pb-2 font-semibold ${
                        activeTab === "history"
                          ? "text-green-600 border-b-2 border-green-600"
                          : "text-gray-500"
                      }`}
                    >
                      Donation History
                    </button>
                    <button
                      onClick={() => setActiveTab("analytics")}
                      className={`pb-2 font-semibold ${
                        activeTab === "analytics"
                          ? "text-green-600 border-b-2 border-green-600"
                          : "text-gray-500"
                      }`}
                    >
                      Analytics
                    </button>
                  </div>
                </div>

                <div className="p-6">
                  <div className="relative mb-4">
                    <Search className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400" size={18} />
                    <input
                      type="text"
                      placeholder="Search projects..."
                      className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-green-500"
                    />
                  </div>

                  <div className="space-y-4">
                    {activeTab === "available" && projectsData.map((project, idx) => (
                      <div key={project.id || idx} className="border border-gray-200 rounded-lg p-5">
                        <h4 className="font-bold text-lg">{project.projectTitle || project.title}</h4>
                        <p className="text-gray-600 text-sm mb-4">{project.description || 'No description available'}</p>
                        <div className="flex gap-3">
                          <button
                            onClick={() => handleViewDetails(project)}
                            className="flex-1 border border-gray-300 px-4 py-2 rounded-lg font-semibold hover:bg-gray-50"
                          >
                            View Details
                          </button>
                          <button
                            onClick={() => handleDonate(project)}
                            className="flex-1 bg-green-600 text-white px-4 py-2 rounded-lg font-semibold hover:bg-green-700"
                          >
                            Donate Now
                          </button>
                        </div>
                      </div>
                    ))}
                    
                    {activeTab === "history" && (
                      <DonorDonationHistoryTable donations={donationsData} />
                    )}
                    
                    {activeTab === "analytics" && (
                      <DonorDonationChart data={chartData} />
                    )}
                  </div>
                </div>
              </div>
            </div>

            <div className="space-y-6">
              <DonorGamificationCard {...gamificationData} />
              <DonorRecentActivityFeed activities={recentActivities} />
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}