import javax.swing.*;

/**
 * The type Gui.
 */
public class GUI {

    private JTextField urlTextField;
    private JTextField saveLocationTextField;
    private JPanel mainPanel;
    private JCheckBox zipCheckBox;
    private JCheckBox jpgCheckBox;
    private JPanel fileTypeSelection;
    private JSpinner noOfThreadsSpinner;
    private JButton downloadButton;
    private JCheckBox pdfCheckBox;

    /**
     * Instantiates a new Gui.
     */
    public GUI() {
        // Download all files for given parameters once the download button is pressed
        downloadButton.addActionListener(e -> {
            String url = urlTextField.getText();
            String saveDir = saveLocationTextField.getText();
            int threads = (int) noOfThreadsSpinner.getValue();

            String extensions = "";
            boolean zips = zipCheckBox.isSelected();
            boolean jpgs = jpgCheckBox.isSelected();
            boolean pdfs = pdfCheckBox.isSelected();

            if (zips && jpgs && pdfs) extensions = "zip|jpg|pdf";
            else if (jpgs && pdfs) extensions = "jpg|pdf";
            else if (zips && pdfs) extensions = "zip|pdf";
            else if (zips && jpgs) extensions = "zip|jpg";
            else if (zips) extensions = "zip";
            else if (jpgs) extensions = "jpg";
            else if (pdfs) extensions = "pdf";

            Download download = new Download(url, extensions, saveDir, threads);
            download.downloadAll();
        });
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("File Downloader");
        frame.setContentPane(new GUI().mainPanel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void createUIComponents() {
        SpinnerNumberModel spinnerNumberModel = new SpinnerNumberModel(1, 1, 10, 1);
        noOfThreadsSpinner = new JSpinner(spinnerNumberModel);
    }
}
