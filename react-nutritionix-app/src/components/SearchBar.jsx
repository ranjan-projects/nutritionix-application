import React, { useState } from 'react';
import axios from 'axios';
import FoodList from './FoodList';
import { Form, Button, Alert } from 'react-bootstrap';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';

import '../asset/css/SearchBar.css';

const SearchBar = () => {
    const [query, setQuery] = useState('');
    const [foodItems, setFoodItems] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [message, setMessage] = useState(null);

    const fetchFoodData = async (query) => {
        setLoading(true);
        setError(null);
        try {
            const response = await axios.get("http://localhost:8080/nutrition-services/api/nutrition/search/" + query);
            setFoodItems(response.data.common);

            if (foodItems.length === 0) {
                setMessage('No food items found.');
            }
        } catch (err) {
            const responsePayload = err.response.data;
            console.log("Search failed with error: " + responsePayload.message)
            setMessage(responsePayload.message);
            setFoodItems([]);
        } finally {
            setLoading(false);
        }
    };

    const handleSearch = (e) => {
        e.preventDefault();

        if (!query) {
            setError('Please enter food item to search.');
            setMessage('');
            return;
        }

        fetchFoodData(query);
    };

    return (
        <div>
            <div className='container seachitem'>
                <Form inline onSubmit={handleSearch}>
                    <Row>
                        <Col>
                            <Form.Control
                                type="text"
                                placeholder="Search"
                                value={query}
                                onChange={(e) => setQuery(e.target.value)}
                                className=""
                            />
                            {error && <Alert variant="danger">{error}</Alert>}
                        </Col>
                        <Col xs="auto">
                            <Button type="submit">Submit</Button>
                        </Col>
                    </Row>
                </Form>

            </div>
            {loading && <p>Loading...</p>}
            <div>


            </div>
            <FoodList foodItems={foodItems} banner={message} />
        </div>

    );
};

export default SearchBar;
