import React from 'react';
import { render, screen } from '@testing-library/react';
import '@testing-library/jest-dom/extend-expect'; // for better assertions
import FoodList from './FoodList';
import FoodItem from './FoodItem';

// Mock the FoodItem component to avoid rendering its internals
jest.mock('./FoodItem', () => ({ item }) => <div data-testid="food-item">{item.food_name}</div>);

describe('FoodList Component', () => {
  const mockFoodItems = [
    {
      food_name: 'Apple',
      serving_qty: 1,
      serving_unit: 'piece',
      tag_id: '123',
      tag_name: 'Fruit',
      common_type: null,
      locale: 'en_US',
      photo: { thumb: 'apple.jpg' },
    },
    {
      food_name: 'Banana',
      serving_qty: 1,
      serving_unit: 'piece',
      tag_id: '124',
      tag_name: 'Fruit',
      common_type: null,
      locale: 'en_US',
      photo: { thumb: 'banana.jpg' },
    },
  ];

  it('renders food items when `foodItems` is provided', () => {
    render(<FoodList foodItems={mockFoodItems} banner={null} />);

    // Check that the banner for common food items is displayed
    expect(screen.getByText(/Common Food Items/i)).toBeInTheDocument();

    // Check that the correct number of food items is rendered
    const foodItemElements = screen.getAllByTestId('food-item');
    expect(foodItemElements).toHaveLength(2);

    // Check that the correct food names are displayed
    expect(screen.getByText(/Apple/i)).toBeInTheDocument();
    expect(screen.getByText(/Banana/i)).toBeInTheDocument();
  });

  it('renders a banner when `banner` is provided and `foodItems` is empty', () => {
    const bannerMessage = 'No food items available';

    render(<FoodList foodItems={[]} banner={bannerMessage} />);

    // Check that the banner message is displayed
    expect(screen.getByText(bannerMessage)).toBeInTheDocument();
  });

  it('renders nothing when `foodItems` is empty and `banner` is null', () => {
    const { container } = render(<FoodList foodItems={[]} banner={null} />);

    // Ensure no content is rendered
    expect(container).toBeEmptyDOMElement();
  });
});
