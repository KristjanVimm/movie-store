import { Route, Routes } from 'react-router-dom'
import './App.css'
import NavigationBar from './components/NavigationBar'
import AddFilm from './pages/AddFilm'
import HomePage from './pages/HomePage'
import Login from './pages/Login'
import { useState } from 'react'
import ReturnFilms from './pages/ReturnFilms'
import AddUser from './pages/AddUser'

function App() {

  const [userId, setUserId] = useState<string>("");
  
  return (
    <>
      
      <NavigationBar />

      <Routes>
        <Route path='/' element={<Login userId={userId} setUserId={setUserId}/>}/>
        <Route path='/home' element={<HomePage userId={userId} />}/>
        <Route path='/return' element={<ReturnFilms userId={userId} />}/>
        <Route path="/add-film" element={<AddFilm/>} />
        <Route path="/add-user" element={<AddUser/>}/>
      </Routes>

    </>
  )
}

export default App
