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
                    companyData.put("companyName", columns.get(2).text());
                    companyData.put("companyCode", columns.get(2).select("div.company-code").text());
                    companyData.put("marketCap", columns.get(3).text());
                    companyData.put("stockPrice", columns.get(4).text());
                    String lastDayVariation = columns.get(5).attr("data-sort");
                    companyData.put("lastDayVariation", formatNumber(lastDayVariation));  // Asumiendo que formatNumber es un método que convierte el número

                    companyList.add(companyData);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this.cleanCompanyNames(companyList);  // Spring convertirá esta lista en un JSON
    }

    public List<Map<String, String>> cleanCompanyNames(List<Map<String, String>> originalList) {
        List<Map<String, String>> cleanedList = new ArrayList<>();

        for (Map<String, String> company : originalList) {
            // Hacer una copia del mapa para evitar modificar el original si es necesario
            Map<String, String> cleanedCompany = new HashMap<>(company);

            String companyName = cleanedCompany.get("companyName");
            String companyCode = cleanedCompany.get("companyCode");

            if (companyName != null && companyCode != null && companyName.endsWith(companyCode)) {
                // Eliminar la terminación que es el código de la compañía
                companyName = companyName.substring(0, companyName.lastIndexOf(companyCode)).trim();
                cleanedCompany.put("companyName", companyName);
            }

            cleanedList.add(cleanedCompany);
        }

        return cleanedList;
    }


    public String formatNumber(String input) {
        try {
            // Convertir el String a un número (usando BigDecimal para mayor precisión)
            BigDecimal number = new BigDecimal(input);

            // Dividir el número por 100
            BigDecimal result = number.divide(new BigDecimal("100"));

            // Convertir el resultado a String, reemplazar punto por coma y añadir '%'
            return result.toString().replace('.', ',') + "%";
        } catch (NumberFormatException e) {
            // En caso de error en la conversión, retornar un mensaje de error
            return "Input inválido";
        }
    }

}
