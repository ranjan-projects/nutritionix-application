import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { BrowserRouter as Router } from 'react-router-dom';
import axios from 'axios';
import Registration from './Registration';

// Mock axios
jest.mock('axios');

describe('Registration Component', () => {
  const renderComponent = () =>
    render(
      <Router>
        <Registration />
      </Router>
    );

  it('renders all form fields and the submit button', () => {
    renderComponent();

    // Check for form inputs
    expect(screen.getByLabelText(/Name/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/Email/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/Password/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/Confirm Password/i)).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /Register/i })).toBeInTheDocument();
  });

  it('displays validation errors for invalid inputs', async () => {
    renderComponent();

    const submitButton = screen.getByRole('button', { name: /Register/i });
    fireEvent.click(submitButton);

    // Ensure validation messages are displayed
    await waitFor(() => {
      expect(screen.getByText('Name is required')).toBeInTheDocument();
      expect(screen.getByText('Email is required')).toBeInTheDocument();
      expect(screen.getByText('Password is required')).toBeInTheDocument();
      expect(screen.getByText('Confirm Password is required')).toBeInTheDocument();
    });
  });

  it('displays validation errors for invalid email and password mismatch', async () => {
    renderComponent();

    // Fill invalid email and mismatched passwords
    fireEvent.change(screen.getByLabelText(/Email/i), {
      target: { value: 'invalid-email' },
    });
    fireEvent.change(screen.getByLabelText(/Password/i), {
      target: { value: 'password123' },
    });
    fireEvent.change(screen.getByLabelText(/Confirm Password/i), {
      target: { value: 'wrongpassword' },
    });

    const submitButton = screen.getByRole('button', { name: /Register/i });
    fireEvent.click(submitButton);

    // Check validation messages
    await waitFor(() => {
      expect(screen.getByText('Invalid email address')).toBeInTheDocument();
      expect(screen.getByText('Passwords must match')).toBeInTheDocument();
    });
  });

  it('submits the form successfully and redirects on success', async () => {
    const mockNavigate = jest.fn();

    // Mock the axios post request to return a success response
    axios.post.mockResolvedValue({ status: 201 });

    renderComponent();

    // Fill valid form values
    fireEvent.change(screen.getByLabelText(/Name/i), {
      target: { value: 'John Doe' },
    });
    fireEvent.change(screen.getByLabelText(/Email/i), {
      target: { value: 'john@example.com' },
    });
    fireEvent.change(screen.getByLabelText(/Password/i), {
      target: { value: 'password123' },
    });
    fireEvent.change(screen.getByLabelText(/Confirm Password/i), {
      target: { value: 'password123' },
    });

    const submitButton = screen.getByRole('button', { name: /Register/i });
    fireEvent.click(submitButton);

    // Wait for form submission and redirection
    await waitFor(() => {
      expect(axios.post).toHaveBeenCalledWith(
        'http://localhost:8080/user-services/api/users',
        {
          name: 'John Doe',
          email: 'john@example.com',
          password: 'password123',
        }
      );
    });

    expect(mockNavigate).toHaveBeenCalledWith('/login');
  });

  it('displays error message when registration fails', async () => {
    // Mock the axios post request to return an error response
    axios.post.mockRejectedValue({
      response: { data: { message: 'Registration failed' } },
    });

    renderComponent();

    // Fill valid form values
    fireEvent.change(screen.getByLabelText(/Name/i), {
      target: { value: 'John Doe' },
    });
    fireEvent.change(screen.getByLabelText(/Email/i), {
      target: { value: 'john@example.com' },
    });
    fireEvent.change(screen.getByLabelText(/Password/i), {
      target: { value: 'password123' },
    });
    fireEvent.change(screen.getByLabelText(/Confirm Password/i), {
      target: { value: 'password123' },
    });

    const submitButton = screen.getByRole('button', { name: /Register/i });
    fireEvent.click(submitButton);

    // Wait for error message to be displayed
    await waitFor(() => {
      expect(screen.getByText('Registration failed')).toBeInTheDocument();
    });
  });
});
