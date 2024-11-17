import React from 'react';
import { render, screen } from '@testing-library/react';
import '@testing-library/jest-dom/extend-expect'; // for better assertions
import Footer from './Footer';

describe('Footer Component', () => {
  it('renders the footer with correct text', () => {
    render(<Footer />);

    // Check if the footer text is present
    expect(screen.getByText(/© 2024 Nutritionix. All Rights Reserved./i)).toBeInTheDocument();

    // Check if the footer contains the correct classes for styling
    const footerElement = screen.getByRole('contentinfo'); // 'contentinfo' role is usually assigned to footers
    expect(footerElement).toHaveClass('fixed-bottom');
    expect(footerElement).toHaveClass('bg-dark');
    expect(footerElement).toHaveClass('text-white');
    expect(footerElement).toHaveClass('text-center');
  });
});
