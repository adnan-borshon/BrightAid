import React, { useState } from 'react'
import { Link, useNavigate } from "react-router-dom";
import StudentBg from "../StudentBg.jsx"


function Login() {

  const [formData, setFormData] = useState({
    email: '',
    password: '',
    rememberMe: false
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleDonorType = (type) => {
  setFormData(prev => ({
    ...prev,
    donorType: type
  }));
  };
const handleInputChange = (e) => { const { name, value } = e.target; setFormData(prev => ({ ...prev, [name]: value })); };
  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      const response = await fetch('http://localhost:8081/api/auth/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          email: formData.email,
          password: formData.password
        })
      });

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'Login failed');
      }

      const data = await response.json();
      
      // Save to localStorage
      const authData = {
        token: data.token,
        user: data.user,
        loginTime: new Date().getTime(),
        rememberMe: formData.rememberMe
      };
      
      localStorage.setItem('authData', JSON.stringify(authData));
      
      // Navigate based on user type
      if (data.user.userType === 'SCHOOL') {
        // Find school ID and navigate to dashboard
        try {
          const schoolResponse = await fetch('http://localhost:8081/api/schools');
          if (schoolResponse.ok) {
            const schools = await schoolResponse.json();
            const userSchool = schools.find(school => 
              (school.userId || school.user_id) === data.user.userId
            );
            if (userSchool) {
              navigate(`/dashboard/${userSchool.schoolId || userSchool.school_id}`);
            } else {
              navigate('/school-profile'); // If no school found, complete profile
            }
          } else {
            navigate('/school-profile'); // If can't fetch schools, go to profile
          }
        } catch (err) {
          console.error('Error fetching school data:', err);
          navigate('/school-profile');
        }
      } else if (data.user.userType === 'DONOR') {
        // Find donor ID and navigate to dashboard
        try {
          const donorResponse = await fetch('http://localhost:8081/api/donors');
          if (donorResponse.ok) {
            const donors = await donorResponse.json();
            const userDonor = donors.find(donor => 
              (donor.userId || donor.user_id) === data.user.userId
            );
            if (userDonor) {
              navigate(`/donor-dashboard/${userDonor.donorId || userDonor.donor_id}`);
            } else {
              navigate('/donor-profile'); // If no donor found, complete profile
            }
          } else {
            navigate('/donor-profile'); // If can't fetch donors, go to profile
          }
        } catch (err) {
          console.error('Error fetching donor data:', err);
          navigate('/donor-profile');
        }
      } else if (data.user.userType === 'NGO') {
        // Find NGO ID and navigate to dashboard
        try {
          const ngoResponse = await fetch('http://localhost:8081/api/ngos');
          if (ngoResponse.ok) {
            const ngos = await ngoResponse.json();
            const userNgo = ngos.find(ngo => 
              (ngo.userId || ngo.user_id) === data.user.userId
            );
            if (userNgo) {
              navigate(`/ngo-dashboard/${userNgo.ngoId || userNgo.ngo_id}`);
            } else {
              navigate('/ngo-profile'); // If no NGO found, complete profile
            }
          } else {
            navigate('/ngo-profile'); // If can't fetch NGOs, go to profile
          }
        } catch (err) {
          console.error('Error fetching NGO data:', err);
          navigate('/ngo-profile');
        }
      } else if (data.user.userType === 'ADMIN') {
        navigate(`/admin-dashboard/${data.user.userId}`)
      } else {
        navigate('/'); // Fallback to landing page
      }
      
    } catch (error) {
      console.error('Login error:', error);
      setError(error.message || 'Login failed. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  const handleLogin = () => {
    console.log('Login clicked');
  };

  const handleInstitutionSignup = () => {
    console.log('Sign up as Institution clicked');
  };

 
  return (
        <section className="relative bg-[#E8FAED]">
      <div className="absolute  mx-20 my-7">
        <img src="logo.svg" alt="" className="h-9"/>
      </div>
      <div className="grid lg:grid-cols-12 ">
        {/* Left Column */}

 
    <div className="flex flex-col col-span-12 lg:col-span-6 justify-center px-6 sm:px-8 lg:px-16 py-12">
      <div className="w-full mt-20 max-w-md mx-auto">
        {/* Header */}
        <div className="mb-8">
          <h1 className="text-3xl font-bold text-gray-900 mb-2">
Welcome Back to BrightAid
          </h1>
          <p className="text-gray-600 text-sm">
          Log in to access your dashboard and help connect donors with schools in need
          </p>
        </div>

        {/* Form */}
        <div className="space-y-6">
          {/* Donor Type */}

          {/* Error Message */}
          {error && (
            <div className="p-3 bg-red-50 border border-red-200 rounded-md text-sm text-red-600">
              {error}
            </div>
          )}

          {/* Email */}
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Email*
            </label>
            <input
              type="email"
              name="email"
              value={formData.email}
              onChange={handleInputChange}
              placeholder="Enter your email"
              className="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-green-500 focus:border-green-500"
              required
            />
          </div>

          {/* Password */}
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Password*
            </label>
            <input
              type="password"
              name="password"
              value={formData.password}
              onChange={handleInputChange}
              placeholder="Enter your password"
              className="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-green-500 focus:border-green-500"
              required
            />
          
          </div>

            {/* Login Button */}
          <button
            type="submit"
            onClick={handleSubmit}
            disabled={loading}
            className="w-full cursor-pointer bg-green-600 hover:bg-green-700 disabled:bg-gray-400 text-white font-medium py-2 px-4 rounded-md transition duration-200 focus:outline-none focus:ring-2 focus:ring-green-500 focus:ring-offset-2"
          >
            {loading ? 'Logging in...' : 'Log In'}
          </button>
        </div>

        {/* Remember Me & Forgot Password */}
        <div className="mt-4 flex items-center justify-between">
          <label className="flex items-center">
            <input
              type="checkbox"
              name="rememberMe"
              checked={formData.rememberMe}
              onChange={(e) => setFormData(prev => ({ ...prev, rememberMe: e.target.checked }))}
              className="h-4 w-4 text-green-600 focus:ring-green-500 border-gray-300 rounded"
            />
            <span className="ml-2 text-sm text-gray-600">Remember for 30 days</span>
          </label>
          <button
            type="button"
            className="text-sm secondary !border-0 !p-0 !bg-transparent hover:!text-green-600"
            onClick={() => console.log('Forgot password clicked')}
          >
            Forgot password?
          </button>
        </div>

        <div className="mt-6 text-center">
          <span className="text-sm text-gray-600">Don't have an account? </span>
          <Link
            to="/signup1"
            className="text-sm !bg-transparent text-[#0E792E] hover:!text-green-600 font-medium"
          >
            SignUp
          </Link>
        </div>

    
      </div>
    </div>

        {/* Right Column with Green Burst */}
      <StudentBg/>
      </div>
    </section>
  )
}

export default Login