# Testing

Repository testing conventions and how to run tests for this project.

JUnit: 5.x
Mocking: Mockito

Folder layout (root-level `Testing` directory):
- `Testing/business`  -> business layer unit tests
- `Testing/data`      -> data layer tests (use mocking for DB access)
- `Testing/presentation` -> presentation layer tests (headless or mocked UI)

Guidelines:
- Tests must be placed under the repository-level `Testing` folder (parallel to `src`).
- Write tests against interfaces so they are swappable.
- Use JUnit 5 for test cases and Mockito for mocking dependencies.
- Keep tests clear about positive, negative, and boundary cases.

Running tests:
- If you use an IDE, mark `Testing` as a test source root and run tests normally.
- If using a build tool (Maven/Gradle), configure test source directory to include `Testing`.

Example layout and minimal sample test are included in `Testing/business`.

Acceptance criteria for this issue:
- `Testing/business`, `Testing/data`, and `Testing/presentation` exist at repo root.
- `Testing/README.md` documents conventions and how to run tests.
- A sample test compiles under JUnit 5.
