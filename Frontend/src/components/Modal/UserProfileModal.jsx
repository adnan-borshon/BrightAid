// UserProfile Modal Component
const UserProfileModal = ({ isOpen, onClose, userData, profileData }) => {
  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 z-50 flex items-center justify-center p-4">
      <div className="bg-white rounded-xl shadow-2xl max-w-md w-full max-h-[90vh] overflow-y-auto">
        {/* Header */}
        <div className="relative bg-gradient-to-r from-green-600 to-green-700 p-6 rounded-t-xl">
          <button
            onClick={onClose}
            className="absolute top-4 right-4 text-white hover:bg-white hover:bg-opacity-20 rounded-full p-1 transition-colors"
          >
            <X className="w-5 h-5" />
          </button>
          <div className="flex flex-col items-center">
            <div className="w-24 h-24 bg-orange-200 rounded-full mb-3 flex items-center justify-center text-4xl font-bold text-orange-600">
              {profileData?.fullName?.charAt(0)?.toUpperCase() || userData?.username?.charAt(0)?.toUpperCase() || '?'}
            </div>
            <h2 className="text-2xl font-bold text-white">
              {profileData?.fullName || userData?.username || 'User'}
            </h2>
            <p className="text-green-100 text-sm mt-1">
              {userData?.userType || 'User'}
            </p>
          </div>
        </div>

        {/* Content */}
        <div className="p-6 space-y-4">
          {/* Email */}
          <div className="flex items-start gap-3">
            <div className="mt-1 p-2 bg-green-50 rounded-lg">
              <Mail className="w-4 h-4 text-green-600" />
            </div>
            <div className="flex-1">
              <p className="text-xs font-medium text-gray-500 uppercase">Email</p>
              <p className="text-sm text-gray-800 break-all">{userData?.email || 'N/A'}</p>
            </div>
          </div>

          {/* Phone */}
          {profileData?.phone && (
            <div className="flex items-start gap-3">
              <div className="mt-1 p-2 bg-green-50 rounded-lg">
                <Phone className="w-4 h-4 text-green-600" />
              </div>
              <div className="flex-1">
                <p className="text-xs font-medium text-gray-500 uppercase">Phone</p>
                <p className="text-sm text-gray-800">{profileData.phone}</p>
              </div>
            </div>
          )}

          {/* Address */}
          {profileData?.address && (
            <div className="flex items-start gap-3">
              <div className="mt-1 p-2 bg-green-50 rounded-lg">
                <MapPin className="w-4 h-4 text-green-600" />
              </div>
              <div className="flex-1">
                <p className="text-xs font-medium text-gray-500 uppercase">Address</p>
                <p className="text-sm text-gray-800">{profileData.address}</p>
              </div>
            </div>
          )}

          {/* Account Status */}
          <div className="flex items-start gap-3">
            <div className="mt-1 p-2 bg-green-50 rounded-lg">
              <Calendar className="w-4 h-4 text-green-600" />
            </div>
            <div className="flex-1">
              <p className="text-xs font-medium text-gray-500 uppercase">Account Status</p>
              <p className="text-sm">
                <span className={`inline-flex px-2 py-1 rounded-full text-xs font-medium ${
                  profileData?.status === 'ACTIVE' ? 'bg-green-100 text-green-700' : 'bg-gray-100 text-gray-700'
                }`}>
                  {profileData?.status || (userData?.isActive ? 'Active' : 'Inactive')}
                </span>
              </p>
            </div>
          </div>

          {/* Last Login */}
          {profileData?.lastLogin && (
            <div className="flex items-start gap-3">
              <div className="mt-1 p-2 bg-green-50 rounded-lg">
                <Calendar className="w-4 h-4 text-green-600" />
              </div>
              <div className="flex-1">
                <p className="text-xs font-medium text-gray-500 uppercase">Last Login</p>
                <p className="text-sm text-gray-800">
                  {new Date(profileData.lastLogin).toLocaleString()}
                </p>
              </div>
            </div>
          )}
        </div>

        {/* Footer */}
        <div className="p-6 bg-gray-50 rounded-b-xl">
          <button
            onClick={onClose}
            className="w-full bg-green-600 hover:bg-green-700 text-white font-medium py-2 px-4 rounded-lg transition-colors"
          >
            Close
          </button>
        </div>
      </div>
    </div>
  );
};
export default UserProfileModal;