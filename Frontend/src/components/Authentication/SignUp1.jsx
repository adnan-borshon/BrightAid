import React, { useState } from 'react'
import { Link, useNavigate } from "react-router-dom";
import StudentBg from "../StudentBg.jsx"


function SignUp1() {

  const [formData, setFormData] = useState({
    donorType: 'Individual',
    name: '',
    email: '',
    password: ''
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
      // Determine user type based on donor type
      const userType = formData.donorType === 'Individual' ? 'DONOR' : 'NGO';
      
      const response = await fetch('http://localhost:8081/api/auth/register', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          username: formData.name,
          email: formData.email,
          passwordHash: formData.password, // Backend will hash this
          userType: userType,
          isActive: true
        })
      });

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'Registration failed');
      }

      const data = await response.json();
      
      // Save user data to localStorage for next steps
      const userData = {
        userId: data.userId,
        email: data.email,
        username: data.username,
        userType: data.userType,
        donorType: formData.donorType
      };
      console.log("Registered user data:", userData);
      
      localStorage.setItem('signupData', JSON.stringify(userData));
      
      // Navigate based on user type
      if (userType === 'DONOR') {
        // Individual donors can go directly to login or dashboard
        navigate('/login');
      } else if (userType === 'NGO') {
        // NGOs might need additional profile setup
        navigate('/login'); // Or create NGO profile page
      }
      
    } catch (error) {
      console.error('Registration error:', error);
      setError(error.message || 'Registration failed. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  const handleLogin = () => {
    navigate('/login');
  };

  const handleInstitutionSignup = () => {
    navigate('/signup'); // Navigate to school signup
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
            Join BrightAid Today
          </h1>
          <p className="text-gray-600 text-sm">
            Help transform education in rural Bangladesh
          </p>
        </div>

        {/* Form */}
        <div className="space-y-6">
          {/* Error Message */}
          {error && (
            <div className="p-3 bg-red-50 border border-red-200 rounded-md text-sm text-red-600">
              {error}
            </div>
          )}

          {/* Donor Type */}

        
           <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Donor Type*
            </label>
            <div className="flex border border-gray-300 rounded-md overflow-hidden">
              <button
                type="button"
                onClick={() => handleDonorType('Individual')}
                className={`flex-1 px-4 py-2 text-sm font-medium transition-colors ${
                  formData.donorType === 'Individual'
                    ? 'bg-green-600 text-white'
                    : '!bg-white !text-gray-700 !hover:bg-gray-50'
                }`}
              >
                Individual
              </button>
              <button
                type="button"
                onClick={() => handleDonorType('Organization')}
                className={`flex-1 px-4 py-2 text-sm font-medium border-l border-gray-300 transition-colors ${
                  formData.donorType === 'Organization'
                    ? 'bg-green-600 text-white'
                    : '!bg-white !text-gray-700 !hover:bg-gray-50'
                }`}
              >
                Organization
              </button>
            </div>
          </div>
       

          {/* Name */}
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              {formData.donorType === 'Individual' ? 'Full Name*' : 'Organization Name*'}
            </label>
            <input
              type="text"
              name="name"
              value={formData.name}
              onChange={handleInputChange}
              placeholder="Enter your name"
              className="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-green-500 focus:border-green-500"
              required
            />
          </div>

          {/* Email */}
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
           {formData.donorType === 'Individual' ? 'Email*': 'Organization Email*'}
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
              placeholder="Create a password"
              className="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-green-500 focus:border-green-500"
              required
            />
            <p className="text-xs text-gray-500 mt-1">
              Must be at least 8 characters.
            </p>
          </div>

          {/* Get Started Button */}
          <button
            type="submit"
            onClick={handleSubmit}
            disabled={loading}
            className="w-full cursor-pointer bg-green-600 hover:bg-green-700 disabled:bg-gray-400 text-white font-medium py-2 px-4 rounded-md transition duration-200 focus:outline-none focus:ring-2 focus:ring-green-500 focus:ring-offset-2"
          >
            {loading ? 'Creating account...' : 'Get started'}
          </button>
        </div>

        {/* Login Link */}
        <div className="mt-6 text-center">
          <span className="text-sm text-gray-600">Already have an account? </span>
          <Link
            to="/login"
            className="text-sm !bg-transparent text-[#0E792E] hover:!text-green-600 font-medium"
          >
            Log in
          </Link>
        </div>

        {/* Institution Signup */}
        <div className="mt-4">
          <Link
            to="/signup"
            className="w-full  border-2 rounded-lg px-2 py-1 bg-white  focus:outline-none focus:ring-2 focus:ring-green-500 focus:ring-offset-2 flex items-center justify-center"
          >
            <svg className="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4" />
            </svg>
            Sign up as Institution
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

export default SignUp1