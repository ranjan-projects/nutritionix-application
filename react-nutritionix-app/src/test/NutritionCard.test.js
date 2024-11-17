import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import axios from 'axios';
import NutritionCard from './NutritionCard';
import { GlobalContext } from './GlobalContext';

// Mock axios
jest.mock('axios');

const mockItem = {
  food_name: 'Apple',
  serving_qty: 1,
  photo_url: 'http://example.com/apple.jpg',
};

const setFavouriteItems = jest.fn();
const setBanner = jest.fn();

describe('NutritionCard Component', () => {
  const renderComponent = (globalObject) =>
    render(
      <GlobalContext.Provider value={{ globalObject }}>
        <NutritionCard item={mockItem} setFavouriteItems={setFavouriteItems} setBanner={setBanner} />
      </GlobalContext.Provider>
    );

  it('renders the nutrition card with correct details', () => {
    renderComponent({ authToken: 'test-token' });

    // Check if item details are rendered correctly
    expect(screen.getByText('Apple')).toBeInTheDocument();
    expect(screen.getByText('Serving quantity: 1')).toBeInTheDocument();
    expect(screen.getByRole('img')).toHaveAttribute('src', 'http://example.com/apple.jpg');
    expect(screen.getByRole('button', { name: /Remove Favourite/i })).toBeInTheDocument();
  });

  it('calls removeFavourite and updates favourite items when "Remove Favourite" is clicked', async () => {
    // Mock the axios delete and get request
    axios.delete.mockResolvedValue({ data: { message: 'Deleted successfully' } });
    axios.get.mockResolvedValue({ data: [] }); // No favourites after deletion

    renderComponent({ authToken: 'test-token' });

    const removeButton = screen.getByRole('button', { name: /Remove Favourite/i });

    // Simulate button click
    fireEvent.click(removeButton);

    // Wait for async actions
    await waitFor(() => expect(axios.delete).toHaveBeenCalledWith(
      'http://localhost:8080/wishlist-services/api/wishlist/Apple',
      { headers: { Authorization: 'Bearer test-token' } }
    ));

    // Check that the get request is called to update the favourites
    await waitFor(() => expect(axios.get).toHaveBeenCalled());

    // Ensure setFavouriteItems and setBanner are called with updated data
    expect(setFavouriteItems).toHaveBeenCalledWith([]);
    expect(setBanner).toHaveBeenCalledWith('Food successfully removed from the favourite list');
  });

  it('displays an error message if the removal fails', async () => {
    // Mock the axios delete request to reject with an error
    axios.delete.mockRejectedValue(new Error('Network error'));

    renderComponent({ authToken: 'test-token' });

    const removeButton = screen.getByRole('button', { name: /Remove Favourite/i });

    // Simulate button click
    fireEvent.click(removeButton);

    // Wait for async actions
    await waitFor(() => expect(screen.getByText('Error fetching data')).toBeInTheDocument());
  });
});
