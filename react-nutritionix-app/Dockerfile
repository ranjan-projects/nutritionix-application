# Use an official Node.js image to build the React app
FROM node:18-alpine

# Set working directory inside the container
WORKDIR /usr/src/app

# Copy package.json and package-lock.json/yarn.lock to install dependencies
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy the rest of the React app to the container
COPY . .

# Expose port 3000 for the React app
EXPOSE 3000

# Run the React app
CMD ["npm", "start"]
