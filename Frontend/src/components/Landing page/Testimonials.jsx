import React from 'react';

const Testimonials = () => {
  const testimonials = [
    {
      id: 1,
      rating: 5,
      text: "A great resource with practical tips for a beautiful, thriving garden!",
      author: "Leslie Alexander",
      avatar: "LA"
    },
    {
      id: 2,
      rating: 5,
      text: "Must-have for beginners! This book made gardening feel simple and fun.",
      author: "Dani Smith",
      avatar: "DS"
    },
    {
      id: 3,
      rating: 4,
      text: "Clear, easy-to-follow steps that helped me transform my garden in no time.",
      author: "Cameron Williamson",
      avatar: "CW"
    }
  ];

  const renderStars = (rating) => {
    return [...Array(5)].map((_, i) => (
      <span key={i} className={`text-yellow-400 ${i < rating ? '' : 'opacity-30'}`}>
        ⭐
      </span>
    ));
  };

  return (
    <section className="py-16 bg-green-50">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        
        {/* Header */}
        <div className="text-center mb-12">
          <h2 className="text-3xl font-bold text-gray-900 mb-4">What Our Community Says</h2>
          <p className="text-gray-600">Stories from donors and schools who've made a difference</p>
        </div>
        
        {/* Testimonials Grid */}
        <div className="grid md:grid-cols-3 gap-8">
          {testimonials.map((testimonial) => (
            <div key={testimonial.id} className="bg-white p-6 rounded-xl shadow-sm">
              
              {/* Rating */}
              <div className="flex mb-4">
                {renderStars(testimonial.rating)}
              </div>
              
              {/* Testimonial Text */}
              <p className="text-gray-700 mb-6 italic">
                "{testimonial.text}"
              </p>
              
              {/* Author */}
              <div className="flex items-center">
                <div className="w-10 h-10 bg-green-600 rounded-full flex items-center justify-center text-white font-semibold mr-3">
                  {testimonial.avatar}
                </div>
                <div>
                  <p className="font-semibold text-gray-900">{testimonial.author}</p>
                  <p className="text-sm text-gray-600">Community Member</p>
                </div>
              </div>
            </div>
          ))}
        </div>
        
        {/* Call to Action */}
        <div className="text-center mt-12">
          <p className="text-gray-600 mb-6">Ready to make a difference in a child's life?</p>
          <div className="flex flex-col sm:flex-row gap-4 justify-center">
            <button className="bg-green-600 text-white px-8 py-3 rounded-lg hover:bg-green-700 transition-colors">
              Start Your Journey
            </button>
            <button className="border-2 border-green-600 text-green-600 px-8 py-3 rounded-lg hover:bg-green-600 hover:text-white transition-colors">
              Learn More
            </button>
          </div>
        </div>
      </div>
    </section>
  );
};

export default Testimonials;