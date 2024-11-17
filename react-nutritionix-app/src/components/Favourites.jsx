import React, { useContext, useEffect, useState } from 'react';
import axios from 'axios';
import NutritionCard from './NutritionCard';
import '../asset/css/Favourites.css';
import { Link } from 'react-router-dom';
import { GlobalContext } from './GlobalContext';

const Favourites = () => {

    const { globalObject } = useContext(GlobalContext);
    const [error, setError] = useState(null);
    const [banner, setBanner] = useState(null);
    const [favouriteItems, setFavouriteItems] = useState([]);
    console.log("Global object is: " + JSON.stringify(globalObject));

    const getFavourite = async () => {
        try {
            const headers = {
                'Authorization': "Bearer " + globalObject.authToken
            };

            const response = await axios.get("http://localhost:8080/wishlist-services/api/wishlist", { headers });
            console.log('Response:', response.data);
            setFavouriteItems(response.data);

        } catch (error) {
            if (error.response) {
                const responsePayload = error.response.data;
                console.log("Failed to load favourites with error: " + responsePayload.message)
                setBanner(responsePayload.message);
                setFavouriteItems([]);
            }

        }
    };

    useEffect(() => {
        getFavourite();
    }, []);

    if (!globalObject) {
        console.log('User is not logged in');
        return <h6 className='text-bg-dark favourite-banner'>Please login to check favourites. &nbsp;
            <Link to="/login" className='text-white'>Login now</Link> </h6>
    } else {
        if (favouriteItems.length === 0) {
            return <h6 className='text-bg-dark favourite-banner'>Nothing added to favourites till now. &nbsp;
                <Link to="/checknutrition" className='text-white'>Add now</Link> </h6>
        } else {
            return <div>
                {banner ? <h6 className='text-bg-dark favourite-banner'>{banner}</h6> : ''}
                <div className='container items'>
                    {favouriteItems.map((item) => <NutritionCard key={item.id} item={item} setFavouriteItems={setFavouriteItems}
                        setBanner={setBanner} />)}

                </div>
            </div>
        }
    }
}

export default Favourites;
