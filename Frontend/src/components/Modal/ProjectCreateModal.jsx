import { useState } from "react";
import { X, Upload, Calendar } from "lucide-react";

const ProjectCreateModal = ({ isOpen, onClose, onSubmit }) => {
  const [formData, setFormData] = useState({
    project_title: "",
    project_description: "",
    project_type_id: "",
    required_amount: "",
    priority_level: "medium",
    start_date: "",
    expected_completion: "",
    project_image: null,
  });

  const [imagePreview, setImagePreview] = useState(null);
  const [charCount, setCharCount] = useState(0);

  const priorityLevels = [
    { value: "low", label: "Low" },
    { value: "medium", label: "Medium" },
    { value: "high", label: "High" },
    { value: "urgent", label: "Urgent" },
  ];

  const projectTypes = [
    { id: 1, name: "Infrastructure", code: "INFRA" },
    { id: 2, name: "Education", code: "EDU" },
    { id: 3, name: "Technology", code: "TECH" },
    { id: 4, name: "Health & Safety", code: "HEALTH" },
    { id: 5, name: "Sports & Recreation", code: "SPORTS" },
    { id: 6, name: "Arts & Culture", code: "ARTS" },
    { id: 7, name: "Environment", code: "ENV" },
  ];

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    
    if (name === "project_description") {
      setCharCount(value.length);
    }
    
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

//   const handleImageUpload = (e) => {
//     const file = e.target.files[0];
//     if (file && (file.type === "image/png" || file.type === "image/jpeg" || file.type === "image/jpg" || file.type === "image/svg+xml")) {
//       setFormData((prev) => ({ ...prev, project_image: file }));
//       const reader = new FileReader();
//       reader.onloadend = () => {
//         setImagePreview(reader.result);
//       };
//       reader.readAsDataURL(file);
//     } else {
//       alert("Please upload only SVG, PNG, JPG or GIF images");
//     }
//   };

  const handleSubmit = async (e) => {
    e.preventDefault();

    // Prepare data for backend
    const submitData = new FormData();

    // Append all form fields
    Object.keys(formData).forEach((key) => {
      if (formData[key] !== null && formData[key] !== "") {
        submitData.append(key, formData[key]);
      }
    });

    // Call the onSubmit callback with FormData
    if (onSubmit) {
      await onSubmit(submitData);
    }

    // Reset form and close modal
    resetForm();
    onClose();
  };

  const resetForm = () => {
    setFormData({
      project_title: "",
      project_description: "",
      project_type_id: "",
      required_amount: "",
      priority_level: "medium",
      start_date: "",
      expected_completion: "",
      project_image: null,
    });
    setImagePreview(null);
    setCharCount(0);
  };

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 backdrop-blur-xs bg-opacity-50  flex items-center justify-center z-50 p-4">
      <div className="bg-white rounded-lg w-full max-w-2xl max-h-[90vh] overflow-y-auto shadow-xl">
        {/* Header */}
        <div className="sticky top-0 bg-white border-b border-gray-200 px-6 py-4 flex items-center justify-between rounded-t-lg">
          <h2 className="text-2xl font-bold text-gray-900">Start a New Project</h2>
          <button
            onClick={onClose}
            className="text-gray-400 hover:text-gray-600 transition-colors"
          >
            <X size={24} />
          </button>
        </div>

        <form onSubmit={handleSubmit} className="p-6">
          {/* Project Info Section */}
          <div className="mb-6">
            <h3 className="text-lg font-semibold text-gray-900 mb-4">
              Project info
            </h3>

     

            {/* Project Title */}
            <div className="mb-4">
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Project Title
              </label>
              <input
                type="text"
                name="project_title"
                value={formData.project_title}
                onChange={handleInputChange}
                required
                className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent outline-none"
                placeholder="e.g., Science Lab Upgrade"
              />
            </div>

            {/* Project Description */}
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Project Description
              </label>
              <textarea
                name="project_description"
                value={formData.project_description}
                onChange={handleInputChange}
                required
                rows="4"
                maxLength={500}
                placeholder="Text area for details, goals, problems to solve, and expected outcomes"
                className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent outline-none resize-none"
              />
              <p className="text-xs text-gray-500 mt-1">{charCount} characters used</p>
            </div>
          </div>

          {/* Timeline & Priority Section */}
          <div className="mb-6">
            <h3 className="text-lg font-semibold text-gray-900 mb-4">
              Timeline & Priority Section
            </h3>

            <div className="space-y-4">
              {/* Required Amount, Project Type, Priority Level */}
              <div className="grid grid-cols-1 md:grid-cols-3 gap-3">
                {/* Required Amount */}
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">
                    Required Amount
                  </label>
                  <div className="relative">
                    <span className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-500 font-medium">
                      ৳
                    </span>
                    <input
                      type="number"
                      name="required_amount"
                      value={formData.required_amount}
                      onChange={handleInputChange}
                      required
                      min="0"
                      step="0.01"
                      placeholder="20,000"
                      className="w-full pl-8 pr-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent outline-none"
                    />
                  </div>
                </div>

                {/* Project Type */}
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">
                    Project Type
                  </label>
                  <select
                    name="project_type_id"
                    value={formData.project_type_id}
                    onChange={handleInputChange}
                    required
                    className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent outline-none bg-white appearance-none cursor-pointer"
                    style={{
                      backgroundImage: `url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' fill='none' viewBox='0 0 24 24' stroke='%236b7280'%3E%3Cpath stroke-linecap='round' stroke-linejoin='round' stroke-width='2' d='M19 9l-7 7-7-7'%3E%3C/path%3E%3C/svg%3E")`,
                      backgroundRepeat: 'no-repeat',
                      backgroundPosition: 'right 0.5rem center',
                      backgroundSize: '1.5em 1.5em',
                      paddingRight: '2.5rem'
                    }}
                  >
                    <option value="">Select type</option>
                    {projectTypes.map((type) => (
                      <option key={type.id} value={type.id}>
                        {type.name}
                      </option>
                    ))}
                  </select>
                </div>

                {/* Priority Level */}
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">
                    Priority Level
                  </label>
                  <select
                    name="priority_level"
                    value={formData.priority_level}
                    onChange={handleInputChange}
                    required
                    className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent outline-none bg-white appearance-none cursor-pointer"
                    style={{
                      backgroundImage: `url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' fill='none' viewBox='0 0 24 24' stroke='%236b7280'%3E%3Cpath stroke-linecap='round' stroke-linejoin='round' stroke-width='2' d='M19 9l-7 7-7-7'%3E%3C/path%3E%3C/svg%3E")`,
                      backgroundRepeat: 'no-repeat',
                      backgroundPosition: 'right 0.5rem center',
                      backgroundSize: '1.5em 1.5em',
                      paddingRight: '2.5rem'
                    }}
                  >
                    {priorityLevels.map((level) => (
                      <option key={level.value} value={level.value}>
                        {level.label}
                      </option>
                    ))}
                  </select>
                </div>
              </div>

              {/* Start Date and Expected Completion */}
              <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
                {/* Start Date */}
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">
                    Start Date
                  </label>
                  <div className="relative">
                    <input
                      type="date"
                      name="start_date"
                      value={formData.start_date}
                      onChange={handleInputChange}
                      className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent outline-none"
                    />
                    <Calendar className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400" size={18} />
                  </div>
                </div>

                {/* Expected Completion */}
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">
                    Expected Completion
                  </label>
                  <div className="relative">
                    <input
                      type="date"
                      name="expected_completion"
                      value={formData.expected_completion}
                      onChange={handleInputChange}
                      min={formData.start_date || undefined}
                      className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent outline-none"
                    />
                    <Calendar className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400" size={18} />
                  </div>
                </div>
              </div>
            </div>
          </div>

          {/* Action Buttons */}
          <div className="flex justify-end gap-3 pt-4 border-t border-gray-200">
            <button
              type="button"
              onClick={() => {
                resetForm();
                onClose();
              }}
              className="px-6 py-2 border border-gray-300 rounded-lg text-gray-700 font-medium hover:bg-gray-50 transition-colors"
            >
              Cancel
            </button>
            <button
              type="submit"
              className="px-6 py-2 bg-green-600 text-white rounded-lg font-medium hover:bg-green-700 transition-colors"
            >
              Submit Project
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default ProjectCreateModal;