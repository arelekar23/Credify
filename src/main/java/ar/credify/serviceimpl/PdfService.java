package ar.credify.serviceimpl;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.properties.TextAlignment;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class PdfService {

    public void generateApplicationPdf(Long applicationId, ByteArrayOutputStream outputStream) throws IOException {
        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument);

        addPersonalDetailsSection(document);

        addEducationDetailsSection(document);

        addWorkExperienceSection(document);

        addProjectDetailsSection(document);

        document.close();
    }

    private void addPersonalDetailsSection(Document document) {
        document.add(new Paragraph("Adwait Pradip Relekar")
                        .setFontSize(18))
                .setTextAlignment(TextAlignment.CENTER);

        document.add(new Paragraph("Boston, MA | relekar.ad@northeastern.edu | (206) 670-6509 | linkedin.com/in/relekaradwait/")
                .setFontSize(12)
                .setTextAlignment(TextAlignment.CENTER));

        document.add(new Paragraph("\n"));
    }

    private void addEducationDetailsSection(Document document) {
        document.add(new Paragraph("EDUCATION")
                .setFontSize(16));

        List educationList = new List().setListSymbol("-");
        educationList.add(new Paragraph("Northeastern University, Boston, MA (Expected Dec 2025)").toString())
                .add(new Paragraph("Master of Science, Computer Software Engineering, 4.0/4.0").toString())
                .add(new Paragraph("Relevant Courses: Front-End Development, Data Structures & Algorithms, Data Management & Database Design").toString())
                .add(new Paragraph("MIT College of Engineering, Pune University, India (May 2019)").toString())
                .add(new Paragraph("Bachelor of Engineering, Electronics & Telecommunications").toString());

        document.add(educationList);
        document.add(new Paragraph("\n"));
    }

    private void addWorkExperienceSection(Document document) {
        document.add(new Paragraph("WORK EXPERIENCE")
                .setFontSize(16));

        List workExperienceList = new List().setListSymbol("-");
        workExperienceList.add(new Paragraph("Associate Programmer Analyst, Cognizant Technology Solutions, India (Jun 2019 – Jul 2023)").toString())
                .add(new Paragraph("• Developed Java scripts for automating various scenarios on cloud-based Android application using Appium framework, enhancing efficiency.").toString())
                .add(new Paragraph("• Engineered an Android application for updating daily status reports to reduce time in scrum meetings by 80%.").toString())
                .add(new Paragraph("• Actively participated in hiring by interviewing candidates for automation roles.").toString());

        document.add(workExperienceList);
        document.add(new Paragraph("\n"));
    }

    private void addProjectDetailsSection(Document document) {
        document.add(new Paragraph("RELEVANT PROJECTS")
                .setFontSize(16));

        List projectList = new List().setListSymbol("-");
        projectList.add(new Paragraph("ShoeStrideAR (Xcode, Swift, UIKit, Stripe API, CameraKit, Firebase)").toString())
                .add(new Paragraph("• Implemented Augmented Reality Shoe Visualization, enabling real-time overlay of virtual shoe models onto users' feet.").toString())
                .add(new Paragraph("• Integrated Stripe payment gateway for seamless and secure transactions.").toString())
                .add(new Paragraph("Checkmate Challenge (Android Studio, Java, SQLite)").toString())
                .add(new Paragraph("• Developed an Android game featuring interactive chess puzzles.").toString())
                .add(new Paragraph("• Implemented minimax algorithm for puzzle generation and designed SQLite database schema.").toString())
                .add(new Paragraph("CodeAb (React, node.js, MongoDB, Firebase, Vercel)").toString())
                .add(new Paragraph("• Built an interactive online coding platform for collaborative problem-solving.").toString())
                .add(new Paragraph("Mind Meld - A Brain-Computer Interface (May 2019)").toString())
                .add(new Paragraph("• Designed a Brain-Computer Interface to authenticate vehicle lock and control turn signals.").toString())
                .add(new Paragraph("• Successfully filed, published, and was granted a patent (Patent No: 540976, Application No: 201921011129).").toString());

        document.add(projectList);
        document.add(new Paragraph("\n"));
    }
}