package Application;

import WebScraping.WebScrapping;
import okhttp3.Response;

public class Main {
    public static void main(String[] args) {
        WebScrapping aux = new WebScrapping();
        System.out.println(aux.getCompanyData().toString());
    }
}

