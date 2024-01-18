# Spring Fox Ticket Project repository

## Initial dependencies

- Java Development Kit - JDK 17

## Development rules

- Do not use `Lombok`
- Do not create getters/setters which are not used
- Make *everything* configurable (e.g. via values in `.env` and `application.properties`), i.e. no constant value should be hard-coded. The database credentials are already pre-configured this way. See [MATERIALS#Dotenv](MATERIALS.md#dotenv)
- Place contents into classes according to this orderedItem:
  - Final fields
  - Fields
  - Default constructor
  - Constructors with parameters
  - Getters & Setters
  - Public methods
  - Private methods
- Use Data Transfer Objects (Dto's) for request/response content (e.g. create a `dtos` package (do not put this package into the `models` package)
- Naming
  - Entities/Models
    - Use camelCase for Java properties and snake_case for the corresponding database column names. For example `private String activationToken` in the User model should be mapped to the `activation_token` column in the `users` table
  - Database:
    - Use plurals for database tables, e.g. `users`, `posts`, `likes` (and singular for the corresponding models names, i.e. `User`, `Post`, `Like`)
    - Use `@GeneratedValue(strategy = GenerationType.IDENTITY)` for auto-incremented fields
  - Tests: use descriptive (test) method names in snake_case (to improve readability):
    - [MethodName_StateUnderTest_ExpectedBehavior]
    - like: `checkCredentialValidity_WithValidCredentials_ReturnsTrue()`
    - in case of endpoints, instead of method name, use descriptive name for the endopint
  - Endpoints: use all lowercase letters and '-' for spaces
    - : `/user/vouchers`
    - : `/forgot-password`
  - Create descriptive branch names, e.g. feature-user-registration (include short description of the task)
- All error handling should be done via exceptions (and `@ControllerAdvice`).
- After global exception handler is present, create and throw custom exceptions in error scenarios
- Use the object wrapper for primitive types, e.g. `Long` instead of `long`
- Use `this` keyword only to avoid variable name conflicts
- Use the [code formatting](https://blog.jetbrains.com/idea/2020/06/code-formatting/) feature in Intellij (CTRL+ALT+L / ⌥⌘L)
- Have at least 90% test coverage regarding services (unit test) and controllers (integration tests)
- Use [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
  - Have a look [here](https://stackoverflow.com/questions/42979700/how-to-configure-google-java-code-formatter-in-intellij-idea-17) on how to configure the google java style guides in IntelliJ
  - Make sure to use braces {} with `if`, `else`, `for`, `do` and `while` statements, even when the body is empty or contains only a single statement.

## Processes

- Push only when *all* tests and style checks have passed
- Make sure there are no unresolved conflicts
- On mentor time present new technologies if you have a relevant PR accepted
- Provided you need a tech Task for something, feel free to create it and assign it to your mentor

## Useful links

Project details:

- examples is docs folder

Jira board:

- https://greenfoxacademy.atlassian.net/jira/software/projects/ATBFB/boards/251

Contribution:

- [CONTRIBUTING.md](CONTRIBUTING.md)

Commit messages:

- https://chris.beams.io/posts/git-commit/

`If applied, this commit will ... [commit message]`

Git cheat sheet

https://docs.google.com/spreadsheets/d/1Y6ylJLSbkUqLzn9kN_rQSgDlssRpR-ZFresF46apFWY

## Git Workflow

See [CONTRIBUTING](CONTRIBUTING.md)

### Start New Feature/Bugfix

In orderedItem to minimize merge conflicts later always open a new feature branch from the most recent state of the `master` branch on GitHub.

- `git pull`
- `git checkout -b <branch_name> origin/development`

### Update Feature Branch

While you're working on your own feature/bugfix other developers make changes on `master` and it's required to update your branch to keep the consistency of the codebase. You can do this in 2 ways.

[`git merge` vs `git rebase`](https://www.atlassian.com/git/tutorials/merging-vs-rebasing)

#### Rebase

[`git rebase`](https://www.atlassian.com/git/tutorials/rewriting-history/git-rebase)

Rebase rewrites commit history; therefore, do not use rebase on the `master` branch.
On the other hand, feel free to use rebase on your own branches.

Use `git rebase master` while on your branch.

#### Merge

[`git merge`](https://www.atlassian.com/git/tutorials/using-branches/git-merge)

This creates a new commit (so-called merge commit) containing changes from both your branch and the development branch.

Use `git merge master` while on your branch.
