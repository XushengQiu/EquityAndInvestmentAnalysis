package WebScraping;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class WebScrapping {
    public List<Map<String, String>> getCompanyData() {
        List<Map<String, String>> companyList = new ArrayList<>();
        try {
            Document doc = Jsoup.connect("https://companiesmarketcap.com/china/largest-companies-in-china-by-market-cap/").get();
            Elements rows = doc.select("tr");

            for (Element row : rows) {
                Elements columns = row.select("td");
                if (columns.size() >= 6) {
                    Map<String, String> companyData = new HashMap<>();
                    companyData.put("companyName", replaceSpacesWithUnderscores(columns.get(2).text()));
                    companyData.put("companyCode", columns.get(2).select("div.company-code").text());
                    companyData.put("marketCap", replaceSpacesWithUnderscores(columns.get(3).text()));
                    companyData.put("stockPrice", columns.get(4).text());
                    String lastDayVariation = columns.get(5).attr("data-sort");
                    companyData.put("lastDayVariation", formatNumber(lastDayVariation));

                    companyList.add(companyData);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this.cleanCompanyNames(companyList);  // Spring convertirá esta lista en un JSON
    }

    public List<Map<String, String>> getCompanyData88() {
        List<Map<String, String>> companyList = this.getCompanyData();
        if (companyList.size() > 88) {
            return companyList.subList(0, 88);
        } else {
            return companyList;
        }
    }

    public String searchForCompanyStockPrice(String name) {
        List<Map<String, String>> companyList = this.getCompanyData();

        for (Map<String, String> company : companyList) {
            if (company.get("companyName").equals(name)) {
                return company.get("stockPrice");
            }
        }

        return "Empresa no encontrada";
    }

    public boolean isTheNameCorrect(String name) {
        List<Map<String, String>> companyList = this.getCompanyData();

        for (Map<String, String> company : companyList) {
            if (company.get("companyName").equals(name)) {
                return true;
            }
        }
        return false;
    }


    /*_________________________________Auxiliares_________________________________*/

    private List<Map<String, String>> cleanCompanyNames(List<Map<String, String>> originalList) {
        List<Map<String, String>> cleanedList = new ArrayList<>();

        for (Map<String, String> company : originalList) {
            // Hacer una copia del mapa para evitar modificar el original si es necesario
            Map<String, String> cleanedCompany = new HashMap<>(company);

            String companyName = cleanedCompany.get("companyName");
            String companyCode = cleanedCompany.get("companyCode");

            if (companyName != null && companyCode != null && companyName.endsWith(companyCode)) {
                // Eliminar la terminación que es el código de la compañía
                companyName = companyName.substring(0, companyName.lastIndexOf(companyCode)).trim();
                companyName = companyName.substring(0, companyName.length() - 1);
                cleanedCompany.put("companyName", companyName);
            }

            cleanedList.add(cleanedCompany);
        }

        return cleanedList;
    }


    private String formatNumber(String input) {
        try {
            // Convertir el String a un número (usando BigDecimal para mayor precisión)
            BigDecimal number = new BigDecimal(input);

            BigDecimal result = number.divide(new BigDecimal("100"));

            String formattedResult = result.compareTo(BigDecimal.ZERO) > 0 ? "+" : "";
            formattedResult += result.toString() + "%";
            return formattedResult;
        } catch (NumberFormatException e) {
            return "Input inválido";
        }
    }

    private String replaceSpacesWithUnderscores(String input) {
        if (input == null) {
            return null;
        }
        return input.replace(" ", "_");
    }

}
