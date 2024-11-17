import React from 'react';
import '../asset/css/Footer.css';
import { Container, Row, Col } from 'react-bootstrap';

export default function Footer() {
  return (
    <footer className="fixed-bottom bg-dark text-white text-center">
      <Container>
        <Row>
          <Col md="12">
            <p>© 2024 Nutritionix. All Rights Reserved.</p>
          </Col>
        </Row>
      </Container>
    </footer>
  )
}
