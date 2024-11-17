import React from 'react';
import { render } from '@testing-library/react';
import { GlobalContext } from './GlobalContext';
import Logout from './Logout';
import { MemoryRouter, Routes, Route } from 'react-router-dom';
import { Navigate } from 'react-router-dom';

describe('Logout Component', () => {
  const setGlobalObject = jest.fn();

  const renderComponent = () =>
    render(
      <GlobalContext.Provider value={{ globalObject: { authToken: 'test-token' }, setGlobalObject }}>
        <MemoryRouter initialEntries={['/logout']}>
          <Routes>
            <Route path="/logout" element={<Logout />} />
            <Route path="/" element={<Navigate to="/" />} />
          </Routes>
        </MemoryRouter>
      </GlobalContext.Provider>
    );

  it('calls setGlobalObject with null and redirects to home page', () => {
    renderComponent();

    // Check if setGlobalObject is called with null (logging the user out)
    expect(setGlobalObject).toHaveBeenCalledWith(null);

    // Check if Navigate is used to redirect the user to the home page
    expect(window.location.pathname).toBe('/');
  });
});
