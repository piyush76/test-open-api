module.exports = {
  default: {
    require: ['acceptance-tests/step_definitions/**/*.js'],
    format: ['progress', 'json:reports/cucumber-report.json'],
    paths: ['acceptance-tests/features/**/*.feature'],
    parallel: 1
  }
};
