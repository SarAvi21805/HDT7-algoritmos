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
    public void testObtenerSiguienteSku() {
        String siguienteSku = Inventario.obtenerSiguienteSku(archivo);
        assertEquals("3", siguienteSku); // El siguiente SKU debería ser 3
    }
}