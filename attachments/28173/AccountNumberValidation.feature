Feature: Account Number Validation

  Scenario Outline: Invalid Account Number Entry With Have An Account Selected
    Given I have entered <account number invalid entry> into the quote form account number field
    And I have selected I have an account on the quote form
    When the account number field on the quote form is validated
    Then the error message "request.quote.account.invalid" is set against account number field

    Examples: 
      | account number invalid entry |
      | "a@a.a"                      |
      | "te$t"                       |
      | "näme"                       |
      | "nâme"                       |
      | "name!"                      |
      | "nàme"                       |
      | "na\\me"                     |
