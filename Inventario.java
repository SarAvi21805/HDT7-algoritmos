import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Alejandra Avilés
 * Clase que maneja la carga, almacenamiento y edición de productos en el inventario.
 * Permite cargar productos desde un CSV, editar produtos existentes y guardar el inventario en un archivo.
 */
public class Inventario {

    /**
     * Carga el inventario desde un archivo e inserta en dos árboles binarios (Sku y Nombre)
     * @param archivo La ruta del archivo CSV
     * @param arbolSku El árbol binario que almacena productos por SKU
     * @param arbolNombre El árbol binario que almacena productos por nombre
     */
    public static void cargarInventario(String archivo, BinaryTree arbolSku, BinaryTree arbolNombre) {
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) { 
            String linea;
            boolean esPrimeraLinea = true; /* Variable para ignorar los encabezados de CSV */
            while ((linea = br.readLine()) != null) {
                if (esPrimeraLinea) {
                    esPrimeraLinea = false;
                    continue;
                }
                if(linea.trim().isEmpty()){
                    continue; /* Salta línea en caso esté vacía */
                }
                String[] partes = linea.split(",");
                if (partes.length < 4) {
                    System.out.println("Línea inválida: " + linea); /* Salto de líneas con formato incorrecto */
                    continue;
                }
                String sku = partes[0];
                String nombre = partes[1];
                String descripcion = partes[2];
                String[] tallasStr = partes[3].split("\\|");
                Map<String, Integer> tallas = new HashMap<>();
                for (String talla : tallasStr) {
                    String[] tallaPartes = talla.split(":");
                    tallas.put(tallaPartes[0], Integer.parseInt(tallaPartes[1]));
                }
                Producto producto = new Producto(sku, nombre, descripcion, tallas);
                arbolSku.insertar(sku, producto);
                arbolNombre.insertar(nombre, producto);
            }
        }   catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Edición de un producto del árbol de inventario
     * @param arbolSku El árbol binario que almacena productos por SKU
     * @param sku El SKU del producto a editar
     * @param nuevaDescripcion La nueva descripción de un producto
     * @param nuevasTallas Un mapa de nuevas tallas y cantidades
     * @param archivo La ruta del CSV que guardará el inventario actualizado
     */

    public void editarProducto(BinaryTree arbolSku, String sku, String nuevaDescripcion, String operacion, Map<String, Integer> cantidades, String archivo) {
        Producto producto = arbolSku.buscar(sku);
        if (producto != null) {
            if (nuevaDescripcion != null && !nuevaDescripcion.isEmpty()) {
                producto.setDescripcion(nuevaDescripcion);
            }
            
            if (cantidades != null && !cantidades.isEmpty()) {
                for (Map.Entry<String, Integer> entry : cantidades.entrySet()) {
                    String talla = entry.getKey();
                    int cantidad = entry.getValue();
                    
                    if (operacion.equals("añadir")) {
                        producto.aumentarCantidad(talla, cantidad);
                    } else if (operacion.equals("restar")) {
                        producto.restarCantidad(talla, cantidad);
                    }
                }
            }
            
            Inventario.guardarInventario(archivo, arbolSku);
        } else {
            System.out.println("El producto con el SKU " + sku + " no existe.");
        }
    }
    
    /**
     * Guarda el inventario en un archivo CSV
     * @param archivo Ruta del archivo CSV
     * @param arbolSku El árbol binario que almacena productos por SKU
     */
    public static void guardarInventario(String archivo, BinaryTree arbolSku) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
            bw.write("SKU,Nombre,Descripción,Tallas\n");
            arbolSku.listarParaGuardar(bw);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Obtiene el último SKU utilizado en el CSV para añadir productos nuevos
     * @param archivo La ruta del CSV que almacena los productos
     * @return El último SKU utilizado
     */
    public static String obtenerUltimoSku(String archivo){
        String ultimoSku = "0";
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            boolean esPrimeraLinea = true;
            while ((linea = br.readLine()) != null) {
                if (esPrimeraLinea) {
                    esPrimeraLinea = false;
                    continue;
                }
                String[] partes = linea.split(",");
                if (partes.length > 0) {
                    String skuActual = partes[0];
                    if (Integer.parseInt(skuActual) > Integer.parseInt(ultimoSku)){
                        ultimoSku = skuActual; /* Actualiza el SKU al último elemento encontrado */
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ultimoSku;
    }
}