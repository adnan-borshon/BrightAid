import React, { useState, useEffect } from 'react';
import { useNavigate, useParams, useLocation } from 'react-router-dom';
import { Home, Users, Briefcase, FileText, CreditCard, HelpCircle, Settings, Search, LogOut, X, Mail, Phone, MapPin, Calendar } from 'lucide-react';
import { useApp } from '../context/AppContext';
import UserProfileModal from './Modal/UserProfileModal';



export default function DashSidebar() {
  const { schoolData, studentsData, projectsData, API_BASE_URL } = useApp();
  const navigate = useNavigate();
  const { schoolId } = useParams();
  const location = useLocation();
  
  const [userData, setUserData] = useState(null);
  const [profileData, setProfileData] = useState(null);
  const [isProfileModalOpen, setIsProfileModalOpen] = useState(false);

  const navItems = [
    { name: 'Dashboard', icon: Home, badge: null, path: `/dashboard/${schoolId}` },
    { name: 'Students', icon: Users, badge: studentsData?.length?.toString() || '0', path: `/students/${schoolId}` },
    { name: 'Projects', icon: Briefcase, badge: projectsData?.length?.toString() || '0', path: `/projects/${schoolId}` },
    { name: 'Reporting', icon: FileText, badge: 'Pending', badgeColor: 'text-red-600', path: `/reporting/${schoolId}` },
    { name: 'Account', icon: CreditCard, badge: null, path: `/account/${schoolId}` },
    { name: 'Support', icon: HelpCircle, badge: null, path: `/support/${schoolId}` }, 
    { name: 'Settings', icon: Settings, badge: null, path: `/settings/${schoolId}` },
  ];

  // Fetch user data on component mount
  useEffect(() => {
    const fetchUserData = async () => {
      try {
        const authData = localStorage.getItem('authData');
        if (authData) {
          const parsed = JSON.parse(authData);
          setUserData(parsed.user);

          // Fetch user profile data using userId endpoint
          const userId = parsed.user.userId;
          try {
            const profileResponse = await fetch(`${API_BASE_URL}/user-profiles/user/${userId}`);
            if (profileResponse.ok) {
              const userProfile = await profileResponse.json();
              setProfileData(userProfile);
            } else {
              console.warn('User profile not found - using basic user data');
              // Use basic user data as fallback
              setProfileData({
                fullName: parsed.user.username,
                status: 'ACTIVE'
              });
            }
          } catch (profileError) {
            console.warn('Failed to fetch user profile:', profileError);
            // Use basic user data as fallback
            setProfileData({
              fullName: parsed.user.username,
              status: 'ACTIVE'
            });
          }
        }
      } catch (error) {
        console.error('Error fetching user data:', error);
      }
    };

    fetchUserData();
  }, [API_BASE_URL]);

  const handleLogout = () => {
    // Clear all auth data
    localStorage.removeItem('authData');
    localStorage.removeItem('userId');
    localStorage.removeItem('donorId');
    
    // Navigate to login page
    navigate('/login');
  };

  const getActiveNav = () => {
    const path = location.pathname;
    if (path.includes('/students/')) return 'Students';
    if (path.includes('/projects/')) return 'Projects';
    if (path.includes('/reporting/')) return 'Reporting';
    if (path.includes('/account/')) return 'Account';
    if (path.includes('/support/')) return 'Support';
    if (path.includes('/settings/')) return 'Settings';
    return 'Dashboard';
  };

  const activeNav = getActiveNav();

  return (
    <>
      <div className="w-64 bg-gradient-to-b from-green-50 to-green-100 p-6 flex flex-col">
        <div className="flex items-center gap-2 mb-8">
    
          <div>
          <img src="/logo_institute.svg" alt="BrightAid Logo" className="h-7" />
          </div>
        </div>

        {/* User Profile Section */}
        <div className="flex items-center gap-3 mb-6 bg-white rounded-lg p-3">
          <div 
            className="w-10 h-10 bg-orange-200 rounded-full flex items-center justify-center cursor-pointer hover:bg-orange-300 transition-colors"
            onClick={() => setIsProfileModalOpen(true)}
          >
            <span className="text-lg font-bold text-orange-600">
              {profileData?.fullName?.charAt(0)?.toUpperCase() || userData?.username?.charAt(0)?.toUpperCase() || '?'}
            </span>
          </div>
          <div 
            className="flex-1 cursor-pointer"
            onClick={() => setIsProfileModalOpen(true)}
          >
            <div className="text-sm font-semibold text-gray-800 truncate">
              {profileData?.fullName || userData?.username || 'Loading...'}
            </div>
            <div className="text-xs text-gray-500">
              {userData?.userType || 'User'}
            </div>
          </div>
          <button
            onClick={handleLogout}
            className="p-1 hover:bg-red-50 rounded transition-colors group"
            title="Logout"
          >
            <LogOut className="w-4 h-4 text-gray-400 group-hover:text-red-600" />
          </button>
        </div>

        <div className="mb-6 relative">
          <Search className="w-4 h-4 absolute left-3 top-3 text-gray-400" />
          <input 
            type="text" 
            placeholder="Search" 
            className="w-full pl-10 pr-3 py-2 bg-white rounded-lg text-sm border-none focus:outline-none focus:ring-2 focus:ring-green-300"
          />
        </div>

        <nav className="flex-1 space-y-1">
          {navItems.map((item) => {
            const Icon = item.icon;
            const isActive = activeNav === item.name;

            if (item.name === "Support") {
              return (
                <div key={item.name} className="border-t border-gray-300 pt-6 mt-6 space-y-1">
                  <button
                    onClick={() => navigate(item.path)}
                    className={`w-full flex items-center gap-3 px-3 py-2.5 rounded-lg text-sm transition-colors ${
                      isActive
                        ? 'bg-white text-green-700 shadow-sm'
                        : 'text-gray-700 hover:bg-gray-50 hover:text-green-700'
                    }`}
                  >
                    <Icon className="w-4 h-4" />
                    <span className="flex-1 text-left">{item.name}</span>
                  </button>
                </div>
              );
            }

            return (
              <button
                key={item.name}
                onClick={() => navigate(item.path)}
                className={`w-full flex items-center gap-3 px-3 py-2.5 rounded-lg text-sm transition-colors ${
                  isActive
                    ? 'bg-white text-green-700 shadow-sm'
                    : 'text-gray-700 hover:bg-gray-50 hover:text-green-700'
                }`}
              >
                <Icon className="w-4 h-4" />
                <span className="flex-1 text-left">{item.name}</span>
                {item.badge && (
                  <span
                    className={`text-[10px] ${
                      item.badgeColor
                        ? `${item.badgeColor} ml-10 px-2 py-1 bg-red-50 rounded-xl`
                        : 'text-gray-500'
                    }`}
                  >
                    {item.badge}
                  </span>
                )}
              </button>
            );
          })}
        </nav>
      </div>

      {/* User Profile Modal */}
      <UserProfileModal
        isOpen={isProfileModalOpen}
        onClose={() => setIsProfileModalOpen(false)}
        userData={userData}
        profileData={profileData}
      />
    </>
  );
}