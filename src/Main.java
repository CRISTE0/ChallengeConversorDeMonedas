import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class Main {


    public static double convertCurrency(String moneda1, String moneda2) {
        HttpClient client = HttpClient.newHttpClient();
        String url = "https://v6.exchangerate-api.com/v6/879464e0c2365ed0e136f14b/pair/" + moneda1 + "/" + moneda2;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        // Realizamos la solicitud y obtenemos la respuesta
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            // Llamamos a parseResponse para obtener el tipo de cambio
            return parseResponse(response.body());
        } catch (Exception e) {
            System.out.println("Error al realizar la solicitud: " + e.getMessage());
            return 0;
        }
    }

    public static double parseResponse(String responseBody) {
        JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
        if (jsonObject.get("result").getAsString().equals("success")) {
            return jsonObject.get("conversion_rate").getAsDouble();
        } else {
            return 0;
        }
    }

    public static double calcularMonto(double cantidad, double tipoCambio){
        return cantidad*tipoCambio;
    }

    public static void main(String[] args) {

        Scanner teclado = new Scanner(System.in);

        String moneda1 = "";
        String moneda2 = "";
        double tipoDeCambio;
        int opc;
        double cantidadConvertir;

        String menu = """
                **********+**** Bienvenido al conversor de divisas ***************
                1. Peso argentino => Real brasileño
                2. Real brasileño => Peso chileno
                3. Peso colombiano => Dólar estadounidense
                4. Real brasileño => Dólar estadounidense
                5. Dólar estadounidense => Peso colombiano
                6. Dólar estadounidense => Peso argentino
                0. Salir
                **********+*******************************************************
                """;
        do {

            System.out.println(menu);
            System.out.println("Selecciona una opcion valida");

            opc = teclado.nextInt();

            if (opc >= 1 && opc <=6) {
                switch (opc){
                    case 1:
                        moneda1 = "ARS";
                        moneda2 = "BRL";
                        break;

                    case 2:
                        moneda1 = "BRL";
                        moneda2 = "CLP";
                        break;

                    case 3:
                        moneda1 = "COP";
                        moneda2 = "USD";
                        break;

                    case 4:
                        moneda1 = "BRL";
                        moneda2 = "USD";
                        break;

                    case 5:
                        moneda1 = "USD";
                        moneda2 = "COP";
                        break;

                    case 6:
                        moneda1 = "USD";
                        moneda2 = "ARS";
                        break;
                }

                System.out.println("Ingrese la cantidad a convertir de "+moneda1+" a "+moneda2);
                cantidadConvertir = teclado.nextDouble();

                tipoDeCambio = convertCurrency(moneda1, moneda2);

                double resultado = calcularMonto(cantidadConvertir,tipoDeCambio);

                String montoCalculado = String.format("%.2f", resultado);

                if (tipoDeCambio != 0) {
                    System.out.println("La conversión de " + cantidadConvertir + " " + moneda1 + " es igual a "+ montoCalculado + " " + moneda2 );
                } else {
                    System.out.println("Error al obtener el tipo de cambio.");
                }
            }else {
                opc = 0;
            }

        }while (opc != 0);

        System.out.println("Gracias por usar el conversor, vuelva pronto :)");





    }
}