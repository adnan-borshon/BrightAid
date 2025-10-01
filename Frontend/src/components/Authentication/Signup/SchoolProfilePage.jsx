import { useState, useEffect } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import Sidebar from "./SideBar.jsx";

const SchoolProfilePage = () => {
  const location = useLocation();
  const [formData, setFormData] = useState({
    officialContact: '',
    principalName: '',
    fullName: '',
    contactNumber: '',
    registrationNumber: '',
    schoolType: 'Primary',
    country: '',
    detailedAddress: '',
    upazila: '',
    district: '',
    division: '',
    postcode: ''
  });

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [userInfo, setUserInfo] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    // Retrieve user info from session storage
    const savedUserInfo = sessionStorage.getItem("userInfo");
    if (savedUserInfo) {
      const parsed = JSON.parse(savedUserInfo);
      setUserInfo(parsed);
      console.log("Retrieved user info:", parsed);
    } else {
      // If no user info, redirect back to registration
      setError("Please complete registration first");
      setTimeout(() => navigate("/signup"), 2000);
    }
  }, [navigate]);

  const handleInputChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");

    if (!userInfo?.userId) {
      setError("User ID not found. Please register again.");
      return;
    }

    // Prepare payload according to API schema
    const payload = {
      userId: userInfo.userId,
      schoolName: formData.fullName,
      registrationNumber: formData.registrationNumber,
      schoolType: formData.schoolType.toUpperCase().replace(' ', '_'),
      address: formData.detailedAddress,
      divisionId: parseInt(formData.division) || 0, // You'll need to map division name to ID
      districtId: parseInt(formData.district) || 28, // You'll need to map district name to ID
      upazilaId: parseInt(formData.upazila) || 0,   // You'll need to map upazila name to ID
      latitude: 0,
      longitude: 0,
      verificationStatus: "PENDING",
      status: "ACTIVE"
    };

    try {
      setLoading(true);
      const token = sessionStorage.getItem("authToken");
      console.log("Token:", token);
      
      const res = await fetch("http://localhost:8081/api/schools", {
        method: "POST",
        headers: { 
          "Content-Type": "application/json",
          // ...(token && { "Authorization": `Bearer ${token}` })
        },
        body: JSON.stringify(payload),
      });
      console.log("Response:", res)
      if (!res.ok) {
        const errData = await res.json();
        throw new Error(errData.message || "Failed to create school profile");
      }

      const data = await res.json();
      console.log("School profile created:", data);

      // Save school info to session storage
      sessionStorage.setItem("schoolInfo", JSON.stringify(data));

      // Navigate to document verification
      navigate("/document-verification");

    } catch (err) {
      setError(err.message);
      console.error("Error creating school profile:", err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gray-50 flex">
      <Sidebar currentPath={location.pathname} />
      <div className="flex-1 flex items-center justify-center p-8">
        <div className="w-full max-w-2xl">
      <div className="text-center mb-8">
        <div className="flex items-center justify-center mb-6">
          <div className="w-8 h-8 bg-green-600 rounded-full flex items-center justify-center mr-3">
            <div className="w-4 h-4 bg-white rounded-full"></div>
          </div>
          <span className="text-2xl font-bold text-gray-900">BrightAid <span className="font-light">Institute</span></span>
        </div>
        
        <h2 className="text-2xl font-bold text-gray-900 mb-2">School Profile Setup</h2>
        <p className="text-gray-600 text-sm">
          Please provide detailed information about your school. This information will be visible to donors and NGOs.
        </p>
        {userInfo && (
          <p className="text-xs text-green-600 mt-2">
            Setting up profile for: {userInfo.username} ({userInfo.email})
          </p>
        )}
      </div>
      
      <form onSubmit={handleSubmit} className="bg-white rounded-2xl shadow-lg p-8">
        <div className="grid grid-cols-2 gap-6">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Official Contact
            </label>
            <input
              type="text"
              name="officialContact"
              value={formData.officialContact}
              onChange={handleInputChange}
              placeholder="Bangabir Principal Monirul"
              className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent outline-none"
            />
          </div>
          
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Contact Number
            </label>
            <input
              type="text"
              name="contactNumber"
              value={formData.contactNumber}
              onChange={handleInputChange}
              placeholder="+880"
              className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent outline-none"
            />
          </div>
          
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Full School Name*
            </label>
            <input
              type="text"
              name="fullName"
              value={formData.fullName}
              onChange={handleInputChange}
              placeholder="Jagannath Adhimik Biddalaya"
              className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent outline-none"
              required
            />
          </div>
          
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Registration Number*
            </label>
            <input
              type="text"
              name="registrationNumber"
              value={formData.registrationNumber}
              onChange={handleInputChange}
              placeholder="registration"
              className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent outline-none"
              required
            />
          </div>
          
          <div className="col-span-2">
            <label className="block text-sm font-medium text-gray-700 mb-2">
              School Type*
            </label>
            <div className="flex space-x-2">
              {['Primary', 'Secondary', 'High School', 'Madrassa'].map((type) => (
                <button
                  key={type}
                  type="button"
                  onClick={() => setFormData({...formData, schoolType: type})}
                  className={`px-4 py-2 rounded-lg border text-sm font-medium ${
                    formData.schoolType === type
                      ? 'bg-green-600 text-white border-green-600'
                      : 'bg-white text-gray-700 border-gray-300 hover:border-green-300'
                  }`}
                >
                  {type}
                </button>
              ))}
            </div>
          </div>
          
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Country*
            </label>
            <select
              name="country"
              value={formData.country}
              onChange={handleInputChange}
              className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent outline-none"
              required
            >
              <option value="">Select</option>
              <option value="Bangladesh">Bangladesh</option>
            </select>
          </div>
          
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Detailed Address*
            </label>
            <input
              type="text"
              name="detailedAddress"
              value={formData.detailedAddress}
              onChange={handleInputChange}
              className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent outline-none"
              required
            />
          </div>
          
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Upazila*
            </label>
            <select
              name="upazila"
              value={formData.upazila}
              onChange={handleInputChange}
              className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent outline-none"
              required
            >
              <option value="">Select</option>
              <option value="19">Dhaka</option>
            </select>
          </div>
          
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              District*
            </label>
            <select
              name="district"
              value={formData.district}
              onChange={handleInputChange}
              className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent outline-none"
              required
            >
              <option value="">Select</option>
              <option value="26">Dhaka</option>
            </select>
          </div>
          
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Division*
            </label>
            <select
              name="division"
              value={formData.division}
              onChange={handleInputChange}
              className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent outline-none"
              required
            >
              <option value="">Select</option>
              <option value="1">Dhaka</option>
            </select>
          </div>
          
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Postcode
            </label>
            <input
              type="text"
              name="postcode"
              value={formData.postcode}
              onChange={handleInputChange}
              className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-transparent outline-none"
            />
          </div>
        </div>

        {error && (
          <p className="text-red-500 text-sm mt-4">{error}</p>
        )}
        
        <button
          type="submit"
          disabled={loading}
          className="w-full bg-green-600 text-white py-3 px-4 rounded-lg font-medium hover:bg-green-700 transition-colors mt-6 disabled:bg-gray-400"
        >
          {loading ? "Saving..." : "Continue"}
        </button>
      </form>
        </div>
      </div>
    </div>
  );
};

export default SchoolProfilePage;