import javax.swing.*;

public class GUI {

    private JTextField URLTextField;
    private JTextField saveLocationTextField;
    private JPanel mainPanel;
    private JCheckBox zipCheckBox;
    private JCheckBox jpgCheckBox;
    private JPanel fileTypeSelection;
    private JSpinner noOfThreadsSpinner;
    private JButton downloadButton;

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
