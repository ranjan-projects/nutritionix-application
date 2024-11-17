import React from 'react';
import { render, screen, waitFor } from '@testing-library/react';

import Favourites from '../components/Favourites';
import { GlobalContext } from '../components/GlobalContext';
const axios = require('axios');

import { BrowserRouter } from 'react-router-dom';

// Mock axios
jest.mock('axios');

// Mock NutritionCard component to simplify the test
jest.mock('../components/NutritionCard', () => ({ item }) => <div data-testid="nutrition-card">{item.name}</div>);

describe('Favourites Component', () => {
  const mockGlobalObject = {
    authToken: 'mockedAuthToken',
  };

  const mockResponseData = [
    { id: 1, name: 'Apple' },
    { id: 2, name: 'Banana' },
  ];

  afterEach(() => {
    jest.clearAllMocks();
  });

  it('displays login message when user is not logged in', () => {
    render(
      <GlobalContext.Provider value={{ globalObject: null }}>
        <BrowserRouter>
          <Favourites />
        </BrowserRouter>
      </GlobalContext.Provider>
    );

    expect(screen.getByText(/Please login to check favourites/i)).toBeInTheDocument();
    expect(screen.getByText(/Login now/i)).toBeInTheDocument();
  });

  it('displays a message when no items are in the favourites', async () => {
    axios.get.mockResolvedValueOnce({ data: [] });

    render(
      <GlobalContext.Provider value={{ globalObject: mockGlobalObject }}>
        <BrowserRouter>
          <Favourites />
        </BrowserRouter>
      </GlobalContext.Provider>
    );

    await waitFor(() => expect(axios.get).toHaveBeenCalledTimes(1));

    expect(screen.getByText(/Nothing added to favourites till now/i)).toBeInTheDocument();
    expect(screen.getByText(/Add now/i)).toBeInTheDocument();
  });

  it('displays favourite items when API request is successful', async () => {
    axios.get.mockResolvedValueOnce({ data: mockResponseData });

    render(
      <GlobalContext.Provider value={{ globalObject: mockGlobalObject }}>
        <BrowserRouter>
          <Favourites />
        </BrowserRouter>
      </GlobalContext.Provider>
    );

    await waitFor(() => expect(axios.get).toHaveBeenCalledTimes(1));

    // Check if the NutritionCard component is rendered for each favorite item
    expect(screen.getAllByTestId('nutrition-card').length).toBe(mockResponseData.length);
    expect(screen.getByText('Apple')).toBeInTheDocument();
    expect(screen.getByText('Banana')).toBeInTheDocument();
  });

  it('displays error message when API request fails', async () => {
    axios.get.mockRejectedValueOnce(new Error('Error fetching data'));

    render(
      <GlobalContext.Provider value={{ globalObject: mockGlobalObject }}>
        <BrowserRouter>
          <Favourites />
        </BrowserRouter>
      </GlobalContext.Provider>
    );

    await waitFor(() => expect(axios.get).toHaveBeenCalledTimes(1));

    expect(screen.getByText(/Error fetching data/i)).toBeInTheDocument();
  });
});
