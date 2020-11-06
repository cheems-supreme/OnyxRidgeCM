package com.icecrown.onyxridgecm.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import com.icecrown.onyxridgecm.R;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.AreaBreakType;
import com.itextpdf.layout.property.HorizontalAlignment;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Calendar;

public class ReportFactory {
    public static File GenerateFile(Context context, SharedPreferences prefs) {
        File dir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);

        Calendar cal = Calendar.getInstance();
        String emailFirstSeg = prefs.getString("email", "null");
        String readyFilename = cal.get(Calendar.MILLISECOND) + "_" + cal.get(Calendar.DAY_OF_MONTH) + "_" + (cal.get(Calendar.MONTH) + 1) + "_" + cal.get(Calendar.YEAR) + "_" + emailFirstSeg + ".pdf";

        return new File(dir, readyFilename);
    }


    public static PdfWriter InitializeWriter(File fileToWriteTo) {
        PdfWriter writer = null;
        try {
            writer = new PdfWriter(fileToWriteTo);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return writer;
    }

    public static Document InitializePdfDocument(PdfWriter writer) {
        PdfDocument doc = new PdfDocument(writer);
        doc.addNewPage(PageSize.A4);
        return new Document(doc).setFontSize(15);
    }

    public static void AddJobNameContent(Context context, Document document, String projectName) {
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

    public static void AddDateContent(Context context, Document document, String dateInString) {
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

    public static void AddCreatedByContent(Context context, Document document, String writtenBy) {
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

    public static void AddWorkersHoursAndTotal(Context context, Document document, int workersOnSite, float hoursPerWorker) {
        Text workersOnSiteLabel = new Text(context.getString(R.string.workers_title));
        workersOnSiteLabel.setBold();
        Text workersOnSiteValue = new Text(workersOnSite + context.getString(R.string.workers_postfix));
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
        Text hoursPerWorkerValue = new Text(hoursPerWorker + context.getString(R.string.hours_postfix));
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
        Text totalHoursValue = new Text(totalHoursWorker + context.getString(R.string.total_hours_postfix));
        totalHoursValue.setUnderline();
        Paragraph totalHoursContent = new Paragraph();
        totalHoursContent.setPaddingTop(5);
        totalHoursContent.setPaddingLeft(15);
        totalHoursContent.setPaddingBottom(5);
        totalHoursContent.add(totalHoursLabel);
        totalHoursContent.add(totalHoursValue);
        document.add(totalHoursContent);
    }


    public static void AddDailyLogContent(Context context, Document document, String dailyLog) {
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

    public static void AddLineSeparator(Document document) {
        document.add(new LineSeparator(new SolidLine()).setPaddingTop(10).setPaddingBottom(10));
    }

    public static void AddWeatherContent(Context context, Document document, String weatherValue) {
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

    public static void AddAccidentOccurredContent(Context context, Document document, boolean accidentHappened, String accidentHappenedDesc) {
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


        Text accidentDetailLabelText = new Text(context.getString(R.string.accident_happened_title));
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

    public static void UploadPhotosToDoc(Context context, Document document, File[] imagesChosen) {
        // TODO: HANDLE PICTURES BEING ADDED TO DOC TEMPLETE
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
}