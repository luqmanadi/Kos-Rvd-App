const js = require("@eslint/js");
const {FlatCompat} = require("@eslint/eslintrc");

const compat = new FlatCompat({
  baseDirectory: __dirname,
  recommendedConfig: js.configs.recommended,
  allConfig: js.configs.all,
});

module.exports = [
  ...compat.config({
    env: {
      es2020: true,
      node: true,
    },
    parserOptions: {
      ecmaVersion: 2020,
    },
    extends: [
      "eslint:recommended",
      "google",
    ],
    rules: {
      "no-restricted-globals": ["error", "name", "length"],
      "prefer-arrow-callback": "error",
      "quotes": ["error", "double", {allowTemplateLiterals: true}],

      // ESLint v9: legacy rules removed, must be disabled
      "require-jsdoc": "off",
      "valid-jsdoc": "off",
    },
    overrides: [
      {
        files: ["**/*.spec.*"],
        env: {mocha: true},
        rules: {},
      },
    ],
    globals: {},
  }),
];
