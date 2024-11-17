import React, { useContext, useState } from 'react';
import axios from 'axios';
import { Link, useNavigate } from 'react-router-dom';
import { Form, Button, Alert } from 'react-bootstrap';
import loginImage from '../asset/images/login.jpg';
import { GlobalContext } from './GlobalContext';

import '../asset/css/Login.css'

import {
  MDBContainer,
  MDBCard,
  MDBCardImage,
  MDBRow,
  MDBCol
}
  from 'mdb-react-ui-kit';

export default function Login() {
  const { globalObject, setGlobalObject } = useContext(GlobalContext);

  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!email || !password) {
      setError('Please fill out all fields.');
      return;
    }

    const data = {
      email: email,
      password: password
    };


    try {
      const response = await axios.post('http://localhost:8080/auth-services/api/auth/token', data);
      const responsePayload = response.data;
      console.log('Access token is:' + responsePayload.token);

      setGlobalObject({
        email: email,
        authToken: responsePayload.token
      });

      console.log("Assigned global object is:" + globalObject);

      navigate('/');
    } catch (err) {
      const responsePayload = err.response.data;
      setError(responsePayload.message);
    }
  };


  return (
    <MDBContainer className="my-1">
      <MDBCard>
        <MDBRow className='g-0'>

          <MDBCol md='6'>
            <MDBCardImage src={loginImage} alt="login form" className='login-image' />
          </MDBCol>

          <MDBCol md='6'>
            <h1 className='login-header'>LOGIN</h1>
            <div className='banner-message'>
              {error && <Alert variant="danger">{error}</Alert>}
            </div>

            <Form onSubmit={handleSubmit} className='container login-form'>
              <Form.Group className="mb-3" controlId="formUsername">
                <Form.Label>Email</Form.Label>
                <Form.Control
                  type="text"
                  placeholder="Enter email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                />
              </Form.Group>

              <Form.Group className="mb-3" controlId="formPassword">
                <Form.Label>Password</Form.Label>
                <Form.Control
                  type="password"
                  placeholder="Enter password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                />
              </Form.Group>

              <Button variant="dark" type="submit">
                Login
              </Button>&nbsp; <span>Don't have account? <Link to="/registration">Register here</Link></span>
            </Form>
          </MDBCol>

        </MDBRow>
      </MDBCard>

    </MDBContainer>
  );
}
