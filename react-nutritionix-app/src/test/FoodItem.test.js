import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import '@testing-library/jest-dom/extend-expect';
import axios from 'axios';
import FoodItem from './FoodItem';
import { GlobalContext } from './GlobalContext';

// Mock axios to prevent actual HTTP requests
jest.mock('axios');

describe('FoodItem Component', () => {
  const mockItem = {
    food_name: 'Apple',
    serving_qty: 1,
    serving_unit: 'piece',
    tag_id: '123',
    tag_name: 'Fruit',
    common_type: null,
    locale: 'en_US',
    photo: { thumb: 'apple.jpg' },
  };

  const mockGlobalObject = {
    authToken: 'mockedAuthToken',
  };

  const mockGlobalContext = {
    globalObject: mockGlobalObject,
  };

  afterEach(() => {
    jest.clearAllMocks();
  });

  it('renders the FoodItem component with correct data', () => {
    render(
      <GlobalContext.Provider value={mockGlobalContext}>
        <FoodItem item={mockItem} />
      </GlobalContext.Provider>
    );

    expect(screen.getByText(/Apple/i)).toBeInTheDocument();
    expect(screen.getByText(/Serving quantity: 1/i)).toBeInTheDocument();
    expect(screen.getByRole('img')).toHaveAttribute('src', 'apple.jpg');
  });

  it('displays an alert if the user is not logged in', () => {
    window.alert = jest.fn(); // Mock window.alert

    render(
      <GlobalContext.Provider value={{ globalObject: null }}>
        <FoodItem item={mockItem} />
      </GlobalContext.Provider>
    );

    fireEvent.click(screen.getByText(/Add to Favourite/i));

    expect(window.alert).toHaveBeenCalledWith('Please login first to add items to favourites');
  });

  it('calls the API to add the item to favourites when the user is logged in', async () => {
    axios.post.mockResolvedValueOnce({ data: { message: 'Successfully added to favourite' } });

    render(
      <GlobalContext.Provider value={mockGlobalContext}>
        <FoodItem item={mockItem} />
      </GlobalContext.Provider>
    );

    fireEvent.click(screen.getByText(/Add to Favourite/i));

    await waitFor(() => expect(axios.post).toHaveBeenCalledTimes(1));

    const expectedRequestData = {
      serving_unit: 'piece',
      serving_qty: 1,
      tag_id: '123',
      tag_name: 'Fruit',
      food_name: 'Apple',
      common_type: null,
      photo_url: 'apple.jpg',
      locale: 'en_US',
    };

    expect(axios.post).toHaveBeenCalledWith(
      'http://localhost:8080/wishlist-services/api/wishlist',
      expectedRequestData,
      { headers: { Authorization: 'Bearer mockedAuthToken' } }
    );

    expect(screen.getByText(/Successfully added to favourite/i)).toBeInTheDocument();
  });

  it('displays an error message when the API request fails', async () => {
    axios.post.mockRejectedValueOnce({ response: { data: { message: 'Error adding to favourites' } } });

    render(
      <GlobalContext.Provider value={mockGlobalContext}>
        <FoodItem item={mockItem} />
      </GlobalContext.Provider>
    );

    fireEvent.click(screen.getByText(/Add to Favourite/i));

    await waitFor(() => expect(axios.post).toHaveBeenCalledTimes(1));

    expect(screen.getByText(/Error adding to favourites/i)).toBeInTheDocument();
  });
});
