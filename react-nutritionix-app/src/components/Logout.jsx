import React, { useContext } from 'react'
import { GlobalContext } from './GlobalContext';
import { Navigate } from 'react-router-dom';

export default function Logout() {

    const { globalObject, setGlobalObject } = useContext(GlobalContext);

    setGlobalObject(null);

    return (
        <Navigate to="/" />
    )
}
