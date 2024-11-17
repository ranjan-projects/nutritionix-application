
import './App.css';
import { BrowserRouter, Routes, Route } from "react-router-dom";

import Login from './components/Login';
import Footer from './components/Footer';
import Header from './components/Header';
import Registration from './components/Registration';
import Home from './components/Home';
import 'bootstrap/dist/css/bootstrap.min.css';
import SearchBar from './components/SearchBar';
import Favourites from './components/Favourites';
import Logout from './components/Logout';

function App() {
  return (
    <div className="App">

      <BrowserRouter>
        <Header />

        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/login" element={<Login />} />
          <Route path="/logout" element={<Logout />} />
          <Route path="/registration" element={<Registration />} />
          <Route path="/checknutrition" element={<SearchBar />} />
          <Route path="/favourites" element={<Favourites />} />
        </Routes>
      </BrowserRouter>

      <Footer />

    </div>
  );
}

export default App;
