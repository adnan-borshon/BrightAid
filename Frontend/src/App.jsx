import { Routes, Route } from 'react-router-dom'
import { useState, useEffect } from 'react'

function App() {
  const [students, setStudents] = useState([])

  useEffect(() => {
    console.log('Fetching students...')
    fetch('http://localhost:8080/api/students')
      .then(res => {
        console.log('Response status:', res.status)
        return res.json()
      })
      .then(data => {
        console.log('Students data:', data)
        setStudents(data)
      })
      .catch(err => console.error('Error:', err))
  }, [])

  return (
    <Routes>
      <Route path="/" element={
        <div className='p-4'>
          <h1 className='text-3xl font-bold mb-4'>Bright AId - Students</h1>
          <p>Students found: {students.length}</p>
          {students.length === 0 ? (
            <p>No students found or loading...</p>
          ) : (
            students.map(student => (
              <div key={student.id} className='border p-4 mb-4 rounded'>
                <h3 className='text-xl font-bold'>{student.name}</h3>
                <p><strong>School:</strong> {student.school_name}</p>
                <p><strong>Father:</strong> {student.father_name}</p>
                <p><strong>Mother:</strong> {student.mother_name}</p>
              </div>
            ))
          )}
        </div>
      } />
    </Routes>
  )
}

export default App
