import React, { useState } from 'react';
import axios from 'axios';
import { Link, useNavigate } from 'react-router-dom';
import { Form, Button, Alert } from 'react-bootstrap';
import loginImage from '../asset/images/login.jpg';
import '../asset/css/Registration.css';

import { useFormik } from "formik";
import * as Yup from "yup";

import {
  MDBContainer,
  MDBCard,
  MDBCardImage,
  MDBRow,
  MDBCol
}
  from 'mdb-react-ui-kit';

export default function Registration() {
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const formik = useFormik({
    initialValues: {
      name: '',
      email: '',
      password: '',
      confirmPassword: ''
    },
    validationSchema: Yup.object({
      name: Yup.string()
        .matches(/^[A-Za-z].*$/, "Name must start with a letter")
        .max(20, 'Name can be 20 characters or less')
        .min(2, 'Name must be at least 2 characters')
        .required('Name is required'),
      email: Yup.string()
        .email('Invalid email address')
        .required('Email is required'),
      password: Yup.string()
        .min(6, 'Password must be at least 6 characters')
        .required('Password is required'),
      confirmPassword: Yup.string()
        .oneOf([Yup.ref('password'), null], 'Passwords must match')
        .required('Confirm Password is required')
    }),
    onSubmit: async (values) => {
      const requestPayload = {
        name: values.name,
        email: values.email,
        password: values.password
      };

      try {
        const response = await axios.post('http://localhost:8080/user-services/api/users', requestPayload);

        if (response.status === 201) {
          navigate('/login');
        }
      } catch (err) {
        const responsePayload = err.response.data;
        setError(responsePayload.message);
      }
    },
  });

  return (
    <MDBContainer className='my-1'>
      <MDBCard>
        <MDBRow className='g-0'>

          <MDBCol md='6'>
            <MDBCardImage src={loginImage} alt="Registration" className='registration-image' />
          </MDBCol>

          <MDBCol md='6'>
            <h1 className='registration-header'>REGISTRATION</h1>
            <div className='banner-message'>
              {error && <Alert variant="danger">{error}</Alert>}
            </div>

            <Form onSubmit={formik.handleSubmit} className='registration-form'>
              <Form.Group className="mb-3" controlId="formName">
                <Form.Label className='form-label'>Name</Form.Label>
                <Form.Control
                  id="name"
                  name="name"
                  type="text"
                  placeholder='Enter name'
                  value={formik.values.name}
                  onChange={formik.handleChange}
                  onBlur={formik.handleBlur}
                />
                {formik.touched.name && formik.errors.name ? (
                  <p style={{ color: 'red' }}>{formik.errors.name}</p>
                ) : null}
              </Form.Group>
              <Form.Group className="mb-3" controlId="formEmail">
                <Form.Label>Email</Form.Label>
                <Form.Control
                  id="email"
                  name="email"
                  type="email"
                  placeholder='Enter email'
                  value={formik.values.email}
                  onChange={formik.handleChange}
                  onBlur={formik.handleBlur}
                />
                {formik.touched.email && formik.errors.email ? (
                  <p style={{ color: 'red' }}>{formik.errors.email}</p>
                ) : null}
              </Form.Group>

              <Form.Group className="mb-3" controlId="formPassword">
                <Form.Label>Password</Form.Label>
                <Form.Control
                  id="password"
                  name="password"
                  type="password"
                  placeholder='Enter password'
                  value={formik.values.password}
                  onChange={formik.handleChange}
                  onBlur={formik.handleBlur}
                />
                {formik.touched.password && formik.errors.password ? (
                  <p style={{ color: 'red' }}>{formik.errors.password}</p>
                ) : null}
              </Form.Group>

              <Form.Group className="mb-3" controlId="formCpassword">
                <Form.Label>Confirm Password</Form.Label>
                <Form.Control
                  id="confirmPassword"
                  name="confirmPassword"
                  type="password"
                  placeholder='Confirm password'
                  value={formik.values.confirmPassword}
                  onChange={formik.handleChange}
                  onBlur={formik.handleBlur}
                />
                {formik.touched.confirmPassword && formik.errors.confirmPassword ? (
                  <p style={{ color: 'red' }}>{formik.errors.confirmPassword}</p>
                ) : null}
              </Form.Group>

              <Button variant="dark" type="submit">
                Register
              </Button>&nbsp; <span>Already registered? <Link to="/login">Login here</Link></span>
            </Form>
          </MDBCol>

        </MDBRow>
      </MDBCard>

    </MDBContainer>
  );
}
