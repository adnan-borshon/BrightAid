import { MapPin, Calendar } from "lucide-react";

export default function DonorProjectCard({ project, onProjectClick, onDonateClick }) {
  const progressPercentage = ((project.raisedAmount || 0) / (project.requiredAmount || 100000)) * 100;

  return (
    <div
      key={project.projectId || project.id}
      className="bg-white border border-gray-200 rounded-xl p-6 hover:shadow-md transition-shadow"
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
            style={{ width: `${Math.min(progressPercentage, 100)}%` }}
          ></div>
        </div>
        <div className="text-sm text-gray-600 mt-1">
          {progressPercentage.toFixed(1)}% funded
        </div>
      </div>
      
      <div className="flex gap-3 mt-4">
        <button
          onClick={(e) => {
            e.stopPropagation();
            onProjectClick && onProjectClick(project);
          }}
          className="flex-1 px-4 py-2 border border-gray-300 text-gray-700 rounded-lg hover:bg-gray-50 font-medium"
        >
          View Details
        </button>
        <button
          onClick={(e) => {
            e.stopPropagation();
            onDonateClick && onDonateClick(project);
          }}
          className="flex-1 px-4 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700 font-medium"
        >
          Donate Now
        </button>
      </div>
    </div>
  );
}