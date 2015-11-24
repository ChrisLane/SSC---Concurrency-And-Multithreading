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

public class Download {
    private String url;
    private String saveLocation;
    private Elements linkElements;

    public Download(String url, String saveLocation) {
        this.url = url;
        this.saveLocation = saveLocation;

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

    public void downloadFiles() {
        for (URL url : getURLs()) {
            if (!(url == null)) {
                String file = url.getFile();
                String baseName = FilenameUtils.getBaseName(file);
                String extension = FilenameUtils.getExtension(file);
                String fileName = baseName + "." + extension;

                File saveFolder = new File(saveLocation);

                try {
                    FileUtils.copyURLToFile(url, new File(saveFolder, fileName));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
