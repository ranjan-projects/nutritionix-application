import React from 'react';
import { render, screen } from '@testing-library/react';
import '@testing-library/jest-dom/extend-expect'; // for better assertions
import Home from './Home';

describe('Home Component', () => {
  it('renders the Home component with the carousel and images', () => {
    render(<Home />);

    // Check if all carousel images are rendered
    const slide1 = screen.getByAltText('...');
    expect(slide1).toBeInTheDocument();
    expect(slide1).toHaveAttribute('src', expect.stringContaining('slide-4.webp'));

    const slide2 = screen.getByAltText('...');
    expect(slide2).toBeInTheDocument();
    expect(slide2).toHaveAttribute('src', expect.stringContaining('slide-6.jpg'));

    const slide3 = screen.getByAltText('...');
    expect(slide3).toBeInTheDocument();
    expect(slide3).toHaveAttribute('src', expect.stringContaining('slide-7.jpg'));
  });

  it('renders the correct captions for each carousel item', () => {
    render(<Home />);

    // Check if the captions are rendered
    expect(screen.getByText('Health is wealth')).toBeInTheDocument();
    expect(screen.getByText('Empowering Health with Every Byte')).toBeInTheDocument();

    expect(screen.getByText("Discover What's Inside Your Food")).toBeInTheDocument();
    expect(screen.getByText('Every food contains some calorois, so you should know what you are eating')).toBeInTheDocument();

    expect(screen.getByText('Know Your Food, Know Your Health')).toBeInTheDocument();
    expect(screen.getByText('If you keep track of your calories, you can make sure that you are healthy')).toBeInTheDocument();
  });

  it('renders carousel controls and indicators', () => {
    render(<Home />);

    // Check if carousel controls are present (previous/next indicators)
    const carousel = screen.getByRole('button', { name: /next/i });
    expect(carousel).toBeInTheDocument();

    const previousButton = screen.getByRole('button', { name: /previous/i });
    expect(previousButton).toBeInTheDocument();
  });
});
