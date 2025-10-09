import React, { useState, useEffect } from 'react';
import { useNavigate, useParams, useLocation } from 'react-router-dom';
import { Home, Users, Briefcase, FileText, DollarSign, Trophy, LogOut } from 'lucide-react';
import { useDonor } from '../context/DonorContext';
import UserProfileModal from './Modal/UserProfileModal';

export default function DonorDashSidebar() {
  const { donationsData, projectsData } = useDonor();
  const navigate = useNavigate();
  const { id: userId } = useParams(); // URL param is now userId
  const location = useLocation();
  
  const [userData, setUserData] = useState(null);
  const [profileData, setProfileData] = useState(null);
  const [isProfileModalOpen, setIsProfileModalOpen] = useState(false);
  const navItems = [
    { name: 'Home', icon: Home, badge: null, path: `/donor-dashboard/${userId}` },
    { name: 'Projects', icon: Briefcase, badge: projectsData?.length?.toString() || '0', path: `/donor-projects/${userId}` },
    { name: 'Students', icon: Users, badge: '0', path: `/donor-students/${userId}` },
    { name: 'Donations', icon: DollarSign, badge: null, path: `/donor-donations/${userId}` },
    { name: 'Gamification', icon: Trophy, badge: null, path: `/donor-gamification/${userId}` },
    { name: 'Reporting', icon: FileText, badge: null, path: `/donor-reporting/${userId}` },
  ];

  const getActiveNav = () => {
    const path = location.pathname;
    if (path.includes('/donor-students/')) return 'Students';
    if (path.includes('/donor-projects/')) return 'Projects';
    if (path.includes('/donor-donations/')) return 'Donations';
    if (path.includes('/donor-gamification/')) return 'Gamification';
    if (path.includes('/donor-reporting/')) return 'Reporting';
    return 'Home';
  };

  const activeNav = getActiveNav();

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
    localStorage.removeItem('donorId');
    navigate('/login');
  };

  return (
    <>
    <div className="w-64 p-6 flex flex-col" style={{ backgroundColor: '#0E792E' }}>
      <div className="flex justify-center items-center gap-2 mb-2 ">
      
       
        <img src="/Logo_white.svg" alt="" className='h-8'/>
         
       
      </div>

      {/* User Profile Section */}
      <div className="flex items-center gap-3 mb-6 bg-green-700 bg-opacity-20 rounded-lg p-3">
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
          <div className={`w-full h-full bg-white rounded-full flex items-center justify-center ${profileData?.profileImageUrl ? 'hidden' : ''}`}>
            <span className="text-lg font-bold text-green-600">
              {profileData?.fullName?.charAt(0)?.toUpperCase() || userData?.username?.charAt(0)?.toUpperCase() || '?'}
            </span>
          </div>
        </div>
        <div 
          className="flex-1 min-w-0 cursor-pointer"
          onClick={() => setIsProfileModalOpen(true)}
        >
          <div className="text-sm font-semibold text-white truncate">
            {profileData?.fullName || userData?.username || 'Loading...'}
          </div>
          <div className="text-xs text-green-100">
            Donor
          </div>
        </div>
        <button
          onClick={handleLogout}
          className="p-1 hover:bg-white  rounded transition-colors group flex-shrink-0"
          title="Logout"
        >
          <LogOut className="w-4 h-4 text-green-100 group-hover:text-black" />
        </button>
      </div>


      <nav className="flex-1 space-y-1">
        {navItems.map((item) => {
          const Icon = item.icon;
          const isActive = activeNav === item.name;

          return (
            <button
              key={item.name}
              onClick={() => navigate(item.path)}
              className={`w-full flex items-center gap-3 px-3 py-2.5 rounded-lg text-sm transition-colors ${
                isActive
                  ? 'bg-white text-green-800 font-semibold'
                  : 'text-green-100 hover:bg-white hover:bg-opacity-20 hover:text-white'
              }`}
              style={isActive ? {} : { backgroundColor: 'transparent' }}
            >
              <Icon className="w-4 h-4" />
              <span className="flex-1 text-left">{item.name}</span>
              {item.badge && (
                <span className="text-[10px] bg-white bg-opacity-20 text-green-100 px-2 py-1 rounded-xl">
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
