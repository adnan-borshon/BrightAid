import { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { ChevronLeft, Target, TrendingUp, DollarSign, Calendar, User } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import ProjectRecordExpenseModal from "./Modal/ProjectRecordExpenseModal";
import ProjectUpdateModal from "./Modal/ProjectPostUpdateModal";
import DashSidebar from "./DashSidebar";
import { useApp } from '../context/AppContext';

export default function ProjectDetails() {
  const { projectId } = useParams();
  const navigate = useNavigate();
  const { schoolData, API_BASE_URL } = useApp();
  const [showRecordExpense, setShowRecordExpense] = useState(false);
  const [showPostUpdate, setShowPostUpdate] = useState(false);
  const [project, setProject] = useState(null);
  const [utilizations, setUtilizations] = useState([]);
  const [updates, setUpdates] = useState([]);
  const [donations, setDonations] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  
  // Determine if came from dashboard or projects page
  const cameFromDashboard = document.referrer.includes('/dashboard/');

  useEffect(() => {
    if (projectId) {
      fetchProjectDetails();
    }
  }, [projectId]);

  const fetchProjectDetails = async () => {
    setIsLoading(true);
    try {
      const projectResponse = await fetch(`${API_BASE_URL}/school-projects/${projectId}`);
      
      if (projectResponse.ok) {
        const projectData = await projectResponse.json();
        setProject({
          projectId: projectData.projectId,
          projectTitle: projectData.projectTitle,
          projectDescription: projectData.projectDescription || 'No description available',
          status: projectData.status || 'active',
          projectType: { typeName: projectData.projectTypeName || 'General' },
          priorityLevel: projectData.priorityLevel || 'Medium',
          startDate: projectData.startDate,
          expectedCompletion: projectData.expectedCompletion,
          requiredAmount: projectData.totalAmount || 0,
          raisedAmount: projectData.scholarshipAmount || 0,
          utilizedAmount: projectData.utilizedAmount || 0
        });
      }

    } catch (error) {
      console.error('Error fetching project details:', error);
    } finally {
      setIsLoading(false);
    }
  };

  if (isLoading) {
    return (
      <div className="flex h-screen bg-gray-50">
        <div className="flex-1 flex items-center justify-center">
          <div className="text-center">
            <div className="w-16 h-16 border-4 border-green-600 border-t-transparent rounded-full animate-spin mx-auto mb-4"></div>
            <p className="text-gray-600">Loading project details...</p>
          </div>
        </div>
      </div>
    );
  }

  if (!project) {
    return (
      <div className="flex h-screen bg-gray-50">
        <DashSidebar />
        <div className="flex-1 flex items-center justify-center">
          <div className="text-center">
            <p className="text-gray-600 mb-4">Project not found</p>
            <Button onClick={() => navigate(-1)}>Go Back</Button>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="flex h-screen bg-gray-50">
      <DashSidebar />
      <div className="flex-1 overflow-auto">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <Button 
          variant="ghost" 
          onClick={() => navigate(-1)}
          className="flex items-center gap-2 mb-6"
          data-testid="button-back-to-projects"
        >
          <ChevronLeft className="w-5 h-5" />
          {cameFromDashboard ? 'Back to Dashboard' : 'Back to Projects'}
        </Button>

        {/* Project Header */}
        <div className="bg-white rounded-lg shadow p-6 mb-6">
          <div className="flex items-start justify-between mb-4">
            <div>
              <h2 className="text-2xl font-bold text-gray-800 mb-2" data-testid="text-project-title">
                {project.projectTitle}
              </h2>
              <span className={`text-sm px-3 py-1 rounded-full ${
                project.status === 'in_progress' ? 'bg-blue-100 text-blue-700' :
                project.status === 'funded' ? 'bg-green-100 text-green-700' :
                'bg-yellow-100 text-yellow-700'
              }`}>
                {project.status.replace('_', ' ')}
              </span>
            </div>
          </div>
          <p className="text-gray-600 mb-4">{project.projectDescription}</p>
          <div className="grid grid-cols-2 md:grid-cols-4 gap-4 text-sm">
            <div>
              <span className="text-gray-500">Type:</span>
              <p className="font-semibold">{project.projectType.typeName}</p>
            </div>
            <div>
              <span className="text-gray-500">Priority:</span>
              <p className="font-semibold">{project.priorityLevel}</p>
            </div>
            <div>
              <span className="text-gray-500">Start Date:</span>
              <p className="font-semibold">{project.startDate || 'Not started'}</p>
            </div>
            <div>
              <span className="text-gray-500">Expected Completion:</span>
              <p className="font-semibold">{project.expectedCompletion}</p>
            </div>
          </div>
        </div>

        {/* Funding Summary */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-6">
          <div className="bg-white rounded-lg shadow p-6">
            <div className="flex items-center justify-between mb-2">
              <span className="text-gray-500">Required</span>
              <Target className="w-5 h-5 text-gray-500" />
            </div>
            <p className="text-2xl font-bold text-gray-800" data-testid="text-required-amount">
              ৳{project.requiredAmount.toLocaleString()}
            </p>
          </div>
          <div className="bg-white rounded-lg shadow p-6">
            <div className="flex items-center justify-between mb-2">
              <span className="text-gray-500">Raised</span>
              <TrendingUp className="w-5 h-5 text-green-500" />
            </div>
            <p className="text-2xl font-bold text-green-600" data-testid="text-raised-amount">
              ৳{project.raisedAmount.toLocaleString()}
            </p>
            <p className="text-sm text-gray-500 mt-1">
              {Math.round((project.raisedAmount / project.requiredAmount) * 100)}% funded
            </p>
          </div>
          <div className="bg-white rounded-lg shadow p-6">
            <div className="flex items-center justify-between mb-2">
              <span className="text-gray-500">Utilized</span>
              <DollarSign className="w-5 h-5 text-blue-600" />
            </div>
            <p className="text-2xl font-bold text-blue-600" data-testid="text-utilized-amount">
              ৳{project.utilizedAmount.toLocaleString()}
            </p>
            <p className="text-sm text-gray-500 mt-1">
              ৳{(project.raisedAmount - project.utilizedAmount).toLocaleString()} remaining
            </p>
          </div>
        </div>

        {/* Tabs */}
        <Tabs defaultValue="utilizations" className="space-y-6">
          <TabsList className="grid w-full grid-cols-3">
            <TabsTrigger value="utilizations" data-testid="tab-utilizations">Fund Utilization</TabsTrigger>
            <TabsTrigger value="updates" data-testid="tab-updates">Project Updates</TabsTrigger>
            <TabsTrigger value="donations" data-testid="tab-donations">Donations</TabsTrigger>
          </TabsList>

          <TabsContent value="utilizations" className="space-y-4">
            <div className="flex items-center justify-between mb-4">
              <h3 className="text-lg font-bold text-gray-800">Fund Utilization Records</h3>
              <Button 
                onClick={() => setShowRecordExpense(true)}
                data-testid="button-record-expense-tab"
              >
                Record Expense
              </Button>
            </div>
            {utilizations.length === 0 ? (
              <div className="text-center py-12 text-gray-500">
                <p>No expenses recorded yet</p>
              </div>
            ) : (
              <div className="space-y-4">
                {utilizations.map((expense) => (
                  <div key={expense.utilizationId} className="border border-gray-200 rounded-lg p-4">
                    <h4 className="font-semibold text-gray-800">{expense.description}</h4>
                    <p className="text-gray-600">Amount: ৳{expense.amount.toLocaleString()}</p>
                  </div>
                ))}
              </div>
            )}
          </TabsContent>

          <TabsContent value="updates" className="space-y-4">
            <div className="flex items-center justify-between mb-4">
              <h3 className="text-lg font-bold text-gray-800">Project Updates</h3>
              <Button 
                onClick={() => setShowPostUpdate(true)}
                className="bg-green-600 hover:bg-green-700"
                data-testid="button-post-update-tab"
              >
                Post Update
              </Button>
            </div>
            {updates.length === 0 ? (
              <div className="text-center py-12 text-gray-500">
                <p>No updates posted yet</p>
              </div>
            ) : (
              <div className="space-y-4">
                {updates.map((update) => (
                  <div key={update.updateId} className="border border-gray-200 rounded-lg p-4" data-testid={`update-${update.updateId}`}>
                    <div className="flex items-start justify-between mb-3">
                      <div className="flex-grow">
                        <h4 className="font-semibold text-gray-800 mb-1">{update.updateTitle}</h4>
                        <p className="text-sm text-gray-600 mb-2">{update.updateDescription}</p>
                        <div className="flex items-center gap-4 text-sm text-gray-500">
                          <span className="flex items-center gap-1">
                            <Calendar className="w-4 h-4" />
                            {new Date(update.createdAt).toLocaleDateString()}
                          </span>
                          <span className="flex items-center gap-1">
                            <User className="w-4 h-4" />
                            {update.updatedByName}
                          </span>
                        </div>
                      </div>
                      <div className="text-right ml-4">
                        <p className="text-xl font-bold text-blue-600">{update.progressPercentage}%</p>
                        <p className="text-xs text-gray-500">Progress</p>
                      </div>
                    </div>
                    
                    <div className="bg-green-50 rounded-lg p-3">
                      <p className="text-sm text-green-800">Amount Utilized: ৳{update.amountUtilized.toLocaleString()}</p>
                    </div>
                  </div>
                ))}
              </div>
            )}
          </TabsContent>

          <TabsContent value="donations" className="space-y-4">
            <h3 className="text-lg font-bold text-gray-800 mb-4">Donations Received</h3>
            {donations.length === 0 ? (
              <div className="text-center py-12 text-gray-500">
                <p>No donations received yet</p>
              </div>
            ) : (
              <div className="space-y-4">
                {donations.map((donation) => (
                  <div key={donation.donationId} className="border border-gray-200 rounded-lg p-4" data-testid={`donation-${donation.donationId}`}>
                    <div className="flex items-start justify-between">
                      <div className="flex-grow">
                        <h4 className="font-semibold text-gray-800 mb-1">{donation.donorName}</h4>
                        <div className="flex items-center gap-4 text-sm text-gray-500 mb-2 flex-wrap">
                          <span className="flex items-center gap-1">
                            <Calendar className="w-4 h-4" />
                            {new Date(donation.donatedAt).toLocaleDateString()}
                          </span>
                          <span className="flex items-center gap-1">
                            <DollarSign className="w-4 h-4" />
                            {donation.paymentMethod}
                          </span>
                          <span className="text-xs px-2 py-1 bg-green-50 text-green-700 rounded">
                            {donation.paymentStatus}
                          </span>
                        </div>
                        {donation.donorMessage && (
                          <p className="text-sm text-gray-600 italic">"{donation.donorMessage}"</p>
                        )}
                        <p className="text-xs text-gray-500 mt-2">Ref: {donation.transactionReference}</p>
                      </div>
                      <div className="text-right ml-4">
                        <p className="text-2xl font-bold text-green-600">৳{donation.amount.toLocaleString()}</p>
                        <p className="text-xs text-gray-500">{donation.donationType.replace('_', ' ')}</p>
                      </div>
                    </div>
                  </div>
                ))}
              </div>
            )}
          </TabsContent>
        </Tabs>
        </div>
      </div>

      <ProjectRecordExpenseModal 
        open={showRecordExpense} 
        onOpenChange={setShowRecordExpense}
        project={project}
      />
      
      <ProjectUpdateModal
        open={showPostUpdate} 
        onOpenChange={setShowPostUpdate}
        project={project}
      />
    </div>
  );
}
