import React, { useContext } from 'react';
import { render, screen } from '@testing-library/react';
import '@testing-library/jest-dom/extend-expect'; // for better assertions
import { GlobalContext, GlobalProvider } from './GlobalContext';

// Test component that consumes the GlobalContext
const TestComponent = () => {
    const { globalObject, setGlobalObject } = useContext(GlobalContext);

    return (
        <div>
            <p data-testid="global-object">
                {globalObject ? JSON.stringify(globalObject) : 'No global object'}
            </p>
            <button onClick={() => setGlobalObject({ user: 'John Doe', authToken: 'abc123' })}>
                Set Global Object
            </button>
        </div>
    );
};

describe('GlobalProvider Component', () => {
    it('provides default null value for globalObject', () => {
        render(
            <GlobalProvider>
                <TestComponent />
            </GlobalProvider>
        );

        // Ensure that the globalObject is initially null
        expect(screen.getByTestId('global-object')).toHaveTextContent('No global object');
    });

    it('updates globalObject when setGlobalObject is called', () => {
        render(
            <GlobalProvider>
                <TestComponent />
            </GlobalProvider>
        );

        // Click the button to update globalObject
        screen.getByText('Set Global Object').click();

        // Ensure that globalObject is updated with new values
        expect(screen.getByTestId('global-object')).toHaveTextContent(
            '{"user":"John Doe","authToken":"abc123"}'
        );
    });
});
