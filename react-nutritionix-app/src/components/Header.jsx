import React, { useContext } from 'react'
import { Link } from "react-router-dom";
import logo from '../asset/images/logo.png'

import Container from 'react-bootstrap/Container';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import '../asset/css/Header.css';
import { GlobalContext } from './GlobalContext';

const Header = () => {
  const { globalObject } = useContext(GlobalContext);

  var loginButton;
  if (globalObject) {
    loginButton = <Link to="/logout" className='add-margin-left add-margin-right'>Logout</Link>;
  } else {
    loginButton = <Link to="/login" className='add-margin-left add-margin-right'>Login/Registration</Link>;
  }

  return (
    <Navbar bg="dark" data-bs-theme="dark" className='nav-style'>
      <img src={logo} alt='Nutritionix' />
      <Container>
        <Nav className="headers">
          <Link to="/" className='add-margin-left add-margin-right'>Home</Link>
          <Link to="/checknutrition" className='add-margin-left add-margin-right'>Check Nutrition</Link>
          <Link to="/favourites" className='add-margin-left add-margin-right'>Favourites</Link>

          {loginButton}


        </Nav>
      </Container>
    </Navbar>
  );
}

export default Header;
