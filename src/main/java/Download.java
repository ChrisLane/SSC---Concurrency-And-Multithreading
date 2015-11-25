import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The type Download.
 */
public class Download {
    private int threads;
    private String url;
    private String saveDir;
    private Elements linkElements;

    /**
     * Instantiates a new Download.
     *
     * @param url     the url
     * @param saveDir the save dir
     * @param threads the threads
     */
    public Download(String url, String saveDir, int threads) {
        this.url = url;
        this.saveDir = saveDir;
        this.threads = threads;

        fetchLinkElements();
    }

    /**
     * Fetch link elements from the web page
     */
    private void fetchLinkElements() {
        Document document = null;

        String extensions = "";
        if (GUI.zipSelected && GUI.jpgSelected && GUI.pdfSelected) extensions = "zip|jpg|pdf";
        else if (GUI.jpgSelected && GUI.pdfSelected) extensions = "jpg|pdf";
        else if (GUI.zipSelected && GUI.pdfSelected) extensions = "zip|pdf";
        else if (GUI.zipSelected && GUI.jpgSelected) extensions = "zip|jpg";
        else if (GUI.zipSelected) extensions = "zip";
        else if (GUI.jpgSelected) extensions = "jpg";
        else if (GUI.pdfSelected) extensions = "pdf";

        System.out.println(extensions);

        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (document != null) {
            linkElements = document.select("a[href~=(?i)\\.(" + extensions + ")]");
        }
    }

    /**
     * Get the URLs from the link elements
     *
     * @return URLs
     */
    private URL[] getURLs() {
        URL[] urls = new URL[linkElements.size()];

        int count = 0;
        for (Element linkElement : linkElements) {
            try {
                urls[count] = new URL(linkElement.attr("abs:href"));
                count++;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        return urls;
    }

    /**
     * Download file runnable.
     *
     * @param url      the url
     * @param fileName the file name
     * @param saveDir  the save dir
     * @return the runnable
     */
    public Runnable downloadFile(URL url, String fileName, File saveDir) {
        return () -> {
            System.out.println("Download started: " + fileName);
            try {
                FileUtils.copyURLToFile(url, new File(saveDir, fileName));
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Download complete: " + fileName);
        };
    }

    /**
     * Download all files.
     */
    public void downloadAll() {
        ExecutorService executor = Executors.newFixedThreadPool(threads);

        for (URL url : getURLs()) {
            String file = url.getFile();
            String baseName = FilenameUtils.getBaseName(file);
            String extension = FilenameUtils.getExtension(file);
            String fileName = baseName + "." + extension;
            File saveDir = new File(this.saveDir);

            Runnable downloadFile = downloadFile(url, fileName, saveDir);
            executor.execute(downloadFile);
            System.out.println("Download queued: " + fileName);
        }

        executor.shutdown();
    }
}
