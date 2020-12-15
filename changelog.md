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

# 11/13/2020 Changes
- Edited `fragment_main_menu`
  - Added `CardView` for project total hours
- Added icon for project total hours report generation
- Added string resources'
- Edited `HelpAndFaqFragment.java`
  - Changed name from `SettingsAndHelpFragment.java` to `HelpAndFaqFragment.java`, since this project
    has no settings the user can customize.
- Added `fragment_help_and_faq.xml`
  - UI for the `HelpAndFaqFragment.java` instance.
  - Added string resources for each entry

# 11/18/2020 Changes
- Added `GenerateMonthlyReportFragment.java`
  - Used to allow users to generate monthly report totals based on the project, year, and month
    entered
  - Also added `fragment_create_monthly_total_report.xml` for UI
- Edited `MainMenuFragment.java`
  - Added code to launch `GenerateMonthlyReportFragment.java`
- Added colors
- Added strings
- Edited `dark` `themes.xml`
  - Added Antwon's dark color theme
- Edited `WorkMonth.java`
  - Added method, `DetermineMonthOffset(...)`, that determines the number offset (array index)
    of month based on given lowercase month passed in.
- Edited `ReportFactory.java`
  - Added methods to generate daily, monthly, and yearly totals.
    - These were taken directly from the previous project, altered slightly.

# 11/20/2020 Changes, Section 1
- Added string resources
- Added `CreateYearlyReportFragment.java`
  - Handles logic for creating reports based on year. Performs what
    `GenerateMonthlyReportFragment.java` does, but for the whole year
  - Added `fragment_create_yearly_report.xml` for UI for this fragment
- Edited `WorkMonth.java`
  - Removed the `equate(...)` method to make things easier
- Edited `WorkYear.java`
  - Removed traces of `WorkMonth.equate(...)`
- Edited `MainMenuFragment.java`
  - Added handling of `CreateYearlyReportFragment.java`-related `CardView`
- Added `CreateProjectHoursTotalFragment.java`
- Edited `WorkDay.java`
  - Removed `WorkDay.equate(...)`
  - Removed traces of it in class
- Edited `CreateMonthlyReportFragment.java`
  - Changed name of File instance that held generated report
- Edited `ReportFactory.java`
  - Added `GenerateYearlyReport(...)` to print out yearly report
  - Changed block of code with method call that did same thing (adding a line separator)
  - Changed filename generation to randomly generated from method call, removed to-do
  - Added commenting for certain cell generation related code
  - Removed original `CreateMonthlyReport(...)` to remove unused method
  - Refactored `printYearlyTotal(...)` to handle `null` months

# 11/20/2020 Changes, Section 2
- Added comment to `AndroidManifest.xml` to remove the attribute of the `MainActivity` tag that
  enables resize of screen content based on whether the keyboard is being displayed
- Refactored `MainContentActivity.java`
  - Refactored `GenerateIntent(...)` to `generateIntent(...)`
  - Refactored the `BottomNavigationView` variable to local in `onCreate(...)` from class-level
  - Removed commented-out `@Override` of `onBackPressed(...)`
- Refactored `WorkYear.java`
  - Removed traces of `WorkMonth.equate(...)`
  - Removed unused testing code
- Refactored `WorkMonth.java`
  - Removed `equate(...)` method
  - Removed unused testing code
- Refactored `WorkDay.java`
  - See 'Reformats', sections (A) and (B)
- Refactored `ReportFactory.java`
  - See 'Reformats', sections (A) and (B)
- Refactored `PhotoFactory.java`
  - See 'Reformats', sections (A) and (B)
- Refactored `Photo.java`
  - Implemented `LocalDate` instead of `Date` instance
  - `Refactored `getDateAsString(...)` to use `LocalDate` and `DateTimeFormatter` classes and
    methods
  - Removed unused imports
  - See 'Reformats', sections (A) and (B)
- Refactored `Document.java`
  - See 'Reformats', section (A)
  - Reformatted `import` list
- Refactored `ViewPdfReportFragment.java`
  - Reformatted `import` list
- Refactored `UploadExistingReportFragment.java`
  - Reformatted `import` list
  - See 'Reformats', sections (A) and (B)
- Refactored `UploadExistingPhotoFragment.java`
  - See 'Reformats', section (B)
- Refactored `PhotosAdapter.java`
  - See 'Reformats', section (A)
- Refactored `BrowsePhotosFragment.java`
  - Removed unused imports
  - Implemented the use of `LocalDate` and removed `Date` instance
  - Reformatted spacing
  - See 'Reformats' section (B)
- Refactored `BrowseReportsFragment.java`
  - Made `documentList` final
  - Reformatted class-level variable list
  - See 'Reformats', section (A) and (B)
- Refactored `CreateMonthlyReportFragment.java`
  - Changed variable name for `File` instance that holds the return value of
    `generateMonthlyReport(...)`
  - Reformatted `import` list
  - Reformatted spacing
  - See 'Reformats', section (A) and (B)
- Refactored `CreateNewReportFragment.java`
  - Reformatted `import` list
  - See 'Reformats', section (A) and (B)
- Refactored `CreateYearlyReportFragment.java`
  - Reformatted `import` list
  - See 'Reformats', section (B)
- Refactored `LoginFragment.java`
  - Changed method name to reflect change in `MainContentActivity.java`
  - Reformatted `import` list
- Refactored `MainMenuFragment.java`
  - Added handling for yearly report generation `CardView`
  - Reformatted `import` list
- Refactored `SelectProjectFragment.java`
  - Reformatted `import` list
- Refactored `TakeNewPhotoFragment.java`
  - Reformatted `import` list
- Reformats:
  - (A) Reformatted comment headers on:
    - `PhotosAdapter.java`
    - `WorkYear.java`
    - `WorkMonth.java`
    - `WorkDay.java`
    - `ReportFactory.java`
    - `PhotoFactory.java`
    - `Photo.java`
    - `Document.java`
    - `UploadExistingReportFragment.java`
    - `BrowseReportsFragment.java`
    - `CreateMonthlyReportFragment.java`
    - `CreateNewReportFragment.java`
  - (B) Refactored method names to lower camel case on:
    - `WorkMonth.java`
    - `WorkDay.java`
    - `ReportFactory.java`
    - `PhotoFactory.java`
    - `Photo.java`
    - `UploadExistingReportFragment.java`
    - `UploadExistingPhotoFragment.java`
    - `BrowsePhotosFragment.java`
    - `BrowseReportsFragment.java`
    - `CreateMonthlyReportFragment.java`
    - `CreateNewReportFragment.java`
    - `CreateYearlyReportFragment.java`

# 11/23/2020 Changes
- Edited `CreateProjectHoursTotalFragment.java`
  - Added class-level variables and entry-code to generating report
  - Created `fragment_create_project_totals.xml` for fragment's UI.
- Added string resources

# 11/24/2020 Changes
- Edited `AndroidManifest.xml`
  - Changed the `windowSoftInputMode` for `MainContentActivity.java` to `adjustPan`. Not sure if it
    does anything (it's supposed to).
- Added `CreateAccidentReportFragment.java`
  - Handles the creation of accident reports for the total of the project.
  - Added UI file, `fragment_create_report_of_accidents.xml`
- Added `CreateNewProjectFragment.java`
  - Handles creation of new projects via inserting a file directly under the project name prefix to
  - Added UI file, `fragment_create_new_project.xml`
    establish permanence in Storage
- Edited `CreateNewReportFragment.java`
  - Fixed report generation to prevent headers from printing twice (with insertion of header creation
    method that also initializes the PdfWriter and Document [might / most likely will change later])
  - Added comments to specify what two Firestore `CollectionReference` instances represent.
  - Added method, `insertAccidentReportIntoDB(...)`, to insert accident information (day, weather
    condition, and accident log) into Firestore.
- Edited `CreateYearlyReportFragment.java`
  - Added code to handle error if user doesn't select job name
- Edited `MainMenuFragment.java`
  - Added event handling for `generateAccidentReportCardView` and `createNewProjectCardView`
- Added string resources
- Edited `fragment_main_menu.xml`
  - Added `CardView` to allow user to create new project in Firebase
- Added `ic_add_project.png`
  - Icon for adding a new project `CardView`
- Edited `ReportFactory.java`
  - Removed methods initializing certain variables and entities
  - Added method to handle report generation for accidents
  - Added method to make new, empty file to establish permanence inside FirebaseStorage
  - Add method to initialize the `PdfWriter`, `Document`, and header text all at once.

# 11/27/2020 Changes
- Added string resources
- Edited `ReportFactory.java`
  - Added two method to print total project reports and yearly headers
- Edited `GenerateNewReportFragment.java`
  - Added a method that inserted an 'anchor' inside the year and month directory in Firebase
    to ensure they can be located by other parts of the program dynamically (like the fragment for
    generating a project total report).;
- Edited `AndroidManifest.xml`
  - Changed `MainContentActivity`'s `windowSoftInputMode` back to `adjustResize`
- Edited `CreateProjectHoursTotalFragment.java`
  - Added code to load months into given `WorkYear` instance, then print out and display a project
    totals report
- Edited `fragment_create_project_totals.xml`
  - Removed `View`'s that wouldn't fit in UI (context-wise) and renamed `View`'s `android:id`
    attributes

### NOTE: Here's hoping this is the final commit for the semester, and that this is the end of the
work for senior project. Fingers crossed that we pass.

# 12/1/2020 Changes
- Edited `PhotosAdapter.java`
  - Added a method to return the `List` of `Photo`s
- Added `SortType.java` and `FilterType.java`
  - Both contain a single enum for sorting and filtering, respectively
  - Used to sort and filter content based on:
    - Sort: None, date of content and uploaded by, either ascending or descending
    - Filter: None, by whether the report contains an accident log
- Edited `BrowseReportsFragment.java`
  - Added code to filter and sort the content inside the list (taken from predecessor project)
- Edited `BrowsePhotosFragment.java`
  - Added code to handle filtering and sorting
    - Since there isn't any current filtering for photos, that feature is omitted and displays
      a message regarding the omission of filtering photos.
- Edited `fragment_browse_content.xml`
  - Fixed the filtering and sorting section by adding two `MaterialTextViews` in place of the
    `NavigationView`
- Added `menu_filter_options.xml` and `menu_sort_options.xml`
  - Both menus are used in the filtering and sorting `PopupMenu` instances as the layout for the
    menu.
- Added string resources

# 12/2/2020 Changes
- Updated 'com.android.tools.buiild:gradle' dependency to version 4.1.1 in `build.gradle (project)`
- Edited `fragment_create_report_of_accidents.xml`
  Changed String resource for the title text to display proper page.
- Edited `TakeNewPhotoFragment.java`
  - Fixed positioning of a bracket, closed-paren, and semicolon pairing.
  - Added line to set `chosenFile` to `null` if the upload of an image was successful.

# 12/3/2020 Changes
- Edited `BrowsePhotosFragment.java`
  - Added code to handle what happens when the user tries to sort and filter without any photos
    in the `photoAdapter`
- Edited `BrowseReportsFragment.java`
  - Added code to handle what happens when the user tries to sort and filter without any documents
    in the `documentAdapter`
- Edited `CreateAccidentReportFragment.java`
  - Added code to handle what happens when no accident logs exist for the given project
- Edited `CreateMonthlyReportFragment.java`
  - Added code to test if the project is selected
- Edited `CreateProjectHoursTotalFragment.java`
  - Added a assignment to the `yearsAddedCount` variable to `0` whenever the user presses the button
    to create a report and when the project years directory variable, `projectYearsDir` is queried
    and data is found. At first, it ran anyway and didn't display data if the `yearsTakenFromDBCount`
    variable was less than the current `yearsAddedCount` varible.
      -Setting it to `0` at the time the `yearsTakenFromDBCount` variable is set to the value
       returned from `Task.getResult.size()` method call fixed this issue (could've been put
       anywhere; put there for cohesion).
  - Added code to handle what happens if the documents size for years is `0`, meaning no years were
    found for this project
  - Moved the `countOfAdditions.getAndIncrement()` to a `else` block after the conditional to test if
    all months were added to the `WorkYear` variable (`countOfAdditions.get() == 11`).
- Edited `CreateYearlyReportFragment.java`
  - Added code to test if the project selected has any entries in its `hours` directory in Firebase
    - If not, no report is made.
  - Add code to handle what happens when no entries in the hours log exist for this project
  - Removed unneeded calls to `Log.d(...)`
- Edited `ReportFactory.java`
  - Removed rudundant initialization of the `File` variable that is an anchor for the yearly report
    - Changed the `File f = null` to `File f = generateFile(...)`
  - Added an apologetic comment above the `count` variable used to keep track of which value in the
    `Table` array is null
- Added string resources

# 12/8/2020 Changes
- Added code to handle what happens if the user signed in doesn't have a proper entry inside
  Firestore
- Added string resources

# 12/14/2020 Changes
- Edited `BrowseReportsFragment.java`
  - Added a line to clear reports from the RecyclerView when a new project is selected.
- Edited `CreateNewProjectFragment.java`
  - Added code to perform a length check on the project name
- Edited `CreateNewReportFragment.java`
  - Added code to perform regular expression check on input received from 'Hours Worked' textbox
- Edited `fragment_main_menu.xml`
  - Changed string resource names to match new naming counterpart in `strings.xml`
- Edited `strings.xml`
  - Added new string resources
  - Changed names from 'generate' to 'create' and 'job' to 'project'

# 12/15/2020 Changes
- Edited `fragment_take_new_photo.xml`
  - Changed the height for the ImageView to preview images taken with the camera to fit it on the
    screen for smaller phones.