import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import '@testing-library/jest-dom/extend-expect';
import Login from './Login';
import { BrowserRouter } from 'react-router-dom';
import { GlobalContext } from './GlobalContext';
import axios from 'axios';

// Mocking axios
jest.mock('axios');

describe('Login Component', () => {
  const setGlobalObject = jest.fn();

  const renderComponent = () =>
    render(
      <GlobalContext.Provider value={{ globalObject: null, setGlobalObject }}>
        <BrowserRouter>
          <Login />
        </BrowserRouter>
      </GlobalContext.Provider>
    );

  it('renders the login form correctly', () => {
    renderComponent();

    // Check if form inputs and button are present
    expect(screen.getByLabelText(/Email/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/Password/i)).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /Login/i })).toBeInTheDocument();
  });

  it('displays an error when fields are empty', () => {
    renderComponent();

    fireEvent.click(screen.getByRole('button', { name: /Login/i }));

    expect(screen.getByText('Please fill out all fields.')).toBeInTheDocument();
  });

  it('updates the email and password fields correctly', () => {
    renderComponent();

    // Simulate typing in the email field
    const emailInput = screen.getByLabelText(/Email/i);
    fireEvent.change(emailInput, { target: { value: 'test@example.com' } });
    expect(emailInput.value).toBe('test@example.com');

    // Simulate typing in the password field
    const passwordInput = screen.getByLabelText(/Password/i);
    fireEvent.change(passwordInput, { target: { value: 'password123' } });
    expect(passwordInput.value).toBe('password123');
  });

  it('submits the form and handles successful login', async () => {
    renderComponent();

    // Mock successful login response
    axios.post.mockResolvedValue({
      data: {
        token: 'fake-jwt-token',
      },
    });

    const emailInput = screen.getByLabelText(/Email/i);
    const passwordInput = screen.getByLabelText(/Password/i);

    fireEvent.change(emailInput, { target: { value: 'test@example.com' } });
    fireEvent.change(passwordInput, { target: { value: 'password123' } });

    fireEvent.click(screen.getByRole('button', { name: /Login/i }));

    await waitFor(() => {
      expect(setGlobalObject).toHaveBeenCalledWith({
        email: 'test@example.com',
        authToken: 'fake-jwt-token',
      });
    });
  });

  it('handles failed login and displays error message', async () => {
    renderComponent();

    // Mock failed login response
    axios.post.mockRejectedValue({
      response: {
        data: {
          message: 'Invalid credentials',
        },
      },
    });

    const emailInput = screen.getByLabelText(/Email/i);
    const passwordInput = screen.getByLabelText(/Password/i);

    fireEvent.change(emailInput, { target: { value: 'wrong@example.com' } });
    fireEvent.change(passwordInput, { target: { value: 'wrongpassword' } });

    fireEvent.click(screen.getByRole('button', { name: /Login/i }));

    await waitFor(() => {
      expect(screen.getByText('Invalid credentials')).toBeInTheDocument();
    });
  });
});
