import { useState } from "react";
import { X, Upload, Calendar } from "lucide-react";

const StudentEnrollmentModal = ({ isOpen, onClose, onSubmit }) => {
  const [formData, setFormData] = useState({
    student_name: "",
    student_id_number: "",
    gender: "Male",
    date_of_birth: "",
    class_level: "1",
    father_name: "",
    father_alive: true,
    father_occupation: "",
    mother_name: "",
    mother_alive: true,
    mother_occupation: "",
    guardian_phone: "",
    address: "",
    family_monthly_income: "",
    profile_image: null,
  });

  const [imagePreview, setImagePreview] = useState(null);
  const [showDatePicker, setShowDatePicker] = useState(false);

  const classLevels = [
    "1",
    "2",
    "3",
    "4",
    "5",
    "6",
    "7",
    "8",
    "9",
    "10",
    "11",
    "12",
  ];

  const lifeStatus = [
    { value: true, label: "Alive" },
    { value: false, label: "Deceased" },
  ];

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleImageUpload = (e) => {
    const file = e.target.files[0];
    if (file && (file.type === "image/png" || file.type === "image/jpeg")) {
      setFormData((prev) => ({ ...prev, profile_image: file }));
      const reader = new FileReader();
      reader.onloadend = () => {
        setImagePreview(reader.result);
      };
      reader.readAsDataURL(file);
    } else {
      alert("Please upload only PNG or JPEG images");
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    // Prepare data for backend
    const submitData = new FormData();

    // Append all form fields
    Object.keys(formData).forEach((key) => {
      if (formData[key] !== null && formData[key] !== "") {
        if (key === "profile_image" && formData[key]) {
          submitData.append(key, formData[key]);
        } else if (key === "family_monthly_income") {
          submitData.append(key, formData[key] || null);
        } else {
          submitData.append(key, formData[key]);
        }
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
      student_name: "",
      student_id_number: "",
      gender: "Male",
      date_of_birth: "",
      class_level: "1",
      father_name: "",
      father_alive: true,
      father_occupation: "",
      mother_name: "",
      mother_alive: true,
      mother_occupation: "",
      guardian_phone: "",
      address: "",
      family_monthly_income: "",
      profile_image: null,
    });
    setImagePreview(null);
  };

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 backdrop-blur-xs bg-opacity-50 flex items-center justify-center z-50 p-4">
      <div className="bg-white rounded-lg w-full max-w-2xl max-h-[90vh] overflow-y-auto">
        {/* Header */}
        <div className="sticky top-0 bg-white border-b border-gray-200 px-6 py-4 flex items-center justify-between">
          <h2 className="text-2xl font-bold text-gray-900">Add New Student</h2>
          <button
            onClick={onClose}
            className="text-gray-400 hover:text-gray-600 transition-colors"
          >
            <X size={24} />
          </button>
        </div>

        <form onSubmit={handleSubmit} className="p-6">
          {/* Personal Info Section */}
          <div className="mb-6">
            <h3 className="text-lg font-semibold text-gray-900 mb-4">
              Personal Info
            </h3>

      <div className="grid grid-cols-1 md:grid-cols-6 gap-6 mb-6">
  {/* Left side: Always show image or placeholder */}
  <div className="md:col-span-2">
    <div className="relative">
      <div className="border rounded-lg w-full h-40 flex items-center justify-center bg-gray-100">
        {imagePreview ? (
          <img
            src={imagePreview}
            alt="Preview"
            className="w-full h-full object-cover rounded-lg"
          />
        ) : (
          <span className="text-gray-400 text-sm">No image</span>
        )}
      </div>
      <input
        type="file"
        id="imageUpload"
        accept="image/png, image/jpeg"
        onChange={handleImageUpload}
        className="hidden"
      />
    </div>
  </div>

  {/* Right side: Upload section always visible */}
  <div className="md:col-span-4">
    <label
      htmlFor="imageUpload"
      className="flex flex-col items-center justify-center border-2 border-dashed border-gray-300 rounded-lg p-4 cursor-pointer hover:border-green-500 transition-colors h-40"
    >
      <Upload className="text-gray-400 mb-2" size={32} />
      <p className="text-sm text-green-600 font-medium">Click to upload</p>
      <p className="text-xs text-gray-500 mt-1">PNG or JPEG</p>
      <p className="text-xs text-gray-400">(max. 800×400px)</p>
    </label>
  </div>
</div>

          </div>
          <div className="md:col-span-2 space-y-4">
            {/* Full Name */}
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Full Name *
              </label>
              <input
                type="text"
                name="student_name"
                value={formData.student_name}
                onChange={handleInputChange}
                required
                className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent outline-none"
                placeholder="Enter full name"
              />
            </div>

            {/* Student ID Number */}
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Student ID Number *
              </label>
              <input
                type="text"
                name="student_id_number"
                value={formData.student_id_number}
                onChange={handleInputChange}
                required
                className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent outline-none"
                placeholder="Enter student ID"
              />
            </div>

            {/* Gender, Class, Date of Birth */}
            <div className="grid grid-cols-3 gap-3">
              {/* Gender */}
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Gender *
                </label>
                <div className="flex  rounded-lg overflow-hidden border border-gray-300">
                  <button
                    type="button"
                    onClick={() =>
                      setFormData((prev) => ({ ...prev, gender: "Male" }))
                    }
                    className={`flex-1 py-2 text-sm font-medium transition-colors ${
                      formData.gender === "Male"
                        ? "bg-green-600 text-white "
                        : "secondary !border-0 hover:!bg-gray-50 hover:!text-green-600"
                    }`}
                  >
                    Male
                  </button>
                  <button
                    type="button"
                    onClick={() =>
                      setFormData((prev) => ({ ...prev, gender: "Female" }))
                    }
                    className={`flex-1 py-2 text-sm font-medium transition-colors ${
                      formData.gender === "Female"
                        ? "bg-green-600 text-white"
                        : "secondary !border-0 hover:!bg-gray-50 hover:!text-green-600"
                    }`}
                  >
                    Female
                  </button>
                </div>
              </div>

              {/* Class/Grade */}
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Class *
                </label>
                <select
                  name="class_level"
                  value={formData.class_level}
                  onChange={handleInputChange}
                  required
                  className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent outline-none bg-white"
                >
                  {classLevels.map((level) => (
                    <option key={level} value={level}>
                      {level}
                    </option>
                  ))}
                </select>
              </div>

              {/* Date of Birth */}
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Date of Birth *
                </label>
                <div className="relative">
                  <input
                    type="date"
                    name="date_of_birth"
                    value={formData.date_of_birth}
                    onChange={handleInputChange}
                    required
                    max={new Date().toISOString().split("T")[0]}
                    className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent outline-none"
                  />
                </div>
              </div>
            </div>
          </div>
          {/* Family & Contact Section */}
          <div className="mb-6">
            <h3 className="text-lg font-semibold text-gray-900 mb-4">
              Family & Contact
            </h3>

            <div className="space-y-4">
              {/* Father's Info */}
              <div className="grid grid-cols-1 md:grid-cols-3 gap-3">
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">
                    Father's Name
                  </label>
                  <input
                    type="text"
                    name="father_name"
                    value={formData.father_name}
                    onChange={handleInputChange}
                    placeholder="Optional"
                    className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent outline-none"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">
                    Occupation
                  </label>
                  <input
                    type="text"
                    name="father_occupation"
                    value={formData.father_occupation}
                    onChange={handleInputChange}
                    placeholder="Optional"
                    className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent outline-none"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">
                    Life
                  </label>
                  <select
                    name="father_alive"
                    value={formData.father_alive}
                    onChange={(e) =>
                      setFormData((prev) => ({
                        ...prev,
                        father_alive: e.target.value === "true",
                      }))
                    }
                    className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent outline-none bg-white"
                  >
                    {lifeStatus.map((status) => (
                      <option key={status.value} value={status.value}>
                        {status.label}
                      </option>
                    ))}
                  </select>
                </div>
              </div>

              {/* Mother's Info */}
              <div className="grid grid-cols-1 md:grid-cols-3 gap-3">
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">
                    Mother's Name
                  </label>
                  <input
                    type="text"
                    name="mother_name"
                    value={formData.mother_name}
                    onChange={handleInputChange}
                    placeholder="Optional"
                    className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent outline-none"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">
                    Occupation
                  </label>
                  <input
                    type="text"
                    name="mother_occupation"
                    value={formData.mother_occupation}
                    onChange={handleInputChange}
                    placeholder="Optional"
                    className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent outline-none"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">
                    Life
                  </label>
                  <select
                    name="mother_alive"
                    value={formData.mother_alive}
                    onChange={(e) =>
                      setFormData((prev) => ({
                        ...prev,
                        mother_alive: e.target.value === "true",
                      }))
                    }
                    className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent outline-none bg-white"
                  >
                    {lifeStatus.map((status) => (
                      <option key={status.value} value={status.value}>
                        {status.label}
                      </option>
                    ))}
                  </select>
                </div>
              </div>

              {/* Family Income and Guardian Phone */}
              <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">
                    Family Income (Monthly)
                  </label>
                  <div className="relative">
                    <span className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-500">
                      ৳
                    </span>
                    <input
                      type="number"
                      name="family_monthly_income"
                      value={formData.family_monthly_income}
                      onChange={handleInputChange}
                      placeholder="20,000"
                      className="w-full pl-8 pr-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent outline-none"
                    />
                  </div>
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">
                    Guardian Phone Number *
                  </label>
                  <input
                    type="tel"
                    name="guardian_phone"
                    value={formData.guardian_phone}
                    onChange={handleInputChange}
                    required
                    placeholder="01712345678"
                    pattern="[0-9]{11}"
                    className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent outline-none"
                  />
                </div>
              </div>

              {/* Address */}
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Address
                </label>
                <textarea
                  name="address"
                  value={formData.address}
                  onChange={handleInputChange}
                  rows="2"
                  placeholder="Enter full address"
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent outline-none resize-none"
                />
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
              Enroll
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};
export default StudentEnrollmentModal;
