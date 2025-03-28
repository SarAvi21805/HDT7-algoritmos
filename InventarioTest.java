import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class InventarioTest {
    private BinaryTree arbolSku;
    private BinaryTree arbolNombre;
    private String archivo;

    @BeforeEach
    public void setUp() {
        arbolSku = new BinaryTree();
        arbolNombre = new BinaryTree();
        archivo = "inventario_test.csv";

        // Crear un archivo de prueba con datos iniciales
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
            bw.write("SKU,Nombre,Descripción,Tallas\n");
            bw.write("1,Camiseta,Camiseta de algodón,S:10|M:5\n");
            bw.write("2,Pantalón,Pantalón de mezclilla,S:8|M:4\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCargarInventario() {
        Inventario.cargarInventario(archivo, arbolSku, arbolNombre);
        
        // Verificar que los productos se hayan cargado correctamente
        assertNotNull(arbolSku.buscar("1"));
        assertNotNull(arbolNombre.buscar("Camiseta"));
        assertNotNull(arbolSku.buscar("2"));
        assertNotNull(arbolNombre.buscar("Pantalón"));
    }

    @Test
public void testGuardarInventario() {
    Inventario.cargarInventario(archivo, arbolSku, arbolNombre);
    
    // Editar un producto para asegurarnos de que se guarda correctamente
    Map<String, Integer> nuevasTallas = new HashMap<>();
    nuevasTallas.put("S", 12);
    nuevasTallas.put("M", 5);
    
    Inventario inventario = new Inventario();
    inventario.editarProducto(arbolSku, "1", "Camiseta de algodón mejorada", "añadir", nuevasTallas, archivo);
    
    // Guardar el inventario
    inventario.guardarInventario(archivo, arbolSku);
    
    // Leer el archivo y verificar que los cambios se hayan guardado
    try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
        String linea;
        boolean encontrado = false;
        while ((linea = br.readLine()) != null) {
            if (linea.startsWith("1,")) {
                String[] partes = linea.split(",");
                assertEquals("Camiseta de algodón mejorada", partes[2]);
                String tallasEsperadas = "S:22|M:10";
                String tallasGuardadas = partes[3].replace(" ", ""); // Eliminar espacios si los hay
                assertEquals(tallasEsperadas, tallasGuardadas);
                encontrado = true;
                break;
            }
        }
        assertTrue(encontrado, "No se encontró el producto editado en el archivo.");
    } catch (IOException e) {
        e.printStackTrace();
        fail("Se produjo una excepción al leer el archivo: " + e.getMessage());
    }
}

@Test
public void testEditarProducto() {
    Inventario.cargarInventario(archivo, arbolSku, arbolNombre);
    
    Map<String, Integer> nuevasTallas = new HashMap<>();
    nuevasTallas.put("S", 12);
    nuevasTallas.put("L", 5);
    
    Inventario inventario = new Inventario();
    inventario.editarProducto(arbolSku, "1", "Camiseta de algodón mejorada", "añadir", nuevasTallas, archivo);
    
    Producto productoEditado = arbolSku.buscar("1");
    assertNotNull(productoEditado, "El producto editado no se encontró en el árbol.");
    assertEquals("Camiseta de algodón mejorada", productoEditado.getDescripcion(), "La descripción del producto no se actualizó correctamente.");
    assertEquals(22, productoEditado.getTallas().get("S"), "La cantidad de tallas 'S' no se actualizó correctamente.");
    assertEquals(5, productoEditado.getTallas().get("L"), "La cantidad de tallas 'L' no se actualizó correctamente.");
}

@Test
public void testObtenerUltimoSku() {
    String ultimoSku = Inventario.obtenerUltimoSku(archivo);
    assertEquals("2", ultimoSku); // El último SKU debería ser 2
}

@Test
public void testCargarInventarioConArchivoVacio() {
    // Crear un archivo vacío para la prueba
    String archivoVacio = "inventario_vacio.csv";
    try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivoVacio))) {
        // No se escribe nada, el archivo queda vacío
    } catch (IOException e) {
        e.printStackTrace();
    }

    // Intentar cargar el inventario desde el archivo vacío
    Inventario.cargarInventario(archivoVacio, arbolSku, arbolNombre);
    
    // Verificar que no se haya cargado ningún producto
    assertNull(arbolSku.buscar("1"), "Se cargó un producto desde un archivo vacío.");
    assertNull(arbolNombre.buscar("Camiseta"), "Se cargó un producto desde un archivo vacío.");
}

@Test
public void testCargarInventarioConFormatoIncorrecto() {
    // Crear un archivo con formato incorrecto
    String archivoIncorrecto = "inventario_incorrecto.csv";
    try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivoIncorrecto))) {
        bw.write("SKU,Nombre,Descripción,Tallas\n");
        bw.write("1,Camiseta\n"); // Falta la parte de tallas
    } catch (IOException e) {
        e.printStackTrace();
    }

    // Intentar cargar el inventario desde el archivo con formato incorrecto
    Inventario.cargarInventario(archivoIncorrecto, arbolSku, arbolNombre);
    
    // Verificar que no se haya cargado ningún producto
    assertNull(arbolSku.buscar("1"), "Se cargó un producto desde un archivo con formato incorrecto.");
}

@Test
public void testEditarProductoNoExistente() {
    Inventario.cargarInventario(archivo, arbolSku, arbolNombre);
    
    Map<String, Integer> nuevasTallas = new HashMap<>();
    nuevasTallas.put("S", 12);
    
    Inventario inventario = new Inventario();
    // Intentar editar un producto que no existe
    inventario.editarProducto(arbolSku, "999", "Producto no existente", "añadir", nuevasTallas, archivo);
    
    // Verificar que el producto no se haya editado
    Producto productoNoExistente = arbolSku.buscar("999");
    assertNull(productoNoExistente, "Se encontró un producto que no debería existir.");
}
}