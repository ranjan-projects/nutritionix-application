import React from 'react';
import slide1 from '../asset/images/slide-4.webp';
import slide2 from '../asset/images/slide-6.jpg';
import slide3 from '../asset/images/slide-7.jpg';
import '../asset/css/Home.css';

import { MDBCarousel, MDBCarouselItem, MDBCarouselCaption } from 'mdb-react-ui-kit';

export default function Home() {
    return (

        <div className='container home-container'>

            <MDBCarousel showControls showIndicators>
                <MDBCarouselItem itemId={1}>
                    <img src={slide1} className='w-60' alt='...' />
                    <MDBCarouselCaption className='carousel-caption'>
                        <h1>Health is wealth</h1>
                        <p>Empowering Health with Every Byte</p>
                    </MDBCarouselCaption>
                </MDBCarouselItem>
                <MDBCarouselItem itemId={2}>
                    <img src={slide2} className='w-60' alt='...' />

                    <MDBCarouselCaption>
                        <h1>Discover What's Inside Your Food</h1>
                        <p>Every food contains some calorois, so you should know what you are eating</p>
                    </MDBCarouselCaption>
                </MDBCarouselItem>
                <MDBCarouselItem itemId={3}>
                    <img src={slide3} className='w-60' alt='...' />
                    <MDBCarouselCaption>
                        <h1>Know Your Food, Know Your Health</h1>
                        <p>If you keep track of your calories, you can make sure that you are healthy</p>
                    </MDBCarouselCaption>
                </MDBCarouselItem>
            </MDBCarousel>

        </div>
    )
}
