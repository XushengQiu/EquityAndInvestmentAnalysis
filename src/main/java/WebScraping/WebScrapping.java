package WebScraping;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class WebScrapping {
    public List<String> getCompanyData() {
        List<String> companyList = new ArrayList<>();
        try {
            Document doc = Jsoup.connect("https://companiesmarketcap.com/china/largest-companies-in-china-by-market-cap/").get();
            Elements rows = doc.select("tr");

            for (Element row : rows) {
                Elements columns = row.select("td");
                if (columns.size() >= 6) {  // Asegurarse de que hay al menos 6 columnas
                    JSONObject companyData = new JSONObject();
                    companyData.put("Company name", columns.get(2).text());
                    companyData.put("Company code", columns.get(2).select("div.company-code").text());
                    companyData.put("Company market cap", columns.get(3).text());
                    companyData.put("Company stock price", columns.get(4).text());
                    // Variación del último día, dividir entre 100 para que esté en el formato de %
                    String lastDayVariation = columns.get(5).attr("data-sort");
                    companyData.put("Last day variation", this.formatNumber(lastDayVariation));

                    companyList.add(companyData.toString());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this.modifyCompanyNames(companyList);
    }

    public List<String> modifyCompanyNames(List<String> originalList) {
        List<String> modifiedList = new ArrayList<>();

        for (String jsonStr : originalList) {
            JSONObject company = new JSONObject(jsonStr);

            String companyName = company.getString("Company name");
            String companyCode = company.getString("Company code");

            // Remover el código de la compañía del nombre de la compañía
            if (companyName.endsWith(companyCode)) {
                companyName = companyName.replace(companyCode, "").trim();
            }

            // Actualizar el objeto JSON
            company.put("Company name", companyName);

            // Agregar a la nueva lista
            modifiedList.add(company.toString());
        }

        return modifiedList;
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

    public static void main(String[] args) {
        WebScrapping webScrapping = new WebScrapping();
        List<String> companyList = null;
        companyList = webScrapping.getCompanyData();
        System.out.println(companyList.toString());
        /*try {
            Document doc = Jsoup.connect("https://companiesmarketcap.com/china/largest-companies-in-china-by-market-cap/").get();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
}
