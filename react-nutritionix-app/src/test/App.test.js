import React from 'react';
import { render, screen } from '@testing-library/react';
import App from '../App'; // Adjust the path if necessary

// Mock components to avoid rendering unnecessary components during the test
jest.mock('../components/Header', () => () => <div>Header Mock</div>);
jest.mock('../components/Footer', () => () => <div>Footer Mock</div>);
jest.mock('../components/Home', () => () => <div>Home Component</div>);
jest.mock('../components/Login', () => () => <div>Login Component</div>);
jest.mock('../components/Logout', () => () => <div>Logout Component</div>);
jest.mock('../components/Registration', () => () => <div>Registration Component</div>);
jest.mock('../components/SearchBar', () => () => <div>Search Bar</div>);
jest.mock('../components/Favourites', () => () => <div>Favourites Component</div>);

describe('App Component', () => {
  it('renders the header and footer', () => {
    render(<App />);

    // Check if header and footer are rendered
    expect(screen.getByText('Header Mock')).toBeInTheDocument();
    expect(screen.getByText('Footer Mock')).toBeInTheDocument();
  });

  it('renders Home component for default route', () => {
    window.history.pushState({}, 'Home', '/'); // Simulate navigation to "/"
    render(<App />);

    // Check if Home component is rendered for "/"
    expect(screen.getByText('Home Component')).toBeInTheDocument();
  });

  it('renders Login component for "/login" route', () => {
    window.history.pushState({}, 'Login', '/login'); // Simulate navigation to "/login"
    render(<App />);

    // Check if Login component is rendered for "/login"
    expect(screen.getByText('Login Component')).toBeInTheDocument();
  });

  it('renders Registration component for "/registration" route', () => {
    window.history.pushState({}, 'Registration', '/registration'); // Simulate navigation to "/registration"
    render(<App />);

    // Check if Registration component is rendered for "/registration"
    expect(screen.getByText('Registration Component')).toBeInTheDocument();
  });

  it('renders SearchBar component for "/checknutrition" route', () => {
    window.history.pushState({}, 'Search Bar', '/checknutrition'); // Simulate navigation to "/checknutrition"
    render(<App />);

    // Check if SearchBar component is rendered for "/checknutrition"
    expect(screen.getByText('Search Bar')).toBeInTheDocument();
  });

  it('renders Favourites component for "/favourites" route', () => {
    window.history.pushState({}, 'Favourites', '/favourites'); // Simulate navigation to "/favourites"
    render(<App />);

    // Check if Favourites component is rendered for "/favourites"
    expect(screen.getByText('Favourites Component')).toBeInTheDocument();
  });
});
