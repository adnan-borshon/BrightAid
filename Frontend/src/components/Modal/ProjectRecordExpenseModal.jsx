import { useState } from "react";
import { X, Upload, Receipt } from "lucide-react";

const ProjectRecordExpenseModal = ({ isOpen, onClose, onSubmit, project }) => {
  const [formData, setFormData] = useState({
    donationId: "",
    amountUsed: "",
    specificPurpose: "",
    detailedDescription: "",
    vendorName: "",
    billInvoiceNumber: "",
    utilizationDate: new Date().toISOString().split('T')[0],
    receiptImage: null
  });

  const [imagePreview, setImagePreview] = useState(null);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleImageUpload = (e) => {
    const file = e.target.files[0];
    if (file && (file.type === "image/png" || file.type === "image/jpeg")) {
      setFormData(prev => ({ ...prev, receiptImage: file }));
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

    const submitData = {
      projectId: project.project_id,
      donationId: parseInt(formData.donationId),
      amountUsed: parseFloat(formData.amountUsed),
      specificPurpose: formData.specificPurpose,
      detailedDescription: formData.detailedDescription,
      vendorName: formData.vendorName,
      billInvoiceNumber: formData.billInvoiceNumber,
      utilizationDate: formData.utilizationDate,
      approvedBy: 1, // TODO: Get from auth context
      receiptImageUrl: null // TODO: Handle image upload separately
    };

    if (onSubmit) {
      await onSubmit(submitData);
    }

    resetForm();
    onClose();
  };

  const resetForm = () => {
    setFormData({
      donationId: "",
      amountUsed: "",
      specificPurpose: "",
      detailedDescription: "",
      vendorName: "",
      billInvoiceNumber: "",
      utilizationDate: new Date().toISOString().split('T')[0],
      receiptImage: null
    });
    setImagePreview(null);
  };

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 backdrop-blur-xs bg-opacity-50 flex items-center justify-center z-50 p-4">
      <div className="bg-white rounded-lg w-full max-w-2xl max-h-[90vh] overflow-y-auto">
        <div className="sticky top-0 bg-white border-b border-gray-200 px-6 py-4 flex items-center justify-between">
          <h2 className="text-2xl font-bold text-gray-900">Record Fund Utilization</h2>
          <button onClick={onClose} className="text-gray-400 hover:text-gray-600">
            <X size={24} />
          </button>
        </div>

        <form onSubmit={handleSubmit} className="p-6">
          <div className="mb-4">
            <h3 className="text-lg font-semibold text-gray-900 mb-2">
              Project: {project?.project_name}
            </h3>
          </div>

          <div className="space-y-4">
            <div className="grid grid-cols-2 gap-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Donation ID *
                </label>
                <input
                  type="number"
                  name="donationId"
                  value={formData.donationId}
                  onChange={handleInputChange}
                  required
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent outline-none"
                  placeholder="Enter donation ID"
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Amount Used (৳) *
                </label>
                <input
                  type="number"
                  name="amountUsed"
                  value={formData.amountUsed}
                  onChange={handleInputChange}
                  required
                  min="0.01"
                  step="0.01"
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent outline-none"
                  placeholder="0.00"
                />
              </div>
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Specific Purpose *
              </label>
              <input
                type="text"
                name="specificPurpose"
                value={formData.specificPurpose}
                onChange={handleInputChange}
                required
                className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent outline-none"
                placeholder="e.g., School supplies, Infrastructure repair"
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Detailed Description
              </label>
              <textarea
                name="detailedDescription"
                value={formData.detailedDescription}
                onChange={handleInputChange}
                rows={3}
                className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent outline-none"
                placeholder="Provide detailed description of the expense"
              />
            </div>

            <div className="grid grid-cols-2 gap-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Vendor Name
                </label>
                <input
                  type="text"
                  name="vendorName"
                  value={formData.vendorName}
                  onChange={handleInputChange}
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent outline-none"
                  placeholder="Vendor/Supplier name"
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Bill/Invoice Number
                </label>
                <input
                  type="text"
                  name="billInvoiceNumber"
                  value={formData.billInvoiceNumber}
                  onChange={handleInputChange}
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent outline-none"
                  placeholder="Invoice/Bill number"
                />
              </div>
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Utilization Date *
              </label>
              <input
                type="date"
                name="utilizationDate"
                value={formData.utilizationDate}
                onChange={handleInputChange}
                required
                className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent outline-none"
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Receipt/Bill Image
              </label>
              
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div className="border rounded-lg h-32 flex items-center justify-center bg-gray-100">
                  {imagePreview ? (
                    <img
                      src={imagePreview}
                      alt="Receipt preview"
                      className="w-full h-full object-cover rounded-lg"
                    />
                  ) : (
                    <span className="text-gray-400 text-sm">No receipt image</span>
                  )}
                </div>

                <div>
                  <input
                    type="file"
                    id="receiptUpload"
                    accept="image/png, image/jpeg"
                    onChange={handleImageUpload}
                    className="hidden"
                  />
                  <label
                    htmlFor="receiptUpload"
                    className="flex flex-col items-center justify-center border-2 border-dashed border-gray-300 rounded-lg p-4 cursor-pointer hover:border-green-500 transition-colors h-32"
                  >
                    <Receipt className="text-gray-400 mb-2" size={24} />
                    <p className="text-sm text-green-600 font-medium">Upload Receipt</p>
                    <p className="text-xs text-gray-500">PNG or JPEG</p>
                  </label>
                </div>
              </div>
            </div>
          </div>

          <div className="flex gap-3 mt-6">
            <button
              type="button"
              onClick={onClose}
              className="flex-1 px-4 py-2 border border-gray-300 text-gray-700 rounded-lg hover:bg-gray-50"
            >
              Cancel
            </button>
            <button
              type="submit"
              className="flex-1 px-4 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700"
            >
              Record Expense
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default ProjectRecordExpenseModal;