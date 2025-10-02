import React, { useState } from "react";
import { Target, Users, Calendar, Eye, Receipt, Send, AlertTriangle } from "lucide-react";
import { Button } from "@/components/ui/button";
import ProjectPostUpdateModal from "./Modal/ProjectPostUpdateModal";
import ProjectRecordExpenseModal from "./Modal/ProjectRecordExpenseModal";

const getStatusColor = (status) => {
  switch ((status || "").toLowerCase()) {
    case "draft": return "bg-gray-100 text-gray-700";
    case "active": return "bg-yellow-100 text-yellow-700";
    case "funded": return "bg-green-100 text-green-700";
    case "in_progress": return "bg-blue-100 text-blue-700";
    case "completed": return "bg-emerald-100 text-emerald-700";
    case "cancelled": return "bg-red-100 text-red-700";
    case "unpaid": return "bg-red-100 text-red-700";
    default: return "bg-gray-100 text-gray-700";
  }
};

const getRiskColor = (risk) => {
  if (!risk) return "text-gray-600";
  const lower = risk.toLowerCase();
  if (lower.includes("high")) return "text-red-600";
  if (lower.includes("medium")) return "text-orange-600";
  return "text-green-600";
};

export default function SchoolProjectCard({ project, onViewDetails, onRecordExpense, onPostUpdate, showAllButtons = true }) {
  const [openUpdate, setOpenUpdate] = useState(false);
  const [openExpense, setOpenExpense] = useState(false);
  const [projectData, setProjectData] = useState(project);
 
  const fundingPercentage = projectData.total_amount > 0
    ? Math.round((projectData.scholarship_amount / projectData.total_amount) * 100)
    : 0;

  const handlePostUpdate = async (data) => {
    try {
      const response = await fetch('http://localhost:8081/api/project-updates', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
      });
      if (response.ok) {
        console.log('Project update posted successfully');
        
        // Fetch latest completion rate from backend
        const completionResponse = await fetch(`http://localhost:8081/api/school-projects/${projectData.project_id}/completion-rate`);
        if (completionResponse.ok) {
          const completionRate = await completionResponse.json();
          setProjectData(prev => ({
            ...prev,
            completion_rate: completionRate
          }));
        }
        
        if (onPostUpdate) onPostUpdate(projectData);
      }
    } catch (error) {
      console.error('Error posting update:', error);
    }
  };

  const handleRecordExpense = async (data) => {
    try {
      const response = await fetch('http://localhost:8081/api/fund-utilizations', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
      });
      if (response.ok) {
        console.log('Expense recorded successfully');
        if (onRecordExpense) onRecordExpense(project);
      }
    } catch (error) {
      console.error('Error recording expense:', error);
    }
  };

  return (
    <div className="bg-card rounded-lg shadow-sm border border-border hover:shadow-md transition-shadow">
           {/* Modals */}
      <ProjectPostUpdateModal 
        isOpen={openUpdate} 
        onClose={() => setOpenUpdate(false)} 
        onSubmit={handlePostUpdate}
        project={projectData} 
      />
      <ProjectRecordExpenseModal 
        isOpen={openExpense} 
        onClose={() => setOpenExpense(false)} 
        onSubmit={handleRecordExpense}
        project={projectData} 
      />
      
      <div className="p-6">
        {/* Header */}
        <div className="flex items-start justify-between mb-4">
          <div className="flex-grow">
            <div className="flex items-center justify-between gap-3 flex-wrap">
              <h3 className="text-xl font-bold text-foreground">
                {projectData.project_name}
              </h3>
              <div className="flex gap-2">

              <span className={`text-xs px-2 py-1 rounded-full ${getStatusColor(projectData.status)}`}>
                {projectData.status}
              </span>
              <span className={`text-xs flex items-center gap-1 font-medium ${getRiskColor(projectData.risk_status)}`}>
                <AlertTriangle className="w-3 h-3" />
                {projectData.risk_status}
              </span>
              </div>
            </div>
            <div className="text-muted-foreground text-sm mb-3">
              <div>{projectData.category}</div>
              <div className="mt-2 font-bold">
                 Donor: {projectData.donor_username}
              </div>
            </div>
            <div className="flex items-center gap-4 text-sm text-muted-foreground flex-wrap">
              <span className="flex items-center gap-1">
                <Target className="w-4 h-4" />
                {projectData.project_type}
              </span>
              <span className="flex items-center gap-1">
                <Users className="w-4 h-4" />
                {Math.floor(Math.random() * 50) + 1} donors
              </span>
              <span className="flex items-center gap-1">
                <Calendar className="w-4 h-4" />
                Performance: {projectData.performance_score}%
              </span>
            </div>
          </div>
        </div>

        {/* Funding Progress */}
        <div className="mb-4">
          <div className="flex items-center justify-between mb-2">
            <span className="text-sm font-semibold text-foreground">
              Raised: ৳{projectData.scholarship_amount.toLocaleString()} / ৳{projectData.total_amount.toLocaleString()}
            </span>
            <span className="text-sm text-muted-foreground">
              {fundingPercentage}%
            </span>
          </div>
          <div className="w-full bg-secondary rounded-full h-2">
            <div
              className="bg-green-500 h-2  rounded-full transition-all"
              style={{ width: `${fundingPercentage}%` }}
            />
          </div>
        </div>

        {/* Completion Progress */}
        <div className="mb-4">
          <div className="flex items-center justify-between mb-2">
            <span className="text-sm font-semibold text-foreground">
              Completion: {projectData.completion_rate || 0}%
            </span>
          </div>
          <div className="w-full bg-secondary rounded-full h-2">
            <div
              className="bg-blue-500 h-2 rounded-full transition-all"
              style={{ width: `${projectData.completion_rate || 0}%` }}
            />
          </div>
        </div>

        {/* Action Buttons */}
        {showAllButtons ? (
          <div className="grid grid-cols-1 md:grid-cols-3 gap-2">
            <button
              onClick={() => onViewDetails(projectData.project_id)}
              
              className="third flex items-center justify-center gap-2"
            >
              <Eye className="w-4 h-4" />
              View Details
            </button>
            <Button
              onClick={() => setOpenExpense(true)}
              className="flex items-center justify-center gap-2"
            >
              <Receipt className="w-4 h-4" />
              Record Expense
            </Button>
            <Button
              onClick={() => setOpenUpdate(true)}
              className="flex items-center justify-center gap-2 bg-green-600 hover:bg-green-700"
            >
              <Send className="w-4 h-4" />
              Post Update
            </Button>
          </div>
        ) : (
          <Button
            onClick={() => onViewDetails(projectData.project_id)}
            variant="secondary"
            className="w-full flex items-center justify-center gap-2"
          >
            <Eye className="w-4 h-4" />
            View Details
          </Button>
        )}
      </div>
    </div>
  );
}
