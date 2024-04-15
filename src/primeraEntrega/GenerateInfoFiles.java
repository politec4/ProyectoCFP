package primeraEntrega;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.text.NumberFormat;
import java.util.Locale;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;


public class GenerateInfoFiles {
    public static void main(String[] args) {
        int numSalesFiles = 6;	// El número de archivos de ventas a generar se definió en 6
        generateSellersFile("info_vendedores.txt");	// Generar archivo de información de vendedores
        generateProductsFile("info_productos.txt");	// Generar archivo de información de productos
        generateSalesFiles(numSalesFiles);	// Generar archivos de ventas por vendedor
        System.out.println("Archivos generados exitosamente.");
    }
    
    // Método para generar los archivos de ventas
    private static void generateSalesFiles(int numFiles) {
        for (int i = 1; i <= numFiles; i++) {
            String salesFilename = "ventas" + i + ".txt";	// Nombre del archivo de ventas
            generateSalesFile(salesFilename,i);	// Generar archivo de ventas
        }
    }

    private static void generateSalesFile(String filename, int vendedorIndex) {
        try {
            FileWriter writer = new FileWriter(filename);
            Random random = new Random();

            // Leer información de vendedores
            List<String> vendedoresInfo = new ArrayList<>();
            BufferedReader vendedoresReader = new BufferedReader(new FileReader("info_vendedores.txt"));
            String vendedorInfo;
            int currentVendedorIndex = 1; // Inicializar el índice del vendedor actual
            while ((vendedorInfo = vendedoresReader.readLine()) != null) {
                if (currentVendedorIndex == vendedorIndex) {
                    // Si es el vendedor que corresponde a este archivo, lo seleccionamos
                	String[] vendedorData = vendedorInfo.split(";");
                    String tipoDocumento = vendedorData[0];
                    String numeroDocumento = vendedorData[1];
                    writer.write(tipoDocumento + ";" + numeroDocumento + "\n");
                    break; // Salir del bucle una vez que se haya seleccionado el vendedor
                }
                currentVendedorIndex++; // Incrementar el índice del vendedor actual
            }
            vendedoresReader.close();

            // Leer información de productos
            BufferedReader productosReader = new BufferedReader(new FileReader("info_productos.txt"));
            String productoInfo;
            while ((productoInfo = productosReader.readLine()) != null) {
                String[] productoData = productoInfo.split(";");
                String productoID = productoData[0];
                int cantidadVendida = random.nextInt(100);
                writer.write(productoID + ";" + cantidadVendida + "\n");
            }
            productosReader.close();

            writer.close();
        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo de ventas: " + filename);
            e.printStackTrace();
        }
    }

    // Método para generar el archivo de información de vendedores
    private static void generateSellersFile(String filename) {
        try {
            FileWriter writer = new FileWriter(filename);

            // Escribir información de vendedores pseudoaleatoria
            Random random = new Random();
            for (int i = 1; i <= 6; i++) { // Suponiendo que hay 6 vendedores
                String tipoDocumento = generateRandomTipoDocumento();
                String numeroDocumento = generateRandomNumeroDocumento();
                String nombresVendedor = generateRandomNames();
                String apellidosVendedor = generateRandomLastNames();
                writer.write(tipoDocumento + ";" + numeroDocumento + ";" + nombresVendedor + ";" + apellidosVendedor + "\n");
            }

            writer.close(); // Cerrar el escritor
        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo de vendedores: " + filename);
            e.printStackTrace();
        }
    }

    // Método para generar el archivo de información de productos
    private static void generateProductsFile(String filename) {
        try {
            FileWriter writer = new FileWriter(filename);

            // Escribir información de productos
            Random random = new Random();
            for (int i = 1; i <= 5; i++) { // Suponiendo que hay 5 productos
                String productoID = generateRandomProductID(); // Generar ID del producto
                String nombreProducto = generateRandomProductName(i); // Generar nombre del producto
                int precioPorUnidad = 1000 + random.nextInt(9000); // Generar precio del producto entre 1000 y 9999
                String formattedPrice = NumberFormat.getNumberInstance(Locale.US).format(precioPorUnidad); // Formatear con separadores de miles
                writer.write(productoID + ";" + nombreProducto + ";" + formattedPrice + "\n"); // Separar ID del producto, nombre del producto y precio por unidad con ; e imprimir nueva línea
            }

            writer.close(); // Cerrar el escritor
        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo de productos: " + filename);
            e.printStackTrace();
        }
    }

    // Métodos para generar datos aleatorios
    private static String generateRandomTipoDocumento() {
        String[] tiposDocumentos = {"CC", "TI", "CE","PAS"}; // Tipos de documento
        Random random = new Random();
        int index = random.nextInt(tiposDocumentos.length);
        return tiposDocumentos[index];
    }

    private static String generateRandomNumeroDocumento() {
        Random random = new Random();
        StringBuilder numeroDocumento = new StringBuilder();
        for (int i = 0; i < 10; i++) { // Documento de 10 dígitos
            numeroDocumento.append(random.nextInt(10));
        }
        return numeroDocumento.toString();
    }

    private static String generateRandomNames() {
        String[] nombres = {"Juan Manuel", "Ana María", "Luis Carlos", "Luisa Fernanda", "Pedro", "Luz Mery"}; //Ejemplos de nombres
        Random random = new Random();
        int index = random.nextInt(nombres.length);
        return nombres[index];
    }

    private static String generateRandomLastNames() {
        String[] apellidos = {"García Diaz", "Martínez Perez", "Rodríguez Rios", "Fernández Alba", "López Botello", "Gómez Barrera"}; //Ejemplos de apellidos
        Random random = new Random();
        int index = random.nextInt(apellidos.length);
        return apellidos[index];
    }

    private static String generateRandomProductID() {
        Random random = new Random();
        StringBuilder productID = new StringBuilder();
        for (int i = 0; i < 10; i++) { // ID de producto de 10 dígitos
            productID.append(random.nextInt(10)); // Dígito aleatorio entre 0 y 9
        }
        return productID.toString();
    }

    private static String generateRandomProductName(int id) {
        String nombresProductos = "Producto"+ id;
        return nombresProductos;
    }
}
