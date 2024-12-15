package ar.credify.serviceimpl;

import ar.credify.dao.StudentDocumentDAO;
import ar.credify.model.StudentDocument;
import ar.credify.model.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

@Service
public class StudentDocumentService {

    private final StudentDocumentDAO studentDocumentDAO;

    // Define the upload directory
    private String uploadDirectory = "/Users/adwaitrelekar/uploads";

    @Autowired
    public StudentDocumentService(StudentDocumentDAO studentDocumentDAO) {
        this.studentDocumentDAO = studentDocumentDAO;
        new File(uploadDirectory).mkdirs(); // Ensure the upload directory exists
    }

    @Transactional
    public void uploadFile(UserAccount userAccount, MultipartFile file, String description, String documentType) throws Exception {
        // Create a unique file name using the student's first name, timestamp, and a random number
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        int randomNumber = new Random().nextInt(10000);
        String userName = userAccount.getPerson().getFirstName().replaceAll("\\s+", "_");
        String uniqueFileName = userName + "_" + timestamp + "_" + randomNumber + "_" + file.getOriginalFilename();

        Path filePath = Paths.get(uploadDirectory, uniqueFileName);

        File destinationFile = filePath.toFile();

        file.transferTo(destinationFile);

        StudentDocument document = new StudentDocument();
        document.setStudent(userAccount);
        document.setFileName(uniqueFileName);
        document.setFilePath(filePath.toString());
        document.setDescription(description);
        document.setUploadedDate(LocalDateTime.now());
        document.setDocumentType(documentType);

        studentDocumentDAO.save(document);
    }

    public List<StudentDocument> getDocumentsForStudent(UserAccount userAccount) {
        return studentDocumentDAO.findByStudent(userAccount);
    }

    public StudentDocument getDocumentById(Long id) {
        return studentDocumentDAO.findById(id);
    }

    @Transactional
    public void deleteDocument(StudentDocument document) throws Exception {
        // Delete the file from the filesystem
        File file = new File(document.getFilePath());
        if (file.exists()) {
            file.delete();
        }
        // Delete the document record from the database
        studentDocumentDAO.delete(document);
    }
}