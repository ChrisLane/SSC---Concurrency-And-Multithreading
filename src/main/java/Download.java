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

public class Download {
    private String url;
    private String saveDir;
    private Elements linkElements;

    public Download(String url, String saveDir) {
        this.url = url;
        this.saveDir = saveDir;

        fetchLinkElements();
    }

    private void fetchLinkElements() {
        Document document = null;

        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (document != null) {
            linkElements = document.select("a[href~=(?i)\\.(zip|png|jpg|pdf)]");
        }
    }

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

    public Runnable downloadFile(URL url, String fileName, File saveDir) {
        return () -> {
            try {
                FileUtils.copyURLToFile(url, new File(saveDir, fileName));
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }

    public void downloadAll() {
        ExecutorService pool = Executors.newFixedThreadPool(4);

        for (URL url : getURLs()) {
            String file = url.getFile();
            String baseName = FilenameUtils.getBaseName(file);
            String extension = FilenameUtils.getExtension(file);
            String fileName = baseName + "." + extension;
            File saveDir = new File(this.saveDir);

            Runnable runnable = downloadFile(url, fileName, saveDir);
            pool.execute(runnable);
        }

        pool.shutdown();
    }
}
