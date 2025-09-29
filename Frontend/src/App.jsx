import React from 'react';

import Stats from './components/Landing page/Stats';
import Eligibility from './components/Landing page/Eligibility';
import Projects from './components/Landing page/Projects';
import About from './components/Landing page/About';
import Testimonials from './components/Landing page/Testimonials';
import Navbar from './Fixed components/Navbar';
import Hero from './components/Landing page/Hero';
import Login from './components/Authentication/Login'
import SignUp from './components/Authentication/Signup/SignUp';
function App() {
  return (
    <div className="min-h-screen bg-white">
       <SignUp/>
        {/* <Login/> */}
      {/* <Hero />
      <Stats />
      <Eligibility />
      <Projects />
      <About />
      <Testimonials /> */}
    </div>
  );
}

export default App;