package FileMaster;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class FileOrganizerUtil {

    public static void organizeFiles(String dirPath, Consumer<String> log) {
        File dir = new File(dirPath);
        if (!dir.exists() || !dir.isDirectory()) {
            log.accept("Invalid directory: " + dirPath + "\n");
            return;
        }

        // Define target directories using File.separator
        File pdfFolder = new File(dirPath + File.separator + "PDF");
        File imgFolder = new File(dirPath + File.separator + "IMAGE");
        File docFolder = new File(dirPath + File.separator + "DOCUMENT");
        File txtFolder = new File(dirPath + File.separator + "TEXT");
        File vidFolder = new File(dirPath + File.separator + "VIDEO");
        File audioFolder = new File(dirPath + File.separator + "AUDIO");

        File[] allFiles = dir.listFiles();
        if (allFiles == null) {
            log.accept("No files found in the directory.\n");
            return;
        }

        for (File file : allFiles) {
            if (file.isFile()) {
                String fileName = file.getName();
                String[] parts = fileName.split("\\.");
                if (parts.length < 2) {
                    continue;
                }
                String ext = parts[parts.length - 1].toLowerCase();
                File targetFolder = switch (ext) {
                    case "pdf" -> pdfFolder;
                    case "docx", "doc" -> docFolder;
                    case "jpg", "png", "jpeg", "gif" -> imgFolder;
                    case "txt", "log" -> txtFolder;
                    case "mp4", "mkv", "avi", "mov" -> vidFolder;
                    case "mp3", "wav", "m4a" -> audioFolder;
                    default -> null;
                };
                if (targetFolder == null) continue;

                if (!targetFolder.exists() && !targetFolder.mkdir()) {
                    log.accept("Failed to create folder: " + targetFolder.getName() + "\n");
                    continue;
                }

                File destFile = new File(targetFolder, fileName);
                if (file.renameTo(destFile)) {
                    log.accept("Moved " + fileName + " to " + targetFolder.getName() + "\n");
                } else {
                    log.accept("Failed to move " + fileName + "\n");
                }
            }
        }
        log.accept("File organization complete.\n");
    }

    public static void findDuplicates(String dirPath1, String dirPath2, Consumer<String> log) {
        File folder1 = new File(dirPath1);
        File folder2 = new File(dirPath2);

        if (!folder1.exists() || !folder1.isDirectory() ||
            !folder2.exists() || !folder2.isDirectory()) {
            log.accept("One or both directories are invalid.\n");
            return;
        }

        File[] files1 = folder1.listFiles();
        File[] files2 = folder2.listFiles();

        if (files1 == null || files2 == null) {
            log.accept("No files found in one of the directories.\n");
            return;
        }

        Set<String> fileNames1 = new HashSet<>();
        for (File f : files1) {
            if (f.isFile()) {
                fileNames1.add(f.getName());
            }
        }

        File dupFolder = new File(folder1.getParent(), "Duplicate");
        if (!dupFolder.exists() && !dupFolder.mkdir()) {
            log.accept("Failed to create Duplicate folder.\n");
            return;
        }

        for (File f2 : files2) {
            if (f2.isFile() && fileNames1.contains(f2.getName())) {
                File destFile = new File(dupFolder, f2.getName());
                if (f2.renameTo(destFile)) {
                    log.accept("Moved duplicate file: " + f2.getName() + "\n");
                } else {
                    log.accept("Failed to move duplicate file: " + f2.getName() + "\n");
                }
            }
        }
        log.accept("Duplicate file check complete.\n");
    }
}
