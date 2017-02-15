# Repository usage tips:

## Branches
Branches naming convention: {issue number}-kebab-case-issue-description

Example: 54-travis-integration

## Commits
Commit message convention: {issue number}: {what was done in the commit}

Example: 54: Change android sdk version

## Pull requests
Pull request naming convention: {issue number}: {issue description}

- Every pull request should contain description what was changed and how it affects the project. Some examples would be nice if code behaviour is not clear.
- Code changes should be as small as possible and should address issue reported in backlog.
- Commits should be squashed if it is possible, or they will be squashed with merging(github option).
- Every pull request should be commented by developers until there are not any more changes to add, it should be approved. Approving changes should be provided with(for main contributor: clicking approve button) adding +1 to pull request main info.
- If you have made initial testing of the pull request, please add a comment about it, and if any bug-issues were found to this, report them in a comment.

If you would like to create pull request for nonexistent issue, please create it before. One of main contributors will add label and milestone for it or it will be discussed to clarify.

## Issues
Issue naming convention: {Shortcut of what is the point of issue}

- Issue description should contain what is the main point of issue and required scenarios to cover.
- Every issue should have list of cases that should be tested during testing process. All of them should be covered in code.
- One issue can be prepared and updated all the time until it is finished and closed by one of the contributors. Every issue could and should be discussed in comments. Do not hesitate to ask questions.
- If discussion shows that the issue could be split to smaller and separated tasks, it should be done. Every issue should be as small as possible for better understanding, easier code review and testing.

## Documentation
Documentation files naming convention: kebab-case-file-name

- All documents should be stored in folder doc on repository and after it is marked as done, wiki should be updated with these changes. There is a risk of inconsistent of wiki and doc folder, but it is responsibility for project main contributors to keep wiki updated.
- Due to the fact that wiki is the first place where new developers look in and pull requests are not able to be done to wiki, every pull request to documentation should be done to doc folder on repo.
- We should store documents and images files together in doc folder.
