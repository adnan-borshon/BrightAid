import React from 'react';

const About = () => {
  const features = [
    {
      icon: '🏫',
      title: 'School Tracking',
      description: '500+ schools monitored'
    },
    {
      icon: '📊',
      title: 'Project Monitoring',
      description: '300+ NGO projects live'
    },
    {
      icon: '🚨',
      title: 'Dropout Alerts',
      description: '1500+ alerts resolved'
    },
    {
      icon: '🎁',
      title: 'Transparent Giving',
      description: '8000+ donations tracked'
    }
  ];

  return (
    <section className="py-16 bg-gray-50">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="grid lg:grid-cols-2 gap-12 items-center">
          
          {/* Left Content */}
          <div>
            <h2 className="text-3xl font-bold text-gray-900 mb-6">About BrightAid</h2>
            <p className="text-gray-600 mb-6">Empowering Schools Everywhere</p>
            
            <div className="space-y-6 mb-8">
              <p className="text-gray-700">
                BrightAid is perfect for donors, schools, and anyone passionate about supporting 
                education. It simplifies giving with transparency, trackable donations, so you can 
                make a real impact with confidence.
              </p>
              
              <p className="text-gray-700">
                Discover tools, insights, and AI-driven data to prevent school dropouts and monitor 
                progress. Whether you're funding a local school or supporting nationwide 
                education projects, BrightAid makes it easy and meaningful.
              </p>
            </div>
            
            {/* Features Grid */}
            <div className="grid grid-cols-2 gap-4 mb-8">
              {features.map((feature, index) => (
                <div key={index} className="flex items-start space-x-3">
                  <div className="text-2xl">{feature.icon}</div>
                  <div>
                    <h3 className="font-semibold text-gray-900">{feature.title}</h3>
                    <p className="text-sm text-gray-600">{feature.description}</p>
                  </div>
                </div>
              ))}
            </div>
            
            {/* CTA Button */}
            <button className="bg-green-600 text-white px-8 py-3 rounded-lg hover:bg-green-700 transition-colors">
              Start Donating
            </button>
          </div>
          
          {/* Right Content - Image */}
          <div className="relative">
            <div className="bg-green-100 rounded-3xl p-8 overflow-hidden">
              {/* Placeholder for children image */}
              <div className="bg-white/60 rounded-2xl h-96 flex items-center justify-center">
                <div className="text-center">
                  <div className="flex justify-center space-x-4 mb-4">
                    <div className="w-12 h-12 bg-red-400 rounded-full flex items-center justify-center">
                      <span className="text-white text-xl">👧</span>
                    </div>
                    <div className="w-12 h-12 bg-blue-400 rounded-full flex items-center justify-center">
                      <span className="text-white text-xl">👦</span>
                    </div>
                    <div className="w-12 h-12 bg-yellow-400 rounded-full flex items-center justify-center">
                      <span className="text-white text-xl">👧</span>
                    </div>
                  </div>
                  <p className="text-gray-700 font-medium">Happy Students</p>
                  <p className="text-gray-600 text-sm">Building Bright Futures</p>
                </div>
              </div>
              
              {/* Decorative leaves */}
              <div className="absolute top-4 right-4 text-green-600 text-3xl">🌿</div>
              <div className="absolute bottom-8 left-8 text-green-600 text-2xl">🍃</div>
            </div>
          </div>
        </div>
      </div>
    </section>
  );
};

export default About;