import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;
import java.net.URI;
import java.awt.Desktop;
import FileMaster.FileOrganizerGUI;
public class SpeechToText {
    public static void main(String[] args) {
        try {
            // Configuration object for Sphinx-4 (1.0beta6)
            Configuration configuration = new Configuration();

            // Set acoustic model, dictionary, and language model paths
            configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
            configuration.setDictionaryPath("VoiceAssistant/src/dic.dic");
            configuration.setLanguageModelPath("VoiceAssistant/src/lm.lm");

            // Create the LiveSpeechRecognizer
            LiveSpeechRecognizer recognizer = new LiveSpeechRecognizer(configuration);
            recognizer.startRecognition(true);
            
            System.out.println("Listening for commands...");
            SpeechResult result;

            // Process speech input continuously
            while ((result = recognizer.getResult()) != null) {
                String speechText = result.getHypothesis().toLowerCase();
                System.out.println("You said: " + speechText);
                
                // Recognize specific commands
                switch (speechText) {
                    case "open google":
                        System.out.println("Opening Google...");
                        Desktop.getDesktop().browse(new URI("http://google.com/"));
                        break;
                    case "open youtube":
                        System.out.println("Opening YouTube...");
                        Desktop.getDesktop().browse(new URI("http://youtube.com/"));
                        break;
                    case "format folder":
                        System.out.println("Executing FileOrganizerGUI...");
                        new Thread(() -> FileOrganizerGUI.main(new String[]{})).start();
;
                        break;
                    case "folder":
                        System.out.println("Opening Folder...");
                        new ProcessBuilder("explorer.exe").start();
                        break;
                    case "start":
                        System.out.println("Starting process...");
                        break;
                    case "stop":
                        System.out.println("Stopping process...");
                        break;
                    default:
                        System.out.println("Unrecognized command: " + speechText);
                        break;
                }
            }
            
            recognizer.stopRecognition();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Function to execute FileOrganizerGUI in a separate process
    public static void executeOtherJavaProgram() {
        try {
            String javaPath = "C:\\Program Files\\Java\\jdk-21\\bin\\java";  // Java path
            String projectPath = "C:\\Users\\TusharKumar\\Desktop\\File_Master\\JAVA"; // Adjusted classpath
            String className = "Project.FileOrganizerGUI"; // Include package name

            ProcessBuilder processBuilder = new ProcessBuilder(javaPath, "-cp", projectPath, className);
            processBuilder.inheritIO();
            processBuilder.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
