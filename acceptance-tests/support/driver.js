const axios = require('axios');

class ApiDriver {
  constructor() {
    this.baseUrl = process.env.API_BASE_URL || 'http://localhost:8080';
    this.defaultAuth = {
      username: process.env.API_USERNAME || 'user',
      password: process.env.API_PASSWORD || 'password'
    };
    this.lastResponse = null;
  }

  async makeRequest(method, endpoint, data = null, options = {}) {
    const config = {
      method: method.toLowerCase(),
      url: `${this.baseUrl}${endpoint}`,
      validateStatus: () => true, // Don't throw on any status code
      ...options
    };

    if (data) {
      config.data = data;
      config.headers = {
        'Content-Type': 'application/json',
        ...config.headers
      };
    }

    try {
      this.lastResponse = await axios(config);
      return this.lastResponse;
    } catch (error) {
      console.error('Request failed:', error.message);
      throw error;
    }
  }

  async get(endpoint, authenticated = true) {
    const options = authenticated ? { auth: this.defaultAuth } : {};
    return this.makeRequest('GET', endpoint, null, options);
  }

  async put(endpoint, data, authenticated = true) {
    const options = authenticated ? { auth: this.defaultAuth } : {};
    return this.makeRequest('PUT', endpoint, data, options);
  }

  async post(endpoint, data, authenticated = true) {
    const options = authenticated ? { auth: this.defaultAuth } : {};
    return this.makeRequest('POST', endpoint, data, options);
  }

  async delete(endpoint, authenticated = true) {
    const options = authenticated ? { auth: this.defaultAuth } : {};
    return this.makeRequest('DELETE', endpoint, null, options);
  }

  getLastResponse() {
    return this.lastResponse;
  }

  getResponseStatus() {
    return this.lastResponse ? this.lastResponse.status : null;
  }

  getResponseData() {
    return this.lastResponse ? this.lastResponse.data : null;
  }

  async waitForApiReady(maxRetries = 30, retryDelay = 1000) {
    for (let i = 0; i < maxRetries; i++) {
      try {
        const response = await this.get('/actuator/health');
        if (response.status === 200) {
          console.log('API is ready');
          return true;
        }
      } catch (error) {
      }
      
      console.log(`Waiting for API to be ready... (attempt ${i + 1}/${maxRetries})`);
      await this.sleep(retryDelay);
    }
    
    throw new Error('API did not become ready within the expected time');
  }

  sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
  }

  async setupTestData() {
    console.log('Test data setup completed');
  }

  async cleanupTestData() {
    console.log('Test data cleanup completed');
  }
}

module.exports = ApiDriver;
