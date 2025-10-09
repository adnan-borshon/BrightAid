import React, { useState, useEffect } from 'react';
import { useNavigate, useParams, useLocation } from 'react-router-dom';
import { Home, Users, Briefcase, FileText, CreditCard, HelpCircle, Settings, LogOut, Heart, Trophy } from 'lucide-react';
import UserProfileModal from './Modal/UserProfileModal';

export default function NgoDashSidebar() {
  const navigate = useNavigate();
  const { ngoId } = useParams();
  const location = useLocation();
  
  const [userData, setUserData] = useState(null);
  const [profileData, setProfileData] = useState(null);
  const [isProfileModalOpen, setIsProfileModalOpen] = useState(false);

  const navItems = [
    { name: 'Home', icon: Home, badge: null, path: `/ngo-dashboard/${ngoId}` },
    { name: 'Projects', icon: Briefcase, badge: '0', path: `/ngo-projects/${ngoId}` },
    { name: 'Donations', icon: Heart, badge: null, path: `/ngo-donations/${ngoId}` },
    { name: 'Reporting', icon: FileText, badge: null, path: `/ngo-reporting/${ngoId}` },
    { name: 'Gamification', icon: Trophy, badge: null, path: `/ngo-gamification/${ngoId}` },
    { name: 'Account', icon: CreditCard, badge: null, path: `/ngo-account/${ngoId}` },
    { name: 'Support', icon: HelpCircle, badge: null, path: `/ngo-support/${ngoId}` }, 
    { name: 'Settings', icon: Settings, badge: null, path: `/ngo-settings/${ngoId}` },
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
            const profileResponse = await fetch(`http://localhost:8081/api/user-profiles/user/${userId}`);
            if (profileResponse.ok) {
              const userProfile = await profileResponse.json();
              setProfileData(userProfile);
            } else {
              console.warn('User profile not found - using basic user data');
              setProfileData({
                fullName: parsed.user.username,
                status: 'ACTIVE'
              });
            }
          } catch (profileError) {
            console.warn('Failed to fetch user profile:', profileError);
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
  }, []);

  const handleLogout = () => {
    localStorage.removeItem('authData');
    localStorage.removeItem('userId');
    localStorage.removeItem('ngoId');
    navigate('/login');
  };

  const getActiveNav = () => {
    const path = location.pathname;
    if (path.includes('/ngo-projects/')) return 'Projects';
    if (path.includes('/ngo-donations/')) return 'Donations';
    if (path.includes('/ngo-reporting/')) return 'Reporting';
    if (path.includes('/ngo-gamification/')) return 'Gamification';
    if (path.includes('/ngo-account/')) return 'Account';
    if (path.includes('/ngo-support/')) return 'Support';
    if (path.includes('/ngo-settings/')) return 'Settings';
    return 'Home';
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
            className="w-10 h-10 rounded-full cursor-pointer hover:opacity-80 transition-opacity flex-shrink-0 relative overflow-hidden"
            onClick={() => setIsProfileModalOpen(true)}
          >
            {profileData?.profileImageUrl ? (
              <img 
                src={`http://localhost:8081${profileData.profileImageUrl}`} 
                alt="Profile" 
                className="w-full h-full object-cover"
                onError={(e) => {
                  e.target.style.display = 'none';
                  e.target.nextSibling.style.display = 'flex';
                }}
              />
            ) : null}
            <div className={`w-full h-full bg-green-200 rounded-full flex items-center justify-center ${profileData?.profileImageUrl ? 'hidden' : ''}`}>
              <span className="text-lg font-bold text-green-600">
                {profileData?.fullName?.charAt(0)?.toUpperCase() || userData?.username?.charAt(0)?.toUpperCase() || '?'}
              </span>
            </div>
          </div>
          <div 
            className="flex-1 min-w-0 cursor-pointer"
            onClick={() => setIsProfileModalOpen(true)}
          >
            <div className="text-sm font-semibold text-gray-800 truncate">
              {profileData?.fullName || userData?.username || 'Loading...'}
            </div>
            <div className="text-xs text-gray-500">
              NGO
            </div>
          </div>
          <button
            onClick={handleLogout}
            className="p-1 hover:bg-red-50 rounded transition-colors group flex-shrink-0"
            title="Logout"
          >
            <LogOut className="w-4 h-4 text-gray-400 group-hover:text-red-600" />
          </button>
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
                  <span className="text-[10px] text-gray-500">
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