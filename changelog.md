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

# 11/6/2020 Changes, Section 1
- Added `ic_images_menu.png` files; somehow, they were deleted (or moved)
- Edited `MainContentActivity.java`
  - Changed `popBackStack()` loop to just once (program won't go beyond one on the backstack)
  - Commented out `onBackPressed(...)` to allow code to compile
  - Changed switch statement that handles changing fragments on bottom nav click to if/else if
    chain; see file update log
- Edited `CreateNewReportFragment.java`
  - Removed unused variable and imports
  - Added variable to reduce calls to a static method
  - Added check to see if backstack is -1 (empty) in `onComplete(...)` method for file upload,
    which ensure that the program won't try to pop the backstack when it's empty (the user exited
    the fragment, and thus popped the backstack, before upload was complete)
  - Simplified an if/else statement
- Edited `LoginFragment.java`
  - Changed anonymous methods/classes to lambdas
  - made certain variables final
  - Removed unused imports
- Edited `MainMenuFragment.java`
  - Changed some lambdas to expression lambdas
  - Converted certain variables from class to local
- Edited `SelectProjectFragment.java`
  - Removed unused instance singleton
- Edited `BrowseReportsFragment.java`
  - Made a method into a lambda

# 11/6/2020 Changes, Section 2
- Added string resources
- Added custom icons for main menu content card views
- Edited `fragment_main_menu.xml`
  - Added a card view to generate an accident report
  - Added card views for uploading new and existing photos
  - Added separators to divide content

# 11/6/2020 Changes, Section 3
- Changed light theme
- Edited `fragment_upload_existing_files.xml`
  - Changed layout
  - Removed commented-out/unused View

# 11/8/2020 Changes
- Added string resources
- Edited `BrowseReportsFragment.java`
  - Added `RecyclerView` instance that handles the holding of content for potential viewing
  - Added View to `fragment_browse_content.xml`
- Edited `UploadExistingReportFragment.java`
  - Added code to upload existing files to Storage
- Edited `LoginFragment.java`
  - Added code to test to see why SharedPreferences data wasn't being loaded in Upload/Generate
    Fragments (misspelt field name in Fragments files)
- Edited `fragment_create_new_report.xml`
  - Changed layout to better suit page
- Edited `ReportFactory.java`
  - Changed string resources being used in constructors/methods
- Edited `CreateNewReportFragment.java`
  - Closed `document` and `writer` instance at end of writing report to file
- Removed unused interface
- Added commenting headers and fixed header layouts
- Edited `WorkDay.java`
  - Changed init variable value

# 11/9/2020 Changes
- Edited `BrowseReportsFragment.java`
  - Added code to handle user clicking on entry in the RecyclerView
- Added `ViewPdfReportFragment.java`
  - Used to view PDF that user clicks on in `BrowseReportsFragment.java`
- Edited `DocumentAdapter.java`
  - Made nested Interface public from package-level
- Added string resources
- Added `view_report_file.xml`
  - Handles layout for `ViewPdfReportFragment.java`

# 11/10/2020 Changes
- Added `PhotosAdapter.java`
  - Handles the connection between a photos recyclerview and photo data from Storage
- Added `Photo.java`
  - Contains information about individual photos taken from Storage
- Added string resources
- Edited `fragment_upload_existing_files.xml`
  - Changed certain attributes to remove redundancy
- Edited `fragment_main_menu.xml`
  - Changed string value of a CardView (was wrong value)
- Edited `MainMenuFragment.java`
  - Added code to launch fragment to upload existing photos to Storage
- Added `fragment_upload_existing_photo.xml`
  - Layout presented to upload existing photo to project. Mirrors existing reports upload menu
- Edited `UploadExistingReportFragment.java`
  - Removed duplicate code for Spinner.OnItemSelectedListener(...) attach.
  - Changed invoke of getSupportFragmentManager(...) to a variable (instatntiated earlier in block)
  - Added brackets around an 'if' statment to prevent logic error
- Added `UploadExistingPhotoFragment.java`
  - Uploads existing photos to Storage, that can then be viewed.
  - Does similar thing to `UploadExistingReportFragment.java`
- Edited `BrowsePhotosFragment.java`
  - Added code to handle most of what this fragment is meant to do
- Added `ViewPhotoFragment.java`
  - Similar to popup from previous version that displayed image preview to screen (will give options
    to download).
- Added `PhotoFactory.java`
  - Will handle widespread methods to manipulate or work with photo files (such as .jpgs)
- Edited/added other xml files.
- Increased minimum SDK level.

# 11/12/2020 Changes
- Added `xml/` and `xml/filepaths`
  - Folder for file provider to access photos
- Edited `AndroidManifest.xml`
  - Added a FileProvider to be able to take and save photos
  - Added `uses-permission` for `READ_EXTERNAL_STORAGE` and `uses-feature` for `camera2`
- Added `TakeNewPhotoFragment.java`
  - Handles the logic behind taking new photos with the device's camera
  - Also allows upload to the Storage solution
- Added string resources
- Edited `UploadExistingPhotoFragment.java`
  - Added header comment to file
  - Added `last_name` SharedPreferences field to metadata for `taken_by` field
- Edited `MainMenuFragment.java`
  - Added code to launch `TakeNewFragment.java` on specified CardView
- Edited `recycler_view_photo_instance.xml`
  - Changed the height of the `ConstraintLayout` from `match_parent` to `wrap_content`
  - Added margin to `ImageView`
  - Added `constraint` to `bottom of parent`
- Added `fragment_take_new_photo.xml`
  - Layout for the `TakeNewPhotoFragment` instance
- Edited `PhotoFactory.java`
  - Added comment header
  - Changed File.createTempFile(...) to include a context's getFilesDir directory
- Edited `Photo.java`
  - Changed name of 'get' method for TakenBy (from getAuthor() to getTakenBy())
  - Added two `String` variables for first name and last name
  - Removed `takenBy` `String` varibale that held full name
  - Made all variables but `Bitmap` `final`
- Edited `BrowsePhotosFragment.java`
  - Changed a List variable to `final`
  - Changed format of onCreateView(...)
- Edited `UploadExistingReportFragment.java`
  - Added a `.load()` method call to PDFView once user selected PDF from file explorer
- Edited `MainActivity.java`
  - Added a note regarding the speed of which the project was made.