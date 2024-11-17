import React, { useContext, useState } from 'react';
import Button from 'react-bootstrap/Button';
import Card from 'react-bootstrap/Card';
import '../asset/css/NutritionCard.css';
import { GlobalContext } from './GlobalContext';
import axios from 'axios';

const NutritionCard = ({ item, setFavouriteItems, setBanner }) => {
    console.log("Favourite to show is:" + JSON.stringify(item));
    const { globalObject } = useContext(GlobalContext);
    const [message, setMessage] = useState(null);

    const removeFavourite = async (foodName) => {
        try {
            const headers = {
                'Authorization': "Bearer " + globalObject.authToken
            };

            const response = await axios.delete("http://localhost:8080/wishlist-services/api/wishlist/" + foodName, { headers });
            console.log('Response:', response.data);
            const favouriteResponse = await axios.get("http://localhost:8080/wishlist-services/api/wishlist", { headers });
            console.log('Response:', favouriteResponse.data);
            setFavouriteItems(favouriteResponse.data);
            setBanner("Food successfully removed from the favourite list");
        } catch (error) {
            setMessage('Error fetching data');
        }
    };

    const handleSubmit = (item) => {
        console.log("Favourite to remove is:" + JSON.stringify(item));
        console.log('Submitted for removal of : ' + item.item.food_name);
        removeFavourite(item.item.food_name);
    };

    return (
        <div className='item'>
            <Card>
                {message ? <Card.Text className='text-bg-success messageBanner w-100'> {message}</Card.Text> : ''}
                <Card.Img variant="top" src={item.photo_url} />
                <Card.Body>
                    <Card.Title>{item.food_name}</Card.Title>
                    <Card.Text>Serving quantity: {item.serving_qty}</Card.Text>
                    <Button variant="primary" onClick={handleSubmit.bind(this, { item })} >Remove Favourite</Button>
                </Card.Body>
            </Card>
        </div>
    )
}

export default NutritionCard;