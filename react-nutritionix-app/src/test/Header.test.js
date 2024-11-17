import React from 'react';
import { render, screen } from '@testing-library/react';
import '@testing-library/jest-dom/extend-expect'; // for better assertions
import { BrowserRouter } from 'react-router-dom';
import Header from './Header';
import { GlobalContext } from './GlobalContext';

// Helper function to render the component within BrowserRouter and GlobalContext
const renderWithContext = (contextValue) => {
  return render(
    <GlobalContext.Provider value={contextValue}>
      <BrowserRouter>
        <Header />
      </BrowserRouter>
    </GlobalContext.Provider>
  );
};

describe('Header Component', () => {
  it('renders with login/registration link when globalObject is null', () => {
    renderWithContext({ globalObject: null });

    // Check if the Login/Registration link is present
    expect(screen.getByText('Login/Registration')).toBeInTheDocument();

    // Check if the Home, Check Nutrition, and Favourites links are present
    expect(screen.getByText('Home')).toBeInTheDocument();
    expect(screen.getByText('Check Nutrition')).toBeInTheDocument();
    expect(screen.getByText('Favourites')).toBeInTheDocument();
  });

  it('renders with logout link when globalObject is set', () => {
    renderWithContext({ globalObject: { user: 'John Doe', authToken: 'abc123' } });

    // Check if the Logout link is present instead of Login/Registration
    expect(screen.getByText('Logout')).toBeInTheDocument();

    // Check if the Home, Check Nutrition, and Favourites links are present
    expect(screen.getByText('Home')).toBeInTheDocument();
    expect(screen.getByText('Check Nutrition')).toBeInTheDocument();
    expect(screen.getByText('Favourites')).toBeInTheDocument();
  });

  it('renders the logo image with correct alt text', () => {
    renderWithContext({ globalObject: null });

    // Check if the logo is rendered with the correct alt text
    const logoImage = screen.getByAltText('Nutritionix');
    expect(logoImage).toBeInTheDocument();
    expect(logoImage).toHaveAttribute('src', expect.stringContaining('logo.png')); // Check that logo image is present
  });
});
