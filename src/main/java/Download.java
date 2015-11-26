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
    private String url;
    private String extensions;
    private String saveDir;
    private Elements linkElements;
    private int threads;

    /**
     * Instantiates a new Download.
     *
     * @param url     the url to download from
     * @param saveDir the save directory
     * @param threads the number of threads to be used
     */
    public Download(String url, String extensions, String saveDir, int threads) {
        this.url = url;
        this.extensions = extensions;
        this.saveDir = saveDir;
        this.threads = threads;

        // Grab the html elements for links on the page
        fetchLinkElements();
    }

    /**
     * Fetch link elements from the web page
     */
    private void fetchLinkElements() {
        Document document = null;

        System.out.println(extensions);

        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Only select link elements that match our extension options
        if (document != null) {
            linkElements = document.select("a[href~=(?i)\\.(" + extensions + ")]");
        }
    }

    /**
     * Get the URLs from the link elements
     *
     * @return URLs from all link elements
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
     * Download single file runnable.
     *
     * @param url      the url of the file's location
     * @param fileName the file name
     * @param saveDir  the save directory
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
            // Split up the file namings to variables
            String file = url.getFile();
            String baseName = FilenameUtils.getBaseName(file);
            String extension = FilenameUtils.getExtension(file);
            String fileName = baseName + "." + extension;
            File saveDir = new File(this.saveDir);

            // Download this file on a new thread
            Runnable downloadFile = downloadFile(url, fileName, saveDir);
            executor.execute(downloadFile);
            System.out.println("Download queued: " + fileName);
        }

        executor.shutdown();
    }
}
