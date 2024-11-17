import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import axios from 'axios';
import SearchBar from './SearchBar';
import { GlobalContext } from './GlobalContext';

// Mock axios
jest.mock('axios');

describe('SearchBar Component', () => {
  const globalObject = { authToken: 'sample_token' };

  const renderComponent = () =>
    render(
      <GlobalContext.Provider value={{ globalObject }}>
        <SearchBar />
      </GlobalContext.Provider>
    );

  beforeEach(() => {
    jest.clearAllMocks();
  });

  it('renders search input and submit button', () => {
    renderComponent();

    // Check for search input and submit button
    expect(screen.getByPlaceholderText(/Search/i)).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /Submit/i })).toBeInTheDocument();
  });

  it('displays error message if no query is provided', async () => {
    renderComponent();

    const submitButton = screen.getByRole('button', { name: /Submit/i });
    fireEvent.click(submitButton);

    // Ensure error message is displayed
    await waitFor(() => {
      expect(screen.getByText('Please enter food item to search.')).toBeInTheDocument();
    });
  });

  it('displays loading state during API call', async () => {
    axios.get.mockResolvedValueOnce({
      data: { common: [{ food_name: 'Apple', photo_url: 'apple.jpg', serving_qty: 1 }] }
    });

    renderComponent();

    fireEvent.change(screen.getByPlaceholderText(/Search/i), { target: { value: 'apple' } });

    const submitButton = screen.getByRole('button', { name: /Submit/i });
    fireEvent.click(submitButton);

    // Check for loading state
    await waitFor(() => {
      expect(screen.getByText(/Loading/i)).toBeInTheDocument();
    });
  });

  it('fetches food items successfully and displays them', async () => {
    // Mock successful response from API
    axios.get.mockResolvedValueOnce({
      data: { common: [{ food_name: 'Apple', photo_url: 'apple.jpg', serving_qty: 1 }] }
    });

    renderComponent();

    // Simulate typing a search query and submitting the form
    fireEvent.change(screen.getByPlaceholderText(/Search/i), { target: { value: 'apple' } });
    const submitButton = screen.getByRole('button', { name: /Submit/i });
    fireEvent.click(submitButton);

    // Wait for the API call to complete and check if food items are rendered
    await waitFor(() => {
      expect(screen.getByText('Apple')).toBeInTheDocument();
    });
  });

  it('displays message if no food items are found', async () => {
    // Mock response with no food items
    axios.get.mockResolvedValueOnce({ data: { common: [] } });

    renderComponent();

    // Simulate typing a search query and submitting the form
    fireEvent.change(screen.getByPlaceholderText(/Search/i), { target: { value: 'unknown' } });
    const submitButton = screen.getByRole('button', { name: /Submit/i });
    fireEvent.click(submitButton);

    // Wait for API response and check if the message is displayed
    await waitFor(() => {
      expect(screen.getByText('No food items found.')).toBeInTheDocument();
    });
  });

  it('displays an error message when the search fails', async () => {
    // Mock an API failure
    axios.get.mockRejectedValueOnce({
      response: { data: { message: 'Search failed with error: Invalid query' } }
    });

    renderComponent();

    fireEvent.change(screen.getByPlaceholderText(/Search/i), { target: { value: 'invalid query' } });
    const submitButton = screen.getByRole('button', { name: /Submit/i });
    fireEvent.click(submitButton);

    // Check if the error message is displayed
    await waitFor(() => {
      expect(screen.getByText('Invalid query')).toBeInTheDocument();
    });
  });
});
