import com.google.gson.Gson;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.Scanner;

public class ConversorDeMonedas {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        HttpClient client = HttpClient.newHttpClient();
        Gson gson = new Gson();

        final String API_URL = "https://v6.exchangerate-api.com/v6/3429f7615e786d892b354bf2/latest/USD";

        boolean running = true;

        while (running) {
            System.out.println("**************************************************");
            System.out.println("Sea bienvenido/a al conversor de monedas =D");
            System.out.println("1) Dólar => Peso mexicano");
            System.out.println("2) Peso mexicano => Dólar");
            System.out.println("3) Dólar => Peso argentino");
            System.out.println("4) Peso argentino => Dólar");
            System.out.println("5) Dólar => Peso colombiano");
            System.out.println("6) Peso colombiano => Dólar");
            System.out.println("7) Salir");
            System.out.println("Elija una opción:");
            System.out.println("**************************************************");

            int option = scanner.nextInt();

            if (option < 1 || option > 7) {
                System.out.println("Opción no válida. Por favor, seleccione una opción entre 1 y 7.");
                continue; // Regresa al inicio del bucle
            }

            if (option == 7) {
                System.out.println("Gracias por usar el conversor. ¡Hasta luego!");
                break;
            }

            // Solicitar la cantidad
            System.out.println("Ingrese la cantidad que desea convertir:");
            double amount = scanner.nextDouble();

            try {
                // Realizar la solicitud a la API
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(new URI(API_URL))
                        .GET()
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                // Parsear la respuesta JSON
                ExchangeRatesResponse exchangeRates = gson.fromJson(response.body(), ExchangeRatesResponse.class);

                if (!"success".equals(exchangeRates.getResult())) {
                    System.out.println("Error al obtener los datos de la API.");
                    continue;
                }

                // Realizar conversión según la opción seleccionada
                double convertedAmount = 0;
                String originalCurrency = "";
                String targetCurrency = "";
                switch (option) {
                    case 1: // Dólar => Peso mexicano
                        convertedAmount = amount * exchangeRates.getConversionRates().get("MXN");
                        originalCurrency = "USD";
                        targetCurrency = "MXN";
                        break;
                    case 2: // Peso mexicano => Dólar
                        convertedAmount = amount / exchangeRates.getConversionRates().get("MXN");
                        originalCurrency = "MXN";
                        targetCurrency = "USD";
                        break;
                    case 3: // Dólar => Peso argentino
                        convertedAmount = amount * exchangeRates.getConversionRates().get("ARS");
                        originalCurrency = "USD";
                        targetCurrency = "ARS";
                        break;
                    case 4: // Peso argentino => Dólar
                        convertedAmount = amount / exchangeRates.getConversionRates().get("ARS");
                        originalCurrency = "ARS";
                        targetCurrency = "USD";
                        break;
                    case 5: // Dólar => Peso colombiano
                        convertedAmount = amount * exchangeRates.getConversionRates().get("COP");
                        originalCurrency = "USD";
                        targetCurrency = "COP";
                        break;
                    case 6: // Peso colombiano => Dólar
                        convertedAmount = amount / exchangeRates.getConversionRates().get("COP");
                        originalCurrency = "COP";
                        targetCurrency = "USD";
                        break;
                }

                // Determinar el formato basado en el valor convertido
                String format;
                if (convertedAmount < 1 && convertedAmount > -1) {
                    format = "%.4f %s equivalen a %.4f %s%n";
                } else {
                    format = "%.2f %s equivalen a %.2f %s%n";
                }

                System.out.printf(format, amount, originalCurrency, convertedAmount, targetCurrency);

            } catch (Exception e) {
                System.out.println("Ocurrió un error al conectar con la API: " + e.getMessage());
            }

            System.out.println("**************************************************");
        }

        scanner.close();
    }
}

// Clase para representar la respuesta de la API
class ExchangeRatesResponse {
    private String result;
    private String base_code;
    private Map<String, Double> conversion_rates;

    // Getters
    public String getResult() {
        return result;
    }

    public String getBaseCode() {
        return base_code;
    }

    public Map<String, Double> getConversionRates() {
        return conversion_rates;
    }
}
