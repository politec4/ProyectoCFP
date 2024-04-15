package primeraEntrega;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.*;

public class GenerateReports {

    public static void main(String[] args) {
        // Nombre de los archivos generados por el programa GenerateInfoFiles
        String sellersFilename = "info_vendedores.txt";
        String productsFilename = "info_productos.txt";

        // Generar reporte de ventas por vendedor
        generateSellersReport(sellersFilename, productsFilename);

        // Generar reporte de productos vendidos por cantidad
        generateProductsReport(productsFilename);

        System.out.println("Reportes generados exitosamente.");
    }

    private static void generateSellersReport(String sellersFilename, String productsFilename) {
        Map<String, Integer> sellerSales = new HashMap<>(); // Mapa para almacenar las ventas totales de cada vendedor
        
        // Leer archivo info_vendedores.txt y extraer nombres y apellidos del vendedor
        try (BufferedReader reader = new BufferedReader(new FileReader(sellersFilename))) {
            String line;
            int i = 1;
            while ((line = reader.readLine()) != null) {
            	String salesFilename = "ventas" + i + ".txt";
            	String[] parts = line.split(";");
                sellerSales.put(parts[2] + " " + parts[3], calculateSales(salesFilename, productsFilename, sellersFilename)); // Almacenar nombre completo del vendedor y su monto de ventas
                i++;
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo de vendedores: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        // Leer archivos de ventas y calcular las ventas de cada vendedor
        for (int i = 1; i <= 6; i++) { // Teniendo en cuenta que hay 6 archivos de ventas
            String salesFilename = "ventas" + i + ".txt";
            calculateSales(salesFilename, productsFilename, sellersFilename);
        }

        // Ordenar los vendedores de acuerdo a sus ventas en orden descendente
        List<Map.Entry<String, Integer>> sortedSellers = new ArrayList<>(sellerSales.entrySet());
        sortedSellers.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        // Escribir el reporte de ventas por vendedor
        try (FileWriter writer = new FileWriter("reporte_ventas_vendedores.txt")) {
            for (Map.Entry<String, Integer> entry : sortedSellers) {
                String sellerInfo = entry.getKey() + ";" + NumberFormat.getCurrencyInstance(Locale.US).format(entry.getValue());
                writer.write(sellerInfo + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error al escribir el reporte de vendedores: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void generateProductsReport(String productsFilename) {
    	Map <String, Integer>productInfo = calculateSalesProducts(productsFilename);
        
    	// Ordenar los productos por cantidad vendida en orden descendente
        List<Map.Entry<String, Integer>> sortedQuantity = new ArrayList<>(productInfo.entrySet());
        sortedQuantity.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
    	
        // Escribir el reporte de productos vendidos por cantidad
        try (FileWriter writer = new FileWriter("reporte_ventas_productos.txt")) {
            for (Map.Entry<String, Integer> entry : sortedQuantity) {
                String quantityT = entry.getKey() + ";" + entry.getValue();
                writer.write(quantityT + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error al escribir el reporte de vendedores: " + e.getMessage());
            e.printStackTrace();
        }   
    }

    private static int calculateSales(String salesFilename, String productsFilename, String sellersFilename) {
    	int salesTotal = 0;	// Variable para almacenar el monto de venta total
    	
    	// Leer el archivo de ventas
    	try (BufferedReader reader = new BufferedReader(new FileReader(salesFilename))) {
    		// Leer el archivo info_productos.txt y almacenar los precios de los productos
            Map<String, Integer> productPrices = new HashMap<>();
            try (BufferedReader productsReader = new BufferedReader(new FileReader(productsFilename))) {
                String line;
                while ((line = productsReader.readLine()) != null) {
                    String[] parts = line.split(";");
                    productPrices.put(parts[0], Integer.parseInt(parts[2].replaceAll("[^\\d]", ""))); // Almacenar Id y precio de los productos
                }
            }

            // Ignorar la primera línea del archivo de ventas (tipo y número de documento del vendedor)
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 2) { // Verificar que haya al menos dos partes separadas por punto y coma en el archivo de ventas
                    String productId = parts[0]; // Id del producto
                    int quantitySold = Integer.parseInt(parts[1]); // Cantidad vendida del producto
                    int productPrice = productPrices.getOrDefault(productId, 0); // Obtener precio del producto
                    int salesAmount = quantitySold * productPrice; // Calcular monto de venta del producto
                    salesTotal += salesAmount;	// Calcular monto de venta total
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo de ventas: " + e.getMessage());
            e.printStackTrace();
        }
        return salesTotal;
    }
    
    private static Map<String, Integer> calculateSalesProducts(String productsFilename) {
    	Map<String, Integer> productInfo = new HashMap<>(); // Mapa para almacenar la información del producto con las cantidades totales vendidas
    	int quantityTotal = 0;	// Variable para almacenar la cantidad total de ventas de un producto
        int lineNumber = 1; // Número de línea del archivo de ventas que se lee actualmente
        int lineNumber1 = 0; // Número de línea del archivo de productos que se lee actualmente
        String productInfo1="";	// Cadena para almacenar nombre y precio del producto 
        
        while (true) {
            boolean hasMoreLines = false; // Bandera para indicar si hay más líneas en al menos un archivo
            // Leer el archivo de ventas
            for (int i = 1; i <= 6; i++) { // Teniendo en cuenta que hay 6 archivos de ventas
                String salesFilename = "ventas" + i + ".txt";
                try (BufferedReader reader = new BufferedReader(new FileReader(salesFilename))) {
                    // Ignorar las primeras (lineNumber - 1) líneas
                    for (int j = 0; j < lineNumber; j++) {
                        reader.readLine();
                    }
                    // Leer la línea actual
                    String line = reader.readLine();
                    if (line != null) {
                        String[] parts = line.split(";");
                        int quantitySold = Integer.parseInt(parts[1]); // Cantidad del producto vendida por un vendedor
                        quantityTotal += quantitySold; //Cantidad total vendida del producto
                        hasMoreLines = true; // Hay más líneas en al menos un archivo
                    }
                } catch (IOException e) {
                    System.err.println("Error al leer el archivo: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            if (!hasMoreLines) {
            	// No hay más líneas en ningún archivo, salir del bucle
            	break;
            }
            
         // Leer el archivo de productos y almacenar nombre y precio del producto
            try (BufferedReader productsReader = new BufferedReader(new FileReader(productsFilename))) {
            	for (int j = 0; j < lineNumber1; j++) {
            		productsReader.readLine();
            	}
            	String line;
                if ((line = productsReader.readLine()) != null) {
                	String[] parts = line.split(";");
                	productInfo1 = (parts[1] + ";" + parts[2]); // Almacenar nombre y precio del producto separado por ;
                	productInfo.put(productInfo1, quantityTotal); // Almacenar nombre y precio del producto y cantidad vendida
                }
            } catch (IOException e) {
                System.err.println("Error al leer el archivo de ventas: " + e.getMessage());
                e.printStackTrace();
            }
            
            lineNumber1++; // Pasar a la siguiente línea del archivo info_productos.txt
            lineNumber++; // Pasar a la siguiente línea de los archivos ventas.txt
            quantityTotal = 0; // Limpiar la variable que almacena la cantidad total de ventas de un producto
        }         
        return productInfo;
    }

}
