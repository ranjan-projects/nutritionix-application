import React, { createContext, useState } from 'react';

// Create a context with a default value
const GlobalContext = createContext();

const GlobalProvider = ({ children }) => {
    const [globalObject, setGlobalObject] = useState(null);
    return (
        <GlobalContext.Provider value={{ globalObject, setGlobalObject }}>
            {children}
        </GlobalContext.Provider>
    );
};

export { GlobalContext, GlobalProvider };
