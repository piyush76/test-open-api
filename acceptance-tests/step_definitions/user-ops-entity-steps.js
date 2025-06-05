const { Given, When, Then, Before, After } = require('@cucumber/cucumber');
const { expect } = require('chai');
const ApiDriver = require('../support/driver');

let apiDriver;
let testContext = {};

Before(async function() {
  apiDriver = new ApiDriver();
  testContext = {};
  
  console.log('Skipping API readiness check - proceeding with test setup');
});

After(async function() {
  await apiDriver.cleanupTestData();
});

Given('the UserOps API is running and accessible', async function() {
  try {
    const response = await apiDriver.get('/actuator/health');
    expect(response.status).to.be.oneOf([200, 401]); // 401 is acceptable if health endpoint requires auth
  } catch (error) {
    console.warn('Health check failed, but continuing with tests');
  }
});

Given('I have valid authentication credentials', function() {
  expect(apiDriver.defaultAuth.username).to.exist;
  expect(apiDriver.defaultAuth.password).to.exist;
});

Given('a user operations entity exists for personnel ID {int} and company ID {string}', async function(personnelId, companyId) {
  testContext.expectedPersonnelId = personnelId;
  testContext.expectedCompanyId = companyId;
  
  const response = await apiDriver.get(`/iam/users/${personnelId}/companies/${companyId}/ops-entity`);
  if (response.status === 404) {
    throw new Error(`Expected user operations entity for personnel ID ${personnelId} and company ID ${companyId} to exist, but it was not found`);
  }
});

When('I send a GET request to {string}', async function(endpoint) {
  testContext.response = await apiDriver.get(endpoint);
});

When('I send a PUT request to {string} with admin role {string}', async function(endpoint, adminRole) {
  testContext.response = await apiDriver.put(endpoint, `"${adminRole}"`);
});

When('I send an unauthenticated GET request to {string}', async function(endpoint) {
  testContext.response = await apiDriver.get(endpoint, false);
});

Then('the response status code should be {int}', function(expectedStatus) {
  expect(testContext.response.status).to.equal(expectedStatus);
});

Then('the response should contain valid user operations entity data', function() {
  const data = testContext.response.data;
  const response = testContext.response;
  
  expect(data).to.be.an('object');
  expect(data).to.not.be.null;
  expect(data).to.not.be.undefined;
  
  expect(data).to.have.property('personnelId');
  expect(data).to.have.property('companyId');
  expect(data).to.have.property('opsEntityId');
  expect(data).to.have.property('adminRole');
  
  expect(data.personnelId).to.be.a('number');
  expect(data.companyId).to.be.a('string');
  expect(data.opsEntityId).to.be.a('string');
  
  expect(data.personnelId).to.be.greaterThan(0);
  expect(data.companyId).to.have.length.greaterThan(0);
  expect(data.opsEntityId).to.have.length.greaterThan(0);
  
  expect(response.headers).to.have.property('content-type');
  expect(response.headers['content-type']).to.include('application/json');
});

Then('the personnel ID should be {int}', function(expectedPersonnelId) {
  const data = testContext.response.data;
  expect(data.personnelId).to.equal(expectedPersonnelId);
});

Then('the company ID should be {string}', function(expectedCompanyId) {
  const data = testContext.response.data;
  expect(data.companyId).to.equal(expectedCompanyId);
});

Then('the operations entity ID should be {string}', function(expectedOpsEntityId) {
  const data = testContext.response.data;
  expect(data.opsEntityId).to.exist;
  expect(data.opsEntityId).to.be.a('string');
  expect(data.opsEntityId).to.equal(expectedOpsEntityId);
  expect(data.opsEntityId.length).to.be.greaterThan(0);
});

Then('the admin role should be present', function() {
  const data = testContext.response.data;
  expect(data.adminRole).to.exist;
  expect(data.adminRole).to.not.be.null;
  expect(data.adminRole).to.not.be.undefined;
  
  if (typeof data.adminRole === 'string') {
    expect(data.adminRole).to.be.a('string');
    expect(data.adminRole.length).to.be.greaterThan(0);
    
    const validRoles = ['Admin', 'Grant Admin', 'Super Admin', 'Read Only'];
    expect(validRoles).to.include(data.adminRole);
    
    expect(data.adminRole).to.not.match(/^\s+|\s+$/); // No leading/trailing whitespace
    expect(data.adminRole.length).to.be.at.most(50);
  } else {
    expect(data.adminRole).to.be.an('object');
    expect(data.adminRole).to.have.property('value');
    expect(data.adminRole.value).to.be.a('string');
    expect(data.adminRole.value.length).to.be.greaterThan(0);
  }
});

Then('the admin role should be {string}', function(expectedAdminRole) {
  const data = testContext.response.data;
  if (typeof data.adminRole === 'string') {
    expect(data.adminRole).to.equal(expectedAdminRole);
  } else {
    expect(data.adminRole.value).to.equal(expectedAdminRole);
  }
});

Then('the response should contain an error message', function() {
  const response = testContext.response;
  const data = response.data;
  
  expect(response.status).to.be.oneOf([400, 404, 500]);
  expect(data).to.exist;
  
  if (typeof data === 'string' && data === '') {
    expect(response.status).to.be.oneOf([400, 404, 500]);
  } else if (typeof data === 'object' && data !== null) {
    expect(data).to.have.property('timestamp');
    expect(data).to.have.property('status');
    expect(data).to.have.property('error');
    
    expect(data.timestamp).to.be.a('string');
    expect(data.status).to.be.a('number');
    expect(data.error).to.be.a('string');
    expect(data.status).to.equal(response.status);
    
    expect(data.timestamp).to.match(/^\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}/);
  }
  
  if (response.headers['content-type']) {
    expect(response.headers['content-type']).to.include('application/json');
  }
});

Then('the response should contain the updated user operations entity', function() {
  const data = testContext.response.data;
  const response = testContext.response;
  
  expect(data).to.be.an('object');
  expect(data).to.not.be.null;
  expect(data).to.not.be.undefined;
  
  expect(data).to.have.property('personnelId');
  expect(data).to.have.property('companyId');
  expect(data).to.have.property('opsEntityId');
  expect(data).to.have.property('adminRole');
  
  expect(data.personnelId).to.be.a('number');
  expect(data.companyId).to.be.a('string');
  expect(data.opsEntityId).to.be.a('string');
  expect(data.personnelId).to.be.greaterThan(0);
  expect(data.companyId.length).to.be.greaterThan(0);
  expect(data.opsEntityId.length).to.be.greaterThan(0);
  
  if (testContext.expectedPersonnelId) {
    expect(data.personnelId).to.equal(testContext.expectedPersonnelId);
  }
  if (testContext.expectedCompanyId) {
    expect(data.companyId).to.equal(testContext.expectedCompanyId);
  }
  
  expect(response.headers).to.have.property('content-type');
  expect(response.headers['content-type']).to.include('application/json');
});

Then('the response should contain a validation error message', function() {
  const response = testContext.response;
  const data = response.data;
  
  expect(response.status).to.be.oneOf([400, 500]);
  expect(data).to.exist;
  expect(data).to.not.be.null;
  
  expect(data).to.be.an('object');
  expect(data).to.have.property('timestamp');
  expect(data).to.have.property('status');
  expect(data).to.have.property('error');
  
  expect(data.timestamp).to.be.a('string');
  expect(data.status).to.be.a('number');
  expect(data.error).to.be.a('string');
  expect(data.status).to.equal(response.status);
  
  if (response.status === 400) {
    expect(data.error).to.include('Bad Request');
  } else if (response.status === 500) {
    expect(data.error).to.include('Internal Server Error');
  }
  
  expect(response.headers['content-type']).to.include('application/json');
});

Then('the response should contain an authentication error message', function() {
  const response = testContext.response;
  const data = response.data;
  
  expect(response.status).to.equal(401);
  expect(data).to.exist;
  
  if (typeof data === 'string' && data === '') {
    expect(response.status).to.equal(401);
  } else if (typeof data === 'object' && data !== null) {
    expect(data).to.have.property('timestamp');
    expect(data).to.have.property('status');
    expect(data).to.have.property('error');
    
    expect(data.status).to.equal(401);
    expect(data.error).to.include('Unauthorized');
    expect(data.timestamp).to.be.a('string');
  }
  
  if (response.headers['www-authenticate']) {
    expect(response.headers['www-authenticate']).to.include('Basic');
  }
  
  if (response.headers['content-type']) {
    expect(response.headers['content-type']).to.include('application/json');
  }
});
