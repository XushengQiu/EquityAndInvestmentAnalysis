package Model;

public class Company {
    private String companyName;
    private String companyCode;
    private String marketCap;
    private String stockPrice;
    private String lastDayVariation;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getMarketCap() {
        return marketCap;
    }

    public void setMarketCap(String marketCap) {
        this.marketCap = marketCap;
    }

    public String getStockPrice() {
        return stockPrice;
    }

    public void setStockPrice(String stockPrice) {
        this.stockPrice = stockPrice;
    }

    public String getLastDayVariation() {
        return lastDayVariation;
    }

    public void setLastDayVariation(String lastDayVariation) {
        this.lastDayVariation = lastDayVariation;
    }
}
