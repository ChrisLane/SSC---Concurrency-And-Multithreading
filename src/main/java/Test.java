public class Test {

    public static void main(String[] args) {
        Download download = new Download("http://www.cs.bham.ac.uk/~dehghanh/vision_files/lab/lab5/", "downloads");
        download.downloadAll();
    }
}
