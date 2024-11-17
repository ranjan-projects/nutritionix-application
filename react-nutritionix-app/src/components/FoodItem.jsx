import React, { useContext, useState } from 'react';
import Button from 'react-bootstrap/Button';
import Card from 'react-bootstrap/Card';
import axios from 'axios';

import '../asset/css/FoodItem.css'
import { GlobalContext } from './GlobalContext';

const FoodItem = ({ item }) => {
    const { globalObject } = useContext(GlobalContext);
    const [error, setError] = useState(null);
    const [success, setSuccess] = useState(null);

    const addFavourite = async (itemData) => {

        const item = itemData.item;

        if (!globalObject) {
            console.log('User is not logged in');
            alert('Please login first to add items to favourites');
            return;
        }

        try {
            console.log("Request payload data is " + JSON.stringify(item));

            const requestData = {
                serving_unit: item.serving_unit,
                serving_qty: item.serving_qty,
                tag_id: item.tag_id,
                tag_name: item.tag_name,
                food_name: item.food_name,
                common_type: item.common_type,
                photo_url: item.photo.thumb,
                locale: item.locale
            };

            const headers = {
                'Authorization': "Bearer " + globalObject.authToken
            };

            await axios.post("http://localhost:8080/wishlist-services/api/wishlist", requestData, { headers });

            setSuccess('Successfully added to favourite');
        } catch (error) {
            const responsePayload = error.response.data;
            setError(responsePayload.message);
            setSuccess(null);
        }
    };

    const handleSubmit = (item) => {
        console.log('Submitted for addition: ' + item);
        addFavourite(item);
    };

    return (
        <div className='search-item'>
            <Card>
                <Card.Text className='bg-danger text-white'>{error}</Card.Text>
                <Card.Text className='bg-success text-white'>{success}</Card.Text>
                <Card.Img variant="top" src={item.photo.thumb} />
                <Card.Body>
                    <Card.Title>{item.food_name}</Card.Title>
                    <Card.Text>Serving quantity: {item.serving_qty}</Card.Text>
                    <Button variant="primary" onClick={handleSubmit.bind(this, { item })}>Add to Favourite</Button>
                </Card.Body>
            </Card>
        </div>
    );
};

export default FoodItem;
