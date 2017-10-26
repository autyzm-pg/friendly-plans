# Contributing to Friendly Plans
## About
Friendly Plans is an application supporting autism treatment.
It provides digital version of activity schedules. More about treatment (ENG): [PCDI website](http://www.pcdi.org/resources/videos.html)

The app is being used by group of users and therapists. Everyone can download it (version 1.x) from [google play store](https://play.google.com/store/apps/details?id=com.przyjaznydamianek&hl=pl).

The article and short film show the usage of app in Institute for Child Development in Gdańsk:

- [Film](https://www.youtube.com/watch?v=zI0ma_XnmCc)

- [Article](http://iwrd.pl/pl/fundacja/aplikacja-przyjazny-plan-dostepna)

The project was started by students from Gdańsk University of Technology. First version of app is still available in google play store and it's source code is on branch v1.x but it's legacy now. Currently we are working on Friendly Plans 2.0
## License
The project is developing by people who want to make other people's life easier, so Friendly Plans are going to be free to use, download and develop for everybody forever.
So, except as otherwise noted, this software is licensed under the [GNU General Public License, v3](https://www.gnu.org/licenses/gpl-3.0.txt)
## Join
If you would like to become a part of community, you are welcome to join and contribute. Your help is needed. It is easy to contact by [project's forum](http://autyzm.eti.pg.gda.pl/forum/) or by conversations in [issues](https://github.com/autyzm-pg/friendly-plans/issues).
## Workflow
In project we are using [GitHub Flow](https://guides.github.com/introduction/flow/). Please make pull requests to master branch.
## Start
Great place to start are project's [wiki pages](https://github.com/autyzm-pg/friendly-plans/wiki/Getting-started)

Making a long story short:

- For questions and bugs please use [Github Issues](https://github.com/autyzm-pg/friendly-plans/issues)
- Feel free to grab an issue and propose solution or answer
- To share your changes please make pull request to master branch
- Write and read project documentation on wiki pages

# Repository usage tips:

## Branches
Branches naming convention: {issue number}-kebab-case-issue-description

Example: 54-travis-integration

## Commits
Commit message convention: #{issue number}: {what was done in the commit}

Example: #54: Change android sdk version

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

# Code style guidelines
## General guidelines
In general please follow guides from Google:

[Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)

There is one important exception from rules described in Google's document: In point 7 (Javadoc), they suggest using Javadoc for every public class and every public or protected member of such a class unless purpose of the existence such method or field  is simple obvious. I'd like to encourage you to writing code which is always simple and obvious instead of using Javadoc.

## Database naming guidelines
Both for table and column names we're using snake_case.

## Android specific guidelines
Guidelines in this paragraph has been taken from (Apache License): [ribot repo](https://github.com/ribot/android-guidelines/blob/master/project_and_code_guidelines.md)

### Resources files

Resources file names are written in __lowercase_underscore__.

#### Drawable files

Naming conventions for drawables:


| Asset Type   | Prefix            |		Example               |
|--------------| ------------------|-----------------------------|
| Action bar   | `ab_`             | `ab_stacked.9.png`          |
| Button       | `btn_`	            | `btn_send_pressed.9.png`    |
| Dialog       | `dialog_`         | `dialog_top.9.png`          |
| Divider      | `divider_`        | `divider_horizontal.9.png`  |
| Icon         | `ic_`	            | `ic_star.png`               |
| Menu         | `menu_	`           | `menu_submenu_bg.9.png`     |
| Notification | `notification_`	| `notification_bg.9.png`     |
| Tabs         | `tab_`            | `tab_pressed.9.png`         |

Naming conventions for icons (taken from [Android iconography guidelines](http://developer.android.com/design/style/iconography.html)):

| Asset Type                      | Prefix             | Example                      |
| --------------------------------| ----------------   | ---------------------------- |
| Icons                           | `ic_`              | `ic_star.png`                |
| Launcher icons                  | `ic_launcher`      | `ic_launcher_calendar.png`   |
| Menu icons and Action Bar icons | `ic_menu`          | `ic_menu_archive.png`        |
| Status bar icons                | `ic_stat_notify`   | `ic_stat_notify_msg.png`     |
| Tab icons                       | `ic_tab`           | `ic_tab_recent.png`          |
| Dialog icons                    | `ic_dialog`        | `ic_dialog_info.png`         |

Naming conventions for selector states:

| State	       | Suffix          | Example                     |
|--------------|-----------------|-----------------------------|
| Normal       | `_normal`       | `btn_order_normal.9.png`    |
| Pressed      | `_pressed`      | `btn_order_pressed.9.png`   |
| Focused      | `_focused`      | `btn_order_focused.9.png`   |
| Disabled     | `_disabled`     | `btn_order_disabled.9.png`  |
| Selected     | `_selected`     | `btn_order_selected.9.png`  |


#### Layout files

Layout files should match the name of the Android components that they are intended for but moving the top level component name to the beginning. For example, if we are creating a layout for the `SignInActivity`, the name of the layout file should be `activity_sign_in.xml`.

| Component        | Class Name             | Layout Name                   |
| ---------------- | ---------------------- | ----------------------------- |
| Activity         | `UserProfileActivity`  | `activity_user_profile.xml`   |
| Fragment         | `SignUpFragment`       | `fragment_sign_up.xml`        |
| Dialog           | `ChangePasswordDialog` | `dialog_change_password.xml`  |
| AdapterView item | ---                    | `item_person.xml`             |
| Partial layout   | ---                    | `partial_stats_bar.xml`       |

A slightly different case is when we are creating a layout that is going to be inflated by an `Adapter`, e.g to populate a `ListView`. In this case, the name of the layout should start with `item_`.

Note that there are cases where these rules will not be possible to apply. For example, when creating layout files that are intended to be part of other layouts. In this case you should use the prefix `partial_`.

#### Menu files

Similar to layout files, menu files should match the name of the component. For example, if we are defining a menu file that is going to be used in the `UserActivity`, then the name of the file should be `activity_user.xml`

A good practice is to not include the word `menu` as part of the name because these files are already located in the `menu` directory.

#### Values files

Resource files in the values folder should be __plural__, e.g. `strings.xml`, `styles.xml`, `colors.xml`, `dimens.xml`, `attrs.xml`

#### Tests

@Before and @After should be defined after the class variables. @Test should be below.
Methods naming conventions:
@Before - setUp()
@After - tearDown()
@Test - whenCreateTaskExpectTaskAddedToDb(), camelcase with schema when...Expect..()

## Import code style settings to Android Studio
Tips in this paragraph has been taken from (Apache License): [Metanome wiki](https://github.com/HPI-Information-Systems/Metanome/wiki/Installing-the-google-styleguide-settings-in-intellij-and-eclipse)

Download the intellij-java-google-style.xml file from the http://code.google.com/p/google-styleguide/ repo.

### Windows

Copy it into your config/codestyles folder in your IntelliJ settings folder. Under Settings -> Editor -> Code Style select the google-styleguide as current code style for the Metanome project.

### Macintosh

Download it and go into Preferences -> Editor -> Code Style. Click on Manage and import the downloaded Style Setting file. Select GoogleStyle as new coding style.
