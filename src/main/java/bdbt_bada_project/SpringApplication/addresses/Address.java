package bdbt_bada_project.SpringApplication.addresses;

import bdbt_bada_project.SpringApplication.mails.Mail;
import bdbt_bada_project.SpringApplication.mails.MailsDAO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.jsoup.Jsoup;
import org.springframework.jdbc.core.JdbcTemplate;

@Setter
@Getter
public class Address {

    private int addressNumber;
    private String city;
    private String street;
    private String apartmentNumber;
    private int mailNumber;
    private Mail mail;

    public Address() {}

    public Address(int addressNumber, String city, String street, String apartmentNumber, int mailNumber) {
        this.addressNumber = addressNumber;
        this.city = city;
        this.street = street;
        this.apartmentNumber = apartmentNumber;
        this.mailNumber = mailNumber;
        this.mail = new MailsDAO(new JdbcTemplate()).get(mailNumber);
    }

    public double[] getLatitude() {
        try {
            String url = String.format("https://nominatim.openstreetmap.org/search?format=json&q=%s %s %s",
                    street, apartmentNumber, city);
            String jsonResponse = Jsoup.connect(url)
                    .ignoreContentType(true)
                    .execute()
                    .body();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            if (rootNode.isArray() && rootNode.size() > 0) {
                JsonNode firstElement = rootNode.get(0);
                double latitude = firstElement.get("lat").asDouble();
                double longitude = firstElement.get("lon").asDouble();
                return new double[]{latitude, longitude};
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new double[]{52.230020, 21.005135};
    }


    @Override
    public String toString() {
        return "Address{" +
                "addressNumber=" + addressNumber +
                ", city='" + city + '\'' +
                ", street='" + street + '\'' +
                ", apartmentNumber='" + apartmentNumber + '\'' +
                ", mailNumber=" + mailNumber +
                '}';
    }
}
