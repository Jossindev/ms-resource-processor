Feature: Resource Processing

  Scenario: Successfully process a message with a valid resource ID
    Given a resource with ID "1"
    When the message with resource ID "1" is received
    Then the resource should be processed successfully
