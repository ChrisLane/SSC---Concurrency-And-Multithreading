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

    /**
     * Instantiates a new Gui.
     */
    public GUI() {
        // Download all files for given parameters once the download button is pressed
        downloadButton.addActionListener(e -> {
            String url = urlTextField.getText();
            String saveDir = saveLocationTextField.getText();
            int threads = (int) noOfThreadsSpinner.getValue();

            Download download = new Download(url, saveDir, threads);
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
