#CHANGELOG

# 11/4/2020 Changes
- Added changelog.md
- Edited `MainActivity.java`
  - Placed in `activities`package
- Added `MainContentActivity.java`
  - Added activity to house the multiple fragments that will be the meat of the
    application
- Added `DocumentAdapter.java`
  - Same as first draft's adapter, except for class name change (from `Adapter_Document.java`)
- Added `ProjectAdapter.java`
  - Same as first draft's `Adapter_Directories.java`, with name changes, both class name and
    variable/method names
- Added `Document.java`
  - Same as first draft's `Document.java`
- Added `BrowsePhotosFragment.java`
  - Barebones fragment that will handle logic for browsing and downloading photos
  - Similar to `Photos_DownloadMenu.java`
- Added `BrowseReportsFragment.java`
  - Will handle browsing and downloading reports
  - Similar to `Reports_DownloadMenu.java`
- Added `LoginFragment.java`
  - Handles logic for logging into the application
- Added `MainMenuFragment.java`
  - Actual logic for navigating main menu
- Added `SelectProjectFragment.java`
  - Similar to `SelectDirectoryActivity.java`
  - Allows users to select project to work in
- Added `SettingsAndHelpFragment.java`
  - Barebones class that will provide help (and maybe settings) to the user.
- Added string resources
  - Also organized said resources into commented blocks
- Added different icons in the `drawables` folder to provide icons to the app
- Added `activity_main_content.xml`, `fragment_browse_content.xml`, `fragment_main_menu.xml`,
  `fragment_select_directory.xml`, `recycler_view_project_instance.xml`, and
  `recycler_view_documents_instance.xml`
  - Used to provide UI to specific fragments/activities
- Added `bottom_nav_menu.xml` and `filter_sort_menu.xml`
  - Menus for users to navigate the app and select filtering and sorting for documents and photos
- Added colors
- Edited both night and light themes

# 11/5/2020 Changes
- Added more drawables
- Added string resourcs
- Edited `MainContentActivity.java`
  - Added catch to remove transactions on backstack until -1 (empty) or baseline menu
  - Began adding a way to confirm signout when user hits back on main menu
- Added `CreateNewReportFragment.java`
  - Handles logic to create a new report and upload it to Firebase Storage instance
  - Different from first draft's; have a dropdown that displays menus (loaded when fragment is created
  - in same manner as SelectProjectMenu
- Edited `fragment_main_menu.xml`
- Added `fragment_create_new_report.xml` and `fragment_upload_existing_files.xml`
- Edited `LoginFragment.java`
  - Changed anonymous listeners and methods to lambdas.
- Added the `WorkSeries` classes from last draft

# 11/8/2020 Changes
- Changed the `ic_image_menu` drawable