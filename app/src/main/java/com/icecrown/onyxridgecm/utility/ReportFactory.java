//*******************************************************************
// Project: OnyxRidge Construction Management
//
// File: ReportFactory.java
//
// Written by: Raymond O'Neill
//
// Date written: 10/8/2020
// Date added: 11/6/2020
//
// Detail: A class that handles the generation of reports based on
//         passed in content. Mainly used to separate this logic from
//         the class it is used in to clear up space and modularize
// ------------------------------------------------
// UPDATES
// ------------------------------------------------
// - 11/18/2020
// - R.O.
// - DETAILS:
//      - Added code to generate monthly, yearly, and daily totals
// ------------------------------------------------
// - 11/20/2020
// - R.O.
// - DETAILS:
//      - Added `generateYearlyReport(...)`
//      - Removed redundant code block
//      - Changed filename generation
//      - Added commenting for certain cell generation related code
//      - Removed unused methods
//      - Refactored `printYearlyTotal(...)` to handle `null` months
//      - Refactored method names to lower camel case
//      - Reformatted comment header
// ------------------------------------------------
// - 11/24/2020
// - R.O.
// - DETAILS:
//      - Removed methods initializing certain factors of report
//        generation and moved them to a single method.
//      - Added method to handle report generation for accidents
//      - Added method to make new, temporary file to solidify
//        project placement in FirebaseStorage
//      - Add method to initialize both writer and document and print
//        headers all at once (might change)
// ------------------------------------------------
// - 11/27/2020
// - R.O.
// - DETAILS:
//      - Added method to create a header for years, before the months
//        belonging to them
//      - Added a method to print project entire hourly totals
// ------------------------------------------------
// - 12/3/2020
// - R.O.
// - DETAILS:
//      - Removed redundant File initialization in
//        generateYearlyReport(...)
//      - Added apologetic comment
//*******************************************************************
package com.icecrown.onyxridgecm.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.icecrown.onyxridgecm.R;
import com.icecrown.onyxridgecm.workseries.WorkDay;
import com.icecrown.onyxridgecm.workseries.WorkMonth;
import com.icecrown.onyxridgecm.workseries.WorkYear;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.DottedBorder;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.AreaBreakType;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

public class ReportFactory {

    public static File generateStorageAnchorFile(Context context) throws IOException{
        File newFile = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "anchor.txt");
        if(newFile.exists()) {
            return newFile;
        }
        else {
            if(newFile.createNewFile()) {
                return newFile;
            }
            else {
                throw new IOException();
            }
        }
    }
    public static File generateFile(Context context, SharedPreferences prefs) {
        File dir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);

        Calendar cal = Calendar.getInstance();
        String emailFirstSeg = prefs.getString("email", "null");
        String readyFilename = cal.get(Calendar.MILLISECOND) + "_" + cal.get(Calendar.DAY_OF_MONTH) + "_" + (cal.get(Calendar.MONTH) + 1) + "_" + cal.get(Calendar.YEAR) + "_" + emailFirstSeg + ".pdf";

        return new File(dir, readyFilename);
    }


    // TODO: POOR PRACTICE, CHANGE
    public static Document initializeDocumentAndHeader(File f, Context appContext, String projectName) {
        PdfWriter writer = null;

        try {
            writer = new PdfWriter(f.getPath());

        } catch (IOException e) {
            e.printStackTrace();
        }

        Document document = null;
        if (writer != null) {
            PdfDocument doc = new PdfDocument(writer);
            doc.addNewPage(PageSize.A4);
            document = new Document(doc);
            document.setFontSize(15);

            addJobNameContent(appContext, document, projectName);

            addLineSeparator(document);
            return document;
        }
        else {
            return null;
        }
    }

    public static void closeDocument(Document d) {
        d.close();
    }
    public static void closeWriter(PdfWriter writer) throws IOException {
        writer.close();
    }
    public static void addJobNameContent(Context context, Document document, String projectName) {
        Text jobNameLabel = new Text(context.getString(R.string.job_name_title));
        jobNameLabel.setBold();
        Text jobNameValue = new Text(projectName);
        jobNameValue.setUnderline();
        Paragraph jobNameContent = new Paragraph();
        jobNameContent.setPaddingTop(5);
        jobNameContent.setPaddingBottom(5);
        jobNameContent.setPaddingLeft(15);
        jobNameContent.add(jobNameLabel);
        jobNameContent.add(jobNameValue);
        document.add(jobNameContent);
    }

    public static void addDateContent(Context context, Document document, String dateInString) {
        Text dateLabel = new Text(context.getString(R.string.date_title));
        dateLabel.setBold();
        Text dateValue = new Text(dateInString);
        dateValue.setUnderline();
        Paragraph dateContent = new Paragraph();
        dateContent.setPaddingTop(5);
        dateContent.setPaddingBottom(5);
        dateContent.setPaddingLeft(15);
        dateContent.add(dateLabel);
        dateContent.add(dateValue);
        document.add(dateContent);
    }

    public static void addCreatedByContent(Context context, Document document, String writtenBy) {
        Text createdByLabel = new Text(context.getString(R.string.document_written_by));
        createdByLabel.setBold();
        Text createdByValue = new Text(writtenBy);
        createdByValue.setUnderline();
        Paragraph createdByContent = new Paragraph();
        createdByContent.setPaddingTop(5);
        createdByContent.setPaddingBottom(5);
        createdByContent.setPaddingLeft(15);
        createdByContent.add(createdByLabel);
        createdByContent.add(createdByValue);
        document.add(createdByContent);
    }

    public static void addWorkersHoursAndTotal(Context context, Document document, int workersOnSite, float hoursPerWorker) {
        Text workersOnSiteLabel = new Text(context.getString(R.string.workers_title));
        workersOnSiteLabel.setBold();
        Text workersOnSiteValue = new Text(workersOnSite + " " + context.getString(R.string.workers_postfix));
        workersOnSiteValue.setUnderline();
        Paragraph workersOnSiteContent = new Paragraph();
        workersOnSiteContent.setPaddingTop(5);
        workersOnSiteContent.setPaddingBottom(5);
        workersOnSiteContent.setPaddingLeft(15);
        workersOnSiteContent.add(workersOnSiteLabel);
        workersOnSiteContent.add(workersOnSiteValue);
        document.add(workersOnSiteContent);

        Text hoursPerWorkerLabel = new Text(context.getString(R.string.hours_per_worker_title));
        hoursPerWorkerLabel.setBold();
        Text hoursPerWorkerValue = new Text(hoursPerWorker + " " + context.getString(R.string.hours_postfix));
        hoursPerWorkerValue.setUnderline();
        Paragraph hoursPerWorkerContent = new Paragraph();
        hoursPerWorkerContent.setPaddingLeft(15);
        hoursPerWorkerContent.setPaddingTop(5);
        hoursPerWorkerContent.setPaddingBottom(5);
        hoursPerWorkerContent.add(hoursPerWorkerLabel);
        hoursPerWorkerContent.add(hoursPerWorkerValue);
        document.add(hoursPerWorkerContent);


        Text totalHoursLabel = new Text(context.getString(R.string.total_hours_title));
        totalHoursLabel.setBold();
        String totalHoursWorker = String.valueOf(hoursPerWorker * workersOnSite);
        Text totalHoursValue = new Text(totalHoursWorker + " " + context.getString(R.string.total_hours_postfix));
        totalHoursValue.setUnderline();
        Paragraph totalHoursContent = new Paragraph();
        totalHoursContent.setPaddingTop(5);
        totalHoursContent.setPaddingLeft(15);
        totalHoursContent.setPaddingBottom(5);
        totalHoursContent.add(totalHoursLabel);
        totalHoursContent.add(totalHoursValue);
        document.add(totalHoursContent);
    }


    public static void addDailyLogContent(Context context, Document document, String dailyLog) {
        Paragraph dailyLogContentOne = new Paragraph();

        Text dailyLogLabel = new Text(context.getString(R.string.daily_log_title));
        dailyLogLabel.setBold();
        dailyLogContentOne.setPaddingLeft(15);
        dailyLogContentOne.add(dailyLogLabel);
        Paragraph dailyLogValue = new Paragraph(dailyLog);
        dailyLogValue.setFontSize(12);
        dailyLogValue.setPaddingLeft(30);
        dailyLogValue.setPaddingRight(30);
        dailyLogValue.setPaddingBottom(15);

        document.add(dailyLogContentOne);
        document.add(dailyLogValue);
    }

    public static void addLineSeparator(Document document) {
        document.add(new LineSeparator(new SolidLine()).setPaddingTop(10).setPaddingBottom(10));
    }

    public static void addWeatherContent(Context context, Document document, String weatherValue) {
        Text weatherLabel = new Text(context.getString(R.string.weather_title));
        weatherLabel.setBold();
        Text weatherContentValue = new Text(weatherValue);
        weatherContentValue.setUnderline();
        Paragraph weatherContent = new Paragraph();
        weatherContent.setPaddingTop(15);
        weatherContent.setPaddingLeft(15);
        weatherContent.setPaddingBottom(5);
        weatherContent.add(weatherLabel);
        weatherContent.add(weatherContentValue);
        document.add(weatherContent);
    }

    public static void addAccidentOccurredContent(Context context, Document document, boolean accidentHappened, String accidentHappenedDesc) {
        Text accidentOccurredLabel = new Text(context.getString(R.string.accident_happened_title));
        accidentOccurredLabel.setBold();
        String isChecked;

        Paragraph accidentDetailValue;

        if(accidentHappened) {
            isChecked = "Yes";
            accidentDetailValue = new Paragraph(accidentHappenedDesc);
        }
        else {
            isChecked = "No";
            accidentDetailValue = new Paragraph(context.getString(R.string.no_accident_occurred_text));
        }

        Text accidentOccurredValue = new Text(isChecked);
        accidentOccurredValue.setUnderline();
        Paragraph accidentOccurredContent = new Paragraph();
        accidentOccurredContent.setPaddingLeft(15);
        accidentOccurredContent.setPaddingTop(5);
        accidentOccurredContent.setPaddingBottom(5);
        accidentOccurredContent.add(accidentOccurredLabel);
        accidentOccurredContent.add(accidentOccurredValue);
        document.add(accidentOccurredContent);


        Text accidentDetailLabelText = new Text(context.getString(R.string.accident_log_title));
        Paragraph accidentDetailLabel = new Paragraph(accidentDetailLabelText);
        accidentDetailLabel.setBold();
        accidentDetailLabel.setPaddingLeft(15);

        accidentDetailValue.setFontSize(12);
        accidentDetailValue.setPaddingLeft(30);
        accidentDetailValue.setPaddingRight(30);
        accidentDetailValue.setPaddingBottom(15);

        document.add(accidentDetailLabel);
        document.add(accidentDetailValue);
    }

    public static void uploadPhotosToDoc(Context context, Document document, File[] imagesChosen) {
        // TODO: HANDLE PICTURES BEING ADDED TO DOC TEMPLATE
        // if(imagesChosen != null) {
        //  Comparator<File> fileSorter = (o1, o2) -> {
        //                        if (o1.lastModified() < o2.lastModified()) {
        //                            return -1;
        //                        } else if (o1.lastModified() == o2.lastModified()) {
        //                            return 0;
        //                        } else {
        //                            return 1;
        //                        }
        //                    };
        //                    Arrays.sort(images, fileSorter);
        //                    Cell[] imagesPerPage = new Cell[images.length];
        //
        //                    if(images.length > 0) {
        //                        int count = 0;
        //
        //                        while(count < images.length) {
        //                            document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
        //                            Table t1 = new Table(2);
        //                            for(int i = 0; i < 6; i++) {
        //                                if(count == images.length) { break;}
        //                                try {
        //                                    Path p = Paths.get(images[count].getPath());
        //                                    byte[] imageBytes = Files.readAllBytes(p);
        //
        //                                    imagesPerPage[count] = new Cell();
        //                                    ImageData imData = ImageDataFactory.create(imageBytes);
        //
        //                                    Image im = new Image(imData);
        //                                    im.setAutoScale(true);
        //
        //                                    imagesPerPage[count].add(im);
        //                                    imagesPerPage[count].setHorizontalAlignment(HorizontalAlignment.CENTER);
        //                                    imagesPerPage[count].setVerticalAlignment(VerticalAlignment.MIDDLE);
        //                                    t1.addCell(imagesPerPage[count]);
        //                                    count++;
        //                                } catch (IOException e) {
        //                                    e.printStackTrace();
        //                                }
        //                            }
        //                            document.add(t1);
        //                        }
        // }
        // else {
        Text noPhotosSelectedValue = new Text(context.getString(R.string.no_pictures_chosen));
        noPhotosSelectedValue.setBold();
        Paragraph noPhotosContent = new Paragraph(noPhotosSelectedValue);
        noPhotosContent.setHorizontalAlignment(HorizontalAlignment.CENTER);
        document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
        document.add(noPhotosContent);
        // }
    }

    public static Table generateYearlyTotalHeader(WorkYear year, Context context, Document document) {
        Cell yearLine = new Cell();
        yearLine.setWidth(document.getPageEffectiveArea(PageSize.A4).getWidth() / 2);
        yearLine.add(new Paragraph(String.valueOf(context.getString(R.string.year_header_title))).setFirstLineIndent(10).setFontSize(15));
        yearLine.setBorder(Border.NO_BORDER);
        yearLine.setBorderBottom(new DottedBorder(1));


        Cell yearValue = new Cell();
        yearValue.add(new Paragraph(String.valueOf(year.getYear())).setFirstLineIndent(20).setFontSize(15));
        yearValue.setTextAlignment(TextAlignment.RIGHT);
        yearValue.setBorder(Border.NO_BORDER);
        yearValue.setWidth(document.getPageEffectiveArea(PageSize.A4).getWidth() / 2);
        yearValue.setBorderBottom(new DottedBorder(1));

        Table t = new Table(2);
        t.addCell(yearLine);
        t.addCell(yearValue);

        return t;
    }

    public static Table[] printYearlyTotal(WorkYear year, Document document) {
        Table[] monthlyRecords = new Table[12];
        for (int i = 0; i < 12; i++) {
            if(year.getMonths()[i] == null ) {
                monthlyRecords[i] = null;
            }
            else {
                monthlyRecords[i] = printMonthTotal(year.getMonths()[i], document);
            }
        }
        return monthlyRecords;
    }

    public static Table printMonthTotal(WorkMonth month, Document document) {
        // Generates table to insert values into (2 columns, one for day, other for hours)
        Table monthRecord = new Table(2);
        monthRecord.setBorder(Border.NO_BORDER);
        monthRecord.setWidth(document.getPageEffectiveArea(PageSize.A4).getWidth());

        // Creates two cells for the month name and total hours worked for the month
        Cell monthHeaderCell = new Cell();
        monthHeaderCell.add(new Paragraph(month.getMonthName()));
        monthHeaderCell.setBorder(Border.NO_BORDER);
        Cell totalHoursHeaderCell = new Cell();

        totalHoursHeaderCell.setBorder(Border.NO_BORDER);
        totalHoursHeaderCell.add(new Paragraph(String.format("%.2f hours", month.generateTotalMonthlyHours())));
        totalHoursHeaderCell.setTextAlignment(TextAlignment.RIGHT);

        monthRecord.addHeaderCell(monthHeaderCell);
        monthRecord.addHeaderCell(totalHoursHeaderCell);

        Cell dayHeaderLine = new Cell();

        dayHeaderLine.add(new Paragraph("Days\n").setFontSize(12));
        dayHeaderLine.setBorder(Border.NO_BORDER);
        monthRecord.addCell(dayHeaderLine);
        dayHeaderLine = new Cell();
        monthRecord.addCell(dayHeaderLine.setBorder(Border.NO_BORDER));

        for (int i = 0; i < month.getDaysInMonth(); i++) {
            if(month.getDays()[i] != null) {
                Cell[] line = printDailyTotal(month.getDays()[i], document);
                monthRecord.addCell(line[0]);
                monthRecord.addCell(line[1]);
            }
            else {
                Log.d("EPOCH-3", "Day " + (i + 1) + " is null");
            }
        }
        return monthRecord;
    }

    public static Cell[] printDailyTotal(WorkDay day, Document document) {
        Cell dayLine = new Cell();
        dayLine.setWidth(document.getPageEffectiveArea(PageSize.A4).getWidth() / 2);
        dayLine.add(new Paragraph(day.generateHyphenDateString()).setFirstLineIndent(10).setFontSize(12));
        dayLine.setBorder(Border.NO_BORDER);
        dayLine.setBorderBottom(new DottedBorder(1));


        Cell hourValue = new Cell();
        String formattedHoursString = String.format("%.2f hours", day.getHours());
        hourValue.add(new Paragraph(String.valueOf(formattedHoursString)).setFirstLineIndent(20).setFontSize(12));
        hourValue.setTextAlignment(TextAlignment.RIGHT);
        hourValue.setBorder(Border.NO_BORDER);
        hourValue.setWidth(document.getPageEffectiveArea(PageSize.A4).getWidth() / 2);
        hourValue.setBorderBottom(new DottedBorder(1));
        return new Cell[]{dayLine, hourValue};
    }

    public static File generateMonthlyReport(WorkMonth month, Context appContext, String projectName) {
        PdfWriter writer = null;
        File f = null;

        try {
            f = generateFile(appContext, appContext.getSharedPreferences("user_info", Context.MODE_PRIVATE));
            writer = new PdfWriter(f.getPath());

        } catch (IOException e) {
            e.printStackTrace();
        }

        Document document;
        if (writer != null) {
            PdfDocument doc = new PdfDocument(writer);
            doc.addNewPage(PageSize.A4);
            document = new Document(doc);
            document.setFontSize(15);

            addJobNameContent(appContext, document, projectName);

            addLineSeparator(document);

            Table record = printMonthTotal(month, document);
            document.add(record);

            document.close();

            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return f;
    }

    // TODO: FIX CELL BOTTOM BORDER ISSUE
    private static Cell[] createAccidentLogEntry(Document document, String date, String accidentLog, String weatherConditions) {
        Cell dayLine = new Cell();
        dayLine.setWidth(document.getPageEffectiveArea(PageSize.A4).getWidth() / 2);
        dayLine.add(new Paragraph(date));
        dayLine.setBorder(Border.NO_BORDER);

        Cell weatherLine = new Cell();
        weatherLine.setWidth(document.getPageEffectiveArea(PageSize.A4).getWidth() / 2);
        weatherLine.add(new Paragraph(weatherConditions));
        weatherLine.setBorder(Border.NO_BORDER);

        Cell accidentEntry = new Cell();
        accidentEntry.add(new Paragraph(accidentLog).setFirstLineIndent(20).setFontSize(12));
        accidentEntry.setTextAlignment(TextAlignment.RIGHT);
        accidentEntry.setBorder(Border.NO_BORDER);
        accidentEntry.setWidth(document.getPageEffectiveArea(PageSize.A4).getWidth());
        accidentEntry.setBorderBottom(new DottedBorder(1));


        return new Cell[]{dayLine, weatherLine, accidentEntry};
    }

    // TODO: ADD CODE TO PRINT A HEADER FOR MONTHS WITHOUT ANY DAYS, FOR BLANK ENTRIES SO ALL
    //       MONTHS ARE PRESENT
    public static File generateYearlyReport(WorkYear year, Context appContext, String projectName) {
        File f = generateFile(appContext, appContext.getSharedPreferences("user_info", Context.MODE_PRIVATE));

        Document document = initializeDocumentAndHeader(f, appContext, projectName);

        if(document == null) {
            Log.d("EPOCH-3", "Yearly report generation failed.");
        }
        else {
            Table[] records = printYearlyTotal(year, document);
            for(Table record : records) {
                // Sorry...
                int count = 0;

                if(record == null) {
                    Log.d("EPOCH-3", "record " + ++count + " is null");
                }
                else {
                    document.add(record);
                }
            }

            try {

                PdfWriter writer = document.getPdfDocument().getWriter();
                closeDocument(document);
                closeWriter(writer);
            } catch(IOException e) {
                Log.d("EPOCH-3", "Exception encountered while closing writer");
            }
        }


        return f;
    }


    public static File generateAccidentReport(List<DocumentSnapshot> accidentLogs, Context appContext, String projectName) {
        File f = generateFile(appContext, appContext.getSharedPreferences("user_info", Context.MODE_PRIVATE));
        Document document = initializeDocumentAndHeader(f, appContext, projectName);
        if(document != null) {
            for(DocumentSnapshot entry : accidentLogs) {
                Cell[] record = createAccidentLogEntry(document, entry.get("date_of_accident").toString(), entry.get("accident_log").toString(), entry.get("weather_conditions").toString());
                for(Cell cell : record) {
                    document.add(cell);
                }
            }

            try {
                PdfWriter writer = document.getPdfDocument().getWriter();
                closeDocument(document);
                closeWriter(writer);

            } catch (IOException e) {
                Log.d("EPOCH-3", "IOException encountered in generateAccidentReport(...)");
                e.printStackTrace();
            }
        } else {
            Log.d("EPOCH-3", "Document creation failed in generateAccidentReport(...)");
        }


        return f;
    }

    public static File generateProjectTotalsReport(WorkYear[] years, Context appContext, String projectName) {
        File f = generateFile(appContext, appContext.getSharedPreferences("user_info", Context.MODE_PRIVATE));
        Document document = initializeDocumentAndHeader(f, appContext, projectName);
        if(document != null) {
            for(WorkYear year : years) {
                document.add(generateYearlyTotalHeader(year, appContext, document));

                Table[] months = printYearlyTotal(year, document);
                for(int i = 0; i < months.length; i++) {
                    if(months[i] == null) {
                        Log.d("EPOCH-3", "Month " + (i + 1) + " is null");
                    } else {
                        document.add(months[i]);
                    }
                }
            }
            try {
                PdfWriter writer = document.getPdfDocument().getWriter();
                closeDocument(document);
                closeWriter(writer);
            } catch(IOException ioe) {
                Log.d("EPOCH-3", "IOException encountered in generateProjectTotalsReport(...)");
            }
        }
        else {
            Log.d("EPOCH-3", "Document creation failed in generateProjectTotalsReport(...)");
        }

        return f;
    }
}
