import React from 'react';
import FoodItem from './FoodItem';
import '../asset/css/FoodList.css'

const FoodList = ({ foodItems, banner }) => {

    if (foodItems.length > 0) {
        return <div>
            <h6 className='text-bg-dark messageBanner'>Common Food Items</h6>
            <div className='container item_list'>
                {foodItems.map((item) => <FoodItem key={item.food_name} item={item} />)}
            </div></div>

    }

    if (banner === null) {
        return '';
    } else {
        return <h6 className='messageBanner'>{banner}</h6>
    }
};

export default FoodList;
