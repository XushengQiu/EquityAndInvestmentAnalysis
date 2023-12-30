package Controller;

import WebScraping.WebScrapping;
import org.apache.catalina.User;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@RestController
public class PersistenceController {

    private static PersistenceController instance = null;

    private PersistenceController() {}


    public static PersistenceController getInstance() {
        if (instance == null)
            instance = new PersistenceController();
        return instance;
    }

    @GetMapping("/api/companies")
    public List<Map<String, String>> getCompanies() {
        WebScrapping aux = new WebScrapping();
        return aux.getCompanyData88();
    }

    @GetMapping("users/{id}")
    public ResponseEntity<User> getById (@PathVariable long id){
        return null;
    }

    // ----------------- SUPPORT FUNCTIONS ----------------- //

    private DataSource InvestmentDB() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        dataSource.setUrl("jdbc:sqlserver://southeast-asia-s3rv3r.database.windows.net:1433;database=PFM_System_Investment");
        dataSource.setUsername("TongjiStudent");
        dataSource.setPassword("Tongji_Root");
        return dataSource;
    }

    private void updateBalance(int spend) throws SQLException {
        //update("Balance", actual()-spend);
    }

}
